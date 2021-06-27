package uz.pdp.apppcmarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.pdp.apppcmarket.entity.Client;
import uz.pdp.apppcmarket.projecton.ClientProjection;

@RepositoryRestResource(path = "client", collectionResourceRel = "clients", excerptProjection = ClientProjection.class)
public interface ClientRepository extends JpaRepository<Client, Integer> {

}
