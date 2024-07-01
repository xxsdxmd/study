package infr.diff;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.util.CollectionUtils;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xxs
 * @Date 2024/6/29 17:17
 */
public class ReflectionUtils {

    private static class ReflectionCache {
        private Field[] fields;
    }

    private static final ConcurrentHashMap<Class<?>, ReflectionCache> CACHE_MAP;


    static {
        CACHE_MAP = new ConcurrentHashMap<>();
    }


    static Field[] getFields(Class<?> entityClass) {
        ReflectionCache cache = getReflectionCache(entityClass);
        return cache.fields;
    }

    static ReflectionCache getReflectionCache(Class<?> entityClass) {
        ReflectionCache cache = CACHE_MAP.get(entityClass);
        if (Objects.isNull(cache)) {
            cache = new ReflectionCache();
            cache.fields = FieldUtils.getAllFields(entityClass);
            CACHE_MAP.put(entityClass, cache);
        }
        return cache;
    }

    static boolean isListItemAssignableTo(Class<?> targetClass, List<?> list) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        return targetClass.isAssignableFrom(list.get(0).getClass());
    }

    static public Object readField(Field field, Object target) {
        try {
            return FieldUtils.readField(field, target, true);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Fail to read Field" + field);
        } catch (Exception e) {
            throw e;
        }
    }
}
