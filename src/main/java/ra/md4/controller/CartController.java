package ra.md4.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ra.md4.dao.user.IUserDao;
import ra.md4.dto.req.FormCart;
import ra.md4.dto.req.FormChangeQuantity;
import ra.md4.dto.res.UserInfo;
import ra.md4.models.CartItem;
import ra.md4.service.cartitem.ICartItemService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private ICartItemService iCartItemService;


@GetMapping
public String viewCart(HttpSession session, Model model) {
    // Lấy thông tin người dùng từ session
    UserInfo userInfo = (UserInfo) session.getAttribute("userLogin");

    if (userInfo == null) {
        // Nếu người dùng chưa đăng nhập, điều hướng về trang đăng nhập hoặc xử lý khác
        return "redirect:/login";
    }

    // Lấy giỏ hàng từ service
    List<CartItem> cartItems = iCartItemService.getCartByUser(userInfo.getId());
    double totalCartPrice = iCartItemService.calculateTotalPrice(cartItems);

    // Thêm vào Model để truyền sang view
    model.addAttribute("cartItems", cartItems);
    model.addAttribute("totalCartPrice", totalCartPrice);

    return "layout/cart/list";
}


    @PostMapping("/add")
    public String addToCart(@RequestParam int productId,
                            @RequestParam(defaultValue = "1") int quantity, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute("userLogin");
        FormCart formCart = new FormCart(productId, quantity);
        iCartItemService.addToCart(formCart, userInfo.getId());
        return "redirect:/cart";
    }

    @PostMapping("/updateQuantity")
    public String updateQuantity(@RequestParam Integer cartItemId,
                               @RequestParam int quantity, HttpSession session) {
    UserInfo userInfo = (UserInfo) session.getAttribute("userLogin");
    FormChangeQuantity formChangeQuantity = new FormChangeQuantity(cartItemId, quantity);
    iCartItemService.changeQuantity(formChangeQuantity);
    return "redirect:/cart";
    }

    @GetMapping("/delete/{id}")
    public String deleteCartItem(@PathVariable Integer id, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute("userLogin");
        iCartItemService.deleteCartItem(id);
        return "redirect:/cart";
    }

    @GetMapping("/success")
    public String showSuccessPage(Model model) {
        model.addAttribute("message", "Đơn hàng của bạn đã được tạo thành công!");
        return "layout/cart/success";
    }
}

