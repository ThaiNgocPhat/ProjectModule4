package ra.md4.dao.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ra.md4.models.CartItem;
import ra.md4.models.Order;
import ra.md4.models.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;

@Repository
public class OrderDaoImpl implements IOrderDao{
    @Autowired
    private EntityManager entityManager;
    @Override
    public void save(Order order) {
        entityManager.persist(order);
    }

    @Override
    public List<Order> findByUser(User user) {
        return entityManager.createQuery("SELECT o FROM Order o WHERE o.user = :user", Order.class)
                .setParameter("user", user)
                .getResultList();
    }

    @Override
    public BigDecimal calculateTotalPrice(List<CartItem> cartItems) {
        BigDecimal totalPrice = BigDecimal.ZERO; // Khởi tạo tổng tiền là 0
        for (CartItem item : cartItems) {
            // Chuyển đổi đơn giá sản phẩm (Double) sang BigDecimal
            BigDecimal unitPrice = BigDecimal.valueOf(item.getProduct().getUnitPrice());

            // Nhân đơn giá với số lượng sản phẩm
            BigDecimal itemTotal = unitPrice.multiply(new BigDecimal(item.getQuantity()));

            // Cộng dồn tổng tiền
            totalPrice = totalPrice.add(itemTotal);
        }
        return totalPrice; // Trả về tổng giá tiền
    }
    @Override
    public List<Order> getOrdersByUserId(Integer userId) {
        TypedQuery<Order> query = entityManager.createQuery(
                "SELECT o FROM Order o WHERE o.id = :userId", Order.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }
}
