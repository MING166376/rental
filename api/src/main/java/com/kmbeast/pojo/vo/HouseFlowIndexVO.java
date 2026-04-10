package com.kmbeast.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 房屋流量列表VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseFlowIndexVO {
    /**
     * 房源ID
     */
    private Integer id;
    /**
     * 房源名
     */
    private String houseName;
    /**
     * 房源图
     */
    private String houseCover;
    /**
     * 展现量
     */
    private Integer showNumber;
    /**
     * 阅读量
     */
    private Integer viewNumber;
    /**
     * 点击率
     */
    private Double clickRate;
    /**
     * 收藏量
     */
    private Integer saveNumber;
    /**
     * 评论量
     */
    private Integer evaluationsNumber;
}