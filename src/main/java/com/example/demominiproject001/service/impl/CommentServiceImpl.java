package com.example.demominiproject001.service.impl;

import com.example.demominiproject001.exception.ResourceNotFoundException;
import com.example.demominiproject001.model.entity.Comment;
import com.example.demominiproject001.model.request.CommentRequest;
import com.example.demominiproject001.model.response.CommentDTO;
import com.example.demominiproject001.repository.CommentRepository;
import com.example.demominiproject001.service.CommentService;
import com.example.demominiproject001.utils.Helper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final Helper helper;
    private final CommentRepository commentRepository;

    @Override
    public CommentDTO getCommentById(long commentId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment not found")
        );

        if(!comment.getUser().getUserId().equals(helper.getCurrentUserId())) {
            throw new ResourceNotFoundException("This comment does not belong to you");
        }

        return comment.convertToCommentDTO();
    }

    @Transactional
    @Override
    public CommentDTO updateCommentById(long commentId, CommentRequest commentRequest) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment not found")
        );

        if(!comment.getUser().getUserId().equals(helper.getCurrentUserId())) {
            throw new ResourceNotFoundException("This comment does not belong to you");
        }

        comment.setContent(commentRequest.getContent());
        commentRepository.save(comment);

        return comment.convertToCommentDTO();
    }

    @Transactional
    @Override
    public void deleteCommentById(long commentId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment not found")
        );

        if(!comment.getUser().getUserId().equals(helper.getCurrentUserId())) {
            throw new ResourceNotFoundException("This comment does not belong to you");
        }

        commentRepository.delete(comment);
    }
}
