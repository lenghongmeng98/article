package com.example.demominiproject001.model.entity;

import com.example.demominiproject001.model.response.ArticleDTO;
import com.example.demominiproject001.model.response.ArticleWithCommentDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "articles")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long articleId;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false,  length = 500)
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // -- Relationship with User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    // -- Relationship with Category
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CategoryArticle> categoryArticles;

    // -- Relationship with Comment
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("createdAt DESC")
    private List<Comment> comments;

    // -- Response
    public ArticleDTO convertToArticleDTO() {
        return ArticleDTO.builder()
                .articleId(this.articleId)
                .title(this.title)
                .description(this.description)
                .userId(this.user.getUserId())
                .categories(this.categoryArticles.stream()
                        .map(c -> c.getCategory().getCategoryName())
                        .collect(Collectors.toList()))
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }

    public ArticleWithCommentDTO convertToArticleWithCommentDTO() {
        return ArticleWithCommentDTO.builder()
                .articleId(this.articleId)
                .title(this.title)
                .description(this.description)
                .userId(this.user.getUserId())
                .categories(this.categoryArticles.stream()
                        .map(c -> c.getCategory().getCategoryName())
                        .collect(Collectors.toList()))
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .comments(this.comments.stream()
                        .map(Comment::convertToCommentDTO)
                        .collect(Collectors.toList())
                )
                .build();
    }

    public void addCategoryArticle(CategoryArticle categoryArticle) {

        if(this.categoryArticles == null) {
            this.categoryArticles = new ArrayList<>();
        }
        this.categoryArticles.add(categoryArticle);

        if (categoryArticle.getArticle() != this) {
            categoryArticle.setArticle(this);
        }
    }

    public void addComment(Comment comment) {

        if(this.comments == null) {
            this.comments = new ArrayList<>();
        }
        this.comments.add(comment);

        if (comment.getArticle() != this) {
            comment.setArticle(this);
        }
    }
}
