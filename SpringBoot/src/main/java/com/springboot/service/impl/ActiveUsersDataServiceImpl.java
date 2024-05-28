package com.springboot.service.impl;

import com.springboot.entity.ActiveUsersData;
import com.springboot.mapper.ActiveUsersDataMapper;
import com.springboot.service.IActiveUsersDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lhl
 * @since 2024-05-15
 */
@Service
public class ActiveUsersDataServiceImpl extends ServiceImpl<ActiveUsersDataMapper, ActiveUsersData> implements IActiveUsersDataService {

}
