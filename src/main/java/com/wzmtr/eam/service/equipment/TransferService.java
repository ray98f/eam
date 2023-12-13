package com.wzmtr.eam.service.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.equipment.TransferSplitReqDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentResDTO;
import com.wzmtr.eam.dto.res.equipment.TransferResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface TransferService {

    Page<TransferResDTO> pageTransfer(String transferNo, String itemCode, String itemName, String position1Code, String eamProcessStatus,
                                      String majorCode, String orderNo, String orderName, PageReqDTO pageReqDTO);

    TransferResDTO getTransferDetail(String id);

    void exportTransfer(String transferNo, String itemCode, String itemName, String position1Code, String eamProcessStatus,
                        String majorCode, String orderNo, String orderName, HttpServletResponse response) throws IOException;

    void encodingTransfer(BaseIdsEntity baseIdsEntity);

    Page<EquipmentResDTO> pageSplitTransfer(String sourceRecId, PageReqDTO pageReqDTO);

    EquipmentResDTO getSplitTransferDetail(String id);

    void saveSplitTransfer(EquipmentResDTO equipmentResDTO);

    void submitSplitTransfer(TransferSplitReqDTO transferSplitReqDTO) throws Exception;

    void exportSplitTransfer(String sourceRecId, HttpServletResponse response) throws IOException;
}
