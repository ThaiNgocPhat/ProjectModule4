package ra.md4.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.md4.dto.req.FormLogin;
import ra.md4.dto.req.FormRegister;
import ra.md4.dto.res.UserInfo;
import ra.md4.exception.AuthenticationException;
import ra.md4.service.user.IUserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class AuthController {
    @Autowired
    private IUserService userService;


    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        // Lấy cookie từ yêu cầu
        Cookie[] cookies = request.getCookies();
        String userEmail = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userEmail")) {
                    userEmail = cookie.getValue();
                    break;
                }
            }
        }

        // Nếu tìm thấy cookie, tự động đăng nhập cho người dùng
        if (userEmail != null) {
            FormLogin formLogin = new FormLogin();
            formLogin.setUsername(userEmail); // Giả sử bạn có thuộc tính username trong FormLogin
            // Thực hiện đăng nhập tự động
            try {
                UserInfo userInfo = userService.login(formLogin);
                // Lưu thông tin người dùng vào model để hiển thị trên giao diện
                model.addAttribute("userLogin", userInfo);
                // Nếu đăng nhập thành công, chuyển hướng
                if (userInfo.isRole()) {
                    return "redirect:/admin/dashboard";
                } else {
                    return "redirect:/";
                }
            } catch (AuthenticationException e) {
                model.addAttribute("error", e.getMessage());
            }
        }

        return "auth/login";
    }


    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("formRegister", new FormRegister());
        return "auth/register";
    }

@PostMapping("/register")
public String handleRegister(@Valid @ModelAttribute FormRegister request,
                             BindingResult result, Model model) {
    if (result.hasErrors()) {
        // Nếu có lỗi, trả lại trang đăng ký với thông báo lỗi
        model.addAttribute("org.springframework.validation.BindingResult.formRegister", result);
        return "auth/register"; // Quay lại trang đăng ký nếu có lỗi
    }
    try {
        userService.register(request);
        return "redirect:/login";
    } catch (Exception e) {
        model.addAttribute("errorMessage", e.getMessage());
        return "auth/register";
    }
}

@PostMapping("/login")
public String handleLogin(@ModelAttribute FormLogin request, HttpSession session, Model model, HttpServletResponse response) {
    try {
        UserInfo userInfo = userService.login(request);

        // Kiểm tra trạng thái hoạt động của người dùng
        if (!userInfo.isStatus()) {
            model.addAttribute("error", "Tài khoản của bạn không hoạt động.");
            return "auth/login";
        }

        session.setAttribute("userLogin", userInfo);

        // Thêm cookie để ghi nhớ đăng nhập
        String encodedUsername = URLEncoder.encode(userInfo.getUsername(), StandardCharsets.UTF_8.toString());
        Cookie cookie = new Cookie("userEmail", encodedUsername);
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);

        if (userInfo.isRole()) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/";
        }
    } catch (AuthenticationException e) {
        model.addAttribute("error", e.getMessage());
        return "auth/login"; // Trả về trang đăng nhập nếu có lỗi xác thực
    } catch (Exception e) {
        model.addAttribute("error", "Đã xảy ra lỗi không xác định.");
        return "auth/login"; // Trả về trang đăng nhập nếu có lỗi không xác định
    }
}


    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        // Xóa cookie khi đăng xuất
        Cookie cookie = new Cookie("userEmail", null);
        cookie.setMaxAge(0); // Đặt thời gian sống bằng 0 để xóa
        cookie.setPath("/"); // Đảm bảo cookie có thể xóa trên toàn bộ ứng dụng
        response.addCookie(cookie);

        session.invalidate();
        return "redirect:/";
    }

}
