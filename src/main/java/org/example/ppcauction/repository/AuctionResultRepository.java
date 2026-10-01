package org.example.ppcauction.repository;

import org.example.ppcauction.entity.AuctionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AuctionResultRepository extends JpaRepository<AuctionResult, Long> {
    @EntityGraph(attributePaths = {"keyword", "advertiser", "campaign"})
    List<AuctionResult> findByAuctionIdOrderByPosition(UUID auctionId);
    @EntityGraph(attributePaths = {"keyword", "advertiser", "campaign"})
    List<AuctionResult> findTop250ByOrderBySimulatedAtDesc();
    @EntityGraph(attributePaths = {"keyword", "advertiser", "campaign"})
    List<AuctionResult> findByCampaignIdAndSimulationTypeOrderBySimulatedAtAsc(Long campaignId, String simulationType);
    boolean existsBySimulationType(String simulationType);
    boolean existsByAdvertiserId(Long advertiserId);
    boolean existsByCampaignId(Long campaignId);
    boolean existsByKeywordId(Long keywordId);

    @Query("select count(a) from AuctionResult a") long countResults();
    @Query("select coalesce(sum(a.impressions), 0) from AuctionResult a") Long sumImpressions();
    @Query("select coalesce(sum(a.clicks), 0) from AuctionResult a") Long sumClicks();
    @Query("select coalesce(sum(a.cost), 0) from AuctionResult a") BigDecimal sumCost();
    @Query("select coalesce(avg(a.cpc), 0) from AuctionResult a") BigDecimal averageCpc();
    @Query("select a from AuctionResult a where (:keyword is null or lower(a.keyword.phrase) like lower(concat('%', :keyword, '%'))) and (:advertiserId is null or a.advertiser.id = :advertiserId) and (:fromDate is null or a.simulatedAt >= :fromDate) and (:toDate is null or a.simulatedAt < :toDate) order by a.simulatedAt desc, a.auctionId, a.position")
    @EntityGraph(attributePaths = {"keyword", "advertiser", "campaign"})
    List<AuctionResult> searchHistory(@Param("keyword") String keyword, @Param("advertiserId") Long advertiserId,
                                     @Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);
}
