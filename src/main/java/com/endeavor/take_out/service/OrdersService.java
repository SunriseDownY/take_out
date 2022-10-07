package com.endeavor.take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.endeavor.take_out.entity.Orders;

/**
 * @author Sunrise
 * @create 2022-10-06 17:35
 */
public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);
}
