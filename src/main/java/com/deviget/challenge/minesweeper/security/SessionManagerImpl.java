package com.deviget.challenge.minesweeper.security;

import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.deviget.challenge.minesweeper.AppCacheConfig;
import com.deviget.challenge.minesweeper.core.model.User;

@Service
public class SessionManagerImpl implements SessionManager {

	@Cacheable(cacheNames=AppCacheConfig.SESSIONS_CACHE_REGION, key="#token")
	public UserSession getSession(String token) {
		return null;
	}

	@CachePut(cacheNames=AppCacheConfig.SESSIONS_CACHE_REGION, key="#result.token")
	public UserSession newSession(User user) {
		String token = UUID.randomUUID().toString();
		return new UserSession(token, user.getId(), user.getUsername());
	}

	@CacheEvict(cacheNames=AppCacheConfig.SESSIONS_CACHE_REGION, key="#session.token")
	public void destroySession(UserSession session) {
	}
	
}
