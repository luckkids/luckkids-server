package com.luckkids;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LuckKidsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuckKidsServerApplication.class, args);
    }

	@Bean
	public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
		return new JPAQueryFactory(entityManager);
	}
}
