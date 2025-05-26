package org.clau.pizzeriabusinessassets.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "OrderDetails")
@Table(name = "order_details")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(setterPrefix = "with")
public class OrderDetails {

	@Id
	private Long id;

	private String deliveryTime;

	private String paymentMethod;

	private Double billToChange;

	private Double changeToGive;

	private String comment;

	private Boolean storePickUp;

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@JsonBackReference
	private Order order;
}