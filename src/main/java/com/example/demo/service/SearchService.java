package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.response.FresherResDto;
import com.example.demo.dto.response.Search;
import com.example.demo.entity.*;
import com.example.demo.exception.AppException;
import com.example.demo.enums.Code;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.commons.text.similarity.LevenshteinDistance;




@Service
public class SearchService {

    @Autowired
    private UsersService usersService;
    
    @Autowired
    private FresherService fresherService;
    
    @Autowired
    private CenterService centerService;
    
    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private AssignmentService assignmentService;
    
    @Autowired
    private FresherProjectService fresherProjectService;
    
    @Autowired
    private NotificationService notificationService;

    private static final Logger log = LogManager.getLogger(SearchService.class);

    // Tìm kiếm fresher theo tên, ngôn ngữ lập trình, email
    public Map<String, List<Search>> searchFresher(String keywords) {
        Map<String, List<Search>> result = new HashMap<>();
        
        // Tìm kiếm theo tên, ngôn ngữ lập trình, và email
        List<Search> byName = smartSearchFresherByName(keywords);
        List<Search> byProgrammingLanguage = smartSearchFresherByProgrammingLanguage(keywords);
        List<Search> byEmail = smartSearchFresherByEmail(keywords);
        
        result.put("name", byName);
        result.put("programmingLanguage", byProgrammingLanguage);
        result.put("email", byEmail);
    
        // Trộn kết quả và loại bỏ các fresher trùng
        List<Search> combinedResult = mergeSearchResults(byName, byProgrammingLanguage, byEmail);
        result.put("combined", combinedResult);
        
        return result;
    }


// Hàm trộn kết quả tìm kiếm và loại bỏ fresher trùng
private List<Search> mergeSearchResults(List<Search>... searchResults) {
    Map<Integer, SearchResult> mergedResults = new HashMap<>();

    for (List<Search> searches : searchResults) {
        for (Search search : searches) {
            int id = search.getId();
            // Nếu fresher chưa tồn tại trong mergedResults hoặc có rank cao hơn, thay thế kết quả
            if (!mergedResults.containsKey(id) || mergedResults.get(id).getRank() < calculateRankForMerge(search)) {
                mergedResults.put(id, new SearchResult(search, calculateRankForMerge(search)));
            }
        }
    }

    // Trả về danh sách đã loại bỏ fresher trùng với thứ hạng thấp hơn
    return mergedResults.values().stream().map(SearchResult::getSearch).collect(Collectors.toList());
}

// Tính toán thứ hạng cho từng fresher (áp dụng cho quá trình merge)
private int calculateRankForMerge(Search search) {
    // Bạn có thể điều chỉnh logic tính rank cho quá trình merge dựa trên thông tin tìm kiếm
    return 10; // Tạm thời giả sử rank cao nhất
}

    // Tìm kiếm trung tâm theo tên
    public Search findCenterByName(String name) {
        return centerService.findByName(name)
                .map(this::convertToDTO)
                .orElseThrow(() -> new AppException(Code.CENTER_NOT_FOUND));
    }

    // Tìm kiếm dự án theo tên
    public Search findProjectByName(String name) {
        return centerService.findByName(name)
                .map(this::convertToDTO)
                .orElseThrow(() -> new AppException(Code.CENTER_NOT_FOUND));
    }

