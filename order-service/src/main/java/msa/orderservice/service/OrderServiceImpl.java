package msa.orderservice.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import msa.orderservice.domain.OrderEntity;
import msa.orderservice.domain.OrderRepository;
import msa.orderservice.dto.OrderDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Data
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

  private OrderRepository orderRepository;

  @Autowired
  public OrderServiceImpl(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Override
  public OrderDto createOrder(OrderDto orderDto) {
    orderDto.setOrderId(UUID.randomUUID().toString());
    orderDto.setTotalPrice(orderDto.getUnitPrice() * orderDto.getQuantity());

    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    OrderEntity orderEntity = mapper.map(orderDto, OrderEntity.class);

    orderRepository.save(orderEntity);

    return mapper.map(orderEntity, OrderDto.class);
  }

  @Override
  public Iterable<OrderEntity> getOrdersByUserId(String userId) {
    return orderRepository.findByUserId(userId);
  }

  @Override
  public OrderDto getOrderByOrderId(String orderId) {
    OrderEntity orderEntity = orderRepository.findByOrderId(orderId);

    return new ModelMapper().map(orderEntity, OrderDto.class);
  }
}
