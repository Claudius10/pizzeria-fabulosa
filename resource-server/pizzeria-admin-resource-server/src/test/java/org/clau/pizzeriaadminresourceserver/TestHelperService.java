package org.clau.pizzeriaadminresourceserver;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriadata.dao.admin.AdminErrorRepository;
import org.clau.pizzeriadata.dao.admin.AdminOrderRepository;
import org.clau.pizzeriadata.model.business.Cart;
import org.clau.pizzeriadata.model.business.CartItem;
import org.clau.pizzeriadata.model.business.Order;
import org.clau.pizzeriadata.model.business.OrderDetails;
import org.clau.pizzeriadata.model.common.APIError;
import org.clau.pizzeriautils.constant.business.OrderState;
import org.clau.pizzeriautils.dto.business.NewUserOrderDTO;
import org.clau.pizzeriautils.util.business.OrderUtils;
import org.clau.pizzeriautils.util.common.TimeUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class TestHelperService {

   private final AdminErrorRepository errorRepository;

   private final AdminOrderRepository orderRepository;

   public void createApiError(String cause, String message, String origin, String uriPath, boolean fatal, LocalDateTime createdOn) {

	  APIError error = APIError.builder()
		 .withCreatedOn(createdOn)
		 .withCause(cause)
		 .withMessage(message)
		 .withOrigin(origin)
		 .withPath(uriPath)
		 .withLogged(true)
		 .withFatal(fatal)
		 .build();

	  errorRepository.save(error);
   }

   public void createOrder(Long userId, NewUserOrderDTO newUserOrder, LocalDateTime createdOn) {

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
		 .withCreatedOn(createdOn)
		 .withFormattedCreatedOn(TimeUtils.formatDateAsString(TimeUtils.getNowAccountingDST()))
		 .withState(OrderState.COMPLETED.toString())
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

	  orderRepository.save(order);
   }
}
