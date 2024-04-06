package com.pay1oad.homepage.controller;

import com.pay1oad.homepage.dto.MemberDTO;
import com.pay1oad.homepage.dto.ResponseDTO;
import com.pay1oad.homepage.model.Member;
import com.pay1oad.homepage.security.TokenProvider;
import com.pay1oad.homepage.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
@RequestMapping("/auth")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody MemberDTO memberDTO) {
        try {
            if (memberDTO==null||memberDTO.getPasswd()==null){
                throw new RuntimeException("invalid Passwd value");
            }

            Member member =Member.builder()
                    .username(memberDTO.getUsername())
                    .passwd(memberDTO.getPasswd())
                    .build();

            Member resisteredByMember=memberService.create(member);
            MemberDTO responseMemberDTO=MemberDTO.builder()
                    .userid(resisteredByMember.getUserid())
                    .username(resisteredByMember.getUsername())
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

        if(member!=null){
            final String token=tokenProvider.create(member);
            final MemberDTO responseMemberDTO = MemberDTO.builder()
                    .username(member.getUsername())
                    .userid(member.getUserid())
                    .token(token)
                    .build();

            return ResponseEntity.ok().body(responseMemberDTO);
        }else{
            ResponseDTO responseDTO=ResponseDTO.builder()
                    .error("Login Failed.")
                    .build();

            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
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
}
