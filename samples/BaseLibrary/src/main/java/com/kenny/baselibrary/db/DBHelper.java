package com.kenny.baselibrary.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.kenny.baselibrary.R;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * description ORM数据库帮助类
 * Created by kenny on 2016/1/26.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

	/**
	 * 数据库名
	 */
	public static String DATABASE_NAME; /*
										 * android.os.Environment.
										 * getExternalStorageDirectory
										 * ().getAbsolutePath() + File.separator
										 * + "workfrom" + File.separator +
										 * "xxx.db";
										 */

	/**
	 * 数据库版本
	 */
	public static int DATABASE_VERSION = 0;
	private AtomicInteger mOpenCounter = new AtomicInteger();
	/**
	 * 数据库操作实例
	 */
	private static DBHelper mHelper;
	/**
	 * 数据库升级或更新接口
	 */
	public static DatabaseUpdate mDatabaseUpdate;
	/**
	 * db
	 */
	private SQLiteDatabase mDatabase;

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文对象
	 */
	private DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * 获得数据库的操作实例DatabaseHelper
	 * 
	 * @param context
	 *            上下文对象
	 * @param databaseVersion
	 *            　数据库的版本
	 * @param databasePath
	 *            　数据库的存在路径
	 * @param databaseUpdateIPL
	 *            　数据库版本升级是的回调接口
	 * @return DatabaseHelper实例
	 * @throws Exception
	 */
	public static void initDBHelper(Context context, int databaseVersion, String databasePath, DatabaseUpdate databaseUpdateIPL) throws Exception {
		if (mHelper == null) {
			if (databasePath == null || "".equals(databasePath)) {
				throw new Exception(context.getString(R.string.lib_database_no_name_exception2));
			} else if (databaseVersion <= 0) {
				throw new Exception(context.getString(R.string.lib_database_no_version_exception2));
			} else if (databaseUpdateIPL == null) {
				throw new Exception(context.getString(R.string.lib_database_no_ipl_exception2));
			}
			DATABASE_VERSION = databaseVersion;
			DATABASE_NAME = databasePath;
			mDatabaseUpdate = databaseUpdateIPL;
		}
	}

	public synchronized SQLiteDatabase openDatabase() {
        if(mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            mDatabase = mHelper.getWritableDatabase();
        }
        return mDatabase;
    }

	/**
	 * 获得数据库的操作实例DatabaseHelper
	 *
	 * @param context
	 *            上下文对象
	 * @return DatabaseHelper实例
	 * @throws Exception
	 *
	 * @since 当第一次使用之前需要设置数据库的版本、数据库的存在路径、数据库版本升级时的回调接口。 后续可以不用。
	 */
	public static synchronized DBHelper getHelper(Context context) throws Exception {
		if (mHelper == null) {
			if (DATABASE_NAME == null) {
				throw new Exception(context.getString(R.string.lib_database_no_name_exception));
			} else if (DATABASE_VERSION <= 0) {
				throw new Exception(context.getString(R.string.lib_database_no_version_exception));
			} else if (mDatabaseUpdate == null) {
				throw new Exception(context.getString(R.string.lib_database_no_ipl_exception));
			} else {
				mHelper = new DBHelper(context);
			}
		}
		if(!mHelper.isOpen()){
			mHelper.getWritableDatabase();
		}
		return mHelper;
	}

    public synchronized void closeDatabase() {
        if(mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            mDatabase.close();

        }
    }

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		mDatabaseUpdate.onCreate(db, connectionSource);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		mDatabaseUpdate.onUpgrade(db, connectionSource, oldVersion, newVersion);
	}

	public AndroidConnectionSource getSource() throws SQLException {
		if (connectionSource.getReadWriteConnection().isClosed() || !connectionSource.isOpen()) {
			connectionSource = new AndroidConnectionSource(getWritableDatabase());
		}
		return connectionSource;
	}

	/**
	 * 关闭事务处理的连接
	 */
	public void closeSource() {
		if (connectionSource != null) {
			connectionSource.close();
		}
	}

	/**
	 * 获得数据库操作器,如果未连接则自动打开连接
	 * 
	 * @param object
	 * @return
	 * @throws SQLException
	 */
	public Dao getDataDao(Class clazz) throws SQLException {
		if (connectionSource.getReadWriteConnection().isClosed() || !connectionSource.isOpen()) {
			connectionSource = new AndroidConnectionSource(getWritableDatabase());
		}
		return DaoManager.createDao(connectionSource, clazz);
	}

	/**
	 * 关闭数据库，并清空缓存
	 */
	@Override
	public void close() {
		super.close();
	}

	/**
	 * 释放数据库
	 */
	public static void releaseDabaHelper() {

		if (mHelper != null) {
			OpenHelperManager.releaseHelper();
			mHelper = null;
		}
	}

	/**
	 * 插入一条数据
	 * 
	 * @param clazz
	 *            数据所封装的类class对象
	 * @param model
	 *            数据类对象
	 * @throws SQLException
	 */
	public synchronized int insertOneModel(Class clazz, Object model) throws SQLException {
		return getDataDao(clazz).create(model);
	}

	/**
	 * 删除一条数据
	 * 
	 * @param clazz
	 *            数据所封装的类class对象
	 * @param model
	 *            数据类对象
	 * @throws SQLException
	 */
	public synchronized int deleteOneModel(Class clazz, Object model) throws SQLException {
		return getDataDao(clazz).delete(model);
	}

	/**
	 * 删除所有数据
	 * 
	 * @param clazz
	 *            数据所封装的类class对象
	 * @throws SQLException
	 */
	public synchronized void deleteAllModels(Class clazz) throws SQLException {
		Dao dao = getDataDao(clazz);
		dao.delete(dao.deleteBuilder().prepare());
	}

	/**
	 * 更新一条数据
	 * 
	 * @param clazz
	 *            数据所封装的类class对象
	 * @param model
	 *            数据类对象
	 * @throws SQLException
	 */
	public synchronized int updateOneModel(Class clazz, Object model) throws SQLException {
		return getDataDao(clazz).update(model);
	}

	/**
	 * 根据字段查询数据对象
	 * 
	 * @param clazz
	 *            数据所封装的类class对象
	 * @param map
	 *            需要过滤的字段
	 * @throws SQLException
	 */
	public synchronized List queryByField(Class clazz, Map map) throws SQLException {
		return getDataDao(clazz).queryForFieldValues(map);
	}

	/**
	 * 执行sql语句
	 * 
	 * @param clazz
	 *            任何数据封装的类class对象，此参数是为了获得一个dao
	 * @param sql
	 *            所要执行的sql语句sql语句
	 * @param values
	 *            查询条件值
	 * @return
	 * @throws SQLException
	 */
	public synchronized int executeRawSql(Class clazz, String sql, String... values) throws SQLException {
		return getDataDao(clazz).executeRaw(sql, values);
	}

	/**
	 * 判断数据是否存在
	 * 
	 * @param sql
	 *            所要执行的sql语句
	 * @param values
	 *            查询条件值
	 * @return
	 */
	public boolean isExistByArgs(String sql, String[] values) {
		Cursor c = getWritableDatabase().rawQuery(sql, values);
		boolean isExist = c.moveToFirst();
		c.close();
		return isExist;
	}

	/**
	 * 查询class对应的表中的所有数据
	 * 
	 * @param clazz
	 *            所要查询数据封装的类class对象
	 * @return
	 * @throws SQLException
	 */
	public List<Object> getAllModel(Class clazz) throws SQLException {
		return getDataDao(clazz).queryForAll();
	}

	/**
	 * 
	 * @param clazz
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	public Object searchOneModelByArgs(Class clazz, Object model) throws SQLException {
		List<Object> users = getDataDao(clazz).queryForMatchingArgs(model);
		if (users != null && users.size() > 0) {
			return users.get(0);
		}
		return null;

	}

	/**
	 * 查找符合条件的结果集的第一条数据
	 * 
	 * @param clazz
	 *            所要查询数据封装的类class对象
	 * @param conditions
	 *            查询条件
	 * @param values
	 *            查询条件的值
	 * @return 查询的结果
	 * @throws SQLException
	 */
	public Object searchOneModelByArgs(Class clazz, String[] conditions, String[] values) throws SQLException {
		QueryBuilder builder = getDataDao(clazz).queryBuilder();
		Where where = builder.where();
		where.raw("1=1").and();
		for (int i = 0; i < values.length; i++) {
			if (i == values.length - 1) {
				where.eq(conditions[i], values[i]);
			} else {
				where.eq(conditions[i], values[i]).and();
			}
		}
		return builder.queryForFirst();
	}

	/**
	 * 查找符合条件的结果集
	 * 
	 * @param clazz
	 *            所要查询数据封装的类class对象
	 * @param conditions
	 *            查询条件
	 * @param values
	 *            查询条件的值
	 * @return 查询的结果
	 * @throws SQLException
	 */
	public List getModelsByArgs(Class clazz, String[] conditions, String[] values) throws SQLException {
		QueryBuilder builder = getDataDao(clazz).queryBuilder();
		Where where = builder.where();
		where.raw("1=1").and();
		for (int i = 0; i < values.length; i++) {
			if (i == values.length - 1) {
				where.eq(conditions[i], values[i]);
			} else {
				where.eq(conditions[i], values[i]).and();
			}

		}
		return builder.query();
	}
	
	public List getModelsByArgs(Class clazz, String columnName, Object... objects) throws SQLException{
		QueryBuilder builder = getDataDao(clazz).queryBuilder();
		Where where = builder.where();
		where.in(columnName, objects);
		return builder.query();
	}

	/**
	 * 根据sql语句查询所要显示的字段集
	 * 
	 * @param clazz
	 *            　　所要查询数据封装的类class对象
	 * @param sql
	 *            　　　查询语句
	 * @param values
	 *            查询语句中“？”所对应的值
	 * @return
	 * @throws SQLException
	 *             　sql异常
	 * @例子 String sql =
	 *     "select NAME,PASSWORD from PUB_USER where DEPARTMENT_ID = ? and DEPARTMENT_NAME = ?"
	 *     ; try { List<String[]> results = dbHelper.queryRaw(User.class,
	 *     sql,"110","会议室1"); for (String[] strings : results) { Log.i(TAG,
	 *     "name:" + strings[0]); Log.i(TAG, "password:" + strings[1]); } }
	 *     catch (SQLException e) { e.printStackTrace(); }
	 * 
	 */
	public List<String[]> queryRaw(Class clazz, String sql, String... values) throws SQLException {
		Dao dao = getDataDao(clazz);
		GenericRawResults<String[]> rawResults = dao.queryRaw(sql, values);
		return rawResults.getResults();
	}

	/**
	 * 在事务中处理数据
	 * 
	 * @param doInTransaction
	 *            　处理数据的回调接口：数据库的操作写在doInDB()方法里面
	 * @throws SQLException
	 *             　　处事过程中抛出的异常
	 */
	public void doInTransaction(final DoInTransaction doInTransaction) throws SQLException {

		TransactionManager.callInTransaction(connectionSource, new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				doInTransaction.doInDB();
				return null;
			}
		});
	}

	/**
	 * 事务中处理事务的接口
	 * 
	 */
	public interface DoInTransaction {
		/**
		 * 具体要在数据库事务中处理的回调方法
		 */
		void doInDB() throws Exception;
	}

	/**
	 * 数据库更新接口
	 */
	public interface DatabaseUpdate {
		void onCreate(SQLiteDatabase db, ConnectionSource connectionSource);

		void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion);
	}

	public int updateModel(Class clazz, String[] conditions, String[] values, String[] updateColumn, String[] updateValues) throws SQLException {
		UpdateBuilder updateBuilder = getDataDao(clazz).updateBuilder();
		Where where = updateBuilder.where().raw("1=1").and();
		for (int i = 0; i < values.length; i++) {
			if (i == values.length - 1) {
				where.eq(conditions[i], values[i]);
			} else {
				where.eq(conditions[i], values[i]).and();
			}

		}
		for (int i = 0; i < updateColumn.length; i++) {
			updateBuilder.updateColumnValue(updateColumn[i], updateValues[i]);
		}

		updateBuilder.setWhere(where);
		return updateBuilder.update();
	}
	
	public List<Object> queryByOrder(Class clazz,String columnName,boolean ascending) throws Exception{
		Dao dao = getDataDao(clazz);
		QueryBuilder builder = dao.queryBuilder();
		builder.orderBy(columnName, ascending);
		List<Object> datas = dao.query(builder.prepare());
		return datas;
	}
	
	/**
	 * 还原初始值
	 */
	public static void revert(){
		mHelper = null;
		mDatabaseUpdate = null;
		DATABASE_NAME = null;
		DATABASE_VERSION = 0;
	}
}
