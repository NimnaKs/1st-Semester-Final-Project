package lk.ijse.posm.dto;

public class ProductDTO {

    private String itemCode;

    private String itemName;

    private double price;

    private int QtyAvailable;

    private String image;

    public ProductDTO(String itemCode, String itemName, int qtyAvailable,double price, String image) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.price = price;
        QtyAvailable = qtyAvailable;
        this.image = image;
    }

    public ProductDTO() {
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQtyAvailable() {
        return QtyAvailable;
    }

    public void setQtyAvailable(int qtyAvailable) {
        QtyAvailable = qtyAvailable;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
