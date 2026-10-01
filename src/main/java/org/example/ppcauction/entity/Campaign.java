package org.example.ppcauction.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "campaigns")
public class Campaign {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 140) private String name;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "advertiser_id", nullable = false)
    private Advertiser advertiser;
    @Column(nullable = false, precision = 18, scale = 2) private BigDecimal budget = BigDecimal.ZERO;
    @Column(nullable = false, precision = 18, scale = 2) private BigDecimal dailyBudget = BigDecimal.ZERO;
    private LocalDate startDate;
    private LocalDate endDate;
    @Column(nullable = false, length = 20) private String status = "ACTIVE";
    @Column(length = 1000) private String description;
    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampaignKeyword> keywordSettings = new ArrayList<>();

    protected Campaign() { }
    public Campaign(String name, Advertiser advertiser, BigDecimal budget, BigDecimal dailyBudget,
                    LocalDate startDate, LocalDate endDate, String status, String description) {
        this.name = name; this.advertiser = advertiser; this.budget = budget; this.dailyBudget = dailyBudget;
        this.startDate = startDate; this.endDate = endDate; this.status = status; this.description = description;
    }
    public void addKeywordSetting(CampaignKeyword setting) { keywordSettings.add(setting); setting.setCampaign(this); }
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Advertiser getAdvertiser() { return advertiser; }
    public void setAdvertiser(Advertiser advertiser) { this.advertiser = advertiser; }
    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }
    public BigDecimal getDailyBudget() { return dailyBudget; }
    public void setDailyBudget(BigDecimal dailyBudget) { this.dailyBudget = dailyBudget; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<CampaignKeyword> getKeywordSettings() { return keywordSettings; }
}
