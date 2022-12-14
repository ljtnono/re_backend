package cn.lingjiatong.re.service.article.config;

import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.util.RedisUtil;
import cn.lingjiatong.re.common.util.SnowflakeIdWorkerUtil;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import java.util.concurrent.*;

/**
 * bean??????????????????
 *
 * @author Ling, Jiatong
 * Date: 2022/10/17 22:38
 */
@Configuration
public class SpringBeanConfig {

    @Autowired
    private SwaggerProperties swaggerProperties;

    @Bean
    public OpenAPI docket() {
        Components components = new Components();
        components.addSecuritySchemes(CommonConstant.TOKE_HTTP_HEADER, new io.swagger.v3.oas.models.security.SecurityScheme().name(CommonConstant.TOKE_HTTP_HEADER).type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"));
        return new OpenAPI()
                .components(components)
                .addSecurityItem(new SecurityRequirement().addList(CommonConstant.TOKE_HTTP_HEADER))
                .info(info());
    }

    /**
     * ?????????API??????????????????????????????????????????????????????????????????
     * ???????????????http://??????????????????/swagger-ui.html
     *
     * @return Info
     */
    private Info info() {
        io.swagger.v3.oas.models.info.Contact contact = new Contact();
        contact.setName("lingjiatong");
        contact.setEmail("935188400@qq.com");
        return new Info()
                .title(swaggerProperties.getApplicationName())
                .description(swaggerProperties.getApplicationDescription())
                .contact(contact)
                .version("Application Version: " + swaggerProperties.getApplicationVersion() + ", Spring Boot Version: " + SpringBootVersion.getVersion());
    }
    @Bean
    @Lazy
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setOptimizeJoin(true);
        paginationInnerInterceptor.setOverflow(true);
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);
        return mybatisPlusInterceptor;
    }


    /**
     * ?????? Jackson2JsonRedisSerializer ???????????????????????? redisTemplate???????????????k,v???
     * ????????????
     */
    private Jackson2JsonRedisSerializer<Object> getJackson2JsonRedisSerializer(){
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance ,
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        // ??????redis???????????????LocalDate???LocalDateTime?????????
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        om.registerModule(javaTimeModule);

        //????????????
        om.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai")));
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }

    @Bean(name = "defaultRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = getJackson2JsonRedisSerializer();

        // ?????? String ?????? Key ???????????????,?????? Jackson ?????? Value ???????????????
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key??????String??????????????????
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // hash???key?????????String??????????????????
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // value?????????????????????jackson
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        // hash???value?????????????????????jackson
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    @Bean
    public RedisUtil redisUtil(@Autowired RedisTemplate<String, Object> redisTemplate) {
        RedisUtil redisUtil = new RedisUtil(redisTemplate);
        return redisUtil;
    }

    @Bean
    public SnowflakeIdWorkerUtil snowflakeIdWorkerUtil() {
        return new SnowflakeIdWorkerUtil();
    }

    @Bean(name="commonThreadPool")
    public ExecutorService commonThreadPool(){
        // ????????????????????????Java??????????????????
        int processNum = Runtime.getRuntime().availableProcessors();
        // ???????????????
        int corePoolSize = (int) (processNum / (1 - 0.2));
        // ???????????????
        int maxPoolSize = (int) (processNum / (1 - 0.5));
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                300,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(maxPoolSize * 1000),
                threadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy());
        return executorService;
    }

}
