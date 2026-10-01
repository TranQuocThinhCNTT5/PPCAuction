package org.example.ppcauction.dto;

import jakarta.validation.constraints.*;

public class KeywordForm {
    @NotBlank(message = "Vui lòng nhập từ khóa") @Size(max=180) private String phrase;
    @NotNull(message="Vui lòng nhập lượt tìm kiếm") @Min(value=0, message="Lượt tìm kiếm phải từ 0 trở lên") private Integer searchVolume=0;
    @NotBlank(message="Vui lòng chọn mức cạnh tranh") private String competition="MEDIUM";
    @NotNull(message="Vui lòng nhập Quality Score") @Min(value=1,message="Quality Score tối thiểu là 1") @Max(value=10, message="Quality Score tối đa là 10") private Integer qualityScore=5;
    @NotBlank(message="Vui lòng chọn trạng thái") private String status="ACTIVE";
    public String getPhrase(){return phrase;} public void setPhrase(String v){phrase=v;}
    public Integer getSearchVolume(){return searchVolume;} public void setSearchVolume(Integer v){searchVolume=v;}
    public String getCompetition(){return competition;} public void setCompetition(String v){competition=v;}
    public Integer getQualityScore(){return qualityScore;} public void setQualityScore(Integer v){qualityScore=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
}
