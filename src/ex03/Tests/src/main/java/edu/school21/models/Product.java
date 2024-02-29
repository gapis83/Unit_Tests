package edu.school21.models;

import java.util.Objects;

public class Product {
    Long id;
    String name;
    double price;

    public Product(Long id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Product product = (Product) obj;
        boolean idEquals = Objects.equals(id, product.id);
        boolean nameEquals = Objects.equals(name, product.name);
        boolean priceEquals = Objects.equals(price, product.price);

        return idEquals && priceEquals && nameEquals;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name,  price);
    }

}
