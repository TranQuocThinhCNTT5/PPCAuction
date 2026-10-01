package org.example.ppcauction.dto;

import java.math.BigDecimal;
import java.util.Map;

public record DashboardData(long advertisers, long keywords, long campaigns, long auctions,
                           long impressions, long clicks, BigDecimal cost, BigDecimal averageCpc,
                           Map<String, ChartData> charts) { }
