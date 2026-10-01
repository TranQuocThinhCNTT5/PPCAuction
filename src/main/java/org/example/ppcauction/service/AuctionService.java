package org.example.ppcauction.service;

import org.example.ppcauction.dto.*;
import org.example.ppcauction.entity.*;
import org.example.ppcauction.exception.ResourceNotFoundException;
import org.example.ppcauction.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuctionService {
    private static final BigDecimal MIN_INCREMENT = BigDecimal.ONE;
    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private final AdvertiserRepository advertisers;
    private final KeywordRepository keywords;
    private final CampaignRepository campaigns;
    private final CampaignKeywordRepository campaignKeywords;
    private final AuctionResultRepository results;

    public AuctionService(AdvertiserRepository advertisers, KeywordRepository keywords, CampaignRepository campaigns,
                         CampaignKeywordRepository campaignKeywords, AuctionResultRepository results) {
        this.advertisers=advertisers; this.keywords=keywords; this.campaigns=campaigns;
        this.campaignKeywords=campaignKeywords; this.results=results;
    }

    /** Deterministic teaching model: rank = bid x quality, rank-ordered positions and next-rank CPC. */
    public static List<AuctionOutcome> calculate(List<AuctionBid> bids) {
        if (bids == null || bids.isEmpty()) throw new IllegalArgumentException("Phiên đấu giá cần ít nhất một người tham gia.");
        List<AuctionBid> ordered = bids.stream().sorted(Comparator
                .comparing((AuctionBid b) -> b.bid().multiply(BigDecimal.valueOf(b.qualityScore()))).reversed()
                .thenComparing(AuctionBid::advertiser, String.CASE_INSENSITIVE_ORDER)).toList();
        List<AuctionOutcome> outcomes = new ArrayList<>();
        for (int i=0; i<ordered.size(); i++) {
            AuctionBid current=ordered.get(i);
            BigDecimal rank=current.bid().multiply(BigDecimal.valueOf(current.qualityScore())).setScale(2,RoundingMode.HALF_UP);
            BigDecimal cpc=current.bid();
            if(i<ordered.size()-1) {
                BigDecimal nextRank=ordered.get(i+1).bid().multiply(BigDecimal.valueOf(ordered.get(i+1).qualityScore()));
                cpc=nextRank.divide(BigDecimal.valueOf(current.qualityScore()), 8, RoundingMode.HALF_UP).add(MIN_INCREMENT)
                        .min(current.bid()).max(BigDecimal.ZERO);
            }
            cpc=cpc.setScale(2,RoundingMode.HALF_UP);
            int clicks=BigDecimal.valueOf(current.impressions()).multiply(current.ctr())
                    .divide(HUNDRED,0,RoundingMode.HALF_UP).intValueExact();
            BigDecimal cost=cpc.multiply(BigDecimal.valueOf(clicks)).setScale(2,RoundingMode.HALF_UP);
            BigDecimal remaining=current.budget().subtract(cost).max(BigDecimal.ZERO).setScale(2,RoundingMode.HALF_UP);
            outcomes.add(new AuctionOutcome(current.advertiser(),current.bid(),current.qualityScore(),rank,i+1,cpc,
                    current.impressions(),current.ctr(),clicks,cost,remaining));
        }
        return List.copyOf(outcomes);
    }

    @Transactional
    public List<AuctionRow> simulate(AuctionRequest request) {
        Keyword keyword=keywords.findById(request.getKeywordId()).orElseThrow(()->new ResourceNotFoundException("Không tìm thấy từ khóa"));
        List<Advertiser> chosen=new ArrayList<>(); List<Campaign> chosenCampaigns=new ArrayList<>(); List<AuctionBid> bids=new ArrayList<>();
        for(AuctionEntryForm entry:request.getEntries()) {
            Advertiser advertiser=advertisers.findById(entry.getAdvertiserId()).orElseThrow(()->new ResourceNotFoundException("Không tìm thấy nhà quảng cáo"));
            if(!"ACTIVE".equalsIgnoreCase(advertiser.getStatus())) throw new IllegalArgumentException("Chỉ nhà quảng cáo đang hoạt động mới tham gia được.");
            Campaign campaign=null;
            if(entry.getCampaignId()!=null) {
                campaign=campaigns.findById(entry.getCampaignId()).orElseThrow(()->new ResourceNotFoundException("Không tìm thấy chiến dịch"));
                if(!campaign.getAdvertiser().getId().equals(advertiser.getId())) throw new IllegalArgumentException("Chiến dịch không thuộc nhà quảng cáo đã chọn.");
                boolean linked=campaign.getKeywordSettings().stream().anyMatch(s->s.getKeyword().getId().equals(keyword.getId()));
                if(!linked) throw new IllegalArgumentException("Từ khóa chưa được thêm vào chiến dịch đã chọn.");
            }
            chosen.add(advertiser); chosenCampaigns.add(campaign);
            BigDecimal budget=campaign==null?advertiser.getBudget():campaign.getBudget();
            bids.add(new AuctionBid(advertiser.getName(),entry.getBid(),entry.getQualityScore(),entry.getImpressions(),entry.getCtr(),budget));
        }
        if(chosen.stream().map(Advertiser::getId).distinct().count()!=chosen.size()) throw new IllegalArgumentException("Mỗi nhà quảng cáo chỉ được tham gia một lần trong một phiên.");
        List<AuctionOutcome> calculated=calculate(bids);
        UUID auctionId=UUID.randomUUID(); LocalDateTime now=LocalDateTime.now(); List<AuctionResult> saved=new ArrayList<>();
        Map<String,Integer> order=new HashMap<>(); for(int i=0;i<chosen.size();i++) order.put(chosen.get(i).getName(),i);
        for(AuctionOutcome o:calculated) {
            int i=order.get(o.advertiser()); AuctionBid b=bids.get(i);
            saved.add(new AuctionResult(auctionId,"AUCTION",keyword,chosen.get(i),chosenCampaigns.get(i),o.bid(),o.qualityScore(),o.adRank(),o.position(),o.cpc(),o.impressions(),o.ctr(),o.clicks(),o.cost(),o.remainingBudget(),now));
        }
        results.saveAll(saved);
        return saved.stream().sorted(Comparator.comparing(AuctionResult::getPosition)).map(AuctionService::toRow).toList();
    }

    @Transactional(readOnly=true)
    public List<AuctionRow> getAuction(UUID id) {
        return results.findByAuctionIdOrderByPosition(id).stream().map(AuctionService::toRow).toList();
    }
    @Transactional(readOnly=true)
    public List<AuctionRow> history(String keyword, Long advertiserId, java.time.LocalDate from, java.time.LocalDate to) {
        return results.searchHistory(keyword==null||keyword.isBlank()?null:keyword.trim(),advertiserId,
                from==null?null:from.atStartOfDay(),to==null?null:to.plusDays(1).atStartOfDay()).stream().map(AuctionService::toRow).toList();
    }
    public static AuctionRow toRow(AuctionResult r) {
        return new AuctionRow(r.getId(),r.getAuctionId(),r.getSimulationType(),r.getKeyword().getPhrase(),r.getAdvertiser().getName(),
                r.getCampaign()==null?null:r.getCampaign().getName(),r.getBid(),r.getQualityScore(),r.getAdRank(),r.getPosition(),
                r.getCpc(),r.getImpressions(),r.getCtr(),r.getClicks(),r.getCost(),r.getRemainingBudget(),r.getSimulatedAt());
    }
}
