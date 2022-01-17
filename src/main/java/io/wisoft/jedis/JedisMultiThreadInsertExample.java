package io.wisoft.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisMultiThreadInsertExample {

    private static final float TOTAL_OP = 10_000_000f;
    private static final int THREAD = 100;

    public static void main(final String... args) {
        final GenericObjectPoolConfig<Jedis> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(500);
        config.setBlockWhenExhausted(true);

        final JedisPool pool = new JedisPool(config, "127.0.0.1", 6379, 500);
        long start = System.currentTimeMillis();

        Runtime.getRuntime().addShutdownHook(
                new Thread(
                        () -> {
                            long elapsed = System.currentTimeMillis() - start;
                            System.out.println("스레드 개수: " + THREAD + "개");
                            System.out.println("초당 처리 건수 " + TOTAL_OP / elapsed * 1_000f);
                            System.out.println("소요 시간 " + elapsed / 1_000f + "초");
                        }
                ));

        JedisMultiThreadInsertExample test = new JedisMultiThreadInsertExample();
        for (int i = 0; i < THREAD; i++) {
            test.makeWorker(pool, i).start();
        }
    }

    private Thread makeWorker(final JedisPool pool, final int index) {
        return new Thread(
                () -> {
                    String key, value;
                    final Jedis jedis = pool.getResource();

                    for (int i = 1; i <= TOTAL_OP; i++) {
                        if (i % THREAD == index) {
                            key = value = "key" + (10_000_000 + i);
                            jedis.set(key, value);

                            if (i % 1_000_000 == 0) {
                                System.out.println(i / 100_000 + "% 완료.");
                            }
                        }
                    }
                    jedis.close();
                });
    }

}
