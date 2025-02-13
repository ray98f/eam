ALTER TABLE "C##EAM_ZTE".SYS_OFFICE ADD NAMES VARCHAR2(2000);

UPDATE T_EQUIPMENT_CATEGORY SET DELETE_FLAG =0;
ALTER TABLE "C##EAM_ZTE".T_EQUIPMENT_CATEGORY MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_FAULT SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_FAULT MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

DELETE FROM T_EQUIPMENT_CATEGORY t1
WHERE (SELECT REC_ID FROM T_EQUIPMENT_CATEGORY t2 WHERE t2.PARENT_NODE_REC_ID!='0' and t2.REC_ID =t1.PARENT_NODE_REC_ID) IS NULL;
DELETE FROM SYS_REGION t1
WHERE (SELECT REC_ID FROM SYS_REGION t2 WHERE t2.REC_ID =t1.PARENT_NODE_REC_ID) IS NULL;

INSERT INTO "C##EAM_ZTE".SYS_OFFICE (ID, PARENT_ID, PARENT_IDS, NAME, "TYPE", GRADE, SORT, REMARKS, USEABLE, DEL_FLAG, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, AREA_ID) VALUES('W', 'root', 'root', '供应商', '0', '2', 9999, NULL, '1', '0', NULL, TIMESTAMP '2023-07-10 02:02:36.000000', NULL, TIMESTAMP '2023-07-10 02:02:36.000000', NULL);

UPDATE SYS_REGION SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".SYS_REGION MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE SYS_ORG_MAJOR SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".SYS_ORG_MAJOR MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE SYS_ORG_TYPE SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".SYS_ORG_TYPE MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE SYS_ORG_LINE SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".SYS_ORG_LINE MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_WO_RULE SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_WO_RULE MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_WO_RULE_DETAIL SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_WO_RULE_DETAIL MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_EQUIPMENT SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_EQUIPMENT MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

INSERT INTO "C##EAM_ZTE".T_EQUIPMENT_CATEGORY (REC_ID, COMPANY_CODE, COMPANY_NAME, NODE_CODE, NODE_NAME, PARENT_NODE_REC_ID, NODE_LEVEL, REMARK, REC_CREATOR, REC_CREATE_TIME, REC_REVISOR, REC_REVISE_TIME, REC_DELETOR, REC_DELETE_TIME, DELETE_FLAG, ARCHIVE_FLAG, REC_STATUS, EXT1, EXT2, EXT3, EXT4, EXT5) VALUES('0', ' ', ' ', '0', '设备分类代码', NULL, 0, '1', 'admin', '20180421', NULL, NULL, NULL, NULL, '0', NULL, '10', NULL, NULL, NULL, NULL, NULL);

UPDATE T_EQUIPMENT SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_EQUIPMENT MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_EQUIPMENT_ROOM SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_EQUIPMENT_ROOM MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_EQUIPMENT_CHARGE SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_EQUIPMENT_CHARGE MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_PART_REPLACEMENT SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_PART_REPLACEMENT MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_GEARBOX_CHANGE_OIL SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_GEARBOX_CHANGE_OIL MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_WHEELSET_LATHING SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_WHEELSET_LATHING MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_GENERAL_SURVEY SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_GENERAL_SURVEY MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_DETECTION_PLAN SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_DETECTION_PLAN MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_DETECTION_PLAN_DETAIL SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_DETECTION_PLAN_DETAIL MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_DETECTION SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_DETECTION MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_DETECTION_DETAIL SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_DETECTION_DETAIL MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_OVERHAUL_TPL SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_OVERHAUL_TPL MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_OVERHAUL_TPL_DETAIL SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_OVERHAUL_TPL_DETAIL MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_OVERHAUL_MATERIAL SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_OVERHAUL_MATERIAL MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_OVERHAUL_PLAN SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_OVERHAUL_PLAN MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_OVERHAUL_OBJECT SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_OVERHAUL_OBJECT MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_OVERHAUL_ORDER SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_OVERHAUL_ORDER MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_OVERHAUL_ORDER_DETAIL SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_OVERHAUL_ORDER_DETAIL MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_WEEKLY_PLAN SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_WEEKLY_PLAN MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_MEA_CHECK_PLAN SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_MEA_CHECK_PLAN MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_MEA_INFO SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_MEA_INFO MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_MEA_SUBMISSION SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_MEA_SUBMISSION MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_MEA_SUBMISSION_DETAIL SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_MEA_SUBMISSION_DETAIL MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_MEA_SUBMISSION_RECORD SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_MEA_SUBMISSION_RECORD MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

UPDATE T_SUBMISSION_RECORD_DETAIL SET DELETE_FLAG=0;
ALTER TABLE "C##EAM_ZTE".T_SUBMISSION_RECORD_DETAIL MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;

ALTER TABLE "C##EAM_ZTE".T_WHEELSET_LATHING ADD WHEEL_NO NVARCHAR2(20);
ALTER TABLE "C##EAM_ZTE".T_WHEELSET_LATHING ADD WHEEL_HEIGHT NVARCHAR2(100);
ALTER TABLE "C##EAM_ZTE".T_WHEELSET_LATHING ADD WHEEL_THICK NVARCHAR2(100);
ALTER TABLE "C##EAM_ZTE".T_WHEELSET_LATHING ADD WHEEL_DIAMETER NVARCHAR2(100);

ALTER TABLE "C##EAM_ZTE".T_GENERAL_SURVEY ADD RECORD_ID NVARCHAR2(2000);

