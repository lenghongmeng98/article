package com.example.demominiproject001.repository;

import com.example.demominiproject001.model.entity.AppUser;
import com.example.demominiproject001.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findAllByUser_UserId(Long userId, Pageable pageable);
    Optional<Category> findCategoryByCategoryNameIgnoreCaseAndUser_UserId(String categoryName, Long userUserId);
    Optional<Category> findCategoryByCategoryIdAndUser_UserId(Long id, Long userId);
    List<Category> findByCategoryIdInAndUser_UserId(List<Long> categoryIds, Long userId);
}
