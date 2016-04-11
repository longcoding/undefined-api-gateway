package com.longcoding.undefined.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by longcoding on 16. 4. 7..
 */
@Component
public class JedisFactory {

    @Autowired
    MessageManager messageManager;

    private static JedisPoolConfig jedisPoolConfig;
    private static JedisPool jedisPool;

    @PostConstruct
    private void initializeJedisPool() {
        jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(messageManager.getIntProperty("undefined.jedis.maxtotal"));
        jedisPoolConfig.setMaxWaitMillis(messageManager.getIntProperty("undefined.jedis.maxwaitmillis"));
        jedisPoolConfig.setMaxIdle(messageManager.getIntProperty("undefined.jedis.maxidle"));
        jedisPoolConfig.setMinIdle(messageManager.getIntProperty("undefined.jedis.minidle"));
        jedisPoolConfig.setNumTestsPerEvictionRun(messageManager.getIntProperty("undefined.jedis.numtestsperevictionrun"));
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(messageManager.getIntProperty("undefined.jedis.timebetweenevictionrunsmillis"));
        jedisPoolConfig.setBlockWhenExhausted(messageManager.getBooleanProperty("undefined.jedis.blockwhenexhausted"));
        jedisPoolConfig.setTestOnBorrow(messageManager.getBooleanProperty("undefined.jedis.testonborrow"));
        jedisPoolConfig.setTestOnReturn(messageManager.getBooleanProperty("undefined.jedis.testonreturn"));
        jedisPoolConfig.setTestWhileIdle(messageManager.getBooleanProperty("undefined.jedis.testwhileidle"));

        String jedisHost = messageManager.getProperty("undefined.jedis.host");
        int jedisPort = messageManager.getIntProperty("undefined.jedis.port");
        int jedisTimeout = messageManager.getIntProperty("undefined.jedis.timeout");

        jedisPool = new JedisPool(jedisPoolConfig, jedisHost, jedisPort, jedisTimeout);
    }

    public Jedis getInstance() {
        return jedisPool.getResource();

    }

    @PreDestroy
    private void releaseResource() {
        jedisPool.close();
    }

}