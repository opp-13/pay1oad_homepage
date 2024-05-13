package com.pay1oad.homepage.controller;

import com.pay1oad.homepage.dto.MemberDTO;
import com.pay1oad.homepage.dto.ResponseDTO;
//import com.pay1oad.homepage.event.UserRegistrationEvent;
import com.pay1oad.homepage.model.Member;
import com.pay1oad.homepage.security.TokenProvider;
import com.pay1oad.homepage.service.EmailVerificationService;
import com.pay1oad.homepage.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Base64;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
@RequestMapping("/auth")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;


    /*private final JavaMailSender mailSender;
    private final EmailVerificationService verificationService;

    @Autowired
    public MemberController(JavaMailSender mailSender, EmailVerificationService verificationService) {
        this.mailSender = mailSender;
        this.verificationService = verificationService;
    }*/

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody MemberDTO memberDTO) {
        try {
            if (memberDTO==null||memberDTO.getPasswd()==null){
                throw new RuntimeException("Passwd value null");
            }else if(!validPasswd(memberDTO.getPasswd())){
                throw new RuntimeException("invalid Passwd value");
            }else if (!validEmail(memberDTO.getEmail())) {
                throw new RuntimeException("invalid Email");
            }

            Member member =Member.builder()
                    .username(memberDTO.getUsername())
                    .passwd(memberDTO.getPasswd())
                    .email(memberDTO.getEmail())
                    .build();

            Member resisteredByMember=memberService.create(member);
            MemberDTO responseMemberDTO=MemberDTO.builder()
                    .userid(String.valueOf(resisteredByMember.getUserid()))
                    .username(resisteredByMember.getUsername())
                    .email(resisteredByMember.getEmail())
                    .build();

            return ResponseEntity.ok().body(responseMemberDTO);

        }catch(Exception e){
            ResponseDTO responseDTO=ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody MemberDTO memberDTO){
        Member member=memberService.getByCredentials(
                memberDTO.getUsername(),
                memberDTO.getPasswd());

        log.info(memberDTO.getUsername()+"\n"+memberDTO.getPasswd()+"\n");
        if(member!=null){
            final String token=tokenProvider.create(member);
            final MemberDTO responseMemberDTO = MemberDTO.builder()
                    .username(member.getUsername())
                    .userid(String.valueOf(member.getUserid()))
                    .email(member.getEmail())
                    .token(token)
                    .build();

            return ResponseEntity.ok().body(responseMemberDTO);
        }else{
            Member ckmember=memberService.checkID(
                    memberDTO.getUsername()
            );

            if(ckmember!=null){
                ResponseDTO responseDTO=ResponseDTO.builder()
                        .error("Password error.\nLogin Failed.")
                        .build();

                return ResponseEntity
                        .badRequest()
                        .body(responseDTO);
            }else{
                ResponseDTO responseDTO=ResponseDTO.builder()
                        .error("Member not exist.\nLogin Failed.")
                        .build();

                return ResponseEntity
                        .badRequest()
                        .body(responseDTO);
            }

        }

    }

    @GetMapping("/test")
    public String getSbbText() {
        return "sbb";
    }

    @PostMapping("/test")
    public String postToSbb() {
        return "sbb";
    }

    private String getText(Member member, String verificationId) {
        String encodedVerificationId = new String(Base64.getEncoder().encode(verificationId.getBytes()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(member.getUsername()).append("님").append(System.lineSeparator()).append(System.lineSeparator());
        buffer.append("Pay1oad 회원 생성이 성공적으로 완료되었습니다.");

        buffer.append("이 링크를 따라서 회원가입을 완료해 주세요: http://localhost:8080/verify/email?id=").append(encodedVerificationId);
        buffer.append(System.lineSeparator()).append(System.lineSeparator());
        buffer.append("만약 이 메일이 온 이유를 모르겠다면 무시하셔도 좋습니다.");
        buffer.append(System.lineSeparator()).append(System.lineSeparator());
        buffer.append("감사합니다.").append(System.lineSeparator());
        return buffer.toString();
    }

    private boolean validPasswd(String passwd){
        if(passwd.length() < 8)
            return false;

        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$";

        return passwd.matches(pattern);
    }

    private boolean validEmail(String email){
        String pattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

        return email.matches(pattern);
    }
}
