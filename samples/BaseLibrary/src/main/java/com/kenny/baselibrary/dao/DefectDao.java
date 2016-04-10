package com.kenny.baselibrary.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kenny.baselibrary.R;
import com.kenny.baselibrary.db.DBHelper;
import com.kenny.baselibrary.entity.DefectModel;
import com.kenny.baselibrary.utils.common.L;
import com.kenny.baselibrary.utils.common.UUIDUtil;
import com.kenny.baselibrary.utils.common.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 缺陷数据访问类--本类中有很多ORM数据库增删改查的案例
 * @author kenny
 * @time 2016/1/22 22:32
 */
public class DefectDao {

	private Context mContext;

	/** 自动化专业*/
	private final String AUTO_TYPE = "2";
	/** 继保专业*/
	private final String RELAY_TYPE = "3";

	public DefectDao(Context context) {
		this.mContext = context;
	}

	/**
	 * 根据ID更新缺陷
	 *
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public int updateDefectById(Map<String,String> obj){
		// 操作结果
		int operateCount = 1;
		if(obj==null){
			return 0;
		}
		String specialityType = obj.get("SPECIALITY_TYPE");
		DBHelper dbHelper;
		SQLiteDatabase db = null;
		try {
			dbHelper = DBHelper.getHelper(mContext);
			db = dbHelper.getWritableDatabase();
			db.beginTransaction();
			String[] args={
					obj.get("DEAL_MAN_UID"),
					obj.get("DEAL_TIME"),
			};
			// 再插入到中间表
			db.execSQL(mContext.getResources().getString(R.string.sql_update_defect_by_id), args);
		} catch (Exception e) {
			--operateCount;
		}
		if(this.AUTO_TYPE.equals(specialityType)){
			// 保存自动化差异信息
			operateCount+=saveAutoInfoByDefectId(db,obj);
		}else if(this.RELAY_TYPE.equals(specialityType)){
			// 保存继保差异信息
			operateCount+=saveRelayInfoByDefectId(db,obj);
		}else{
			operateCount+=1;
		}

		if(operateCount>1){
			db.setTransactionSuccessful();
		}
		db.endTransaction();
		return operateCount;
	}

	/**
	 * 保存自动化差异信息
	 * @param db
	 * @param obj
	 * @return 整数
	 */
	public int saveAutoInfoByDefectId(SQLiteDatabase db,Map<String,String> obj){
		int operateCount = 1;
		String sqlStr = null;
		if(null==obj){
			return 0;
		}
		try {
			// 获取自动化信息列表
			List<HashMap<String, String>> autoList=queryAutoInfoByDefectId(obj.get("ID"));
			if(autoList.size()==0){
				// 执行插入操作
				String[] args={
						UUIDUtil.generateHexUUID(),
						obj.get("ID"),
						obj.get("PREVENT_MEASURE"),
						obj.get("REWARD")
				        };
				sqlStr = mContext.getResources().getString(R.string.sql_insert_autoInfo);
				db.execSQL(sqlStr, args);
			}else if(autoList.size()==1){
				HashMap<String, String> autoMap = autoList.get(0);
			    // 执行更新操作
				String[] args={
						obj.get("PREVENT_MEASURE"),
						obj.get("REWARD"),
						autoMap.get("ID")
				};
				sqlStr = mContext.getResources().getString(R.string.sql_update_autoInfo_by_defectId);
				db.execSQL(sqlStr, args);
			}

		} catch (Exception e) {
			--operateCount;
			L.saveExceptionToFile(mContext, "保存自动化差异信息出错:"+sqlStr, e);
		}
		return operateCount;
	}

