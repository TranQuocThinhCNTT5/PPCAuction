package org.example.ppcauction.controller;

import org.example.ppcauction.repository.AdvertiserRepository;
import org.example.ppcauction.service.AuctionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.UUID;

@Controller @RequestMapping("/history")
public class HistoryController {
    private final AuctionService auctions; private final AdvertiserRepository advertisers;
    public HistoryController(AuctionService auctions,AdvertiserRepository advertisers){this.auctions=auctions;this.advertisers=advertisers;}
    @GetMapping public String list(@RequestParam(required=false) String keyword,@RequestParam(required=false) Long advertiserId,@RequestParam(required=false) LocalDate from,@RequestParam(required=false) LocalDate to,Model model){model.addAttribute("rows",auctions.history(keyword,advertiserId,from,to));model.addAttribute("advertisers",advertisers.findAll());model.addAttribute("keyword",keyword);model.addAttribute("advertiserId",advertiserId);model.addAttribute("from",from);model.addAttribute("to",to);return "history/list";}
    @GetMapping("/{auctionId}") public String detail(@PathVariable UUID auctionId,Model model){model.addAttribute("rows",auctions.getAuction(auctionId));model.addAttribute("auctionId",auctionId);return "history/detail";}
}
