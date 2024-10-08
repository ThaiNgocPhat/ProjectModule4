package ra.md4.service.cartitem;
import ra.md4.dto.req.FormCart;
import ra.md4.dto.req.FormChangeQuantity;
import ra.md4.models.CartItem;
import ra.md4.service.IService;

import java.util.List;

public interface ICartItemService {
    List<CartItem> getCartByUser(Integer userId);
    void addToCart(FormCart formCart, Integer userId);
    void changeQuantity(FormChangeQuantity formChangeQuantity);
    void deleteCartItem(Integer cartItemId);
    double calculateTotalPrice(List<CartItem> cartItems);
}
