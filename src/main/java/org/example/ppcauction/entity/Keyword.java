package org.example.ppcauction.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "keywords")
public class Keyword {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 180) private String phrase;
    @Column(nullable = false) private Integer searchVolume = 0;
    @Column(nullable = false, length = 20) private String competition = "MEDIUM";
    @Column(nullable = false) private Integer qualityScore = 5;
    @Column(nullable = false, length = 20) private String status = "ACTIVE";

    protected Keyword() { }
    public Keyword(String phrase, Integer searchVolume, String competition, Integer qualityScore, String status) {
        this.phrase = phrase; this.searchVolume = searchVolume; this.competition = competition;
        this.qualityScore = qualityScore; this.status = status;
    }
    public Long getId() { return id; }
    public String getPhrase() { return phrase; }
    public void setPhrase(String phrase) { this.phrase = phrase; }
    public Integer getSearchVolume() { return searchVolume; }
    public void setSearchVolume(Integer searchVolume) { this.searchVolume = searchVolume; }
    public String getCompetition() { return competition; }
    public void setCompetition(String competition) { this.competition = competition; }
    public Integer getQualityScore() { return qualityScore; }
    public void setQualityScore(Integer qualityScore) { this.qualityScore = qualityScore; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
