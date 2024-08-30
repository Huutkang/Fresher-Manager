package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Assignment;



@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    List<Assignment> findByFresherIdAndProjectId(Integer fresherId, Integer projectId);
    
    List<Assignment> findByFresherId(Integer fresherId);
    
    List<Assignment> findByProjectId(Integer projectId);
    
    List<Assignment> findByAssignmentNumber(Integer assignment_number);
    List<Assignment> findByScore(Double score);
    List<Assignment> findByAssignmentNumberAndScore(Integer assignment_number, Double score);
    
}
