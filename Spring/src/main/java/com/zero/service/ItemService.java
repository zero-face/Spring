package com.zero.service;

import com.zero.error.BusinessException;
import com.zero.service.model.ItemModel;

import java.util.List;

/**
 * @author: Zero
 * @Date: 2021/4/4 - 14:05
 * @since: jdk 1.8
 */
public interface ItemService {
    //创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;
    //商品列表浏览
    List<ItemModel> listItem();
    //商品详情浏览
    ItemModel getItemById(Integer id);
    //库存扣减
    boolean decreaseStock(Integer itemId,Integer amount);
    //商品销量增加
    void increaseSales(Integer itemId,Integer amount);
}
