package lk.ijse.posm.dto;

public class ProductReportDTO {
    private String productName;

    private int qty;

    private double price;

    public ProductReportDTO() {
    }

    public ProductReportDTO(String productName, int qty, double price) {
        this.productName = productName;
        this.qty = qty;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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
}
