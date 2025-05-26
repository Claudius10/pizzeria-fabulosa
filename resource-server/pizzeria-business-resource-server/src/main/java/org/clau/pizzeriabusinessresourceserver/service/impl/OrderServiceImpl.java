package org.clau.pizzeriabusinessresourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.apiutils.util.TimeUtils;
import org.clau.pizzeriabusinessassets.dto.NewAnonOrderDTO;
import org.clau.pizzeriabusinessassets.dto.NewUserOrderDTO;
import org.clau.pizzeriabusinessassets.model.Cart;
import org.clau.pizzeriabusinessassets.model.CartItem;
import org.clau.pizzeriabusinessassets.model.Order;
import org.clau.pizzeriabusinessassets.model.OrderDetails;
import org.clau.pizzeriabusinessassets.util.OrderUtils;
import org.clau.pizzeriabusinessresourceserver.dao.OrderRepository;
import org.clau.pizzeriabusinessresourceserver.dao.projection.CreatedOnProjection;
import org.clau.pizzeriabusinessresourceserver.dao.projection.OrderProjection;
import org.clau.pizzeriabusinessresourceserver.dao.projection.OrderSummaryProjection;
import org.clau.pizzeriabusinessresourceserver.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;

	@Override
	public Optional<OrderProjection> findOrderDTOById(Long orderId) {
		return orderRepository.findOrderDTOById(orderId);
	}

	@Override
	public Order createAnonOrder(NewAnonOrderDTO newAnonOrder) {

		Cart cart = Cart.builder()
				.withTotalQuantity(newAnonOrder.cart().totalQuantity())
				.withTotalCost(newAnonOrder.cart().totalCost())
				.withTotalCostOffers(newAnonOrder.cart().totalCostOffers())
				.build();

		List<CartItem> items = newAnonOrder
				.cart()
				.cartItemsDTO()
				.stream()
				.map(cartItemDTO -> CartItem.builder()
						.withType(cartItemDTO.type())
						.withName(cartItemDTO.name())
						.withQuantity(cartItemDTO.quantity())
						.withDescription(cartItemDTO.description())
						.withPrice(cartItemDTO.price())
						.withFormats(cartItemDTO.formats())
						.build())
				.toList();

		for (CartItem cartItem : items) {
			cart.addItem(cartItem);
		}

		Order anonOrder = Order.builder()
				.withCreatedOn(LocalDateTime.now())
				.withFormattedCreatedOn(TimeUtils.formatDateAsString(TimeUtils.getNowAccountingDST()))
				.withAnonCustomerName(newAnonOrder.customer().name())
				.withAnonCustomerContactNumber(newAnonOrder.customer().contactNumber())
				.withAnonCustomerEmail(newAnonOrder.customer().email())
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

	@Override
	public Order createUserOrder(Long userId, NewUserOrderDTO newUserOrder) {

		Cart cart = Cart.builder()
				.withTotalQuantity(newUserOrder.cart().totalQuantity())
				.withTotalCost(newUserOrder.cart().totalCost())
				.withTotalCostOffers(newUserOrder.cart().totalCostOffers())
				.build();

		newUserOrder
				.cart()
				.cartItemsDTO()
				.stream()
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

		Order order = Order.builder()
				.withCreatedOn(LocalDateTime.now())
				.withFormattedCreatedOn(TimeUtils.formatDateAsString(TimeUtils.getNowAccountingDST()))
				.withUserId(userId)
				.withAddress(newUserOrder.address())
				.build();

		order.setOrderDetails(OrderDetails.builder()
				.withDeliveryTime(newUserOrder.orderDetails().deliveryTime())
				.withPaymentMethod(newUserOrder.orderDetails().paymentMethod())
				.withBillToChange(newUserOrder.orderDetails().billToChange())
				.withStorePickUp(newUserOrder.orderDetails().storePickUp())
				.withComment(newUserOrder.orderDetails().comment())
				.build());

		order.setCart(cart);

		if (null != newUserOrder.orderDetails().billToChange()) {
			order.getOrderDetails().setChangeToGive(OrderUtils.calculatePaymentChange(
					newUserOrder.orderDetails().billToChange(),
					newUserOrder.cart().totalCost(),
					newUserOrder.cart().totalCostOffers())
			);
		}

		return orderRepository.save(order);
	}

	@Override
	public void deleteUserOrderById(Long orderId) {
		orderRepository.deleteById(orderId);
	}

	@Override
	public Page<OrderSummaryProjection> findUserOrderSummary(Long userId, int size, int page) {
		Sort.TypedSort<Order> order = Sort.sort(Order.class);
		Sort sort = order.by(Order::getId).descending();
		PageRequest pageRequest = PageRequest.of(page, size, sort);
		return orderRepository.findAllByUserId(userId, pageRequest);
	}

	@Override
	public Optional<CreatedOnProjection> findCreatedOnDTOById(Long orderId) {
		return orderRepository.findCreatedOnById(orderId);
	}
}
