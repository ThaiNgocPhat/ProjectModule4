package ra.md4.dto.req;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProductEditDTO {
    @NotNull(message = "ID sản phẩm không được để trống.")
    private Integer id; // ID của sản phẩm

    @NotBlank(message = "Tên sản phẩm không được để trống.")
    private String name; // Tên sản phẩm

    @NotBlank(message = "Mô tả sản phẩm không được để trống.")
    private String description; // Mô tả sản phẩm

    @NotNull(message = "Giá sản phẩm không được để trống.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá sản phẩm phải lớn hơn 0.")
    private Double unitPrice; // Giá sản phẩm

    @NotNull(message = "Số lượng trong kho không được để trống.")
    @Min(value = 0, message = "Số lượng trong kho phải không âm.")
    private Integer stockQuantity; // Số lượng trong kho

    private String image; // Hình ảnh sản phẩm (URL)

    @DecimalMin(value = "0.0", inclusive = false, message = "Giảm giá phải lớn hơn hoặc bằng 0.")
    private Double discount; // Giảm giá

    @NotNull(message = "ID danh mục không được để trống.")
    private Integer categoryId; // ID danh mục
}
