package org.clau.fabulosa.data.model.business;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Order")
@Table(name = "orders")
@Getter
@Setter
@Builder(setterPrefix = "with")
public class Order {

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_generator")
   @SequenceGenerator(name = "order_generator", sequenceName = "order_seq", allocationSize = 1)
   private Long id;

   private Long userId;

   private LocalDateTime createdOn;

   private String formattedCreatedOn;

   private String state;

   private String anonCustomerName;

   private Integer anonCustomerContactNumber;

   private String anonCustomerEmail;

   private String address;

   @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, optional = false)
   @JsonManagedReference
   private OrderDetails orderDetails;

   // NOTE - bidirectional OneToOne association's non-owning side
   //  can only be lazy fetched if the association is never null ->
   //  optional = false
   @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, optional = false)
   @JsonManagedReference
   private Cart cart;

   public void setOrderDetails(OrderDetails orderDetails) {
	  if (orderDetails == null) {
		 if (this.orderDetails != null) {
			this.orderDetails.setOrder(null);
		 }
	  } else {
		 orderDetails.setOrder(this);
	  }
	  this.orderDetails = orderDetails;
   }

   public void setCart(Cart cart) {
	  if (cart == null) {
		 if (this.cart != null) {
			this.cart.setOrder(null);
		 }
	  } else {
		 cart.setOrder(this);
	  }
	  this.cart = cart;
   }

   @Override
   public int hashCode() {
	  return getClass().hashCode();
   }

   @Override
   public boolean equals(Object obj) {
	  if (this == obj)
		 return true;

	  if (!(obj instanceof Order))
		 return false;

	  return id != null && id.equals(((Order) obj).getId());
   }
}