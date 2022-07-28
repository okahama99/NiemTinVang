package com.ntv.ntvcons_backend;

import com.ntv.ntvcons_backend.utils.MiscUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.format.DateTimeFormatter;


@SpringBootApplication
@EnableJpaAuditing
@EnableSwagger2
public class NtvConsBackendApplication {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration()
				.setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}

	@Bean
	public DateTimeFormatter dateTimeFormatter() {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	}

	@Bean
	public MiscUtil thanhUtil() {
		return new MiscUtil();
	}

	public static void main(String[] args) {
		SpringApplication.run(NtvConsBackendApplication.class, args);
	}

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//		return builder.sources(NtvConsBackendApplication.class);
//	}

	@Bean
	public WebMvcConfigurer corsConfigurer(){
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("*")
						.allowedMethods("GET","POST","PUT","DELETE")
						.allowedHeaders("*")
						.allowCredentials(false);
			}
		};
	}
}
