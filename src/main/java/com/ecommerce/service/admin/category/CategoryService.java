package com.ecommerce.service.admin.category;

import com.ecommerce.dto.CategoryDto;
import com.ecommerce.entity.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryDto categoryDto);

    List<CategoryDto> getAllCategories();
}
