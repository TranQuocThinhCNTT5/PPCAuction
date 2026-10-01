package org.example.ppcauction.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class AuctionEntryForm {
    @NotNull(message="Hãy chọn nhà quảng cáo") private Long advertiserId;
    private Long campaignId;
    @NotNull(message="Bid phải lớn hơn 0") @DecimalMin(value="0.01", message="Bid phải lớn hơn 0") private BigDecimal bid;
    @NotNull(message="Vui lòng nhập Quality Score") @Min(value=1,message="Quality Score tối thiểu là 1") @Max(value=10,message="Quality Score tối đa là 10") private Integer qualityScore;
    @NotNull(message="Vui lòng nhập số lượt hiển thị") @Min(value=0,message="Impressions phải từ 0 trở lên") private Integer impressions=1000;
    @NotNull(message="Vui lòng nhập CTR") @DecimalMin(value="0.00",message="CTR phải từ 0 đến 100") @DecimalMax(value="100.00",message="CTR phải từ 0 đến 100") private BigDecimal ctr=new BigDecimal("3.00");
    public Long getAdvertiserId(){return advertiserId;} public void setAdvertiserId(Long v){advertiserId=v;}
    public Long getCampaignId(){return campaignId;} public void setCampaignId(Long v){campaignId=v;}
    public BigDecimal getBid(){return bid;} public void setBid(BigDecimal v){bid=v;}
    public Integer getQualityScore(){return qualityScore;} public void setQualityScore(Integer v){qualityScore=v;}
    public Integer getImpressions(){return impressions;} public void setImpressions(Integer v){impressions=v;}
    public BigDecimal getCtr(){return ctr;} public void setCtr(BigDecimal v){ctr=v;}
}
