package org.clau.pizzeriaassetsresourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.apiutils.util.TimeUtils;
import org.clau.pizzeriaassetsresourceserver.dao.OrderRepository;
import org.clau.pizzeriaassetsresourceserver.service.OrderService;
import org.clau.pizzeriabusinessassets.dto.NewAnonOrderDTO;
import org.clau.pizzeriabusinessassets.model.Cart;
import org.clau.pizzeriabusinessassets.model.CartItem;
import org.clau.pizzeriabusinessassets.model.Order;
import org.clau.pizzeriabusinessassets.model.OrderDetails;
import org.clau.pizzeriabusinessassets.util.OrderUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;

	@Override
	public Order createAnonOrder(NewAnonOrderDTO newAnonOrder) {

		Cart cart = Cart.builder()
				.withTotalQuantity(newAnonOrder.cart().totalQuantity())
				.withTotalCost(newAnonOrder.cart().totalCost())
				.withTotalCostOffers(newAnonOrder.cart().totalCostOffers())
				.build();

		newAnonOrder.cart().cartItems().stream()
				.map(cartItemDTO -> CartItem.builder()
						.withType(cartItemDTO.type())
						.withName(cartItemDTO.name())
						.withQuantity(cartItemDTO.quantity())
						.withDescription(cartItemDTO.description())
						.withPrice(cartItemDTO.price())
						.withFormats(cartItemDTO.formats())
						.build())
				.toList()
				.forEach(cart::addItem);

		Order anonOrder = Order.builder()
				.withCreatedOn(LocalDateTime.now())
				.withFormattedCreatedOn(TimeUtils.formatDateAsString(TimeUtils.getNowAccountingDST()))
				.withAnonCustomerName(newAnonOrder.customer().anonCustomerName())
				.withAnonCustomerContactNumber(newAnonOrder.customer().anonCustomerContactNumber())
				.withAnonCustomerEmail(newAnonOrder.customer().anonCustomerEmail())
				.withAddress(newAnonOrder.address())
				.build();

		anonOrder.setOrderDetails(OrderDetails.builder()
				.withDeliveryTime(newAnonOrder.orderDetails().deliveryTime())
				.withPaymentMethod(newAnonOrder.orderDetails().paymentMethod())
				.withBillToChange(newAnonOrder.orderDetails().billToChange())
				.withStorePickUp(newAnonOrder.orderDetails().storePickUp())
				.withComment(newAnonOrder.orderDetails().comment())
				.build());

		anonOrder.setCart(cart);

		if (null != newAnonOrder.orderDetails().billToChange()) {
			anonOrder.getOrderDetails().setChangeToGive(OrderUtils.calculatePaymentChange(
					newAnonOrder.orderDetails().billToChange(),
					newAnonOrder.cart().totalCost(),
					newAnonOrder.cart().totalCostOffers())
			);
		}

		return orderRepository.save(anonOrder);
	}
}
