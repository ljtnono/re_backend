package cn.lingjiatong.re.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * JSON转换工具类
 *
 * @author Wang, Haoyue
 * Date: 2020/7/17 9:44 上午
 */
@Slf4j
public class JSONUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // 反序列化时忽略对象中不存在的json字段
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 序列化时中文转unicode
        // SimpleModule simpleModule = new SimpleModule();
        // simpleModule.addSerializer(String.class, new StringUnicodeSerializer());
        // objectMapper.registerModule(simpleModule);
        // 序列化时忽略空值
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * 对象转string
     *
     * @param object 对象
     * @return string
     */
    public static <T> String objectToString(T object) {
        if (object == null) {
            return null;
        }
        try {
            return object instanceof String ? (String) object : OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            log.error(e.toString(), e);
            return null;
        }
    }

    /**
     * string转对象
     *
     * @param string string
     * @param clazz  对象
     * @return 对象
     */
    public static <T> T stringToObject(String string, Class<T> clazz) {
        if (StringUtils.isEmpty(string) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) string : OBJECT_MAPPER.readValue(string, clazz);
        } catch (Exception e) {
            log.error(e.toString(), e);
            return null;
        }
    }

    /**
     * string转List集合对象
     *
     * @param string        string
     * @param typeReference List集合类型
     * @return List集合对象
     */
    public static <T> T stringToObject(String string, TypeReference typeReference) {
        if (StringUtils.isEmpty(string) || typeReference == null) {
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(String.class) ? string : OBJECT_MAPPER.readValue(string, typeReference));
        } catch (Exception e) {
            log.error(e.toString(), e);
            return null;
        }
    }

    /**
     * string转List集合对象
     *
     * @param string          string
     * @param collectionClass List集合类型
     * @param elementClasses  集合元素类型
     * @return List集合对象
     */
    public static <T> T stringToObject(String string, Class<?> collectionClass, Class<?>... elementClasses) {
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        try {
            return OBJECT_MAPPER.readValue(string, javaType);
        } catch (Exception e) {
            log.error(e.toString(), e);
            return null;
        }
    }
}