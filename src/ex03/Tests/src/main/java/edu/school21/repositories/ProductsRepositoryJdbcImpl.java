package edu.school21.repositories;

import edu.school21.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductsRepositoryJdbcImpl implements ProductsRepository {
    private final Connection connection;

    public ProductsRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public List<Product> findAll() throws SQLException {
        List<Product> productList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Long productId = resultSet.getLong("id");
                String productName = resultSet.getString("name");
                double productPrice = resultSet.getDouble("price");
                productList.add(new Product(productId, productName, productPrice));
            }
        }

        return productList;
    }

    @Override
    public Optional<Product> findById(Long id) throws SQLException {
        String sqlQuery = "SELECT * FROM product WHERE id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? Optional.of(new Product(resultSet.getLong("id"),
                        resultSet.getString("name"), resultSet.getDouble("price")))
                        : Optional.empty();
            }

        }
    }

    @Override
    public void update(Product product) throws SQLException {
        if (product == null || product.getId() == null) {
            throw new IllegalArgumentException("Product or its ID cannot be null.");
        }
        String sqlQuery = "UPDATE product SET name = ?, price = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setLong(3, product.getId());

            int count = preparedStatement.executeUpdate();

            if (count != 1) {
                throw new SQLException("Failed to update product. No rows affected or multiple rows updated.");
            }
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sqlQuery = "DELETE FROM product WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, id);

            int count = preparedStatement.executeUpdate();

            if (count != 1) {
                throw new SQLException("Failed to delete product. No rows affected or multiple rows deleted.");
            }
        }
    }

    @Override
    public void save(Product product) throws SQLException{
        String insertText = "INSERT INTO product (name, price) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertText)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new SQLException("Error while saving product: " + e.getMessage(), e);
        }
    }

}
