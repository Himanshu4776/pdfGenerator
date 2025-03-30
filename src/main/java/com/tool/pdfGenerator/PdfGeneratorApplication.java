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

}
