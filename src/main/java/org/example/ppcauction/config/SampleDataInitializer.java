package org.example.ppcauction.config;

import org.example.ppcauction.dto.*;
import org.example.ppcauction.entity.*;
import org.example.ppcauction.repository.*;
import org.example.ppcauction.service.AuctionService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Configuration
public class SampleDataInitializer {
    @Bean
    ApplicationRunner sampleData(AdvertiserRepository advertiserRepository, KeywordRepository keywordRepository,
                                 CampaignRepository campaignRepository, AuctionResultRepository resultRepository,
                                 AuctionService auctionService) {
        return args -> seed(advertiserRepository,keywordRepository,campaignRepository,resultRepository,auctionService);
    }

    @Transactional
    void seed(AdvertiserRepository advertisers,KeywordRepository keywords,CampaignRepository campaigns,
              AuctionResultRepository results,AuctionService auctionService) {
        if(advertisers.count()==0) {
            String[] names={"Alpha Digital","Nova Media","Green Ads","Smart Growth","Vision Marketing"};
            for(int i=0;i<names.length;i++) advertisers.save(new Advertiser(names[i],names[i]+" Co.","contact"+(i+1)+"@example.vn","090000000"+(i+1),new BigDecimal("10000000.00"),"ACTIVE"));
            advertisers.flush();
        }
        if(keywords.count()==0) {
            String[] phrases={"quảng cáo google","dịch vụ SEO","quảng cáo online","marketing online","khóa học marketing","agency marketing","quảng cáo facebook","SEM","PPC","digital marketing"};
            List<Keyword> seed=new ArrayList<>();
            for(int i=0;i<phrases.length;i++) seed.add(new Keyword(phrases[i],800+175*i,i%3==0?"HIGH":"MEDIUM",5+i%5,"ACTIVE"));
            keywords.saveAll(seed);keywords.flush();
        }
        if(campaigns.count()==0 && advertisers.count()>0 && keywords.count()>0) {
            List<Advertiser> adList=advertisers.findAll(); List<Keyword> keyList=keywords.findAll();
            for(int i=0;i<Math.min(5,adList.size());i++) {
                Campaign c=new Campaign(new String[]{"Tăng nhận diện PPC","Tìm kiếm khách hàng","SEM mùa hè","Marketing số","Từ khóa chuyển đổi"}[i],adList.get(i),new BigDecimal("2500000.00"),new BigDecimal("150000.00"),LocalDate.now().minusDays(5),LocalDate.now().plusDays(30),"ACTIVE","Chiến dịch minh họa dùng dữ liệu mô phỏng.");
                for(int k=0;k<Math.min(3,keyList.size());k++) c.addKeywordSetting(new CampaignKeyword(keyList.get(k),new BigDecimal("1.50").add(new BigDecimal("0.25").multiply(BigDecimal.valueOf(i))),5+i%5,new BigDecimal("3.00")));
                campaigns.save(c);
            }
            campaigns.flush();
        }
        if(results.count()==0 && advertisers.count()>=2 && keywords.count()>0) {
            List<Advertiser> adList=advertisers.findAll(); List<Keyword> keyList=keywords.findAll();
            AuctionRequest req=new AuctionRequest();req.setKeywordId(keyList.getFirst().getId());
            List<AuctionEntryForm> entries=new ArrayList<>();
            String[] bids={"5.00","4.00","3.00"}; int[] scores={8,9,7};
            for(int i=0;i<Math.min(3,adList.size());i++) {AuctionEntryForm e=new AuctionEntryForm();e.setAdvertiserId(adList.get(i).getId());e.setBid(new BigDecimal(bids[i]));e.setQualityScore(scores[i]);e.setImpressions(1200+i*250);e.setCtr(new BigDecimal("2.50").add(new BigDecimal("0.50").multiply(BigDecimal.valueOf(i))));entries.add(e);}
            req.setEntries(entries);auctionService.simulate(req);
        }
    }
}
