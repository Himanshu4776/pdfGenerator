package com.tool.pdfGenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;

@SpringBootApplication
public class PdfGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdfGeneratorApplication.class, args);

		// Create PDF storage directory if it doesn't exist
		File directory = new File("pdf-storage");
		if (!directory.exists()) {
			directory.mkdir();
		}
	}

//	@Bean
//	public Docket api() {
//		return new Docket(DocumentationType.SWAGGER_2)
//				.select()
//				.apis(RequestHandlerSelectors.basePackage("com.invoice.controller"))
//				.paths(PathSelectors.any())
//				.build()
//				.apiInfo(apiInfo());
//	}
//
//	private ApiInfo apiInfo() {
//		return new ApiInfoBuilder()
//				.title("Pdf Generator API")
//				.description("API for generating PDFs")
//				.version("1.0")
//				.contact(new Contact("Your Name", "https://your-website.com", "your-email@example.com"))
//				.build();
//	}

}
