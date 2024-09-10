package com.example.demo.service;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Token;
import com.example.demo.repository.TokenRepository;


@Component
public class TokenCleanupTaskService {

    @Autowired
    private TokenRepository tokenRepository;

    private static final Logger log = LogManager.getLogger(TokenCleanupTaskService.class);
    
    @Scheduled(cron = "${token.cleanup.cron}")
    public void runDailyTask() {
        System.out.println("Nhiệm vụ dọn dẹp các bản ghi token được chạy mỗi ngày vào thời gian chỉ định");

        // Lấy thời gian hiện tại
        Date now = new Date();

        // Tìm tất cả các token hết hạn
        List<Token> expiredTokens = tokenRepository.findByExpiryTimeBefore(now);

        // Xóa các token hết hạn
        tokenRepository.deleteAll(expiredTokens);

        log.info("Deleted " + expiredTokens.size() + " expired tokens.");
    }
}
