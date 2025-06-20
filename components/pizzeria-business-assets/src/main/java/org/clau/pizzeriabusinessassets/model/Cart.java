package org.clau.pizzeriabusinessassets.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Cart")
@Table(name = "cart")
@Getter
@Setter
@Builder(setterPrefix = "with")
public class Cart {

   @Id
   private Long id;

   private Integer totalQuantity;

   private Double totalCost;

   private Double totalCostOffers;

   // INFO to remember about the Cart/CartItem association:
   // given that Order & Cart association has CascadeType.ALL
   // and Cart & CartItem bidirectional association also has CascadeType.ALL
   // when updating Cart, the merge operation is going to be cascaded to the
   // CartItem association as well, so there's no need to manually
   // sync the bidirectional association
   @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
   @JsonManagedReference
   @Builder.Default
   private List<CartItem> cartItems = new ArrayList<>();

   @OneToOne(fetch = FetchType.LAZY)
   @MapsId
   @JsonBackReference
   private Order order;

   public void addItem(CartItem item) {
	  item.setId(null);
	  cartItems.add(item);
	  item.setCart(this);
   }

//	public void removeItem(CartItem item) {
//		cartItemsDTO.remove(item);
//		item.setCart(null);
//	}
}