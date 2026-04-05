package com.kmbeast.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmbeast.context.LocalThreadHolder;
import com.kmbeast.mapper.HouseOrderInfoMapper;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.HouseOrderInfoQueryDto;
import com.kmbeast.pojo.em.HouseOrderStatusEnum;
import com.kmbeast.pojo.entity.HouseOrderInfo;
import com.kmbeast.pojo.vo.HouseOrderInfoVO;
import com.kmbeast.service.HouseOrderInfoService;
import com.kmbeast.utils.AssertUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 预约看房业务逻辑接口实现类
 */
@Service
public class HouseOrderInfoServiceImpl extends ServiceImpl<HouseOrderInfoMapper, HouseOrderInfo> implements HouseOrderInfoService {


    /**
     * 查询预约看房列表
     *
     * @param houseOrderInfoQueryDto 查询条件
     * @return Result<List < HouseOrderInfoVO>>
     */
    @Override
    public Result<List<HouseOrderInfoVO>> list(HouseOrderInfoQueryDto houseOrderInfoQueryDto) {
        List<HouseOrderInfoVO> houseNewsListVOList = this.baseMapper.list(houseOrderInfoQueryDto);
        Integer count = this.baseMapper.listCount(houseOrderInfoQueryDto);
        return ApiResult.success(houseNewsListVOList, count);
    }

    /**
     * 预约看房新增
     *
     * @param houseOrderInfo 实体
     * @return Result<String>
     */
    @Override
    public Result<String> saveEntity(HouseOrderInfo houseOrderInfo) {
        judge(houseOrderInfo);
        houseOrderInfo.setCreateTime(LocalDateTime.now()); // 设置时间
        houseOrderInfo.setUserId(LocalThreadHolder.getUserId()); // 设置操作者用户ID
        houseOrderInfo.setOrderStatus(HouseOrderStatusEnum.STATUS_1.getType());// 初始状态是预约中
        save(houseOrderInfo);
        return ApiResult.success();
    }

    @Override
    public Result<String> updateEntity(HouseOrderInfo houseOrderInfo) {
        judge(houseOrderInfo);
        updateById(houseOrderInfo);
        return ApiResult.success();
    }

    private void judge(HouseOrderInfo houseOrderInfo) {
        AssertUtils.notNull(houseOrderInfo, "实体数据不能为空");
        AssertUtils.notNull(houseOrderInfo.getHouseId(), "房源ID不能为空");
        AssertUtils.hasText(houseOrderInfo.getOrderDate(), "必须选择日期");
        AssertUtils.hasText(houseOrderInfo.getOrderTimeSplit(), "时间段不能为空");
    }


}