CREATE TABLE EAM.T_BPMN_EXAMINE (
	NODE_ID VARCHAR2(100),
	NODE_NAME VARCHAR2(100),
	FLOW_ID VARCHAR2(100),
	FLOW_NAME VARCHAR2(100),
	ROLE_ID VARCHAR2(100),
	ROLE_NAME VARCHAR2(100),
	IS_OWNER_ORG NUMBER DEFAULT 0 NOT NULL,
	STEP NUMBER
);

INSERT INTO EAM.T_EQUIPMENT_CATEGORY (REC_ID, COMPANY_CODE, COMPANY_NAME, NODE_CODE, NODE_NAME, PARENT_NODE_REC_ID, NODE_LEVEL, REMARK, REC_CREATOR, REC_CREATE_TIME, REC_REVISOR, REC_REVISE_TIME, REC_DELETOR, REC_DELETE_TIME, DELETE_FLAG, ARCHIVE_FLAG, REC_STATUS, EXT1, EXT2, EXT3, EXT4, EXT5) VALUES('0', ' ', ' ', 'root', '根目录', NULL, 0, ' ', 'admin', '20180622175344', ' ', ' ', ' ', ' ', '0', ' ', '10', ' ', ' ', ' ', ' ', ' ');

UPDATE T_MEA_INSTRUMENT SET LINE_NO='01' WHERE REC_ID IS NOT NULL

UPDATE T_OVERHAUL_TPL_DETAIL SET
MIN_VALUE =NULL
WHERE (MIN_VALUE='' OR MIN_VALUE=' ');

UPDATE T_OVERHAUL_TPL_DETAIL SET
MAX_VALUE =NULL
WHERE (MAX_VALUE='' OR MAX_VALUE=' ');


ALTER TABLE "EAM"."T_SEC_RISK"
    MODIFY ("SEC_RISK_PIC" NVARCHAR2(500))
    MODIFY ("RESTORE_PIC" NVARCHAR2(500));

ALTER TABLE "EAM"."T_RISK_RECORD"
    MODIFY ("RISK_PIC" NVARCHAR2(500))


---------------------------- 2024.01.22 每日列车里程及能耗表 ----------------------------
CREATE TABLE EAM.T_TRAIN_MILE (
	REC_ID NVARCHAR2(50) NOT NULL,
	EQUIP_CODE NVARCHAR2(20) NULL,
	EQUIP_NAME NVARCHAR2(200),
	"DAY" TIMESTAMP,
	DAILY_MILE NUMBER,
	TOTAL_WORK_MILE NUMBER,
	TOTAL_MILE NUMBER,
	TRACTION_INCREMENT NUMBER,
	TOTAL_TRACTION_ENERGY NUMBER,
	AUXILIARY_INCREMENT NUMBER,
	TOTAL_AUXILIARY_ENERGY NUMBER,
	REGENRATED_INCREMENT NUMBER,
	TOTAL_REGENRATED_ELECTRICITY NUMBER,
	REC_CREATOR NVARCHAR2(20),
	REC_CREATE_TIME NVARCHAR2(20),
	REC_REVISOR NVARCHAR2(20),
	REC_REVISE_TIME NVARCHAR2(20),
	REC_DELETOR NVARCHAR2(20),
	REC_DELETE_TIME NVARCHAR2(20),
	DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL
);
COMMENT ON TABLE EAM.T_TRAIN_MILE IS '每日列车里程及能耗表';

ALTER TABLE EAM.T_TRAIN_MILE RENAME COLUMN DAILY_MILE TO DAILY_WORK_MILE;
ALTER TABLE EAM.T_TRAIN_MILE ADD DAILY_MILE NUMBER;
ALTER TABLE EAM.T_TRAIN_MILE ADD REMARK NVARCHAR2(1000);
ALTER TABLE EAM.T_TRAIN_MILE MODIFY "DAY" NVARCHAR2(20);

---------------------------- 2024.01.25 故障提报暂存 ----------------------------
ALTER TABLE EAM.T_FAULT_INFO MODIFY COMPANY_CODE NVARCHAR2(20) NULL;
ALTER TABLE EAM.T_FAULT_INFO MODIFY COMPANY_NAME NVARCHAR2(50) NULL;
ALTER TABLE EAM.T_FAULT_INFO MODIFY FAULT_NO NVARCHAR2(20) NULL;
ALTER TABLE EAM.T_FAULT_INFO MODIFY FAULT_FLAG NVARCHAR2(1) NULL;
ALTER TABLE EAM.T_FAULT_INFO MODIFY LINE_CODE NVARCHAR2(3) NULL;
ALTER TABLE EAM.T_FAULT_INFO MODIFY POSITION_CODE NVARCHAR2(50) NULL;
ALTER TABLE EAM.T_FAULT_INFO MODIFY MAJOR_CODE NVARCHAR2(20) NULL;
ALTER TABLE EAM.T_FAULT_INFO MODIFY FAULT_TYPE NVARCHAR2(2) NULL;
ALTER TABLE EAM.T_FAULT_INFO MODIFY SOURCE_CODE NVARCHAR2(20) NULL;
ALTER TABLE EAM.T_FAULT_INFO MODIFY FAULT_DISPLAY_CODE NVARCHAR2(10) NULL;
ALTER TABLE EAM.T_FAULT_INFO MODIFY FAULT_DISPLAY_DETAIL NVARCHAR2(200) NULL;
ALTER TABLE EAM.T_FAULT_INFO MODIFY DISCOVERER_NAME NVARCHAR2(20) NULL;
ALTER TABLE EAM.T_FAULT_INFO MODIFY DISCOVERY_TIME NVARCHAR2(30) NULL;
ALTER TABLE EAM.T_FAULT_INFO MODIFY FILLIN_USER_ID NVARCHAR2(20) NULL;
ALTER TABLE EAM.T_FAULT_INFO MODIFY FILLIN_DEPT_CODE NVARCHAR2(20) NULL;
ALTER TABLE EAM.T_FAULT_INFO MODIFY FILLIN_TIME NVARCHAR2(30) NULL;
ALTER TABLE EAM.T_FAULT_INFO MODIFY FAULT_LEVEL NVARCHAR2(2) NULL;
ALTER TABLE EAM.T_FAULT_INFO MODIFY FAULT_STATUS NVARCHAR2(3) NULL;
ALTER TABLE EAM.T_FAULT_INFO MODIFY DOC_ID NVARCHAR2(500) NULL;

