package ra.md4.dao.cart;
import org.springframework.stereotype.Repository;
import ra.md4.models.CartItem;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
@Repository
public class CartItemDaoImpl implements ICartDaoItem {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<CartItem> getAll() {
        return new ArrayList<>();
    }

    @Override
    public CartItem findById(Integer id) {
        return entityManager.find(CartItem.class, id);
    }

    @Override
    public void save(CartItem cart) {
        entityManager.persist(cart);
    }

    @Override
    public void update(CartItem cart) {
        entityManager.merge(cart);
    }

    @Override
    public void delete(Integer id) {
        CartItem cart = findById(id);
        if (cart != null) {
            entityManager.remove(cart);
        }
    }
    @Override
    public List<CartItem> findByUserId(Integer userId) {
        String jpql = "SELECT c FROM CartItem c WHERE c.user.id = :userId";
        return entityManager.createQuery(jpql, CartItem.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public double calculateTotalPrice(List<CartItem> cartItems) {
        return cartItems.stream().mapToDouble(item -> item.getProduct().getUnitPrice() * item.getQuantity()).sum();
    }

    @Override
    public void clearCart(Integer userId) {
        entityManager.createQuery("DELETE FROM CartItem c WHERE c.user.id = :userId")
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    public List<CartItem> getCartItems(Integer userId) {
        return entityManager.createQuery(
                        "SELECT c FROM CartItem c WHERE c.user.id = :userId", CartItem.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
