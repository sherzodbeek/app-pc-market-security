package uz.pdp.apppcmarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.apppcmarket.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    void deleteAllByParentCategoryId(Category parentCategoryId);
}
