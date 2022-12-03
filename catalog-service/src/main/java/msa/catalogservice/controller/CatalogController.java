package msa.catalogservice.controller;

import msa.catalogservice.domain.CatalogEntity;
import msa.catalogservice.service.CatalogService;
import msa.catalogservice.vo.ResponseCatalog;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/catalog-service")
public class CatalogController {

  private final Environment environment;
  private final CatalogService catalogService;

  @Autowired
  public CatalogController(Environment environment, CatalogService catalogService) {
    this.environment = environment;
    this.catalogService = catalogService;
  }

  @GetMapping("/health_check")
  public String status() {
    return String.format(
        "It's Working in Catalog Service on PORT %s", environment.getProperty("local.server.port"));
  }

  @GetMapping("/catalogs")
  public ResponseEntity<List<ResponseCatalog>> getCatalogs() {
    Iterable<CatalogEntity> users = catalogService.getAllCatalogs();

    List<ResponseCatalog> result = new ArrayList<>();
    users.forEach(
        v -> {
          result.add(new ModelMapper().map(v, ResponseCatalog.class));
        });

    return ResponseEntity.ok().body(result);
  }
}
