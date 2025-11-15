package org.clau.pizzeriabusinessresourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriabusinessresourceserver.dao.OrderRepository;
import org.clau.pizzeriabusinessresourceserver.service.AnonOrderService;
import org.clau.pizzeriabusinessresourceserver.util.OrderUtils;
import org.clau.pizzeriadata.dto.business.NewAnonOrderDTO;
import org.clau.pizzeriadata.model.business.Cart;
import org.clau.pizzeriadata.model.business.CartItem;
import org.clau.pizzeriadata.model.business.Order;
import org.clau.pizzeriadata.model.business.OrderDetails;
import org.clau.pizzeriautils.enums.OrderState;
import org.clau.pizzeriautils.util.TimeUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class AnonOrderServiceImpl implements AnonOrderService {

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
		 .withCreatedOn(TimeUtils.getNowAccountingDST())
		 .withFormattedCreatedOn(TimeUtils.formatDateAsString(TimeUtils.getNowAccountingDST()))
		 .withState(OrderState.COMPLETED.toString()) // todo - set to pending when created and figure out a way to pass them to completed
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
