package com.monrabal.springboot.webflux.springbootwebflux;

import com.monrabal.springboot.webflux.springbootwebflux.models.documents.Category;
import com.monrabal.springboot.webflux.springbootwebflux.models.documents.Producto;
import com.monrabal.springboot.webflux.springbootwebflux.models.services.ProductoService;
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
    private ProductoService service;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebfluxApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        mongoTemplate.dropCollection("productos");
        mongoTemplate.dropCollection("categories");

        Category electronic = new Category("Electronic");
        Category sports = new Category("Sports");
        Category computers = new Category("Computers");
        Category music = new Category("Music");

        Flux.just(electronic, sports, computers, music)
                .flatMap(service::saveCategory)
                .doOnNext(c -> {
                    log.info("Category created " + c.getName());
                }).thenMany(
                Flux.just(new Producto("TV Panasonic LCD", 456.25, electronic),
                        new Producto("Playstation 4", 523.32, electronic),
                        new Producto("Apple Watch", 1000.23, electronic),
                        new Producto("Lenovo 17 ASD", 1253.32, computers),
                        new Producto("Apple XS Max", 1100.99, electronic),
                        new Producto("Depeche Mode Box set", 250.23, music),
                        new Producto("Bike", 250.23, sports)
                )
                        .flatMap(producto -> {
                            producto.setCreatedAt(new Date());
                            return service.save(producto);
                        })
        ).subscribe(producto -> log.info("Insert " + producto.toString()));
    }
}
