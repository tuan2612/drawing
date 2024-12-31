// package com.huce.project.config;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
// import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
// import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.data.redis.serializer.GenericToStringSerializer;
// import org.springframework.data.redis.serializer.StringRedisSerializer;



// @Configuration
// public class RedisConfig {
//     @Value("${spring.redis.password}")
//     private String redisPassword;

//     @Bean
//     public LettuceConnectionFactory redisConnectionFactory() {
//         RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
//         redisConfig.setHostName("redis");
//         redisConfig.setPort(6379);
//         redisConfig.setPassword(redisPassword);
//         return new LettuceConnectionFactory(redisConfig);
//     }

//     @Bean
//     public RedisTemplate<String, Object> redisTemplate() {
//         RedisTemplate<String, Object> template = new RedisTemplate<>();
//         template.setConnectionFactory(redisConnectionFactory());
//         template.setKeySerializer(new StringRedisSerializer());
//         template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
//         return template;
//     }
// }
