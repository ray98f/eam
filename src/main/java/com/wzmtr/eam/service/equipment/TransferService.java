package com.wzmtr.eam.service.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.TransferSplitReqDTO;
import com.wzmtr.eam.dto.res.EquipmentResDTO;
import com.wzmtr.eam.dto.res.TransferResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

public interface TransferService {

    Page<TransferResDTO> pageTransfer(String transferNo, String itemCode, String itemName, String position1Code, String eamProcessStatus,
                                      String majorCode, String orderNo, String orderName, PageReqDTO pageReqDTO);

    TransferResDTO getTransferDetail(String id);

    void exportTransfer(String transferNo, String itemCode, String itemName, String position1Code, String eamProcessStatus,
                        String majorCode, String orderNo, String orderName, HttpServletResponse response);

    void encodingTransfer(BaseIdsEntity baseIdsEntity);

    Page<EquipmentResDTO> pageSplitTransfer(String sourceRecId, PageReqDTO pageReqDTO);

    void saveSplitTransfer(List<EquipmentResDTO> list);

    void submitSplitTransfer(TransferSplitReqDTO transferSplitReqDTO) throws Exception;

    void exportSplitTransfer(String sourceRecId, HttpServletResponse response);
}
