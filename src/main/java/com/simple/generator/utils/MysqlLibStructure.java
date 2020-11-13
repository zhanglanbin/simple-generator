package com.simple.generator.utils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.generator.constant.CoulumnsKey;
import com.simple.generator.pojo.ColumnInfo;
import com.simple.generator.pojo.dto.GenerateModelDTO;

/**
 * @author: zhanglanbin
 * @date: Oct 19, 2019 1:19:41 PM
 */
public class MysqlLibStructure {
	
	private static final Logger logger = LoggerFactory.getLogger(MysqlLibStructure.class);
	
	
	private String dataBaseUrl;
	private String username;
	private String password;
	private static final String SQL = "SELECT * FROM ";// 数据库操作
	private String dataBaseName;
	private List<String> ignoreTalbePrefix;
	
	
	/**
	 * @author: zhanglanbin
	 * @date: Oct 19, 2019 1:19:44 PM
	 */
	public MysqlLibStructure(String dataBaseUrl, String username, String password, String dataBaseName, List<String> ignoreTalbePrefix) {
		this.dataBaseUrl = dataBaseUrl;
		this.username = username;
		this.password = password;
		this.dataBaseName = dataBaseName;
		this.ignoreTalbePrefix = ignoreTalbePrefix;
	}
	
	/**
	 * 获取所有表信息
	 * */
	public List<GenerateModelDTO> getGenerateModelDTOList(){
		List<GenerateModelDTO> generateModelDTOList = new ArrayList<>();
		
		Map<String, String> tableNames = getTableNameAndExplain();
		Set<String> names = tableNames.keySet();
		Iterator<String> iter = names.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			String value = tableNames.get(key);
//			System.out.println("Table Name: " + key + ", explain: " + value);
			GenerateModelDTO generateModelDTO = new GenerateModelDTO();
			generateModelDTO.setModelTableName(key);
			
			
			//解析java对象名称
			String objectName = analysisTableNameToObjectName(key);
			generateModelDTO.setModelClassName(objectName);
			
			generateModelDTO.setExplain(value);
			
			List<ColumnInfo> columnInfos = getColumnNames(generateModelDTO);
//			logger.info("cs::{}",cs);
			generateModelDTO.setColumnInfos(columnInfos);
			
			generateModelDTOList.add(generateModelDTO);
		}
		return generateModelDTOList;
	}
	
	
	
	/**
	 * 链接数据库
	 * @author: zhanglanbin
	 * @date: Oct 18, 2019 11:29:42 PM
	 * @param dataBaseUrl 链接地址
	 * @param username 用户名
	 * @param password 密码
	 */
	public Connection getConnection() {
		Connection conn = null;
		try {
//			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dataBaseUrl, username, password);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return conn;
	}

	/**
	 * 关闭数据库连接
	 * 
	 * @param conn
	 */
	public void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("close connection failure", e);
			}
		}
	}
	



	/**
	 * 获取表中所有字段名称
	 * 
	 * @param tableName 表名
	 * @return
	 */
	/**
	 * @author: zhanglanbin
	 * @date: Oct 19, 2019 6:03:47 PM
	 */
	/**
	 * @author: zhanglanbin
	 * @date: Oct 19, 2019 6:03:51 PM
	 */
	public List<ColumnInfo> getColumnNames(GenerateModelDTO generateModelDTO) {
		List<ColumnInfo> columnNames = new ArrayList<>();
		// 与数据库的连接
		Connection conn = getConnection();
		PreparedStatement pStemt = null;
		String tableSql = SQL + generateModelDTO.getModelTableName();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			pStemt = conn.prepareStatement(tableSql);
			stmt = conn.createStatement();
			
//			rs = pStemt.executeQuery("show full columns from " + tableName);
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("SELECT ")
			.append("COLUMN_NAME,")
			.append("ORDINAL_POSITION,")
			.append("IS_NULLABLE,")
			.append("DATA_TYPE,")
			.append("COLUMN_TYPE,")
			.append("COLUMN_KEY,")
			.append("COLUMN_COMMENT")
			.append(" FROM information_schema.columns")
			.append(" WHERE TABLE_SCHEMA=").append("'").append(dataBaseName).append("'")
			.append(" AND TABLE_NAME=").append("'").append(generateModelDTO.getModelTableName()).append("'");
			rs = stmt.executeQuery(sqlBuffer.toString());
			while (rs.next()) {
				ColumnInfo columnInfo = new ColumnInfo();
				
				//列名
				String columnName = rs.getString("COLUMN_NAME");
//				logger.info("列名::{}",columnName);
				columnInfo.setName(columnName);
				
				//解析驼峰
				String javaName = analysisNameToHump(columnName);
				columnInfo.setJavaName(javaName);
				
				//java方法名称, 首字母大写
				columnInfo.setJavaMethodName(GeneratorUtils.firstCharToUpperCase(javaName));
				
				//依次的位置
				String sortPositionStr = rs.getString("ORDINAL_POSITION");
				int sortPosition = null!=sortPositionStr&&!"".equals(sortPositionStr) ? Integer.parseInt(sortPositionStr) : 1;
//				logger.info("依次的位置::{}",sortPosition);
				columnInfo.setSortPosition(sortPosition);
				
				//是否可以为null
				String isNullableStr = rs.getString("IS_NULLABLE");
//				logger.info(CoulumnsNull.getExplain(isNullable));
				boolean isNullable = null!=isNullableStr&&!"".equals(isNullableStr) ? "1".equals(isNullableStr)? true : false : false;
				columnInfo.setNullable(isNullable);
				
				
				//类型
				String dataType = rs.getString("DATA_TYPE");
//				logger.info("类型::{}",dataType);
				columnInfo.setDataType(dataType);
				
				//解析java类型
				Class<?> javaType = analysisDataTypeToJabaType(dataType);
				if(Date.class.getName().equals(javaType.getName())) {
					generateModelDTO.setImportDate(true);
				}
				if(BigDecimal.class.getName().equals(javaType.getName())) {
					generateModelDTO.setImportBigDecimal(true);
				}
				columnInfo.setJavaType(javaType);
				
				columnInfo.setJavaTypeSimpleName(javaType.getSimpleName());
				if(String.class.getName().equals(javaType.getName())) {
					columnInfo.setIsString(true);
				}
				
				
				
				//解析jdbcType
				String jdbcType = analysisDataTypeToJdbcType(dataType);
				columnInfo.setJdbcType(jdbcType);
				
				
				//解析长度
				Map<String, String> map = analysisColumnType(rs.getString("COLUMN_TYPE"));
				String lengthStr = map.get("length");
//				logger.info("长度::{}",length);
				int length = null!=lengthStr && !"".equals(lengthStr) ? Integer.parseInt(lengthStr) : 0;
				columnInfo.setLength(length);
				
				String floatLengthStr = map.get("floatLength");
//				logger.info("浮点长度::{}",floatLength);
				int floatLength = null!=floatLengthStr && !"".equals(floatLengthStr) ? Integer.parseInt(floatLengthStr) : 0;
				columnInfo.setFloatLength(floatLength);
				
				//约束类型
				String constraintType = rs.getString("COLUMN_KEY");
//				logger.info("约束类型::{}",CoulumnsKey.getName(constraintType));
				columnInfo.setConstraintType(constraintType);
				
				//注释
				String columnComment = rs.getString("COLUMN_COMMENT");
//				logger.info("注释::{}",columnComment);
				columnInfo.setExplain(columnComment);
				
				
				if(CoulumnsKey.PRI.getCode().equals(constraintType)) {
					generateModelDTO.setModelPrimaryKeyInfo(columnInfo);
				}
				columnNames.add(columnInfo);
			}
		} catch (SQLException e) {
			logger.error("getColumnNames failure", e);
		} finally {
			if (pStemt != null) {
				try {
					pStemt.close();
				} catch (SQLException e) {
					logger.error("getColumnNames close pstem and connection failure", e);
				}
			}
			
			if(conn!=null) {
				closeConnection(conn);
			}
		}
		return columnNames;
	}


	/**
	 * 获得某表的建表语句
	 * 
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> getTableNameAndExplain(){
		Map<String,String> map = new HashMap<>();
		Connection conn = getConnection();
		ResultSet rs = null;
		ResultSet qrs = null;
		try {
			// 获取数据库的元数据
			DatabaseMetaData db = conn.getMetaData();
			Statement stmt = conn.createStatement();
			rs = db.getTables(dataBaseName, null, null, new String[] { "TABLE" });
			while (rs.next()) {
				String tableName = rs.getString(3);
				qrs = stmt.executeQuery("SHOW CREATE TABLE " + tableName);
				if (qrs != null && qrs.next()) {
					String createDDL = qrs.getString(2);
					String comment = parse(createDDL);
					map.put(tableName, comment);
				}
			}
			return map;
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("getColumnComments close ResultSet and connection failure", e);
				}
			}
			if (qrs != null) {
				try {
					qrs.close();
				} catch (SQLException e) {
					logger.error("getColumnComments close ResultSet and connection failure", e);
				}
			}
			if(conn !=null) {
				closeConnection(conn);
			}
			
		}
		return map;
	}

	/**
	 * 返回注释信息
	 * 
	 * @param all
	 * @return
	 */

	public String parse(String all) {
		if(null==all || "".equals(all)) {
			return "";
		}
		String comment = null;
		int index = all.indexOf("COMMENT='");
		if (index < 0) {
			return "";
		}
		comment = all.substring(index + 9);
		comment = comment.substring(0, comment.length() - 1);
		return comment;
	}
	/**
	 * 解析类型和长度
	 * @return
	 * */
	protected Map<String, String> analysisColumnType(String columnType) {
		Map<String, String> map = new HashMap<>();
		String length = "";
		String floatLength = "";
		if(null!=columnType && !"".equals(columnType) && columnType.indexOf("(")>0) {
			//切割字符串
			String[] strArr = columnType.split("\\(");
			String lengthStr = strArr[1].replace(")", "");
			//解析整长度,浮点长度
			
			if(null!=lengthStr && !"".equals(lengthStr) && lengthStr.indexOf(",")>0) {
				//切割长度
				String[] lenStrArr = lengthStr.split(",");
				length = lenStrArr[0];
				floatLength = lenStrArr[1];
			}else {
				length = lengthStr;
			}
			
		}
		map.put("length", length);
		map.put("floatLength", floatLength);
		return map;
	}
	
	
	protected Class<?> analysisDataTypeToJabaType(String dataType) {
		Class<?> javaType = String.class;
		switch (dataType.toLowerCase()){
			//字符转换
	        case "varchar" :
	        	javaType = String.class;
	        	break;
	        case "char" :
	        	javaType = String.class;
	        	break;
	        case "text" :
	        	javaType = String.class;
	        	break;
	        case "longtext" :
	        	javaType = String.class;
	        	break;
	        	
	        //布尔型转换
	        case "bit" :
	        	javaType = Boolean.class;
	        	break;
	        	
	        //整数转换
	        case "int":
	        	javaType = Integer.class;
	        	break;
	        case "bigint":
	        	javaType = Long.class;
	        	break;
	        
	        //小数转换
	        case "float" :
	        	javaType = Float.class;
	        	break;
	        case "double" :
	        	javaType = Double.class;
	        	break;
	        
	        //字节转换
	        case "tinyint" :
	        	javaType = Byte.class;
	        	break;
	        case "smallint" :
	        	javaType = Short.class;
	        	break;
	        
	        //超长
	        case "decimal" :
	        	javaType = BigDecimal.class;
	        	break;
	        
	        //日期时间类转换
	        case "datetime" :
	        	javaType = Date.class;
	        	break;
	        case "date" :
	        	javaType = Date.class;
	        	break;
	        case "year" :
	        	javaType = Date.class;
	        	break;
	        case "time" :
	        	javaType = Date.class;
	        	break;
	        case "timestamp" :
	        	javaType = Date.class;
	        	break;
	        
	    }
		return javaType;
	}
	
	
	protected String analysisDataTypeToJdbcType(String dataType) {
		String jdbcType = dataType;
		switch (dataType.toLowerCase()){
			case "int":
				jdbcType = "integer";
				break;
			case "bit" :
				jdbcType = "boolean";
				break;
			case "text" :
				jdbcType = "varchar";
				break;
			case "longtext" :
				jdbcType = "longvarchar";
				break;
			case "datetime" :
				jdbcType = "timestamp";
				break;
	        case "date" :
	        	jdbcType = "timestamp";
	        	break;
	        case "year" :
	        	jdbcType = "timestamp";
	        	break;
	        case "time" :
	        	jdbcType = "timestamp";
	        	break;
		}
		return jdbcType.toUpperCase();
	}
	
	/**
	 * 解析表明 转 对象名
	 * */
	protected String analysisTableNameToObjectName(String tableName) {
		String objectName = "";
		//忽略前缀
		if(null!=this.ignoreTalbePrefix && this.ignoreTalbePrefix.size()>0) {
			for(String prefix: this.ignoreTalbePrefix) {
				if(tableName.indexOf(prefix)>-1) {
					tableName = tableName.replace(prefix, "");
				}
			}
		}
		objectName = analysisNameToHump(tableName);
		return objectName.substring(0, 1).toUpperCase() + objectName.substring(1);
	}
	
	/**
	 * 转驼峰
	 * */
	protected String analysisNameToHump(String str) {
		String hump = "";
		if(str.indexOf("_")>0) {
			//切割驼峰命名
			String[] strArr = str.split("_");
			for(String notHump : strArr) {
				hump += notHump.substring(0, 1).toUpperCase() + notHump.substring(1);
			}
		}else {
			hump = str;
		}
		return hump.substring(0, 1).toLowerCase() + hump.substring(1);
	}
}
