package edu.school21.repositories;

import edu.school21.models.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProductsRepository {
    List<Product> findAll() throws SQLException;

    public Optional<Product> findById(Long id) throws SQLException;

    public void update(Product product) throws SQLException;

    public void save(Product product) throws SQLException;

    public void delete(Long id) throws SQLException;
}
