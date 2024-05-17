package susussg.pengreenlive.broadcast.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import susussg.pengreenlive.broadcast.dto.*;
import susussg.pengreenlive.broadcast.mapper.BroadcastRegisterMapper;
import susussg.pengreenlive.util.Service.ByteArrayMultipartFile;
import susussg.pengreenlive.util.Service.ImageService;
import susussg.pengreenlive.util.Service.S3Service;


import java.util.Base64;
import java.util.List;


@Service
@Log4j2
@RequiredArgsConstructor

public class BroadcastRegisterServiceImpl implements BroadcastRegisterService {

    @Autowired
    private final BroadcastRegisterMapper broadcastRegisterMapper;

    private final BroadcastStatisticsService broadcastStatisticsService;

    @Autowired
    ImageService imageService;

    @Autowired
    S3Service s3Service;


    @Override
    @Transactional
    public List<BroadcastCategoryDTO> getAllCategory() {
        List<BroadcastCategoryDTO> categoryList = broadcastRegisterMapper.selectAllCategory();
        if (categoryList.isEmpty()) {
            throw new RuntimeException("category  empty");
        } else {
            return categoryList;
        }
    }

    @Override
    @Transactional
    public void registerBroadcast(BroadcastRegistrationRequestDTO broadcastRegisterInfo, long vendorId) {
        String channelName = getChannelName(vendorId); // 판매자 정보

        BroadcastDTO broadcastDTO = BroadcastDTO.builder()
                .channelNm(channelName)
                .broadcastTitle(broadcastRegisterInfo.getBroadcastTitle())
                .broadcastSummary(broadcastRegisterInfo.getBroadcastSummary())
                .broadcastScheduledTime(broadcastRegisterInfo.getBroadcastScheduledTime())
                .categoryCd(broadcastRegisterInfo.getCategoryCd())
                .build();
        try {
            if(broadcastRegisterInfo.getImage() != null) {
                byte[] imageBytes = Base64.getDecoder().decode(broadcastRegisterInfo.getImage());
                byte[] compressedImage = imageService.compressAndResizeImage(imageBytes, 400, 1f);
                MultipartFile multipartFile = new ByteArrayMultipartFile(compressedImage, "broadcast", "broadcast.jpg", "image/jpeg");
                String url = s3Service.uploadFile(multipartFile, "broadcast");
                broadcastDTO.setBroadcastImageUrl(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Broadcast image decode Failed");
        }

        saveBroadcast(broadcastDTO);

        broadcastRegisterInfo.getRegisteredProducts().forEach(productInfo -> {
            BroadcastProductDTO productDTO = BroadcastProductDTO.builder()
                    .broadcastSeq(broadcastDTO.getBroadcastSeq())
                    .productSeq(productInfo.getProductSeq())
                    .discountRate(productInfo.getDiscountRate())
                    .discountPrice(productInfo.getDiscountPrice())
                    .build();
            saveBroadcastProduct(productDTO);
        });

        broadcastRegisterInfo.getNotices().forEach(notice -> {
            NoticeDTO noticeDTO = NoticeDTO.builder()
                    .broadcastSeq(broadcastDTO.getBroadcastSeq())
                    .noticeContent(notice)
                    .build();
            saveNotice(noticeDTO);
        });

        broadcastRegisterInfo.getBenefits().forEach(benefit -> {
            BenefitDTO benefitDTO = BenefitDTO.builder()
                    .broadcastSeq(broadcastDTO.getBroadcastSeq())
                    .benefitContent(benefit)
                    .build();
            saveBenefit(benefitDTO);
        });

        broadcastRegisterInfo.getQa().forEach(qaInfo -> {
            FaqDTO faqDTO = FaqDTO.builder()
                    .broadcastSeq(broadcastDTO.getBroadcastSeq())
                    .questionTitle(qaInfo.getQuestionTitle())
                    .questionAnswer(qaInfo.getQuestionAnswer())
                    .build();
            saveFaq(faqDTO);
        });
    }

    private String getChannelName(long vendorId) {
        String channelName = broadcastRegisterMapper.selectChannelName(vendorId);
        if (channelName.isEmpty()) {
            throw new RuntimeException("channel name empty");
        } else {
            return channelName;
        }
    }

    private void saveBroadcast(BroadcastDTO broadcastDTO) {
        int result = broadcastRegisterMapper.insertBroadcast(broadcastDTO);
        BroadcastStatistics broadcastStatistics = BroadcastStatistics.builder()
                .broadcastSeq(broadcastDTO.getBroadcastSeq())
                .broadcastDuration(0)
                .avgProductClicks(0)
                .avgViewerCount(0)
                .avgViewingTime(0)
                .likesCount(0)
                .maxViewerCount(0)
                .avgPurchaseAmount(0)
                .totalSalesAmount(0)
                .totalSalesQty(0)
                .viewsCount(0)
                .build();
        broadcastStatisticsService.insertBroadcastStatistics(broadcastStatistics);
        if (result != 1) {
            throw new RuntimeException("broadcast insert failed");
        }
    }

    private void saveBroadcastProduct(BroadcastProductDTO broadcastProductDTO) {
        int result = broadcastRegisterMapper.insertBroadcastProduct(broadcastProductDTO);
        if (result != 1) {
            throw new RuntimeException("broadcastProduct insert failed");
        }
    }

    private void saveNotice(NoticeDTO noticeDTO) {
        int result = broadcastRegisterMapper.insertNotice(noticeDTO);
        if (result != 1) {
            throw new RuntimeException("notice insert failed");
        }
    }

    private void saveFaq(FaqDTO faqDTO) {
        int result = broadcastRegisterMapper.insertFaq(faqDTO);
        if (result != 1) {
            throw new RuntimeException("faq insert failed");
        }
    }

    private void saveBenefit(BenefitDTO benefitDTO) {
        int result = broadcastRegisterMapper.insertBenefit(benefitDTO);
        if (result != 1) {
            throw new RuntimeException("benefit insert failed");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChannelSalesProductDTO> getChannelSalesProductAll(long vendorId) {
        List<ChannelSalesProductDTO> productList = broadcastRegisterMapper.selectChannelSalesProduct(vendorId);
        if (productList.isEmpty()) {
            throw new RuntimeException("product list empty");
        } else {
            return productList;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrepareBroadcastInfoDTO> getUpcomingBroadcastInfo(long vendorId) {
        return broadcastRegisterMapper.selectPrepareBroadcastInfo(vendorId);
    }
}
