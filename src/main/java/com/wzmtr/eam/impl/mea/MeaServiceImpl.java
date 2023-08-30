package com.wzmtr.eam.impl.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.*;
import com.wzmtr.eam.dto.res.*;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.mapper.mea.MeaMapper;
import com.wzmtr.eam.service.mea.MeaService;
import com.wzmtr.eam.utils.ExcelPortUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author frp
 */
@Service
@Slf4j
public class MeaServiceImpl implements MeaService {

    @Autowired
    private MeaMapper meaMapper;

    @Override
    public Page<MeaResDTO> pageMea(MeaListReqDTO meaListReqDTO, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return meaMapper.pageMea(pageReqDTO.of(), meaListReqDTO);
    }

    @Override
    public MeaResDTO getMeaDetail(String id) {
        return meaMapper.getMeaDetail(id);
    }

    @Override
    public void importMea(MultipartFile file) {
        // todo
    }

    @Override
    public void exportMea(MeaListReqDTO meaListReqDTO, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "计量器具编码", "计量器具名称", "型号规格", "证书编号", "移交日期", "制造单位",
                "条件代码", "检定/校准周期（月）", "使用公司", "使用单位", "开始使用日期", "上次检定/校准日期", "证书有效日期", "出厂编号",
                "使用保管人手机号", "使用保管人姓名", "线别");
        List<MeaResDTO> meaList = meaMapper.listMea(meaListReqDTO);
        List<Map<String, String>> list = new ArrayList<>();
        if (meaList != null && !meaList.isEmpty()) {
            for (MeaResDTO resDTO : meaList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("计量器具编码", resDTO.getEquipCode());
                map.put("计量器具名称", resDTO.getEquipName());
                map.put("型号规格", resDTO.getMatSpecifi());
                map.put("证书编号", resDTO.getCertificateNo());
                map.put("移交日期", resDTO.getTransferDate());
                map.put("制造单位", resDTO.getManufacture());
                map.put("条件代码", resDTO.getSource());
                map.put("检定/校准周期（月）", String.valueOf(resDTO.getVerifyPeriod()));
                map.put("使用公司", resDTO.getUseDeptCode());
                map.put("使用单位", resDTO.getUseDeptCname());
                map.put("开始使用日期", resDTO.getUseBeginDate());
                map.put("上次检定/校准日期", resDTO.getLastVerifyDate());
                map.put("证书有效日期", resDTO.getExpirationDate());
                map.put("出厂编号", resDTO.getManufactureNo());
                map.put("使用保管人手机号", resDTO.getPhoneNo());
                map.put("使用保管人姓名", resDTO.getUseName());
                map.put("线别", resDTO.getLineNo());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("计量器具台账信息", listName, list, null, response);
    }

    @Override
    public Page<SubmissionRecordDetailResDTO> pageMeaRecord(String equipCode, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return meaMapper.pageMeaRecord(pageReqDTO.of(), equipCode);
    }

}
