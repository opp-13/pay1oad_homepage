package com.pay1oad.homepage.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "username", timeToLive = 86400) // 1일 저장
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class JwtList {
    @Id
    private String username;

    private String jwt;

}
