package pets.adoption.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pets.adoption.models.Category;
import pets.adoption.repositories.CategoryRepository;
import pets.adoption.exceptions.ResourceNotFoundException;
import pets.adoption.exceptions.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    public Category createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new DuplicateResourceException("Category already exists with name: " + category.getName());
        }
        return categoryRepository.save(category);
    }
    
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }
    
    public Category getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name);
        if (category == null) {
            throw new ResourceNotFoundException("Category not found with name: " + name);
        }
        return category;
    }
    
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = getCategoryById(id);
        
        if (!category.getName().equals(categoryDetails.getName()) && 
            categoryRepository.existsByName(categoryDetails.getName())) {
            throw new DuplicateResourceException("Category already exists with name: " + categoryDetails.getName());
        }
        
        category.setName(categoryDetails.getName());
        return categoryRepository.save(category);
    }
    
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }
}
