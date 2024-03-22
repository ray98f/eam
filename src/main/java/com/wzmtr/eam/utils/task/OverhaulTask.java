package com.wzmtr.eam.utils.task;

import com.alibaba.fastjson.JSONObject;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.overhaul.OverhaulPlanReqDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulPlanResDTO;
import com.wzmtr.eam.entity.CurrentLoginUser;
import com.wzmtr.eam.mapper.overhaul.OverhaulPlanMapper;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 定时任务-检修计划
 * @author  Ray
 * @version 1.0
 * @date 2024/03/21
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OverhaulTask {

    @Value("${local.base-url}")
    private String baseUrl;

    @Autowired
    private OverhaulPlanMapper overhaulPlanMapper;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 检修计划自动触发
     * 因原先检修计划触发接口中需要用到登录用户的id，所以此处采用调用接口的方式进行自动触发操作
     */
    @Scheduled(cron = "0 30 1 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void trigger() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
        // 获取需要自动触发的检修计划并去重
        List<OverhaulPlanResDTO> plans = overhaulPlanMapper.getTriggerOverhaulPlan(sdf.format(new Date()));
        plans = plans.stream().distinct().collect(Collectors.toList());
        // 获取默认系统管理员token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/json;UTF-8"));
        headers.add("Authorization", TokenUtils.createSimpleToken(buildAdmin()));
        if (StringUtils.isNotEmpty(plans)) {
            for (OverhaulPlanResDTO plan : plans) {
                OverhaulPlanReqDTO req = new OverhaulPlanReqDTO();
                BeanUtils.copyProperties(plan, req);
                // 调用本地接口触发检修计划
                String url = baseUrl + "/overhaul/zc/plan/trigger";
                HttpEntity<String> strEntity = new HttpEntity<>(JSONObject.toJSONString(req), headers);
                JSONObject res = restTemplate.postForObject(url, strEntity, JSONObject.class);
                if (!CommonConstants.ZERO_STRING.equals(Objects.requireNonNull(res).getString(CommonConstants.CODE))) {
                    log.error("计划编号为：" + req.getPlanCode() + "的检修计划自动触发失败，错误原因为：" + Objects.requireNonNull(res).getString(CommonConstants.MESSAGE));
                }
            }
        }
    }

    /**
     * 拼装admin用户信息
     * @return 用户信息
     */
    private CurrentLoginUser buildAdmin() {
        CurrentLoginUser person = new CurrentLoginUser();
        person.setPersonId(CommonConstants.ADMIN);
        person.setPersonNo(CommonConstants.ADMIN);
        person.setPersonName("系统管理员");
        person.setCompanyId("A");
        person.setCompanyName("集团本级");
        person.setOfficeId("A02");
        person.setOfficeName("办公室");
        person.setNames("集团本级-办公室");
        return person;
    }

}
