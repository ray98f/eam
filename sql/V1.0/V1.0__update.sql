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
CREATE TABLE EAM.T_TARIN_MILE (
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
COMMENT ON TABLE EAM.T_TARIN_MILE IS '每日列车里程及能耗表';

ALTER TABLE EAM.T_TARIN_MILE RENAME COLUMN DAILY_MILE TO DAILY_WORK_MILE;
ALTER TABLE EAM.T_TARIN_MILE ADD DAILY_MILE NUMBER;
ALTER TABLE EAM.T_TARIN_MILE ADD REMARK NVARCHAR2(1000);
ALTER TABLE EAM.T_TARIN_MILE MODIFY "DAY" NVARCHAR2(20);

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