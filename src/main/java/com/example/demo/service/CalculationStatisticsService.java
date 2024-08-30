package com.example.demo.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.response.AssignmentResDto;
import com.example.demo.entity.Assignment;




@Service
public class CalculationStatisticsService {
    @Autowired
    private UsersService users;
    @Autowired
    private FresherService fresher;
    @Autowired
    private CenterService center;
    @Autowired
    private ProjectService project;
    @Autowired
    private AssignmentService assignment;
    @Autowired
    private FresherProjectService fresherProject;
    @Autowired
    private NotificationService notification;

    // View Số Lượng 
    public long count(String obj){
        switch (obj){
            case "center":
                return fresher.countFresher();

        }
        return 0;
    }

    // Tính Điểm cho fresher 
    public Double score(int fresher_id){
        List<Assignment> listAssignment = assignment.findAssignments(fresher_id, null);
        // có thể số bài sẽ khác với id nên không lấy 3 cái id đầu
        Double b1, b2, b3;
        b1=b2=b3=0.0;
        for (Assignment obj : listAssignment) {
            if (obj.getAssignmentNumber() == 1) {
                b1 = obj.getScore();
            }else if (obj.getAssignmentNumber() == 2) {
                b2 = obj.getScore();
            } else if (obj.getAssignmentNumber() == 3) {
                b3 = obj.getScore();
            }
        }
        return (b1 + b2 + b3) / 3;
    }

    public Double averageScore(int fresher_id){
        List<Assignment> listAssignment = assignment.findAssignments(fresher_id, null);
        Double totalScore = 0.0;
        for (Assignment obj : listAssignment) {
            totalScore += obj.getScore();
        }
        return totalScore / listAssignment.size();
    }

    // Thống kê số lượng fresher theo từng trung tâm.
    public Map<Integer, Integer> statisticalFresherByCenter(){
        Map<Integer, Integer> statistical = new HashMap<>();
        int j;
        for (int i=0; i < center.countCenter();i++) {
            j = fresher.getFresherByCenterId(i).size();
            statistical.put(i, j);
        }
        return statistical;
    }

    // Thống kê số lượng fresher theo điểm số
    public Map<BigDecimal, Integer> statisticalFresherByScore() {
        List<AssignmentResDto> list = assignment.getAllAssignments();
        Map<BigDecimal, Integer> statistical = new HashMap<>();
        
        for (AssignmentResDto obj : list) {
            BigDecimal score = roundToThreeDecimalPlaces(obj.getScore());
            statistical.put(score, statistical.getOrDefault(score, 0) + 1);
        }
        
        return statistical;
    }

    private BigDecimal roundToThreeDecimalPlaces(Double value) {
        return new BigDecimal(value).setScale(3, RoundingMode.HALF_UP);
    }

    public String convertScoreToGrade(BigDecimal score) {
        double scoreValue = score.doubleValue();
        if (scoreValue >= 9.0 && scoreValue <= 10.0) {
            return "A+";
        } else if (scoreValue >= 8.5 && scoreValue < 9.0) {
            return "A";
        } else if (scoreValue >= 8.0 && scoreValue < 8.5) {
            return "B+";
        } else if (scoreValue >= 7.0 && scoreValue < 8.0) {
            return "B";
        } else if (scoreValue >= 6.5 && scoreValue < 7.0) {
            return "C+";
        } else if (scoreValue >= 5.5 && scoreValue < 6.5) {
            return "C";
        } else if (scoreValue >= 5.0 && scoreValue < 5.5) {
            return "D+";
        } else if (scoreValue >= 4.0 && scoreValue < 5.0) {
            return "D";
        } else if (scoreValue >= 0.0 && scoreValue < 4.0) {
            return "F";
        } else {
            return "Invalid Score";
        }
    }
    
    public Map<String, Integer> statisticalFresherByGrade() {
        List<AssignmentResDto> list = assignment.getAllAssignments();
        Map<String, Integer> statistical = new HashMap<>();
        
        for (AssignmentResDto obj : list) {
            BigDecimal score = roundToThreeDecimalPlaces(obj.getScore());
            String grade = convertScoreToGrade(score);
            statistical.put(grade, statistical.getOrDefault(grade, 0) + 1);
        }
        
        return statistical;
    }
}

