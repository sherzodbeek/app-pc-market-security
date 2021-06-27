package uz.pdp.apppcmarket.payload;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class OrderDto {
    private Integer id;
    private Integer clientId;
    private String orderNote;
    private Timestamp date;
    private boolean delivered;
    private List<OrderProductDto> orderProducts;
}
