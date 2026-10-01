package org.example.ppcauction;

import org.example.ppcauction.dto.AuctionBid;
import org.example.ppcauction.dto.AuctionOutcome;
import org.example.ppcauction.service.AuctionService;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class AuctionServiceTest {
    private AuctionBid bid(String name,String amount,int score,int impressions,String ctr,String budget){
        return new AuctionBid(name,new BigDecimal(amount),score,impressions,new BigDecimal(ctr),new BigDecimal(budget));
    }
    private List<AuctionOutcome> example(){return AuctionService.calculate(List.of(
            bid("Nhà A","5",8,1000,"3","1000"),bid("Nhà B","4",9,1000,"3","1000"),bid("Nhà C","3",7,1000,"3","1000")));}

    @Test void calculatesAdRankAsBidTimesQualityScore(){assertEquals(new BigDecimal("40.00"),example().getFirst().adRank());}
    @Test void sortsParticipantsByAdRankDescending(){assertEquals(List.of("Nhà A","Nhà B","Nhà C"),example().stream().map(AuctionOutcome::advertiser).toList());}
    @Test void assignsSequentialPositions(){assertEquals(List.of(1,2,3),example().stream().map(AuctionOutcome::position).toList());}
    @Test void calculatesCpcFromNextRank(){assertEquals(new BigDecimal("5.00"),example().getFirst().cpc());assertEquals(new BigDecimal("3.33"),example().get(1).cpc());}
    @Test void lastPositionPaysItsBid(){assertEquals(new BigDecimal("3.00"),example().getLast().cpc());}
    @Test void cpcNeverExceedsCurrentBid(){var result=AuctionService.calculate(List.of(bid("Top","1",1,100,"1","100"),bid("Next","100",10,100,"1","100")));assertTrue(result.getFirst().cpc().compareTo(result.getFirst().bid())<=0);}
    @Test void calculatesClicksFromImpressionsAndCtr(){var result=AuctionService.calculate(List.of(bid("A","2",5,1000,"3.50","100")));assertEquals(35,result.getFirst().clicks());}
    @Test void calculatesCostFromClicksAndCpc(){var result=AuctionService.calculate(List.of(bid("A","2",5,1000,"1.00","100")));assertEquals(new BigDecimal("20.00"),result.getFirst().cost());}
    @Test void remainingBudgetNeverGoesBelowZero(){var result=AuctionService.calculate(List.of(bid("A","2",5,1000,"10.00","1")));assertEquals(BigDecimal.ZERO.setScale(2),result.getFirst().remainingBudget());}
}
