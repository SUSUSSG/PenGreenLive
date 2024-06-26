package susussg.pengreenlive.order.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import susussg.pengreenlive.order.dto.ReviewDTO;

@Mapper
public interface ReviewMapper {

  List<ReviewDTO> findOrdersByUser(@Param("userUuid") String userUuid);

  List<ReviewDTO> findUnreviewedOrdersByUser(@Param("userUuid") String userUuid);

  List<ReviewDTO> findReviewedOrdersByUser(String userUuid);

  void deleteReviewByUserAndReviewSeq(@Param("userUuid") String userUuid, @Param("reviewSeq") long reviewSeq);

  void insertReview(@Param("review") ReviewDTO review);

  void updateReviewYn(@Param("review") ReviewDTO review);

}