ALTER TABLE EAM.T_FAULT_ORDER MODIFY COMPANY_CODE NVARCHAR2(20) NULL;
ALTER TABLE EAM.T_FAULT_ORDER MODIFY COMPANY_NAME NVARCHAR2(50) NULL;
ALTER TABLE EAM.T_FAULT_ORDER MODIFY FAULT_NO NVARCHAR2(20) NULL;
ALTER TABLE EAM.T_FAULT_ORDER MODIFY FAULT_WORK_NO NVARCHAR2(20) NULL;
ALTER TABLE EAM.T_FAULT_ORDER MODIFY ORDER_STATUS NVARCHAR2(3) NULL;
ALTER TABLE EAM.T_FAULT_ORDER MODIFY DOC_ID NVARCHAR2(500) NULL;

ALTER TABLE EAM.T_GEARBOX_CHANGE_OIL ADD TOTAL_MILES NUMBER;

---------------------------- 2024.01.30 特种设备分类表 ----------------------------
CREATE TABLE EAM.T_SPECIAL_EQUIP_TYPE (
	REC_ID NVARCHAR2(50) NOT NULL,
	TYPE_CODE NVARCHAR2(50),
	TYPE_NAME NVARCHAR2(50),
	DETECTION_PERIOD NVARCHAR2(50),
	REC_CREATOR NVARCHAR2(20),
	REC_CREATE_TIME NVARCHAR2(20),
	REC_REVISOR NVARCHAR2(20),
	REC_REVISE_TIME NVARCHAR2(20),
	REC_DELETOR NVARCHAR2(20),
	REC_DELETE_TIME NVARCHAR2(20),
	DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL
);

CREATE TABLE EAM.T_OTHER_EQUIP_TYPE (
	REC_ID NVARCHAR2(50) NOT NULL,
	TYPE_CODE NVARCHAR2(50),
	TYPE_NAME NVARCHAR2(50),
	DETECTION_PERIOD NVARCHAR2(50),
	REC_CREATOR NVARCHAR2(20),
	REC_CREATE_TIME NVARCHAR2(20),
	REC_REVISOR NVARCHAR2(20),
	REC_REVISE_TIME NVARCHAR2(20),
	REC_DELETOR NVARCHAR2(20),
	REC_DELETE_TIME NVARCHAR2(20),
	DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL
);

ALTER TABLE EAM.T_MEA_SUBMISSION_RECORD MODIFY DOC_ID NVARCHAR2(1000);

ALTER TABLE EAM.T_MEA_SUBMISSION_DETAIL MODIFY REC_CREATOR NVARCHAR2(30) NOT NULL;
ALTER TABLE EAM.T_MEA_SUBMISSION_DETAIL MODIFY REC_CREATE_TIME NVARCHAR2(20) NOT NULL;
ALTER TABLE EAM.T_MEA_SUBMISSION_DETAIL MODIFY REC_REVISOR NVARCHAR2(30) NULL;
ALTER TABLE EAM.T_MEA_SUBMISSION_DETAIL MODIFY REC_REVISE_TIME NVARCHAR2(20) NULL;

-- EAM.T_OTHER_EQUIP_EXT definition
CREATE TABLE "EAM"."T_OTHER_EQUIP_EXT" (
    "REC_ID" NVARCHAR2(50) NOT NULL,
	"REG_ORG" NVARCHAR2(50),
	"REG_NO" NVARCHAR2(50),
	"EQUIP_CODE" NVARCHAR2(50) NOT NULL,
	"OTHER_EQUIP_TYPE" NVARCHAR2(20),
	"OTHER_EQUIP_CODE" NVARCHAR2(50),
	"FACT_NO" NVARCHAR2(50),
	"EQUIP_INNER_NO" NVARCHAR2(100),
	"EQUIP_POSITION" NVARCHAR2(100),
	"EQUIP_DETAILED_POSITION" NVARCHAR2(100),
	"EQUIP_PARAMETER" NVARCHAR2(100),
	"MANAGE_ORG" NVARCHAR2(50),
	"SEC_ORG" NVARCHAR2(50),
	"SEC_STAFF_NAME" NVARCHAR2(20),
	"SEC_STAFF_PHONE" NVARCHAR2(20),
	"SEC_STAFF_MOBILE" NVARCHAR2(20),
	"LINKMAN_NAME" NVARCHAR2(20),
	"LINKMAN_PHONE" NVARCHAR2(20),
	"LINKMAN_MOBILE" NVARCHAR2(20),
	"REMARK" NVARCHAR2(1000),
	"REC_CREATOR" NVARCHAR2(20) NOT NULL,
	"REC_CREATE_TIME" NVARCHAR2(20) NOT NULL,
	"REC_REVISOR" NVARCHAR2(20),
	"REC_REVISE_TIME" NVARCHAR2(20),
	"REC_DELETOR" NVARCHAR2(20),
	"REC_DELETE_TIME" NVARCHAR2(20),
	"DELETE_FLAG" NVARCHAR2(1),
	"ARCHIVE_FLAG" NVARCHAR2(1),
	"REC_STATUS" NVARCHAR2(2),
	"EXT1" NVARCHAR2(100),
	"EXT2" NVARCHAR2(100),
	"EXT3" NVARCHAR2(100),
	"EXT4" NVARCHAR2(100),
	"EXT5" NVARCHAR2(100),
	"EQUIP_NAME" NVARCHAR2(400)
   );

