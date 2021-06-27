package uz.pdp.apppcmarket.payload;

import lombok.Data;

import java.util.List;

@Data
public class ProductDto {
    private String name;
    private String description;
    private String info;
    private List<Integer> attachmentId;
    private double price;
    private Integer categoryId;
}
