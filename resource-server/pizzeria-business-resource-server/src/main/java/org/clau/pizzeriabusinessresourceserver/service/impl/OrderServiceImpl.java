package org.clau.pizzeriabusinessresourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriabusinessresourceserver.service.OrderService;
import org.clau.pizzeriadata.dao.business.OrderRepository;
import org.clau.pizzeriadata.dao.business.projection.CreatedOnProjection;
import org.clau.pizzeriadata.model.business.Cart;
import org.clau.pizzeriadata.model.business.CartItem;
import org.clau.pizzeriadata.model.business.Order;
import org.clau.pizzeriadata.model.business.OrderDetails;
import org.clau.pizzeriautils.dto.business.NewUserOrderDTO;
import org.clau.pizzeriautils.util.business.OrderUtils;
import org.clau.pizzeriautils.util.common.TimeUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
		 .withCreatedOn(LocalDateTime.now())
		 .withFormattedCreatedOn(TimeUtils.formatDateAsString(TimeUtils.getNowAccountingDST()))
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
   public void deleteById(Long orderId) {
	  orderRepository.deleteById(orderId);
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