CREATE TABLE EAM.T_FAULT_FLOW (
	REC_ID NVARCHAR2(50) NOT NULL,
	FAULT_NO NVARCHAR2(20),
	FAULT_WORK_NO NVARCHAR2(20),
	ORDER_STATUS NVARCHAR2(3),
	OPERATE_USER NVARCHAR2(20),
	OPERATE_TIME NVARCHAR2(20)
);

ALTER TABLE EAM.T_FAULT_FLOW ADD OPERATE_USER_NAME NVARCHAR2(20);
ALTER TABLE EAM.T_FAULT_FLOW RENAME COLUMN OPERATE_USER TO OPERATE_USER_ID;

INSERT INTO EAM.SYS_DICT (CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME, REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE) VALUES('dm.faultType2', '10', '行车设备故障', ' ', ' ', ' ', '10', '1', 'admin', '20180529161148', ' ', ' ', ' ', 'WZPLAT', ' ');
INSERT INTO EAM.SYS_DICT (CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME, REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE) VALUES('dm.faultType2', '20', '票务设备故障', ' ', ' ', ' ', '20', '1', 'admin', '20180529161148', ' ', ' ', ' ', 'WZPLAT', ' ');
INSERT INTO EAM.SYS_DICT (CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME, REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE) VALUES('dm.faultType2', '30', '客运服务故障', ' ', ' ', ' ', '30', '1', 'admin', '20180529161148', ' ', ' ', ' ', 'WZPLAT', ' ');
INSERT INTO EAM.SYS_DICT (CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME, REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE) VALUES('dm.faultType2', '40', '车站设备区故障', ' ', ' ', ' ', '40', '1', 'admin', '20180529161148', ' ', ' ', ' ', 'WZPLAT', ' ');
INSERT INTO EAM.SYS_DICT (CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME, REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE) VALUES('dm.faultType2', '50', '自检自修故障', ' ', ' ', ' ', '50', '1', 'admin', '20180529161148', ' ', ' ', ' ', 'WZPLAT', ' ');

UPDATE T_BOM SET DELETE_FLAG=0;
ALTER TABLE EAM.T_BOM MODIFY DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL;
ALTER TABLE EAM.T_BOM MODIFY RELATION_ID NVARCHAR2(50) NULL;
ALTER TABLE EAM.T_BOM MODIFY TBLNAME NVARCHAR2(100) NULL;
ALTER TABLE EAM.T_BOM MODIFY EXT1 NVARCHAR2(100) NULL;
ALTER TABLE EAM.T_BOM MODIFY EXT2 NVARCHAR2(100) NULL;
ALTER TABLE EAM.T_BOM MODIFY EXT3 NVARCHAR2(100) NULL;
ALTER TABLE EAM.T_BOM MODIFY EXT4 NVARCHAR2(100) NULL;
ALTER TABLE EAM.T_BOM MODIFY EXT5 NVARCHAR2(100) NULL;
ALTER TABLE EAM.T_BOM MODIFY TREE_ID NVARCHAR2(100) NULL;


CREATE TABLE EAM.T_BOM_TRAIN (
	REC_ID VARCHAR2(100) NOT NULL,
	BOM_PARENT_CODE VARCHAR2(100),
	EQUIP_CODE VARCHAR2(100),
	EQUIP_NAME VARCHAR2(100)
);
COMMENT ON TABLE EAM.T_BOM_TRAIN IS '列车bom表';
ALTER TABLE EAM.T_BOM_TRAIN ADD BOM_PARENT_NAME VARCHAR2(100);

CREATE TABLE EAM.T_FAULT_ERROR (
	REC_ID NVARCHAR2(50) NOT NULL,
	FAULT_NO NVARCHAR2(20),
	FAULT_WORK_NO NVARCHAR2(20),
	FAULT_INFO VARCHAR2(2000),
	REC_CREATOR NVARCHAR2(20) NOT NULL,
	REC_CREATE_TIME NVARCHAR2(20) NOT NULL,
	REC_REVISOR NVARCHAR2(20),
	REC_REVISE_TIME NVARCHAR2(20),
	DELETE_FLAG NVARCHAR2(1) DEFAULT 0 NOT NULL
);
ALTER TABLE EAM.T_FAULT_ERROR ADD ERROR_MSG VARCHAR2(2000);

ALTER TABLE EAM.T_GENERAL_SURVEY MODIFY DOC_ID NVARCHAR2(2000);
ALTER TABLE EAM.T_GENERAL_SURVEY MODIFY REC_DELETE_TIME NVARCHAR2(24);
ALTER TABLE EAM.T_GENERAL_SURVEY MODIFY REC_REVISE_TIME NVARCHAR2(24);
ALTER TABLE EAM.T_GENERAL_SURVEY MODIFY REC_CREATE_TIME NVARCHAR2(24);
ALTER TABLE EAM.T_WHEELSET_LATHING MODIFY REC_DELETE_TIME NVARCHAR2(24);
ALTER TABLE EAM.T_WHEELSET_LATHING MODIFY REC_REVISE_TIME NVARCHAR2(24);
ALTER TABLE EAM.T_WHEELSET_LATHING MODIFY REC_CREATE_TIME NVARCHAR2(24);
ALTER TABLE EAM.T_GEARBOX_CHANGE_OIL MODIFY REC_DELETE_TIME NVARCHAR2(24);
ALTER TABLE EAM.T_GEARBOX_CHANGE_OIL MODIFY REC_REVISE_TIME NVARCHAR2(24);
ALTER TABLE EAM.T_GEARBOX_CHANGE_OIL MODIFY REC_CREATE_TIME NVARCHAR2(24);

