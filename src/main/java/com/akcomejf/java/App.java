package com.akcomejf.java;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ClusterOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.types.RedisClientInfo;

@SpringBootApplication
public class App {

	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

	@Bean
	Receiver receiver(CountDownLatch latch) {
		return new Receiver(latch);
	}

	@Bean
	CountDownLatch latch() {
		return new CountDownLatch(1);
	}
	
	@Bean
	StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}

	public static void main(String[] args) throws InterruptedException {

		ApplicationContext ctx = SpringApplication.run(App.class, args);

		StringRedisTemplate template = ctx.getBean(StringRedisTemplate.class);
		CountDownLatch latch = ctx.getBean(CountDownLatch.class);

		LOGGER.info("Sending message...");
		template.convertAndSend("chat", "Hello from Redis!");
		List<RedisClientInfo> list = template.getClientList();
		ClusterOperations<String, String>  cluster = template.opsForCluster();
		ValueOperations<String, String> ops = template.opsForValue();
		ops.set("akcomejf", "1q2w3e4r");
		System.out.println(ops.get("akcomejf"));
		ops.append("akcomejf", "pl,okm");
		System.out.println(ops.get("akcomejf"));
		RedisClusterNode node = new RedisClusterNode("192.168.82.252",7001);
		Collection<RedisClusterNode>  clusters = cluster.getSlaves(node);
		latch.await();
		System.exit(0);
	}
}