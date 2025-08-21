package com.example.demominiproject001.controller;

import com.example.demominiproject001.model.entity.Comment;
import com.example.demominiproject001.model.request.CommentRequest;
import com.example.demominiproject001.model.response.ApiResponse;
import com.example.demominiproject001.model.response.CommentDTO;
import com.example.demominiproject001.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/comments")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{commentId}")
    @Operation(summary = "Get comment by comment id, can view only your own comment")
    public ResponseEntity<ApiResponse<CommentDTO>> convertToCommentDTO(@PathVariable Long commentId) {
        return ResponseEntity.ok(ApiResponse.success("Get comment successfully", commentService.getCommentById(commentId)));
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "Update comment by comment id, can update only your own comment")
    public ResponseEntity<ApiResponse<CommentDTO>> updateCommentById(@PathVariable Long commentId, @RequestBody CommentRequest commentRequest) {
        return ResponseEntity.ok(ApiResponse.success("Updated comment successfully", commentService.updateCommentById(commentId, commentRequest)));
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Delete comment by comment id, can delete only your own comment")
    public ResponseEntity<ApiResponse<CommentDTO>> deleteCommentById(@PathVariable Long commentId) {
        commentService.deleteCommentById(commentId);
        return ResponseEntity.ok(ApiResponse.success("Deleted comment successfully", null));
    }
}