ALTER TABLE EAM.T_RISK_RECORD MODIFY REC_CREATE_TIME NVARCHAR2(20);
ALTER TABLE EAM.T_RISK_RECORD MODIFY REC_REVISE_TIME NVARCHAR2(20);
ALTER TABLE EAM.T_RISK_RECORD MODIFY REC_DELETE_TIME NVARCHAR2(20);
ALTER TABLE EAM.T_DANGER_RISK MODIFY DISC_DATE NVARCHAR2(20);
ALTER TABLE EAM.T_DANGER_RISK MODIFY REC_CREATE_TIME NVARCHAR2(20);
ALTER TABLE EAM.T_DANGER_RISK MODIFY REC_REVISE_TIME NVARCHAR2(20);
ALTER TABLE EAM.T_DANGER_RISK MODIFY REC_DELETE_TIME NVARCHAR2(20);
ALTER TABLE EAM.T_PART_REPLACEMENT MODIFY REC_CREATE_TIME NVARCHAR2(20);
ALTER TABLE EAM.T_PART_REPLACEMENT MODIFY REC_REVISE_TIME NVARCHAR2(20);
ALTER TABLE EAM.T_PART_REPLACEMENT MODIFY REC_DELETE_TIME NVARCHAR2(20);
ALTER TABLE EAM.T_REQUEST_POINTS MODIFY REC_REVISE_TIME NVARCHAR2(20);
ALTER TABLE EAM.T_REQUEST_POINTS MODIFY REC_CREATE_TIME NVARCHAR2(20);
ALTER TABLE EAM.T_REQUEST_POINTS MODIFY REC_DELETE_TIME NVARCHAR2(20);
ALTER TABLE EAM.T_REQUEST_POINTS MODIFY REC_DELETOR NVARCHAR2(20);
ALTER TABLE EAM.T_REQUEST_POINTS MODIFY REC_REVISOR NVARCHAR2(20);
ALTER TABLE EAM.T_REQUEST_POINTS MODIFY REC_CREATOR NVARCHAR2(20);
ALTER TABLE EAM.T_SEC_RISK MODIFY REC_CREATE_TIME NVARCHAR2(20);
ALTER TABLE EAM.T_SEC_RISK MODIFY REC_REVISE_TIME NVARCHAR2(20);
ALTER TABLE EAM.T_SEC_RISK MODIFY REC_DELETE_TIME NVARCHAR2(20);
ALTER TABLE EAM.T_SEC_RISK MODIFY EXAM_DATE NVARCHAR2(20);
ALTER TABLE EAM.T_SEC_RISK MODIFY LAST_PLAN_DATE NVARCHAR2(20);
ALTER TABLE EAM.T_SEC_RISK MODIFY LAST_CHECK_DATE NVARCHAR2(20);
ALTER TABLE EAM.T_SEC_RISK MODIFY PLAN_DATE NVARCHAR2(20);
ALTER TABLE EAM.T_SEC_RISK MODIFY INSPECT_DATE NVARCHAR2(20);
ALTER TABLE EAM.T_SUBMISSION_RECORD_DETAIL MODIFY VERIFY_REPORT_DATE NVARCHAR2(20);
ALTER TABLE EAM.T_SUBMISSION_RECORD_DETAIL MODIFY VERIFY_DATE NVARCHAR2(20);
ALTER TABLE EAM.T_SUBMISSION_RECORD_DETAIL MODIFY NEXT_VERIFY_DATE NVARCHAR2(20);
ALTER TABLE EAM.T_SUBMISSION_RECORD_DETAIL MODIFY LAST_VERIFY_DATE NVARCHAR2(20);
ALTER TABLE EAM.T_SUBMISSION_RECORD_DETAIL MODIFY POSITION2_CODE NVARCHAR2(20);
ALTER TABLE EAM.T_SUBMISSION_RECORD_DETAIL MODIFY POSITION1_CODE NVARCHAR2(20);
ALTER TABLE EAM.T_WORK_PLAN MODIFY REC_DELETE_TIME NVARCHAR2(20);
ALTER TABLE EAM.T_WORK_PLAN MODIFY REC_REVISE_TIME NVARCHAR2(20);
ALTER TABLE EAM.T_WORK_PLAN MODIFY REC_CREATE_TIME NVARCHAR2(20);
ALTER TABLE EAM.T_WORK_PLAN MODIFY REC_CREATOR NVARCHAR2(20);
ALTER TABLE EAM.T_WORK_PLAN MODIFY REC_REVISOR NVARCHAR2(20);
ALTER TABLE EAM.T_WORK_PLAN MODIFY REC_DELETOR NVARCHAR2(20);

ALTER TABLE EAM.T_BOM MODIFY STATUS NVARCHAR2(30) DEFAULT 0;
ALTER TABLE EAM.T_BOM_TEST MODIFY STATUS NVARCHAR2(30) DEFAULT 0;

ALTER TABLE T_FAULT_INFO ADD IF_OTHER VARCHAR2(1) DEFAULT 1 NOT NULL;


