package org.example.ppcauction.dto;

import java.math.BigDecimal;

public record AuctionBid(String advertiser, BigDecimal bid, int qualityScore, int impressions,
                         BigDecimal ctr, BigDecimal budget) { }
