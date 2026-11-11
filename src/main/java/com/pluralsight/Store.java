package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;


public class Store {

    public static void main(String[] args) {

        // Create lists for inventory and the shopping cart
        ArrayList<Product> inventory = new ArrayList<>();
        ArrayList<Product> cart = new ArrayList<>();

        // Load inventory from the data file (pipe-delimited: SKU|Product Name|Price|Department)
        loadInventory("products.csv", inventory);

        // Main menu loop
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        while (choice != 3) {
            System.out.println("\nWelcome to the Online Store!");
            System.out.println("1. Show Products");
            System.out.println("2. Show Cart");
            System.out.println("3. Exit");
            System.out.print("Your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter 1, 2, or 3.");
                scanner.nextLine();                 // discard bad input
                continue;
            }
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> displayProducts(inventory, cart, scanner);
                case 2 -> displayCart(cart, scanner);
                case 3 -> System.out.println("Thank you for shopping with us!");
                default -> System.out.println("Invalid choice!");
            }
        }

    }


    public static void loadInventory(String fileName, ArrayList<Product> inventory) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String csvLine;
            boolean isHeader = true;

            while ((csvLine = bufferedReader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] token = csvLine.split("\\|");
                if (token.length == 4) {
                    String SKU = token[0].trim();
                    String productName = token[1].trim();
                    double productPrice = Double.parseDouble(token[2].trim());
                    String department = token[3].trim();
                }
            }
        } catch (Exception e) {
            System.out.println("Sorry, an error occurred 1. ");
        }
    }

    public static void displayProducts(ArrayList<Product> inventory,
                                       ArrayList<Product> cart,
                                       Scanner scanner) {

       System.out.println("\n ==== Available Products ==== ");
        for (Product product : inventory) {
            System.out.printf("SKU: %-8s | Name: %30s | Price: $%.2f | Dept: %s\n",
                    product.getSKU(), product.getProductName(), product.getProductPrice(), product.getDepartment());
        }
        while (true) {
            System.out.println("\nEnter the SKU of the product to add to cart (press 'x' to go back: )");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("X")) {
                return;
            }

            Product selectedProduct = findProductById(input, inventory);
            if (selectedProduct != null) {
                cart.add(selectedProduct);
                System.out.println("\"" + selectedProduct.getProductName() + "\" has been added to your cart.");
            }else {
                System.out.println("Sorry, SKU not found. Please try again: ");
            }
        }
    }

    /**
     * Shows the contents of the cart, calculates the total,
     * and offers the option to check out.
     */
    public static void displayCart(ArrayList<Product> cart, Scanner scanner) {
        // TODO:
        //   • list each product in the cart
        //   • compute the total cost
        //   • ask the user whether to check out (C) or return (X)
        //   • if C, call checkOut(cart, totalAmount, scanner)
    }

    /**
     * Handles the checkout process:
     * 1. Confirm that the user wants to buy.
     * 2. Accept payment and calculate change.
     * 3. Display a simple receipt.
     * 4. Clear the cart.
     */
    public static void checkOut(ArrayList<Product> cart,
                                double totalAmount,
                                Scanner scanner) {
        // TODO: implement steps listed above
    }

    /**
     * Searches a list for a product by its id.
     *
     * @return the matching Product, or null if not found
     */
    public static Product findProductById(String id, ArrayList<Product> inventory) {
        // TODO: loop over the list and compare ids
        return null;
    }
}
