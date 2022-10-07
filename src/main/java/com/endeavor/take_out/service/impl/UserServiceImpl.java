package com.endeavor.take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.endeavor.take_out.entity.User;
import com.endeavor.take_out.mapper.UserMapper;
import com.endeavor.take_out.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author Sunrise
 * @create 2022-10-04 17:23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
