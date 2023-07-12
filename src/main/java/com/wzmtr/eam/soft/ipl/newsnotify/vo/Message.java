package com.wzmtr.eam.soft.ipl.newsnotify.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2022/4/20 11:11
 */
@Data
public class Message implements Serializable {

    String id;
    String category_id;
    String title;
    String link;
    String image;
    String userid;
    Date imp_date;
    String oper_id;
    String type;
    String company_id;
    String org_type;
    String office_id;
    String user_id;
    String dphClassify;
    String mnClassify;
    String userOffice;
    String fawen_flag;
    String fawen_type;
}
