package org.example.ppcauction.controller;

import org.example.ppcauction.dto.AuctionRow;
import org.example.ppcauction.repository.AdvertiserRepository;
import org.example.ppcauction.repository.AuctionResultRepository;
import org.example.ppcauction.repository.CampaignRepository;
import org.example.ppcauction.repository.KeywordRepository;
import org.example.ppcauction.service.AuctionService;
import org.example.ppcauction.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class PageController {
    private final AdvertiserRepository advertisers; private final KeywordRepository keywords; private final CampaignRepository campaigns;
    private final AuctionResultRepository results; private final AuctionService auctions; private final DashboardService dashboard;
    public PageController(AdvertiserRepository advertisers,KeywordRepository keywords,CampaignRepository campaigns,AuctionResultRepository results,AuctionService auctions,DashboardService dashboard){this.advertisers=advertisers;this.keywords=keywords;this.campaigns=campaigns;this.results=results;this.auctions=auctions;this.dashboard=dashboard;}
    @GetMapping("/") @Transactional(readOnly=true) public String home(Model model){model.addAttribute("advertiserCount",advertisers.count());model.addAttribute("keywordCount",keywords.count());model.addAttribute("campaignCount",campaigns.count());model.addAttribute("auctionCount",results.countResults());model.addAttribute("recent",results.findTop250ByOrderBySimulatedAtDesc().stream().limit(6).map(AuctionService::toRow).toList());return "home";}
    @GetMapping("/dashboard") public String dashboard(Model model){model.addAttribute("data",dashboard.getDashboard());return "dashboard";}
}
