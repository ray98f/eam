package com.wzmtr.eam.entity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class PageReqDTO {

    @Min(value = 1L, message = "32000008")
    @NotNull(message = "32000006")
    @ApiModelProperty(value = "当前页码", required = true)
    private Integer pageNo;

    @Range(min = 1, max = 500, message = "32000003")
    @NotNull(message = "32000006")
    @ApiModelProperty(value = "每页条数。范围：1-500", required = true)
    private Integer pageSize;

    public <T> Page<T> of() {
        return new Page<>(this.getPageNo(), this.getPageSize());
    }

}
