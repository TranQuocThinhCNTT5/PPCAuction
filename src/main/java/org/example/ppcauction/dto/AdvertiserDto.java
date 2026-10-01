package org.example.ppcauction.dto;
import java.math.BigDecimal;
public record AdvertiserDto(Long id,String name,String company,String email,String phone,BigDecimal budget,String status) { }
