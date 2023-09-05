package com.wzmtr.eam.dto.req;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class EipMsgPushReq {

    private String todoId;

    private String title;

    private String operId;

    private String syscode;

    private String todoStatus;

    private String eipUrl;

    private String phoneUrl;

    private String kindType;

    private String userId;

    public void add() {
        this.operId = "1";
        this.todoStatus = "1";
        this.kindType = "1";
    }

    public void update() {
        this.operId = "2";
        this.todoStatus = "2";
        this.kindType = "1";
    }

    public void readed() {
        this.operId = "1";
        this.todoStatus = "2";
        this.kindType = "1";
    }

    public void delete() {
        this.operId = "3";
        this.todoStatus = "1";
        this.kindType = "1";
    }

    public void send() {
        this.operId = "1";
        this.todoStatus = "7";
        this.kindType = "1";
    }

}
