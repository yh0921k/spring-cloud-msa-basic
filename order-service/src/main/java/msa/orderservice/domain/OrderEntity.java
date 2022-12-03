package msa.orderservice.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "orders")
public class OrderEntity implements Serializable {
  @Id @GeneratedValue private Long id;

  @Column(nullable = false, length = 120, unique = true)
  private String productId;

  @Column(nullable = false)
  private Integer quantity;

  @Column(nullable = false)
  private Integer unitPrice;

  @Column(nullable = false)
  private Integer totalPrice;

  @Column(nullable = false)
  private String userId;

  @Column(nullable = false, unique = true)
  private String orderId;
}
