package com.wzmtr.eam.impl.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.basic.BomReqDTO;
import com.wzmtr.eam.dto.req.basic.BomTrainReqDTO;
import com.wzmtr.eam.dto.req.basic.Excel.ExcelBomReqDTO;
import com.wzmtr.eam.dto.req.basic.Excel.ExcelBomTrainReqDTO;
import com.wzmtr.eam.dto.res.basic.BomResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.bom.BomMapper;
import com.wzmtr.eam.service.basic.BomService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author frp
 */
@Service
@Slf4j
public class BomServiceImpl implements BomService {

    @Autowired
    private BomMapper bomMapper;

    @Override
    public Page<BomResDTO> listBom(String parentId, String code, String name, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return bomMapper.pageBom(pageReqDTO.of(), parentId, code, name);
    }

    @Override
    public BomResDTO getBomDetail(String id) {
        return bomMapper.getBomDetail(id);
    }

    @Override
    public void addBom(BomReqDTO bomReqDTO) {
        Integer result = bomMapper.selectBomIsExist(bomReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        bomReqDTO.setRecId(TokenUtils.getUuId());
        bomReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        bomReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        if (Objects.isNull(bomReqDTO.getSortIndex())) {
            bomReqDTO.setSortIndex(new BigDecimal(0));
        }
        bomMapper.addBom(bomReqDTO);
    }

    @Override
    public void modifyBom(BomReqDTO bomReqDTO) {
        Integer result = bomMapper.selectBomIsExist(bomReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        bomReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        bomReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        if (Objects.isNull(bomReqDTO.getSortIndex())) {
            bomReqDTO.setSortIndex(new BigDecimal(0));
        }
        bomMapper.modifyBom(bomReqDTO);
    }

    @Override
    public void deleteBom(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            bomMapper.deleteBom(baseIdsEntity.getIds(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void importBom(MultipartFile file) {
        List<ExcelBomReqDTO> list = EasyExcelUtils.read(file, ExcelBomReqDTO.class);
        List<BomReqDTO> temp = new ArrayList<>();
        BomReqDTO firstSysBom = new BomReqDTO();
        BomReqDTO firstComBom = new BomReqDTO();
        BomReqDTO secondSysBom = new BomReqDTO();
        BomReqDTO secondComBom = new BomReqDTO();
        BomReqDTO thirdComBom;
        String firstSysBomCode = "", firstComBomCode = "", secondSysBomCode = "", secondComBomCode = "", thirdComBomCode = "";
        for (ExcelBomReqDTO reqDTO : list) {
            if (StringUtils.isNotEmpty(reqDTO.getFirstSysName())) {
                firstSysBom = new BomReqDTO();
                firstComBomCode = "";
                secondSysBomCode = "";
                secondComBomCode = "";
                thirdComBomCode = "";
                firstSysBomCode = getTreeCode(firstSysBomCode, null, 1);
                firstSysBom.setEname(firstSysBomCode);
                firstSysBom.setCname(reqDTO.getFirstSysName());
                firstSysBom.setParentId("1");
                temp.add(firstSysBom);
            }
            if (StringUtils.isNotEmpty(reqDTO.getFirstComName())) {
                firstComBom = new BomReqDTO();
                secondSysBomCode = "";
                secondComBomCode = "";
                thirdComBomCode = "";
                firstComBomCode = getTreeCode(firstComBomCode, firstSysBomCode, 2);
                firstComBom.setEname(firstComBomCode);
                firstComBom.setCname(reqDTO.getFirstComName());
                firstComBom.setParentId(firstSysBom.getEname());
                temp.add(firstComBom);
            }
            if (StringUtils.isNotEmpty(reqDTO.getSecondSysName())) {
                secondSysBom = new BomReqDTO();
                secondComBomCode = "";
                thirdComBomCode = "";
                secondSysBomCode = getTreeCode(secondSysBomCode, firstComBomCode, 3);
                secondSysBom.setEname(secondSysBomCode);
                secondSysBom.setCname(reqDTO.getSecondSysName());
                secondSysBom.setParentId(firstComBom.getEname());
                temp.add(secondSysBom);
            }
            if (StringUtils.isNotEmpty(reqDTO.getSecondComName())) {
                secondComBom = new BomReqDTO();
                thirdComBomCode = "";
                secondComBomCode = getTreeCode(secondComBomCode, secondSysBomCode, 4);
                secondComBom.setEname(secondComBomCode);
                secondComBom.setCname(reqDTO.getSecondComName());
                secondComBom.setParentId(secondSysBom.getEname());
                temp.add(secondComBom);
            }
            if (StringUtils.isNotEmpty(reqDTO.getThirdComName())) {
                thirdComBom = new BomReqDTO();
                thirdComBomCode = getTreeCode(thirdComBomCode, secondComBomCode, 5);
                thirdComBom.setEname(thirdComBomCode);
                thirdComBom.setCname(reqDTO.getThirdComName());
                thirdComBom.setParentId(secondComBom.getEname());
                temp.add(thirdComBom);
            }
        }
        if (!temp.isEmpty()) {
            bomMapper.importBom(temp, TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
        }
    }

    /**
     * 获取树状code
     *
     * @param nowCode  当前code
     * @param laseCode 上一层code
     * @param type     类型
     * @return code
     */
    private String getTreeCode(String nowCode, String laseCode, int type) {
        if (type == CommonConstants.ONE) {
            if (StringUtils.isEmpty(nowCode)) {
                nowCode = "B00";
            }
        } else {
            if (StringUtils.isEmpty(nowCode)) {
                nowCode = laseCode + "00";
            }
        }
        switch (type) {
            case CommonConstants.ONE:
                return CodeUtils.getNextCode(nowCode, 1);
            case CommonConstants.TWO:
                return CodeUtils.getNextCode(nowCode, 3);
            case CommonConstants.THREE:
                return CodeUtils.getNextCode(nowCode, 5);
            case CommonConstants.FOUR:
                return CodeUtils.getNextCode(nowCode, 7);
            default:
                return CodeUtils.getNextCode(nowCode, 9);
        }
    }

    @Override
    public void importBomTrain(MultipartFile file) {
        List<ExcelBomTrainReqDTO> list = EasyExcelUtils.read(file, ExcelBomTrainReqDTO.class);
        List<BomTrainReqDTO> temp = new ArrayList<>();
        String partCode = bomMapper.getMaxPartCode();
        if (StringUtils.isEmpty(partCode)) {
            partCode = "P0000000000";
        }
        for (ExcelBomTrainReqDTO reqDTO : list) {
            List<BomResDTO> boms = bomMapper.getChildBom(reqDTO.getBomParenName());
            if (StringUtils.isNotEmpty(boms)) {
                for (BomResDTO bom : boms) {
                    BomTrainReqDTO req = new BomTrainReqDTO();
                    req.setRecId(TokenUtils.getUuId());
                    req.setBomCode(bom.getEname());
                    req.setBomName(bom.getCname());
                    partCode = CodeUtils.getNextCodeByAddNum(partCode, 1, 1);
                    req.setPartCode(partCode);
                    req.setEquipCode(reqDTO.getEquipCode());
                    req.setEquipName(reqDTO.getEquipName());
                    temp.add(req);
                }
            }
        }
        if (!temp.isEmpty()) {
            int times = (int) Math.ceil(temp.size() / 1000.0);
            for (int j = 0; j < times; j++) {
                bomMapper.importBomTrain(temp.subList(j * 1000, Math.min((j + 1) * 1000, temp.size() - 1)),
                        TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
            }
        }
    }

}
