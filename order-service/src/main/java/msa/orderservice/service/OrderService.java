package msa.orderservice.service;


import msa.orderservice.domain.OrderEntity;
import msa.orderservice.dto.OrderDto;

public interface OrderService {
  OrderDto createOrder(OrderDto orderDetails);

  Iterable<OrderEntity> getOrdersByUserId(String userId);

  OrderDto getOrderByOrderId(String orderId);
}
