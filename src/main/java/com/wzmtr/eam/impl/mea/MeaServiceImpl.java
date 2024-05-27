package com.wzmtr.eam.impl.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.dto.req.mea.MeaListReqDTO;
import com.wzmtr.eam.dto.req.mea.MeaReqDTO;
import com.wzmtr.eam.dto.req.mea.SubmissionRecordDetailReqDTO;
import com.wzmtr.eam.dto.req.mea.excel.ExcelMeaReqDTO;
import com.wzmtr.eam.dto.res.mea.MeaResDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionRecordDetailResDTO;
import com.wzmtr.eam.dto.res.mea.excel.ExcelMeaResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.mapper.mea.MeaMapper;
import com.wzmtr.eam.service.mea.MeaService;
import com.wzmtr.eam.service.mea.SubmissionRecordService;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author frp
 */
@Service
@Slf4j
public class MeaServiceImpl implements MeaService {

    @Autowired
    private MeaMapper meaMapper;

    @Autowired
    private SubmissionRecordService submissionRecordService;

    @Override
    public Page<MeaResDTO> pageMea(MeaListReqDTO meaListReqDTO, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return meaMapper.pageMea(pageReqDTO.of(), meaListReqDTO);
    }

    @Override
    public MeaResDTO getMeaDetail(String id) {
        return meaMapper.getMeaDetail(id, null);
    }

    @Override
    public void importMea(MultipartFile file) {
        List<ExcelMeaReqDTO> list = EasyExcelUtils.read(file, ExcelMeaReqDTO.class);
        List<MeaReqDTO> temp = new ArrayList<>();
        if (!Objects.isNull(list) && !list.isEmpty()) {
            for (ExcelMeaReqDTO reqDTO : list) {
                MeaReqDTO req = new MeaReqDTO();
                BeanUtils.copyProperties(reqDTO, req);
                req.setVerifyPeriod(Integer.valueOf(reqDTO.getVerifyPeriod()));
                req.setRecId(TokenUtils.getUuId());
                req.setRecCreator(TokenUtils.getCurrentPersonId());
                req.setRecCreateTime(DateUtils.getCurrentTime());
                temp.add(req);
                addSubmissionRecordDetail(req);
            }
        }
        if (!temp.isEmpty()) {
            meaMapper.importMea(temp);
        }
    }

    @Override
    public void addMea(MeaReqDTO meaReqDTO) {
        meaReqDTO.setRecId(TokenUtils.getUuId());
        meaReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        meaReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        meaMapper.addMea(meaReqDTO);
        addSubmissionRecordDetail(meaReqDTO);
    }

    /**
     * 新增计量器具检修记录
     * @param meaReqDTO 计量器具参数
     */
    private void addSubmissionRecordDetail(MeaReqDTO meaReqDTO) {
        SubmissionRecordDetailReqDTO req = new SubmissionRecordDetailReqDTO();
        BeanUtils.copyProperties(meaReqDTO, req);
        req.setVerifyPeriod(String.valueOf(meaReqDTO.getVerifyPeriod()));
        req.setNextVerifyDate(DateUtils.addMonthDay(req.getLastVerifyDate(), Integer.parseInt(req.getVerifyPeriod())));
        req.setUseDeptCname(meaReqDTO.getUseDeptCode());
        req.setVerificationNo(meaReqDTO.getCertificateNo());
        req.setVerificationType(meaReqDTO.getCertificateType());
        submissionRecordService.addSubmissionRecordDetail(req);
    }

    @Override
    public void exportMea(List<String> ids, HttpServletResponse response) throws IOException {
        List<MeaResDTO> meaList = meaMapper.listMea(ids);
        if (meaList != null && !meaList.isEmpty()) {
            List<ExcelMeaResDTO> list = new ArrayList<>();
            for (MeaResDTO resDTO : meaList) {
                ExcelMeaResDTO res = new ExcelMeaResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setVerifyPeriod(String.valueOf(resDTO.getVerifyPeriod()));
                res.setLineNo(resDTO.getLineNo());
                list.add(res);
            }
            EasyExcelUtils.export(response, "计量器具台账信息", list);
        }
    }

    @Override
    public Page<SubmissionRecordDetailResDTO> pageMeaRecord(String equipCode, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return meaMapper.pageMeaRecord(pageReqDTO.of(), equipCode);
    }

}
