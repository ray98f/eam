// package com.wzmtr.eam.controller.fault;
//
// import com.wzmtr.eam.dto.req.secure.SecureCheckAddReqDTO;
// import com.wzmtr.eam.dto.req.secure.SecureCheckDetailReqDTO;
// import com.wzmtr.eam.dto.req.secure.SecureCheckRecordListReqDTO;
// import com.wzmtr.eam.dto.res.secure.SecureCheckRecordListResDTO;
// import com.wzmtr.eam.entity.BaseIdsEntity;
// import com.wzmtr.eam.entity.response.DataResponse;
// import com.wzmtr.eam.entity.response.PageResponse;
// import com.wzmtr.eam.service.Fault.TrackService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
// import io.swagger.annotations.ApiParam;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;
//
// import javax.servlet.http.HttpServletResponse;
//
// @RestController
// @RequestMapping("/fault/track")
// @Api(tags = "故障管理-故障跟踪工单")
// public class FaultTrackController {
//
//     @Autowired
//     private TrackService trackService;
//
//     @ApiOperation(value = "安全/质量/消防/-检查问题记录列表")
//     @PostMapping("/record/list")
//     public PageResponse<SecureCheckRecordListResDTO> list(@RequestBody SecureCheckRecordListReqDTO reqDTO) {
//         return PageResponse.of(trackService.list(reqDTO));
//     }
//     @ApiOperation(value = "安全/质量/消防/-检查问题单详情")
//     @PostMapping("/detail")
//     public DataResponse<SecureCheckRecordListResDTO> detail(@RequestBody SecureCheckDetailReqDTO reqDTO) {
//         return DataResponse.of(trackService.detail(reqDTO));
//     }
//     // @ResponseBody
//     // @PostMapping("/mobile/status/list")
//     // public Result<List<AdminMobileStatusResDTO>> adminMobileStatusList(@ApiParam @RequestBody AdminMobileStatusReqDTO dto){
//     //     List<AdminManagerBO> resultList = adminManagerService.query(Query.builder().criteria(Criteria.in(ClueCols.ADMIN_ID, dto.getAdminIds())).build());
//     //     if(__CollectionUtil.isEmpty(resultList))
//     //         return Result.ok();
//     //     return Result.ok(__StreamUtil.map(resultList,bo -> AdminMobileStatusResDTO.create(bo)));
//     // }
//     @ApiOperation(value = "安全/质量/消防/-检查问题单新增")
//     @PostMapping("/record/add")
//     public DataResponse<SecureCheckRecordListResDTO> add(@RequestBody SecureCheckAddReqDTO reqDTO) {
//         trackService.add(reqDTO);
//         return DataResponse.success();
//     }
//     @ApiOperation(value = "安全/质量/消防/-检查问题单删除")
//     @PostMapping("/record/delete")
//     public DataResponse<SecureCheckRecordListResDTO> delete(@RequestBody BaseIdsEntity ids) {
//         trackService.delete(ids);
//         return DataResponse.success();
//     }
//     @ApiOperation(value = "安全/质量/消防/-检查问题单导出")
//     @GetMapping("/record/export")
//     public void export(@RequestParam(required = false) @ApiParam("检查问题单号") String secRiskId,
//                        @RequestParam(required = false) @ApiParam("发现日期") String inspectDate,
//                        @RequestParam(required = false) @ApiParam(value = "整改情况") String restoreDesc,
//                        @RequestParam(required = false) @ApiParam(value = "流程状态") String workFlowInstStatus,
//                        HttpServletResponse response) {
//         trackService.export(secRiskId,inspectDate,restoreDesc,workFlowInstStatus,response);
//     }
//
// }
