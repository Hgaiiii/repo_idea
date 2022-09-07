package com.lagou.service;

import com.lagou.domain.Menu;

import java.util.List;

public interface MenuService {
    /*
        查询所有父子菜单信息
     */
    public List<Menu> findSubMenuListByPid(int id);

    /*
       查询所有菜单信息
    */
    public List<Menu> findAllMenu();

    public Menu findMenuById(Integer id);
}
