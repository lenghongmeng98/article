package com.example.demominiproject001.repository;

import com.example.demominiproject001.model.entity.Article;
import com.example.demominiproject001.model.response.ArticleDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findArticleByArticleIdAndUser_UserId(Long articleId, Long userUserId);
    void deleteArticlesByArticleIdAndUser_UserId(Long articleId, Long userUserId);
}
