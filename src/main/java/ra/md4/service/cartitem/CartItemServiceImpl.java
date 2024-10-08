package ra.md4.service.cartitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.md4.dao.cart.ICartDaoItem;
import ra.md4.dao.product.IProductDao;
import ra.md4.dao.user.IUserDao;
import ra.md4.dto.req.FormCart;
import ra.md4.dto.req.FormChangeQuantity;
import ra.md4.models.CartItem;
import ra.md4.models.Product;
import ra.md4.models.User;

import javax.transaction.Transactional;
import java.util.List;
@Service
public class CartItemServiceImpl implements ICartItemService {

    @Autowired
    private ICartDaoItem iCartDao;

    @Autowired
    private IUserDao iUserDao;

    @Autowired
    private IProductDao iProductDao;

    @Override
    public List<CartItem> getCartByUser(Integer userId) {
        return iCartDao.findByUserId(userId);
    }

    private CartItem findCartItemByProductId(Integer productId, Integer userId){
        return iCartDao.findByUserId(userId).stream().filter(cartItem -> cartItem.getProduct().getId() == productId).findFirst().orElse(null);
    }
    @Transactional
    @Override
    public void addToCart(FormCart formCart, Integer userId) {
        CartItem cartItem = findCartItemByProductId(formCart.getProductId(), userId);
        if (cartItem != null){
            cartItem.setQuantity(cartItem.getQuantity()+formCart.getQuantity());
            iCartDao.update(cartItem);
        }else{
            User user = iUserDao.findById(userId);
            Product product = iProductDao.findById(formCart.getProductId());
            if (user == null || product == null){
                throw new RuntimeException("Lỗi");
            }
            cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(formCart.getQuantity());
            iCartDao.save(cartItem);
        }
    }

    @Transactional
    @Override
    public void changeQuantity(FormChangeQuantity formChangeQuantity) {
        CartItem cartItem = iCartDao.findById(formChangeQuantity.getCartItemId());
        if (cartItem!= null){
            cartItem.setQuantity(formChangeQuantity.getNewQuantity());
            iCartDao.update(cartItem);
        }else{
            throw new RuntimeException("Sản phẩm không tồn tại trong gi�� hàng");
        }
    }

    @Transactional
    @Override
    public void deleteCartItem(Integer cartItemId) {
        iCartDao.delete(cartItemId);
    }

    @Override
    public double calculateTotalPrice(List<CartItem> cartItems) {
        return iCartDao.calculateTotalPrice(cartItems);
    }

    @Transactional
    @Override
    public void clearCart(Integer userId) {
        List<CartItem> cartItems = iCartDao.findByUserId(userId);
        cartItems.forEach(cartItem -> iCartDao.delete(cartItem.getId()));
        iCartDao.clearCart(userId);
    }

    @Override
    public List<CartItem> getCartItems(Integer userId) {
        return iCartDao.getCartItems(userId);
    }
}
