package ra.md4.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "soppingCartId", referencedColumnName = "id")
    private ShoppingCart shoppingCart;

    @ManyToOne
    @JoinColumn(name = "productId", referencedColumnName = "id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "orderId", referencedColumnName = "id")
    private Order order;


    private Integer quantity;

    public BigDecimal getTotalPrice() {
        BigDecimal unitPrice = BigDecimal.valueOf(product.getUnitPrice());
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
