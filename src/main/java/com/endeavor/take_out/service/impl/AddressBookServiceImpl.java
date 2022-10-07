package com.endeavor.take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.endeavor.take_out.entity.AddressBook;
import com.endeavor.take_out.mapper.AddressBookMapper;
import com.endeavor.take_out.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author Sunrise
 * @create 2022-10-05 0:14
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
