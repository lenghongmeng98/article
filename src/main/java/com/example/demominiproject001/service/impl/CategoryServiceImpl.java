package com.example.demominiproject001.service.impl;

import com.example.demominiproject001.exception.AlreadyExistsException;
import com.example.demominiproject001.exception.ResourceNotFoundException;
import com.example.demominiproject001.model.entity.AppUser;
import com.example.demominiproject001.model.entity.Category;
import com.example.demominiproject001.model.enums.CategorySortBy;
import com.example.demominiproject001.model.request.CategoryRequest;
import com.example.demominiproject001.model.response.CategoryDTO;
import com.example.demominiproject001.model.response.UserDTO;
import com.example.demominiproject001.repository.AppUserRepository;
import com.example.demominiproject001.repository.CategoryRepository;
import com.example.demominiproject001.service.CategoryService;
import com.example.demominiproject001.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final Helper helper;
    private final CategoryRepository categoryRepository;
    private final AppUserRepository appUserRepository;

    @Override
    public List<CategoryDTO> getAllCategories(int page, int size, CategorySortBy sortBy, Sort.Direction sortDirection) {
        Pageable pageable = PageRequest.of(page-1, size, sortDirection, String.valueOf(sortBy));
        Page<Category> categoryPage = categoryRepository.findAllByUser_UserId(helper.getCurrentUserId(), pageable);
        return categoryPage.getContent().stream().map(Category::convertToCategoryDTO).toList();
    }

    @Override
    public CategoryDTO getCategoryById(Long categoryId) {

        Long userId = helper.getCurrentUserId();
        Category category = categoryRepository.findCategoryByCategoryIdAndUser_UserId(categoryId, userId).orElseThrow(
                () -> new ResourceNotFoundException("Category not found with id: " + categoryId)
        );
        return category.convertToCategoryDTO();
    }

    @Transactional
    @Override
    public CategoryDTO createCategory(CategoryRequest categoryRequest) {

        if(categoryRepository.findCategoryByCategoryNameIgnoreCaseAndUser_UserId(categoryRequest.getCategoryName().toLowerCase().trim(), helper.getCurrentUserId()).isPresent()) {
            throw new AlreadyExistsException("Category already exists");
        }

        AppUser appUser = appUserRepository.findByEmail(helper.getCurrentUserEmail()).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );

        Category category = Category.builder()
                .categoryName(categoryRequest.getCategoryName())
                .amountOfArticles(0)
                .user(appUser)
                .build();

        return categoryRepository.save(category).convertToCategoryDTO();
    }

    @Transactional
    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryRequest categoryRequest) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category not found with id: " + categoryId)
        );

        if(categoryRequest.getCategoryName().trim().toLowerCase().equals(category.getCategoryName().toLowerCase().trim())) {
            throw new AlreadyExistsException("Category already exists, Please choose a different category name");
        }

        category.setCategoryName(categoryRequest.getCategoryName());
        return categoryRepository.save(category).convertToCategoryDTO();
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category not found with id: " + categoryId)
        );

        try {
            categoryRepository.deleteById(categoryId);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("This category is already in use");
        }
    }
}
