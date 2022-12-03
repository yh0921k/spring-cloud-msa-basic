package msa.catalogservice.service;

import msa.catalogservice.domain.CatalogEntity;

public interface CatalogService {
  Iterable<CatalogEntity> getAllCatalogs();
}
