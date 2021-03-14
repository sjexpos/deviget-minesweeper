package com.deviget.challenge.minesweeper.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {
	private final Logger log = LoggerFactory.getLogger(GameService.class);

	private MongoTemplate mongoTemplate;
	
	@Autowired
	public GameServiceImpl(MongoTemplate mongoTemplate) {
		super();
		this.mongoTemplate = mongoTemplate;
	}

}
