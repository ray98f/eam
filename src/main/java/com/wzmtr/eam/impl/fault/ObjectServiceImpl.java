package com.wzmtr.eam.impl.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dataobject.BomDO;
import com.wzmtr.eam.dto.req.fault.CarObjectReqDTO;
import com.wzmtr.eam.dto.req.fault.ObjectReqDTO;
import com.wzmtr.eam.dto.res.basic.LineResDTO;
import com.wzmtr.eam.dto.res.fault.ObjectResDTO;
import com.wzmtr.eam.dto.res.fault.car.CarObjResDTO;
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
    public CarObjResDTO query(CarObjectReqDTO reqDTO) {
        // todo
        // String node = inInfo.getCellStr(EiConstant.queryBlock, 0, "node");
        // String type = (String) inInfo.get("type");
        // EiInfo outInfo = new EiInfo();
        // if (node.equals("0")) {
        //     List<Map<String, String>> list = new ArrayList();
        //     EiInfo info = new EiInfo();
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
        // } else if (type.equals("xl")) {
        //     HashMap<Object, Object> params = new HashMap<>();
        //     params.put("lineCode", node);
        //     List<Map<String, String>> list = this.dao.query("DMDM01.queryForLine", params, 0, -999999);
        //     List<Map<Object, Object>> carList = new ArrayList();
        //     Map<String, String> map = list.get(0);
        //     Map<Object, Object> carMap = new HashMap<>();
        //     String nodeCode = ((String) map.get("nodeCode")).substring(1, 3);
        //     for (String key : map.keySet()) {
        //         if (key.equals("text")) {
        //             carMap.put("text", "E" + nodeCode + " " + nodeCode + "车辆");
        //             continue;
        //         }
        //         if (key.equals("nodeCode")) {
        //             carMap.put("nodeCode", "E" + nodeCode);
        //             continue;
        //         }
        //         if (key.equals("label")) {
        //             carMap.put("label", UUID.randomUUID().toString());
        //             continue;
        //         }
        //         carMap.put(key, map.get(key));
        //     }
        //     carList.add(carMap);
        //     EiBlockMeta eiBlockMeta = new EiBlockMeta("result");
        //     eiBlockMeta.addMetas((new EEDM05()).eiMetadata);
        //     outInfo.addBlock(node).setBlockMeta(eiBlockMeta);
        //     outInfo.getBlock(node).addRows(carList);
        // } else if (type.equals("wz")) {
        //     HashMap<Object, Object> params = new HashMap<>();
        //     params.put("realNode", node);
        //     params.put("useLineNo", inInfo.get("line"));
        //     List list = this.dao.query("DMDM01.queryForCar", params, 0, -999999);
        //     EiBlockMeta eiBlockMeta = new EiBlockMeta("result");
        //     eiBlockMeta.addMetas((new EEDM05()).eiMetadata);
        //     outInfo.addBlock(node).setBlockMeta(eiBlockMeta);
        //     outInfo.getBlock(node).addRows(list);
        // } else if (type.equals("tz")) {
        //     Map<Object, Object> map = new HashMap<>();
        //     map.put("realNode", node);
        //     map.put("useLineNo", inInfo.get("line"));
        //     List list = this.dao.query("DMDM01.queryForCarEquip", map, 0, -999999);
        //     EiBlockMeta eiBlockMeta = new EiBlockMeta("result");
        //     eiBlockMeta.addMetas((new EEDM05()).eiMetadata);
        //     outInfo.addBlock(node).setBlockMeta(eiBlockMeta);
        //     outInfo.getBlock(node).addRows(list);
        // } else if (type.equals("cc")) {
        //     Map<Object, Object> map = new HashMap<>();
        //     map.put("node", node);
        //     map.put("line", inInfo.get("line"));
        //     map.put("carEquipCode", inInfo.get("carEquipCode"));
        //     map.put("carEquipName", inInfo.get("carEquipName"));
        //     List child = this.dao.query("DMDM04.queryForCarChild", map, 0, -999999);
        //     EiBlockMeta eiBlockMeta = new EiBlockMeta("result");
        //     eiBlockMeta.addMetas((new EEDM05()).eiMetadata);
        //     outInfo.addBlock(node).setBlockMeta(eiBlockMeta);
        //     outInfo.getBlock(node).addRows(child);
        // }
        // outInfo.setMsg("");
        // return outInfo;
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
