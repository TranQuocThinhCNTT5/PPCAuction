package org.example.ppcauction.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "campaign_keywords", uniqueConstraints = @UniqueConstraint(columnNames = {"campaign_id", "keyword_id"}))
public class CampaignKeyword {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal bid = BigDecimal.ONE;
    @Column(nullable = false) private Integer qualityScore = 5;
    @Column(nullable = false, precision = 5, scale = 2) private BigDecimal expectedCtr = new BigDecimal("3.00");

    protected CampaignKeyword() { }
    public CampaignKeyword(Keyword keyword, BigDecimal bid, Integer qualityScore, BigDecimal expectedCtr) {
        this.keyword = keyword; this.bid = bid; this.qualityScore = qualityScore; this.expectedCtr = expectedCtr;
    }
    public Long getId() { return id; }
    public Campaign getCampaign() { return campaign; }
    public void setCampaign(Campaign campaign) { this.campaign = campaign; }
    public Keyword getKeyword() { return keyword; }
    public BigDecimal getBid() { return bid; }
    public void setBid(BigDecimal bid) { this.bid = bid; }
    public Integer getQualityScore() { return qualityScore; }
    public void setQualityScore(Integer qualityScore) { this.qualityScore = qualityScore; }
    public BigDecimal getExpectedCtr() { return expectedCtr; }
    public void setExpectedCtr(BigDecimal expectedCtr) { this.expectedCtr = expectedCtr; }
}
