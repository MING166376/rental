package com.kmbeast.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 房屋资讯信息查询条件类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HouseNewsQueryDto extends QueryDto{
    /**
     * 标题
     */
    private String title;
}