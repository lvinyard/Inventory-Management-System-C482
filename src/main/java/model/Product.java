package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class models what a product is.
 */
public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int max;
    private int min;
    private int machineid;
    private ObservableList<Part>  partAssociations = FXCollections.observableArrayList();

    public Product(int id, String name, double price, int stock, int min, int max, int machineid, ObservableList<Part> partassociations){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.max = max;
        this.min = min;
        this.machineid = machineid;
        this.partAssociations = partassociations;
    }

    /**
     * @return The list of parts that are associated with the Product
     */
    public ObservableList<Part> getPartAssociations() {
        return partAssociations;
    }

    /**
     * @return the Product Id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name of the Product
     */
    public String getName() {
        return name;
    }

    /**
     * @return the Price of the Product
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return the inventory of the Product
     */
    public int getStock() {
        return stock;
    }

    /**
     * @return the Max
     */
    public int getMax() {
        return max;
    }

    /**
     * @return the Min
     */
    public int getMin() {
        return min;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock  ) {
        this.stock = stock;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the Machine Id
     */
    public int getMachineid() {
        return machineid;
    }

    /**
     * @param machineid the Machine Id to set
     */
    public void setMachineid(int machineid) {
        this.machineid = machineid;
    }
}
