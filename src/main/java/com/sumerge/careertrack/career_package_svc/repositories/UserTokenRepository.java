package com.sumerge.careertrack.wiki_svc.repositories;

import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserTokenRepository {

    private final String hashKey = "TOKENS";
    private final RedisTemplate<String, String> userTokensTemplate;

    public boolean existsById(UUID userId) {
        String fullKey = hashKey + ":" + userId.toString();
        return userTokensTemplate.hasKey(fullKey);
    }

}
