package lk.ijse.posm.dto;

public class ItemRemove {

    private String item_code;

    private int qty;

    public ItemRemove(String item_code, int qty) {
        this.item_code = item_code;
        this.qty = qty;
    }

    public ItemRemove() {
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
