package com.mr.controller;

import com.mr.model.TMallShoppingCar;
import com.mr.model.TMallUserAccount;
import com.mr.service.CartService;
import com.mr.service.UserService;
import com.mr.util.MyCookieUtils;
import com.mr.util.MyJsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Lmy on 2018/11/5.
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private RedisTemplate redisTemplate;
    @RequestMapping("login")
    public String login(String userName , String password, HttpSession session ,
                        HttpServletRequest request  , HttpServletResponse response,
                        @CookieValue(value = "cookieCartList",required = false)
                                    String cookieCartList ){


        TMallUserAccount user=userService.findUserNamePswd(userName,password);
        if(user==null){
            //判断user如果为空的情况下 返回登录页面
            return "redirect:toLoginPage.do";
        }else {
        }
        //将对象放到cookie中
        session.setAttribute("user",user); //第一种方式
        String yhMch = user.getYhMch();
        //MyCookieUtils.setCookie(request,response,"yhMch",yhMch,24*60*60,true);

        //更新购物车
        if(!StringUtils.isBlank(cookieCartList)){ //有数据
            //将cookie中的数据添加在db
            List<TMallShoppingCar> cartListCookie = MyJsonUtil.jsonToList(cookieCartList, TMallShoppingCar.class);

            List<TMallShoppingCar> carListDb = cartService.listCartByUserId(user.getId());

            for (int i = 0; i < cartListCookie.size(); i++) { //cookie的
                        //如何判断当前对象是否重复
                        //根据当前对象的skuid和用户id查询数据
                    TMallShoppingCar cart=cartService.findCartBySkuIdAngUserId(cartListCookie.get(i).getSkuId(),user.getId()); //通过skuid和用户id去查

               if(cart!=null){ //重复
                   //更新
                   Map<String, Object> cartMap = new HashMap<>();
                   cartMap.put("skuId",cartListCookie.get(i).getSkuId());
                   cartMap.put("userId",user.getId());

                   //修改对象的数量
                   cartMap.put("tjshl",cartListCookie.get(i).getTjshl()+cart.getTjshl());
                   cartListCookie.get(i).setTjshl(cartListCookie.get(i).getTjshl()+cart.getTjshl());
                    cartListCookie.get(i).setTjshl(cartListCookie.get(i).getTjshl()+cart.getTjshl());
                        /*BigDecimal jg = new BigDecimal(cartListCookie.get(i).getSkuJg() + "");
                        BigDecimal shl = new BigDecimal(cartListCookie.get(i).getTjshl()+cart.getTjshl());*/



                   cartMap.put("hj",CartController.getHj(cartListCookie.get(i)));


                   cartService.updateCartBySkuIdAndUserId(cartMap);
               }else {
                   //添加当前对象
                   cartListCookie.get(i).setYhId(user.getId());
                   cartService.saveCart(cartListCookie.get(i));
               }
            }

            //清空cookie中的购物车
            MyCookieUtils.deleteCookie(request,response,"cookieCartList");

            //清空redis中的购物车
            redisTemplate.delete("redisCartListUser"+ user.getId());
        }



        //第二种方式
        Cookie cookie = new Cookie("yhMch", yhMch);   //把对象放到cookie中
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
        //user有 id 情况下进入首页
      return "redirect:toMainPage.do";
    }

    //用户注销
    @RequestMapping("logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:toLoginPage.do";
    }
}
