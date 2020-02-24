package com.monrabal.springboot.webflux.springbootwebflux.models.dao;

import com.monrabal.springboot.webflux.springbootwebflux.models.documents.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryDao extends ReactiveMongoRepository<Category, String> {

}
