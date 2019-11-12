package org.csu.mypetstore_springboot.controller;

import org.csu.mypetstore_springboot.domain.Category;
import org.csu.mypetstore_springboot.domain.Item;
import org.csu.mypetstore_springboot.domain.Product;
import org.csu.mypetstore_springboot.service.AccountService;
import org.csu.mypetstore_springboot.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.util.*;

@Controller
public class CatalogController {
    @Autowired
    private CatalogService catalogService;

    private Map<String, List<Item>> itemlists;

    @GetMapping("/catalog/main")
    public String viewMain(Model model) {
        List<Product> fishes = catalogService.getProductListByCategory("FISH");
        List<Product> dogs = catalogService.getProductListByCategory("DOGS");
        List<Product> cats = catalogService.getProductListByCategory("CATS");
        List<Product> reptiles = catalogService.getProductListByCategory("REPTILES");
        List<Product> birds = catalogService.getProductListByCategory("BIRDS");

        Iterator<Product> it = fishes.iterator();
        itemlists = new HashMap<>();
        while (it.hasNext()) {
            String data = it.next().getProductId();
            itemlists.put(data, catalogService.getItemListByProduct(data));
        }

        Iterator<Product> it1 = dogs.iterator();
        while (it1.hasNext()) {
            String data = it1.next().getProductId();
            itemlists.put(data, catalogService.getItemListByProduct(data));
        }

        Iterator<Product> it2 = cats.iterator();
        while (it2.hasNext()) {
            String data = it2.next().getProductId();
            itemlists.put(data, catalogService.getItemListByProduct(data));
        }

        Iterator<Product> it3 = reptiles.iterator();
        while (it3.hasNext()) {
            String data = it3.next().getProductId();
            itemlists.put(data, catalogService.getItemListByProduct(data));
        }

        Iterator<Product> it4 = birds.iterator();
        while (it4.hasNext()) {
            String data = it4.next().getProductId();
            itemlists.put(data, catalogService.getItemListByProduct(data));
        }

        model.addAttribute("fishes", fishes);
        model.addAttribute("dogs", dogs);
        model.addAttribute("cats", cats);
        model.addAttribute("reptiles", reptiles);
        model.addAttribute("birds", birds);
        model.addAttribute("itemlists", itemlists);

        return "catalog/main";
    }

    @GetMapping("/catalog/category")
    public String viewCategory(@RequestParam("categoryId") String categoryId, Model model){
        if(categoryId != null){
            Category category = catalogService.getCategory(categoryId);
            List<Product> productList = catalogService.getProductListByCategory(categoryId);
            model.addAttribute("category", category);
            model.addAttribute("productList", productList);
        }

        return "catalog/category";
    }

    @GetMapping("/catalog/product")
    public String viewProduct(@RequestParam("productId") String productId,Model model){
        if (productId != null){
            Product product = catalogService.getProduct(productId);
            List<Item> itemList = catalogService.getItemListByProduct(productId);
            model.addAttribute("product",product);
            model.addAttribute("productId",productId);
            model.addAttribute("itemList",itemList);
        }
        return "catalog/product";
    }

    @GetMapping("/catalog/item")
    public String viewItem(@RequestParam("itemId") String itemId,Model model){
        Item item = catalogService.getItem(itemId);
        Product product = item.getProduct();

        model.addAttribute("item",item);
        model.addAttribute("product",product);

        return "catalog/item";
    }

    //根据关键字查找Product
    @PostMapping("/catalog/searchProducts")
    public String searchProducts(HttpServletRequest request,HttpSession session,Model model){
        String keywords = request.getParameter("searchInput");
        session.setAttribute("test","test");
        //System.out.println(searchInput);
        List<Product> productList = catalogService.searchProductList(keywords);
        Iterator<Product> iterator = productList.iterator();
        System.out.println(keywords);
        System.out.println(productList.size());
        model.addAttribute("productList",productList);
        return "catalog/searchProducts";
    }

    @ResponseBody
    @PostMapping("/catalog/searchProductAutoComplete")
    public String searchProductAutoComplete(HttpSession session,HttpServletRequest request,Model model){
        String[] s;
        String message="";

        List<String> nameList = catalogService.getNames();

        Iterator<String> it = nameList.iterator();
        s = new String[nameList.size()];

        int i = 0;
        while(it.hasNext()){
            s[i] = it.next();
            i ++;
        }

        Arrays.sort(s,String.CASE_INSENSITIVE_ORDER);
        i = 0;
        message += s[i];
        i ++;
        while (i < s.length){
            message += "*" + s[i];
            i ++;
        }

        return message;
    }
}
