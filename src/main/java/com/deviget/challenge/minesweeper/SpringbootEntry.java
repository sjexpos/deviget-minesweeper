package com.deviget.challenge.minesweeper;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class SpringbootEntry {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringbootEntry.class, args);
    }

}
