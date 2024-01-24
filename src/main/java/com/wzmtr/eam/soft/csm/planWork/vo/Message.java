package com.wzmtr.eam.soft.csm.planWork.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2022/4/20 11:11
 */
@Data
public class Message implements Serializable {

    String equipType1;
    String equipType2;
    String equipType3;
    String groupName;
    String lineName;
    String objectCodes;
    String objectNames;
    String operType;
    String planFinishDate;
    String planName;
    String planNo;
    String planStartDate;
    String posName;
    String syscode;
    String uuid;
    String workNo;
    String workType;
    String groupCode;
}
