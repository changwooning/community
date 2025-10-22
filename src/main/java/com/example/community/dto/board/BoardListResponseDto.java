package com.example.community.dto.board;

import com.example.community.entity.Board;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BoardListResponseDto {

  private Long id;
  private String title;
  private String nickName;
  private int views;
  private LocalDateTime createdAt;

  // entity -> dto 변환
  public static BoardListResponseDto from(Board board) {

    return BoardListResponseDto.builder()
        .id(board.getId())
        .title(board.getTitle())
        .nickName(board.getUser().getNickName())
        .views(board.getViews())
        .createdAt(board.getCreatedAt())
        .build();
  }

}
