package com.kenny.baselibrary.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 缺陷信息实体类--实际项目中抽取
 * 
 * @author kenny
 * 
 */
@DatabaseTable(tableName = "SP_PD_DEFECT")
public class DefectModel implements Serializable {

	private static final long serialVersionUID = 5737218882880829051L;
	/** 缺陷信息ID */
	@DatabaseField(id = true, columnName = "ID", canBeNull = false)
	private String defectId;
	/** 设备ID */
	@DatabaseField(columnName = "DEVICE_ID")
	private String deviceId;
	/** 设备名称 */
	@DatabaseField(columnName = "DEVICE_NAME")
	private String deviceName;
	/** 设备名称全路径 */
	@DatabaseField(columnName = "DEVICE_NAME_FULL_PATH")
	private String deviceNameFullPath;
	/** 缺陷严重等级id */
	@DatabaseField(columnName = "DEFECT_LEVEL")
	private int defectLevelId;
	/** 发现时间 */
	@DatabaseField(columnName = "FIND_TIME")
	private String findTime;
	/** 缺陷表象ID */
	@DatabaseField(columnName = "DEFECT_PHENOMENON_ID")
	private String defectPhenomenonId;
	/** 缺陷表象 */
	@DatabaseField(columnName = "DEFECT_PHENOMENON")
	private String defectPhenomenon;
	/** 缺陷描述 */
	@DatabaseField(columnName = "DEFECT_DESC")
	private String defectDescr;
	/** 缺陷类别id */
	@DatabaseField(columnName = "DEFECT_TYPE_ID")
	private String defectTypeId;
	/** 缺陷类别名称 */
	@DatabaseField(columnName = "DEFECT_TYPE_NAME")
	private String defectTypeName;
	/** 客户端状态(现有新增和历史两种状态) */
	@DatabaseField(columnName = "CLIENT_STATE")
	private String clientState;
	/** 缺陷发现人 */
	@DatabaseField(columnName = "CLIENT_FINDER")
	private String clientFinder;
	/** 发现人ID，填报阶段填写，可修改 */
	@DatabaseField(columnName = "FINDER_UID")
	private String finderUId;
	/** 发现班组ID，填报阶段填写，可修改 */
	@DatabaseField(columnName = "FIND_TEAM_OID")
	private String findTeamOId;
	/** 缺陷发现班组 */
	@DatabaseField(columnName = "CLIENT_TEAM_NAME")
	private String clientTeamName;
	/** 填报人ID，保存时默认取当前用户，不可修改 */
	@DatabaseField(columnName = "CREATOR_UID")
	private String creatorUId;
	/** 填报班组ID，可修改 */
	@DatabaseField(columnName = "TEAM_OID")
	private String teamOId;
	/** 上报人ID，上报时默认取当前用户，不可修改 */
	@DatabaseField(columnName = "REPORTOR_UID")
	private String reportorUId;	
	/** 上报班组ID，上报时默认取当前用户所在班组，不可修改 */
	@DatabaseField(columnName = "REPORT_TEAM_OID")
	private String reportorTeamOId;
	/** 上报时间，上报时默认取当前系统时间，不可修改 */
	@DatabaseField(columnName = "REPORT_TIME")
	private String reportorTime;	
	/** 设备类别Id */
	@DatabaseField(columnName = "CLASSIFY_ID")
	private String classifyId;
	/** 缺陷单状态 */
	@DatabaseField(columnName = "STATE")
	private int state;
	/** 缺陷发现来源（1:巡视;2:监视;3:预试定检;4:检修;5:工程遗留;6:操作;7:维护;8:其它;），单选 */
	@DatabaseField(columnName = "FIND_SOURCE")
	private int findSource;
	/** 缺陷数据来源，0：手动录入；1：缺陷降级；2：安全督察 */
	@DatabaseField(columnName = "DEFECT_SOURCE_TYPE")
	private int defectSourceType;
	/** 电压等级 */
	@DatabaseField(columnName = "VOLTAGE_LEVEL")
	private String voltageLevel;
	/** 地点编号 */
	@DatabaseField(columnName = "SITE_ID")
	private String siteId;
	/** 地点，输变电、继保、自动化缺陷时取值具体到变电站，如"XX站"，配网缺陷取值具体到线路，如"XX站/XX线" */
	@DatabaseField(columnName = "SITE_NAME")
	private String siteName;
	/** 功能位置ID */
	@DatabaseField(columnName = "FUNCTION_LOCATION_ID")
	private String functionLocationId;
	/** 功能位置名称 */
	@DatabaseField(columnName = "FUNCTION_LOCATION_NAME")
	private String functionLocationName;
	/** 是否补登（1：否；2：是） */
	@DatabaseField(columnName = "HAS_RETRO")
	private String hasRetro = "1";
	/** 是否基建工程保修期（1：否；2：是） */
	@DatabaseField(columnName = "IS_PROJECT_PERIOD")
	private String isProjectPeriod = "1";
	
