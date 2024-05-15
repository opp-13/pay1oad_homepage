package com.pay1oad.homepage.persistence;

import com.pay1oad.homepage.model.JwtList;

import org.springframework.data.repository.CrudRepository;

public interface JwtRedisRepository extends CrudRepository<JwtList, String> {
    JwtList findByUsername(String username);

    JwtList findByJwt(String jwt);

    JwtList deleteByUsername(String username);
}
