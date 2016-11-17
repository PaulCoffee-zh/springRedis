package com.akcomejf.java.jedis;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

public class JedisMain {

    static JedisPool pool;  
    static {  
         // 池基本配置   
        JedisPoolConfig config = new JedisPoolConfig();   
        config.setMaxIdle(5);   
        config.setMaxWaitMillis(1000l);    
        config.setTestOnBorrow(false);  
        pool = new JedisPool(config,"192.168.82.252",6379, 100000); //容忍的超时时间 
        // cluster链接    you cannot use transactions, pipelining, pub/sub, especially not across shards! 
        //参见：https://github.com/xetorthio/jedis/wiki/AdvancedUsage
//        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();   
//        shards.add(new JedisShardInfo("192.168.82.252", 7000));   
//        shards.add(new JedisShardInfo("192.168.82.252", 7001));   
//        shards.add(new JedisShardInfo("192.168.82.252", 7002));   
//        ShardedJedis jedis = new ShardedJedis(shards);
//        jedis.set("a", "foo");
//        jedis.disconnect();
        // 构造池   
    }  
    
	public static void main(String[] args) {
		Jedis jedis = new Jedis("192.168.82.252", 6379);
		// 集群模式
//		clusterOp();
		// 单机事务
//		redisTranstion(jedis);
		// 批量操作
		PipelineOp(jedis);
		//发布订阅监听
		//jedis的发布订阅不能在同一个上下文中，订阅会阻塞当前的线程
		Jedis jedis1 = pool.getResource();
		MyListener l = new MyListener();
		new Thread(new JedisPublish(jedis1, "foo")).start();
		jedis.subscribe(l, "foo");
	}

	public static void clusterOp() {
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
//		 Jedis Cluster will attempt to discover cluster nodes automatically
		jedisClusterNodes.add(new HostAndPort("192.168.82.252", 7000));
		 jedisClusterNodes.add(new HostAndPort("192.168.82.252", 7001));
		 jedisClusterNodes.add(new HostAndPort("192.168.82.252", 7002));
		 jedisClusterNodes.add(new HostAndPort("192.168.82.252", 7003));
		 jedisClusterNodes.add(new HostAndPort("192.168.82.252", 7004));
		 jedisClusterNodes.add(new HostAndPort("192.168.82.252", 7005));
		JedisCluster jc = new JedisCluster(jedisClusterNodes);
		jc.set("foo", "bar");
		String value = jc.get("foo");
		System.out.println(value);
	}

	public static void redisTranstion(Jedis jedis) {
		jedis.watch("foo", "foo1");
		Transaction t = jedis.multi();
		t.set("foo", "redisTranstion");
		new InterruptedThread().run();
		t.exec();
		System.out.println(jedis.get("foo"));

		Transaction t2 = jedis.multi();
		t2.set("AKCOME1", "bar");
		Response<String> result1 = t2.get("fool");

		t2.zadd("AKCOME2", 1, "barowitch");
		t2.zadd("AKCOME2", 0, "barinsky");
		t2.zadd("AKCOME2", 0, "barikoviev");
		Response<Set<String>> sose = t2.zrange("AKCOME2", 0, -1); // get the
																	// entiresortedset
		List<Object> allResults = t2.exec(); // you could still get all
		// t2.exec(); // dont forget it

		String foolbar = result1.get(); // use Response.get() to retrieve things
										// from a Response
		int soseSize = sose.get().size(); // on sose.get() you can directly call
											// Set methods!
		System.out.println(foolbar + "--" + soseSize);
	}

	public static void PipelineOp(Jedis jedis) {
		Pipeline p = jedis.pipelined();
		p.set("pipStr", "bar");
		p.zadd("pip", 1, "barowitch");
		p.zadd("pip", 0, "barinsky");
		p.zadd("pip", 0, "barikoviev");
		Response<String> pipeString = p.get("pipStr");
		Response<Set<String>> sose = p.zrange("pip", 0, -1);
		p.sync();

		int soseSize = sose.get().size();
		Set<String> setBack = sose.get();
		System.out.println("PipelineOp:"+pipeString+"--" + setBack);
	}
}

class InterruptedThread implements Runnable {
	Jedis jedis = new Jedis("192.168.82.252", 6379);

	public InterruptedThread() {
	}

	@Override
	public void run() {
		jedis.set("foo", "sxq");
	}

}
