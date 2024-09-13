package com.example.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Project;
import com.example.demo.entity.Users;
import com.example.demo.enums.Code;
import com.example.demo.exception.AppException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UsersService usersService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    NotificationService notificationService;

    private static final Logger log = LogManager.getLogger(EmailService.class);
    
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("thang.scime@gmail.com");
        try{
            mailSender.send(message);
            log.info("Sent message to " + to);
        } catch (RuntimeException e){
            log.error("Error sending email to {}: {}", to, e.getMessage());
            throw new AppException(Code.ERROR_SEND_EMAIL);
        }
    }

    public boolean projectAnnouncement(int idUser, int idPrj, boolean status){
        Users user = usersService.getUser(idUser);
        Project project = projectService.getProject(idPrj);
        String nameUser = user.getName();
        String namePrj = project.getName();
        String to = user.getEmail();
        String message;
        String subject = "Thông báo về dự án "+ namePrj;
        if (status){
            message = "được thêm vào ";
        }else{
            message = "bị loại khỏi ";
        }
        message = "Xin chào "+nameUser+". Bạn đã "+message+"dự án "+namePrj+".\nXin chân thành cảm ơn!";
        try{
            sendEmail(to, subject, message);
            notificationService.addNotification(idUser, idPrj, to+"\n"+subject+"\n"+message);
            return true;
        } catch (AppException e){
            return false;
        }
    }

}
