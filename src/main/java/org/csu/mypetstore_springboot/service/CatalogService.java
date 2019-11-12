package org.csu.mypetstore_springboot.service;

import org.csu.mypetstore_springboot.domain.Category;
import org.csu.mypetstore_springboot.domain.Item;
import org.csu.mypetstore_springboot.domain.Product;

import java.util.List;

public interface CatalogService {

    List<Category> getCategoryList();

    Category getCategory(String categoryId);

    Product getProduct(String productId);

    List<Product> getProductListByCategory(String categoryId);

    List<String> getNames();

    List<Product> searchProductList(String keyword);

    List<Item> getItemListByProduct(String productId);

    Item getItem(String itemId);

    boolean isItemInStock(String itemId);

    int getInventoryQuantity(String itemId);
}
