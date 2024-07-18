package com.wzmtr.eam.mapper.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.basic.EquipmentCategoryPartReqDTO;
import com.wzmtr.eam.dto.req.basic.EquipmentCategoryReqDTO;
import com.wzmtr.eam.dto.res.basic.EquipmentCategoryModuleResDTO;
import com.wzmtr.eam.dto.res.basic.EquipmentCategoryResDTO;
import com.wzmtr.eam.dto.res.basic.EquipmentCategorySubclassResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface EquipmentCategoryMapper {

    /**
     * 获取设备分类列表-分页
     * @param page 分页参数
     * @param name 名称
     * @param code 编号
     * @param parentId 父id
     * @return 设备分类列表
     */
    Page<EquipmentCategoryResDTO> pageEquipmentCategory(Page<EquipmentCategoryResDTO> page, String name, String code, String parentId);

    /**
     * 获取设备分类根列表
     * @return 设备分类根列表
     */
    List<EquipmentCategoryResDTO> listEquipmentCategoryRootList();

    /**
     * 获取设备分类子列表
     * @return 设备分类子列表
     */
    List<EquipmentCategoryResDTO> listEquipmentCategoryBodyList();

    /**
     * 获取设备分类详情
     * @param id id
     * @return 设备分类详情
     */
    EquipmentCategoryResDTO getEquipmentCategoryDetail(String id);

    /**
     * 查询设备分类是否已存在
     * @param equipmentCategoryReqDTO 传参
     * @return 是否已存在
     */
    Integer selectEquipmentCategoryIsExist(EquipmentCategoryReqDTO equipmentCategoryReqDTO);

    /**
     * 新增设备分类
     * @param equipmentCategoryReqDTO 传参
     */
    void addEquipmentCategory(EquipmentCategoryReqDTO equipmentCategoryReqDTO);

    /**
     * 修改设备分类
     * @param equipmentCategoryReqDTO 传参
     */
    void modifyEquipmentCategory(EquipmentCategoryReqDTO equipmentCategoryReqDTO);

    /**
     * 删除设备分类
     * @param ids ids
     * @param userId 用户id
     * @param time 时间
     */
    void deleteEquipmentCategory(List<String> ids, String userId, String time);

    /**
     * 获取设备分类列表
     * @param name 名称
     * @param code 编号
     * @param parentId 父id
     * @return 设备分类列表
     */
    List<EquipmentCategoryResDTO> listEquipmentCategory(String name, String code, String parentId);

    /**
     * 导出设备分类列表
     * @param ids ids
     * @return 设备分类列表
     */
    List<EquipmentCategoryResDTO> exportEquipmentCategory(List<String> ids);

    /**
     * 获取设备分类一级分类
     * @param type 类型 1中铁通 2中车
     * @return 设备分类一级分类
     */
    List<EquipmentCategoryResDTO> getFirstEquipmentCategory(String type);

    /**
     * 获取设备分类子级分类
     * @param code 父级设备分类编号
     * @return 设备分类子级分类
     */
    List<EquipmentCategoryResDTO> getChildEquipmentCategory(String code);

    /**
     * 根据元素获取元素
     * @param nodeCode 节点编号
     * @param nodeName 节点名称
     * @param nodeLevel 节点等级
     * @return 元素
     */
    EquipmentCategoryResDTO getIndexByIndex(String nodeCode, String nodeName, Integer nodeLevel);

    /**
     * 获取设备分类绑定的部件模块列表
     * @param majorCode 专业编码
     * @param systemCode 系统编码
     * @param equipTypeCode 设备分类编码
     * @return 设备分类绑定的部件列表
     */
    List<EquipmentCategoryModuleResDTO> listEquipmentCategoryModule(String majorCode,
                                                                    String systemCode,
                                                                    String equipTypeCode);

    /**
     * 获取部件模块下的部件列表
     * @param majorCode 专业编码
     * @param systemCode 系统编码
     * @param equipTypeCode 设备分类编码
     * @param moduleName 模块名称
     * @return 部件模块下的部件列表
     */
    List<EquipmentCategoryModuleResDTO.Part> listEquipmentCategoryPart(String majorCode,
                                                                       String systemCode,
                                                                       String equipTypeCode,
                                                                       String moduleName);

    /**
     * 获取设备分类绑定的设备小类列表
     * @param majorCode 专业编码
     * @param systemCode 系统编码
     * @param equipTypeCode 设备分类编码
     * @return 设备分类绑定的设备小类列表
     */
    List<EquipmentCategorySubclassResDTO> listEquipmentCategorySubclass(String majorCode,
                                                                        String systemCode,
                                                                        String equipTypeCode);

    /**
     * 获取部件模块下的部件列表-设备小类
     * @param majorCode 专业编码
     * @param systemCode 系统编码
     * @param equipTypeCode 设备分类编码
     * @param equipSubclassName 设备小类名称
     * @return 部件模块下的部件列表
     */
    List<EquipmentCategorySubclassResDTO.Module> listEquipmentCategoryModuleBySubclass(String majorCode,
                                                                                       String systemCode,
                                                                                       String equipTypeCode,
                                                                                       String equipSubclassName);

    /**
     * 获取部件模块下的部件列表-设备小类
     * @param majorCode 专业编码
     * @param systemCode 系统编码
     * @param equipTypeCode 设备分类编码
     * @param equipSubclassName 设备小类名称
     * @param moduleName 模块名称
     * @return 部件模块下的部件列表
     */
    List<EquipmentCategoryModuleResDTO.Part> listEquipmentCategoryPartBySubclass(String majorCode,
                                                                                 String systemCode,
                                                                                 String equipTypeCode,
                                                                                 String equipSubclassName,
                                                                                 String moduleName);

    /**
     * 导入设备分类绑定的部件列表
     * @param list 设备分类绑定的部件列表
     */
    void importEquipmentCategoryPart(List<EquipmentCategoryPartReqDTO> list);

}
