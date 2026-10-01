package org.example.ppcauction.service;

import org.example.ppcauction.dto.*;
import org.example.ppcauction.entity.AuctionResult;
import org.example.ppcauction.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private final AdvertiserRepository advertisers; private final KeywordRepository keywords; private final CampaignRepository campaigns; private final AuctionResultRepository results;
    public DashboardService(AdvertiserRepository advertisers,KeywordRepository keywords,CampaignRepository campaigns,AuctionResultRepository results){this.advertisers=advertisers;this.keywords=keywords;this.campaigns=campaigns;this.results=results;}

    @Transactional(readOnly=true)
    public DashboardData getDashboard() {
        List<AuctionResult> all=results.findAll();
        long auctions=all.stream().map(AuctionResult::getAuctionId).distinct().count();
        Map<String,List<AuctionResult>> daily=all.stream().collect(Collectors.groupingBy(r->r.getSimulatedAt().toLocalDate().toString(),TreeMap::new,Collectors.toList()));
        DateTimeFormatter fmt=DateTimeFormatter.ofPattern("dd/MM");
        List<String> dates=new ArrayList<>(); List<Number> dailyCost=new ArrayList<>(),dailyClicks=new ArrayList<>(),dailyImpressions=new ArrayList<>();
        daily.forEach((day,rows)->{dates.add(java.time.LocalDate.parse(day).format(fmt));dailyCost.add(rows.stream().map(AuctionResult::getCost).reduce(BigDecimal.ZERO,BigDecimal::add));dailyClicks.add(rows.stream().mapToLong(AuctionResult::getClicks).sum());dailyImpressions.add(rows.stream().mapToLong(AuctionResult::getImpressions).sum());});
        Map<Integer,Long> positions=all.stream().collect(Collectors.groupingBy(AuctionResult::getPosition,TreeMap::new,Collectors.counting()));
        Map<String,BigDecimal> advertiserRanks=all.stream().collect(Collectors.groupingBy(r->r.getAdvertiser().getName(),TreeMap::new,Collectors.mapping(AuctionResult::getAdRank,Collectors.reducing(BigDecimal.ZERO,BigDecimal::add))));
        Map<String,ChartData> charts=new LinkedHashMap<>();
        charts.put("cost",new ChartData(dates,dailyCost)); charts.put("clicks",new ChartData(dates,dailyClicks)); charts.put("impressions",new ChartData(dates,dailyImpressions));
        charts.put("positions",new ChartData(positions.keySet().stream().map(p->"Vị trí "+p).toList(),positions.values().stream().map(Long::valueOf).map(n->(Number)n).toList()));
        charts.put("adRank",new ChartData(new ArrayList<>(advertiserRanks.keySet()),new ArrayList<>(advertiserRanks.values())));
        return new DashboardData(advertisers.count(),keywords.count(),campaigns.count(),auctions,
                all.stream().mapToLong(AuctionResult::getImpressions).sum(),all.stream().mapToLong(AuctionResult::getClicks).sum(),
                all.stream().map(AuctionResult::getCost).reduce(BigDecimal.ZERO,BigDecimal::add),
                all.isEmpty()?BigDecimal.ZERO:all.stream().map(AuctionResult::getCpc).reduce(BigDecimal.ZERO,BigDecimal::add).divide(BigDecimal.valueOf(all.size()),2,java.math.RoundingMode.HALF_UP),charts);
    }
}
