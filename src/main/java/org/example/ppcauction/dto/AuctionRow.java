package org.example.ppcauction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AuctionRow(Long id, UUID auctionId, String simulationType, String keyword, String advertiser,
                        String campaign, BigDecimal bid, Integer qualityScore, BigDecimal adRank, Integer position,
                        BigDecimal cpc, Integer impressions, BigDecimal ctr, Integer clicks, BigDecimal cost,
                        BigDecimal remainingBudget, LocalDateTime simulatedAt) { }
