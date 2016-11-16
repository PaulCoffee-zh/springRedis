package com.akcomejf.java.jedis;

import redis.clients.jedis.Jedis;

public class JedisPublish implements Runnable {
	private Jedis jedis;

	private String channel;

	public JedisPublish(Jedis jedis, String channel) {
		this.jedis = jedis;
		this.channel = channel;
	}

	@Override
	public void run() {
		for(int i = 0 ; i < 100; i++){
			jedis.publish(channel, "num:"+i);
		}
		
	}

}
