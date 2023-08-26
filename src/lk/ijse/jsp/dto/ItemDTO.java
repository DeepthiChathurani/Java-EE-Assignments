package lk.ijse.jsp.dto;

public class ItemDTO {
    private String code;
    private String description;
    private int qty;
    private double unitPrice;


    public ItemDTO() {
    }

    public ItemDTO(String code, String description, int qty, double unitPrice) {
        this.code = code;
        this.description = description;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qtyOnHand) {
        this.qty = qtyOnHand;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
