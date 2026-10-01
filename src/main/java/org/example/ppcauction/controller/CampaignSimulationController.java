package org.example.ppcauction.controller;

import org.example.ppcauction.dto.DailySimulationRow;
import org.example.ppcauction.repository.CampaignRepository;
import org.example.ppcauction.service.CampaignSimulationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller @RequestMapping("/campaign-simulator")
public class CampaignSimulationController {
    private final CampaignSimulationService service; private final CampaignRepository campaigns;
    public CampaignSimulationController(CampaignSimulationService service,CampaignRepository campaigns){this.service=service;this.campaigns=campaigns;}
    @GetMapping public String page(@RequestParam(required=false) Long campaignId,Model model){model.addAttribute("campaigns",campaigns.findAll());model.addAttribute("campaignId",campaignId);model.addAttribute("days",7);return "campaign-simulator";}
    @PostMapping public String run(@RequestParam Long campaignId,@RequestParam int days,Model model){model.addAttribute("campaigns",campaigns.findAll());model.addAttribute("campaignId",campaignId);model.addAttribute("days",days);try{List<DailySimulationRow> rows=service.simulate(campaignId,days);model.addAttribute("rows",rows);model.addAttribute("labels",rows.stream().map(r->r.date().toString()).toList());model.addAttribute("costs",rows.stream().map(DailySimulationRow::cost).toList());model.addAttribute("clicks",rows.stream().map(DailySimulationRow::clicks).toList());model.addAttribute("balances",rows.stream().map(DailySimulationRow::remainingBudget).toList());}catch(IllegalArgumentException ex){model.addAttribute("error",ex.getMessage());}return "campaign-simulator";}
}
