package org.csu.mypetstore_springboot.persistence;

import org.csu.mypetstore_springboot.domain.LineItem;

import java.util.List;

public interface LineItemMapper {
    List<LineItem> getLineItemsByOrderId(int orderId);

    void insertLineItem(LineItem lineItem);
}