---------------------------- 2024.04.19 ----------------------------
ALTER TABLE EAM.T_OVERHAUL_ORDER_SCHEDULING ADD PACKAGE_TYPE VARCHAR2(24);
COMMENT ON COLUMN EAM.T_OVERHAUL_ORDER_SCHEDULING.PACKAGE_TYPE IS '二级修包类型';

ALTER TABLE EAM.T_FAULT_FLOW ADD REMARK VARCHAR2(1000);

INSERT INTO EAM.SYS_DICT_TYPE
(CODESET_CODE, CODESET_NAME, CODESET_ENAME, GB_CODE, REMARK, REC_CREATOR, REC_CREATE_TIME, REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, CODESET_TYPE, CODESET_HIERARCHY, CODESET_URL, PROJECT_NAME, SUB_CODESET_CODE, REF_ID)
VALUES('dm.faultFollowStatus', '故障跟踪工单状态', 'faultFollowStatus', NULL, NULL, 'admin', '20240510144600', 'admin', '20240510144600', ' ', ' ', ' ', ' ', 'WZPLAT', ' ', ' ');
INSERT INTO EAM.SYS_DICT
(CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME, REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE)
VALUES('dm.faultFollowStatus', '10', '草稿', ' ', ' ', ' ', ' ', '1', 'admin', '20240510144600', ' ', ' ', ' ', 'WZPLAT', ' ');
INSERT INTO EAM.SYS_DICT
(CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME, REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE)
VALUES('dm.faultFollowStatus', '15', '待派工', ' ', ' ', ' ', ' ', '1', 'admin', '20240510144600', ' ', ' ', ' ', 'WZPLAT', ' ');
INSERT INTO EAM.SYS_DICT
(CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME, REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE)
VALUES('dm.faultFollowStatus', '20', '跟踪中', ' ', ' ', ' ', ' ', '1', 'admin', '20240510144600', ' ', ' ', ' ', 'WZPLAT', ' ');
INSERT INTO EAM.SYS_DICT
(CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME, REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE)
VALUES('dm.faultFollowStatus', '30', '工班长待审核', ' ', ' ', ' ', ' ', '1', 'admin', '20240510144600', ' ', ' ', ' ', 'WZPLAT', ' ');
INSERT INTO EAM.SYS_DICT
(CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME, REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE)
VALUES('dm.faultFollowStatus', '35', '专业工程师待审核', ' ', ' ', ' ', ' ', '1', 'admin', '20240510144600', ' ', ' ', ' ', 'WZPLAT', ' ');
INSERT INTO EAM.SYS_DICT
(CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME, REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE)
VALUES('dm.faultFollowStatus', '40', '报告驳回', ' ', ' ', ' ', ' ', '1', 'admin', '20240510144600', ' ', ' ', ' ', 'WZPLAT', ' ');
INSERT INTO EAM.SYS_DICT
(CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME, REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE)
VALUES('dm.faultFollowStatus', '50', '关闭', ' ', ' ', ' ', ' ', '1', 'admin', '20240510144600', ' ', ' ', ' ', 'WZPLAT', ' ');

ALTER TABLE EAM.T_FAULT_FOLLOW ADD DISPATCH_USER_ID VARCHAR2(100);
ALTER TABLE EAM.T_FAULT_FOLLOW ADD DISPATCH_USER_NAME VARCHAR2(100);
ALTER TABLE EAM.T_FAULT_FOLLOW ADD DISPATCH_TIME VARCHAR2(100);

--屏蔽门故障数据表
CREATE TABLE EAM.T_DOOR_FAULT (
	REC_ID VARCHAR2(100) NOT NULL,
	"MONTH" VARCHAR2(100),
	FAULT_NUM NUMBER,
	ACTION_NUM NUMBER,
	REC_CREATOR VARCHAR2(100),
	REC_CREATE_TIME VARCHAR2(100),
	REC_REVISOR VARCHAR2(100),
	REC_REVISE_TIME VARCHAR2(100),
	DELETE_FLAG VARCHAR2(100)
);
COMMENT ON TABLE EAM.T_DOOR_FAULT IS '屏蔽门故障数据表';

ALTER TABLE EAM.T_DOOR_FAULT MODIFY DELETE_FLAG VARCHAR2(100) DEFAULT 0 NOT NULL;

ALTER TABLE EAM.T_FAULT_FOLLOW_REPORT ADD FOLLOW_EXAMINE_USER_ID VARCHAR2(100);
ALTER TABLE EAM.T_FAULT_FOLLOW_REPORT ADD FOLLOW_EXAMINE_USER_NAME VARCHAR2(100);
ALTER TABLE EAM.T_FAULT_FOLLOW_REPORT ADD FOLLOW_EXAMINE_TIME VARCHAR2(100);
ALTER TABLE EAM.T_FAULT_FOLLOW_REPORT ADD FOLLOW_EXAMINE_OPINION VARCHAR2(1000);
ALTER TABLE EAM.T_FAULT_FOLLOW_REPORT ADD FOLLOW_EXAMINE_STATUS VARCHAR2(100);

ALTER TABLE EAM.T_FAULT_FOLLOW_REPORT MODIFY FOLLOW_EXAMINE_STATUS VARCHAR2(2) DEFAULT 0 NOT NULL;
ALTER TABLE EAM.T_FAULT_FOLLOW_REPORT MODIFY EXAMINE_STATUS VARCHAR2(2) DEFAULT 0 NOT NULL;

CREATE TABLE EAM.SYS_COMMON_FUNC (
	ID VARCHAR2(100),
	USER_ID VARCHAR2(100),
	PERMISSION_ID VARCHAR2(100)
);
COMMENT ON TABLE EAM.SYS_COMMON_FUNC IS '常用功能表';
ALTER TABLE EAM.SYS_COMMON_FUNC ADD SORT VARCHAR2(100);

