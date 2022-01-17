package io.wisoft.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class JedisPipelineExample {

    private static final float TOTAL_OP = 10_000_000f;

    public static void main(final String... args) {
        final Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.connect();

        String key, value;
        long start = System.currentTimeMillis();

        final Pipeline p = jedis.pipelined();
        for (int i = 1; i <= TOTAL_OP; i++) {
            key = value = "key" + (10_000_000 + i);
            p.set(key, value);

            if (i % 1_000_000 == 0) {
                System.out.println(i / 100_000 + "% 완료.");
            }
        }
        p.sync();
        jedis.disconnect();

        long elapsed = System.currentTimeMillis() - start;
        System.out.println("초당 처리 건수 " + TOTAL_OP / elapsed * 1_000f);
        System.out.println("소요 시간 " + elapsed / 1_000f + "초");
    }

}
