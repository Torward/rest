package ru.lomov.rest.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.lomov.rest.entity.Product;

import java.util.List;
import java.util.Optional;
@Repository
//@Qualifier("product")
public interface ProductDao extends JpaRepository<Product, Long> {

    Optional<Product> findByTitle(String title);
    List<Product> findAllByTitleContaining(String title);

}
