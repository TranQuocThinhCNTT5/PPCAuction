package org.example.ppcauction.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class AuctionRequest {
    @NotNull(message="Vui lòng chọn từ khóa") private Long keywordId;
    @NotEmpty(message="Thêm ít nhất một nhà quảng cáo tham gia") @Valid private List<AuctionEntryForm> entries=new ArrayList<>();
    public Long getKeywordId(){return keywordId;} public void setKeywordId(Long v){keywordId=v;}
    public List<AuctionEntryForm> getEntries(){return entries;} public void setEntries(List<AuctionEntryForm> v){entries=v;}
}
