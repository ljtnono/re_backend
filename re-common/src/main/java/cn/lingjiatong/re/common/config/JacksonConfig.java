package cn.lingjiatong.re.common.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Jackson 全局序列化配置（所有扫描了cn.lingjiatong.re.common的服务自动生效）
 *
 * 1. Long 序列化时仅当超出 JS 安全整数范围（2^53）才转为 String，
 *    避免雪花id（19位）在前端 JS Number 中精度丢失；分页 total 等小数字不受影响
 * 2. 统一 LocalDateTime / LocalDate 格式为 yyyy-MM-dd HH:mm:ss / yyyy-MM-dd
 *
 * @author Ling, Jiatong
 */
@Configuration
public class JacksonConfig {

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /** JS Number 最大安全整数 */
    private static final long JS_SAFE_LONG_MAX = (1L << 53) - 1;

    /**
     * Long 序列化器：超出 JS 安全整数范围转 String，否则按数字输出
     */
    private static final JsonSerializer<Long> LONG_SERIALIZER = new JsonSerializer<>() {
        @Override
        public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value != null && Math.abs(value) > JS_SAFE_LONG_MAX) {
                gen.writeString(value.toString());
            } else {
                gen.writeNumber(value);
            }
        }
    };

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> builder
                .serializerByType(Long.class, LONG_SERIALIZER)
                .serializerByType(Long.TYPE, LONG_SERIALIZER)
                .serializerByType(BigInteger.class, ToStringSerializer.instance)
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DATETIME_FORMATTER))
                .serializerByType(LocalDate.class, new LocalDateSerializer(DATE_FORMATTER))
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DATETIME_FORMATTER))
                .deserializerByType(LocalDate.class, new LocalDateDeserializer(DATE_FORMATTER));
    }
}
