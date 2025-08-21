package com.example.demominiproject001.repository;

import com.example.demominiproject001.model.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Page<Bookmark> findAllByUser_UserId(Long userUserId, Pageable pageable);
    Optional<Bookmark> findBookmarkByArticle_ArticleIdAndUser_UserId(Long article_articleId, Long user_userId);
}
