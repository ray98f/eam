package com.wzmtr.eam.bizobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Li.Wang
 * Date: 2023/9/12 14:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HomeCountBO {
    private String modelName;
    private String state;
    private String userId;
    private String todoStatus;
    private String receiveUserId;
    private String status;
}
