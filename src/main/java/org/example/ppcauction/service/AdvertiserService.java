package org.example.ppcauction.service;

import org.example.ppcauction.dto.AdvertiserForm;
import org.example.ppcauction.entity.Advertiser;
import org.example.ppcauction.exception.RelatedDataException;
import org.example.ppcauction.exception.ResourceNotFoundException;
import org.example.ppcauction.repository.AdvertiserRepository;
import org.example.ppcauction.repository.AuctionResultRepository;
import org.example.ppcauction.repository.CampaignRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class AdvertiserService {
    private final AdvertiserRepository advertisers;
    private final AuctionResultRepository results;
    private final CampaignRepository campaigns;
    public AdvertiserService(AdvertiserRepository advertisers, AuctionResultRepository results, CampaignRepository campaigns) { this.advertisers=advertisers; this.results=results; this.campaigns=campaigns; }
    @Transactional(readOnly=true) public List<Advertiser> search(String q) {
        return q == null || q.isBlank() ? advertisers.findAll(Sort.by("name").ascending()) : advertisers.findByNameContainingIgnoreCaseOrCompanyContainingIgnoreCaseOrderByName(q.trim(), q.trim());
    }
    @Transactional(readOnly=true) public Advertiser get(Long id) { return advertisers.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhà quảng cáo")); }
    public Advertiser save(Long id, AdvertiserForm form) {
        Advertiser a = id == null ? new Advertiser(form.getName().trim(), form.getCompany(), form.getEmail(), form.getPhone(), form.getBudget(), form.getStatus()) : get(id);
        a.setName(form.getName().trim()); a.setCompany(form.getCompany()); a.setEmail(form.getEmail()); a.setPhone(form.getPhone()); a.setBudget(form.getBudget()); a.setStatus(form.getStatus());
        return advertisers.save(a);
    }
    public void delete(Long id) {
        Advertiser a=get(id);
        if (results.existsByAdvertiserId(id)) throw new RelatedDataException("Nhà quảng cáo đã có lịch sử đấu giá nên không thể xóa.");
        if (campaigns.existsByAdvertiserId(id)) throw new RelatedDataException("Nhà quảng cáo đang được chiến dịch tham chiếu nên không thể xóa.");
        advertisers.delete(a);
    }
}
