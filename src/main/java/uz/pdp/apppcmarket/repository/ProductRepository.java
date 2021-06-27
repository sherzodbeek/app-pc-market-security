package uz.pdp.apppcmarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.apppcmarket.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
