package com.example.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
@AllArgsConstructor
public class BoardListResponseDto {

  Page<BoardListDto> boardList;

}
