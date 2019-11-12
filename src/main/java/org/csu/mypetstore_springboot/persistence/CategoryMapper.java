package org.csu.mypetstore_springboot.persistence;

import org.csu.mypetstore_springboot.domain.Category;

import java.util.List;

public interface CategoryMapper {

    //返回所有大类的一个集合
    List<Category> getCategoryList();

    //根据类的ID返回某一类
    Category getCategory(String categoryId);
}
