package org.clau.pizzeriabusinessresourceserver;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriabusinessassets.model.Cart;
import org.clau.pizzeriabusinessassets.model.CartItem;
import org.clau.pizzeriabusinessassets.model.Order;
import org.clau.pizzeriabusinessassets.model.OrderDetails;
import org.clau.pizzeriabusinessresourceserver.dao.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class TestHelperService {

	private final OrderRepository orderRepository;

	public Order createOrder(Long userId, int minusMins) {

		Cart cart = Cart.builder()
				.withTotalQuantity(1)
				.withTotalCost(13.30)
				.withTotalCostOffers(0D)
				.build();

		List.of(CartItem.builder()
						.withId(null)
						.withType("pizza")
						.withPrice(13.30)
						.withQuantity(1)
						.withName(Map.of("es", "Cuatro Quesos", "en", "Cuatro Quesos"))
						.withDescription(Map.of(
								"es", List.of("Salsa de Tomate", "Mozzarella 100%", "Parmesano", "Emmental", "Queso Azul"),
								"en", List.of("Tomato Sauce", "100% Mozzarella", "Parmesan Cheese", "Emmental Cheese", "Blue Cheese")
						))
						.withFormats(Map.of("m", Map.of("es", "Mediana", "en", "Medium"), "l", Map.of(), "s", Map.of()))
						.build())
				.forEach(cart::addItem);

		LocalDateTime createdOn = LocalDateTime.now().minusMinutes(minusMins);
		Order order = Order.builder()
				.withCreatedOn(createdOn)
				.withFormattedCreatedOn(createdOn.format(DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")))
				.withUserId(userId)
				.withAddress("Baker Street 221b")
				.build();

		order.setOrderDetails(OrderDetails.builder()
				.withDeliveryTime("ASAP")
				.withPaymentMethod("Card")
				.withBillToChange(null)
				.withComment(null)
				.withStorePickUp(false)
				.withChangeToGive(0D).
				build());

		order.setCart(cart);

		Long id = orderRepository.save(order).getId();
		return findOrder(id);
	}

	public Order findOrder(Long orderId) {
		return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
	}
}
