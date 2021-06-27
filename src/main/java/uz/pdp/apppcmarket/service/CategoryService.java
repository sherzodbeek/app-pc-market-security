package uz.pdp.apppcmarket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.apppcmarket.entity.Category;
import uz.pdp.apppcmarket.payload.ApiResponse;
import uz.pdp.apppcmarket.payload.CategoryDto;
import uz.pdp.apppcmarket.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;


    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    public Category getCategory(Integer id) {
        return categoryRepository.findById(id).orElseGet(Category::new);
    }

    public ApiResponse addCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setActive(categoryDto.isActive());
        if(categoryDto.getParentCategoryId()!=null) {
            Optional<Category> optionalCategory = categoryRepository.findById(categoryDto.getParentCategoryId());
            if(!optionalCategory.isPresent())
                return new ApiResponse("Parent category not found!", false);
            Category parentCategory = optionalCategory.get();
            category.setParentCategoryId(parentCategory);
        }
        categoryRepository.save(category);
        return new ApiResponse("Category added!", true);
    }

    public ApiResponse editCategory(Integer id, CategoryDto categoryDto) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if(!optionalCategory.isPresent())
            return new ApiResponse("Category not found!", false);
        Category editingCategory = optionalCategory.get();
        editingCategory.setActive(categoryDto.isActive());
        editingCategory.setName(categoryDto.getName());
        if(categoryDto.getParentCategoryId()!=null) {
            Optional<Category> optionalParentCategory = categoryRepository.findById(categoryDto.getParentCategoryId());
            if(!optionalParentCategory.isPresent())
                return new ApiResponse("Parent category not found!", false);
            Category parentCategory = optionalCategory.get();
            editingCategory.setParentCategoryId(parentCategory);
        }
        categoryRepository.save(editingCategory);
        return new ApiResponse("Category edited!", true);
    }

    public ApiResponse deleteCategory(Integer id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if(!optionalCategory.isPresent())
            return new ApiResponse("Category not found!", false);
        Category category = optionalCategory.get();
        categoryRepository.deleteAllByParentCategoryId(category);
        categoryRepository.deleteById(id);
        return new ApiResponse("Category deleted!", true);
    }
}
