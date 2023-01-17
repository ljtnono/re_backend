package cn.lingjiatong.re.service.article.config;

import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.constant.ProfileEnum;
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
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.*;

/**
 * bean对象注入配置
 *
 * @author Ling, Jiatong
 * Date: 2022/10/17 22:38
 */
@Configuration
public class SpringBeanConfig {

    @Autowired
    private SwaggerProperties swaggerProperties;
    @Value("${spring.elasticsearch.rest.uris}")
    private String elasticsearchUri;
    @Value("${spring.elasticsearch.rest.port}")
    private Integer elasticsearchPort;
    @Value("${spring.profiles.active}")
    private String profile;

    @Bean
    public RestHighLevelClient elasticsearchClient() {
        // 设置elasticsearch
        List<HttpHost> httpHostsList = new ArrayList<>();
        httpHostsList.add(new HttpHost(elasticsearchUri, elasticsearchPort));
        HttpHost[] httpHostsArray = new HttpHost[httpHostsList.size()];
        httpHostsArray = httpHostsList.toArray(httpHostsArray);
        RestClientBuilder builder = RestClient.builder(httpHostsArray);
        // 设置5分钟保持活跃
        builder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setKeepAliveStrategy((response, context) -> Duration.ofMinutes(5).toMillis()));
        return new RestHighLevelClient(builder);
    }

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
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问地址：http://项目实际地址/swagger-ui.html
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
     * 配置 Jackson2JsonRedisSerializer 序列化器，在配置 redisTemplate需要用来做k,v的
     * 序列化器
     */
    private Jackson2JsonRedisSerializer<Object> getJackson2JsonRedisSerializer(){
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance ,
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        // 处理redis不能序列化LocalDate和LocalDateTime的问题
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        om.registerModule(javaTimeModule);

        //设置时区
        om.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai")));
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }

    @Bean(name = "defaultRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = getJackson2JsonRedisSerializer();

        // 使用 String 作为 Key 的序列化器,使用 Jackson 作为 Value 的序列化器
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
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
        // 返回可用处理器的Java虚拟机的数量
        int processNum = Runtime.getRuntime().availableProcessors();
        // 核心池大小
        int corePoolSize = (int) (processNum / (1 - 0.2));
        // 最大线程数
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


    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws IOException {
        ClassPathResource resource;
        // 本例子使用的是yaml格式的配置文件，读取使用Config.fromYAML，如果是Json文件，则使用Config.fromJSON
        if (ProfileEnum.PRD.getName().equalsIgnoreCase(profile)) {
            resource = new ClassPathResource("redission/redission-prd.yml");
        } else {
            resource = new ClassPathResource("redission/redission-dev.yml");
        }
        Config config = Config.fromYAML(resource.getInputStream());
        return Redisson.create(config);
    }

}
