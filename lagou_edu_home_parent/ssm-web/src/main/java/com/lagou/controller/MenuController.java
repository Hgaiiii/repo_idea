package com.lagou.controller;

import com.github.pagehelper.PageInfo;
import com.lagou.domain.Menu;
import com.lagou.domain.ResponseResult;
import com.lagou.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;


    /*
        查询所有菜单信息
     */
    @RequestMapping("/findAllMenu")
    public ResponseResult findAllMenu(){
        List<Menu> allMenu = menuService.findAllMenu();
        ResponseResult responseResult = new ResponseResult(true, 200, "查询所有菜单信息成功", allMenu);
        return responseResult;
    }

    /*
        回显菜单信息
     */
    @RequestMapping("/findMenuInfoById")
    public ResponseResult findMenuInfoById(Integer id){
        //根据id值判断当前是更新还是添加操作 id值等于-1为添加
        if (id == -1){
            //新增回显操作
            List<Menu> list = menuService.findSubMenuListByPid(id);
            HashMap<Object, Object> map = new HashMap<>();
            map.put("menuInfo",null);
            map.put("parentMenuList",list);
            ResponseResult responseResult = new ResponseResult(true,200,"新增回显成功",map);
            return responseResult;
        }else {
            //更新回显操作
            Menu menu = menuService.findMenuById(id);
            List<Menu> list = menuService.findSubMenuListByPid(id);
            HashMap<Object, Object> map = new HashMap<>();
            map.put("menuInfo",menu);
            map.put("parentMenuList",list);
            ResponseResult responseResult = new ResponseResult(true,200,"修改回显成功",map);
            return responseResult;
        }
    }
}
