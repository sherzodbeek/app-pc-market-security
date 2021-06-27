package uz.pdp.apppcmarket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.apppcmarket.entity.Client;
import uz.pdp.apppcmarket.entity.Order;
import uz.pdp.apppcmarket.entity.OrderProducts;
import uz.pdp.apppcmarket.entity.Product;
import uz.pdp.apppcmarket.payload.ApiResponse;
import uz.pdp.apppcmarket.payload.OrderDto;
import uz.pdp.apppcmarket.payload.OrderProductDto;
import uz.pdp.apppcmarket.repository.ClientRepository;
import uz.pdp.apppcmarket.repository.OrderProductRepository;
import uz.pdp.apppcmarket.repository.OrderRepository;
import uz.pdp.apppcmarket.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ProductRepository productRepository;


    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrder(Integer id) {
        return orderRepository.findById(id).orElseGet(Order::new);
    }

    @Transactional
    public ApiResponse addOrder(OrderDto orderDto) {
        Optional<Client> optionalClient = clientRepository.findById(orderDto.getClientId());
        if(!optionalClient.isPresent())
            return new ApiResponse("Client not found!", false);
        Order order = new Order();
        order.setClient(optionalClient.get());
        order.setOrderNote(orderDto.getOrderNote());
        order.setDate(orderDto.getDate());
        order.setDelivered(orderDto.isDelivered());
        Order savedOrder = orderRepository.save(order);
        double totalPrice = 0;
        List<OrderProductDto> orderProducts = orderDto.getOrderProducts();
        for (OrderProductDto products : orderProducts) {
            Optional<Product> optionalProduct = productRepository.findById(products.getProductId());
            if(!optionalProduct.isPresent())
                return new ApiResponse("Product not found!", false);
            Product product = optionalProduct.get();
            OrderProducts orderProduct = new OrderProducts();
            orderProduct.setProduct(product);
            orderProduct.setOrder(savedOrder);
            orderProduct.setAmount(products.getAmount());
            orderProductRepository.save(orderProduct);
            totalPrice = totalPrice + (products.getAmount()*product.getPrice());
        }
        savedOrder.setTotalPrice(totalPrice);
        orderRepository.save(savedOrder);
        return new ApiResponse("Order added!", true);
    }

    @Transactional
    public ApiResponse editOrder(Integer id, OrderDto orderDto) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if(!optionalOrder.isPresent())
            return new ApiResponse("Order not found!", false);
        Optional<Client> optionalClient = clientRepository.findById(orderDto.getClientId());
        if(!optionalClient.isPresent())
            return new ApiResponse("Client not found!", false);
        Order editingOrder = optionalOrder.get();
        editingOrder.setClient(optionalClient.get());
        editingOrder.setOrderNote(orderDto.getOrderNote());
        editingOrder.setDate(orderDto.getDate());
        editingOrder.setDelivered(orderDto.isDelivered());
        Order savedOrder = orderRepository.save(editingOrder);
        orderProductRepository.deleteAllByOrderId(savedOrder.getId());
        double totalPrice = 0;
        List<OrderProductDto> orderProducts = orderDto.getOrderProducts();
        for (OrderProductDto products : orderProducts) {
            Optional<Product> optionalProduct = productRepository.findById(products.getProductId());
            if(!optionalProduct.isPresent())
                return new ApiResponse("Product not found!", false);
            Product product = optionalProduct.get();
            OrderProducts orderProduct = new OrderProducts();
            orderProduct.setProduct(product);
            orderProduct.setOrder(savedOrder);
            orderProduct.setAmount(products.getAmount());
            orderProductRepository.save(orderProduct);
            totalPrice = totalPrice + (products.getAmount()*product.getPrice());
        }
        savedOrder.setTotalPrice(totalPrice);
        orderRepository.save(savedOrder);
        return new ApiResponse("Order edited!", true);
    }

    public ApiResponse deleteOrder(Integer id) {
        boolean existsById = orderRepository.existsById(id);
        if(!existsById)
            return new ApiResponse("Order not found!", false);
        orderProductRepository.deleteAllByOrderId(id);
        orderRepository.deleteById(id);
        return new ApiResponse("Order deleted!", true);
    }
}
