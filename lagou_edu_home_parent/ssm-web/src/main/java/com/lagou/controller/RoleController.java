package com.lagou.controller;

import com.lagou.domain.*;
import com.lagou.service.MenuService;
import com.lagou.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /*
        查询所有角色（条件）
     */
    @RequestMapping("/findAllRole")
    public ResponseResult findAllRole(@RequestBody Role role){
        List<Role> list = roleService.findAllRole(role);

        ResponseResult responseResult = new ResponseResult(true, 200, "查询所有角色成功", list);
        return responseResult;

    }

    @Autowired
    private MenuService menuService;

    /*
        查询所有的父子级菜单
     */
    @RequestMapping("/findAllMenu")
    public ResponseResult findSubMenuListByPid(){
        //-1表示查询所有的父子级菜单
        List<Menu> subMenuListByPid = menuService.findSubMenuListByPid(-1);

        Map<String, Object> map = new HashMap<>();
        map.put("parentMenuList",subMenuListByPid);

        ResponseResult responseResult = new ResponseResult(true, 200, "查询所有的父子级菜单成功", map);
        return responseResult;
    }

    /*
        根据角色id查询关联的菜单信息ID
     */
    @RequestMapping("/findMenuByRoleId")
    public ResponseResult findMenuByRoleId(Integer id){
        List<Integer> menuByRoleId = roleService.findMenuByRoleId(id);
        ResponseResult responseResult = new ResponseResult(true, 200, "查询角色关联的菜单ID成功", menuByRoleId);
        return responseResult;
    }

    /*
        为角色分配菜单
     */
    @RequestMapping("/RoleContextMenu")
    public ResponseResult roleContextMenu(@RequestBody RoleMenuVO roleMenuVO){
        roleService.roleContextMenu(roleMenuVO);
        ResponseResult responseResult = new ResponseResult(true, 200, "角色分配菜单成功", null);
        return responseResult;
    }

    /*
        删除角色
     */
    @RequestMapping("/deleteRole")
    public ResponseResult deleteRole(Integer rid){
        roleService.deleteRole(rid);
        ResponseResult responseResult = new ResponseResult(true, 200, "删除角色成功", null);
        return responseResult;
    }
}
