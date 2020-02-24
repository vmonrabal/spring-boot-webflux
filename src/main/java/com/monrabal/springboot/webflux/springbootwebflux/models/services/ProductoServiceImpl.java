package com.monrabal.springboot.webflux.springbootwebflux.models.services;

import com.monrabal.springboot.webflux.springbootwebflux.models.dao.CategoryDao;
import com.monrabal.springboot.webflux.springbootwebflux.models.dao.ProductoDao;
import com.monrabal.springboot.webflux.springbootwebflux.models.documents.Category;
import com.monrabal.springboot.webflux.springbootwebflux.models.documents.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoDao dao;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public Flux<Producto> findAll() {
        return dao.findAll();
    }

    @Override
    public Flux<Producto> findAllWithNombreUpperCase() {
        return dao.findAll().map(producto -> {
            producto.setNombre(producto.getNombre().toUpperCase());
            return producto;
        });
    }

    @Override
    public Flux<Producto> findAllWithNombreUpperCaseAndRepeat() {
        return this.findAllWithNombreUpperCase().repeat(5000);
    }


    @Override
    public Mono<Producto> findById(String id) {
        return dao.findById(id);
    }

    @Override
    public Mono<Producto> save(Producto product) {
        return dao.save(product);
    }

    @Override
    public Mono<Void> delete(Producto product) {
        return dao.delete(product);
    }

    @Override
    public Flux<Category> findAllCategories() {
        return categoryDao.findAll();
    }

    @Override
    public Mono<Category> findCategoryById(String id) {
        return categoryDao.findById(id);
    }

    @Override
    public Mono<Category> saveCategory(Category category) {
        return categoryDao.save(category);
    }


}
