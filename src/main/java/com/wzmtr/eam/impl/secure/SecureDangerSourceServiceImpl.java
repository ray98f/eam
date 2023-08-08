package com.wzmtr.eam.impl.secure;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.secure.SecureDangerSourceAddReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureDangerSourceDetailReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureDangerSourceListReqDTO;
import com.wzmtr.eam.dto.res.secure.SecureDangerSourceResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.secure.SecureDangerSourceMapper;
import com.wzmtr.eam.service.secure.SecureDangerSourceService;
import com.wzmtr.eam.utils.ExcelPortUtil;
import com.wzmtr.eam.utils.TokenUtil;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Author: Li.Wang
 * Date: 2023/8/1 10:37
 */
@Service
@Slf4j
public class SecureDangerSourceServiceImpl implements SecureDangerSourceService {

    @Autowired
    SecureDangerSourceMapper secureDangerSourceMapper;
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public Page<SecureDangerSourceResDTO> dangerSourceList(SecureDangerSourceListReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<SecureDangerSourceResDTO> query = secureDangerSourceMapper.query(reqDTO.of(), reqDTO.getDangerRiskId(), reqDTO.getDiscDate());
        List<SecureDangerSourceResDTO> records = query.getRecords();
        if (CollectionUtil.isNotEmpty(records)){
            records.forEach(a->{
                a.setRecDeptName(organizationMapper.getExtraOrgByAreaId(a.getRecDept()));
                a.setRespDeptName(organizationMapper.getExtraOrgByAreaId(a.getRespDeptCode()));
            });
        }
        return query;
    }

    @Override
    public void export(String dangerRiskId, String discDate, HttpServletResponse response) {
        List<String> listName = Arrays.asList("危险源单号", "危险源", "危险源级别", "危险源内容", "发现部门", "后果", "地点", "防范措施", "责任部门", "责任人");
        List<SecureDangerSourceResDTO> resList = secureDangerSourceMapper.list(dangerRiskId, discDate);
        List<Map<String, String>> list = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(resList)) {
            for (SecureDangerSourceResDTO resDTO : resList) {
                Map<String, String> map = new HashMap<>();
                map.put("危险源单号", resDTO.getDangerRiskId());
                map.put("危险源", resDTO.getDangerRisk());
                map.put("危险源级别", resDTO.getDangerRiskRank());
                map.put("发现部门", organizationMapper.getExtraOrgByAreaId(resDTO.getRecDept()));
                map.put("后果", resDTO.getConsequense());
                map.put("地点", resDTO.getPositionDesc());
                map.put("防范措施", resDTO.getControlDetail());
                map.put("责任部门", organizationMapper.getExtraOrgByAreaId(resDTO.getRespDeptCode()));
                map.put("责任人", resDTO.getRespCode());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("危险源排查信息", listName, list, null, response);
    }

    @Override
    public SecureDangerSourceResDTO detail(SecureDangerSourceDetailReqDTO reqDTO) {
        if (StrUtil.isEmpty(reqDTO.getDangerRiskId())) {
            log.warn("dangerRiskId is null!");
            return null;
        }
        SecureDangerSourceResDTO detail = secureDangerSourceMapper.detail(reqDTO.getDangerRiskId());
        detail.setRecDeptName(organizationMapper.getExtraOrgByAreaId(detail.getRecDept()));
        detail.setRespDeptName(organizationMapper.getExtraOrgByAreaId(detail.getRespDeptCode()));
        return detail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SecureDangerSourceAddReqDTO reqDTO) {
        reqDTO.setRecId(TokenUtil.getUuId());
        reqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        // TODO
        // String dangerRiskId = IMakeSeq.getSeqFromTable("WX", DateUtils.curDateStr("yyyy"), "WBPLAT.SEQ_TDMSM23");
        reqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        secureDangerSourceMapper.add(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(BaseIdsEntity reqDTO) {
        if (CollectionUtil.isEmpty(reqDTO.getIds())) {
            return;
        }
        secureDangerSourceMapper.deleteByIds(reqDTO.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SecureDangerSourceAddReqDTO reqDTO) {
        if (StrUtil.isEmpty(reqDTO.getDangerRiskId())) {
            log.warn("危险源记录单号为空!");
            return;
        }
        reqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        reqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        secureDangerSourceMapper.update(reqDTO);
    }
    // public void upload(){
    //     minioClient.putObject()
    // }
}
