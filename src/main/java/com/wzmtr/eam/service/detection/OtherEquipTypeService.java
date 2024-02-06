package com.wzmtr.eam.service.detection;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.detection.OtherEquipTypeReqDTO;
import com.wzmtr.eam.dto.res.detection.OtherEquipTypeResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 其他设备管理-其他设备分类管理
 * @author  Ray
 * @version 1.0
 * @date 2024/02/02
 */
public interface OtherEquipTypeService {

    /**
     * 分页获取其他设备分类
     * @param typeCode 分类编号
     * @param typeName 分类名称
     * @param pageReqDTO 分页参数
     * @return 其他设备分类列表
     */
    Page<OtherEquipTypeResDTO> pageOtherEquipType(String typeCode, String typeName, PageReqDTO pageReqDTO);

    /**
     * 获取其他设备分类详情
     * @param id id
     * @return 其他设备分类详情
     */
    OtherEquipTypeResDTO getOtherEquipTypeDetail(String id);

    /**
     * 导入其他设备分类
     * @param file 文件
     */
    void importOtherEquipType(MultipartFile file);

    /**
     * 新增其他设备分类
     * @param otherEquipTypeReqDTO 其他设备分类信息
     */
    void addOtherEquipType(OtherEquipTypeReqDTO otherEquipTypeReqDTO);

    /**
     * 修改其他设备分类
     * @param otherEquipTypeReqDTO 其他设备分类信息
     */
    void modifyOtherEquipType(OtherEquipTypeReqDTO otherEquipTypeReqDTO);

    /**
     * 删除其他设备分类
     * @param ids ids
     */
    void deleteOtherEquipType(List<String> ids);

    /**
     * 删除其他设备分类
     * @param typeCode 分类编号
     * @param typeName 分类名称
     * @param ids ids
     * @param response response
     * @throws IOException 流异常
     */
    void exportOtherEquipType(String typeCode, String typeName, String ids, HttpServletResponse response) throws IOException;

    /**
     * 获取其他设备分类列表
     * @param typeCode 分类编号
     * @param typeName 分类名称
     * @return 其他设备分类列表
     */
    List<OtherEquipTypeResDTO> listOtherEquipType(String typeCode, String typeName);

}
