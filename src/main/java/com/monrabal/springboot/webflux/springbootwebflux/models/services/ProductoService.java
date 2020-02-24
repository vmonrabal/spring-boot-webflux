package com.monrabal.springboot.webflux.springbootwebflux.models.services;

import com.monrabal.springboot.webflux.springbootwebflux.models.documents.Category;
import com.monrabal.springboot.webflux.springbootwebflux.models.documents.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {

    Flux<Producto> findAll();

    Flux<Producto> findAllWithNombreUpperCase();

    Flux<Producto> findAllWithNombreUpperCaseAndRepeat();

    Mono<Producto> findById(String id);

    Mono<Producto> save(Producto product);

    Mono<Void> delete(Producto product);

    Flux<Category> findAllCategories();

    Mono<Category> findCategoryById(String id);

    Mono<Category> saveCategory(Category category);

}
