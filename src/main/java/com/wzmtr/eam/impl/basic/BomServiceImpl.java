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
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
            for (ExcelBomReqDTO reqDTO : list) {
                BomReqDTO req = new BomReqDTO();
                BeanUtils.copyProperties(reqDTO, req);
                temp.add(req);
            }
            if (!temp.isEmpty()) {
//                bomMapper.importBom(temp);
            }
        } catch (Exception e) {
            throw new CommonException(ErrorCode.IMPORT_ERROR);
        }
    }

}
