package com.wzmtr.eam.mapper.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.res.TransferResDTO;
import com.wzmtr.eam.dto.res.RegionResDTO;
import com.wzmtr.eam.entity.Bom;
import com.wzmtr.eam.entity.WorkFlow;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface TransferMapper {

    Page<TransferResDTO> pageTransfer(Page<TransferResDTO> page, String transferNo, String itemCode, String itemName, String position1Code, String eamProcessStatus,
                                      String eamProcessStatus1, String eamProcessStatus2, String majorCode, String orderNo, String orderName);

    TransferResDTO getTransferDetail(String id);

    List<TransferResDTO> listTransfer(String transferNo, String itemCode, String itemName, String position1Code, String eamProcessStatus,
                                      String majorCode, String orderNo, String orderName);

    void updateTransfer(TransferResDTO transferResDTO);

    List<WorkFlow> queryNotWorkFlow(String todoId);

    List<Bom> queryBomTree(String ename);

}
