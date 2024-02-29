package edu.school21.repositories;

import edu.school21.models.Product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductsRepositoryJdbcImplTest {
    private EmbeddedDataSourceTest embeddedDataSourceTest;
    ProductsRepository productsRepository;
    private final List<Product> EXPECTED_FIND_ALL_PRODUCTS = new ArrayList<>();
    private final Product EXPECTED_FIND_BY_ID_PRODUCT = new Product(1L, "Product1", 10.99);
    private final Product EXPECTED_UPDATED_PRODUCT = new Product(1L, "UpdatedProduct", 15.99);
    final Product EXPECTED_SAVE_PRODUCT = new Product(6L, "Product6", 10.0);
    final Product EXPECTED_DELETE_PRODUCT = new Product(5L, "Product5", 8.95);

    @BeforeEach
    void setUp() {
        embeddedDataSourceTest = new EmbeddedDataSourceTest();
        embeddedDataSourceTest.init();
        productsRepository = new ProductsRepositoryJdbcImpl(embeddedDataSourceTest.getConnection());
        initializeDatabase();
    }

    @Test
    void testFindAllShouldReturnTrue() {
        try {
            List<Product> actualProducts = productsRepository.findAll();
            assertEquals(EXPECTED_FIND_ALL_PRODUCTS, actualProducts);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    void testFindByIdShouldReturnTrue() {
        try {
            Optional<Product> actualProduct = productsRepository.findById(1L);
            assertEquals(EXPECTED_FIND_BY_ID_PRODUCT, actualProduct.orElse(null));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    void testFindByIdShouldReturnNull() {
        Long NonExistentProductId = 100L;
        try {
            Optional<Product> actualProduct = productsRepository.findById(NonExistentProductId);
            assertNull(actualProduct.orElse(null));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    void testUpdateShouldReturnTrue() {
        try {
            productsRepository.update(EXPECTED_UPDATED_PRODUCT);
            Optional<Product> updatedProduct = productsRepository.findById(1L);
            assertEquals(EXPECTED_UPDATED_PRODUCT, updatedProduct.orElse(null));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    void testUpdateShouldReturnFalse() {
        Product product = new Product(90L, null, 0);
        try {
            productsRepository.update(product);
            Optional<Product> updatedProduct = productsRepository.findById(product.getId());
            assertNull(updatedProduct.orElse(null));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    void testUpdateThrowsException() {
        Product product = new Product(null, null, 0);
        assertThrows(IllegalArgumentException.class, () -> {
            productsRepository.update(product);
        });
    }

    @Test
    void testSaveShouldReturnTrue() {
        try {
            productsRepository.save(EXPECTED_SAVE_PRODUCT);
            Optional<Product> savedProduct = productsRepository.findById(6L);
            assertEquals(EXPECTED_SAVE_PRODUCT, savedProduct.orElse(null));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

     @Test
    void testSaveShouldThrowSQLException() {
    embeddedDataSourceTest.closeConnection();
         assertThrows(SQLException.class, () -> {
             productsRepository.save(EXPECTED_SAVE_PRODUCT);
         });
     }

    @Test
    void testDeleteShouldReturnTrue() {
        try {
            Optional<Product> actualProduct = productsRepository.findById(EXPECTED_DELETE_PRODUCT.getId());
            assertEquals(EXPECTED_DELETE_PRODUCT, actualProduct.orElse(null));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        try {
            productsRepository.delete(EXPECTED_DELETE_PRODUCT.getId());
            Optional<Product> deletedProduct = productsRepository.findById(EXPECTED_DELETE_PRODUCT.getId());
            assertFalse(deletedProduct.isPresent());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    void testDeleteShouldThrowsException() {
        assertThrows(SQLException.class, () -> {
            productsRepository.delete(100L);
        });
    }

    @AfterEach
    void tearDown() {
        if (embeddedDataSourceTest.getConnection() != null) {
            embeddedDataSourceTest.closeConnection();
        }
    }

    private void initializeDatabase() {
        EXPECTED_FIND_ALL_PRODUCTS.add(new Product(1L, "Product1", 10.99));
        EXPECTED_FIND_ALL_PRODUCTS.add(new Product(2L, "Product2", 19.99));
        EXPECTED_FIND_ALL_PRODUCTS.add(new Product(3L, "Product3", 5.49));
        EXPECTED_FIND_ALL_PRODUCTS.add(new Product(4L, "Product4", 15.79));
        EXPECTED_FIND_ALL_PRODUCTS.add(new Product(5L, "Product5", 8.95));
    }
}
