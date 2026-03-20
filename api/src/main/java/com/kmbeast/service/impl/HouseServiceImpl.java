package com.kmbeast.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmbeast.mapper.HouseMapper;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.HouseQueryDto;
import com.kmbeast.pojo.entity.House;
import com.kmbeast.pojo.vo.HouseListItemVO;
import com.kmbeast.service.HouseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 房屋业务逻辑接口实现类
 */
@Service
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House> implements HouseService {

    /**
     * 房屋列表查询
     *
     * @param houseQueryDto 查询条件类
     * @return Result<List < HouseListItemVO>>
     */
    @Override
    public Result<List<HouseListItemVO>> list(HouseQueryDto houseQueryDto) {
        return null;
    }

    /**
     * 房屋状态修改
     *
     * @param house 房屋信息
     * @return Result<String>
     */
    @Override
    public Result<String> update(House house) {
        return null;
    }

    /**
     * 房屋状态新增
     *
     * @param house 房屋信息
     * @return Result<String>
     */
    @Override
    public Result<String> saveEntity(House house) {
        return null;
    }
}