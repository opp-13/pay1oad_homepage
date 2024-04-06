package com.pay1oad.homepage.service;
import lombok.extern.slf4j.Slf4j;
import com.pay1oad.homepage.model.Member;
import com.pay1oad.homepage.persistence.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public Member create(final Member member){
        if(member==null||member.getUsername()==null){
            throw new RuntimeException("Invalid Arguments");
        }
        final String username=member.getUsername();
        if(memberRepository.existsByUsername(username)){
            log.warn("Username already exists {}", username);
            throw new RuntimeException("Username already exists");
        }


        return memberRepository.save(member);
    }

    public Member getByCredentials(final String username, final String passwd){
        return memberRepository.findByUsernameAndPasswd(username, passwd);
    }
}
