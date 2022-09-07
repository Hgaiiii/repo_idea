package com.lagou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lagou.dao.UserMapper;
import com.lagou.domain.*;
import com.lagou.service.UserService;
import com.lagou.utils.Md5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public PageInfo findAllUserByPage(UserVO userVO) {

        PageHelper.startPage(userVO.getCurrentPage(),userVO.getPageSize());
        List<User> list = userMapper.findAllUserByPage(userVO);
        PageInfo<User> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public void updateUserStatus(int id, String status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        user.setUpdate_time(new Date());
        userMapper.updateUserStatus(user);
    }

    @Override
    public User login(User user) throws Exception {
        User login = userMapper.login(user);
        if (login!=null&& Md5.verify(user.getPassword(),"lagou",login.getPassword())){
            return login;
        }else {
            return null;
        }
    }

    @Override
    public List<Role> findUserRelationRoleById(Integer id) {
        List<Role> roles = userMapper.findUserRelationRoleById(id);
        return roles;
    }

    @Override
    public void userContextRole(UserVO userVO) {
        //根据用户id清空中间表关系
        userMapper.deleteUserContextRole(userVO.getUserId());
        //重新建立中间表关系
        for (Integer rid : userVO.getRoleIdList()) {
            //封装数据
            User_Role_relation user_role_relation = new User_Role_relation();
            user_role_relation.setUserId(userVO.getUserId());
            user_role_relation.setRoleId(rid);
            Date date = new Date();
            user_role_relation.setCreatedTime(date);
            user_role_relation.setUpdatedTime(date);
            user_role_relation.setCreatedBy("system");
            user_role_relation.setUpdatedby("system");
            userMapper.userContextRole(user_role_relation);
        }
    }

    /*
        获取用户权限信息
     */
    @Override
    public ResponseResult getUserPermissions(Integer userId) {
        //获取当前用户所拥有的角色信息
        List<Role> roleList = userMapper.findUserRelationRoleById(userId);

        List<Integer> roleIds = new ArrayList<>();
        for (Role role : roleList) {
            Integer id = role.getId();
            roleIds.add(id);
        }
        //根据角色ID查询父菜单
        List<Menu> parentMenu = userMapper.findParentMenuByRoleId(roleIds);
        //查询封装父菜单关联的子菜单
        for (Menu menu : parentMenu) {
            List<Menu> subMenu = userMapper.findSubMenuByPid(menu.getId());
            menu.setSubMenuList(subMenu);

        }

        //获取资源信息
        List<Resource> resourceList = userMapper.findResourceByRoleId(roleIds);

        HashMap<String, Object> map = new HashMap<>();
        map.put("menuList",parentMenu);
        map.put("resourceList",resourceList);

        ResponseResult responseResult = new ResponseResult(true, 200, "获取用户权限信息成功", map);

        return responseResult;
    }
}
