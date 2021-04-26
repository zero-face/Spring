package com.zero.service.impl;

import com.zero.dao.ItemDaoMapper;
import com.zero.dao.ItemStockDaoMapper;
import com.zero.dataobject.ItemDao;
import com.zero.dataobject.ItemStockDao;
import com.zero.error.BusinessException;
import com.zero.error.EmBustinessError;
import com.zero.service.ItemService;
import com.zero.service.model.ItemModel;
import com.zero.validator.ValidationResult;
import com.zero.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Zero
 * @Date: 2021/4/4 - 14:09
 * @since: jdk 1.8
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ValidatorImpl validator;
    @Autowired
    private ItemDaoMapper itemDaoMapper;
    @Autowired
    private ItemStockDaoMapper itemStockDaoMapper;

    //创建商品依赖，将model转化为item表的dataobject
    private ItemDao convertItemDaoFromItemModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemDao itemDao = new ItemDao();
        BeanUtils.copyProperties(itemModel,itemDao);
        itemDao.setPrice(itemModel.getPrice());
        return itemDao;
    }
    //创建商品依赖，将model转化为stock表的dataobject
    private ItemStockDao convertItemStockFromItemModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemStockDao itemStockDao = new ItemStockDao();
        itemStockDao.setItemId(itemModel.getId());
        itemStockDao.setStock(itemModel.getStock());
        return itemStockDao;
    }
    //创建商品（将商品信息添加进数据库）
    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        //校验入参是否合法
        ValidationResult result = validator.validate(itemModel);
        if (result.isHasErrs()) {
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }
        //转化itemmodel转化为dataobject
        ItemDao itemDao = convertItemDaoFromItemModel(itemModel);
        //写入数据库
        int num = itemDaoMapper.insert(itemDao);
        itemModel.setId(itemDao.getId());
        ItemStockDao itemStockDao = convertItemStockFromItemModel(itemModel);
        itemStockDaoMapper.insert(itemStockDao);
        //返回完成的对象(直接从数据库中拿出创建的商品用于返回)
        return getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        List<ItemDao> list = itemDaoMapper.selectAll();

        List<ItemModel> itemModelList = list.stream().map(itemDao -> {
            ItemStockDao itemStockDao = itemStockDaoMapper.selectByItemId(itemDao.getId());
            ItemModel itemModel = convertItemModelFromdataobject(itemDao,itemStockDao);
            return itemModel;
        }).collect(Collectors.toList());
        return itemModelList;
    }

    //根据id获取商品依赖
    private ItemModel convertItemModelFromdataobject(ItemDao itemDao, ItemStockDao itemStockDao) {
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDao,itemModel);
        itemModel.setStock(itemStockDao.getStock());

        return itemModel;
    }
    @Override
    public ItemModel getItemById(Integer id) {
        ItemDao itemDao = itemDaoMapper.selectByPrimaryKey(id);
        if(itemDao == null) {
            return null;
        }
        //操作获得库存数量
        ItemStockDao itemStockDao = itemStockDaoMapper.selectByItemId(itemDao.getId());
        //将dataobject封装成核心领域模型
        ItemModel itemModel = convertItemModelFromdataobject(itemDao, itemStockDao);
        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId,Integer amount) {
        int affectRow = itemStockDaoMapper.decreaseStock(itemId,amount);//使用法affectrow去判断是否更新了表里的数据
        if(affectRow > 0) {
            //更新成功
            return true;
        }
        return false;

    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) {
        itemDaoMapper.increaseSales(itemId,amount);
    }


}
