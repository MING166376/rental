package com.kmbeast.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.HouseQueryDto;
import com.kmbeast.pojo.entity.House;
import com.kmbeast.pojo.vo.HouseListItemVO;

import java.util.List;

/**
 * 房屋业务逻辑接口
 */
public interface HouseService extends IService<House> {

    Result<List<HouseListItemVO>> list(HouseQueryDto houseQueryDto);

    Result<String> update(House house);

    Result<String> saveEntity(House house);

}