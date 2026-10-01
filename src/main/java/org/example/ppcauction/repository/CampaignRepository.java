package org.example.ppcauction.repository;

import org.example.ppcauction.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.List;
import java.util.Optional;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    @Override @EntityGraph(attributePaths = {"advertiser", "keywordSettings", "keywordSettings.keyword"})
    List<Campaign> findAll();
    @Override @EntityGraph(attributePaths = {"advertiser", "keywordSettings", "keywordSettings.keyword"})
    Optional<Campaign> findById(Long id);
    boolean existsByAdvertiserId(Long advertiserId);
    @Override @EntityGraph(attributePaths = {"advertiser", "keywordSettings", "keywordSettings.keyword"})
    List<Campaign> findAll(org.springframework.data.domain.Sort sort);
    @EntityGraph(attributePaths = {"advertiser", "keywordSettings", "keywordSettings.keyword"})
    List<Campaign> findByNameContainingIgnoreCaseAndStatusContainingIgnoreCaseOrderByName(String name, String status);
    @EntityGraph(attributePaths = {"advertiser", "keywordSettings", "keywordSettings.keyword"})
    List<Campaign> findByStatusIgnoreCaseOrderByName(String status);
}
