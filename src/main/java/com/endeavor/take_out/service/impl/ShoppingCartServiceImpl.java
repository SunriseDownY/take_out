package com.endeavor.take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.endeavor.take_out.entity.ShoppingCart;
import com.endeavor.take_out.mapper.ShoppingCartMapper;
import com.endeavor.take_out.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author Sunrise
 * @create 2022-10-05 22:25
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
