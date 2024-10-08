package ra.md4.service.order;

import ra.md4.models.Order;
import ra.md4.models.User;

import java.util.List;

public interface IOrderService {
    void save(Order order);
    List<Order> findByUser(User user);

}
