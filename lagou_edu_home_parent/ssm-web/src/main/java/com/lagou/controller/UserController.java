package com.lagou.controller;

import com.github.pagehelper.PageInfo;
import com.lagou.domain.ResponseResult;
import com.lagou.domain.Role;
import com.lagou.domain.User;
import com.lagou.domain.UserVO;
import com.lagou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /*
        分页&多条件查询用户
     */
    @RequestMapping("/findAllUserByPage")
    public ResponseResult findAllUserByPage(@RequestBody UserVO userVO){
        PageInfo pageInfo = userService.findAllUserByPage(userVO);
        ResponseResult responseResult = new ResponseResult(true, 200, "分页&多条件查询成功", pageInfo);
        return responseResult;
    }

    /*
        根据id修改用户状态
     */
    @RequestMapping("/updateUserStatus")
    public ResponseResult updateUserStatus(Integer id,String status){
        userService.updateUserStatus(id,status);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("status",status);
        ResponseResult responseResult = new ResponseResult(true, 200, "根据id修改状态成功", map);
        return responseResult;
    }
    /*
        用户登录
     */
    @RequestMapping("/login")
    public ResponseResult login(User user, HttpServletRequest request) throws Exception {
        User login = userService.login(user);
        if (login!=null){
            // 保存用户id及access_token到session中
            HttpSession session = request.getSession();
            String access_token = UUID.randomUUID().toString();
            System.out.println(access_token);
            session.setAttribute("access_token",access_token);
            session.setAttribute("user_id",login.getId());

            // 将查询出来的信息响应给前台
            Map<String, Object> map = new HashMap<>();
            map.put("access_token",access_token);
            map.put("user_id",login.getId());

            // 将查询出来的user,也存到map中
            map.put("user",login);

            return new ResponseResult(true,1,"登陆成功",map);
        }else {
            ResponseResult responseResult = new ResponseResult(true, 400, "用户名密码错误", null);
            return responseResult;
        }

    }

    /*
        分配角色(回显)
     */
    @RequestMapping("/findUserRoleById")
    public ResponseResult findUserRelationRoleById(Integer id){
        List<Role> list = userService.findUserRelationRoleById(id);
        ResponseResult responseResult = new ResponseResult(true, 400, "分配角色（回显）成功", list);
        return responseResult;
    }

    /*
        分配角色
     */
    @RequestMapping("/userContextRole")
    public ResponseResult userContextRole(@RequestBody UserVO userVO){
        userService.userContextRole(userVO);
        ResponseResult responseResult = new ResponseResult(true, 400, "分配角色成功", null);
        return responseResult;
    }

    /*
        获取用户权限，进行菜单动态展示
     */
    @RequestMapping("/getUserPermissions")
    public ResponseResult getUserPermissions(HttpServletRequest request){
        //获取请求头中的token
        String header_token = request.getHeader("Authorization");

        //获取session中的token
        HttpSession session = request.getSession();
        String access_token = (String)session.getAttribute("access_token");

        //判断token是否一致
        if (header_token.equals(access_token)){
            Integer user_id = (Integer)session.getAttribute("user_id");
            ResponseResult userPermissions = userService.getUserPermissions(user_id);
            return userPermissions;
        }else {
            return new ResponseResult(false,400,"获取菜单信息失败",null);
        }

    }

}
