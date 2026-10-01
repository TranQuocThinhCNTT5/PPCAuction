package org.example.ppcauction.repository;

import org.example.ppcauction.entity.CampaignKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CampaignKeywordRepository extends JpaRepository<CampaignKeyword, Long> {
    List<CampaignKeyword> findByCampaignId(Long campaignId);
    boolean existsByKeywordId(Long keywordId);
}
