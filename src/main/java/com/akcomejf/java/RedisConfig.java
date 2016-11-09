package com.akcomejf.java;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisClusterConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

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

	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}
	
	@Bean
	JedisClusterConnection clusterConnection(){
		HostAndPort ip = new HostAndPort("192.168.82.252", 7001);
		JedisCluster node = new JedisCluster(ip);
		JedisClusterConnection cluster = new JedisClusterConnection(node);
		return cluster;
	}
	
//	@Bean
//	JedisConnectionFactory factory(){
//		JedisConnectionFactory factory = new JedisConnectionFactory();
//		factory.setHostName("192.168.82.252");
//		factory.setPort(7001);
//		return factory;
//	}

}
