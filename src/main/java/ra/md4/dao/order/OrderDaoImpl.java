package ra.md4.dao.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ra.md4.models.Order;
import ra.md4.models.User;

import javax.persistence.EntityManager;
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
}
