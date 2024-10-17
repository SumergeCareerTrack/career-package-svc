package com.sumerge.careertrack.career_package_svc.repositories;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserTokenRepository {

    private final String hashKey = "TOKENS";
    private final RedisTemplate<String, String> userTokensTemplate;

    public boolean existsByEmail(String email) {
        String fullKey = hashKey + ":" + email;
        return userTokensTemplate.hasKey(fullKey);
    }

}
