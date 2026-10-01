package org.example.ppcauction.service;

import org.example.ppcauction.dto.CampaignForm;
import org.example.ppcauction.entity.*;
import org.example.ppcauction.exception.RelatedDataException;
import org.example.ppcauction.exception.ResourceNotFoundException;
import org.example.ppcauction.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service @Transactional
public class CampaignService {
    private final CampaignRepository campaigns; private final AdvertiserRepository advertisers; private final KeywordRepository keywords; private final AuctionResultRepository results;
    public CampaignService(CampaignRepository campaigns, AdvertiserRepository advertisers, KeywordRepository keywords, AuctionResultRepository results) { this.campaigns=campaigns; this.advertisers=advertisers; this.keywords=keywords; this.results=results; }
    @Transactional(readOnly=true) public List<Campaign> search(String q,String status) {
        if((q==null||q.isBlank())&&(status==null||status.isBlank())) return campaigns.findAll();
        return campaigns.findByNameContainingIgnoreCaseAndStatusContainingIgnoreCaseOrderByName(q==null?"":q.trim(),status==null?"":status.trim());
    }
    @Transactional(readOnly=true) public Campaign get(Long id) { return campaigns.findById(id).orElseThrow(()->new ResourceNotFoundException("Không tìm thấy chiến dịch")); }
    @Transactional(readOnly=true) public List<Campaign> activeCampaigns() { return campaigns.findByStatusIgnoreCaseOrderByName("ACTIVE"); }
    public Campaign save(Long id,CampaignForm f) {
        if(f.getStartDate()!=null&&f.getEndDate()!=null&&f.getEndDate().isBefore(f.getStartDate())) throw new IllegalArgumentException("Ngày kết thúc phải bằng hoặc sau ngày bắt đầu.");
        Advertiser advertiser=advertisers.findById(f.getAdvertiserId()).orElseThrow(()->new ResourceNotFoundException("Không tìm thấy nhà quảng cáo"));
        Campaign c=id==null?new Campaign(f.getName().trim(),advertiser,f.getBudget(),f.getDailyBudget(),f.getStartDate(),f.getEndDate(),f.getStatus(),f.getDescription()):get(id);
        c.setName(f.getName().trim()); c.setAdvertiser(advertiser); c.setBudget(f.getBudget()); c.setDailyBudget(f.getDailyBudget()); c.setStartDate(f.getStartDate()); c.setEndDate(f.getEndDate()); c.setStatus(f.getStatus()); c.setDescription(f.getDescription());
        c.getKeywordSettings().clear();
        List<Long> ids=f.getKeywordIds()==null?List.of():f.getKeywordIds().stream().distinct().toList();
        for(Keyword k:keywords.findAllById(ids)) c.addKeywordSetting(new CampaignKeyword(k,new BigDecimal("1.00"),k.getQualityScore(),new BigDecimal("3.00")));
        return campaigns.save(c);
    }
    public void delete(Long id) { Campaign c=get(id); if(results.existsByCampaignId(id)) throw new RelatedDataException("Chiến dịch đã có lịch sử mô phỏng nên không thể xóa."); campaigns.delete(c); }
}
