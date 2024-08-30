package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.response.Search;
import com.example.demo.entity.Assignment;
import com.example.demo.entity.Center;
import com.example.demo.entity.Fresher;
import com.example.demo.entity.FresherProject;
import com.example.demo.entity.Notification;
import com.example.demo.entity.Project;
import com.example.demo.entity.Users;




@Service
public class SearchService {
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
    
    public Search user(String name){
        
        Search result = new Search();
        return result;
    }

    // Tìm fresher theo tên, theo ngôn ngữ lập trình, theo email
    public Search fresher(String keywords){
        List<Fresher> n = fresher.findFreshersByName(keywords);
        List<Fresher> p = fresher.findFreshersByProgrammingLanguage(keywords);
        List<Fresher> e = fresher.findFreshersByEmail(keywords);
        Search result = new Search();
        return result;
    }

    public Search center(String name){
        Search result = new Search();
        return result;
    }

    // Fresher có thể tìm kiếm các dự án mình đã tham gia
    public List<FresherProject> findMyProjects(int fresher_id){
        List<FresherProject> prj = fresherProject.findFresherProjects(fresher_id, null);
        return prj;
    }

    public Search convertToDTO(Object obj) {
        Search result = new Search();
        if (obj.getClass() == Fresher.class){
            Fresher fresher = (Fresher) obj;
            result.setName(fresher.getUser().getName());
            result.setUsername(fresher.getUser().getUsername());
        }
        else if (obj.getClass() == Users.class){
            Users user = (Users) obj;
            result.setName(user.getName());
            result.setUsername(user.getUsername());
        }
        else if (obj.getClass() == Center.class){
            Center center = (Center) obj;
            result.setId(center.getId());
            result.setName(center.getName());
        }
        else if (obj.getClass() == Project.class){
            Project project = (Project) obj;
            result.setId(project.getId());
            result.setName(project.getName());
        }
        else if (obj.getClass() == FresherProject.class){
            FresherProject fresherProject = (FresherProject) obj;
            result.setId(fresherProject.getProject().getId());
            result.setName(fresherProject.getProject().getName());
        }
        else if (obj.getClass() == Assignment.class){
            Assignment assignment = (Assignment) obj;
            result.setId(assignment.getProject().getId());
            result.setName(assignment.getProject().getName());
        }
        else if (obj.getClass() == Notification.class){
            Notification notification = (Notification) obj;
            result.setId(notification.getProject().getId());
            result.setName(notification.getProject().getName());
        }
        return result;
    }

    public List<Search> convertToListDTO(List<Object> obj) {
        List<Search> result = obj.stream().map(o -> convertToDTO(o)).toList();
        return result;
    }
}
