DROP TABLE IF EXISTS SP_PD_DEFECT_AUTOMATION; 
CREATE TABLE SP_PD_DEFECT_AUTOMATION (ID VARCHAR2(32) not null, PROVINCE_CODE VARCHAR2(2), BUREAU_CODE VARCHAR2(4), DEFECT_ID VARCHAR2(32) not null,SUBSTATION_TYPE VARCHAR2(20), REMOTE_CONTROL_TYPE NUMBER(1), REMOTE_CONTROL_RANGE NUMBER(1),DEAL_BEGIN_TIME DATE,FACTORY_ANA_ABILITY VARCHAR2(300),FACTORY_DEAL_ABILITY VARCHAR2(300),FACTORY_WORK_QUALITY VARCHAR2(300),FACTORY_SERVICE_QUALITY VARCHAR2(300),FACTORY_SUPPORT_ABILITY VARCHAR2(300),REWARD VARCHAR2(300),PREVENT_MEASURE VARCHAR2(2000),FLOW_STATE NUMBER(2),PROCESS_INS_ID VARCHAR2(40),OPTIMISTIC_LOCK_VERSION NUMBER(12),DATA_FROM VARCHAR2(80),UPDATE_TIME DATE);
DROP TABLE IF EXISTS SP_PD_DEFECT_RELAY; 
CREATE TABLE SP_PD_DEFECT_RELAY (ID VARCHAR2(32) not null,PROVINCE_CODE VARCHAR2(2),BUREAU_CODE VARCHAR2(4),DEFECT_ID VARCHAR2(32) not null,DEVICE_MODEL_SERIES NUMBER(3),PROTECT_EXIT_TIME DATE,PROTECT_RUN_TIME DATE,PROTECT_TIME NUMBER(10,1),IS_NEED_MACHINE_PART NUMBER(1),WAIT_TIME NUMBER(10),FLOW_STATE NUMBER(2),PROCESS_INS_ID VARCHAR2(40),OPTIMISTIC_LOCK_VERSION NUMBER(12),DATA_FROM VARCHAR2(80),UPDATE_TIME DATE);
ALTER TABLE SP_PD_TF_RECORD add WORK_OBJECT_ID VARCHAR2(3200);
ALTER TABLE SP_PD_TF_STANDARD_CONFIRM add WORK_OBJECT_ID VARCHAR2(32);
ALTER TABLE SP_PD_REL_INS_ITEM_FUNCTION add WORK_OBJECT_ID VARCHAR2(32);
ALTER TABLE SP_PD_TF_TABLE add WORK_OBJECT_RULE NUMBER(1);
UPDATE SP_PD_TF_RECORD SET WORK_OBJECT_ID =  (SELECT ID FROM SP_PD_PP_WORK_OBJECT SPPWO WHERE SPPWO.TASK_FORM_RELATION_ID = INSTANCE_ID  AND SPPWO.DEVICE_ID = DEVICE_ID) where WORK_OBJECT_ID  is null;
UPDATE SP_PD_TF_STANDARD_CONFIRM SET WORK_OBJECT_ID =  (SELECT ID FROM SP_PD_PP_WORK_OBJECT SPPWO WHERE SPPWO.DEVICE_ID = DEVICE_ID) where WORK_OBJECT_ID  is null;