package org.csu.mypetstore_springboot.controller;

import org.csu.mypetstore_springboot.domain.Account;
import org.csu.mypetstore_springboot.domain.Cart;
import org.csu.mypetstore_springboot.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class AccountController {
    private String[] ver;
    private ByteArrayOutputStream outputStream;

    @Autowired
    private AccountService accountService;

    private static final List<String> LANGUAGE_LIST;
    private static final List<String> CATEGORY_LIST;
    static {
        List<String> langList = new ArrayList<String>();
        langList.add("english");
        langList.add("japanese");
        LANGUAGE_LIST = Collections.unmodifiableList(langList);

        List<String> catList = new ArrayList<String>();
        catList.add("FISH");
        catList.add("DOGS");
        catList.add("REPTILES");
        catList.add("CATS");
        catList.add("BIRDS");
        CATEGORY_LIST = Collections.unmodifiableList(catList);
    }

    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String favouriteCategoryId;
    private String languagePreference;
    private boolean listOption;
    private boolean bannerOption;

    private Account account;

    @GetMapping("/account/newAccountForm")
    public String newAccountForm (HttpServletRequest request, Model model){
        model.addAttribute("languages",LANGUAGE_LIST);
        model.addAttribute("categories",CATEGORY_LIST);
        return "account/newAccountForm";
    }

    @PostMapping("/account/newAccount")
    public String newAccount(Model model, HttpServletRequest request, HttpSession session){
        username = request.getParameter("username");
        password = request.getParameter("password");
        firstname = request.getParameter("firstname");
        lastname = request.getParameter("lastname");
        email = request.getParameter("email");
        phone = request.getParameter("phone");
        address1 = request.getParameter("address1");
        address2 = request.getParameter("address2");
        city = request.getParameter("city");
        state = request.getParameter("state");
        zip = request.getParameter("zip");
        country = request.getParameter("country");
        languagePreference = request.getParameter("languagePreference");
        favouriteCategoryId = request.getParameter("favouriteCategoryId");
        if(request.getParameter("listOption") != null){
            listOption = true;
        }else {
            listOption = false;
        }
        if(request.getParameter("bannerOption") != null){
            bannerOption = true;
        }else {
            bannerOption = false;
        }

        if(!username.isEmpty() && !password.isEmpty()){
            account = new Account();
            account.setUsername(username);
            account.setPassword(password);
            account.setFirstName(firstname);
            account.setLastName(lastname);
            account.setEmail(email);
            account.setPhone(phone);
            account.setAddress1(address1);
            account.setAddress2(address2);
            account.setCity(city);
            account.setState(state);
            account.setZip(zip);
            account.setCountry(country);
            account.setLanguagePreference(languagePreference);
            account.setFavouriteCategoryId(favouriteCategoryId);

            accountService.insertAccount(account);
            account = accountService.getAccount(account.getUsername());
            account.setListOption(listOption);
            account.setBannerOption(bannerOption);

            return "catalog/main";
        }else{
            model.addAttribute("languages",LANGUAGE_LIST);
            model.addAttribute("categories",CATEGORY_LIST);
            return "account/newAccount";
        }
    }

    @GetMapping("/checkCode")
    public void checkCode(HttpServletResponse httpServletResponse, HttpSession session) throws Exception {
        ver = new String[62];
        for (int i = 0; i < 10; i++) {
            ver[i] = new Integer(i).toString();
        }
        for (int i = 0; i < 26; i++) {
            ver[10 + i] = new Character((char) (97 + i)).toString();
        }
        for (int i = 0; i < 26; i++) {
            ver[36 + i] = new Character((char) (65 + i)).toString();
        }
        try {
            BufferedImage image = new BufferedImage(50, 20, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();

            g.setColor(new Color(200, 200, 200));
            g.fillRect(0, 0, 50, 20);

            g.setColor(new Color(150, 150, 150));
            for (int i = 0; i < 20; i++) {
                int x1 = (int) (Math.random() * 50);
                int y1 = (int) (Math.random() * 20);
                int x2 = (int) (Math.random() * 50);
                int y2 = (int) (Math.random() * 20);

                g.drawLine(x1, y1, x2, y2);
            }

            String checkCode = "", v;
            for (int i = 0; i < 4; i++) {
                v = (ver[(int) (Math.random() * 62)]);
                checkCode += v;
                g.setColor(new Color((int) (Math.random() * 150), (int) (Math.random() * 150), (int) (Math.random() * 150)));
                g.drawString(v, 8 * i + 10, 15);
            }

            session.setAttribute("checkCode", checkCode);

            g.dispose();

            outputStream = new ByteArrayOutputStream();

            ImageOutputStream imageOut = ImageIO.createImageOutputStream(outputStream);
            ImageIO.write(image, "JPEG", imageOut);
            imageOut.close();
            httpServletResponse.setHeader("Cache-Control", "no-store");
            httpServletResponse.setHeader("Pragma", "no-cache");
            httpServletResponse.setDateHeader("Expires", 0);
            httpServletResponse.setContentType("image/jpeg");
            ServletOutputStream responseOutputStream =
                    httpServletResponse.getOutputStream();
            responseOutputStream.write(outputStream.toByteArray());
            responseOutputStream.flush();
            responseOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //返回值试着加这个注解@RequestBody
    @ResponseBody
    @PostMapping("/account/signon")
    public String signon(HttpServletRequest request,HttpSession session,Model model){
//        if(session.get("flag") != null){
//            accountService = new AccountService();
//            List<String> usernameList = accountService.getUsernames();
//            session.put("usernameList",usernameList);
//            return "usersLog";
//        }
        username = request.getParameter("username");
        password = request.getParameter("password");
        account = accountService.getAccount(username,password);
        String message;
        String checkCode = request.getParameter("checkCode");
        if(account == null){
            account = new Account();
            message = "Username or Password error!";
//            inputStream = new ByteArrayInputStream(message.getBytes("UTF-8"));
            return message;
        }else if(checkCode.isEmpty()){
            message = "checkCode can't be null!";
//            inputStream = new ByteArrayInputStream(message.getBytes("UTF-8"));
            return message;
        }else if(!checkCode.equals(session.getAttribute("checkCode"))){
            message = "checkCode error!";
//            inputStream = new ByteArrayInputStream(message.getBytes("UTF-8"));
            return message;
        }else{
            account.setPassword(null);
            session.setAttribute("account",account);
            message = username;
//            inputStream = new ByteArrayInputStream(message.getBytes("UTF-8"));
            return message;
        }
    }

    @GetMapping("/account/editAccountForm")
    public String editAccountForm(Model model){
        model.addAttribute("languages",LANGUAGE_LIST);
        model.addAttribute("categories",CATEGORY_LIST);
        System.out.println("编辑");
        return "account/editAccountForm";
    }

    @PostMapping("/account/editAccount")
    public String editAccount(Model model,HttpServletRequest request,HttpSession session){
        System.out.println("编辑个人信息表单提交成功！");
//        ActionContext actionContext = ActionContext.getContext();
//        Map session = actionContext.getSession();
//        Map request = (Map)actionContext.get("request");
        username = request.getParameter("username");
        System.out.println(username);
        password = request.getParameter("password");
        System.out.println(password);
        firstname = request.getParameter("firstname");
        lastname = request.getParameter("lastname");
        email = request.getParameter("email");
        phone = request.getParameter("phone");
        address1 = request.getParameter("address1");
        address2 = request.getParameter("address2");
        city = request.getParameter("city");
        state = request.getParameter("state");
        zip = request.getParameter("zip");
        country = request.getParameter("country");
        languagePreference = request.getParameter("languagePreference");
        favouriteCategoryId = request.getParameter("favouriteCategoryId");
        Account account = (Account) session.getAttribute("account");
        username = account.getUsername();

        if(request.getParameter("listOption") != null){
            listOption = true;
        }else {
            listOption = false;
        }
        if(request.getParameter("bannerOption") != null){
            bannerOption = true;
        }else {
            bannerOption = false;
        }

        if(!username.isEmpty() && !password.isEmpty()){
            account = new Account();
            account.setUsername(username);
            account.setPassword(password);
            account.setFirstName(firstname);
            account.setLastName(lastname);
            account.setEmail(email);
            account.setPhone(phone);
            account.setAddress1(address1);
            account.setAddress2(address2);
            account.setCity(city);
            account.setState(state);
            account.setZip(zip);
            account.setCountry(country);
            account.setLanguagePreference(languagePreference);
            account.setFavouriteCategoryId(favouriteCategoryId);

            accountService.updateAccount(account);
            account = accountService.getAccount(account.getUsername());
            account.setListOption(listOption);
            account.setBannerOption(bannerOption);

            session.setAttribute("account",account);
            return "catalog/main";
        }else{
            request.setAttribute("languages",LANGUAGE_LIST);
            request.setAttribute("categories",CATEGORY_LIST);
            return "account/editAccountForm";
        }
    }

    @GetMapping("/account/signOut")
    public String accountSignOut(HttpSession session,HttpServletRequest request,Model model){
        Cart cart;
        session.removeAttribute("cartItemNumber");
        account = (Account) session.getAttribute("account");
        cart = (Cart) session.getAttribute("cart");

        account=null;
        cart=null;
        session.setAttribute("account",account);
        session.setAttribute("cart",cart);
        int cartItemNumber = 0;
        session.setAttribute("cartItemNumber", cartItemNumber);

        return "/catalog/main";
    }

    @ResponseBody
    @GetMapping("/account/accountCheckCode")
    public String accountCheckCode(HttpServletRequest request,HttpSession session,Model model){
        String checkCode;
        checkCode = request.getParameter("checkCode");
        if(checkCode.isEmpty()){
            String error = "验证码不能为空！";
            return error;
        }else if(!checkCode.equals(session.getAttribute("checkCode"))){
            String error = "验证码错误！";
            return error;
        }else {
//            inputStream = new ByteArrayInputStream("1".getBytes("UTF-8"));
//
            return "1";
        }
    }

    @GetMapping("/account/usernameIsExist")
    public void usernameIsExist(HttpServletResponse response,HttpServletRequest request,Model model,HttpSession session) throws IOException {
        String username=request.getParameter("username");
//        HttpServletResponse response = ServletActionContext.getResponse();
//        AccountService service = new AccountService();

        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
//        Map session = ActionContext.getContext().getSession();
        Account account = (Account) session.getAttribute("account");
        if(account != null){
            if (accountService.getAccount(username) != null && !username.equals(account.getUsername())){
                out.println("<msg>Exist</msg>");
            }
            else if (username != ""){
                out.println("<msg>NotExist</msg>");
            }
            else{
                out.println("<msg>PleaseInput</msg>");
            }
        }else{
            if (accountService.getAccount(username) != null){
                out.println("<msg>Exist</msg>");
            }
            else if (username != ""){
                out.println("<msg>NotExist</msg>");
            }
            else{
                out.println("<msg>PleaseInput</msg>");
            }
        }
        out.flush();
        out.close();
    }
}
