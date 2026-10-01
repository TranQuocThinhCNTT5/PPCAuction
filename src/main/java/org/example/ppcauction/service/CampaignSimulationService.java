package org.example.ppcauction.service;

import org.example.ppcauction.dto.DailySimulationRow;
import org.example.ppcauction.dto.AuctionBid;
import org.example.ppcauction.dto.AuctionOutcome;
import org.example.ppcauction.entity.*;
import org.example.ppcauction.exception.ResourceNotFoundException;
import org.example.ppcauction.repository.AuctionResultRepository;
import org.example.ppcauction.repository.CampaignRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.*;
import java.time.*;
import java.util.*;

@Service
public class CampaignSimulationService {
    private final CampaignRepository campaigns; private final AuctionResultRepository results;
    public CampaignSimulationService(CampaignRepository campaigns,AuctionResultRepository results){this.campaigns=campaigns;this.results=results;}

    @Transactional
    public List<DailySimulationRow> simulate(Long campaignId,int days) {
        if(days<1||days>30) throw new IllegalArgumentException("Số ngày mô phỏng phải từ 1 đến 30.");
        Campaign campaign=campaigns.findById(campaignId).orElseThrow(()->new ResourceNotFoundException("Không tìm thấy chiến dịch"));
        if(campaign.getKeywordSettings().isEmpty()) throw new IllegalArgumentException("Hãy thêm ít nhất một từ khóa vào chiến dịch trước khi mô phỏng.");
        BigDecimal balance=campaign.getBudget(); List<DailySimulationRow> rows=new ArrayList<>(); LocalDate first=LocalDate.now();
        List<Campaign> market=campaigns.findAll();
        for(int day=0;day<days;day++) {
            LocalDate date=first.plusDays(day); int impressions=0,clicks=0; BigDecimal cost=BigDecimal.ZERO;
            BigDecimal dailyBalance=campaign.getDailyBudget().min(balance);
            for(CampaignKeyword setting:campaign.getKeywordSettings()) {
                if(dailyBalance.signum()<=0) break;
                int views=setting.getKeyword().getSearchVolume()==0?0:(int)Math.ceil(setting.getKeyword().getSearchVolume()/30.0);
                List<CampaignKeyword> offerings=market.stream()
                        .filter(c->c.getId().equals(campaign.getId()) || ("ACTIVE".equalsIgnoreCase(c.getStatus())
                                && (c.getStartDate()==null||!date.isBefore(c.getStartDate()))
                                && (c.getEndDate()==null||!date.isAfter(c.getEndDate()))))
                        .flatMap(c->c.getKeywordSettings().stream())
                        .filter(candidate->candidate.getKeyword().getId().equals(setting.getKeyword().getId()))
                        .toList();
                List<AuctionBid> bids=offerings.stream().map(candidate->new AuctionBid("campaign-"+candidate.getCampaign().getId(),
                        candidate.getBid(),candidate.getQualityScore(),views,candidate.getExpectedCtr(),candidate.getCampaign().getBudget())).toList();
                List<AuctionOutcome> ranked=AuctionService.calculate(bids);
                AuctionOutcome outcome=ranked.stream().filter(candidate->candidate.advertiser().equals("campaign-"+campaign.getId())).findFirst().orElseThrow();
                int allowedClicks=outcome.clicks();
                if(outcome.cpc().signum()>0 && outcome.cpc().multiply(BigDecimal.valueOf(allowedClicks)).compareTo(dailyBalance)>0) {
                    allowedClicks=dailyBalance.divide(outcome.cpc(),0,RoundingMode.FLOOR).intValue();
                }
                BigDecimal rowCost=outcome.cpc().multiply(BigDecimal.valueOf(allowedClicks)).setScale(2,RoundingMode.HALF_UP);
                BigDecimal actualCtr=views==0?BigDecimal.ZERO:BigDecimal.valueOf(allowedClicks).multiply(new BigDecimal("100"))
                        .divide(BigDecimal.valueOf(views),2,RoundingMode.HALF_UP);
                BigDecimal after=dailyBalance.subtract(rowCost).max(BigDecimal.ZERO).setScale(2,RoundingMode.HALF_UP);
                results.save(new AuctionResult(UUID.randomUUID(),"CAMPAIGN",setting.getKeyword(),campaign.getAdvertiser(),campaign,
                        setting.getBid(),setting.getQualityScore(),outcome.adRank(),outcome.position(),outcome.cpc(),views,actualCtr,allowedClicks,
                        rowCost,after,date.atTime(12,0)));
                impressions+=views; clicks+=allowedClicks; cost=cost.add(rowCost); dailyBalance=after;
            }
            balance=balance.subtract(cost).max(BigDecimal.ZERO).setScale(2,RoundingMode.HALF_UP);
            rows.add(new DailySimulationRow(date,impressions,clicks,
                    clicks==0?BigDecimal.ZERO.setScale(2):cost.divide(BigDecimal.valueOf(clicks),2,RoundingMode.HALF_UP),cost,balance));
        }
        return List.copyOf(rows);
    }
}