	/**
	 * 保存继保差异信息
	 * @param db
	 * @param obj
	 * @return 整数
	 */
	public int saveRelayInfoByDefectId(SQLiteDatabase db,Map<String,String> obj){
		// 操作结果
		int operateCount = 1;
		// 执行SQL字符串
		String sqlStr = null;
		if(null==obj){
			return 0;
		}
		try {
			// 获取继保差异信息列表
			List<HashMap<String, String>> relayList=queryRelayInfoByDefectId(obj.get("ID"));
			if(relayList.size()==0){
				// 执行插入操作
				String[] args={
						UUIDUtil.generateHexUUID(),
						obj.get("ID"),
						obj.get("IS_NEED_MACHINE_PART"),
						obj.get("PROTECT_TIME")
				};
				sqlStr = mContext.getResources().getString(R.string.sql_insert_relayInfo);
				db.execSQL(sqlStr, args);
			}else if(relayList.size()==1){
				HashMap<String, String> relayMap = relayList.get(0);
				// 执行更新操作
				String[] args={
						obj.get("IS_NEED_MACHINE_PART"),
						obj.get("WAIT_TIME"),
						relayMap.get("ID")
				};
				sqlStr = mContext.getResources().getString(R.string.sql_update_relayInfo_by_defectId);
				db.execSQL(sqlStr, args);
			}

		} catch (Exception e) {
			--operateCount;
			L.saveExceptionToFile(mContext, "保存继保差异信息出错:"+sqlStr, e);
		}
		return operateCount;
	}

	/**
	 * 获取记录条数
	 *
	 * @return
	 * @throws Exception
	 */
	public int queryRecordCount() throws Exception {
		int result = 0;
		DBHelper dbHelper = DBHelper.getHelper(mContext);
		List<?> list = dbHelper.getAllModel(DefectModel.class);
		if (!Utility.isEmpty(list)) {
			result = list.size();
		}
		return result;
	}

