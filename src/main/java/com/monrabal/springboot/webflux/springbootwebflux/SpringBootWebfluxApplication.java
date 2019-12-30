package com.monrabal.springboot.webflux.springbootwebflux;

import com.monrabal.springboot.webflux.springbootwebflux.models.dao.ProductoDao;
import com.monrabal.springboot.webflux.springbootwebflux.models.documents.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Date;

@SpringBootApplication
public class SpringBootWebfluxApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApplication.class);

	@Autowired
	private ProductoDao dao;

	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		mongoTemplate.dropCollection("productos")
		.subscribe();

		Flux.just(new Producto("TV Panasonic LCD", 456.25),
				new Producto("Playstation 4", 523.32),
				new Producto("Apple Watch", 1000.23),
				new Producto("Lenovo 17 ASD", 1253.32),
				new Producto("Apple XS Max", 1100.99),
				new Producto("Depeche Mode Box set", 250.23)
		)
		.flatMap(producto -> {
			producto.setCreatedAt(new Date());
			return dao.save(producto);
		})
		.subscribe(producto -> log.info("Insert " + producto.toString()));
	}
}
