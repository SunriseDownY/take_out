package com.endeavor.take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.endeavor.take_out.entity.OrderDetail;
import com.endeavor.take_out.mapper.OrderDetailMapper;
import com.endeavor.take_out.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author Sunrise
 * @create 2022-10-06 17:39
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
