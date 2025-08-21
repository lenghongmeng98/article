package com.example.demominiproject001.service;

import com.example.demominiproject001.model.entity.Article;
import com.example.demominiproject001.model.enums.ArticleSortBy;
import com.example.demominiproject001.model.request.ArticleRequest;
import com.example.demominiproject001.model.request.CommentRequest;
import com.example.demominiproject001.model.response.ArticleDTO;
import com.example.demominiproject001.model.response.ArticleWithCommentDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ArticleService {

    List<ArticleDTO> getAllArticles(int page, int size, ArticleSortBy sortBy, Sort.Direction sortDir);

    ArticleDTO getArticleById(Long id);

    ArticleDTO createArticle(ArticleRequest articleRequest);

    ArticleDTO updateArticle(Long id, ArticleRequest articleRequest);

    void deleteArticle(Long id);

    ArticleWithCommentDTO createCommentOnArticle(Long articleId, CommentRequest commentRequest);

    ArticleWithCommentDTO getAllCommentsByArticleId(Long articleId);
}
