package org.csu.mypetstore_springboot.service;

import org.csu.mypetstore_springboot.domain.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OrderService {
    public void insertOrder(Order order);
    public Order getOrder(int orderId);
    public List<Order> getOrdersByUsername(String username);
    public int getNextId(String name) ;
    public int getCurrentId();
}
