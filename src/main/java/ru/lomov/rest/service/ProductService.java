package ru.lomov.rest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.lomov.rest.controller.dto.ProductDto;
import ru.lomov.rest.dao.ProductDao;
import ru.lomov.rest.entity.Product;
import ru.lomov.rest.entity.enums.Status;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductDao productDao;

    public ProductDto save(ProductDto productDto) {
        Product savingProduct;
        if (productDto.getId() != null) {
            Optional<Product> productFromDBOptional = productDao.findById(productDto.getId());
            savingProduct = productFromDBOptional.orElseGet(Product::new);
        } else {
            savingProduct = new Product();
        }

        savingProduct.setTitle(productDto.getTitle());
        savingProduct.setCost(productDto.getCost());
        savingProduct.setManufactureDate(productDto.getManufactureDate());
        savingProduct.setStatus(productDto.getStatus());
        savingProduct = productDao.save(savingProduct);
        productDto.setId(savingProduct.getId());
        return productDto;
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productDao.findById(id).orElse(null);
    }

    public List<Product> findAll() {
        return productDao.findAll();
    }


    public void deleteById(Long id) {
        try {
            productDao.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
        }
    }

    public void disable(Long id) {
        Optional<Product> product = productDao.findById(id);
        product.ifPresent(p -> {
            p.setStatus(Status.DISABLED);
            productDao.save(p);
        });
    }


}
