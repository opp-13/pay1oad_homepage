package com.pay1oad.homepage.service;

import com.pay1oad.homepage.model.JwtList;
import com.pay1oad.homepage.persistence.JwtRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtRedisService {
    private final JwtRedisRepository jwtRedisRepository;

    @Transactional
    public JwtList addJwtList(JwtList jwtList){
        JwtList save=jwtRedisRepository.save(jwtList);

        Optional<JwtList> result = Optional.ofNullable(jwtRedisRepository.findByUsername(save.getUsername()));

        if(result.isPresent()) {
            log.info("jwt saved");
            return result.get();
        }else {throw new RuntimeException("Jwt not Saved.");}
    }

    @Transactional(readOnly = true)
    public boolean getJwtListById(String reqId) {
        Optional<JwtList> result = Optional.ofNullable(jwtRedisRepository.findByUsername(reqId));
        log.info("jwt exist, find by id");
        // Handling
        return result.isPresent();
    }

    @Transactional(readOnly = true)
    public boolean getJwtById(String reqId, String jwt) {
        Optional<JwtList> result = Optional.ofNullable(jwtRedisRepository.findByUsername(reqId));
        log.info(result.get().getJwt());
        if(result.isPresent() && result.get().getJwt().equals(jwt)){
            log.info("jwt exist, find by jwt");
            return true;
        } else {
            log.info("jwt not exist, find by jwt");
            return false;
        }
    }

    @Transactional
    public boolean deleteJwtListById(String reqId) {
        Optional<JwtList> result = Optional.ofNullable(jwtRedisRepository.findByUsername(reqId));

        // Handling
        if(result.isPresent()) {
            jwtRedisRepository.deleteByUsername(reqId);
            log.info("jwt delete");
            return true;
        }else {
            log.info("jwt delete failed, not exist");
            return false;
        }
    }

}
