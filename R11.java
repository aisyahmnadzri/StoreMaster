// SECJ2154-01 MINI PROJECT
// MINI PROJECT TITLE: STOREMASTER RETAIL ORDERING SYSTEM
// 
// GROUP MEMBERS:
// 1. AISYAH BINTI MOHD NADZRI                
// 2. AFIQAH IZZATI BINTI AZZEROL EFFENDI     
// 3. MUHAMMAD IZAT BIN MD KAMIL              
// 4. NURATHIRAH BINTI MUHAMAD ZAKI           
// 5. MUHAMMAD HAZIQ BIN AZLI                 

import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

class FileManager {
    private static final String USERS_FILE_PATH = "users.txt";

    public void registerUser(String username, String password) {
        try {
            FileWriter writer = new FileWriter(USERS_FILE_PATH, true);
            writer.write(username + ":" + password + "\n");
            writer.close();
            System.out.println("User registration successful.");
        } catch (IOException e) {
            System.out.println("An error occurred while registering the user.");
        }
    }

    public boolean authenticateUser(String username, String password) {
        try {
            File file = new File(USERS_FILE_PATH);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    scanner.close();
                    return true;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("User file not found.");
        }
        return false;
    }
}

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

interface Orderable {
    double calculateTotalPrice();
}

abstract class Product {
    protected String name;
    protected double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public abstract int getQuantity();

    public abstract void setQuantity(int quantity);
}

class Category extends Product implements Orderable {
    private int quantity;

    public Category(String name, double price) {
        super(name, price);
        this.quantity = 0;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public double calculateTotalPrice() {
        return price * quantity;
    }

    @Override
    public String toString() {
        return name + " (RM" + price + ")";
    }
}

class ShoppingCart {
    private ArrayList<Product> items;

    ShoppingCart() {
        items = new ArrayList<>();
    }

    public ArrayList<Product> getItems() {
        return items;
    }

    public void addItem(Product item, int quantity) {
        item.setQuantity(quantity);
        items.add(item);
        //System.out.println("Item added to the cart.");
    }

    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            System.out.println("Item removed from the cart.");
        } else {
            System.out.println("Invalid item index.");
        }
    }

    public void updateItem(int index, Product item, int quantity) {
        if (index >= 0 && index < items.size()) {
            item.setQuantity(quantity);
            items.set(index, item);
            System.out.println("Item updated.");
        } else {
            System.out.println("Invalid item index.");
        }
    }

    public void displayCart() {
        if (items.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            System.out.println("Items in your cart:");
            for (int i = 0; i < items.size(); i++) {
                Product item = items.get(i);
                int quantity = item.getQuantity();
                System.out.println(i + 1 + ". " + item.getName() + " x " + quantity);
            }
        }
    }
}

enum OrderStatus {
    PENDING,
    COMPLETED
}

class Receipt {
    private ShoppingCart cart;
    private OrderStatus orderStatus;

    public Receipt(ShoppingCart cart) {
        this.cart = cart;
        this.orderStatus = OrderStatus.PENDING;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double calculateTotalPrice() {
        
        double totalPrice = 0.0;
        for (Product item : cart.getItems()) {
            //System.out.printf("%-25s %-10d RM%.2f\n", item.getName(), item.getQuantity(), item.getPrice());
            totalPrice += item.getPrice() * item.getQuantity(); //!nanti tukar guna Orderable class
        }

        return totalPrice;
    }

    public void printReceipt() {
        System.out.println("Details");
        System.out.println("=================================================");
        System.out.printf("%-25s%-10s%s%n", "Item", "Quantity", "Price");
        System.out.println("-------------------------------------------------");
        for (Product item : cart.getItems()) {
            int quantity = item.getQuantity();
            double price = item.getPrice();
            System.out.printf("%-25s%-10dRM%.2f%n", item.getName(), quantity, price);
        }
        System.out.println("\n-------------------------------------------------");
        System.out.printf("%-35sRM%.2f%n", "Total:", calculateTotalPrice());
        System.out.println("=================================================");
        System.out.println("Order Status: " + orderStatus);
    }

    public void makePayment() {
        orderStatus = OrderStatus.COMPLETED;
        System.out.println("Payment completed. Order status: " + orderStatus);
    }
}

public class R11 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FileManager fileManager = new FileManager();

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // User registration
        User user = new User(username, password);
        fileManager.registerUser(user.getUsername(), user.getPassword());

        // User authentication
        System.out.print("Enter username to authenticate: ");
        String authUsername = scanner.nextLine();

        System.out.print("Enter password to authenticate: ");
        String authPassword = scanner.nextLine();

        boolean isAuthenticated = fileManager.authenticateUser(authUsername, authPassword);

        if (isAuthenticated) {
            System.out.println("Authentication successful.");
        } else {
            System.out.println("Authentication failed.");
        }

        // Create some sample products
        Category item1 = new Category("Checkered Flannel", 59.00);
        Category item2 = new Category("Y2K Skirt", 19.99);
        Category item3 = new Category("Tank Top", 10.49);

        // Create a shopping cart
        ShoppingCart cart = new ShoppingCart();

        // Create a receipt
        Receipt receipt = new Receipt(cart);

        // Add sample products to the cart
        cart.addItem(item1, 1);
        cart.addItem(item2, 2);
        cart.addItem(item3, 3);

        //scanner.close();

        // Main program loop
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add item to cart");
            System.out.println("2. Delete item from cart");
            System.out.println("3. Update item in cart");
            System.out.println("4. View cart");
            System.out.println("5. Print receipt");
            System.out.println("6. Make payment");
            System.out.println("7. Exit");

            try {
                System.out.print("\nEnter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        System.out.println("\nAvailable items:");
                        System.out.println("1. " + item1);
                        System.out.println("2. " + item2);
                        System.out.println("3. " + item3);

                        System.out.print("\nEnter item number: ");
                        int itemNumber = scanner.nextInt();

                        System.out.print("Enter quantity: ");
                        int quantity = scanner.nextInt();

                        switch (itemNumber) {
                            case 1:
                                cart.addItem(item1, quantity);
                                break;
                            case 2:
                                cart.addItem(item2, quantity);
                                break;
                            case 3:
                                cart.addItem(item3, quantity);
                                break;
                            default:
                                System.out.println("Invalid item number.");
                                break;
                        }
                        break;

                    case 2:
                        System.out.println();
                        cart.displayCart();

                        System.out.print("\nEnter item index to delete: ");
                        int index = scanner.nextInt();
                        cart.removeItem(index - 1);
                        break;

                    case 3:
                        System.out.println();
                        cart.displayCart();

                        System.out.print("\nEnter item index to update: ");
                        int itemIndex = scanner.nextInt();

                        System.out.print("Enter new quantity: ");
                        int newQuantity = scanner.nextInt();

                        Product itemToUpdate = cart.getItems().get(itemIndex - 1);
                        cart.updateItem(itemIndex - 1, itemToUpdate, newQuantity);
                        break;

                    case 4:
                        System.out.println();
                        cart.displayCart();
                        break;

                    case 5:
                        System.out.println();
                        receipt.printReceipt();
                        break;

                    case 6:
                        System.out.println();
                        receipt.makePayment();
                        break;

                    case 7:
                        System.out.println("\nGoodbye!");
                        System.exit(0);
                        break;

                    default:
                        System.out.println("\nInvalid choice. Please try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("\nInvalid input. Please enter a number.");
                scanner.nextLine(); // Clear the input buffer
            }
        }
    }
}
