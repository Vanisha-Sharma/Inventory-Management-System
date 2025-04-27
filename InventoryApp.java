import java.io.*;
import java.util.*;

class Product implements Serializable {
    private String id;
    private String name;
    private double price;
    private int quantity;

    public Product(String id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return String.format("ID: %s | Name: %s | Price: $%.2f | Qty: %d", id, name, price, quantity);
    }
}

class InventoryManager {
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
    }

    public boolean updateStock(String productId, int quantityChange) {
        for (Product p : products) {
            if (p.getId().equals(productId)) {
                p.setQuantity(p.getQuantity() + quantityChange);
                return true;
            }
        }
        return false;
    }

    public Product findProduct(String productId) {
        for (Product p : products) {
            if (p.getId().equals(productId)) {
                return p;
            }
        }
        return null;
    }

    public void listProducts() {
        if (products.isEmpty()) {
            System.out.println("No products in inventory.");
            return;
        }
        System.out.println("\n--- INVENTORY ---");
        products.forEach(System.out::println);
    }

    public void saveToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(products);
            System.out.println("Inventory saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving inventory: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            products = (List<Product>) ois.readObject();
            System.out.println("Inventory loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading inventory: " + e.getMessage());
        }
    }
}

public class InventoryApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InventoryManager manager = new InventoryManager();
        String filename = "inventory.dat";

        manager.loadFromFile(filename);

        while (true) {
            System.out.println("\n=== INVENTORY MANAGEMENT ===");
            System.out.println("1. Add Product");
            System.out.println("2. Update Stock");
            System.out.println("3. View Products");
            System.out.println("4. Save & Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.print("Enter Product ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter Product Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Product Price: ");
                    double price = scanner.nextDouble();
                    System.out.print("Enter Initial Quantity: ");
                    int qty = scanner.nextInt();
                    manager.addProduct(new Product(id, name, price, qty));
                    System.out.println("Product added successfully!");
                    break;

                case 2: 
                    System.out.print("Enter Product ID: ");
                    String pid = scanner.nextLine();
                    System.out.print("Enter Quantity Change (+/-): ");
                    int change = scanner.nextInt();
                    if (manager.updateStock(pid, change)) {
                        System.out.println("Stock updated!");
                    } else {
                        System.out.println("Product not found!");
                    }
                    break;

                case 3: 
                    manager.listProducts();
                    break;

                case 4: 
                    manager.saveToFile(filename);
                    System.out.println("Exiting...");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}