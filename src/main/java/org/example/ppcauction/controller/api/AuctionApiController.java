package org.example.ppcauction.controller.api;

import org.example.ppcauction.dto.*;
import org.example.ppcauction.entity.*;
import org.example.ppcauction.repository.*;
import org.example.ppcauction.service.AuctionService;
import org.example.ppcauction.service.DashboardService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController @RequestMapping("/api")
public class AuctionApiController {
    private final AdvertiserRepository advertisers; private final KeywordRepository keywords; private final CampaignRepository campaigns;
    private final AuctionResultRepository results; private final AuctionService auctionService; private final DashboardService dashboard;
    public AuctionApiController(AdvertiserRepository advertisers,KeywordRepository keywords,CampaignRepository campaigns,AuctionResultRepository results,AuctionService auctionService,DashboardService dashboard){this.advertisers=advertisers;this.keywords=keywords;this.campaigns=campaigns;this.results=results;this.auctionService=auctionService;this.dashboard=dashboard;}
    @GetMapping("/advertisers") public List<AdvertiserDto> advertisers(){return advertisers.findAll().stream().map(a->new AdvertiserDto(a.getId(),a.getName(),a.getCompany(),a.getEmail(),a.getPhone(),a.getBudget(),a.getStatus())).toList();}
    @GetMapping("/keywords") public List<KeywordDto> keywords(){return keywords.findAll().stream().map(k->new KeywordDto(k.getId(),k.getPhrase(),k.getSearchVolume(),k.getCompetition(),k.getQualityScore(),k.getStatus())).toList();}
    @GetMapping("/campaigns") public List<CampaignDto> campaigns(){return campaigns.findAll().stream().map(c->new CampaignDto(c.getId(),c.getName(),c.getAdvertiser().getId(),c.getAdvertiser().getName(),c.getBudget(),c.getDailyBudget(),c.getStartDate(),c.getEndDate(),c.getStatus(),c.getDescription(),c.getKeywordSettings().stream().map(s->s.getKeyword().getPhrase()).toList())).toList();}
    @GetMapping("/auctions") public List<AuctionRow> auctions(){return results.findTop250ByOrderBySimulatedAtDesc().stream().map(AuctionService::toRow).toList();}
    @GetMapping("/auctions/{id}") public List<AuctionRow> auction(@PathVariable UUID id){return auctionService.getAuction(id);}
    @GetMapping("/dashboard") public DashboardData dashboard(){return dashboard.getDashboard();}
}
