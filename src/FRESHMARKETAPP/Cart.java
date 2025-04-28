package FRESHMARKETAPP;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static List<CartItem>items = new ArrayList<>();
    public static void addItem(CartItem item){
        items.add(item);
    }
    public static List<CartItem> getItems() {
        return items;
    }
    public static void clear() {
        items.clear();
    }
}
