package com.example.bai_tap_cuoi_ky_web.Controller;

import com.example.bai_tap_cuoi_ky_web.Entity.Category;
import com.example.bai_tap_cuoi_ky_web.Entity.Order;
import com.example.bai_tap_cuoi_ky_web.Entity.Product;
import com.example.bai_tap_cuoi_ky_web.Service.CategoryService;
import com.example.bai_tap_cuoi_ky_web.Service.CommentService;
import com.example.bai_tap_cuoi_ky_web.Service.OrderService;
import com.example.bai_tap_cuoi_ky_web.Service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class AdminController {
    public static String uploadDir=System.getProperty("user.dir")+"/src/main/resources/static/css/images";
    @Autowired
    ProductService serviceP;
    @Autowired
    OrderService orderService;
    @Autowired
    CommentService commentService;
    @Autowired
    CategoryService serviceC;
    @GetMapping("/admin")
    public String getAdmin(Model model, HttpSession session){
        model.addAttribute("products",serviceP.getProducts());
        model.addAttribute("categories",serviceC.getCategorys());
        return "Admin";
    }
    @GetMapping("/admin/delete/{id}")
    public String postDelete(Model model, @PathVariable("id") int id){
        orderService.deleteOrderByProductID(orderService.findByProductID(id));
        commentService.deleteCommentByProductID(commentService.cmFindByProductID(id));
        serviceP.deleteProduct(id);
        model.addAttribute("products",serviceP.getProducts());
        model.addAttribute("categories",serviceC.getCategorys());
        return "redirect:/admin";
    }
    @GetMapping("/admin/add/{id}")
    public String getAddProduct(Model model,@PathVariable("id") int id){
        if(id==-1){
            model.addAttribute("product",new Product());
        }
        else {
            model.addAttribute("product",serviceP.getProductById(id));
        }
        model.addAttribute("res",id);
        model.addAttribute("categories", serviceC.getCategorys());
        return "Addproduct";
    }

    @PostMapping("/admin/add/{id}")
    public String postAddProduct(Model model,
                                 @ModelAttribute("product") Product product,
                                 @RequestParam("productImage") MultipartFile file,
                                 @RequestParam("imgName") String imgName){
        String imageUUID;
        try{
            if(!file.isEmpty()){
                imageUUID=file.getOriginalFilename();
                Path fileNameAndPath = Paths.get(uploadDir,imageUUID);
                Files.write(fileNameAndPath,file.getBytes());
            }
            else {
                imageUUID=imgName;
            }
            product.setImage("/css/images/"+imageUUID);
        }catch (Exception e){
            e.printStackTrace();
        }
        serviceP.addProduct(product);
        model.addAttribute("products",serviceP.getProducts());
        return "redirect:/admin";
    }

    @PutMapping("admin/add/{id}")
    public String putEditProduct(Model model,
                                 @ModelAttribute("product") Product product,
                                 @RequestParam("productImage")MultipartFile file,
                                 @RequestParam("imgName") String imgName){
        String imageUUID;
        try{
            if(!file.isEmpty()){
                imageUUID=file.getOriginalFilename();
                Path fileNameAndPath = Paths.get(uploadDir,imageUUID);
                Files.write(fileNameAndPath,file.getBytes());
            }
            else {
                imageUUID=imgName;
            }
            if(imageUUID.contains("css")){
                product.setImage(imageUUID);
            }
            else {
                product.setImage("/css/images/"+imageUUID);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        serviceP.addProduct(product);
        model.addAttribute("products",serviceP.getProducts());
        return "redirect:/admin";
    }
}
