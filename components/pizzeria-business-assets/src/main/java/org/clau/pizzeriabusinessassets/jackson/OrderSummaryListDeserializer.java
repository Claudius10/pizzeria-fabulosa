package org.clau.pizzeriabusinessassets.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.clau.pizzeriabusinessassets.dto.OrderSummaryDTO;
import org.clau.pizzeriabusinessassets.dto.OrderSummaryListDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderSummaryListDeserializer extends JsonDeserializer<OrderSummaryListDTO> {

	@Override
	public OrderSummaryListDTO deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
		JsonNode root = jp.getCodec().readTree(jp);
		List<OrderSummaryDTO> content = new ArrayList<>();

		JsonNode contentArray = root.get("content");
		for (JsonNode orderNode : contentArray) {
			content.add(new OrderSummaryDTO(
					orderNode.get("id").asLong(),
					orderNode.get("formattedCreatedOn").asText(),
					orderNode.get("orderDetails").get("paymentMethod").asText(),
					orderNode.get("cart").get("totalQuantity").asInt(),
					orderNode.get("cart").get("totalCost").asDouble(),
					orderNode.get("cart").get("totalCostOffers").asDouble()
			));
		}

		return new OrderSummaryListDTO(
				content,
				root.get("number").asInt(),
				root.get("size").asInt(),
				root.get("totalElements").asLong(),
				root.get("last").asBoolean()
		);
	}
}
