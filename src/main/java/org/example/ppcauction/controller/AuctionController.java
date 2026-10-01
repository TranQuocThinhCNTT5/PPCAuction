package org.example.ppcauction.controller;

import jakarta.validation.Valid;
import org.example.ppcauction.dto.*;
import org.example.ppcauction.entity.Advertiser;
import org.example.ppcauction.repository.AdvertiserRepository;
import org.example.ppcauction.repository.CampaignRepository;
import org.example.ppcauction.repository.KeywordRepository;
import org.example.ppcauction.service.AuctionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuctionController {
    private final AuctionService service; private final AdvertiserRepository advertisers; private final KeywordRepository keywords; private final CampaignRepository campaigns;
    public AuctionController(AuctionService service,AdvertiserRepository advertisers,KeywordRepository keywords,CampaignRepository campaigns){this.service=service;this.advertisers=advertisers;this.keywords=keywords;this.campaigns=campaigns;}
    @GetMapping("/auction") public String page(Model model){AuctionRequest req=new AuctionRequest();req.setEntries(advertisers.findAll().stream().filter(a->"ACTIVE".equalsIgnoreCase(a.getStatus())).map(AuctionController::defaultEntry).toList());model.addAttribute("request",req);references(model);return "auction/index";}
    @PostMapping("/auction") public String run(@Valid @ModelAttribute("request") AuctionRequest request,BindingResult errors,Model model){references(model);if(!errors.hasErrors()){try{model.addAttribute("rows",service.simulate(request));model.addAttribute("message","Phiên mô phỏng đã được lưu vào lịch sử.");}catch(IllegalArgumentException ex){errors.reject("auction.invalid",ex.getMessage());}}if(errors.hasErrors()) model.addAttribute("error",errors.getAllErrors().getFirst().getDefaultMessage());return "auction/index";}
    private void references(Model model){model.addAttribute("advertisers",advertisers.findAll());model.addAttribute("keywords",keywords.findAll());model.addAttribute("campaigns",campaigns.findAll());}
    private static AuctionEntryForm defaultEntry(Advertiser a){AuctionEntryForm e=new AuctionEntryForm();e.setAdvertiserId(a.getId());e.setBid(new java.math.BigDecimal("2.00"));e.setQualityScore(5);e.setImpressions(1000);e.setCtr(new java.math.BigDecimal("3.00"));return e;}
}
