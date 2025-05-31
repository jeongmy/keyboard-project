package chosun.keyboard_project.service;

import chosun.keyboard_project.repository.UserRepository;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    public MailService(JavaMailSender mailSender, UserRepository userRepository){
        this.mailSender = mailSender;
        this.userRepository = userRepository;
    }

    public void sendTemporaryMessage(String toEmail, String tempPassword){
        String username = userRepository.findByEmail(toEmail).get().getUsername();
        System.out.println(username + "님에게 임시 비밀번호 이메일 보냄 - " + toEmail);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("키덕-임시 비밀번호 안내");
        message.setText("임시 비밀번호: " + tempPassword + "\n로그인 후 비밀번호를 꼭 변경해주세요");
        mailSender.send(message);
    }

}
