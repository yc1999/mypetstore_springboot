package org.csu.mypetstore_springboot.controller;

import org.csu.mypetstore_springboot.domain.Account;
import org.csu.mypetstore_springboot.domain.Cart;
import org.csu.mypetstore_springboot.domain.Item;
import org.csu.mypetstore_springboot.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class CartController {
    @Autowired
    private CatalogService catalogService;

    @Autowired
    HttpServletRequest request;

    Account account;

    private Cart cart;

    @GetMapping("/cart/addCart")
    public String addItemToCart(@RequestParam("workingItemId") String workingItemId, HttpSession session){
        if(workingItemId != null){
            cart = (Cart)session.getAttribute("cart");

            if(cart == null){
                cart = new Cart();
            }

            if(cart.containsItemId(workingItemId)){
                cart.incrementQuantityByItemId(workingItemId);
            }else{
                boolean isInStock = catalogService.isItemInStock(workingItemId);
                Item item = catalogService.getItem(workingItemId);
                cart.addItem(item,isInStock);
            }
            int cartItemNumber = cart.getCartItemList().size();
            session.setAttribute("cartItemNumber", cartItemNumber);
            session.setAttribute("cart",cart);
        }
        return "cart/cart";
    }

    @GetMapping("/cart/viewCart")
    public String viewCart(HttpSession session){
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null){
            cart = new Cart();
            session.setAttribute("cart",cart);
        }
        return "cart/cart";
    }
}
