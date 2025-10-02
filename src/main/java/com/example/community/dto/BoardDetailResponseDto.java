package com.example.community.dto;

import com.example.community.entity.Board;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BoardDetailResponseDto {

  private Long id;
  private String title;
  private String content;
  private String nickName;
  private int views;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  // 댓글 계층 구조 추가
  private Page<CommentResponseDto> comments;

  // entity + 댓글 dto -> ResponseDto 변환
  public static BoardDetailResponseDto from (Board board, Page<CommentResponseDto> comments){

    return BoardDetailResponseDto.builder()
        .id(board.getId())
        .title(board.getTitle())
        .content(board.getContent())
        .nickName(board.getUser().getNickName())
        .views(board.getViews())
        .createdAt(board.getCreatedAt())
        .updatedAt(board.getUpdatedAt())
        .comments(comments)
        .build();

  }

}
