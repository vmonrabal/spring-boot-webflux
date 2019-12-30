package com.monrabal.springboot.webflux.springbootwebflux.controllers;

import com.monrabal.springboot.webflux.springbootwebflux.models.dao.ProductoDao;
import com.monrabal.springboot.webflux.springbootwebflux.models.documents.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Controller
public class ProductoController {

    private static final Logger log = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoDao dao;

    @GetMapping({"/list", "/"})
    public String list(Model model){
        Flux<Producto> productos = dao.findAll()
                .map(producto -> {
                    producto.setNombre(producto.getNombre().toUpperCase());
                    return producto;
                });

        productos.subscribe(prod -> log.info(prod.getNombre()));

        model.addAttribute("products", productos);
        model.addAttribute("title", "Products list");
        return "list";
    }

    //Introducimos un delay y utilizamos el data driver para ir mostrando los datos mientras se "reciben"
    @GetMapping("/list-datadriver")
    public String listDataDriver(Model model){
        Flux<Producto> productos = dao.findAll()
            .map(producto -> {
                producto.setNombre(producto.getNombre().toUpperCase());
                return producto;
            })
            .delayElements(Duration.ofSeconds(1));

        productos.subscribe(prod -> log.info(prod.getNombre()));

        model.addAttribute("products", new ReactiveDataDriverContextVariable(productos, 1));
        model.addAttribute("title", "Products list");
        return "list";
    }

    @GetMapping("/list-full")
    public String listFull(Model model){
        Flux<Producto> productos = dao.findAll()
            .map(producto -> {
                producto.setNombre(producto.getNombre().toUpperCase());
                return producto;
            })
            .repeat(5000);

        model.addAttribute("products", productos);
        model.addAttribute("title", "Products list");
        return "list";
    }

    @GetMapping("/list-chunked")
    public String listChunked(Model model){
        Flux<Producto> productos = dao.findAll()
                .map(producto -> {
                    producto.setNombre(producto.getNombre().toUpperCase());
                    return producto;
                })
                .repeat(5000);

        model.addAttribute("products", productos);
        model.addAttribute("title", "Products list");
        return "list-chunked";
    }
}
