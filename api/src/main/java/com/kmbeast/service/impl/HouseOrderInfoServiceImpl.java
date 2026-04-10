package com.kmbeast.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmbeast.context.LocalThreadHolder;
import com.kmbeast.mapper.HouseOrderInfoMapper;
import com.kmbeast.mapper.HouseOrderStatusMapper;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.HouseOrderInfoQueryDto;
import com.kmbeast.pojo.em.DateTimeSplitEnum;
import com.kmbeast.pojo.em.HouseOrderStatusEnum;
import com.kmbeast.pojo.entity.HouseOrderInfo;
import com.kmbeast.pojo.entity.HouseOrderStatus;
import com.kmbeast.pojo.vo.HouseOrderInfoVO;
import com.kmbeast.service.HouseOrderInfoService;
import com.kmbeast.utils.AssertUtils;
import com.kmbeast.utils.DateFormatUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 预约看房业务逻辑接口实现类
 */
@Service
public class HouseOrderInfoServiceImpl extends ServiceImpl<HouseOrderInfoMapper, HouseOrderInfo> implements HouseOrderInfoService {

    @Resource
    private HouseOrderStatusMapper houseOrderStatusMapper;

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
        // 参数校验
        judge(houseOrderInfo);
        // 如果对应的时间段超过了当前时间，不能预约
        validDateSplit(houseOrderInfo);
        // 如果这个时间段已经被别人预约，并且预约状态不处于已完成或者已取消状态，不能预约
        validStatus(houseOrderInfo);
        // 设置初始默认值
        defaultCreate(houseOrderInfo);
        // 预约记录入库
        save(houseOrderInfo);
        return ApiResult.success();
    }

    /**
     * 设置初始默认值 - 创建时间、操作者、初始状态预约中
     */
    private void defaultCreate(HouseOrderInfo houseOrderInfo) {
        houseOrderInfo.setCreateTime(LocalDateTime.now()); // 设置时间
        houseOrderInfo.setUserId(LocalThreadHolder.getUserId()); // 设置操作者用户ID
        houseOrderInfo.setOrderStatus(HouseOrderStatusEnum.STATUS_1.getType());// 初始状态是预约中
    }

    /**
     * 判断当前预约时间段是否可用
     */
    private void validDateSplit(HouseOrderInfo houseOrderInfo) {
        LocalDate localDate = DateFormatUtil.parseChineseDate(houseOrderInfo.getOrderDate());
        boolean timeSlotValid = DateTimeSplitEnum.isTimeSlotValid(houseOrderInfo.getOrderTimeSplit(), localDate);
        AssertUtils.isTrue(timeSlotValid, "当前时间段不能预约");
    }

    /**
     * 判断当前预约时间段时候存在竞争关系
     */
    private void validStatus(HouseOrderInfo houseOrderInfo) {
        HouseOrderInfoQueryDto houseOrderInfoQueryDto = new HouseOrderInfoQueryDto();
        houseOrderInfoQueryDto.setHouseId(houseOrderInfo.getHouseId());
        houseOrderInfoQueryDto.setOrderDate(houseOrderInfo.getOrderDate());
        houseOrderInfoQueryDto.setOrderTimeSplit(houseOrderInfo.getOrderTimeSplit());
        List<HouseOrderInfoVO> houseOrderInfoVOS = this.baseMapper.list(houseOrderInfoQueryDto);
        for (HouseOrderInfoVO houseOrderInfoVO : houseOrderInfoVOS) {
            AssertUtils.isFalse(
                    Objects.equals(HouseOrderStatusEnum.STATUS_1.getType(), houseOrderInfoVO.getOrderStatus()) ||
                            Objects.equals(houseOrderInfoVO.getOrderStatus(), HouseOrderStatusEnum.STATUS_2.getType()),
                    "当前时间段存在其他预约信息，请稍候再试");
        }
    }

    @Override
    public Result<String> updateEntity(HouseOrderInfo houseOrderInfo) {
        judge(houseOrderInfo);
        // 状态流转处理
        statusFlow(houseOrderInfo);
        updateById(houseOrderInfo);
        return ApiResult.success();
    }

    /**
     * 状态流转过程
     *
     * @param houseOrderInfo 预约看房实体
     */
    private void statusFlow(HouseOrderInfo houseOrderInfo){
        // 最终：记录状态流转
        // 关键点：原始状态是啥？最终状态是啥？什么情况下需要记录？
        HouseOrderInfo orderInfo = getById(houseOrderInfo.getId());
        AssertUtils.notNull(orderInfo, "订单查询异常");
        AssertUtils.notNull(orderInfo.getOrderStatus(), "订单原始状态缺失");
        Integer originStatus = orderInfo.getOrderStatus(); //原始状态
        //如果传进来的状态，跟数据库存储的原始状态不一样，证明状态发生了流转此时要记录流转的变化路径
        if(!Objects.equals(originStatus, houseOrderInfo.getOrderStatus())){
            HouseOrderStatus houseOrderStatus = new HouseOrderStatus();
            houseOrderStatus.setOriginStatus(originStatus);
            houseOrderStatus.setNewId(houseOrderInfo.getOrderStatus());
            houseOrderStatus.setCreateTime(LocalDateTime.now()); //流转时间
            houseOrderStatus.setHouseOrderInfoId(houseOrderInfo.getId());
            houseOrderStatusMapper.insert(houseOrderStatus); //状态流转入库

        }
    }

    private void judge(HouseOrderInfo houseOrderInfo) {
        AssertUtils.notNull(houseOrderInfo, "实体数据不能为空");
        AssertUtils.notNull(houseOrderInfo.getHouseId(), "房源ID不能为空");
        AssertUtils.hasText(houseOrderInfo.getOrderDate(), "必须选择日期");
        AssertUtils.hasText(houseOrderInfo.getOrderTimeSplit(), "时间段不能为空");
    }


}