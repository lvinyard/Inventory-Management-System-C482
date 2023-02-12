package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class represents the Inventory of Parts and Products in our I.M.S.
 *
 */
public class Inventory {

    /**
     * Holds all Parts in Inventory
     */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    /**
     * Holds all Products in Inventory
     */
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Adds new Part to inventory
     * @param newPart
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Adds new Product to inventory
     * @param newProduct
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Looks up Part via ID
     * @param partId
     * @return Part
     */
    public static Part lookupPart(int partId) {
        ObservableList<Part> allParts = Inventory.getAllParts();

        for (Part part : allParts) {
            if (part.getId() == partId) {
                return part;
            }
        }
        return null;
    }

    /**
     * Looks up Product via ID
     * @param productId
     * @return Product
     */
    public static Product lookupProduct(int productId) {
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        for (Product product : allProducts) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    /**
     * Looks up parts via name
     * @param Name
     * @return List of Parts
     */
    public static ObservableList<Part> lookupPart(String Name) {
        ObservableList<Part> results = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getAllParts();

        for (Part part : allParts) {
            if (part.getName().toLowerCase().contains(Name.toLowerCase())) {
                results.add(part);
            }
        }
        return results;
    }

    /**
     * Looks up products via name
     * @param Name
     * @return List of Products
     */
    public static ObservableList<Product> lookupProduct(String Name) {
        ObservableList<Product> results = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(Name.toLowerCase())) {
                results.add(product);
            }
        }
        return results;
    }

    /**
     * Update existing Part from inventory
     * @param index
     * @param selectedPart
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     *  Update existing Product from inventory
     * @param index
     * @param selectedProduct
     */
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }


    /**
     *  Delete existing part from inventory
     * @param selectedPart
     * @return boolean
     */
    public static boolean deletePart(Part selectedPart) {
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        for (Product product : allProducts) {
            if (product.getPartAssociations().contains(selectedPart)) {
                return false;
            }
        }
        allParts.remove(Inventory.getAllParts().indexOf(selectedPart));
        return true;
    }

    /**
     *  Delete existing product from inventory
     * @param selectedProduct
     * @return boolean
     */
    public static boolean deleteProduct(Product selectedProduct) {
        if (selectedProduct.getPartAssociations().size() == 0) {
            allProducts.remove(Inventory.getAllProducts().indexOf(selectedProduct));
            return true;
        }
        return false;
    }

    /**
     * Gets all parts in inventory
     * @return List of Parts
     */
    public static ObservableList<Part> getAllParts() {

        return allParts;
    }

    /**
     *
     * @return List of Products
     */
    public static ObservableList<Product> getAllProducts() {

        return allProducts;
    }

    /**
     * Dynamically creates a unique Part ID for a new Part
     * @return int
     */
    public static int getPartID() {
        int newPartId = 0;
        ObservableList<Part> allParts = Inventory.getAllParts();

        for (Part part : allParts) {
            if (part.getId() > newPartId) {
                newPartId = part.getId();
            }
        }
        return ++newPartId;
    }

    /**
     * Dynamically created a unique Product ID for a new Product
     * @return int
     */
    public static int getProductID() {
        int newProductId = 0;
        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for (Product product : allProducts) {
            if (product.getId() > newProductId) {
                newProductId = product.getId();
            }
        }
        return ++newProductId;
    }
}