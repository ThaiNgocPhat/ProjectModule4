package ra.md4.dao.cart;

import ra.md4.dao.IGenericDao;
import ra.md4.models.CartItem;

import java.util.List;


public interface ICartDaoItem extends IGenericDao<CartItem, Integer> {
    List<CartItem> findByUserId(Integer userId);
    double calculateTotalPrice(List<CartItem> cartItems);
    void clearCart(Integer userId);
    List<CartItem> getCartItems(Integer userId);
}
