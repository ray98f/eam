package com.wzmtr.eam.service.common;

import com.wzmtr.eam.constant.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Value("${pro.name}")
    public String proName;

    @Value("${spring.redis.key-prefix}")
    public String keyPrefix;

    public String KEY_PREFIX_VALUE = "nature:value:";
    public String KEY_PREFIX_SET = "nature:set:";
    public String KEY_PREFIX_LIST = "nature:list:";
    public String KEY_PREFIX_MAP = "nature:map:";

    public RedisTemplate<String, String> redisTemplate;

    /**
     * 缓存value操作
     *
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean cacheValue(String k, String v, long time) {
        String key = proName + keyPrefix + KEY_PREFIX_VALUE + k;
        try {
            ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
            valueOps.set(key, v);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + key + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    /**
     * 缓存value操作
     *
     * @param k
     * @param v
     * @return
     */
    public boolean cacheValue(String k, String v) {
        return cacheValue(k, v, -1);
    }

    /**
     * 判断缓存是否存在
     *
     * @param k
     * @return
     */
    public boolean containsValueKey(String k) {
        return containsKey(proName + keyPrefix + KEY_PREFIX_VALUE + k);
    }

    /**
     * 判断缓存是否存在
     *
     * @param k
     * @return
     */
    public boolean containsSetKey(String k) {
        return containsKey(proName + keyPrefix + KEY_PREFIX_SET + k);
    }

    /**
     * 判断缓存是否存在
     *
     * @param k
     * @return
     */
    public boolean containsListKey(String k) {
        return containsKey(proName + keyPrefix + KEY_PREFIX_LIST + k);
    }

    public boolean containsKey(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Throwable t) {
            logger.error("判断缓存存在失败key[" + key + ", error[" + t + "]");
        }
        return false;
    }

    /**
     * 获取缓存
     *
     * @param k
     * @return
     */
    public String getValue(String k) {
        try {
            ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
            String value = valueOps.get(proName + keyPrefix + KEY_PREFIX_VALUE + k);
            if (!Objects.isNull(value)) {
                if (value.contains(CommonConstants.DOUBLE_QUOTATION_MARKS)) {
                    // 去掉之前jaskson序列化多余产生的空格
                    return value.replaceAll("\"", "");
                }
            }
            return value;
        } catch (Throwable t) {
            logger.error("获取缓存失败key[" + proName + keyPrefix + KEY_PREFIX_VALUE + k + ", error[" + t + "]");
        }
        return null;
    }

    /**
     * 移除缓存
     *
     * @param k
     * @return
     */
    public boolean removeValue(String k) {
        return remove(proName + keyPrefix + KEY_PREFIX_VALUE + k);
    }

    public boolean removeSet(String k) {
        return remove(proName + keyPrefix + KEY_PREFIX_SET + k);
    }

    public boolean removeList(String k) {
        return remove(proName + keyPrefix + KEY_PREFIX_LIST + k);
    }

    /**
     * 移除缓存
     *
     * @param key
     * @return
     */
    public boolean remove(String key) {
        try {
            redisTemplate.delete(key);
            return true;
        } catch (Throwable t) {
            logger.error("获取缓存失败key[" + key + ", error[" + t + "]");
        }
        return false;
    }

    /**
     * 缓存set操作
     *
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean cacheSet(String k, String v, long time) {
        String key = proName + keyPrefix + KEY_PREFIX_SET + k;
        try {
            SetOperations<String, String> valueOps = redisTemplate.opsForSet();
            valueOps.add(key, v);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + key + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    /**
     * 缓存set
     *
     * @param k
     * @param v
     * @return
     */
    public boolean cacheSet(String k, String v) {
        return cacheSet(k, v, -1);
    }

    /**
     * 缓存set
     *
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean cacheSet(String k, Set<String> v, long time) {
        String key = proName + keyPrefix + KEY_PREFIX_SET + k;
        try {
            SetOperations<String, String> setOps = redisTemplate.opsForSet();
            setOps.add(key, v.toArray(new String[v.size()]));
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + key + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    /**
     * 缓存set
     *
     * @param k
     * @param v
     * @return
     */
    public boolean cacheSet(String k, Set<String> v) {
        return cacheSet(k, v, -1);
    }

    /**
     * 获取缓存set数据
     *
     * @param k
     * @return
     */
    public Set<String> getSet(String k) {
        try {
            SetOperations<String, String> setOps = redisTemplate.opsForSet();
            return setOps.members(proName + keyPrefix + KEY_PREFIX_SET + k);
        } catch (Throwable t) {
            logger.error("获取set缓存失败key[" + proName + keyPrefix + KEY_PREFIX_SET + k + ", error[" + t + "]");
        }
        return null;
    }

    /**
     * list缓存
     *
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean cacheList(String k, String v, long time) {
        String key = proName + keyPrefix + KEY_PREFIX_LIST + k;
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            listOps.rightPush(key, v);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + key + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    /**
     * 缓存list
     *
     * @param k
     * @param v
     * @return
     */
    public boolean cacheList(String k, String v) {
        return cacheList(k, v, -1);
    }

    /**
     * 缓存list
     *
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean cacheList(String k, List<String> v, long time) {
        String key = proName + keyPrefix + KEY_PREFIX_LIST + k;
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            listOps.rightPushAll(key, v);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + key + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    /**
     * 缓存list
     *
     * @param k
     * @param v
     * @return
     */
    public boolean cacheList(String k, List<String> v) {
        return cacheList(k, v, -1);
    }

    /**
     * 获取list缓存
     *
     * @param k
     * @param start
     * @param end
     * @return
     */
    public List<String> getList(String k, long start, long end) {
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            return listOps.range(proName + keyPrefix + KEY_PREFIX_LIST + k, start, end);
        } catch (Throwable t) {
            logger.error("获取list缓存失败key[" + proName + keyPrefix + KEY_PREFIX_LIST + k + ", error[" + t + "]");
        }
        return null;
    }

    /**
     * 获取总条数, 可用于分页
     *
     * @param k
     * @return
     */
    public long getListSize(String k) {
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            return listOps.size(proName + keyPrefix + KEY_PREFIX_LIST + k);
        } catch (Throwable t) {
            logger.error("获取list长度失败key[" + proName + keyPrefix + KEY_PREFIX_LIST + k + "], error[" + t + "]");
        }
        return 0;
    }

    /**
     * 获取总条数, 可用于分页
     *
     * @param listOps
     * @param k
     * @return
     */
    public long getListSize(ListOperations<String, String> listOps, String k) {
        try {
            return listOps.size(proName + keyPrefix + KEY_PREFIX_LIST + k);
        } catch (Throwable t) {
            logger.error("获取list长度失败key[" + proName + keyPrefix + KEY_PREFIX_LIST + k + "], error[" + t + "]");
        }
        return 0;
    }

    /**
     * 移除list缓存
     *
     * @param k
     * @return
     */
    public boolean removeOneOfList(String k) {
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            listOps.rightPop(proName + keyPrefix + KEY_PREFIX_LIST + k);
            return true;
        } catch (Throwable t) {
            logger.error("移除list缓存失败key[" + proName + keyPrefix + KEY_PREFIX_LIST + k + ", error[" + t + "]");
        }
        return false;
    }

    /**
     * 将hashKey hashValue存放在map中
     *
     * @param k
     * @param hashKey
     * @param value
     * @param time
     * @return
     */
    public boolean cacheMap(String k, String hashKey, String value, long time) {
        String key = proName + keyPrefix + KEY_PREFIX_MAP + k;
        try {
            HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
            hashOps.put(key, hashKey, value);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + key + "]失败, hashKey[" + hashKey + "] value[" + value + "]", t);
        }
        return false;
    }

    /**
     * 将Map存放在缓存
     *
     * @param k
     * @param map
     * @param time
     * @return
     */
    public boolean cacheMap(String k, Map<String, String> map, long time) {
        String key = proName + keyPrefix + KEY_PREFIX_MAP + k;
        try {
            BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(k);
            hashOps.putAll(map);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + key + "]失败, map[" + map + "] ", t);
        }
        return false;
    }

    /**
     * 根据Map中的hashKey获取value
     *
     * @param k
     * @param hk
     * @return
     */
    public String getMapValue(String k, String hk) {
        String key = proName + keyPrefix + KEY_PREFIX_MAP + k;
        try {
            HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
            return hashOps.get(key, hk);
        } catch (Throwable t) {
            logger.error("获取map缓存失败key[" + key + ", error[" + t + "]");
        }
        return null;
    }

    /**
     * 获得Map
     *
     * @param k
     * @return
     */
    public Map<String, String> getMap(String k) {
        String key = proName + keyPrefix + KEY_PREFIX_MAP + k;
        try {
            BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(k);
            return hashOps.entries();
        } catch (Throwable t) {
            logger.error("获取map缓存失败key[" + key + ", error[" + t + "]");
        }
        return null;
    }

    public RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

}
