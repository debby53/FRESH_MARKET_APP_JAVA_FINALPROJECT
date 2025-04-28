package FRESHMARKETAPP;

public class CartItem {
    int productId;
    String productName;
    int quantity;
    int farmerId;

    public CartItem(int productId, String productName, int quantity, int farmerId) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.farmerId = farmerId;
    }
}
