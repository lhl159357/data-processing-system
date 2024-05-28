package com.springboot.service.impl;

import com.springboot.entity.MonthlyActiveUser;
import com.springboot.mapper.MonthlyActiveUserMapper;
import com.springboot.service.IMonthlyActiveUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lhl
 * @since 2024-05-20
 */
@Service
public class MonthlyActiveUserServiceImpl extends ServiceImpl<MonthlyActiveUserMapper, MonthlyActiveUser> implements IMonthlyActiveUserService {

}
