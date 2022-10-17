package cn.lingjiatong.re.common.util;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis操作工具类
 *
 * @author Ling, Jiatong
 * Date: 2020/7/15 14:54 下午
 *
 * 基于Spring和redisTemplate工具类
 * 针对所有的hash 都是以h开头的方法
 * 针对所有的set 都是以s开头的方法 不含通用方法
 * 针对所有的List 都是以l开头的方法
 * 为避免不必要的错误，key值都设置了不能为空串
 */
public class RedisUtil {

    private RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 锁超时时间默认值 30s
     */
    private final static Long DEFAULT_LOCK_EXPIRE = 30000L;

    /**
     * 锁阻塞时，线程睡眠时间
     */
    private final static Long DEFAULT_LOCK_WAITING = 3000L;

    /**
     * 默认锁值
     */
    private final static String DEFAULT_LOCK_VALUE = "LOCKED";

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public void setCacheObject(final String key, final Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public void setCacheObject(final String key, final Object value, final Integer timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 获得缓存的基本对象
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public Object getCacheObject(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 根据正则表达式获取key
     *
     * @param pattern 正则表达式
     * @return 匹配到的key
     */
    public Set<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 删除单个对象
     *
     * @param key 缓存键值
     */
    public Boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
    }

    /**
     * setIfAbsent
     *
     * @param key       lock key
     * @param value     value
     * @param timeout   timeout
     * @param unit  	time unit
     * @return 设置成功返回true，设置失败返回false
     * @author Ling,Jiatong
     */
    public Boolean setIfAbsent(String key, String value, long timeout, TimeUnit unit) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
    }

    /**
     * 获取hash中的数据
     *
     * @param hashKey hash表的大key
     * @param key hash元素的key
     * @return 数据对象
     * @author Ling, Jiatong
     */
    public Object hGet(String hashKey, String key) {
        return redisTemplate.opsForHash().get(hashKey, key);
    }

    /**
     * 阻塞式获取锁
     *
     * @param key 锁的键
     * @author Ling,Jiatong
     */
    public void lock(String key) throws InterruptedException {
        // 如果已经有线程持有锁了，就先睡眠 3 秒，再次尝试获取锁
        while (!setIfAbsent(key, DEFAULT_LOCK_VALUE, DEFAULT_LOCK_EXPIRE, TimeUnit.MILLISECONDS)) {
            Thread.sleep(DEFAULT_LOCK_WAITING);
        }
    }

    /**
     * 释放锁
     *
     * @param key 锁的键
     * @author Ling, Jiatong
     */
    public void unLock(String key) {
        redisTemplate.delete(key);
    }
}
