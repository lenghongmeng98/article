package com.example.demominiproject001.model.entity;

import com.example.demominiproject001.model.response.CategoryDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "categories")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Column(name = "amount_of_articles")
    private Integer amountOfArticles;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;


    // -- To response
    public CategoryDTO convertToCategoryDTO() {
        return CategoryDTO.builder()
                .categoryId(this.categoryId)
                .categoryName(this.categoryName)
                .amountOfArticles(this.amountOfArticles)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
