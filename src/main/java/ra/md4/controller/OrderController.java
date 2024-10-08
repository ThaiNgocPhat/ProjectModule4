package ra.md4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ra.md4.dto.req.FormAddressOrder;
import ra.md4.dto.res.UserInfo;
import ra.md4.models.Order;
import ra.md4.models.User;
import ra.md4.service.order.IOrderService;
import ra.md4.service.user.IUserService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private IUserService iUserService;

    // Phương thức này để hiển thị trang nhập địa chỉ giao hàng
    @GetMapping("/checkout")
    public String showCheckoutForm(Model model, HttpSession session) {
        model.addAttribute("formAddressOrder", new FormAddressOrder());
        return "layout/cart/userinformation"; // Tên của trang checkout
    }

    @PostMapping("/checkout")
    public String createOrder(@ModelAttribute FormAddressOrder formAddressOrder, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute("userLogin");

        // Giả sử UserInfo chứa id, tìm User từ UserService
        User user = iUserService.findById(userInfo.getId()); // Chuyển đổi từ UserInfo sang User

        // Tạo một đơn hàng mới
        Order order = new Order();
        order.setReceiveName(formAddressOrder.getReceiveName());
        order.setReceiveAddress(formAddressOrder.getReceiveAddress());
        order.setReceivePhone(formAddressOrder.getReceivePhone());
        order.setUser(user); // Đặt đối tượng User vào đơn hàng

        // Lưu đơn hàng vào cơ sở dữ liệu thông qua service
        iOrderService.save(order);

        return "redirect:/cart/success"; // Chuyển hướng đến trang thành công hoặc giỏ hàng
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
        return "/layout/cart/orderHistory"; // Đảm bảo trả về tên đúng của trang lịch sử giao hàng
    }

}
