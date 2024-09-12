package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.response.Api;
import com.example.demo.dto.response.Search;
import com.example.demo.enums.Code;
import com.example.demo.service.SearchService;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    // Tìm kiếm fresher theo các tiêu chí như tên, ngôn ngữ lập trình, email
    @GetMapping("/fresher")
    public ResponseEntity<Api<Map<String, List<Search>>>> searchFresher(@RequestParam String keywords) {
        Map<String, List<Search>> result = searchService.searchFresher(keywords);
        return Api.response(Code.OK, result);
    }

    // Tìm kiếm fresher theo các tiêu chí như tên, ngôn ngữ lập trình, email
    @GetMapping("/smartsearchfresher")
    public ResponseEntity<Api<Map<String, List<Search>>>> smartSearchFresher(@RequestParam String keywords) {
        Map<String, List<Search>> result = searchService.smartSearchFresher(keywords);
        return Api.response(Code.OK, result);
    }

    // Tìm kiếm trung tâm theo tên
    @GetMapping("/center/{name}")
    public ResponseEntity<Api<List<Search>>> searchCenterByName(@PathVariable String name) {
        List<Search> result = searchService.findCenterByName(name);
        return Api.response(Code.OK, result);
        
    }

    // Tìm kiếm dự án theo tên
    @GetMapping("/project/{name}")
    public ResponseEntity<Api<List<Search>>> searchProjectByName(@PathVariable String name) {
        List<Search> result = searchService.findProjectByName(name);
        return Api.response(Code.OK, result);
        
    }

    // Tìm kiếm các dự án của một fresher dựa trên fresherId
    @GetMapping("/fresher/{fresherId}/projects")
    public ResponseEntity<Api<List<Search>>> searchProjectsByFresherId(@PathVariable int fresherId) {
        System.out.println("fresherId = " + fresherId);
        List<Search> result = searchService.findProjectsByFresherId(fresherId);
        return Api.response(Code.OK, result);
    }
}
