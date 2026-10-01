package org.example.ppcauction.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailySimulationRow(LocalDate date, Integer impressions, Integer clicks, BigDecimal cpc,
                                 BigDecimal cost, BigDecimal remainingBudget) { }