ALTER TABLE EAM.T_EQUIPMENT ADD OTHER_EQUIP_FLAG VARCHAR2(2);

INSERT INTO EAM.SYS_DICT_TYPE
(CODESET_CODE, CODESET_NAME, CODESET_ENAME, GB_CODE, REMARK, REC_CREATOR, REC_CREATE_TIME, REC_REVISOR,
REC_REVISE_TIME, ARCHIVE_FLAG, CODESET_TYPE, CODESET_HIERARCHY, CODESET_URL, PROJECT_NAME, SUB_CODESET_CODE, REF_ID)
VALUES('dm.import.template', '导入模板', 'importTemplate', NULL, NULL, 'admin', '20240510144600', 'admin',
'20240510144600', ' ', ' ', ' ', ' ', 'WZPLAT', ' ', ' ');
INSERT INTO EAM.SYS_DICT
(CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME,
REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE)
VALUES('dm.import.template', 'equipment', '设备台账导入模板', '/eam/importTemplate/设备台账-导入模板.xlsx',
' ', ' ', ' ', '1', 'admin', '20240510144600', ' ', ' ', ' ', 'WZPLAT', ' ');
INSERT INTO EAM.SYS_DICT
(CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME,
REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE)
VALUES('dm.import.template', 'trainMile', '每日列车里程及能耗导入模板', '/eam/importTemplate/每日列车里程及能耗-导入模板.xlsx',
' ', ' ', ' ', '1', 'admin', '20240510144600', ' ', ' ', ' ', 'WZPLAT', ' ');
INSERT INTO EAM.SYS_DICT
(CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME,
REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE)
VALUES('dm.import.template', 'partReplace', '部件更换台账导入模板', '/eam/importTemplate/部件更换台账-导入模板.xlsx',
' ', ' ', ' ', '1', 'admin', '20240510144600', ' ', ' ', ' ', 'WZPLAT', ' ');
INSERT INTO EAM.SYS_DICT
(CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME,
REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE)
VALUES('dm.import.template', 'gearboxChangeOil', '齿轮箱换油台账导入模板', '/eam/importTemplate/齿轮箱换油台账-导入模板.xlsx',
' ', ' ', ' ', '1', 'admin', '20240510144600', ' ', ' ', ' ', 'WZPLAT', ' ');
INSERT INTO EAM.SYS_DICT
(CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME,
REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE)
VALUES('dm.import.template', 'wheelsetLathing', '轮对镟修台账导入模板', '/eam/importTemplate/轮对镟修台账-导入模板.xlsx',
' ', ' ', ' ', '1', 'admin', '20240510144600', ' ', ' ', ' ', 'WZPLAT', ' ');
INSERT INTO EAM.SYS_DICT
(CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME,
REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE)
VALUES('dm.import.template', 'generalSurvey', '普查技改台账导入模板', '/eam/importTemplate/普查与技改台账-导入模板.xlsx',
' ', ' ', ' ', '1', 'admin', '20240510144600', ' ', ' ', ' ', 'WZPLAT', ' ');
INSERT INTO EAM.SYS_DICT
(CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME,
REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE)
VALUES('dm.import.template', 'specialEquip', '特种设备台账导入模板', '/eam/importTemplate/特种设备台账-导入模板.xlsx',
' ', ' ', ' ', '1', 'admin', '20240510144600', ' ', ' ', ' ', 'WZPLAT', ' ');
INSERT INTO EAM.SYS_DICT
(CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME,
REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE)
VALUES('dm.import.template', 'mea', '计量器具台账导入模板', '/eam/importTemplate/计量器具台账-导入模板.xlsx',
' ', ' ', ' ', '1', 'admin', '20240510144600', ' ', ' ', ' ', 'WZPLAT', ' ');
INSERT INTO EAM.SYS_DICT
(CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME,
REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE)
VALUES('dm.import.template', 'otherEquip', '其他设备台账导入模板', '/eam/importTemplate/其他设备台账-导入模板.xlsx',
' ', ' ', ' ', '1', 'admin', '20240510144600', ' ', ' ', ' ', 'WZPLAT', ' ');
INSERT INTO EAM.SYS_DICT
(CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME,
REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE)
VALUES('dm.import.template', 'overhaulTpl', '检修模板导入模板', '/eam/importTemplate/预防性检修模板-导入模板.xlsx',
' ', ' ', ' ', '1', 'admin', '20240510144600', ' ', ' ', ' ', 'WZPLAT', ' ');
INSERT INTO EAM.SYS_DICT
(CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME,
REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE)
VALUES('dm.import.template', 'bom', 'Bom结构导入模板', '/eam/importTemplate/Bom结构.xlsx',
' ', ' ', ' ', '1', 'admin', '20240510144600', ' ', ' ', ' ', 'WZPLAT', ' ');
INSERT INTO EAM.SYS_DICT
(CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME,
REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE)
VALUES('dm.import.template', 'bomTrain', '车辆与Bom关联导入模板', '/eam/importTemplate/车辆与Bom关联关系.xls',
' ', ' ', ' ', '1', 'admin', '20240510144600', ' ', ' ', ' ', 'WZPLAT', ' ');

ALTER TABLE EAM.T_OVERHAUL_ITEM MODIFY DOC_ID NVARCHAR2(1000);

