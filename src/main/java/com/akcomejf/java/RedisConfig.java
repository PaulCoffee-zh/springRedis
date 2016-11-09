package com.akcomejf.java;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {
	@Bean
	RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {

		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(listenerAdapter, new PatternTopic("chat"));

		return container;
	}

	/**
	 * 消息监听适配器
	 * @param receiver 委托对象（指定执行方法）
	 * @return
	 */
	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}
	
//	@Bean
//	JedisClusterConnection clusterConnection(){
//		HostAndPort ip = new HostAndPort("192.168.82.252", 7001);
//		JedisCluster node = new JedisCluster(ip);
//		JedisClusterConnection cluster = new JedisClusterConnection(node);
//		return cluster;
//	}
	
	@Bean
	JedisConnectionFactory factory(){
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName("192.168.82.39");
		factory.setPort(6379);
		return factory;
	}

	@Bean
	StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}
	
}
