package com.example.demominiproject001.service;

import com.example.demominiproject001.model.request.CommentRequest;
import com.example.demominiproject001.model.response.CommentDTO;

public interface CommentService {

    CommentDTO getCommentById(long commentId);

    CommentDTO updateCommentById(long commentId, CommentRequest commentRequest);

    void deleteCommentById(long commentId);
}
