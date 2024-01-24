package com.wzmtr.eam.impl.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.mea.MeaListReqDTO;
import com.wzmtr.eam.dto.req.mea.MeaReqDTO;
import com.wzmtr.eam.dto.req.mea.excel.ExcelMeaReqDTO;
import com.wzmtr.eam.dto.res.mea.MeaResDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionRecordDetailResDTO;
import com.wzmtr.eam.dto.res.mea.excel.ExcelMeaResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.mea.MeaMapper;
import com.wzmtr.eam.service.mea.MeaService;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
        try {
            List<ExcelMeaReqDTO> list = EasyExcelUtils.read(file, ExcelMeaReqDTO.class);
            List<MeaReqDTO> temp = new ArrayList<>();
            if (!Objects.isNull(list) && !list.isEmpty()) {
                for (ExcelMeaReqDTO reqDTO : list) {
                    MeaReqDTO req = new MeaReqDTO();
                    BeanUtils.copyProperties(reqDTO, req);
                    req.setVerifyPeriod(Integer.valueOf(reqDTO.getVerifyPeriod()));
                    req.setRecId(TokenUtil.getUuId());
                    req.setRecCreator(TokenUtil.getCurrentPersonId());
                    req.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                    temp.add(req);
                }
            }
            if (!temp.isEmpty()) {
                meaMapper.importMea(temp);
            }
        } catch (Exception e) {
            throw new CommonException(ErrorCode.IMPORT_ERROR);
        }
    }

    @Override
    public void exportMea(MeaListReqDTO meaListReqDTO, HttpServletResponse response) throws IOException {
        List<MeaResDTO> meaList = meaMapper.listMea(meaListReqDTO);
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
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return meaMapper.pageMeaRecord(pageReqDTO.of(), equipCode);
    }

}
