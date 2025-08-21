package com.example.demominiproject001.service;

import com.example.demominiproject001.model.enums.CategorySortBy;
import com.example.demominiproject001.model.request.CategoryRequest;
import com.example.demominiproject001.model.response.CategoryDTO;
import com.example.demominiproject001.model.response.UserDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> getAllCategories(int page, int size, CategorySortBy sortBy, Sort.Direction sortDir);

    CategoryDTO getCategoryById(Long categoryId);

    CategoryDTO createCategory(CategoryRequest categoryRequest);

    CategoryDTO updateCategory(Long categoryId, CategoryRequest categoryRequest);

    void deleteCategory(Long categoryId);
}
