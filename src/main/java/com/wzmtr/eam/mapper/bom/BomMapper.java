package com.wzmtr.eam.mapper.bom;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dataobject.BomDO;
import com.wzmtr.eam.dto.req.fault.ObjectReqDTO;
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
    List<CarTreeListObjResDTO> queryForLine(String node);

    List<CarTreeListObjResDTO> queryForCar(String node, String line);

    List<CarTreeListObjResDTO> queryForCarEquip(String node, String line);

    List<CarTreeListObjResDTO> queryForCarChild(String node, String line, String carEquipCode, String carEquipName);

    Page<ObjectResDTO> queryCarEquip(Page<Object> of,@Param("req")  ObjectReqDTO reqDTO);

    List<String> queryCarTree(String carNode);

    Page<ObjectResDTO> queryCarChild(Page<Object> of, @Param("req") ObjectReqDTO reqDTO);

    Page<ObjectResDTO> queryCarLastChild(Page<Object> of,@Param("req")  ObjectReqDTO reqDTO);

    Page<ObjectResDTO> queryCar(Page<Object> of, @Param("req") ObjectReqDTO reqDTO);
}