	/**
	 * 插入一条缺陷记录
	 *
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public int insertDefect(DefectModel obj, String planId) throws Exception {
//		DBHelper dbHelper = DBHelper.getHelper(mContext);
//		SQLiteDatabase db = dbHelper.getWritableDatabase();
//		db.beginTransaction();
//		// 先插入缺陷信息表
//		dbHelper.insertOneModel(DefectModel.class, obj);
//
//		// 再插入到中间表
//		db.execSQL(mContext.getResources().getString(R.string.sql_insert_defect_to_rel), new String[] { UUIDUtil.generateHexUUID(), obj.getDefectId(),
//				planId, CommonHelper.getSystemDate("") });
//		db.setTransactionSuccessful();
//		db.endTransaction();

		return 1;
	}

	/**
	 * 获取所有的缺陷记录
	 *
	 * @return
	 * @throws Exception
	 */
	public List<?> queryAllDefect(){
		try {
			DBHelper dbHelper = DBHelper.getHelper(mContext);
			return dbHelper.getAllModel(DefectModel.class);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 更新一条缺陷记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public int updateDefect(DefectModel obj) throws Exception {
		DBHelper dbHelper = DBHelper.getHelper(mContext);
		return dbHelper.updateOneModel(DefectModel.class, obj);
	}

	/**
	 * 刪除一条缺陷记录
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
//	public boolean deleteDefect(DefectModel obj) throws Exception {
//		DBHelper dbHelper = DBHelper.getHelper(mContext);
//		SQLiteDatabase db = dbHelper.getWritableDatabase();
//		db.beginTransaction();
//		// 先删除缺陷关联的附件数据
//		dbHelper.executeRawSql(AttachmentModel.class, mContext.getResources().getString(R.string.sql_delete_attachment_by_defect_id),
//				new String[] { obj.getDefectId() });
//
//		// 再刪除缺陷关联的计划业务表数据
//		db.execSQL(mContext.getResources().getString(R.string.sql_delete_defect_to_rel), new String[] { obj.getDefectId() });
//
//		// 最后删除缺陷信息数据
//		boolean ret = dbHelper.deleteOneModel(DefectModel.class, obj) > 0 ? true : false;
//
//		db.setTransactionSuccessful();
//		db.endTransaction();
//		db.close();
//		return ret;
//	}

	/**
	 * 根据设备Id查询缺陷列表
	 * 
	 * @param deviceId
	 *            设备Id
	 * @return
	 * @throws Exception
	 */
	public List<DefectModel> queryDefectByDeviceId(String deviceId) throws Exception {
		List<DefectModel> list;
		DBHelper dbHelper = DBHelper.getHelper(mContext);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("DEVICE_ID", deviceId);
		list = (List<DefectModel>) dbHelper.queryByField(DefectModel.class, map);
		return list;
	}
	
	/**
	 * 根据缺陷ID查询缺陷列表
	 * 
	 * @param id
	 *            表单实例Id
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String,String>> queryDefectById(String id){
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		DBHelper dbHelper;
		try {
			dbHelper = DBHelper.getHelper(mContext);
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			Cursor cursor = db.rawQuery(mContext.getResources().getString(R.string.sql_get_defect_by_id), new String[] { id });
			if (null != cursor) {
				while (cursor.moveToNext()) {
					HashMap<String,String> defectMap = new HashMap<String,String>();
					defectMap.put("DEAL_MAN_UID",cursor.getString(cursor.getColumnIndex("DEAL_MAN_UID")));
					defectMap.put("DEAL_TIME",cursor.getString(cursor.getColumnIndex("DEAL_TIME")));
					
					list.add(defectMap);
				}
				if (!cursor.isClosed()) {
					cursor.close();
				}
				if (null != db) {
					db.releaseReference();
				}
			}
		} catch (Exception e) {
			L.e("根据缺陷ID查询缺陷列表查询异常：" + e.getMessage());
		}
		return list;
	}
	
	/**
	 * 根据缺陷ID查询自动化差异信息列表
	 * 
	 * @param id
	 *            缺陷Id
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String,String>> queryAutoInfoByDefectId(String id){
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		DBHelper dbHelper;
		try {
			dbHelper = DBHelper.getHelper(mContext);
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			Cursor cursor = db.rawQuery(mContext.getResources().getString(R.string.sql_get_autoInfo_by_defectId), new String[] { id });
			if (null != cursor) {
				while (cursor.moveToNext()) {
					HashMap<String,String> defectMap = new HashMap<String,String>();
					defectMap.put("ID",cursor.getString(cursor.getColumnIndex("ID")));
					defectMap.put("PROVINCE_CODE",cursor.getString(cursor.getColumnIndex("PROVINCE_CODE")));
					defectMap.put("BUREAU_CODE",cursor.getString(cursor.getColumnIndex("BUREAU_CODE")));
					
					list.add(defectMap);
				}
				if (!cursor.isClosed()) {
					cursor.close();
				}
			}
		} catch (Exception e) {
			L.e("根据缺陷ID查询缺陷列表查询异常："+e.getMessage());
		}
		return list;
	}
	
	/**
	 * 根据缺陷ID查询继保差异信息列表
	 * 
	 * @param id
	 *            缺陷Id
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String,String>> queryRelayInfoByDefectId(String id){
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		DBHelper dbHelper;
		try {
			dbHelper = DBHelper.getHelper(mContext);
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			Cursor cursor = db.rawQuery(mContext.getResources().getString(R.string.sql_get_relayInfo_by_defectId), new String[] { id });
			if (null != cursor) {
				while (cursor.moveToNext()) {
					HashMap<String,String> defectMap = new HashMap<String,String>();
					defectMap.put("ID",cursor.getString(cursor.getColumnIndex("ID")));
					defectMap.put("PROVINCE_CODE",cursor.getString(cursor.getColumnIndex("PROVINCE_CODE")));

					
					list.add(defectMap);
				}
				if (!cursor.isClosed()) {
					cursor.close();
				}
			}
		} catch (Exception e) {
			L.e("根据缺陷ID查询缺陷列表查询异常："+e.getMessage());
		}
		return list;
	}

	/**
	 * 根据设备ID查询历史很新增的数据
	 * 
	 * @param deviceId
	 * @return
	 * @throws Exception
	 */
	public List<String[]> queryAddAndHistoryCountByDeviceId(String deviceId) throws Exception {
		DBHelper dbHelper = DBHelper.getHelper(mContext);
		String sql = String.format(mContext.getResources().getString(R.string.sql_get_add_history_count), deviceId);
		List<String[]> ret = dbHelper.queryRaw(DefectModel.class, sql);
		return ret;
	}
	
	/**
	 * 根据实例ID更新缺陷客户端状态
	 * @param instanceId
	 */
	public int updateDefectClientStateByInstanceId(String instanceId) {
		int ret=0;
		DBHelper dbHelper;
		SQLiteDatabase db;
		String sql=null;
		String[] sqlArgs={instanceId};
		try {
			 dbHelper = DBHelper.getHelper(mContext);
			 db=dbHelper.getWritableDatabase();
			 sql=mContext.getResources().getString(R.string.sql_update_defect_client_state);			 
			 db.execSQL(sql, sqlArgs);			 
		} catch (Exception e) {
			return -1;
		}	
		return ret;
	}
}
