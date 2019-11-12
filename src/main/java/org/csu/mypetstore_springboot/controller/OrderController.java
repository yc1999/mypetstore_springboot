package org.csu.mypetstore_springboot.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.csu.mypetstore_springboot.domain.*;
import org.csu.mypetstore_springboot.service.CatalogService;
import org.csu.mypetstore_springboot.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    HttpServletRequest request;
    @Autowired
    CatalogService catalogService;

    private static final List<String> CARD_TYPE_LIST;

    static {
        List<String> cardList = new ArrayList<String>();
        cardList.add("Visa");
        cardList.add("MasterCard");
        cardList.add("American Express");
        CARD_TYPE_LIST = Collections.unmodifiableList(cardList);
    }
    @GetMapping("/order/oderList")
    public String viewOrderList(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");

        List<Order> orderList = orderService.getOrdersByUsername(account.getUsername());
        model.addAttribute("orderList", orderList);
        return "order/listOrders";
    }

    @GetMapping("/order/order")
    public String viewOrder(@RequestParam("orderId") int orderId,Model model,HttpSession session){
        Account account = (Account) session.getAttribute("account");

        if(account != null){
            Order order = (Order)session.getAttribute("order");
            if(order != null){
                //成功存储订单后，清空购物车,在session里维护order这个变量为null
                order = null;
                session.setAttribute("order",order);
            }
        }

        Order order = orderService.getOrder(orderId);
        model.addAttribute("order",order);
        model.addAttribute("username",account.getUsername());
        return "order/viewOrder";
    }

    @GetMapping("/order/newOrderForm")
    public String newOrderForm(HttpSession session,Model model){
        Cart cart;
        Order order;
        Account account = (Account) session.getAttribute("account");

        if(account == null){
            String message = "You must login first!";
            model.addAttribute("message",message);
            return "common/error";
        }else {
            cart = (Cart) session.getAttribute("cart");
        }

        if (cart != null) {
            order = new Order();
            order.initOrder(account, cart);
            session.setAttribute("cardList",CARD_TYPE_LIST);
            System.out.println(order.getShipAddress1());
            orderService.insertOrder(order);
            order = orderService.getOrder(order.getOrderId());
            session.setAttribute("order",order);
            return "order/newOrderForm";
        } else {
            String message = "An order could not be created because a cart could not be found.";
            model.addAttribute("message",message);
            return "common/error";
        }
    }

    @ResponseBody
    @PostMapping("/order/newOrder")
    public String newOrder(HttpServletRequest request,HttpSession session,Model model){
        Cart cart;
        Order order = null;
        String shippingAddressRequired = request.getParameter("shippingAddressRequired");
        String cardType=request.getParameter("cardType");
        String creditCard=request.getParameter("creditCard");
        String expiryDate=request.getParameter("expiryDate");
        String billToFirstName=request.getParameter("billToFirstName");
        System.out.println(billToFirstName);
        String billToLastName=request.getParameter("billToLastName");
        System.out.println(billToLastName);
        String billAddress1=request.getParameter("billAddress1");
        String billAddress2=request.getParameter("billAddress2");
        String billCity=request.getParameter("billCity");
        String billState=request.getParameter("billState");
        String billZip=request.getParameter("billZip");
        String billCountry=request.getParameter("billCountry");

        order = (Order) session.getAttribute("order");

        System.out.println("捕获order");
        System.out.println(order.toString());

        StringBuffer data = new StringBuffer("{\"order\": [ {");
        data.append("\"cardType\":\"");
        data.append(cardType);
        data.append("\",\"creditCard\":\"");
        data.append(creditCard);
        data.append("\",\"expiryDate\":\"");
        data.append(expiryDate);
        data.append("\",\"billToFirstName\":\"");
        data.append(billToFirstName);
        data.append("\",\"billToLastName\":\"");
        data.append(billToLastName);
        data.append("\",\"billAddress1\":\"");
        data.append(billAddress1);
        data.append("\",\"billAddress2\":\"");
        data.append(billAddress2);
        data.append("\",\"billCity\":\"");
        data.append(billCity);
        data.append("\",\"billState\":\"");
        data.append(billState);
        data.append("\",\"billZip\":\"");
        data.append(billZip);
        data.append("\",\"billCountry\":\"");
        data.append(billCountry);

        //如果勾选了shippingAddress
        if(shippingAddressRequired != null && shippingAddressRequired.equals("true")){
            order.setCardType(cardType);
            order.setCreditCard(creditCard);
            order.setExpiryDate(expiryDate);
            order.setBillToFirstName(billToFirstName);
            order.setBillToLastName(billToLastName);
            order.setBillAddress1(billAddress1);
            order.setBillAddress2(billAddress2);
            order.setBillCity(billCity);
            order.setBillState(billState);
            order.setBillZip(billZip);
            order.setBillCountry(billCountry);

            session.setAttribute("order",order);
            data.append("\",\"orderId\":\"");
            data.append(order.getOrderId());
            data.append("\"} ]}");
            Order testOrder = (Order) session.getAttribute("order");
            System.out.println("测试中"+testOrder.getBillToFirstName());
            return data.toString();
        }else if(order != null){

            order.setCardType(cardType);
            order.setCreditCard(creditCard);
            order.setExpiryDate(expiryDate);
            order.setBillToFirstName(billToFirstName);
            order.setBillToLastName(billToLastName);
            order.setBillAddress1(billAddress1);
            order.setBillAddress2(billAddress2);
            order.setBillCity(billCity);
            order.setBillState(billState);
            order.setBillZip(billZip);
            order.setBillCountry(billCountry);

            session.setAttribute("order",order);
            session.removeAttribute("cartItemNumber");

            //成功存储订单后，清空购物车,在session里维护order这个变量为null
            cart = (Cart)session.getAttribute("cart");
            cart = new Cart();
            session.setAttribute("cart",cart);
            data.append("\",\"orderId\":\"");
            data.append(order.getOrderId());
            data.append("\"} ]}");
            Order testOrder = (Order) session.getAttribute("order");
            System.out.println("测试中"+testOrder.getBillToFirstName());
            return data.toString();
        }else{
            return data.toString();
        }
    }

    @ResponseBody
    @PostMapping("/order/shippingForm")
    public String shippingForm(HttpSession session,HttpServletRequest request,Model model){
        Order order;
        String shipToFirstName=request.getParameter("shipToFirstName");
        System.out.println(shipToFirstName);
        String shipToLastName=request.getParameter("shipToLastName");
        String shipAddress1=request.getParameter("shipAddress1");
        String shipAddress2=request.getParameter("shipAddress2");
        String shipCity=request.getParameter("shipCity");
        String shipState=request.getParameter("shipState");
        String shipZip=request.getParameter("shipZip");
        String shipCountry=request.getParameter("shipCountry");
        Cart cart;

        order = (Order) session.getAttribute("order");

        StringBuffer data = new StringBuffer("{\"order\": [ {");
        data.append("\"shipToFirstName\":\"");
        data.append(shipToFirstName);
        data.append("\",\"shipToLastName\":\"");
        data.append(shipToLastName);
        data.append("\",\"shipAddress1\":\"");
        data.append(shipAddress1);
        data.append("\",\"shipAddress2\":\"");
        data.append(shipAddress2);
        data.append("\",\"shipCity\":\"");
        data.append(shipCity);
        data.append("\",\"shipState\":\"");
        data.append(shipState);
        data.append("\",\"shipZip\":\"");
        data.append(shipZip);
        data.append("\",\"shipCountry\":\"");
        data.append(shipCountry);
        data.append("\"} ]}");

        order.setShipToFirstName(shipToFirstName);
        order.setShipToLastName(shipToLastName);
        order.setShipAddress1(shipAddress1);
        order.setShipAddress2(shipAddress2);
        order.setShipCity(shipCity);
        order.setShipState(shipState);
        order.setShipZip(shipZip);
        order.setShipCountry(shipCountry);

        session.removeAttribute("cartItemNumber");
        session.setAttribute("order",order);
        cart = (Cart)session.getAttribute("cart");
        cart = new Cart();
        session.setAttribute("cart",cart);

        return data.toString();
    }

    @ResponseBody
    @GetMapping("/order/removeItemFromCart")
    public String removeItemFromCart(HttpServletRequest request,HttpSession session,Model model){
        String workingItemId = request.getParameter("workingItemId");
        Cart cart;
        cart = (Cart) session.getAttribute("cart");
        Item item = cart.removeItemById(workingItemId);
        session.setAttribute("cart",cart);

        System.out.println("执行到 removeItemFromCart 了");
        return cart.getSubTotal().toString();
    }

    @GetMapping("/order/updateCartQuantities")
    public void updateCartQuantities(HttpSession session, HttpServletResponse response,HttpServletRequest request, Model model) throws IOException {
        Cart cart;
        String itemId=request.getParameter("itemId");
        int quantity=0;

        int status=Integer.parseInt(request.getParameter("status"));

        cart=(Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        int flag = 0;   //为零表示尚未out

        response.setContentType("text/text;charset=utf-8");

        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        System.out.println("执行到 removeItemFromCart1 了");

        response.setContentType("updateCartQuantities");

        if (status == 0) {
            quantity=Integer.parseInt(request.getParameter("quantity"));
            if (quantity > catalogService.getInventoryQuantity(itemId)) {
                String message = "The inventory isn't enough";
                flag = 1;
                out.print(message);
                out.flush();
            } else {
                cart.setQuantityByItemId(itemId, quantity);
                session.setAttribute("cart", cart);
                String message = cart.getSubTotal() + "";
                flag = -1;
                out.print(message);
                out.flush();
            }
        } else {
            Iterator<CartItem> cartItems = cart.getAllCartItems();
            while (cartItems.hasNext()) {
                CartItem cartItem = (CartItem) cartItems.next();
                String id = cartItem.getItem().getItemId();
                if (id.equals(itemId)) {
                    cart.setQuantityByItemId(itemId, cartItem.getItem().getQuantity() + 1);
                    out.print("y*" + id);
                    out.flush();
                    flag = 2;
                } else {
                    cart.setQuantityByItemId(id, cartItem.getItem().getQuantity());
                }
            }

            if (catalogService.getInventoryQuantity(itemId) < 0) {
                out.print("e");
                out.flush();
                flag = 3;
            }

            if (flag == 0) {
                Item item = catalogService.getItem(itemId);
                boolean isInStock = catalogService.isItemInStock(itemId);
                cart.addItem(item, isInStock);
                int cartItemNumber = cart.getCartItemList().size();
                session.setAttribute("cartItemNumber", cartItemNumber);
                session.setAttribute("cart", cart);
                String description = "";
                if (item.getAttribute1() != null) {
                    description += item.getAttribute1();
                } else if (item.getAttribute2() != null) {
                    description += item.getAttribute2();
                } else if (item.getAttribute3() != null) {
                    description += item.getAttribute3();
                } else if (item.getAttribute4() != null) {
                    description += item.getAttribute4();
                } else if (item.getAttribute5() != null) {
                    description += item.getAttribute5();
                }

                description += item.getProduct().getName();
                out.print("n*" + itemId + "*" + item.getProduct().getProductId() + "*" + description + "*" + catalogService.isItemInStock(itemId) + "*" + item.getListPrice());
                out.flush();
                flag = 4;
            }
        }
        session.setAttribute("cart",cart);

        if(flag == 0){
            out.println("1");
            out.flush();
        }
        System.out.println("执行到 removeItemFromCart2 了");
    }

    @GetMapping("order/addItemToCart")
    public String addItemToCart(HttpServletResponse response, HttpSession session, Model model){
        String workingItemId=request.getParameter("workingItemId");
        Cart cart;
        cart = (Cart)session.getAttribute("cart");

        if(cart == null){
            cart = new Cart();
        }
        if (cart.containsItemId(workingItemId)) {
            cart.incrementQuantityByItemId(workingItemId);
        } else {
            boolean isInStock = catalogService.isItemInStock(workingItemId);
            Item item = catalogService.getItem(workingItemId);
            cart.addItem(item, isInStock);
        }
        int cartItemNumber = cart.getCartItemList().size();
        session.setAttribute("cartItemNumber", cartItemNumber);
        session.setAttribute("cart",cart);

        return "/cart/cart";
    }

}
