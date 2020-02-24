package com.monrabal.springboot.webflux.springbootwebflux.controllers;

import com.monrabal.springboot.webflux.springbootwebflux.models.documents.Producto;
import com.monrabal.springboot.webflux.springbootwebflux.models.services.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.Duration;
import java.util.Date;

@SessionAttributes("producto")
@Controller
public class ProductoController {

    private static final Logger log = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoService productoService;

    @GetMapping({"/list", "/"})
    public Mono<String> list(Model model) {
        Flux<Producto> productos = productoService.findAllWithNombreUpperCase();

        productos.subscribe(prod -> log.info(prod.getNombre()));

        model.addAttribute("products", productos);
        model.addAttribute("title", "Products list");
        return Mono.just("list");
    }

    @GetMapping("/form")
    public Mono<String> create(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("title", "New Product Form");
        model.addAttribute("button", "Create");
        return Mono.just("form");
    }

    @GetMapping("/form-v2/{id}")
    public Mono<String> editV2(@PathVariable String id, Model model) {

        return productoService.findById(id)
                .doOnNext(p -> {
                    log.info("Product" + p.toString());
                    model.addAttribute("Title", "Edit Product");
                    model.addAttribute("producto", p);
                    model.addAttribute("button", "Edit");
                }).defaultIfEmpty(new Producto())
                .flatMap(p -> {
                    if (p.getId() == null) {
                        return Mono.error(new InterruptedException("Product non existent"));
                    }
                    return Mono.just(p);
                })
                .then(Mono.just("form"))
                .onErrorResume(ex -> Mono.just("redirect:/list?error=Product+non+existent"));
    }

    @GetMapping("/form/{id}")
    public Mono<String> edit(@PathVariable String id, Model model) {
        Mono<Producto> product = productoService.findById(id)
                .doOnNext(p -> {
                    log.info("Product" + p.getNombre());
                }).defaultIfEmpty(new Producto());

        model.addAttribute("Title", "Edit Product");
        model.addAttribute("button", "Edit");
        model.addAttribute("producto", product);

        return Mono.just("form");
    }

    @PostMapping("/form")
    public Mono<String> save(@Valid Producto product, BindingResult result, Model model, SessionStatus status) {
        if (result.hasErrors()) {
            model.addAttribute("title", "Errors on form");
            model.addAttribute("button", "Save");
            return Mono.just("form");
        } else {
            status.setComplete();
            if (product.getCreatedAt() == null) {
                product.setCreatedAt(new Date());
            }
            return productoService.save(product)
                    .doOnNext(p -> log.info("Saved product: " + p.getNombre() + " Id:" + p.getId()))
                    .thenReturn("redirect:/list?success=Saved+succesfully");
        }
    }

    @GetMapping("/delete/{id}")
    public Mono<String> delete(@PathVariable String id) {
        return productoService.findById(id)
                .defaultIfEmpty(new Producto())
                .flatMap(p -> {
                    if (p.getId() == null) {
                        return Mono.error(new InterruptedException("Product non existent"));
                    }
                    return Mono.just(p);
                })
                .flatMap(productoService::delete)
                .then(Mono.just("redirect:/list?success=Product+deleted"))
                .onErrorResume(ex -> Mono.just("redirect:/list?error=Product+not+found"));
    }

    //Introducimos un delay y utilizamos el data driver para ir mostrando los datos mientras se "reciben"
    @GetMapping("/list-datadriver")
    public String listDataDriver(Model model) {
        Flux<Producto> productos = productoService.findAllWithNombreUpperCase()
                .delayElements(Duration.ofSeconds(1));

        productos.subscribe(prod -> log.info(prod.getNombre()));

        model.addAttribute("products", new ReactiveDataDriverContextVariable(productos, 1));
        model.addAttribute("title", "Products list");
        return "list";
    }

    @GetMapping("/list-full")
    public String listFull(Model model) {
        Flux<Producto> productos = productoService.findAllWithNombreUpperCaseAndRepeat();

        model.addAttribute("products", productos);
        model.addAttribute("title", "Products list");
        return "list";
    }

    @GetMapping("/list-chunked")
    public String listChunked(Model model) {
        Flux<Producto> productos = productoService.findAllWithNombreUpperCaseAndRepeat();

        model.addAttribute("products", productos);
        model.addAttribute("title", "Products list");
        return "list-chunked";
    }
}
