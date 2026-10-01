package org.example.ppcauction.dto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
public record CampaignDto(Long id,String name,Long advertiserId,String advertiserName,BigDecimal budget,BigDecimal dailyBudget,
                          LocalDate startDate,LocalDate endDate,String status,String description,List<String> keywords) { }
