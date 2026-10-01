package org.example.ppcauction.service;

import org.example.ppcauction.dto.KeywordForm;
import org.example.ppcauction.entity.Keyword;
import org.example.ppcauction.exception.RelatedDataException;
import org.example.ppcauction.exception.ResourceNotFoundException;
import org.example.ppcauction.repository.AuctionResultRepository;
import org.example.ppcauction.repository.CampaignKeywordRepository;
import org.example.ppcauction.repository.KeywordRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @Transactional
public class KeywordService {
    private final KeywordRepository keywords; private final CampaignKeywordRepository campaignKeywords; private final AuctionResultRepository results;
    public KeywordService(KeywordRepository keywords, CampaignKeywordRepository campaignKeywords, AuctionResultRepository results) { this.keywords=keywords; this.campaignKeywords=campaignKeywords; this.results=results; }
    @Transactional(readOnly=true) public List<Keyword> search(String q, String status) {
        if ((q==null||q.isBlank())&&(status==null||status.isBlank())) return keywords.findAll(Sort.by("phrase").ascending());
        return keywords.findByPhraseContainingIgnoreCaseAndStatusContainingIgnoreCaseOrderByPhrase(q==null?"":q.trim(), status==null?"":status.trim());
    }
    @Transactional(readOnly=true) public Keyword get(Long id) { return keywords.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy từ khóa")); }
    public Keyword save(Long id, KeywordForm form) {
        Keyword k=id==null?new Keyword(form.getPhrase().trim(),form.getSearchVolume(),form.getCompetition(),form.getQualityScore(),form.getStatus()):get(id);
        k.setPhrase(form.getPhrase().trim()); k.setSearchVolume(form.getSearchVolume()); k.setCompetition(form.getCompetition()); k.setQualityScore(form.getQualityScore()); k.setStatus(form.getStatus());
        return keywords.save(k);
    }
    public void delete(Long id) {
        Keyword k=get(id);
        if(campaignKeywords.existsByKeywordId(id)||results.existsByKeywordId(id)) throw new RelatedDataException("Từ khóa đang được chiến dịch hoặc lịch sử đấu giá sử dụng nên không thể xóa.");
        keywords.delete(k);
    }
}
