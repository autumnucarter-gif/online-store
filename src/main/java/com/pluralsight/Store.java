package com.pluralsight;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                    inventory.add(new Product(SKU, productName, productPrice, department));
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


    public static void displayCart(ArrayList<Product> cart, Scanner scanner) {
      while (true) {
          System.out.printf("\n===== Your Shopping Cart =====");
          if (cart.isEmpty()) {
              System.out.println("Cart is currently empty!");
              System.out.println("Press enter to return to Main Menu: ");
              scanner.nextLine();
              return;
          }

          double totalAmount = 0.0;
          for (Product product : cart) {
              System.out.printf("- %-30s : $%.2f\n", product.getProductName(), product.getProductPrice());
              totalAmount += product.getProductPrice();
          }
          System.out.printf("Total Cost: $%.2f\n", totalAmount);

          System.out.print("\nSelect an option: [C] to continue to Check Out or [X] to return to Main Menu: ");
          String option = scanner.nextLine().trim().toUpperCase();

          if (option.equals("C")) {
              checkOut(cart, totalAmount, scanner);
              return;
          } else if (option.equals("X")) {
              return;
          }else {
              System.out.println("Invalid option. Please enter C or X: ");
          }
      }
    }

    public static void checkOut(ArrayList<Product> cart,
                                double totalAmount,
                                Scanner scanner) {
        System.out.printf("\nYour final balance due is: $%.2f\n", totalAmount);
        System.out.print("Confirm checkout? (Y/N): ");
        String confirmation = scanner.nextLine().trim().toUpperCase();

        if (!confirmation.equals("Y")) {
            System.out.println("Checkout canceled. Returning to cart. ");
            return;
        }
        double amountPaid = -1.0;
        while (amountPaid < totalAmount) {
            System.out.println("Enter your cash payment amount: $");
            if (!scanner.hasNextDouble()) {
                System.out.println("Invalid currency value. Please try again: ");
                scanner.nextLine();
                continue;
            }
            amountPaid = scanner.nextDouble();
            scanner.nextLine();

            if (amountPaid < totalAmount) {
                System.out.printf("Insufficient cash. You still owe  $%.2f\n",(totalAmount - amountPaid));
            }
        }
        double change = amountPaid - totalAmount;


        System.out.println("\n========================================\n");
        System.out.println("             ONLINE STORE               \n");
        System.out.println("========================================\n");
        for (Product product : cart) {
            System.out.printf("%-25s $%-10.2f\n", product.getProductName(), product.getProductPrice());
        }

        System.out.println("----------------------------------------\n");
        System.out.printf("%-25s $%-10.2f\n", "TOTAL OWED:", totalAmount);
        System.out.printf("%-25s $%-10.2f\n", "CASH PAID:", amountPaid);
        System.out.printf("%-25s $%-10.2f\n", "CHANGE GIVEN:", change);
        System.out.println("========================================\n");
        System.out.println("        THANK YOU FOR SHOPPING!         \n");
        System.out.println("========================================\n");

        //logReceipt(cart, totalAmount, amountPaid, change);
        cart.clear();

    }
    /*public static Product findProductById(String id, ArrayList<Product> inventory) {
        for (Product product : inventory) {
            if (product.getSKU().equalsIgnoreCase(id.trim())) {
                return product;
            }
        }
        return null;
    }
    /*public static void logReceipt(ArrayList<Product> cart,
                                  double totalAmount,
                                  double amountPaid,
                                  double change) {

        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMDddHHmm");
            String fileName = "Receipts" + now.format(formatter) + ".txt";


            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));

            bufferedWriter.write("========================================\n");
            bufferedWriter.write("              SALES RECEIPT            \n");
            bufferedWriter.write("========================================\n");
            for (Product product : cart) {
                bufferedWriter.write(String.format("%-30s $%.2f\n", product.getProductPrice(), product.getProductPrice()));
            }
            bufferedWriter.write("----------------------------------------\n");
            bufferedWriter.write(String.format("Subtotal Due: $%.2f\n", totalAmount));
            bufferedWriter.write(String.format("Cash Tendered: $%.2f\n", amountPaid));
            bufferedWriter.write(String.format("Change Returned: $%.2f\n", change));
            bufferedWriter.write("========================================\n");
            bufferedWriter.write(" Thank you for your purchase! \n");

            bufferedWriter.close();
            System.out.println("Receipt saved to: " + fileName + "! ");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/
}
