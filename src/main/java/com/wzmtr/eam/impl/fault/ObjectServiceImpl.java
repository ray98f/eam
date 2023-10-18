package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.wzmtr.eam.dataobject.BomDO;
import com.wzmtr.eam.dto.req.fault.CarObjectReqDTO;
import com.wzmtr.eam.dto.req.fault.ObjectReqDTO;
import com.wzmtr.eam.dto.res.basic.LineResDTO;
import com.wzmtr.eam.dto.res.fault.ObjectResDTO;
import com.wzmtr.eam.dto.res.fault.car.CarObjResDTO;
import com.wzmtr.eam.dto.res.fault.car.CarTreeListObjResDTO;
import com.wzmtr.eam.mapper.bom.BomMapper;
import com.wzmtr.eam.mapper.equipment.EquipmentMapper;
import com.wzmtr.eam.mapper.fault.ObjectMapper;
import com.wzmtr.eam.service.fault.ObjectService;
import com.wzmtr.eam.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Author: Li.Wang
 * Date: 2023/8/16 14:47
 */
@Service
@Slf4j
public class ObjectServiceImpl implements ObjectService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BomMapper bomMapper;
    @Autowired
    private EquipmentMapper equipmentMapper;

    @Override
    public Page<ObjectResDTO> queryObject(ObjectReqDTO reqDTO) {
        return objectMapper.queryObject(reqDTO.of(), reqDTO.getObjectCode(), reqDTO.getMajorCode(), reqDTO.getObjectName(), reqDTO.getBrand(), reqDTO.getManufacture(), reqDTO.getEquipTypeCode(), reqDTO.getSystemCode(), reqDTO.getPosition1Code(), reqDTO.getManufactureDateStart(), reqDTO.getManufactureDateEnd(), reqDTO.getUseLineNo(), reqDTO.getUseSegNo());
    }

    @Override
    public CarObjResDTO getQuery(CarObjectReqDTO reqDTO) {
        String pathName = null;
        if (reqDTO.getLabel() != null) {
            StringBuilder rescvs = new StringBuilder();
            List<String> rescv = new ArrayList<>();
            List<String> res = queryNodes(reqDTO.getLabel(), rescv);
            Collections.reverse(res);
            for (String re : res) {
                rescvs.append(re);
            }
            String partNamePath = rescvs.toString();
            log.info(partNamePath);
            if (StringUtils.isNotEmpty(partNamePath)) {
                pathName = partNamePath.substring(0, partNamePath.length() - 1);
            }
        }
        return getCarObjResDTO(reqDTO, pathName);
    }

    @Override
    public List<CarTreeListObjResDTO> query(CarObjectReqDTO reqDTO) {
        // com.baosight.wzplat.dm.fm.service.ServiceDMFM0021Tree#query
        String node = reqDTO.getNodeCode();
        String type = reqDTO.getType();
        if (node.equals("0")) {
            // todo 接口暂时没有 先写死
            List<CarTreeListObjResDTO> list = Lists.newArrayList();
            list.add(CarTreeListObjResDTO.toS1ResDTO());
            list.add(CarTreeListObjResDTO.toS2ResDTO());
            return list;
        }
        // 这是原来的代码
        // if (node.equals("0")) {
        //     List<Map<String, String>> list = new ArrayList();
        //     info.set(EiConstant.serviceId, "S_ED_11");
        //     info.set("tableName", "tedcm01");
        //     info.set("valueColumnName", "ITEM_CODE");
        //     info.set("labelColumnName", "ITEM_CNAME");
        //     info.set("condition", "CODESET_CODE = 'line'");
        //     info.set("orderBy", "ITEM_CODE asc");
        //     EiInfo out = XServiceManager.call(info);
        //     List<HashMap> lineList = (List<HashMap>) out.get("list");
        //     for (int i = 0; i < lineList.size(); i++) {
        //         Map<String, String> map = lineList.get(i);
        //         map.put("parent", node);
        //         map.put("pId", node);
        //         map.put("text", (new StringBuilder()).append(map.get("value")).append(" ").append(map.get("label")).toString());
        //         map.put("label", map.get("value"));
        //         map.put("leaf", "0");
        //         map.put("type", "xl");
        //         map.put("path", node + "." + map.get("label"));
        //         map.put("nodeCode", map.get("label"));
        //         map.put("line", map.get("label"));
        //         list.add(map);
        //     }
        //     EiBlockMeta eiBlockMeta = new EiBlockMeta("result");
        //     eiBlockMeta.addMetas((new EEDM05()).eiMetadata);
        //     outInfo.addBlock(node).setBlockMeta(eiBlockMeta);
        //     outInfo.getBlock(node).addRows(list);
        // }
        switch (type) {
            case "xl":
                List<CarTreeListObjResDTO> res = bomMapper.queryForLine(node);
                List<CarTreeListObjResDTO> carList = Lists.newArrayList();
                // 过滤脏数据
                List<CarTreeListObjResDTO> filter = res.stream().filter(a -> a.getNodeCode().length() == 3).collect(Collectors.toList());
                for (CarTreeListObjResDTO dto : filter) {
                    String nodeCode = dto.getNodeCode().substring(1, 3);
                    dto.setText("E" + nodeCode + " " + nodeCode + "车辆");
                    dto.setNodeCode("E" + nodeCode);
                    dto.setLabel(UUID.randomUUID().toString());
                    carList.add(dto);
                }
                return carList;
            case "wz":
                return bomMapper.queryForCar(node, reqDTO.getLine());
            case "tz":
                return bomMapper.queryForCarEquip(node, reqDTO.getLine());
            case "cc":
                return bomMapper.queryForCarChild(node, reqDTO.getLine(), reqDTO.getCarEquipCode(), reqDTO.getCarEquipName());
        }
        return null;
    }

    private CarObjResDTO getCarObjResDTO(CarObjectReqDTO reqDTO, String pathName) {
        CarObjResDTO carObjResDTO = new CarObjResDTO();
        carObjResDTO.setPathName(pathName);
        if (reqDTO.getType() != null && reqDTO.getType().trim() != "") {
            if (reqDTO.getType().equals("xl")) {
                carObjResDTO.setUseLineName(reqDTO.getText());
                carObjResDTO.setUseLineNo(reqDTO.getNodeCode());
                carObjResDTO.setPosition1Name("");
                carObjResDTO.setPosition1Code("");
                carObjResDTO.setMajorName("");
                carObjResDTO.setMajorCode("");
                carObjResDTO.setSystemName("");
                carObjResDTO.setSystemCode("");
                carObjResDTO.setEquipTypeName("");
                carObjResDTO.setEquipTypeCode("");
            } else if (reqDTO.getType().equals("wz") && !reqDTO.getLabel().equals("0")) {
                LineResDTO lineResDTOS;
                if (!reqDTO.getText().contains("ES")) {
                    lineResDTOS = equipmentMapper.queryLine(reqDTO.getLabel());
                } else {
                    lineResDTOS = equipmentMapper.queryCarLine(reqDTO.getLine());
                }
                carObjResDTO.setUseLineName(lineResDTOS.getLineName());
                carObjResDTO.setUseLineNo(lineResDTOS.getLineCode());
                carObjResDTO.setPosition1Name(reqDTO.getText());
                carObjResDTO.setPosition1Code(reqDTO.getNodeCode());
                carObjResDTO.setMajorName("");
                carObjResDTO.setMajorCode("");
                carObjResDTO.setSystemName("");
                carObjResDTO.setSystemCode("");
                carObjResDTO.setEquipTypeName("");
                carObjResDTO.setEquipTypeCode("");
            }
        }
        return carObjResDTO;
    }

    @Override
    public Page<ObjectResDTO> queryForObject(ObjectReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        String position1Code = (reqDTO.getPosition1Code() == null) ? "" : reqDTO.getPosition1Code();
        String car = reqDTO.getCar() == null ? "" : reqDTO.getCar();
        if (position1Code.contains("ES") && car.trim().isEmpty()) {
            return bomMapper.queryCarEquip(reqDTO.of(), reqDTO);
        } else if (car.equals("car")) {
            List<String> carChild = bomMapper.queryCarTree(reqDTO.getCarNode());
            if (CollectionUtil.isNotEmpty(carChild)) {
                return bomMapper.queryCarChild(reqDTO.of(), reqDTO);
            } else {
                return bomMapper.queryCarLastChild(reqDTO.of(), reqDTO);
            }
        } else if (car.equals("carpos")) {
            return bomMapper.queryCar(reqDTO.of(), reqDTO);
        }
        return null;
    }

    private List<String> queryNodes(String label, List<String> rescv) {
        BomDO bomDO = bomMapper.selectById(label);
        if (bomDO != null) {
            String parentId = bomDO.getParentId();
            String cname = bomDO.getCname();
            if (parentId != null && !parentId.equals("1") && !parentId.equals("root")) {
                rescv.add(cname + "-");
                queryNodes(parentId, rescv);
            }
        }
        return rescv;
    }
}
