package nl.butads.challenge.transferservice.config;

import nl.butads.challenge.transferservice.api.AccountExchangeApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerDocumentationConfig {

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("TransferService - The Transfer Service API")
            .description("Transfer Service API")
            .license("GNU GPLv3")
            .licenseUrl("https://choosealicense.com/licenses/gpl-3.0/")
            .version("1.0.0")
            .contact(new Contact("Mark Bertels", "", "mark.bertels@teamrockstars.nl"))
            .build();
    }

    @Bean
    public Docket customImplementation() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage(AccountExchangeApi.class.getPackage().getName()))
            .build()
            .apiInfo(apiInfo());
    }

}
