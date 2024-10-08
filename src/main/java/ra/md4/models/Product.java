package ra.md4.models;

import lombok.*;

import javax.persistence.*;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String sku;
    private String name;
    private String description;
    private Double unitPrice;
    private Double discount;
    private Double discounted;
    private Integer stockQuantity;
    private String image;
    private boolean status = true;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    private Date createdAt;
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    public String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(amount) + "₫";
    }

    public String getFormattedUnitPrice() {
        return formatCurrency(unitPrice);
    }

    public String getFormattedDiscountedPrice() {
        return formatCurrency(discounted);
    }
}
