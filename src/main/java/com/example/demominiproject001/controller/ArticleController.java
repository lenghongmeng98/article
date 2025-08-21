package com.example.demominiproject001.controller;

import com.example.demominiproject001.model.enums.ArticleSortBy;
import com.example.demominiproject001.model.request.ArticleRequest;
import com.example.demominiproject001.model.request.CommentRequest;
import com.example.demominiproject001.model.response.ApiResponse;
import com.example.demominiproject001.model.response.ArticleDTO;
import com.example.demominiproject001.model.response.ArticleWithCommentDTO;
import com.example.demominiproject001.repository.CommentRepository;
import com.example.demominiproject001.service.ArticleService;
import com.example.demominiproject001.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/articles")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    @Operation(summary = "Get all articles. can be used by all roles")
    public ResponseEntity<ApiResponse<List<ArticleDTO>>> getAllArticles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") ArticleSortBy sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDir)
    {
        return ResponseEntity.ok(ApiResponse.success("Get all articles successfully.", articleService.getAllArticles(page, size, sortBy, sortDir)));
    }

    @GetMapping("/{articleId}")
    @Operation(summary = "Get article by article id. can be used by all roles")
    public ResponseEntity<ApiResponse<ArticleDTO>> getArticleById(@PathVariable Long articleId)
    {
        return ResponseEntity.ok(ApiResponse.success("Get article successfully.", articleService.getArticleById(articleId)));
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @PostMapping
    @Operation(summary = "Create new article. can be used by only AUTHOR role")
    public ResponseEntity<ApiResponse<ArticleDTO>> createArticle(@Valid @RequestBody ArticleRequest articleRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Article created successfully", articleService.createArticle(articleRequest)));
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @PutMapping("/{articleId}")
    @Operation(summary = "Update existing article. can be used by only AUTHOR role")
    public ResponseEntity<ApiResponse<ArticleDTO>> updateArticle(@PathVariable Long articleId, @Valid @RequestBody ArticleRequest articleRequest) {
    return ResponseEntity.ok(ApiResponse.success("Article updated successfully", articleService.updateArticle(articleId, articleRequest)));
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @DeleteMapping("/{articleId}")
    @Operation(summary = "Delete existing article. can be used by only AUTHOR role")
    public ResponseEntity<ApiResponse<ArticleDTO>> deleteArticle(@PathVariable Long articleId) {
        articleService.deleteArticle(articleId);
        return ResponseEntity.ok(ApiResponse.success("Deleted article successfully", null));
    }

    @PostMapping("/{articleId}/comment")
    @Operation(summary = "Comment on specific article, can be used to create comment on specific article by all roles")
    public ResponseEntity<ApiResponse<ArticleWithCommentDTO>> createCommentOnArticle(@PathVariable Long articleId, @Valid @RequestBody CommentRequest commentRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Create comment successfully", articleService.createCommentOnArticle(articleId, commentRequest)));
    }

    @GetMapping("/{articleId}/comments")
    @Operation(summary = "Get all comments by article id, can be used by all roles")
    public ResponseEntity<ApiResponse<ArticleWithCommentDTO>> getAllCommentsByArticleId(@PathVariable Long articleId) {
        return ResponseEntity.ok(ApiResponse.success("Get article with comments successfully", articleService.getAllCommentsByArticleId(articleId)));
    }
}