CREATE TABLE EAM.T_EQUIPMENT_CATEGORY_PART (
        REC_ID VARCHAR2(100) NOT NULL,
        MAJOR_CODE VARCHAR2(100),
        MAJOR_NAME VARCHAR2(100),
        SYSTEM_CODE VARCHAR2(100),
        SYSTEM_NAME VARCHAR2(100),
        EQUIP_TYPE_CODE VARCHAR2(100),
        EQUIP_TYPE_NAME VARCHAR2(100),
        PART_NAME VARCHAR2(100),
        QUANTITY NUMBER,
        REC_CREATOR VARCHAR2(100),
        REC_CREATE_TIME VARCHAR2(100),
        REC_REVISOR VARCHAR2(100),
        REC_REVISE_TIME VARCHAR2(100),
        REC_DELETOR VARCHAR2(100),
        REC_DELETE_TIME VARCHAR2(100),
        DELETE_FLAG VARCHAR2(100) DEFAULT 0 NOT NULL
);
COMMENT ON COLUMN EAM.T_EQUIPMENT_CATEGORY_PART.REC_ID IS 'id';
COMMENT ON COLUMN EAM.T_EQUIPMENT_CATEGORY_PART.MAJOR_CODE IS '专业编码';
COMMENT ON COLUMN EAM.T_EQUIPMENT_CATEGORY_PART.MAJOR_NAME IS '专业名称';
COMMENT ON COLUMN EAM.T_EQUIPMENT_CATEGORY_PART.SYSTEM_CODE IS '系统编码';
COMMENT ON COLUMN EAM.T_EQUIPMENT_CATEGORY_PART.SYSTEM_NAME IS '系统名称';
COMMENT ON COLUMN EAM.T_EQUIPMENT_CATEGORY_PART.EQUIP_TYPE_CODE IS '设备分类编码';
COMMENT ON COLUMN EAM.T_EQUIPMENT_CATEGORY_PART.EQUIP_TYPE_NAME IS '设备分类名称';
COMMENT ON COLUMN EAM.T_EQUIPMENT_CATEGORY_PART.PART_NAME IS '部件名称';
COMMENT ON COLUMN EAM.T_EQUIPMENT_CATEGORY_PART.QUANTITY IS '数量';

ALTER TABLE EAM.T_EQUIPMENT_CATEGORY_PART ADD MODULE_NAME VARCHAR2(500);
COMMENT ON COLUMN EAM.T_EQUIPMENT_CATEGORY_PART.MODULE_NAME IS '模块名称';

ALTER TABLE EAM.T_FAULT_ORDER ADD FINISH_OBJECT_CODE VARCHAR2(100);
ALTER TABLE EAM.T_FAULT_ORDER ADD FINISH_OBJECT_NAME VARCHAR2(100);
ALTER TABLE EAM.T_FAULT_ORDER ADD FINISH_MAJOR_CODE VARCHAR2(100);
ALTER TABLE EAM.T_FAULT_ORDER ADD FINISH_SYSTEM_CODE VARCHAR2(100);
ALTER TABLE EAM.T_FAULT_ORDER ADD FINISH_EQUIP_TYPE_CODE VARCHAR2(100);
ALTER TABLE EAM.T_FAULT_ORDER ADD FINISH_POSITION_CODE VARCHAR2(100);
ALTER TABLE EAM.T_FAULT_ORDER ADD FINISH_POSITION2_CODE VARCHAR2(100);
ALTER TABLE EAM.T_FAULT_ORDER ADD FINISH_PART_ID VARCHAR2(100);

ALTER TABLE EAM.T_FAULT_INFO MODIFY OBJECT_NAME NVARCHAR2(500);
ALTER TABLE EAM.T_FAULT_ORDER MODIFY FINISH_OBJECT_NAME VARCHAR2(500);

ALTER TABLE EAM.T_EQUIPMENT_CATEGORY_PART ADD EQUIP_SUBCLASS_NAME VARCHAR2(500);
ALTER TABLE EAM.T_EQUIPMENT_CATEGORY_PART MODIFY EQUIP_TYPE_NAME VARCHAR2(500);
ALTER TABLE EAM.T_EQUIPMENT_CATEGORY_PART MODIFY EQUIP_TYPE_CODE VARCHAR2(500);
ALTER TABLE EAM.T_EQUIPMENT_CATEGORY_PART MODIFY SYSTEM_NAME VARCHAR2(500);
ALTER TABLE EAM.T_EQUIPMENT_CATEGORY_PART MODIFY SYSTEM_CODE VARCHAR2(500);
ALTER TABLE EAM.T_EQUIPMENT_CATEGORY_PART MODIFY MAJOR_NAME VARCHAR2(500);
ALTER TABLE EAM.T_EQUIPMENT_CATEGORY_PART MODIFY MAJOR_CODE VARCHAR2(500);
INSERT INTO EAM.SYS_DICT
		(CODESET_CODE, ITEM_CODE, ITEM_CNAME, ITEM_ENAME, REMARK, ITEM_STATUS, SORT_ID, STATUS, REC_CREATOR, REC_CREATE_TIME, REC_REVISOR, REC_REVISE_TIME, ARCHIVE_FLAG, PROJECT_NAME, SUB_CODESET_CODE)
VALUES('dm.equip.category.subclass', '10', '11', ' ', ' ', ' ', ' ', '1', 'admin', '20181102162935', ' ', ' ', ' ', 'WZPLAT', ' ');
ALTER TABLE EAM.T_EQUIPMENT_CATEGORY_PART MODIFY QUANTITY NUMBER DEFAULT 1 NOT NULL;

ALTER TABLE EAM.T_FAULT_ORDER ADD IS_REPLACE_PART VARCHAR2(10);
