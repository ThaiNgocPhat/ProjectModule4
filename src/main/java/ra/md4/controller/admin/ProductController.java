package ra.md4.controller.admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ra.md4.dto.req.ProductDTO;
import ra.md4.dto.req.ProductEditDTO;
import ra.md4.models.Category;
import ra.md4.models.Product;
import ra.md4.service.admin.category.ICategoryService;
import ra.md4.service.admin.product.IProductService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class ProductController {
    @Autowired
    private IProductService iProductService;

    @Autowired
    private ICategoryService iCategoryService;

    //Phân trang sản phẩm
    @GetMapping
    public String getProducts(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size, Model model) {
        List<Product> products = iProductService.getProducts(page, size);
        long totalProducts = iProductService.getTotalProducts();
        int totalPages = (int) Math.ceil((double) totalProducts / size);
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        return "admin/product/product";
    }

    // lấy form chỉnh sửa sản phẩm
    @GetMapping("/edit")
    public String editProduct(@RequestParam("id") Integer id, Model model) {
        List<Category> categories = iCategoryService.getAll();
        Product product = iProductService.findById(id);
        model.addAttribute("categories", categories);
        model.addAttribute("product", product);
        return "admin/product/editProduct";
    }
//
//    //Chỉnh sửa sản phẩm
    @PostMapping("/update")
    public String updateProduct(@Valid @ModelAttribute ProductEditDTO productEditDTO) {
        Category category = iCategoryService.findById(productEditDTO.getCategoryId());
        Product product = new Product();
        product.setId(productEditDTO.getId());
        product.setName(productEditDTO.getName());
        product.setDescription(productEditDTO.getDescription());
        product.setUnitPrice(productEditDTO.getUnitPrice());
        product.setStockQuantity(productEditDTO.getStockQuantity());
        product.setImage(productEditDTO.getImage());
        product.setDiscount(productEditDTO.getDiscount());
        product.setCategory(category);

        iProductService.update(product);
        return "redirect:/admin/products";
    }

    // Xóa sản phẩm
    @GetMapping("/delete")
    public String deleteProduct(@RequestParam("id") Integer id) {
        iProductService.delete(id);
        return "redirect:/admin/products";
    }

    //lấy form thêm mới sản phẩm
    @GetMapping("/add")
    public String newProduct(Model model) {
        List<Category> categories = iCategoryService.getAll();
        model.addAttribute("categories", categories);
        model.addAttribute("product", new Product());
        return "admin/product/newProduct";
    }


    // Thêm mới sản phẩm
    @PostMapping("/add")
    public String createProduct(@ModelAttribute Product product, @RequestParam Integer categoryId) {
        Category category = iCategoryService.findById(categoryId);
        product.setCategory(category); // Gán danh mục cho sản phẩm
        iProductService.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/search")
    public String searchProduct(@RequestParam("name") String name, Model model) {
        List<Product> products = iProductService.searchByName(name);
        model.addAttribute("products", products);
        return "admin/product/product";
    }

    @GetMapping("/changeStatus/{id}")
    public String changeUserStatus(@PathVariable("id") Integer id) {
        iProductService.changeStatus(id);
        return "redirect:/admin/products";
    }
}
