package org.example.ppcauction.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "advertisers")
public class Advertiser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120) private String name;
    @Column(length = 160) private String company;
    @Column(length = 180) private String email;
    @Column(length = 40) private String phone;
    @Column(nullable = false, precision = 18, scale = 2) private BigDecimal budget = BigDecimal.ZERO;
    @Column(nullable = false, length = 20) private String status = "ACTIVE";

    protected Advertiser() { }
    public Advertiser(String name, String company, String email, String phone, BigDecimal budget, String status) {
        this.name = name; this.company = company; this.email = email; this.phone = phone;
        this.budget = budget; this.status = status;
    }
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
