package com.monrabal.springboot.webflux.springbootwebflux.models.dao;

import com.monrabal.springboot.webflux.springbootwebflux.models.documents.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductoDao extends ReactiveMongoRepository<Producto, String> {

}
