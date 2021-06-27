package uz.pdp.apppcmarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.apppcmarket.entity.OrderProducts;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProducts, Integer> {

    void deleteAllByOrderId(Integer order_id);
}
