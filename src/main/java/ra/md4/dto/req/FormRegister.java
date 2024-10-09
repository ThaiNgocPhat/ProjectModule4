package ra.md4.dto.req;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.*;

@Data
public class FormRegister {
    private Integer id;

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Pattern(regexp = "^[^\\s]+$", message = "Tên đăng nhập không được chứa khoảng trắng")
    @Size(min = 3, max = 20, message = "Tên đăng nhập phải có từ 3 đến 20 ký tự")
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Pattern(regexp = "^[^\\s]+$", message = "Tên đăng nhập không được chứa khoảng trắng")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Họ và tên không được để trống")
    @Size(max = 50, message = "Họ và tên không được vượt quá 50 ký tự")
    private String fullName;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Pattern(regexp = "^[^\\s]+$", message = "Tên đăng nhập không được chứa khoảng trắng")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    @Pattern(regexp = "^[^\\s]+$", message = "Tên đăng nhập không được chứa khoảng trắng")
    @Size(min = 6, message = "Xác nhận mật khẩu phải có ít nhất 6 ký tự")
    private String confirmPassword;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(\\+84|0)\\d{9,10}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 100, message = "Địa chỉ không được vượt quá 100 ký tự")
    private String address;

    public boolean isPasswordMatch() {
        return password != null && password.equals(confirmPassword);
    }
}
