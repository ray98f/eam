package com.wzmtr.eam.mapper.home;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.res.home.HomeCountResDTO;
import com.wzmtr.eam.dto.res.home.ShowAResDTO;
import com.wzmtr.eam.dto.res.home.ShowBCResDTO;
import com.wzmtr.eam.entity.StatusWorkFlowLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/9/12 15:03
 */
@Mapper
public interface HomeMapper {

    /**
     * 获取首页工作台列表
     * @param page 分页信息
     * @param type 类型 1：待办 2：已办
     * @param userId 用户id
     * @return 工作列表
     */
    Page<StatusWorkFlowLog> todoList(Page<StatusWorkFlowLog> page, String type, String userId);

    /**
     * 返回工作台工作数量
     * @param userId 用户id
     * @return 工作数量
     */
    HomeCountResDTO todoCount(String userId);

    /**
     * 催办
     * @param todoId 代办id
     */
    void urgingTodo(String todoId);

    List<ShowBCResDTO> queryC();

    List<ShowBCResDTO> queryB();

    List<ShowAResDTO> queryA();
}
