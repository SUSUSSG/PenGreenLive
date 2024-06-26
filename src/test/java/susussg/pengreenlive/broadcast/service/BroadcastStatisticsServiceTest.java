package susussg.pengreenlive.broadcast.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import susussg.pengreenlive.broadcast.dto.BroadcastStatistics;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class BroadcastStatisticsServiceTest {

    @Autowired
    private BroadcastStatisticsService broadcastStatisticsService;

//    @Test
//    public void testInsertBroadcastStatistics() {
//        BroadcastStatistics broadcastStatistics = new BroadcastStatistics(
//                7L,
//                100,
//                200,
//                50,
//                50,
//                10000L,
//                30,
//                60,
//                15,
//                500000L,
//                20
//        );
//
//        broadcastStatisticsService.insertBroadcastStatistics(broadcastStatistics);
//
//        BroadcastStatistics result = broadcastStatisticsService.findById(7L);
//        assertNotNull(result);
//        assertEquals(7L, result.getBroadcastSeq());
//        assertEquals(100, result.getAvgViewerCount());
//        assertEquals(200, result.getMaxViewerCount());
//        assertEquals(50, result.getViewsCount());
//        assertEquals(50, result.getLikesCount());
//        assertEquals(10000L, result.getAvgPurchaseAmount());
//        assertEquals(30, result.getAvgViewingTime());
//        assertEquals(60, result.getBroadcastDuration());
//        assertEquals(15, result.getAvgProductClicks());
//        assertEquals(500000L, result.getTotalSalesAmount());
//        assertEquals(20, result.getTotalSalesQty());
//    }

    @Test
    public void testUpdateLikesCount() {
        broadcastStatisticsService.updateLikesCount(6L);
        BroadcastStatistics result = broadcastStatisticsService.findById(6L);
        assertNotNull(result);
        assertEquals(2, result.getLikesCount());
    }

    @Test
    public void testDecrementLikesCount(){
        broadcastStatisticsService.decrementLikesCount(6L);
        BroadcastStatistics result = broadcastStatisticsService.findById(6L);
        assertEquals(0, result.getLikesCount());
    }

    @Test
    public void testUpdateAverageViewerCount() {
        broadcastStatisticsService.updateAverageViewerCount(6L, 150);
        BroadcastStatistics result = broadcastStatisticsService.findById(6L);
        assertNotNull(result);
        assertEquals(150, result.getAvgViewerCount());
    }

    @Test
    public void testUpdateMaxViewerCount() {
        broadcastStatisticsService.updateMaxViewerCount(6L, 250);
        BroadcastStatistics result = broadcastStatisticsService.findById(6L);
        assertNotNull(result);
        assertEquals(250, result.getMaxViewerCount());
    }

    @Test
    public void testUpdateBroadcastStatistics() {
        BroadcastStatistics broadcastStatistics = new BroadcastStatistics();
        broadcastStatistics.setBroadcastSeq(6L);
        broadcastStatistics.setAvgViewerCount(150);
        broadcastStatistics.setMaxViewerCount(250);
        broadcastStatistics.setBroadcastDuration(3600);

        broadcastStatisticsService.updateBroadcastStatistics(6L, broadcastStatistics);

        BroadcastStatistics result = broadcastStatisticsService.findById(6L);
        assertNotNull(result);
        assertEquals(150, result.getAvgViewerCount());
        assertEquals(250, result.getMaxViewerCount());
        assertEquals(3600, result.getBroadcastDuration());
    }

    @Test
    public void testIncrementViewsCount(){
        broadcastStatisticsService.incrementViewsCount(6L);
        BroadcastStatistics result = broadcastStatisticsService.findById(6L);
        assertNotNull(result);
        assertEquals(1, result.getViewsCount());
    }

    @Test
    public void testGetStatisticsByVendorAndDateRange() {
        long vendorSeq = 1L;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse("2023-01-01 00:00:00", formatter);
        LocalDateTime endDate = LocalDateTime.parse("2024-12-31 23:59:59", formatter);

        List<BroadcastStatistics> statistics = broadcastStatisticsService.getStatisticsByVendorAndDateRange(vendorSeq, startDate, endDate);

        assertNotNull(statistics);
        assertEquals(4, statistics.size());
    }

    @Test
    public void testGetAverageBroadcastDuration() {
        long vendorSeq = 1L;
        LocalDate startDate = LocalDate.parse("2023-01-01");
        LocalDate endDate = LocalDate.parse("2023-12-31");
        int avgBroadcastDuration = broadcastStatisticsService.getAverageBroadcastDuration(vendorSeq, startDate, endDate);
        assertTrue(avgBroadcastDuration > 0);
    }

    @Test
    public void testGetAverageViewerCount() {
        long vendorSeq = 1L;
        LocalDate startDate = LocalDate.parse("2023-01-01");
        LocalDate endDate = LocalDate.parse("2023-12-31");
        int avgViewerCount = broadcastStatisticsService.getAverageViewerCount(vendorSeq, startDate, endDate);
        assertTrue(avgViewerCount > 0);
    }

    @Test
    public void testGetAveragePurchaseQuantity() {
        long vendorSeq = 1L;
        LocalDate startDate = LocalDate.parse("2023-01-01");
        LocalDate endDate = LocalDate.parse("2023-12-31");
        int avgPurchaseQuantity = broadcastStatisticsService.getAveragePurchaseQuantity(vendorSeq, startDate, endDate);
        assertTrue(avgPurchaseQuantity > 0);
    }

    @Test
    public void testGetAverageProductClicks() {
        long vendorSeq = 1L;
        LocalDate startDate = LocalDate.parse("2023-01-01");
        LocalDate endDate = LocalDate.parse("2023-12-31");
        int avgProductClicks = broadcastStatisticsService.getAverageProductClicks(vendorSeq, startDate, endDate);
        assertTrue(avgProductClicks > 0);
    }

    @Test
    public void testGetAverageViewingTime() {
        long vendorSeq = 1L;
        LocalDate startDate = LocalDate.parse("2023-01-01");
        LocalDate endDate = LocalDate.parse("2023-12-31");
        int avgViewingTime = broadcastStatisticsService.getAverageViewingTime(vendorSeq, startDate, endDate);
        assertTrue(avgViewingTime > 0);
    }

    @Test
    public void testGetAverageLikesCount() {
        long vendorSeq = 1L;
        LocalDate startDate = LocalDate.parse("2023-01-01");
        LocalDate endDate = LocalDate.parse("2023-12-31");
        int avgLikesCount = broadcastStatisticsService.getAverageLikesCount(vendorSeq, startDate, endDate);
        assertTrue(avgLikesCount > 0);
    }

    @Test
    public void testGetAveragePurchaseAmount() {
        long vendorSeq = 1L;
        LocalDate startDate = LocalDate.parse("2023-01-01");
        LocalDate endDate = LocalDate.parse("2023-12-31");
        long avgPurchaseAmount = broadcastStatisticsService.getAveragePurchaseAmount(vendorSeq, startDate, endDate);
        assertTrue(avgPurchaseAmount > 0);
    }
}
