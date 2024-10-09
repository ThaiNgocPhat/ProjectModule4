package ra.md4.dto.req;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ProductDTO {
    private Integer id;

    @NotBlank(message = "Mã sản phẩm không được để trống")
    private String sku;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;

    @NotBlank(message = "Mô tả sản phẩm không được để trống")
    private String description;

    @NotNull(message = "Giá sản phẩm không được để trống")
    @Min(value = 0, message = "Giá sản phẩm phải lớn hơn hoặc bằng 0")
    private Double unitPrice;

    @Min(value = 0, message = "Giảm giá phải lớn hơn hoặc bằng 0")
    private Double discount;

    @NotNull(message = "Giá sau giảm giá không được để trống")
    @Min(value = 0, message = "Giá sau giảm giá phải lớn hơn hoặc bằng 0")
    private Double discounted;

    @NotNull(message = "Số lượng sản phẩm không được để trống")
    @Min(value = 0, message = "Số lượng sản phẩm phải lớn hơn hoặc bằng 0")
    private Integer stockQuantity;

    private String image;

    @NotNull(message = "Danh mục không được để trống")
    private Integer categoryId;

    private boolean status = true;
}
