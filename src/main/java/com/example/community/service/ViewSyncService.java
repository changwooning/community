package com.example.community.service;

import com.example.community.repository.BoardRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ViewSyncService {

  private final RedisTemplate<String, String> redisTemplate;
  private final BoardRepository boardRepository;

  @Scheduled(fixedRate = 60000) // 1분마다
  @Transactional
  public void syncViewCounts() {
    Set<String> keys = redisTemplate.keys("board:*:views");

    if (keys == null)
      return;

    for (String key : keys) {
      Long boardId = Long.valueOf(key.split(":")[1]);
      String count = redisTemplate.opsForValue().get(key);

      if (count == null)
        continue;

      int views = Integer.parseInt(count);
      boardRepository.updateViews(boardId, views);
    }
  }


}
