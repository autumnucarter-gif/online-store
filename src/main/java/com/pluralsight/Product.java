package com.pluralsight;

public class Product {
    private String SKU;
    private String productName;
    private double productPrice;
    private String department;

    public Product(String SKU, String productName, double productPrice, String department) {
        this.SKU = SKU;
        this.productName = productName;
        this.productPrice = productPrice;
        department = department;
    }

    public String getSKU() {
        return SKU;
    }

    public String getProductName() {
        return productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | $%.2f | %s", SKU, productName, productPrice, department);
    }

}
