package com.example.demominiproject001.controller;

import com.example.demominiproject001.model.enums.ArticleSortBy;
import com.example.demominiproject001.model.response.ApiResponse;
import com.example.demominiproject001.model.response.ArticleDTO;
import com.example.demominiproject001.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/bookmarks")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping
    @Operation(summary = "Get all articles which has added bookmark by current user id")
    public ResponseEntity<ApiResponse<List<ArticleDTO>>> getAllBookmarkArticles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection
    ) {
        return ResponseEntity.ok(ApiResponse.success("Get all articles which has added bookmark successfully", bookmarkService.getAllBookmarkArticles(page, size, sortDirection)));
    }

    @PostMapping("/{articleId}")
    @Operation(summary = "Add bookmark on article")
    public ResponseEntity<ApiResponse<ArticleDTO>> addBookmark(@PathVariable Long articleId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Added bookmark successfully", bookmarkService.addBookmark(articleId)));
    }

    @DeleteMapping("/{articleId}")
    @Operation(summary = "Delete bookmark on article")
    public ResponseEntity<ApiResponse<ArticleDTO>> deleteBookmark(@PathVariable Long articleId) {
        bookmarkService.deleteBookmark(articleId);
        return ResponseEntity.ok(ApiResponse.success("Deleted bookmark successfully", null));
    }
}
