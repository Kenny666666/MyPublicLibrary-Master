package com.kenny.baselibrary.utils.xml;

import android.content.Context;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.kenny.baselibrary.utils.common.L;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * xml解析工具类
 */
public class XMLParserUtil {

	/**
	 * 解析xml文件生成实体类
	 * 
	 * @param context
	 *            　 上下文对象
	 * @param instream
	 *            　 所要解析的xml数据流
	 * @return　　　实体类集合
	 * @throws Exception
	 *             异常信息
	 * @since 注意：所生成的实体类要求：对象的成员变量用DatabaseField注解类进行注解，以便根据xml子标签设置相应的属性
	 */
	public static HashMap<String, ArrayList<Object>> parseToClass(
			Context context, InputStream instream) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser paser = factory.newSAXParser();
		XMLParser XMLParser = new XMLParser(context);
		paser.parse(instream, XMLParser);
		instream.close();
		return XMLParser.getResult();
	}

	/**
	 * 解析xml文件生成实体类
	 * 
	 * @param context
	 *            　 上下文对象
	 * @param content
	 *            　 所要解析的xml数据
	 * @return　　 实体类集合
	 * @throws Exception
	 *             异常信息
	 * @since 注意：所生成的实体类要求：对象的成员变量用DatabaseField注解类进行注解，以便根据xml子标签设置相应的属性
	 */
	public static HashMap<String, ArrayList<Object>> parseToClass(
			Context context, String content) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser paser = factory.newSAXParser();
		XMLParser XMLParser = new XMLParser(context);
		InputStream instream = new ByteArrayInputStream(content.getBytes());
		paser.parse(instream, XMLParser);
		instream.close();
		return XMLParser.getResult();
	}

	/**
	 * 将对象信息用xml输出
	 * 
	 * @param context
	 *            　　上下文
	 * @param object
	 *            　　所要转换的对象
	 * @return　xml信息
	 * @throws Exception
	 * 
	 * @since　注意：对转换的对象要求：１、对象要用DatabaseTable注解类进行注解，以便作为xml的根标签
	 *        　　２、对象的成员变量用DatabaseField注解类进行注解，以便生成相应的xml子标签
	 */
	public static String objectToXML(Object object) throws Exception {
		Field[] fields = object.getClass().getDeclaredFields();
		StringBuffer sb = new StringBuffer();
		DatabaseTable classAnno = object.getClass().getAnnotation(
				DatabaseTable.class);
		boolean annotation = classAnno != null ? true : false;
		if (annotation) {
			sb.append("<").append(classAnno.tableName()).append(">");
		} else {
			sb.append("<").append("Element").append(">");
		}

		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			if (annotation) {
				if (!field.isAnnotationPresent(DatabaseField.class)) {// 如果变量不存在注解，则跨过
					continue;
				}
				DatabaseField anno = field.getAnnotation(DatabaseField.class);
				String tag = anno.columnName();
				sb.append("<")
				  .append(tag)
				  .append(">")
				  .append(field.get(object))
				  .append("</")
				  .append(tag)
				  .append(">");
			} else {
				sb.append("<")
				  .append(field.getName())
				  .append(">")
				  .append(field.get(object))
				  .append("</")
				  .append(field.getName())
				  .append(">");
			}
		}
		if (annotation) {
			sb.append("<").append(classAnno.tableName()).append(">");
		} else {
			sb.append("<").append("Element").append(">");
		}
		return sb.toString();
	}

	/**
	 * 将map转换成xml字符串
	 * 
	 * @param map
	 *            　			需要转换的map数据集合
	 * @return　　　xml字符串
	 * @throws Exception
	 */
	public static String mapToXML(Map<String, Object> map) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("<").append("Root").append(">");
		for (Entry<String, Object> entry : map.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof Integer || value instanceof String
					|| value instanceof Boolean || value instanceof Double) {
				String key = entry.getKey();
				sb.append("<")
				  .append(key)
				  .append(">")
				  .append(value)
				  .append("</")
				  .append(key)
				  .append(">");
			} else {
				sb.append(objectToXML(value));
			}

		}
		sb.append("</").append("Root").append(">");
		return sb.toString();

	}

	public static String convertXml(Map<String,String> map){
		if (map == null){
			return "" ;
		}
		String content = "<?xml version= '1.0' encoding='UTF-8' ?>"
				+ "<request>";
		Set<Entry<String,String>> set = map.entrySet();
		Iterator<Entry<String,String>> iterator =  set.iterator();

		while (iterator.hasNext()){
			Map.Entry<String,String> entry= iterator.next();
			content += "<" + entry.getKey()+">" + entry.getValue() + "</"+entry.getKey()+">";
		}
		content += "</request>";

		return  content;

	}
	/**
	 * 解析器
	 * 
	 * @author zhanghaoxin
	 * 
	 */
	private static class XMLParser extends DefaultHandler {

		/**
		 * 当前解析到的标签
		 */
		private String tagName = null;

		/**
		 * 当前解析到的表名
		 */
		private String curTable = null;

		/**
		 * 根据property信息获得当前解析所相关的class类对象
		 */
		private Class<? extends Object> clazz;

		/**
		 * 当前class类对象的相关成员变量信息集合
		 */
		private ArrayList<FieldModel> fieldModels;

		/**
		 * 当前class类对象实例
		 */
		private Object object;

		/**
		 * 表跟实体类之间的对应关系配置信息类
		 */
		private static Properties prop;

		/**
		 * 代表是否需要对prop类进行初始化的标志位
		 */
		private static boolean needLoad = true;

		/**
		 * 解析后的结果集
		 */
		private HashMap<String, ArrayList<Object>> results;

		public XMLParser(Context context) {
			if (needLoad) {// 判断是否需要初始化prop对象
				prop = new Properties();
				try {
					InputStream in = context.getAssets().open(
							"TableToClass.properties");
					prop.load(in);
				} catch (IOException e) {
					e.printStackTrace();
					XMLParser.needLoad = true;
				}
				XMLParser.needLoad = false;
			}
		}

		@Override
		public void startDocument() throws SAXException {
			super.startDocument();

			results = new HashMap<String, ArrayList<Object>>();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);

			tagName = localName;

			// 根据property设置好的对应信息生成类
			String className = prop.getProperty(localName);
			if (className != null && !"".equals(className)) {
				curTable = localName;// 保存当前表名
				try {
					clazz = Class.forName(className);// 生成class文件
					object = clazz.newInstance();// 实例化对象
					init(clazz);// 初始化类成员变量信息，并将注解字段保存起来
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			super.characters(ch, start, length);
			if (tagName != null) {
				String data = new String(ch, start, length);
				try {
					if (object != null) {
						// 通过反射给当前对象object实例的成员变量赋值
						for (FieldModel field : fieldModels) {
							if (tagName.equalsIgnoreCase(field.tag)) {// 判断tagName是否与当前的成员变量的注解对应上
								// 根据当前类成员变量的类型，通过反射对object实例赋值
								if ("String".equals(field.type)) {
									field.field.set(object, data);
								} else if ("int".equals(field.type)) {
									field.field.set(object,
											Integer.parseInt(data));
								} else if ("double".equals(field.type)) {
									field.field.set(object,
											Double.parseDouble(data));
								} else if("long".equals(field.type)){
									field.field.set(object,
											Long.parseLong(data));
								} else if("float".equals(field.type)){
									field.field.set(object,
											Float.parseFloat(data));
								}
							}

						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					L.e("XMLParserUtil XMLParser", "parser:<" + tagName
							+ ">error");
				}
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			super.endElement(uri, localName, qName);
			tagName = null;
			if (curTable.equals(localName)) {// 判断当前表名是否解析完毕
				if (object != null) {
					ArrayList<Object> list = results.get(curTable);// 取出之前保存过的数据
					if (list != null) {// 如果之前已经保存了数据，则继续添加
						list.add(object);
					} else {// 之前没有数据则新建集合保存
						ArrayList<Object> subList = new ArrayList<Object>();
						subList.add(object);
						results.put(curTable, subList);
					}

				}

			}
		}

		/**
		 * 获得解析后的结果集
		 * 
		 * @return
		 */
		public HashMap<String, ArrayList<Object>> getResult() {
			return results;
		}

		/**
		 * 初始化类成员变量信息，并将注解字段保存起来
		 * 
		 * @param clazz
		 */
		private void init(Class<? extends Object> clazz) {
			Field[] fields = clazz.getDeclaredFields();
			fieldModels = new ArrayList<FieldModel>();// 存放成员变量信息的集合

			for (int i = 0; i < fields.length; i++) {
				if (!fields[i].isAnnotationPresent(DatabaseField.class)) {// 如果变量不存在注解，则跨过
					continue;
				}
				DatabaseField anno = fields[i]
						.getAnnotation(DatabaseField.class);
				FieldModel model = new FieldModel();
				model.field = fields[i];
				model.field.setAccessible(true);
				model.type = model.field.getType().getSimpleName();// 保存变量类型
				model.tag = anno.columnName();// 保存注解信息（注解代表该成员变量在数据库中所对应的字段名）
				fieldModels.add(model);
			}
		}

		private class FieldModel {
			/**
			 * 成员变量
			 */
			public Field field;
			/**
			 * 成员变量类型
			 */
			public String type;

			/**
			 * 成员变量所对数据库的字段名
			 */
			public String tag;
		}
	}

}
