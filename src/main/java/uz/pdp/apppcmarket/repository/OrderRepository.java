package uz.pdp.apppcmarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.apppcmarket.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
