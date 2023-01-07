package msa.orderservice.controller;

import lombok.extern.slf4j.Slf4j;
import msa.orderservice.domain.OrderEntity;
import msa.orderservice.dto.OrderDto;
import msa.orderservice.messagequeue.KafkaProducer;
import msa.orderservice.messagequeue.OrderProducer;
import msa.orderservice.service.OrderService;
import msa.orderservice.vo.RequestOrder;
import msa.orderservice.vo.ResponseOrder;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order-service")
@Slf4j
public class OrderController {

  private final Environment environment;
  private final OrderService orderService;
  private final KafkaProducer kafkaProducer;
  private final OrderProducer orderProducer;

  @Autowired
  public OrderController(
      Environment environment,
      OrderService orderService,
      KafkaProducer kafkaProducer,
      OrderProducer orderProducer) {
    this.environment = environment;
    this.orderService = orderService;
    this.kafkaProducer = kafkaProducer;
    this.orderProducer = orderProducer;
  }

  @GetMapping("/health_check")
  public String status() {
    return String.format(
        "It's Working in Order Service on PORT %s", environment.getProperty("local.server.port"));
  }

  @PostMapping("/{userId}/orders")
  public ResponseEntity<ResponseOrder> createOrder(
      @PathVariable("userId") String userId, @RequestBody RequestOrder orderDetails) {

    log.info("Before add orders data");

    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    OrderDto orderDto = mapper.map(orderDetails, OrderDto.class);
    orderDto.setUserId(userId);

    // JPA
     OrderDto createdOrder = orderService.createOrder(orderDto);
     ResponseOrder responseOrder = mapper.map(createdOrder, ResponseOrder.class);

    // kafka
//    orderDto.setOrderId(UUID.randomUUID().toString());
//    orderDto.setTotalPrice(orderDto.getUnitPrice() * orderDto.getQuantity());

    // send this order to the kafka
    kafkaProducer.send("example-catalog-topic", orderDto);
//    orderProducer.send("orders", orderDto);

//    ResponseOrder responseOrder = mapper.map(orderDto, ResponseOrder.class);

    log.info("Before added orders data");

    return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
  }

  @GetMapping("/{userId}/orders")
  public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId) {
    log.info("Before retrieve orders data");
    Iterable<OrderEntity> orderList = orderService.getOrdersByUserId(userId);
    List<ResponseOrder> result = new ArrayList<>();
    orderList.forEach(
        orderEntity -> {
          result.add(new ModelMapper().map(orderEntity, ResponseOrder.class));
        });
    log.info("After retrieved orders data");

    return ResponseEntity.status(HttpStatus.OK).body(result);
  }
}
