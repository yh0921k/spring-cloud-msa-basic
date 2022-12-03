package msa.catalogservice.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import msa.catalogservice.domain.CatalogEntity;
import msa.catalogservice.domain.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Slf4j
@Service
public class CatalogServiceImpl implements CatalogService {

  private final CatalogRepository catalogRepository;

  @Autowired
  public CatalogServiceImpl(CatalogRepository catalogRepository) {
    this.catalogRepository = catalogRepository;
  }

  @Override
  public Iterable<CatalogEntity> getAllCatalogs() {
    return catalogRepository.findAll();
  }
}
