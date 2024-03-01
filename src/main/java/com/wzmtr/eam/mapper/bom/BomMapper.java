package com.wzmtr.eam.mapper.bom;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dataobject.BomDO;
import com.wzmtr.eam.dto.req.basic.BomReqDTO;
import com.wzmtr.eam.dto.req.basic.BomTrainReqDTO;
import com.wzmtr.eam.dto.req.fault.ObjectReqDTO;
import com.wzmtr.eam.dto.res.basic.BomResDTO;
import com.wzmtr.eam.dto.res.fault.ObjectResDTO;
import com.wzmtr.eam.dto.res.fault.car.CarTreeListObjResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/10/17 15:38
 */
@Mapper
@Repository
public interface BomMapper extends BaseMapper<BomDO> {
    List<CarTreeListObjResDTO> queryForLine(@Param("lineCode") String node);

    List<CarTreeListObjResDTO> queryForCar(@Param("realNode") String node,@Param("useLineNo") String line);

    List<CarTreeListObjResDTO> queryForCarEquip(@Param("realNode") String node,@Param("useLineNo") String line);

    List<CarTreeListObjResDTO> queryForCarChild(@Param("node") String node,@Param("line") String line,@Param("carEquipCode") String carEquipCode,@Param("carEquipName") String carEquipName);

    Page<ObjectResDTO> queryCarEquip(Page<Object> of,@Param("req")ObjectReqDTO reqDTO);

    List<String> queryCarTree(@Param("carNode") String carNode);

    Page<ObjectResDTO> queryCarChild(Page<Object> of, @Param("req") ObjectReqDTO reqDTO);

    Page<ObjectResDTO> queryCarLastChild(Page<Object> of,@Param("req")  ObjectReqDTO reqDTO);

    Page<ObjectResDTO> queryCar(Page<Object> of, @Param("req") ObjectReqDTO reqDTO);

    /**
     * 分页获取Bom结构列表
     * @param page 分页参数
     * @param parentId 父级id
     * @param code 编码
     * @param name 名称
     * @return Bom结构列表
     */
    Page<BomResDTO> pageBom(Page<BomResDTO> page, String parentId, String code, String name);

    /**
     * 获取Bom结构详情
     * @param id id
     * @return Bom结构详情
     */
    BomResDTO getBomDetail(String id);

    /**
     * 查询Bom结构是否已存在
     * @param bomReqDTO bom结构数据
     * @return 是否已存在状态 0 否 1 是
     */
    Integer selectBomIsExist(BomReqDTO bomReqDTO);

    /**
     * 新增Bom结构
     * @param bomReqDTO bom结构数据
     */
    void addBom(BomReqDTO bomReqDTO);

    /**
     * 修改Bom结构
     * @param bomReqDTO bom结构数据
     */
    void modifyBom(BomReqDTO bomReqDTO);

    /**
     * 删除Bom结构
     * @param ids ids
     * @param userId 用户id
     * @param time 时间
     */
    void deleteBom(List<String> ids, String userId, String time);

    /**
     * 导入Bom结构
     * @param list Bom结构
     * @param userId 用户id
     * @param time 时间
     */
    void importBom(List<BomReqDTO> list, String userId, String time);

    /**
     * 获取子集Bom结构列表
     * @param name 名称
     * @return 子集Bom结构列表
     */
    List<BomResDTO> getChildBom(String name);

    /**
     * 导入车辆与Bom关联关系
     * @param list 车辆与Bom关联关系
     * @param userId 人员编号
     * @param startTime 时间
     */
    void importBomTrain(List<BomTrainReqDTO> list, String userId, String startTime);
}
