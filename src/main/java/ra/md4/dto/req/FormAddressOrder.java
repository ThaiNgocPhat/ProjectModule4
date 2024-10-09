package ra.md4.dto.req;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class FormAddressOrder {

    @NotBlank(message = "Tên người nhận không được để trống.")
    @Size(max = 100, message = "Tên người nhận không được vượt quá 100 ký tự.")
    private String receiveName;

    @NotBlank(message = "Địa chỉ giao hàng không được để trống.")
    @Size(max = 255, message = "Địa chỉ giao hàng không được vượt quá 255 ký tự.")
    private String receiveAddress;

    @NotBlank(message = "Số điện thoại không được để trống.")
    @Pattern(regexp = "^\\d{10,15}$", message = "Số điện thoại phải từ 10 đến 15 chữ số.")
    private String receivePhone;

    @Size(max = 255, message = "Ghi chú không được vượt quá 255 ký tự.")
    private String node;
}
