package susussg.pengreenlive.statistics.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import susussg.pengreenlive.statistics.dto.ProductDetailsDTO;
import susussg.pengreenlive.statistics.dto.ProductInChannelDTO;
import susussg.pengreenlive.statistics.dto.SalesDataDTO;

@Mapper
public interface ProductStatisticsMapper {
    List<ProductInChannelDTO> findTop10ProductsByChannel(Long channelSeq);
    List<ProductInChannelDTO> findAllProductsByChannelWithSales(Long channelSeq);
    SalesDataDTO findTotalSalesOrdersAvgPriceAvgBuyersAndAvgQuantityByChannel(Long channelSeq);
    ProductDetailsDTO findProductDetailsByProductCd(String productCd);

}
