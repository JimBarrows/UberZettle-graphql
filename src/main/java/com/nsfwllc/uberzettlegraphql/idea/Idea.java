package com.nsfwllc.uberzettlegraphql.idea;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Idea {
	@Id
	@UuidGenerator(style = UuidGenerator.Style.VERSION_7)
	@NotNull
	private UUID id;

	@Length(max = 500)
	@NotEmpty
	private String idea;
}
