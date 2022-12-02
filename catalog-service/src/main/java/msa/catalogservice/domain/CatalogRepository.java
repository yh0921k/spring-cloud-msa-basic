package msa.catalogservice.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogRepository extends CrudRepository<CatalogEntity, Long> {
  CatalogEntity findByProductId(String productId);
}
