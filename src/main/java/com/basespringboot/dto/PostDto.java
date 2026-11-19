package com.basespringboot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {
    private String content;
    private Long userId;
}