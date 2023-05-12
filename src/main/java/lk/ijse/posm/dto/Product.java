package lk.ijse.posm.dto;

public class Product {

    private String itemCode;
    private String type;
    private int qty;
    private double price;

    public Product() {
    }

    public Product(String itemCode, String type, int qty, double price) {
        this.itemCode = itemCode;
        this.type = type;
        this.qty = qty;
        this.price = price;
    }


    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "itemCode='" + itemCode + '\'' +
                ", type='" + type + '\'' +
                ", qty=" + qty +
                ", price=" + price +
                '}';
    }
}
