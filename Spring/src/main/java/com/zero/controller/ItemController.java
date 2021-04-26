package com.zero.controller;

import com.zero.viewobject.ItemVO;
import com.zero.error.BusinessException;
import com.zero.service.ItemService;
import com.zero.service.model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.zero.response.CommonReturnType;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Zero
 * @Date: 2021/4/4 - 14:50
 * @since: jdk 1.8
 */
@Controller("/item")
@RequestMapping("/item")
public class ItemController extends BaseController{
    @Autowired
    private ItemService itemService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    private ItemVO convertItemVOFromItemModel(ItemModel item) {
        if(item == null) {
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(item,itemVO);
        return itemVO;
    }
    //创建商品的接口
    @RequestMapping(value = "/create",method={RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createItem(
            @RequestParam(name ="title") String title,
            @RequestParam(name="description") String description,
            @RequestParam(name="price")double price,
            @RequestParam(name="stock") Integer stock,
            @RequestParam(name="imgUrl") String imgUrl) throws BusinessException {
        //封装service请求用来创建商品
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setimgUrl(imgUrl);
        ItemModel item = itemService.createItem(itemModel);

        //将后端返回的数据库商品转化为VO对象
        ItemVO itemVO = convertItemVOFromItemModel(item);
        return CommonReturnType.create(itemVO);
    }
    //根据商品id浏览商品信息
    @RequestMapping(value = "/get",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getItem(@RequestParam(name="id") Integer id){
        //进入了商品详情页面
        System.out.println("进入了商品详情页面");
        //System.out.println(httpServletRequest.getParameter("id"));
        ItemModel item = itemService.getItemById(id);
        ItemVO itemVO = convertItemVOFromItemModel(item);
        return CommonReturnType.create(itemVO);
    }



    //商品列表页面浏览
    @RequestMapping(value = "/list",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listItem() {
        System.out.println("进入了浏览后台");
        List<ItemModel> itemModelList = itemService.listItem();

        List<ItemVO> itemVOList = itemModelList.stream().map(itemModel -> {
            ItemVO itemVO = convertItemVOFromItemModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());

        return CommonReturnType.create(itemModelList);
    }

}
