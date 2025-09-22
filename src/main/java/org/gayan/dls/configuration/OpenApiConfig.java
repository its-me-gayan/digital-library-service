package org.gayan.dls.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Author: Gayan Sanjeewa User: gayan Date: 9/22/25 Time: 11:01 PM */
@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Digital Library Service API")
                .version("1.0")
                .description("API documentation for Digital Library Service"));
  }
}
