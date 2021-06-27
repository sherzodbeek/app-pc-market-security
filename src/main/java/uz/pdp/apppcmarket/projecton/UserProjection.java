package uz.pdp.apppcmarket.projecton;

import org.springframework.data.rest.core.config.Projection;
import uz.pdp.apppcmarket.entity.User;

@Projection(name = "userProjection", types = User.class)
public interface UserProjection {
    Integer getId();

    String getName();

    String getRoleName();

    String getPassword();

    String getLogin();

    String getEmail();
}
