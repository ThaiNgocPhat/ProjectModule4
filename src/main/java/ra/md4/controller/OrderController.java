package ra.md4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ra.md4.dao.cart.ICartDaoItem;
import ra.md4.dto.req.FormAddressOrder;
import ra.md4.dto.res.UserInfo;
import ra.md4.models.CartItem;
import ra.md4.models.Order;
import ra.md4.models.User;
import ra.md4.service.cartitem.ICartItemService;
import ra.md4.service.order.IOrderService;
import ra.md4.service.user.IUserService;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICartItemService iCartItemService;

    // Phương thức này để hiển thị trang nhập địa chỉ giao hàng
    @GetMapping("/checkout")
    public String showCheckoutForm(Model model, HttpSession session) {
        model.addAttribute("formAddressOrder", new FormAddressOrder());
        return "layout/cart/userinformation"; // Tên của trang checkout
    }

    @PostMapping("/checkout")
    public String createOrder(@ModelAttribute FormAddressOrder formAddressOrder, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute("userLogin");
        User user = iUserService.findById(userInfo.getId());

        // Tạo một đơn hàng mới
        Order order = new Order();
        order.setReceiveName(formAddressOrder.getReceiveName());
        order.setReceiveAddress(formAddressOrder.getReceiveAddress());
        order.setReceivePhone(formAddressOrder.getReceivePhone());
        order.setSerialNumber(UUID.randomUUID().toString());
        order.setNode(formAddressOrder.getNode());
        order.setReceivedAt(new Date());
        order.setUser(user);

        // Tính toán tổng tiền từ giỏ hàng
        List<CartItem> cartItems = iCartItemService.getCartItems(user.getId());
        BigDecimal totalPrice = iOrderService.calculateTotalPrice(cartItems);
        order.setTotalPrice(totalPrice);

        // Lưu đơn hàng vào cơ sở dữ liệu thông qua service
        iOrderService.save(order);

        // Xóa giỏ hàng sau khi đặt hàng thành công
        iCartItemService.clearCart(user.getId());

        return "redirect:/cart/success";
    }


    @GetMapping("/history")
    public String showOrderHistory(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute("userLogin");

        // Lấy thông tin người dùng từ ID
        User user = iUserService.findById(userInfo.getId());

        // Lấy danh sách đơn hàng của người dùng
        List<Order> orderList = iOrderService.findByUser(user);

        // Thêm danh sách đơn hàng vào mô hình
        model.addAttribute("orders", orderList);
        return "/layout/cart/orderHistory"; // Đảm bảo tên trang đúng
    }


}
