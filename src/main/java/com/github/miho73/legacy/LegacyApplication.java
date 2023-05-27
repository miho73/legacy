package com.github.miho73.legacy;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession
@EnableEncryptableProperties
public class LegacyApplication {

	public static void main(String[] args) {
		SpringApplication.run(LegacyApplication.class, args);
	}

}
