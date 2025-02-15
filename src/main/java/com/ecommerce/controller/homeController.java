package com.ecommerce.controller;

import com.ecommerce.model.*;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.UserService;
import com.ecommerce.service.impl.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@SessionAttributes({"userId", "userEmail", "userName", "userRole"})
@RequestMapping("/home")
public class homeController {

    private final HttpServletRequest httpServletRequest;
    UserDetails userDetails;
    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;
    public homeController(ProductService productService, HttpServletRequest httpServletRequest, OrderService orderService, UserService userService) {
        this.productService = productService;
        this.httpServletRequest = httpServletRequest;
        this.orderService = orderService;
        this.userService = userService;
    }

//    @GetMapping()
//    public String showHomeProducts(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
//            model.addAttribute("products", productService.getAllProducts());
//            return "home";
//        } else {
//            return "redirect:/login";
//        }
//    }
    @GetMapping()
    public String showHomeProducts(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            model.addAttribute("products", productService.getAllProducts());
            return "home";
    }

    @GetMapping("/cart")
    public String showCart(Model model) {
        model.addAttribute("product", new Product());
        return "cart";
    }

    @GetMapping("/UserOrders")
    public String viewOrders(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        HttpSession session = httpServletRequest.getSession();

        String userEmail = (String) session.getAttribute("userEmail");
        List<Orders> orders = orderService.getOrdersByEmail(userEmail);
        model.addAttribute("orders", orders);
        return "user-orders";
    }


    @GetMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable Long orderId, Model model) {
        Orders order = orderService.findById(orderId);
        if (order != null && order.getStatus().equalsIgnoreCase("Pending")) {
            order.setStatus("Canceled");
            orderService.updateStatus(order);
            model.addAttribute("message", "Order canceled successfully.");
        } else {
            model.addAttribute("error", "Unable to cancel the order.");
        }
        return "redirect:/UserOrders";
    }

}
