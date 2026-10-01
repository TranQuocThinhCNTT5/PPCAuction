package org.example.ppcauction;

import org.example.ppcauction.dto.ChartData;
import org.example.ppcauction.dto.DashboardData;
import org.example.ppcauction.repository.AdvertiserRepository;
import org.example.ppcauction.repository.AuctionResultRepository;
import org.example.ppcauction.repository.CampaignRepository;
import org.example.ppcauction.repository.KeywordRepository;
import org.example.ppcauction.service.AdvertiserService;
import org.example.ppcauction.service.AuctionService;
import org.example.ppcauction.service.CampaignService;
import org.example.ppcauction.service.CampaignSimulationService;
import org.example.ppcauction.service.DashboardService;
import org.example.ppcauction.service.KeywordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.Map;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers={org.example.ppcauction.controller.PageController.class,
        org.example.ppcauction.controller.AdvertiserController.class,
        org.example.ppcauction.controller.KeywordController.class,
        org.example.ppcauction.controller.CampaignController.class,
        org.example.ppcauction.controller.AuctionController.class,
        org.example.ppcauction.controller.CampaignSimulationController.class})
class PageRoutesTest {
    @Autowired MockMvc mvc;
    @MockitoBean AdvertiserRepository advertisers;
    @MockitoBean KeywordRepository keywords;
    @MockitoBean CampaignRepository campaigns;
    @MockitoBean AuctionResultRepository results;
    @MockitoBean AuctionService auctionService;
    @MockitoBean DashboardService dashboard;
    @MockitoBean AdvertiserService advertiserService;
    @MockitoBean KeywordService keywordService;
    @MockitoBean CampaignService campaignService;
    @MockitoBean CampaignSimulationService campaignSimulationService;

    @Test void removedInformationalRoutesAreNotAvailable() throws Exception {
        mvc.perform(get("/theory")).andExpect(status().isNotFound());
        mvc.perform(get("/ppc-vs-seo")).andExpect(status().isNotFound());
    }

    @Test void dashboardRouteRendersDataBackedKpisAndCharts() throws Exception {
        ChartData empty=new ChartData(java.util.List.of(),java.util.List.of());
        DashboardData data=new DashboardData(0,0,0,0,0,0, BigDecimal.ZERO,BigDecimal.ZERO,
                Map.of("cost",empty,"clicks",empty,"impressions",empty,"positions",empty,"adRank",empty));
        when(dashboard.getDashboard()).thenReturn(data);
        mvc.perform(get("/dashboard")).andExpect(status().isOk()).andExpect(view().name("dashboard"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Chi phí theo thời gian")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Ad Rank")));
    }

    @Test void managementFormsAndSimulationPagesRender() throws Exception {
        when(advertiserService.search("")).thenReturn(java.util.List.of());
        when(keywordService.search("", "")).thenReturn(java.util.List.of());
        when(campaignService.search("", "")).thenReturn(java.util.List.of());
        when(advertisers.findAll()).thenReturn(java.util.List.of());
        when(keywords.findAll()).thenReturn(java.util.List.of());
        when(campaigns.findAll()).thenReturn(java.util.List.of());
        mvc.perform(get("/advertisers/new")).andExpect(status().isOk()).andExpect(view().name("advertisers/form"));
        mvc.perform(get("/keywords/new")).andExpect(status().isOk()).andExpect(view().name("keywords/form"));
        mvc.perform(get("/campaigns/new")).andExpect(status().isOk()).andExpect(view().name("campaigns/form"));
        mvc.perform(get("/auction")).andExpect(status().isOk()).andExpect(view().name("auction/index"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Chạy mô phỏng đấu giá")));
        mvc.perform(get("/campaign-simulator")).andExpect(status().isOk()).andExpect(view().name("campaign-simulator"));
    }
}
