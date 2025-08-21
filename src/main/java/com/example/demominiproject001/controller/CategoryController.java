package com.example.demominiproject001.controller;

import com.example.demominiproject001.model.enums.CategorySortBy;
import com.example.demominiproject001.model.request.CategoryRequest;
import com.example.demominiproject001.model.response.ApiResponse;
import com.example.demominiproject001.model.response.CategoryDTO;
import com.example.demominiproject001.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
@PreAuthorize("hasRole('AUTHOR')")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get all categories. can be used by only AUTHOR role")
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") CategorySortBy sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDir
    ) {
        return ResponseEntity.ok(ApiResponse.success("Get all categories successfully", categoryService.getAllCategories(page, size, sortBy, sortDir)));
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "Get category by id. can be used by only AUTHOR role")
    public ResponseEntity<ApiResponse<CategoryDTO>> getCategoryById(@PathVariable Long categoryId) {
        return ResponseEntity.ok(ApiResponse.success("Get category successfully", categoryService.getCategoryById(categoryId)));
    }

    @PostMapping
    @Operation(summary = "Create new category. can be used by only AUTHOR role")
    public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(@RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("Created new category successfully", categoryService.createCategory(categoryRequest))
        );
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "Update category. can be used by only AUTHOR role")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategoryById(@PathVariable Long categoryId, @RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.ok(ApiResponse.success("Updated category successfully", categoryService.updateCategory(categoryId, categoryRequest)));
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "Delete category. can be used by only AUTHOR role")
    public ResponseEntity<ApiResponse<CategoryDTO>> deleteCategoryById(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success("Deleted category successfully", null));
    }
}

