package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.FresherProject;

@Repository
public interface FresherProjectRepository extends JpaRepository<FresherProject, Integer> {
    List<FresherProject> findByFresherIdAndProjectId(Integer fresherId, Integer projectId);
    List<FresherProject> findByFresherId(Integer fresherId);
    List<FresherProject> findByProjectId(Integer projectId);
    List<FresherProject> findByRole(String role);
}
