import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author: wjl
 * @time: 2022/4/11 20:24
 * @version: 1.0
 */
@Component
public class RedisUtil {

   @Resource
   private RedisTemplate<String, Object> redisTemplate;

   // ============================== common ==================================

   /**
    * set expire time
    *
    * @param key  key
    * @param time time
    * @return state
    */
   public boolean expire(String key, long time) {
       try {
           if (time > 0) {
               redisTemplate.expire(key, time, TimeUnit.SECONDS);
           }
           return true;
       } catch (Exception e) {
           e.printStackTrace();
           return false;
       }
   }

   /**
    * get expire time
    *
    * @param key key
    * @return time
    */
   public Long getExpire(String key) {
       if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
           return redisTemplate.getExpire(key, TimeUnit.SECONDS);
       }
       return 0L;
   }

   /**
    * key if exists
    *
    * @param key key
    * @return state
    */
   public Boolean hasKey(String key) {
       try {
           return redisTemplate.hasKey(key);
       } catch (Exception e) {
           e.printStackTrace();
           return false;
       }
   }

   /**
    * delete cache
    *
    * @param key key
    */
   @SuppressWarnings("unchecked")
   public void del(String... key) {
       if (key != null && key.length > 0) {
           if (key.length == 1) {
               redisTemplate.delete(key[0]);
           } else {
               redisTemplate.delete((Collection<String>)
                       CollectionUtils.arrayToList(key));
           }
       }
   }

   // ============================== string ==================================

   /**
    * get value by key
    *
    * @param key key
    * @return value
    */
   public Object get(String key) {
       return key == null ? null : redisTemplate.opsForValue().get(key);
   }

   /**
    * set data
    *
    * @param key   key
    * @param value value
    * @return state
    */
   public boolean set(String key, Object value) {
       try {
           redisTemplate.opsForValue().set(key, value);
           return true;
       } catch (Exception e) {
           e.printStackTrace();
           return false;
       }
   }

   /**
    * set data and time
    *
    * @param key   key
    * @param value value
    * @param time  time
    * @return state
    */
   public boolean set(String key, Object value, long time) {
       try {
           if (time > 0) {
               redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
           } else {
               set(key, value);
           }
           return true;
       } catch (Exception e) {
           e.printStackTrace();
           return false;
       }
   }

   /**
    * increase
    *
    * @param key   key
    * @param delta delta > 0
    * @return long
    */
   public Long incr(String key, long delta) {
       if (delta < 0) {
           throw new RuntimeException("delta must bigger than 0");
       }
       return redisTemplate.opsForValue().increment(key, delta);
   }

   /**
    * decrease
    *
    * @param key   key
    * @param delta delta > 0
    * @return long
    */
   public Long decr(String key, long delta) {
       if (delta < 0) {
           throw new RuntimeException("delta must bigger than 0");
       }
       return redisTemplate.opsForValue().increment(key, -delta);
   }

   // ================================ map ===================================

   /**
    * HashGet
    *
    * @param key  key - Notnull
    * @param item item - NotNull
    * @return data
    */
   public Object hget(String key, String item) {
       return redisTemplate.opsForHash().get(key, item);
   }

   /**
    * get keys of hashKey
    *
    * @param key key
    * @return map
    */
   public Map<Object, Object> hmget(String key) {
       return redisTemplate.opsForHash().entries(key);
   }

   /**
    * HashSet
    *
    * @param key key
    * @param map map
    * @return state
    */
   public boolean hmset(String key, Map<String, Object> map) {
       try {
           redisTemplate.opsForHash().putAll(key, map);
           return true;
       } catch (Exception e) {
           e.printStackTrace();
           return false;
       }
   }

   /**
    * HashSet and time
    *
    * @param key  key
    * @param map  map
    * @param time time
    * @return state
    */
   public boolean hmset(String key, Map<String, Object> map, long time) {
       try {
           redisTemplate.opsForHash().putAll(key, map);
           if (time > 0) {
               expire(key, time);
           }
           return true;
       } catch (Exception e) {
           e.printStackTrace();
           return false;
       }
   }

   /**
    * set data into a hash table
    *
    * @param key   key
    * @param item  item
    * @param value value
    * @return state
    */
   public boolean hset(String key, String item, Object value) {
       try {
           redisTemplate.opsForHash().put(key, item, value);
           return true;
       } catch (Exception e) {
           e.printStackTrace();
           return false;
       }
   }

   /**
    * set data into a hash table
    *
    * @param key   key
    * @param item  item
    * @param value value
    * @param time  time
    * @return state
    */
   public boolean hset(String key, String item, Object value, long time) {
       try {
           redisTemplate.opsForHash().put(key, item, value);
           if (time > 0) {
               expire(key, time);
           }
           return true;
       } catch (Exception e) {
           e.printStackTrace();
           return false;
       }
   }

   /**
    * delete value in hash table
    *
    * @param key  key
    * @param item item
    */
   public void hdel(String key, Object... item) {
       redisTemplate.opsForHash().delete(key, item);
   }

   /**
    * if value exist in hash table
    *
    * @param key  key
    * @param item item
    * @return state
    */
   public boolean hHasKey(String key, String item) {
       return redisTemplate.opsForHash().hasKey(key, item);
   }

   /**
    * hash increase
    *
    * @param key  key
    * @param item item
    * @param by   by
    * @return double
    */
   public double hincr(String key, String item, double by) {
       return redisTemplate.opsForHash().increment(key, item, by);
   }

   /**
    * hash decrease
    *
    * @param key  key
    * @param item item
    * @param by   by
    * @return double
    */
   public double hdecr(String key, String item, double by) {
       return redisTemplate.opsForHash().increment(key, item, -by);
   }

   // ================================ set ===================================

   /**
    * get value in set by key
    *
    * @param key key
    * @return set
    */
   public Set<Object> sGet(String key) {
       try {
           return redisTemplate.opsForSet().members(key);
       } catch (Exception e) {
           e.printStackTrace();
           return null;
       }
   }

   /**
    * query by value in set
    *
    * @param key   key
    * @param value value
    * @return state
    */
   public Boolean sHashKey(String key, Object value) {
       try {
           return redisTemplate.opsForSet().isMember(key, value);
       } catch (Exception e) {
           e.printStackTrace();
           return false;
       }
   }

   /**
    * set data into set
    *
    * @param key    key
    * @param values values
    * @return long
    */
   public Long sSet(String key, Object... values) {
       try {
           return redisTemplate.opsForSet().add(key, values);
       } catch (Exception e) {
           e.printStackTrace();
           return 0L;
       }
   }

   /**
    * set data into set
    *
    * @param key    key
    * @param time   time
    * @param values values
    * @return long
    */
   public Long sSetAndTime(String key, long time, Object... values) {
       try {
           Long count = redisTemplate.opsForSet().add(key, values);
           if (time > 0) {
               expire(key, time);
           }
           return count;
       } catch (Exception e) {
           e.printStackTrace();
           return 0L;
       }
   }

   /**
    * get length of set
    *
    * @param key key
    * @return length
    */
   public Long sGetSetSize(String key) {
       try {
           return redisTemplate.opsForSet().size(key);
       } catch (Exception e) {
           e.printStackTrace();
           return 0L;
       }
   }

   /**
    * remove key of value
    *
    * @param key    key
    * @param values values
    * @return long
    */
   public Long setRemove(String key, Object... values) {
       try {
           return redisTemplate.opsForSet().remove(key, values);
       } catch (Exception e) {
           e.printStackTrace();
           return 0L;
       }
   }

   /**
    * get data of list
    *
    * @param key   key
    * @param start start
    * @param end   end
    * @return list
    */
   public List<Object> lGet(String key, long start, long end) {
       try {
           return redisTemplate.opsForList().range(key, start, end);
       } catch (Exception e) {
           e.printStackTrace();
           return null;
       }
   }

   /**
    * get length of list
    *
    * @param key key
    * @return length
    */
   public Long lGetListSize(String key) {
       try {
           return redisTemplate.opsForList().size(key);
       } catch (Exception e) {
           e.printStackTrace();
           return 0L;
       }
   }

   /**
    * get value by key
    *
    * @param key   key
    * @param index index
    * @return object
    */
   public Object lGetIndex(String key, long index) {
       try {
           return redisTemplate.opsForList().index(key, index);
       } catch (Exception e) {
           e.printStackTrace();
           return null;
       }
   }

   /**
    * set list
    *
    * @param key   key
    * @param value value
    * @return state
    */
   public boolean lSet(String key, Object value) {
       try {
           redisTemplate.opsForList().rightPush(key, value);
           return true;
       } catch (Exception e) {
           e.printStackTrace();
           return false;
       }
   }

   /**
    * set list and time
    *
    * @param key   key
    * @param value value
    * @param time  time
    * @return state
    */
   public boolean lSet(String key, Object value, long time) {
       try {
           redisTemplate.opsForList().rightPush(key, value);
           if (time > 0) {
               expire(key, time);
           }
           return true;
       } catch (Exception e) {
           e.printStackTrace();
           return false;
       }
   }

   /**
    * set list
    *
    * @param key   key
    * @param value value
    * @return state
    */
   public boolean lSet(String key, List<Object> value) {
       try {
           redisTemplate.opsForList().rightPushAll(key, value);
           return true;
       } catch (Exception e) {
           e.printStackTrace();
           return false;
       }
   }

   /**
    * set list and time
    *
    * @param key   key
    * @param value value
    * @param time  time
    * @return state
    */
   public boolean lSet(String key, List<Object> value, long time) {
       try {
           redisTemplate.opsForList().rightPushAll(key, value);
           if (time > 0) {
               expire(key, time);
           }
           return true;
       } catch (Exception e) {
           e.printStackTrace();
           return false;
       }
   }

   /**
    * update data by index
    *
    * @param key   key
    * @param index index
    * @param value value
    * @return state
    */
   public boolean lUpdateIndex(String key, long index, Object value) {
       try {
           redisTemplate.opsForList().set(key, index, value);
           return true;
       } catch (Exception e) {
           e.printStackTrace();
           return false;
       }
   }

   /**
    * remove value
    *
    * @param key   key
    * @param count count
    * @param value value
    * @return long
    */
   public Long lRemove(String key, long count, Object value) {
       try {
           return redisTemplate.opsForList().remove(key, count, value);
       } catch (Exception e) {
           e.printStackTrace();
           return 0L;
       }
   }

}
