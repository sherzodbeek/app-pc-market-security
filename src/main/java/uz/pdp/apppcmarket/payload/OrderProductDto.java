package uz.pdp.apppcmarket.payload;

import lombok.Data;

@Data
public class OrderProductDto {
    private Integer productId;
    private int amount;
}
