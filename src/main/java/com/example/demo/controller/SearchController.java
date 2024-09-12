package com.example.demo.controller;

import com.example.demo.dto.response.Api;
import com.example.demo.dto.response.Search;
import com.example.demo.enums.Code;
import com.example.demo.exception.AppException;
import com.example.demo.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    // Tìm kiếm fresher theo các tiêu chí như tên, ngôn ngữ lập trình, email
    @GetMapping("/fresher")
    public ResponseEntity<Api<Map<String, List<Search>>>> searchFresher(@RequestParam String keywords) {
        try {
            Map<String, List<Search>> result = searchService.searchFresher(keywords);
            return Api.response(Code.OK, result);
        } catch (Exception e) {
            return Api.response(Code.INTERNAL_SERVER_ERROR);
        }
    }

    // Tìm kiếm trung tâm theo tên
    @GetMapping("/center/{name}")
    public ResponseEntity<Api<Search>> searchCenterByName(@PathVariable String name) {
        try {
            Search result = searchService.findCenterByName(name);
            return Api.response(Code.OK, result);
        } catch (AppException e) {
            return Api.response(e.getCode());
        } catch (Exception e) {
            return Api.response(Code.INTERNAL_SERVER_ERROR);
        }
    }

    // Tìm kiếm dự án theo tên
    @GetMapping("/project/{name}")
    public ResponseEntity<Api<Search>> searchProjectByName(@PathVariable String name) {
        try {
            Search result = searchService.findProjectByName(name);
            return Api.response(Code.OK, result);
        } catch (AppException e) {
            return Api.response(e.getCode());
        } catch (Exception e) {
            return Api.response(Code.INTERNAL_SERVER_ERROR);
        }
    }

    // Tìm kiếm các dự án của một fresher dựa trên fresherId
    @GetMapping("/fresher/{fresherId}/projects")
    public ResponseEntity<Api<List<Search>>> searchProjectsByFresherId(@PathVariable int fresherId) {
        System.out.println("fresherId = " + fresherId);
        List<Search> result = searchService.findProjectsByFresherId(fresherId);
        return Api.response(Code.OK, result);
    }
}
