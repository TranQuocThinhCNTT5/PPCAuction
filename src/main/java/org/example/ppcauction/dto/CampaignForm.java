package org.example.ppcauction.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CampaignForm {
    @NotBlank(message="Vui lòng nhập tên chiến dịch") @Size(max=140) private String name;
    @NotNull(message="Vui lòng chọn nhà quảng cáo") private Long advertiserId;
    @NotNull(message="Vui lòng nhập ngân sách") @DecimalMin(value="0.00", message="Ngân sách phải từ 0 trở lên") private BigDecimal budget=BigDecimal.ZERO;
    @NotNull(message="Vui lòng nhập ngân sách ngày") @DecimalMin(value="0.00", message="Ngân sách ngày phải từ 0 trở lên") private BigDecimal dailyBudget=BigDecimal.ZERO;
    private LocalDate startDate=LocalDate.now();
    private LocalDate endDate=LocalDate.now().plusDays(30);
    @NotBlank(message="Vui lòng chọn trạng thái") private String status="ACTIVE";
    @Size(max=1000) private String description;
    private List<Long> keywordIds=new ArrayList<>();
    public String getName(){return name;} public void setName(String v){name=v;}
    public Long getAdvertiserId(){return advertiserId;} public void setAdvertiserId(Long v){advertiserId=v;}
    public BigDecimal getBudget(){return budget;} public void setBudget(BigDecimal v){budget=v;}
    public BigDecimal getDailyBudget(){return dailyBudget;} public void setDailyBudget(BigDecimal v){dailyBudget=v;}
    public LocalDate getStartDate(){return startDate;} public void setStartDate(LocalDate v){startDate=v;}
    public LocalDate getEndDate(){return endDate;} public void setEndDate(LocalDate v){endDate=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public List<Long> getKeywordIds(){return keywordIds;} public void setKeywordIds(List<Long> v){keywordIds=v;}
}
