package com.akcomejf.java;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

@SpringBootApplication
public class App {

	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) throws InterruptedException {
		// 获取上下文
		ApplicationContext ctx = SpringApplication.run(App.class, args);

		// 找到RedisTemplate
		StringRedisTemplate template = ctx.getBean(StringRedisTemplate.class);
		System.out.println("============================String 操作==================================");
		// String 操作
		ValueOperations<String, String> strOps = template.opsForValue();
		strOps.set("redisStr", "String类型数据");
		LOGGER.info("redis string set result : " + strOps.get("redisStr"));
		strOps.append("redisStr", "pl,okm");
		LOGGER.info("redis string get append result : " + strOps.get("redisStr"));

		System.out.println("============================List 操作==================================");
		// List 操作
		ListOperations<String, String> listOps = template.opsForList();
		listOps.leftPush("redisList", "List结构");
		listOps.leftPushAll("redisList", "apple", "ship", "rabbit");
		listOps.leftPush("redisList", "apple");
		LOGGER.info("redis List push result : " + listOps.range("redisList", 0, 12));
		LOGGER.info("redis List pop result : " + listOps.rightPop("redisList"));
		LOGGER.info("redis List after pop result : " + listOps.range("redisList", 0, 12));

		System.out.println("============================hash 操作==================================");
		// hash 操作
		HashOperations<String, Object, Object> hashOps = template.opsForHash();
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "leesun");
		map.put("job", "java engineer");
		map.put("age", "23");
		hashOps.putAll("redisMap", map);
		LOGGER.info("redis map putAll result : " + hashOps.values("redisMap"));

		System.out.println("============================set 操作==================================");
		SetOperations<String, String> setOps = template.opsForSet();
		setOps.add("redisSet", "dell", "lenovo", "hp", "华硕");
		setOps.add("redisSet", "小米");
		setOps.add("redisSet", "lenovo");
		LOGGER.info("redis set add result : " + setOps.members("redisSet"));

		System.out.println("============================zset 操作==================================");
		ZSetOperations<String, String> zsetOps = template.opsForZSet();
		zsetOps.add("redisZset", "China", 0.1);
		zsetOps.add("redisZset", "American", 0.13);
		zsetOps.add("redisZset", "Indea", 0.4);
		zsetOps.add("redisZset", "Krean", 0.3);
		zsetOps.add("redisZset", "Germany", 0.6);
		zsetOps.add("redisZset", "French", 0.7);
		LOGGER.info("redis set add result : " + zsetOps.range("redisZset", 0, 10));
		
		System.out.println("============================渠道信息发送==================================");
		// 渠道信息发送
		// 找到计数器
		CountDownLatch latch = ctx.getBean(CountDownLatch.class);
		LOGGER.info("Sending message...");
		// 发送通道消息
		template.convertAndSend("chat", "Hello from Redis!");

		latch.await();
		System.exit(0);
	}
}