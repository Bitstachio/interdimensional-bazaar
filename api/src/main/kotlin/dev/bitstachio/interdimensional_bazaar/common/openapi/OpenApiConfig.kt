package dev.bitstachio.interdimensional_bazaar.common.openapi

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

	@Bean
	fun openAPI(): OpenAPI =
		OpenAPI()
			.info(
				Info()
					.title("Interdimensional Bazaar API")
					.description("REST API for the Interdimensional Bazaar e-commerce backend.")
					.version("0.0.1"),
			)
}