    // Tìm kiếm các dự án mà fresher đã tham gia
    public List<Search> findProjectsByFresherId(int fresherId) {
        return fresherProjectService.findFresherProjects(fresherId, 0).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<Search> smartSearchFresherByEmail(String content) {
        List<Fresher> freshers = fresherService.findAllFreshers();
        List<SearchResult> results = new ArrayList<>();
    
        for (Fresher fresher : freshers) {
            String email = fresher.getUser().getEmail();
            int rank = calculateRank(content, email);
            if (rank > 0) {
                results.add(new SearchResult(convertToDTO(fresher), rank));
            }
        }
    
        // Sắp xếp kết quả theo rank giảm dần
        results.sort(Comparator.comparingInt(SearchResult::getRank).reversed());
    
        // Chuyển thành danh sách DTO để trả về
        return results.stream().map(SearchResult::getSearch).collect(Collectors.toList());
    }
    
    public List<Search> smartSearchFresherByProgrammingLanguage(String content) {
        List<Fresher> freshers = fresherService.findAllFreshers();
        List<SearchResult> results = new ArrayList<>();
    
        for (Fresher fresher : freshers) {
            String programmingLanguage = fresher.getProgrammingLanguage();
            int rank = calculateRank(content, programmingLanguage);
            if (rank > 0) {
                results.add(new SearchResult(convertToDTO(fresher), rank));
            }
        }
    
        // Sắp xếp kết quả theo rank giảm dần
        results.sort(Comparator.comparingInt(SearchResult::getRank).reversed());
    
        // Chuyển thành danh sách DTO để trả về
        return results.stream().map(SearchResult::getSearch).collect(Collectors.toList());
    }

    // Smart Search Fresher by Name
    public List<Search> smartSearchFresherByName(String content) {
        List<FresherResDto> freshers = fresherService.getAllFreshers();
        List<SearchResult> results = new ArrayList<>();

        for (FresherResDto fresher : freshers) {
            String name = fresher.getName();
            int rank = calculateRank(content, name);
            if (rank > 0) {
                results.add(new SearchResult(convertToDTO(fresher), rank));
            }
        }

        // Sắp xếp kết quả theo rank giảm dần
        results.sort(Comparator.comparingInt(SearchResult::getRank).reversed()); //

        // Chuyển thành danh sách DTO để trả về
        return results.stream().map(SearchResult::getSearch).collect(Collectors.toList());
    }

    // Tính toán rank cho từng fresher dựa trên từ khóa và tên
    private int calculateRank(String content, String target) {
    content = content.trim().toLowerCase();
    target = target.trim().toLowerCase();

    // 1. Exact Match
    if (content.equals(target)) {
        return 10;
    }

    // 2. Partial Match (các ký tự trong từ khóa xuất hiện liên tiếp)
    if (target.contains(content)) {
        return 9;
    }

    // 3. Fuzzy Match (Levenshtein Distance <= 2 được coi là gần đúng)
    LevenshteinDistance levenshtein = new LevenshteinDistance();
    int distance = levenshtein.apply(content, target);
    if (distance <= 2) {
        return 8;
    }

    // 4. Subword Search (tìm các từ con trong target)
    String[] targetParts = target.split("\\s+");
    for (String part : targetParts) {
        if (part.contains(content)) {
            return 7;
        }
    }

    // 5. Prefix/Suffix Search
    if (target.startsWith(content) || target.endsWith(content)) {
        return 6;
    }

    // Không tìm thấy phù hợp
    return 0;
}

    // Inner class để chứa kết quả với rank
    private static class SearchResult {
        private final Search search;
        private final int rank;

        public SearchResult(Search search, int rank) {
            this.search = search;
            this.rank = rank;
        }

        public Search getSearch() {
            return search;
        }

        public int getRank() {
            return rank;
        }
    }

    // Phương pháp chuyển đổi entity thành DTO Search
    public Search convertToDTO(Object obj) {
        Search result = new Search();
        if (obj instanceof Fresher fresher) {
            result.setId(fresher.getUser().getId());
            result.setName(fresher.getUser().getName());
            result.setUsername(fresher.getUser().getUsername());
        } else if (obj instanceof Users user) {
            result.setId(user.getId());
            result.setName(user.getName());
            result.setUsername(user.getUsername());
        } else if (obj instanceof Center center) {
            result.setId(center.getId());
            result.setName(center.getName());
        } else if (obj instanceof Project project) {
            result.setId(project.getId());
            result.setName(project.getName());
        } else if (obj instanceof FresherProject fresherProject) {
            result.setId(fresherProject.getProject().getId());
            result.setName(fresherProject.getProject().getName());
        } else if (obj instanceof Assignment assignment) {
            result.setId(assignment.getProject().getId());
            result.setName(assignment.getProject().getName());
        } else if (obj instanceof Notification notification) {
            result.setId(notification.getId());
            result.setName(notification.getProject().getName());
        }
        return result;
    }

    // Chuyển đổi danh sách entity thành danh sách DTO
    public List<Search> convertToListDTO(List<?> entities) {
        return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
