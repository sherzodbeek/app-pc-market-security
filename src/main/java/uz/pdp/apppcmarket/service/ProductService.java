package uz.pdp.apppcmarket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.apppcmarket.entity.Attachment;
import uz.pdp.apppcmarket.entity.Category;
import uz.pdp.apppcmarket.entity.Product;
import uz.pdp.apppcmarket.payload.ApiResponse;
import uz.pdp.apppcmarket.payload.ProductDto;
import uz.pdp.apppcmarket.repository.AttachmentRepository;
import uz.pdp.apppcmarket.repository.CategoryRepository;
import uz.pdp.apppcmarket.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    CategoryRepository categoryRepository;


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Integer id) {
        return productRepository.findById(id).orElseGet(Product::new);
    }

    public ApiResponse addProduct(ProductDto productDto) {
        List<Integer> attachmentId = productDto.getAttachmentId();
        List<Attachment> attachments = new ArrayList<>();
        for (Integer attId : attachmentId) {
            Optional<Attachment> optionalAttachment = attachmentRepository.findById(attId);
            if(!optionalAttachment.isPresent())
                return new ApiResponse("Attachment not found!", false);
            attachments.add(optionalAttachment.get());
        }
        Optional<Category> optionalCategory = categoryRepository.findById(productDto.getCategoryId());
        if(!optionalCategory.isPresent())
            return new ApiResponse("Category not found!", false);
        Category category = optionalCategory.get();
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setInfo(productDto.getInfo());
        product.setAttachments(attachments);
        product.setPrice(productDto.getPrice());
        product.setCategory(category);
        productRepository.save(product);
        return new ApiResponse("Product added!", true);
    }


    public ApiResponse editProduct(Integer id, ProductDto productDto) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(!optionalProduct.isPresent())
            return new ApiResponse("Product not found!", false);
        Product editingProduct = optionalProduct.get();
        List<Integer> attachmentId = productDto.getAttachmentId();
        List<Attachment> attachments = new ArrayList<>();
        for (Integer attId : attachmentId) {
            Optional<Attachment> optionalAttachment = attachmentRepository.findById(attId);
            if(!optionalAttachment.isPresent())
                return new ApiResponse("Attachment not found!", false);
            attachments.add(optionalAttachment.get());
        }
        Optional<Category> optionalCategory = categoryRepository.findById(productDto.getCategoryId());
        if(!optionalCategory.isPresent())
            return new ApiResponse("Category not found!", false);
        Category category = optionalCategory.get();
        editingProduct.setName(productDto.getName());
        editingProduct.setDescription(productDto.getDescription());
        editingProduct.setInfo(productDto.getInfo());
        editingProduct.setAttachments(attachments);
        editingProduct.setPrice(productDto.getPrice());
        editingProduct.setCategory(category);
        productRepository.save(editingProduct);
        return new ApiResponse("Product edited!", true);
    }

    public ApiResponse deleteProduct(Integer id) {
        boolean existsById = productRepository.existsById(id);
        if(!existsById)
            return new ApiResponse("Product not found!", false);
        productRepository.deleteById(id);
        return new ApiResponse("Product deleted!", true);
    }
}
