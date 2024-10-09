package ra.md4.dao.order;

import ra.md4.models.CartItem;
import ra.md4.models.Order;
import ra.md4.models.User;

import java.math.BigDecimal;
import java.util.List;

public interface IOrderDao {
    void save(Order order);
    List<Order> findByUser(User user);
    BigDecimal calculateTotalPrice(List<CartItem> cartItems);
    List<Order> getOrdersByUserId(Integer userId);

}
