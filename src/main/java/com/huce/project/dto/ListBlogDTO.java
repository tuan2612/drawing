package com.huce.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.huce.project.entity.AuthorType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ListBlogDTO {
    @JsonProperty("blog_id")
    private int blogId;
    private String title;
    private String content;
    private String sumary;
    @JsonProperty("blog_image")
    private String blogImage;
    @Builder.Default
    private AuthorType author = AuthorType.ADMIN;
}