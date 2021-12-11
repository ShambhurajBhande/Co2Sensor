package com.co2.sensor;

import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;

@SpringBootApplication
public class Co2SensorsApplication {

	public static void main(String[] args) {
		SpringApplication.run(Co2SensorsApplication.class, args);
	}

	@Bean
	public OpenApiCustomiser openAPICustomiser() {
		return openApi -> {
			openApi.getComponents().getSchemas().forEach((s, schema) -> {
				Map<String, Schema> properties = schema.getProperties();
				if (properties == null) {
					properties = Map.of();
				}
				for (String propertyName : properties.keySet()) {
					Schema propertySchema = properties.get(propertyName);
					if (propertySchema instanceof DateTimeSchema) {
						properties.replace(propertyName, new StringSchema()
							.example("2021-07-05 10:35:17")
							.pattern("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")
							.description(((DateTimeSchema) propertySchema).getDescription()));
					}
				}
			});
		};
	}
}
