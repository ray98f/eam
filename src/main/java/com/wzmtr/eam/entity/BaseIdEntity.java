package com.wzmtr.eam.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author frp
 */
@Data
public class BaseIdEntity {

    @NotNull(message = "32000006")
    private String id;
}
