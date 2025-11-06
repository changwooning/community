package com.example.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 연결 설정 클래스
 */
@Configuration
public class RedisConfig {

//  @Bean
//  public RedisConnectionFactory redisConnectionFactory(){
//    // docker-compose.yml 에서 정의한 서비스 이름 기준으로 연결
//    return new LettuceConnectionFactory("community-redis", 6379);
//  }

  // 로컬 환경용 Redis 설정
  @Bean
  @Profile("local")
  public RedisConnectionFactory localRedisConnectionFactory(){
    System.out.println("RedisConfig: local 환경 연결 시도 -> localhost:6379");
    return new LettuceConnectionFactory("localhost",6379);
  }

  // Docker 환경용 Redis 설정
  @Bean
  @Profile("docker")
  public RedisConnectionFactory dockerRedisConnectionFactory(){
    System.out.println("RedisConfig: docker 환경 연결 시도 -> community-redis:6379");
    return new LettuceConnectionFactory("community-redis",6379);
  }

  @Bean
  public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory){
    RedisTemplate<String, String> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    // 키와 값 모두 String 으로 변환
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());
    return template;
  }



}
