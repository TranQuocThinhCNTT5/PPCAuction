package org.example.ppcauction.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class AdvertiserForm {
    @NotBlank(message = "Vui lòng nhập tên nhà quảng cáo") @Size(max = 120) private String name;
    @Size(max = 160) private String company;
    @Email(message = "Email chưa đúng định dạng") @Size(max = 180) private String email;
    @Size(max = 40) private String phone;
    @NotNull(message = "Vui lòng nhập ngân sách") @DecimalMin(value = "0.00", message = "Ngân sách phải từ 0 trở lên") private BigDecimal budget = BigDecimal.ZERO;
    @NotBlank(message="Vui lòng chọn trạng thái") private String status = "ACTIVE";
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getCompany(){return company;} public void setCompany(String v){company=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public BigDecimal getBudget(){return budget;} public void setBudget(BigDecimal v){budget=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
}
