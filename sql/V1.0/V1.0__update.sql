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

