package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.FresherProject;

@Repository
public interface FresherProjectRepository extends JpaRepository<FresherProject, Integer> {
    List<FresherProject> findByFresherIdAndProjectId(int fresherId, int projectId);
    List<FresherProject> findByFresherId(int fresherId);
    List<FresherProject> findByProjectId(int projectId);
    List<FresherProject> findByRole(String role);
}
