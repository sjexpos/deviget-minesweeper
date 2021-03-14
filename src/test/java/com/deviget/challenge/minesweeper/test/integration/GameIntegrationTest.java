package com.deviget.challenge.minesweeper.test.integration;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.mongodb.MongoClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class GameIntegrationTest {
	private final Logger log = LoggerFactory.getLogger(GameIntegrationTest.class);

	@Autowired
    private TestRestTemplate restTemplate;
	
	@Autowired
	private MongoClient mongoClient;
	
	@Value("${spring.data.mongodb.database}")
	private String databaseName;

	@Test
	public void test() {
//		this.mongoClient.getDatabase(this.databaseName).drop();

		log.info("Checking full game");
//		ResponseEntity<GameResponse> response = this.restTemplate.getForEntity("/game", GameResponse.class);
//		Assert.assertEquals("Endpoint response was not status 200!", response.getStatusCode(), HttpStatus.OK);

	}	

}
