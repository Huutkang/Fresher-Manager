package com.example.demo.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.response.Api;
import com.example.demo.enums.Code;
import com.example.demo.service.CalculationStatisticsService;

@RestController
@RequestMapping("/statistics")
public class CalculationStatisticsController {

    @Autowired
    private CalculationStatisticsService calculationStatisticsService;

    // API để đếm số lượng đối tượng (fresher hoặc center)
    @PostMapping("/count")
    public ResponseEntity<Api<Long>> count(@RequestBody Map<String, String> requestBody) {
        String object = requestBody.get("object");
        long count = calculationStatisticsService.count(object);
        return Api.response(Code.OK, count);
    }


    // API để tính điểm trung bình của fresher dựa trên fresher_id
    @GetMapping("/average-score/{fresherId}")
    public ResponseEntity<Api<Double>> averageScore(@PathVariable("fresherId") int fresherId) {
        Double averageScore = calculationStatisticsService.averageScore(fresherId);
        return Api.response(Code.OK, averageScore);
    }

    // API để tính điểm của fresher dựa trên fresher_id
    @GetMapping("/score/{fresherId}")
    public ResponseEntity<Api<Double>> score(@PathVariable("fresherId") int fresherId) {
        Double score = calculationStatisticsService.score(fresherId);
        return Api.response(Code.OK, score);
    }

    // API để thống kê số lượng fresher theo từng trung tâm
    @GetMapping("/fresher-by-center")
    public ResponseEntity<Api<Map<Integer, Integer>>> fresherByCenter() {
        Map<Integer, Integer> statistics = calculationStatisticsService.statisticalFresherByCenter();
        return Api.response(Code.OK, statistics);
    }

    // API để thống kê số lượng fresher theo điểm số
    @GetMapping("/fresher-by-score")
    public ResponseEntity<Api<Map<BigDecimal, Integer>>> fresherByScore() {
        Map<BigDecimal, Integer> statistics = calculationStatisticsService.statisticalFresherByScore();
        return Api.response(Code.OK, statistics);
    }

    // API để thống kê số lượng fresher theo điểm chữ (grade)
    @GetMapping("/fresher-by-grade")
    public ResponseEntity<Api<Map<String, Integer>>> fresherByGrade() {
        Map<String, Integer> statistics = calculationStatisticsService.statisticalFresherByGrade();
        return Api.response(Code.OK, statistics);
    }

    // API để lấy toàn bộ các thống kê liên quan đến fresher
    @GetMapping("/fresher-statistics")
    public ResponseEntity<Api<Map<String, Object>>> getFresherStatistics() {
        Map<String, Object> statistics = calculationStatisticsService.getFresherStatistics();
        return Api.response(Code.OK, statistics);
    }
}
