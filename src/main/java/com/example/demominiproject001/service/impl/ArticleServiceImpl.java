package com.example.demominiproject001.service.impl;

import com.example.demominiproject001.exception.ResourceNotFoundException;
import com.example.demominiproject001.model.entity.AppUser;
import com.example.demominiproject001.model.entity.Article;
import com.example.demominiproject001.model.entity.Category;
import com.example.demominiproject001.model.entity.CategoryArticle;
import com.example.demominiproject001.model.enums.ArticleSortBy;
import com.example.demominiproject001.model.request.ArticleRequest;
import com.example.demominiproject001.model.request.CommentRequest;
import com.example.demominiproject001.model.response.ArticleDTO;
import com.example.demominiproject001.repository.*;
import com.example.demominiproject001.service.ArticleService;
import com.example.demominiproject001.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final Helper helper;
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final AppUserRepository appUserRepository;
    private final CategoryArticleRepository categoryArticleRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<ArticleDTO> getAllArticles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") ArticleSortBy sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDir)
    {
        Pageable pageable = PageRequest.of(page-1, size, sortDir, String.valueOf(sortBy));
        Page<Article> articlePage = articleRepository.findAll(pageable);
        return articlePage.getContent().stream().map(Article::convertToArticleDTO).toList();
    }

    @Override
    public ArticleDTO getArticleById(Long id) {

        Article article = articleRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Article not found with id: " + id)
        );

        return article.convertToArticleDTO();
    }

    @Transactional
    @Override
    public ArticleDTO createArticle(ArticleRequest articleRequest) {

        AppUser appUser = appUserRepository.findByEmail(helper.getCurrentUserEmail()).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );

        // Validate duplicate category IDs
        Set<Long> seenCreate = new HashSet<>();
        Set<Long> duplicateIdsCreate = articleRequest.getCategoryIds().stream()
                .filter(id -> !seenCreate.add(id))
                .collect(Collectors.toSet());

        if (!duplicateIdsCreate.isEmpty()) {
            throw new RuntimeException("Duplicate category ids: " + duplicateIdsCreate);
        }

        List<Category> categories = categoryRepository.findByCategoryIdInAndUser_UserId(articleRequest.getCategoryIds(), helper.getCurrentUserId());

        if (categories.size() != articleRequest.getCategoryIds().size()) {
            Set<Long> foundIds = categories.stream().map(Category::getCategoryId).collect(Collectors.toSet());
            Set<Long> requestedIds = new HashSet<>(articleRequest.getCategoryIds());
            requestedIds.removeAll(foundIds);
            throw new RuntimeException("Categories not found: " + requestedIds);
        }

        Article article = Article.builder()
                .title(articleRequest.getTitle())
                .description(articleRequest.getDescription())
                .user(appUser)
                .build();

        Article savedArticle = articleRepository.save(article);

        categories.forEach(category -> {
            CategoryArticle categoryArticle = CategoryArticle.builder()
                    .article(savedArticle)
                    .category(category)
                    .build();
            categoryArticleRepository.save(categoryArticle);
            savedArticle.addCategoryArticle(categoryArticle);
            category.setAmountOfArticles(category.getAmountOfArticles() + 1);
            categoryRepository.save(category);
        });

        return savedArticle.convertToArticleDTO();
    }

    @Transactional
    @Override
    public ArticleDTO updateArticle(Long id, ArticleRequest articleRequest) {

        AppUser appUser = appUserRepository.findByEmail(helper.getCurrentUserEmail()).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );

        Article article = articleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Article not found")
        );

        List<Category> newCategories = categoryRepository.findAllById(articleRequest.getCategoryIds());

        if (newCategories.size() != articleRequest.getCategoryIds().size()) {
            Set<Long> foundIds = newCategories.stream().map(Category::getCategoryId).collect(Collectors.toSet());
            Set<Long> requestedIds = new HashSet<>(articleRequest.getCategoryIds());
            requestedIds.removeAll(foundIds);
            throw new RuntimeException("Categories not found: " + requestedIds);
        }

        if(article.getCategoryArticles() != null) {
            article.getCategoryArticles().forEach(categoryArticle -> {

                Category category = categoryArticle.getCategory();
                if(category != null) {
                    category.setAmountOfArticles(Math.max(0, category.getAmountOfArticles() - 1));
                    categoryRepository.save(category);
                }
            });

            article.getCategoryArticles().clear();
        }


        article.setTitle(articleRequest.getTitle());
        article.setDescription(articleRequest.getDescription());
        article.setUser(appUser);

        Article updatedArticle = articleRepository.save(article);

        newCategories.forEach(category -> {
            CategoryArticle categoryArticle = CategoryArticle.builder()
                    .article(updatedArticle)
                    .category(category)
                    .build();
            categoryArticleRepository.save(categoryArticle);
            updatedArticle.addCategoryArticle(categoryArticle);
            category.setAmountOfArticles(category.getAmountOfArticles() + 1);
            categoryRepository.save(category);
        });

        return updatedArticle.convertToArticleDTO();
    }

    @Transactional
    @Override
    public void deleteArticle(Long id) {
        Article article = articleRepository.findArticleByArticleIdAndUser_UserId(id, helper.getCurrentUserId()).orElseThrow(
                () -> new ResourceNotFoundException("Article not found")
        );

        if(article.getCategoryArticles() != null) {
            article.getCategoryArticles().forEach(categoryArticle -> {

                Category category = categoryArticle.getCategory();
                if(category != null) {
                    category.setAmountOfArticles(Math.max(0, category.getAmountOfArticles() - 1));
                    categoryRepository.save(category);
                }
            });

            article.getCategoryArticles().clear();
        }

        articleRepository.deleteArticlesByArticleIdAndUser_UserId(article.getArticleId(), helper.getCurrentUserId());
    }

    @Transactional
    @Override
    public ArticleDTO createCommentOnArticle(Long articleId, CommentRequest commentRequest) {

        AppUser appUser = appUserRepository.findByEmail(helper.getCurrentUserEmail()).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );

        Article article = articleRepository.findArticleByArticleIdAndUser_UserId(articleId, helper.getCurrentUserId()).orElseThrow(
                () -> new ResourceNotFoundException("Article not found")
        );



        return null;
    }

}
