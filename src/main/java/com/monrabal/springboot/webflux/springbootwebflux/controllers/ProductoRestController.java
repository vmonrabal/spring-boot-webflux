package com.monrabal.springboot.webflux.springbootwebflux.controllers;

import com.monrabal.springboot.webflux.springbootwebflux.models.dao.ProductoDao;
import com.monrabal.springboot.webflux.springbootwebflux.models.documents.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
public class ProductoRestController {
    private static final Logger log = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoDao dao;

    @GetMapping
    public Flux<Producto> index(){
        Flux<Producto> productos = dao.findAll()
            .map(producto -> {
                producto.setNombre(producto.getNombre().toUpperCase());
                return producto;
            }).doOnNext(prod -> log.info(prod.toString()));

        return productos;
    }

    @GetMapping("/{id}")
    public Mono<Producto> show(@PathVariable String id){
        Mono<Producto> product = dao.findById(id);
        //Flux<Producto> productos = dao.findAll();
        //Mono<Producto> product = productos.filter(producto -> producto.getId().equals(id)).next()
        //        .doOnNext(prod -> log.info(prod.toString()));
        return product;
    }
}
