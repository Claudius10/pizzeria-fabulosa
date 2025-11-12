package org.clau.pizzeriabusinessresourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriabusinessresourceserver.dao.OrderRepository;
import org.clau.pizzeriabusinessresourceserver.dao.projection.CreatedOnProjection;
import org.clau.pizzeriabusinessresourceserver.service.OrderService;
import org.clau.pizzeriabusinessresourceserver.util.OrderUtils;
import org.clau.pizzeriadata.dto.business.NewUserOrderDTO;
import org.clau.pizzeriadata.model.business.Cart;
import org.clau.pizzeriadata.model.business.CartItem;
import org.clau.pizzeriadata.model.business.Order;
import org.clau.pizzeriadata.model.business.OrderDetails;
import org.clau.pizzeriautils.enums.OrderState;
import org.clau.pizzeriautils.util.TimeUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

   private final OrderRepository orderRepository;

   @Override
   public Optional<Order> findById(Long orderId) {
	  return orderRepository.findById(orderId);
   }

   @Override
   public Order create(Long userId, NewUserOrderDTO newUserOrder) {

	  Cart cart = Cart.builder()
		 .withTotalQuantity(newUserOrder.cart().totalQuantity())
		 .withTotalCost(newUserOrder.cart().totalCost())
		 .withTotalCostOffers(newUserOrder.cart().totalCostOffers())
		 .build();

	  newUserOrder.cart().cartItems().stream()
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
		 .withCreatedOn(TimeUtils.getNowAccountingDST())
		 .withFormattedCreatedOn(TimeUtils.formatDateAsString(TimeUtils.getNowAccountingDST()))
		 .withState(OrderState.COMPLETED.toString()) // todo - set to pending when created and figure out a way to pass them to completed
		 .withUserId(userId)
		 .withAddress(newUserOrder.address())
		 .build();

	  order.setOrderDetails(
		 OrderDetails.builder()
			.withDeliveryTime(newUserOrder.orderDetails().deliveryTime())
			.withPaymentMethod(newUserOrder.orderDetails().paymentMethod())
			.withBillToChange(newUserOrder.orderDetails().billToChange())
			.withStorePickUp(newUserOrder.orderDetails().storePickUp())
			.withComment(newUserOrder.orderDetails().comment())
			.build()
	  );

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
   public void cancelById(Long orderId) {
	  orderRepository.updateState(orderId, OrderState.CANCELLED.toString());
   }

   @Override
   public Page<Order> findSummary(Long userId, int size, int page) {
	  PageRequest pageRequest = PageRequest.of(page, size, Sort.sort(Order.class).by(Order::getId).descending());
	  return orderRepository.findAllByUserId(userId, pageRequest);
   }

   // for internal use only

   @Override
   public Optional<CreatedOnProjection> findCreatedOnById(Long orderId) {
	  return orderRepository.findCreatedOnById(orderId);
   }
}
