package org.csu.mypetstore_springboot.persistence;

import org.apache.ibatis.annotations.Param;
import org.csu.mypetstore_springboot.domain.Product;

import java.util.List;

public interface ProductMapper {

    //根据类别返回产品清单
    List<Product> getProductListByCategory(String categoryId);

    //根据产品ID获得指定产品
    Product getProduct(String productId);

    //根据关键字搜索产品的集合
    List<Product> searchProductList(@Param("keywords") String keywords);

    //直接搜索所有产品的集合
    List<String> searchProductNameList();
}
