package com.mr.controller;

import com.mr.model.TMallShoppingCar;
import com.mr.model.TMallUserAccount;
import com.mr.service.CartService;
import com.mr.util.MyCookieUtils;
import com.mr.util.MyJsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lmy on 2018/11/7.
 */
@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private RedisTemplate redisTemplate;

    //获取合计值
    //提取冗余的代码，static静态的Login控制层再调用
    public static Double getHj(TMallShoppingCar cart){

        BigDecimal jg = new BigDecimal(cart.getSkuJg() + "");
        BigDecimal thShl = new BigDecimal(cart.getTjshl());

        double hj = thShl.multiply(jg).doubleValue();
        return hj;
    }
    /**
     * 保存购物车
     *
     * cookieName : cookieCartList
     *
     * @CookieValue("key")：从cookie对象中获取名为key的cookie值
     *  将list对象转为json字符串 存放在cookie中
     *
     * @param cart
     * @param session
     * @return
     */
    @RequestMapping("saveCart")
    public String saveCart(TMallShoppingCar cart, HttpSession session,
                           @CookieValue(value = "cookieCartList",required = false)
                                   String cookieCartList,ModelMap map,
                           HttpServletRequest request , HttpServletResponse response){

        //算合计 ：当前数量乘以价格
        cart.setHj(getHj(cart));
        //定义购物车集合
        List<TMallShoppingCar> cartList = new ArrayList<TMallShoppingCar>();

        //判断是否登录
        TMallUserAccount user = (TMallUserAccount)session.getAttribute("user");

        if(user == null){ //未登录
            if(StringUtils.isBlank(cookieCartList)){//为空：直接添加在cookie中
                //给购物车集合添加数据
                cartList.add(cart);
            }else{//有数据，则需要判断更新
                //如何判断是否存在的
                cartList = MyJsonUtil.jsonToList(cookieCartList, TMallShoppingCar.class);
                boolean b1 = false;
                for (int i = 0; i < cartList.size(); i++) {
                    if(cartList.get(i).getSkuId() == cart.getSkuId()){//说明存在
                        b1 = true;
                    }
                }
                if(b1){//存在
                    //更新：拿到购物车中的数据，更新，循环

                    for (int i = 0; i < cartList.size(); i++) {
                        if(cartList.get(i).getSkuId() == cart.getSkuId()){//一样则修改数量
                            cartList.get(i).setTjshl(cartList.get(i).getTjshl() + cart.getTjshl());

                            //计算金额
                  /*          BigDecimal jg=new BigDecimal(cartList.get(i).getSkuJg()+ "");
                            BigDecimal shl=new BigDecimal(cartList.get(i).getTjshl());*/
                            cartList.get(i).setHj(getHj(cartList.get(i)));
                        }
                    }
                }else{//不存在
                    cartList.add(cart);
                }
            }

            //第二种方式    :存放在cookie中
            Cookie cookie = new Cookie("cookieCartList", MyJsonUtil.objectToJson(cartList));   //把对象放到cookie中
            cookie.setMaxAge(3600);
            response.addCookie(cookie);
        }else{//登录

            //判断数据库是否有数据
            //获取数据
            cartList=cartService.listCartByUserId(user.getId());

            if(cartList !=null && cartList.size() > 0){ //有数据
                //获取数据
                boolean b2 =false;

                //循环遍历 。如果存在
                for (int i = 0; i < cartList.size(); i++) {
                    if(cartList.get(i).getSkuId() == cart.getSkuId()){
                        b2=true;
                    }
                }
                if(b2){ //存在
                    //更新数据
                    for (int i = 0; i < cartList.size(); i++) {
                        if(cartList.get(i).getSkuId() == cart.getSkuId()){

                            Map<String, Object> cartMap = new HashMap<>();
                            cartMap.put("skuId",cartList.get(i).getSkuId());
                            cartMap.put("userId",user.getId());
                            cartMap.put("tjshl",cartList.get(i).getTjshl() + cart.getTjshl());
                            cartList.get(i).setTjshl(cartList.get(i).getTjshl() + cart.getTjshl());
                            //改变当前添加数量
                            cartList.get(i).setTjshl(cartList.get(i).getTjshl() + cart.getTjshl());
                           /* BigDecimal jg = new BigDecimal(cartList.get(i).getSkuJg() + "");
                            BigDecimal shl = new BigDecimal(cartList.get(i).getTjshl());*/


                            cartMap.put("hj",getHj(cartList.get(i)));


                            cartService.updateCartBySkuIdAndUserId(cartMap);
                        }
                    }
                }else {
                        //添加数据
                    cart.setYhId(user.getId());
                    cartService.saveCart(cart);
                }
            }else {// 没有数据
                    //添加数据
                cart.setYhId(user.getId());
                cartService.saveCart(cart);
            }
            //更新redis (清空redis中cart的list，当前用户) 确保key的唯一性
            redisTemplate.delete("redisCartListUser"+user.getId());

            /*用户登录之后，将数据保存在redis中
            当前用户的key如何确定
            redisTemplate.opsForValue().set("redisCartListUser"+user.getId(),cartList);
*/
        }
        map.put("cart",cart);
        return "cart-success";
    }

    /**
     * 查询mini购物
     * @return
     */
    @RequestMapping("fingMiniCart")
    public String fingMiniCart(HttpSession session, ModelMap map,
                               @CookieValue(value = "cookieCartList",required = false)
                                       String cookieCartList){
        List<TMallShoppingCar> cartList=new ArrayList<>();
        TMallUserAccount user =(TMallUserAccount) session.getAttribute("user");
        if(user ==null){ //未登录
             cartList = MyJsonUtil.jsonToList(cookieCartList, TMallShoppingCar.class);
        }else {// 登陆
            //从redis中获取数据
            cartList = (List<TMallShoppingCar>)redisTemplate.opsForValue().get("redisCartListUser" + user.getId());

            if(cartList == null || cartList.size() > 0){ //没有数据
                //直接将数据返回
                cartList = cartService.listCartByUserId(user.getId());
                redisTemplate.opsForValue().set("redisCartListUser"+user.getId(),cartList);
            } //没有、同步redis
                //查询数据库 通过用户查询
        }

        Integer countNum = 0;
        for (int i = 0; i < cartList.size(); i++) {
            countNum +=cartList.get(i).getTjshl();
        }
        map.put("cartList",cartList);
        map.put("countNum",countNum);
        map.put("hjSum",getSum(cartList));
        return "miniCartInner";
    }

    // 计算金额 BigDecimal
    public BigDecimal getSum(List<TMallShoppingCar> cartList){
        BigDecimal sum =new BigDecimal("0");
        for (int i = 0; i < cartList.size(); i++) {
            if(cartList.get(i).getShfxz().equals("1")){ //String 类型用equals 判断   是否选中字段 1选中
                sum=sum.add(new BigDecimal(cartList.get(i).getHj() +  ""));
            }
        }
            return sum;
    }

    /**
     * 跳转购物车页面
     * @return
     */
    @RequestMapping("toCartListPage")
    public String toCartListPage(HttpSession session, ModelMap map,
                                 @CookieValue(value = "cookieCartList",required = false) String cookieCartList){

        List<TMallShoppingCar> cartList = new ArrayList<>();
        //判断是否登录
        TMallUserAccount user = (TMallUserAccount)session.getAttribute("user");

        if(user == null){//未登录
            cartList = MyJsonUtil.jsonToList(cookieCartList, TMallShoppingCar.class);
        }else{//登录

            //从redis中获取数据
            cartList = (List<TMallShoppingCar>)redisTemplate.opsForValue().get("redisCartListUser" + user.getId());
            if(cartList == null || cartList.size() == 0){//没有数据
                //查询数据库、通过用户
                cartList = cartService.listCartByUserId(user.getId());
                redisTemplate.opsForValue().set("redisCartListUser"+user.getId(),cartList);
            }
        }

        /*Integer countNum = 0;
        for (int i = 0; i < cartList.size(); i++) {
            countNum += cartList.get(i).getTjshl();
        }*/
        map.put("cartList",cartList);
        //map.put("countNum",countNum);
        map.put("hjSum",getSum(cartList));
        return "cartList";
    }

    /**
     * 根据skuid修改对象的选中状态，并且刷新合计
     * @return
     */
    @RequestMapping("changeShfxz")      //获取参数 skuid String类型的 Shfxz（是否选中）
    public String changeShfxz(HttpServletResponse response ,HttpServletRequest request,
                              int skuId , String shfxz , ModelMap map,HttpSession session, //放在session 中
                              @CookieValue(value = "cookieCartList",required = false) String cookieCartList){


        List<TMallShoppingCar> cartList = new ArrayList<>();
        //判断是否登录
        TMallUserAccount user = (TMallUserAccount)session.getAttribute("user");

        if(user != null){ //登录     当前对象非空的情况下是登陆状态
            //通过skuid 修改cart
            //登陆成功后直接去redis中查找     获取的这个对象
            cartList =  (List<TMallShoppingCar>)redisTemplate.opsForValue().get("redisCartListUser"+user.getId());
            //然后更新数据库
            for (int i = 0; i < cartList.size(); i++) {
                if(cartList.get(i).getSkuId() == skuId) {
                    //判断集合中的skuid 是否和 当前skuid 相等
                    cartService.updateCartShfxzBySkuIdAndUserId(skuId, user.getId(), shfxz);
                    //修改的是从redis获取到的购物车
                    cartList.get(i).setShfxz(shfxz);
                }
            }
            //再同步redis中
            redisTemplate.opsForValue().set("cookieCartList"+user.getId(),cartList);
        }else { //未登录
            cartList = MyJsonUtil.jsonToList(cookieCartList,TMallShoppingCar.class);
            for (int i = 0; i < cartList.size(); i++) {
                //如果skuId一样的话，修改该对象的状态。
                if(cartList.get(i).getSkuId() == skuId){
                    cartList.get(i).setShfxz(shfxz);
                }
            }
            //更新cookie 把对象放进去 改为json 数据
            MyCookieUtils.setCookie(request,response,"cookieCartList",
                    MyJsonUtil.objectToJson(cartList),3*24*60*60,true);
        }

        map.put("cartList",cartList);
        map.put("hjSum",getSum(cartList));
        return "cartListInner";
    }
}
