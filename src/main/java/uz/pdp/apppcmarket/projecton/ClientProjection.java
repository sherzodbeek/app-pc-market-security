package uz.pdp.apppcmarket.projecton;

import org.springframework.data.rest.core.config.Projection;
import uz.pdp.apppcmarket.entity.Client;

@Projection(name = "clientProjection", types = Client.class)
public interface ClientProjection {
    Integer getId();

    String getName();

    String getAddress();

    String getPhoneNumber();

    String getEmail();
}
