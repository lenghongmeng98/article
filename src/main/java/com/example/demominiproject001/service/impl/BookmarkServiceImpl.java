package com.example.demominiproject001.service.impl;

import com.example.demominiproject001.exception.ResourceNotFoundException;
import com.example.demominiproject001.model.entity.AppUser;
import com.example.demominiproject001.model.entity.Article;
import com.example.demominiproject001.model.entity.Bookmark;
import com.example.demominiproject001.model.response.ArticleDTO;
import com.example.demominiproject001.repository.AppUserRepository;
import com.example.demominiproject001.repository.ArticleRepository;
import com.example.demominiproject001.repository.BookmarkRepository;
import com.example.demominiproject001.repository.CommentRepository;
import com.example.demominiproject001.service.BookmarkService;
import com.example.demominiproject001.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final Helper helper;
    private final BookmarkRepository bookmarkRepository;
    private final AppUserRepository appUserRepository;
    private final ArticleRepository articleRepository;

    @Override
    public List<ArticleDTO> getAllBookmarkArticles(int page, int size, Sort.Direction sortDirection) {

        Pageable pageable = PageRequest.of(page-1, size, sortDirection, "createdAt");
        Page<Bookmark> bookmarkPage = bookmarkRepository.findAllByUser_UserId(helper.getCurrentUserId(), pageable);

        return bookmarkPage.getContent().stream()
                .map(Bookmark::getArticle)
                .map(Article::convertToArticleDTO)
                .toList();
    }

    @Transactional
    @Override
    public ArticleDTO addBookmark(Long articleId) {

        AppUser appUser = appUserRepository.findByEmail(helper.getCurrentUserEmail()).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );

        Article article = articleRepository.findById(articleId).orElseThrow(
                ()-> new ResourceNotFoundException("Article not found with id: " + articleId)
        );

        Bookmark bookmark = Bookmark.builder().article(article).user(appUser).build();

        try {
            Bookmark savedBookmark = bookmarkRepository.save(bookmark);
            return savedBookmark.getArticle().convertToArticleDTO();

        } catch (Exception ex) {
            throw new IllegalStateException("You have already bookmarked this article.");
        }
    }

    @Transactional
    @Override
    public void deleteBookmark(Long articleId) {
        Bookmark bookmark = bookmarkRepository.findBookmarkByArticle_ArticleIdAndUser_UserId(articleId, helper.getCurrentUserId()).orElseThrow(
                () -> new ResourceNotFoundException("No bookmark found for article with id: " + articleId)
        );
        bookmarkRepository.delete(bookmark);
    }
}
