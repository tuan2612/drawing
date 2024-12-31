package com.huce.project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huce.project.dto.CRUDBlogDTO;
import com.huce.project.dto.ListBlogDTO;
import com.huce.project.dto.ResponseAPIDTO;
import com.huce.project.entity.BlogEntity;
import com.huce.project.service.BlogService;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/blogs")
public class BlogController {
    private final BlogService blogService;
    private final ModelMapper modelMapper;

    @GetMapping("{id}")
    public ResponseEntity<ResponseAPIDTO<BlogEntity>> getBlogById(@PathVariable int id) {
        try {
            BlogEntity blog = blogService.getBlogById(id);

            if (blog == null) {
                ResponseAPIDTO<BlogEntity> response = ResponseAPIDTO.<BlogEntity>builder()
                        .code(404)
                        .message("Blog not found")
                        .result(null)
                        .build();
                return ResponseEntity.status(404).body(response);
            }

            ResponseAPIDTO<BlogEntity> response = ResponseAPIDTO.<BlogEntity>builder()
                    .result(blog)
                    .code(200)
                    .message("Blog retrieved successfully")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(ResponseAPIDTO.<BlogEntity>builder()
                            .code(500)
                            .message("An error occurred: " + e.getMessage())
                            .result(null)
                            .build());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("addBlog")
    public ResponseEntity<ResponseAPIDTO<BlogEntity>> addBlog(@RequestBody CRUDBlogDTO form) {
        try {
            BlogEntity blog = modelMapper.map(form, BlogEntity.class);
            blogService.addBlog(blog);

            ResponseAPIDTO<BlogEntity> response = ResponseAPIDTO.<BlogEntity>builder()
                    .result(blog)
                    .code(201)
                    .message("Blog added successfully")
                    .build();

            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(ResponseAPIDTO.<BlogEntity>builder()
                            .code(500)
                            .message("An error occurred: " + e.getMessage())
                            .result(null)
                            .build());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("updateBlog/{id}")
    public ResponseEntity<ResponseAPIDTO<Object>> updateBlog(@PathVariable int id, @RequestBody CRUDBlogDTO form) {
        try {
            BlogEntity blog = blogService.getBlogById(id);
            if (blog == null) {
                ResponseAPIDTO<Object> response = ResponseAPIDTO.<Object>builder()
                        .code(404)
                        .message("Blog not found")
                        .result(null)
                        .build();
                return ResponseEntity.status(404).body(response);
            }

            BlogEntity updatedBlog = modelMapper.map(form, BlogEntity.class);
            blogService.updateBlog(id, updatedBlog);

            ResponseAPIDTO<Object> response = ResponseAPIDTO.<Object>builder()
                    .code(200)
                    .message("Update successful")
                    .result(null)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(ResponseAPIDTO.<Object>builder()
                            .code(500)
                            .message("An error occurred: " + e.getMessage())
                            .result(null)
                            .build());
        }
    }

    @DeleteMapping("deleteBlog/{id}")
    public ResponseEntity<ResponseAPIDTO<Object>> deleteBlog(@PathVariable int id) {
        try {
            BlogEntity blog = blogService.getBlogById(id);
            if (blog == null) {
                ResponseAPIDTO<Object> response = ResponseAPIDTO.<Object>builder()
                        .code(404)
                        .message("Blog not found")
                        .result(null)
                        .build();
                return ResponseEntity.status(404).body(response);
            }

            blogService.deleteBlog(id);

            ResponseAPIDTO<Object> response = ResponseAPIDTO.<Object>builder()
                    .code(200)
                    .message("Delete successful")
                    .result(null)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(ResponseAPIDTO.<Object>builder()
                            .code(500)
                            .message("An error occurred: " + e.getMessage())
                            .result(null)
                            .build());
        }
    }

    @GetMapping("blogList")
    public ResponseEntity<ResponseAPIDTO<Page<ListBlogDTO>>> getAllBlogEntity(Pageable pageable) {
        try {
            Page<BlogEntity> blogPage = blogService.findAll(pageable);
            Page<ListBlogDTO> dtoPage = blogPage.map(blog -> modelMapper.map(blog, ListBlogDTO.class));

            ResponseAPIDTO<Page<ListBlogDTO>> response = ResponseAPIDTO.<Page<ListBlogDTO>>builder()
                    .code(200)
                    .message("Blogs retrieved successfully")
                    .result(dtoPage)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(ResponseAPIDTO.<Page<ListBlogDTO>>builder()
                            .code(500)
                            .message("An error occurred: " + e.getMessage())
                            .result(null)
                            .build());
        }
    }
}
