package org.clau.pizzeriabusinessassets.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.clau.pizzeriabusinessassets.dto.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderDeserializer extends JsonDeserializer<CreatedOrderDTO> {

	private final ObjectMapper mapper = new ObjectMapper();

	private final TypeReference<Map<String, String>> nameMapReference = new TypeReference<>() {
	};

	private final TypeReference<Map<String, List<String>>> descriptionMapReference = new TypeReference<>() {
	};

	private final TypeReference<Map<String, Map<String, String>>> formatMapReference = new TypeReference<>() {
	};

	@Override
	public CreatedOrderDTO deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
		JsonNode root = jp.getCodec().readTree(jp);

		CustomerDTO customer = buildCustomerDTO(root);
		OrderDetailsDTO orderDetailsDTO = buildOrderDetailsDTO(root.get("orderDetails"));
		CartDTO cartDTO = buildCartDTO(root.get("cart"));

		Long id = root.get("id").asLong();
		String formattedCreatedOn = root.get("formattedCreatedOn").asText();
		String address = root.get("address").asText();

		return new CreatedOrderDTO(id, formattedCreatedOn, customer, address, orderDetailsDTO, cartDTO);
	}

	private CustomerDTO buildCustomerDTO(JsonNode root) {

		String anonCustomerName = root.get("anonCustomerName").isNull() ? null : root.get("anonCustomerName").asText();
		Integer anonCustomerContactNumber = root.get("anonCustomerContactNumber").isNull() ? null : root.get("anonCustomerContactNumber").asInt();
		String anonCustomerEmail = root.get("anonCustomerEmail").isNull() ? null : root.get("anonCustomerEmail").asText();

		return new CustomerDTO(anonCustomerName, anonCustomerContactNumber, anonCustomerEmail);
	}

	private OrderDetailsDTO buildOrderDetailsDTO(JsonNode orderDetails) {

		String deliveryTime = orderDetails.get("deliveryTime").asText();
		String paymentMethod = orderDetails.get("paymentMethod").asText();
		Boolean storePickUp = orderDetails.get("storePickUp").asBoolean();

		Double billToChange = orderDetails.get("billToChange").isNull() ? null : orderDetails.get("billToChange").asDouble();
		String comment = orderDetails.get("comment").isNull() ? null : orderDetails.get("comment").asText();
		Double changeToGive = orderDetails.get("changeToGive").isNull() ? null : orderDetails.get("changeToGive").asDouble();

		return new OrderDetailsDTO(deliveryTime, paymentMethod, billToChange, comment, storePickUp, changeToGive);
	}

	private CartDTO buildCartDTO(JsonNode cart) {

		Integer totalQuantity = cart.get("totalQuantity").asInt();
		Double totalCost = cart.get("totalCost").asDouble();
		Double totalCostOffers = cart.get("totalCostOffers").isNull() ? null : cart.get("totalCostOffers").asDouble();

		List<CartItemDTO> cartItemsDTO = buildCartItemsDTO(cart.get("cartItems"));

		return new CartDTO(totalQuantity, totalCost, totalCostOffers, cartItemsDTO);
	}

	private List<CartItemDTO> buildCartItemsDTO(JsonNode cartItems) {
		List<CartItemDTO> cartItemsDTO = new ArrayList<>();

		for (JsonNode item : cartItems) {

			Long id = item.get("id").asLong();
			String type = item.get("type").asText();
			Double price = item.get("price").asDouble();
			Integer quantity = item.get("quantity").asInt();

			Map<String, String> name = mapper.convertValue(item.get("name"), nameMapReference);
			Map<String, List<String>> description = mapper.convertValue(item.get("description"), descriptionMapReference);
			Map<String, Map<String, String>> formats = mapper.convertValue(item.get("formats"), formatMapReference);

			cartItemsDTO.add(new CartItemDTO(
					id,
					type,
					price,
					quantity,
					name,
					description,
					formats
			));
		}

		return cartItemsDTO;
	}
}
