package org.example.ppcauction.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "auction_results", indexes = {
        @Index(name = "ix_auction_results_batch", columnList = "auction_id"),
        @Index(name = "ix_auction_results_time", columnList = "simulated_at")
})
public class AuctionResult {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "auction_id", nullable = false) private UUID auctionId;
    @Column(nullable = false, length = 24) private String simulationType;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "advertiser_id", nullable = false)
    private Advertiser advertiser;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "campaign_id")
    private Campaign campaign;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal bid;
    @Column(nullable = false) private Integer qualityScore;
    @Column(nullable = false, precision = 16, scale = 2) private BigDecimal adRank;
    @Column(nullable = false) private Integer position;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal cpc;
    @Column(nullable = false) private Integer impressions;
    @Column(nullable = false, precision = 5, scale = 2) private BigDecimal ctr;
    @Column(nullable = false) private Integer clicks;
    @Column(nullable = false, precision = 18, scale = 2) private BigDecimal cost;
    @Column(nullable = false, precision = 18, scale = 2) private BigDecimal remainingBudget;
    @Column(name = "simulated_at", nullable = false) private LocalDateTime simulatedAt;

    protected AuctionResult() { }
    public AuctionResult(UUID auctionId, String simulationType, Keyword keyword, Advertiser advertiser, Campaign campaign,
                         BigDecimal bid, Integer qualityScore, BigDecimal adRank, Integer position, BigDecimal cpc,
                         Integer impressions, BigDecimal ctr, Integer clicks, BigDecimal cost,
                         BigDecimal remainingBudget, LocalDateTime simulatedAt) {
        this.auctionId = auctionId; this.simulationType = simulationType; this.keyword = keyword;
        this.advertiser = advertiser; this.campaign = campaign; this.bid = bid; this.qualityScore = qualityScore;
        this.adRank = adRank; this.position = position; this.cpc = cpc; this.impressions = impressions;
        this.ctr = ctr; this.clicks = clicks; this.cost = cost; this.remainingBudget = remainingBudget;
        this.simulatedAt = simulatedAt;
    }
    public Long getId() { return id; }
    public UUID getAuctionId() { return auctionId; }
    public String getSimulationType() { return simulationType; }
    public Keyword getKeyword() { return keyword; }
    public Advertiser getAdvertiser() { return advertiser; }
    public Campaign getCampaign() { return campaign; }
    public BigDecimal getBid() { return bid; }
    public Integer getQualityScore() { return qualityScore; }
    public BigDecimal getAdRank() { return adRank; }
    public Integer getPosition() { return position; }
    public BigDecimal getCpc() { return cpc; }
    public Integer getImpressions() { return impressions; }
    public BigDecimal getCtr() { return ctr; }
    public Integer getClicks() { return clicks; }
    public BigDecimal getCost() { return cost; }
    public BigDecimal getRemainingBudget() { return remainingBudget; }
    public LocalDateTime getSimulatedAt() { return simulatedAt; }
}