	public int getDefectLevelId() {
		return defectLevelId;
	}

	public void setDefectLevelId(int defectLevelId) {
		this.defectLevelId = defectLevelId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getDefectId() {
		return defectId;
	}

	public void setDefectPhenomenonId(String defectPhenomenonId) {
		this.defectPhenomenonId = defectPhenomenonId;
	}

	public void setDefectPhenomenon(String defectPhenomenon) {
		this.defectPhenomenon = defectPhenomenon;
	}

	public String getDefectPhenomenonId() {
		return defectPhenomenonId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public String getFindTime() {
		return findTime;
	}

	public String getDefectPhenomenon() {
		return defectPhenomenon;
	}

	public String getDefectDescr() {
		return defectDescr;
	}

	public String getDefectTypeId() {
		return defectTypeId;
	}

	public String getDefectTypeName() {
		return defectTypeName;
	}

	public void setDefectTypeName(String defectTypeName) {
		this.defectTypeName = defectTypeName;
	}

	public String getClientState() {
		return clientState;
	}

	public void setClientState(String clientState) {
		this.clientState = clientState;
	}

	public void setDefectId(String defectId) {
		this.defectId = defectId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public void setFindTime(String findTime) {
		this.findTime = findTime;
	}

	public void setDefectDescr(String defectDescr) {
		this.defectDescr = defectDescr;
	}

	public void setDefectTypeId(String defectTypeId) {
		this.defectTypeId = defectTypeId;
	}

	public String getDeviceClassifyId() {
		return classifyId;
	}

	public void setDeviceClassifyId(String classifyId) {
		this.classifyId = classifyId;
	}

	public String getClientFinder() {
		return clientFinder;
	}

	public void setClientFinder(String clientFinder) {
		this.clientFinder = clientFinder;
	}

	public String getClientTeamName() {
		return clientTeamName;
	}

	public void setClientTeamName(String clientTeamName) {
		this.clientTeamName = clientTeamName;
	}

	public int getFindSource() {
		return findSource;
	}

	public void setFindSource(int findSource) {
		this.findSource = findSource;
	}

	public int getDefectSourceType() {
		return defectSourceType;
	}

	public void setDefectSourceType(int defectSourceType) {
		this.defectSourceType = defectSourceType;
	}

	public String getFinderUId() {
		return finderUId;
	}

	public void setFinderUId(String finderUId) {
		this.finderUId = finderUId;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getFunctionLocationId() {
		return functionLocationId;
	}

	public void setFunctionLocationId(String functionLocationId) {
		this.functionLocationId = functionLocationId;
	}

	public String getFunctionLocationName() {
		return functionLocationName;
	}

	public void setFunctionLocationName(String functionLocationName) {
		this.functionLocationName = functionLocationName;
	}

	public String getVoltageLevel() {
		return voltageLevel;
	}

	public void setVoltageLevel(String voltageLevel) {
		this.voltageLevel = voltageLevel;
	}

	public String getFindTeamOId() {
		return findTeamOId;
	}

	public void setFindTeamOId(String findTeamOId) {
		this.findTeamOId = findTeamOId;
	}

	public String getCreatorUId() {
		return creatorUId;
	}

	public void setCreatorUId(String creatorUId) {
		this.creatorUId = creatorUId;
	}

	public String getTeamOId() {
		return teamOId;
	}

	public void setTeamOId(String teamOId) {
		this.teamOId = teamOId;
	}

	public String getHasRetro() {
		return hasRetro;
	}

	public void setHasRetro(String hasRetro) {
		this.hasRetro = hasRetro;
	}

	public String getIsProjectPeriod() {
		return isProjectPeriod;
	}

	public void setIsProjectPeriod(String isProjectPeriod) {
		this.isProjectPeriod = isProjectPeriod;
	}

	public String getDeviceNameFullPath() {
		return deviceNameFullPath;
	}

	public void setDeviceNameFullPath(String deviceNameFullPath) {
		this.deviceNameFullPath = deviceNameFullPath;
	}

	public String getReportorUId() {
		return reportorUId;
	}

	public void setReportorUId(String reportorUId) {
		this.reportorUId = reportorUId;
	}

	public String getReportorTeamOId() {
		return reportorTeamOId;
	}

	public void setReportorTeamOId(String reportorTeamOId) {
		this.reportorTeamOId = reportorTeamOId;
	}	
	public String getReportorTime() {
		return reportorTime;
	}

	public void setReportorTime(String reportorTime) {
		this.reportorTime = reportorTime;
	}

}
