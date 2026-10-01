package org.example.ppcauction.dto;

import java.math.BigDecimal;

public record AuctionOutcome(String advertiser, BigDecimal bid, int qualityScore, BigDecimal adRank,
                             int position, BigDecimal cpc, int impressions, BigDecimal ctr,
                             int clicks, BigDecimal cost, BigDecimal remainingBudget) { }
