package org.clau.pizzeriastoreassets.model;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.Map;

@Entity(name = "Store")
@Table(name = "store")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(setterPrefix = "with")
@EqualsAndHashCode
public class Store {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "store_generator")
	@SequenceGenerator(name = "store_generator", sequenceName = "store_seq", allocationSize = 1)
	private Long id;

	@NotNull
	private String image;

	@NotNull
	private String name;

	@NotNull
	private Integer phoneNumber;

	@Type(JsonType.class)
	@Column(columnDefinition = "json")
	@NotNull
	private Map<String, String> schedule;

	@NotNull
	private String address;
}