package org.clau.fabulosa.pizzeria.adminresourceserver;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.fabulosa.pizzeria.adminresourceserver.data.dao.AdminErrorRepository;
import org.clau.fabulosa.pizzeria.adminresourceserver.data.dao.OrderStatisticsRepository;
import org.clau.fabulosa.data.dto.business.NewUserOrderDTO;
import org.clau.fabulosa.data.model.business.Cart;
import org.clau.fabulosa.data.model.business.CartItem;
import org.clau.fabulosa.data.model.business.Order;
import org.clau.fabulosa.data.model.business.OrderDetails;
import org.clau.fabulosa.data.model.common.APIError;
import org.clau.fabulosa.utils.enums.OrderState;
import org.clau.fabulosa.utils.util.TimeUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class TestHelperService {

   private final AdminErrorRepository errorRepository;

   private final OrderStatisticsRepository orderRepository;

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
	  boolean randomBool = new Random().nextBoolean();

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
		 .withState(randomBool ? OrderState.COMPLETED.toString() : OrderState.CANCELLED.toString())
		 .withUserId(randomBool ? userId : null)
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

	  orderRepository.save(order);
   }
}
