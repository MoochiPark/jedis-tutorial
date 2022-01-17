package io.wisoft.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

public class JedisPoolExample {

    public static void main(final String... args) {
        final GenericObjectPoolConfig<Jedis> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(20);
        config.setBlockWhenExhausted(true);

        final JedisPool pool = new JedisPool(config, "127.0.0.1", 6379);
        final Jedis firstClient = pool.getResource();
        firstClient.hset("info:daewon", "이름", "대원");
        firstClient.hset("info:daewon", "생일", "1994-05-20");

        final Jedis secondClient = pool.getResource();
        final Map<String, String> result = secondClient.hgetAll("info:daewon");
        System.out.println("이름: " + result.get("이름"));
        System.out.println("생일: " + result.get("생일"));

        firstClient.close();
        secondClient.close();
        pool.close();
    }

}
