package com.example.community.dto;

import com.example.community.entity.Comment;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CommentResponseDto {

  private Long id;
  private String content;
  private String nickName;
  private LocalDateTime createdAt;
  private Long parentId;

  @Builder.Default
  private List<CommentResponseDto> children = new ArrayList<>();

  // entity -> dto 변환
  public static CommentResponseDto from(Comment comment) {

    return CommentResponseDto.builder()
        .id(comment.getId())
        .content(comment.getContent())
        .nickName(comment.getUser().getNickName())
        .createdAt(comment.getCreatedAt())
        .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
        .children(comment.getChildren().stream()
            .map(CommentResponseDto::from)
            .toList())
        .build();
  }

}
