package com.wzmtr.eam.entity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/12/10 9:49
 */
@Data
@ApiModel
public class PageReqDTO {

    @Min(value = 1L, message = "32000008")
    @NotNull(message = "32000006")
    @ApiModelProperty(value = "当前页码", required = true)
    private Integer pageNo;

    @Range(min = 1, max = 500, message = "32000003")
    @NotNull(message = "32000006")
    @ApiModelProperty(value = "每页条数。范围：1-50", required = true)
    private Integer pageSize;

//    @Size(max = 128, message = "32000003")
//    @ApiModelProperty(value = "排序字段。长度：0-128")
//    private String sort;

//    @ApiModelProperty(value = "顺序,asc: 逆序，desc")
//    private String order;

    public <T> Page<T> of() {
//        if (null != sort && null != order) {
//            Page<T> page = new Page<>(this.getPage(), this.getSize());
//            page.setOrders(Collections.singletonList(new OrderItem().setAsc("asc".equalsIgnoreCase(order)).setColumn(sort)));
//            return page;
//        }
        return new Page<>(this.getPageNo(), this.getPageSize());
    }

}
