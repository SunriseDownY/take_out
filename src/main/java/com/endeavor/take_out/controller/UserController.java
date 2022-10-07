package com.endeavor.take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.endeavor.take_out.common.R;
import com.endeavor.take_out.entity.User;
import com.endeavor.take_out.service.UserService;
import com.endeavor.take_out.util.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author Sunrise
 * @create 2022-10-04 17:25
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Resource
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 给前端输入的邮箱，发送验证码
     *
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        String email = user.getEmail();
        if (email == null) {
            return R.error("邮箱为空");
        }
        //随机生成码
        String code = String.valueOf(ValidateCodeUtils.generateValidateCode(6));
        session.setAttribute("email", email);
        session.setAttribute("code", code);
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setSubject("外卖邮箱验证码");
            mailMessage.setText("您收到的验证码是："+code);
            mailMessage.setTo(email);
            mailMessage.setFrom(from);
            javaMailSender.send(mailMessage);
            return R.success("验证码发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("验证码发送失败");
        }
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map<String, String> map, HttpSession session) {
        String email = map.get("email");
        // 获取验证码
        String code = map.get("code");
        // 从session中获取验证码

        // 从缓存中获取验证码
        String codeInSession = (String) session.getAttribute("code");
        //从session中获取请求验证码的手机号
        String emailInSession = (String) session.getAttribute("email");
        // 进行验证码比对
        if (emailInSession == null || !codeInSession.equals(code) || !emailInSession.equals(email)) {
            return R.error("验证码错误");
        }
        // 判断该用户是否注册
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getEmail, email);
        User user = userService.getOne(lqw);
        if (user == null) {
            // 用户还未注册，自动注册
            user = new User();
            user.setEmail(email);
            userService.save(user);
        }
        //设置session
        session.setAttribute("user", user.getId());
        session.setMaxInactiveInterval(6 * 60 * 60);
        //删除验证码缓存

        return R.success(user);
    }
}