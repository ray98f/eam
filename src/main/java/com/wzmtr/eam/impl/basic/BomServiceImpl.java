package com.wzmtr.eam.impl.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.basic.BomReqDTO;
import com.wzmtr.eam.dto.req.basic.Excel.ExcelBomReqDTO;
import com.wzmtr.eam.dto.res.basic.BomResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.bom.BomMapper;
import com.wzmtr.eam.service.basic.BomService;
import com.wzmtr.eam.utils.CodeUtils;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
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
        bomReqDTO.setRecId(TokenUtil.getUuId());
        bomReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        bomReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        bomMapper.addBom(bomReqDTO);
    }

    @Override
    public void modifyBom(BomReqDTO bomReqDTO) {
        Integer result = bomMapper.selectBomIsExist(bomReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        bomReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        bomReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        bomMapper.modifyBom(bomReqDTO);
    }

    @Override
    public void deleteBom(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            bomMapper.deleteBom(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void importBom(MultipartFile file) {
        try {
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
                    if (StringUtils.isEmpty(firstSysBomCode)) {
                        firstSysBomCode = "B00";
                    }
                    firstSysBomCode = CodeUtils.getNextCode(firstSysBomCode, 1);
                    firstSysBom.setEname(firstSysBomCode);
                    firstSysBom.setCname(reqDTO.getFirstSysName());
                    firstSysBom.setParentId("1");
                    firstSysBom.setRecCreator(TokenUtil.getCurrentPersonId());
                    firstSysBom.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                    temp.add(firstSysBom);
                }
                if (StringUtils.isNotEmpty(reqDTO.getFirstComName())) {
                    firstComBom = new BomReqDTO();
                    secondSysBomCode = "";
                    secondComBomCode = "";
                    thirdComBomCode = "";
                    if (StringUtils.isEmpty(firstComBomCode)) {
                        firstComBomCode = firstSysBomCode + "00";
                    }
                    firstComBomCode = CodeUtils.getNextCode(firstComBomCode, 3);
                    firstComBom.setEname(firstComBomCode);
                    firstComBom.setCname(reqDTO.getFirstComName());
                    firstComBom.setParentId(firstSysBom.getEname());
                    firstComBom.setRecCreator(TokenUtil.getCurrentPersonId());
                    firstComBom.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                    temp.add(firstComBom);
                }
                if (StringUtils.isNotEmpty(reqDTO.getSecondSysName())) {
                    secondSysBom = new BomReqDTO();
                    secondComBomCode = "";
                    thirdComBomCode = "";
                    if (StringUtils.isEmpty(secondSysBomCode)) {
                        secondSysBomCode = firstComBomCode + "00";
                    }
                    secondSysBomCode = CodeUtils.getNextCode(secondSysBomCode, 5);
                    secondSysBom.setEname(secondSysBomCode);
                    secondSysBom.setCname(reqDTO.getSecondSysName());
                    secondSysBom.setParentId(firstComBom.getEname());
                    secondSysBom.setRecCreator(TokenUtil.getCurrentPersonId());
                    secondSysBom.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                    temp.add(secondSysBom);
                }
                if (StringUtils.isNotEmpty(reqDTO.getSecondComName())) {
                    secondComBom = new BomReqDTO();
                    thirdComBomCode = "";
                    if (StringUtils.isEmpty(secondComBomCode)) {
                        secondComBomCode = secondSysBomCode + "00";
                    }
                    secondComBomCode = CodeUtils.getNextCode(secondComBomCode, 7);
                    secondComBom.setEname(secondComBomCode);
                    secondComBom.setCname(reqDTO.getSecondComName());
                    secondComBom.setParentId(secondSysBom.getEname());
                    secondComBom.setRecCreator(TokenUtil.getCurrentPersonId());
                    secondComBom.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                    temp.add(secondComBom);
                }
                if (StringUtils.isNotEmpty(reqDTO.getThirdComName())) {
                    thirdComBom = new BomReqDTO();
                    if (StringUtils.isEmpty(thirdComBomCode)) {
                        thirdComBomCode = secondComBomCode + "00";
                    }
                    thirdComBomCode = CodeUtils.getNextCode(thirdComBomCode, 9);
                    thirdComBom.setEname(thirdComBomCode);
                    thirdComBom.setCname(reqDTO.getThirdComName());
                    thirdComBom.setParentId(secondComBom.getEname());
                    thirdComBom.setRecCreator(TokenUtil.getCurrentPersonId());
                    thirdComBom.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                    temp.add(thirdComBom);
                }
            }
            if (!temp.isEmpty()) {
                bomMapper.importBom(temp);
            }
        } catch (Exception e) {
            throw new CommonException(ErrorCode.IMPORT_ERROR);
        }
    }

}
