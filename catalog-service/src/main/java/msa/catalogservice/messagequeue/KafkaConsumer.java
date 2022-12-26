package msa.catalogservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import msa.catalogservice.domain.CatalogEntity;
import msa.catalogservice.domain.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class KafkaConsumer {

  private final CatalogRepository catalogRepository;

  @Autowired
  public KafkaConsumer(CatalogRepository catalogRepository) {
    this.catalogRepository = catalogRepository;
  }

  @KafkaListener(topics = "example-catalog-topic")
  public void updateQuantity(String kafkaMessage) {
    log.info("Kafka Message: -> " + kafkaMessage);
    Map<Object, Object> map = new HashMap<>();
    ObjectMapper mapper = new ObjectMapper();

    try {
      map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
    } catch (JsonProcessingException exception) {
      exception.printStackTrace();
    }

    CatalogEntity entity = catalogRepository.findByProductId((String) map.get("productId"));
    if (entity != null) {
      entity.setStock(entity.getStock() - (Integer) map.get("quantity"));
      catalogRepository.save(entity);
    }
  }
}
