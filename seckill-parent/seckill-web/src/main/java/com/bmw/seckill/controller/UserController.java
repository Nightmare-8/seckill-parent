package com.bmw.seckill.controller;

import com.alibaba.fastjson.JSON;
import com.bmw.seckill.common.base.BaseRequest;
import com.bmw.seckill.common.base.BaseResponse;
import com.bmw.seckill.common.entity.CommonWebUser;
import com.bmw.seckill.common.exception.ErrorMessage;
import com.bmw.seckill.model.SeckillUser;
import com.bmw.seckill.model.http.SeckillReq;
import com.bmw.seckill.model.http.UserReq;
import com.bmw.seckill.model.http.UserResp;
import com.bmw.seckill.security.WebUserUtil;
import com.bmw.seckill.service.UserService;
import com.bmw.seckill.util.RedisUtil;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserController {

    private final String USER_PHONE_CODE_BEFORE = "u:p:c:b:";

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/getPhoneSmsCode")
    public BaseResponse<Boolean> getPhoneSmsCode(@Valid @RequestBody BaseRequest<UserReq.BaseUserInfo> req) {
        String phone = req.getData().getPhone();
        SeckillUser seckillUser = userService.findByPhone(phone);
        //先判断用户存在
        //接下来是调用第三方http接口发送短信验证码，通过验证码存储在redis中，方便后续判断，此处不展示http接口调用了
        if (seckillUser != null) {
            //短信验证码
            String randomCode = "123456";
            redisUtil.set(USER_PHONE_CODE_BEFORE + phone, randomCode,60*30);
            return BaseResponse.ok(true);
        } else return BaseResponse.ok(false);
    }


    @PostMapping("/userPhoneLogin")
    public BaseResponse userPhoneLogin(@Valid @RequestBody BaseRequest<UserReq.LoginUserInfo> req) throws Exception{
        UserReq.LoginUserInfo loginInfo = req.getData();

        Object existObj = redisUtil.get(USER_PHONE_CODE_BEFORE + loginInfo.getPhone());
        if (existObj == null || !existObj.toString().equals(loginInfo.getSmsCode())) {
            return BaseResponse.error(ErrorMessage.SMSCODE_ERROR);
        } else {
            redisUtil.del(USER_PHONE_CODE_BEFORE + loginInfo.getPhone());

            SeckillUser seckillUser = userService.findByPhone(loginInfo.getPhone());
            CommonWebUser commonWebUser = new CommonWebUser();
            BeanUtils.copyProperties(commonWebUser, seckillUser);
            String token = UUID.randomUUID().toString().replaceAll("-","");
            //设置token超时时间为1个月，实际根据需求确定
            redisUtil.set(token, JSON.toJSONString(commonWebUser), 60*60*24*30);
            UserResp.BaseUserResp resp = new UserResp.BaseUserResp();
            resp.setToken(token);
            return BaseResponse.ok(resp);
        }

    }

    @GetMapping("/checkUserToken")
    public void checkUserToken() {
        CommonWebUser commonWebUser = WebUserUtil.getLoginUser();
        log.info(JSON.toJSONString(commonWebUser));
    }
}
