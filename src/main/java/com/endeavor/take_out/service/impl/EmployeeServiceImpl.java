package com.endeavor.take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.endeavor.take_out.entity.Employee;
import com.endeavor.take_out.mapper.EmployeeMapper;
import com.endeavor.take_out.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author Sunrise
 * @create 2022-09-29 11:12
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
