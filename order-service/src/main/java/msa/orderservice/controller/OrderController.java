package msa.orderservice.controller;

import msa.orderservice.domain.OrderEntity;
import msa.orderservice.dto.OrderDto;
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

@RestController
@RequestMapping("/order-service")
public class OrderController {

  private final Environment environment;
  private final OrderService orderService;

  @Autowired
  public OrderController(Environment environment, OrderService orderService) {
    this.environment = environment;
    this.orderService = orderService;
  }

  @GetMapping("/health_check")
  public String status() {
    return String.format(
        "It's Working in Order Service on PORT %s", environment.getProperty("local.server.port"));
  }

  @PostMapping("/{userId}/orders")
  public ResponseEntity<ResponseOrder> createOrder(
      @PathVariable("userId") String userId, @RequestBody RequestOrder orderDetails) {

    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    OrderDto orderDto = mapper.map(orderDetails, OrderDto.class);
    orderDto.setUserId(userId);
    OrderDto createdOrder = orderService.createOrder(orderDto);

    ResponseOrder responseOrder = mapper.map(createdOrder, ResponseOrder.class);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
  }

  @GetMapping("/{userId}/orders")
  public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId) {

    Iterable<OrderEntity> orderList = orderService.getOrdersByUserId(userId);
    List<ResponseOrder> result = new ArrayList<>();
    orderList.forEach(
        orderEntity -> {
          result.add(new ModelMapper().map(orderEntity, ResponseOrder.class));
        });

    return ResponseEntity.status(HttpStatus.OK).body(result);
  }
}
