package com.example.redis.test;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.jedis.params.geo.GeoRadiusParam;

import java.util.*;


public class JedisClient {

    private static Logger logger = LoggerFactory.getLogger(JedisClient.class);
    private JedisPool jedisPool;
    public final static int TIME_MINUTE = 60;
    public final static int TIME_5_MINUTE = 300;
    public final static int TIME_10_MINUTE = 600;
    public final static int TIME_HOUR = 3600;
    public final static int TIME_DAY = 24 * 3600;
    public final static int TIME_WEEK = 7 * 24 * 3600;

    //获取当前时间

    //今日剩余时间
    public final static int timeLeast() {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DATE, 1);
        date.set(Calendar.HOUR_OF_DAY, 4);
        long endTime = date.getTimeInMillis();
        return (int) (endTime - System.currentTimeMillis()) / 1000;
    }


    public JedisClient() {
        if (jedisPool == null) {
            String ip = "60.205.247.163";
            int port = 6379;
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxIdle(8);
            jedisPoolConfig.setMaxWaitMillis(-1);
            jedisPool = new JedisPool(jedisPoolConfig, ip, port, 10000);
        }
    }

    public void setJSON(String key, Object o, int time) {
        if (o == null) {
            return;
        }
        String json = JSON.toJSONString(o);
        set(key, json, time);
    }

    public boolean exist(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(key);
        } catch (Exception e) {
            logger.info("", e);
        }
        return false;
    }

    public <T> T get(String key, Class<T> cl) {
        String json = get(key);
        try {
            return JSON.parseObject(json, cl);
        } catch (Exception e) {

        }
        return null;
    }

    public <T> List<T> getList(String key, Class<T> cl) {
        String json = get(key);
        try {
            return JSON.parseArray(json, cl);
        } catch (Exception e) {

        }
        return null;
    }


    //获取key的value值
    public String get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            logger.info("", e);
        }
        return null;
    }

    public void delete(String... keys) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(keys);
        } catch (Exception e) {
            logger.info("", e);
        }
    }

    public String set(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.set(key, value);
        } catch (Exception e) {
            logger.info("", e);
        }
        return null;
    }


    public String set(String key, String value, int seconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.setex(key, seconds, value);
        } catch (Exception e) {
            logger.info("", e);
        }
        return null;
    }

    public List<String> lrang(String key, long start, long end) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            logger.info("", e);
        }
        return null;
    }






    public void lpush(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.lpush(key, value);
        }
    }

    /**
     * 后进先出
     *
     * @param key
     * @return
     */
    public String lpop(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lpop(key);
        }
    }


    public Long incrBy(String key, int step) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.incrBy(key, step);
        }
    }

    /**
     * 先进先出
     *
     * @param key
     * @return
     */
    public String rpop(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.rpop(key);
        }
    }

    public Long hincrBy(String key, String field, int step) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hincrBy(key, field, step);
        }
    }

    public String hget(String key, String field) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hget(key, field);
        }
    }


    public Long hlen(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hlen(key);
        }
    }

    public Boolean hexists(String key, String field) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hexists(key, field);
        }
    }

    public void expire(String key, int second) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.expire(key, second);
        }
    }

    public void hmset(String key, Map<String, String> map) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hmset(key, map);
        }
    }

    public List<String> hmget(String key, String... field) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hmget(key, field);
        }
    }

    public void hset(String key, String field, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(key, field, value);
        }
    }

    public List<String> hvals(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hvals(key);
        }
    }

    public void hdel(String key, String field) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hdel(key, field);
        }
    }

    public void zadd(String key, String value, long score) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.zadd(key, score, value);
        }
    }

    public void sadd(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.sadd(key,value);
        }
    }
    public void srem(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.srem(key,value);
        }
    }

    public Set<String> smembers(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.smembers(key);
        }
    }


    /**
     * 有序集合的添加及更新
     *
     * @param key
     * @param value
     * @param score
     */
    public void zincrby(String key, String value, long score) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.zincrby(key, score, value);
        }
    }

    public synchronized String zpop(String key, long score) {
        try (Jedis jedis = jedisPool.getResource()) {
            Set<Tuple> tupleSet = jedis.zrangeWithScores(key, 0, 0);
            if (tupleSet.iterator().hasNext()) {
                Tuple tuple = tupleSet.iterator().next();
                if (score >= tuple.getScore()) {
                    jedis.zrem(key, tuple.getElement());
                    return tuple.getElement();
                }
            }
            return null;
        }
    }

    public Set<String> zRange(String key, long start, long end) {
        Set<String> stringSet = null;
        try (Jedis jedis = jedisPool.getResource()) {
            stringSet = jedis.zrange(key, start, end);
            return stringSet;
        }catch (Exception e){
            logger.error("zRangeByScore Exception: ",e);
        }
        return stringSet;
    }

    public Set<String> zRangeByScore(String key, long start, long end) {
        Set<String> stringSet = null;
        try (Jedis jedis = jedisPool.getResource()) {
            stringSet = jedis.zrangeByScore(key, start, end);
            return stringSet;
        }catch (Exception e){
            logger.error("zRangeByScore Exception: ",e);
        }
        return stringSet;
    }

    //返回有序集中，指定区间内的成员
    public Set<String> zRevRange(String key, long start, long end) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zrevrange(key, start, end);
        }
    }
    //score
    public Set<String> zrevrangebyscore(String key, long start, long end, int offset, int count) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zrevrangeByScore(key, start, end, offset, count);
        }
    }

    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zrevrangeWithScores(key, start, end);
        }
    }

    public Set<Tuple> zrangeWithScores(String key, long start, long end) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zrangeWithScores(key, start, end);
        }
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, long start, long end) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zrangeByScoreWithScores(key, start, end);
        }
    }

    public long zcount(String key, long start, long end) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zcount(key, start, end);
        }
    }

    public long zcard(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zcard(key);
        }
    }

    public long zRemoveRange(String key, long start, long end) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zremrangeByRank(key, start, end);
        }
    }

    public long zRemoveByScore(String key, long start, long end) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zremrangeByScore(key, start, end);
        }
    }

    public long zrem(String key, String... member) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zrem(key, member);
        }
    }

    public Set<String> keys(String keys) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.keys(keys);
        }
    }

    public void close() {
        try {
            jedisPool.close();

        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public void subscribe(JedisPubSub pubSub, String... channels) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.subscribe(pubSub, channels);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    public boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime) {
        try (Jedis jedis = jedisPool.getResource()) {

            String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);

            if (LOCK_SUCCESS.equals(result)) {
                return true;
            }
            return false;
        }

    }

    public boolean releaseDistributedLock(String lockKey, String requestId) {
        try (Jedis jedis = jedisPool.getResource()) {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));

            if (LOCK_SUCCESS.equals(result)) {
                return true;
            }
            return false;
        }
    }

    public Long llen(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.llen(key);
        }
    }

    public boolean zExist(String key, String member) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zrank(key, member) != null;
        }
    }

    public long incr(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.incr(key);
        }
    }

    public long decr(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.decr(key);
        }
    }

    //key : 城市 member：用户ID
    public List<GeoCoordinate> geopos(String key, String member){
        try (Jedis jedis = jedisPool.getResource()) {
            List<GeoCoordinate> list = jedis.geopos(key, member);
            return list;
        }
    }

    //key : 城市 member：用户ID
    public long geoadd(String key,Double longitude, Double latitude, String member){
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.geoadd(key,longitude, latitude ,member);
        }
    }

    //key : 城市 Long:经度 lat:纬度 radius：半径 count：条数
    public List<GeoRadiusResponse> georadius(String key,Double longitude, Double latitude, Double radius, int count){
        try (Jedis jedis = jedisPool.getResource()) {
            GeoRadiusParam param = GeoRadiusParam.geoRadiusParam().withDist().withCoord().count(count);

            return jedis.georadius(key, longitude, latitude, radius,GeoUnit.M,param);
        }
    }

    public double geodist(String key, long id, long otherId) {

        try (Jedis jedis = jedisPool.getResource()) {

            return jedis.geodist(key,String.valueOf(id),String.valueOf(otherId), GeoUnit.KM);
        }
    }
}