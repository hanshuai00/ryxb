package support.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import support.common.BasicInfo;

public class Tools {
	private static final Logger log = LoggerFactory.getLogger(Tools.class);
	public static final String KEY = "3862FA908FD14742B09D0E24CA762F7F";

	/**
	 * 字符串截取
	 * 
	 * @Description: TODO (这里用一句话描述这个类的作用)
	 * @author BlueBird
	 * @date 2014-11-26 下午3:43:28
	 * @param string
	 * @param length
	 * @return
	 */
	public static final String subStringAuto(String string, int length) {
		if (string == null)
			return "";
		if (string.length() <= length)
			return string;
		return string.substring(0, length - 1);
	}
	/**
	 * 字符串截取(默认10)
	* @Description: TODO (这里用一句话描述这个类的作用)
	* @author BlueBird   
	* @date 2014-11-26 下午4:19:31 
	* @param string
	* @return
	 */
	public static final String subStringAuto(String string) {
		return subStringAuto(string, 10);
	}

	/**
	 * 将null转换为""
	 * 
	 * @param string
	 * @return
	 */
	public static final String null2Str(String string) {
		if (string == null) {
			return "";
		}
		return string;
	}

	/**
	 * 将Double类型的null转换为""
	 * 
	 * @param double1
	 * @return
	 */
	public static final String null2Str(Double double1) {
		if (double1 == null) {
			return "";
		}
		return double1.toString();
	}

	/**
	 * 将字符串转换为Integer类型
	 * 
	 * @param string
	 * @return
	 */
	public static final Integer str2Integer(String string) {
		if (string == null || "".equals(string)) {
			return null;
		}
		return Integer.parseInt(string);
	}

	/**
	 * 将字符串转换为Long类型
	 * 
	 * @param string
	 * @return
	 */
	public static final Long str2Long(String string) {
		if (string == null || "".equals(string)) {
			return null;
		}
		return Long.parseLong(string);
	}

	/**
	 * 将字符串转换为Date类型
	 * 
	 * @param string
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static final Date str2Date(String string, String pattern)
			throws ParseException {
		if (string == null || "".equals(string)) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		Date date = dateFormat.parse(string);
		return date;
	}

	/**
	 * 将Date类型转换为字符串
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static final String date2Str(Date date, String pattern) {
		if (date == null || "".equals(date)) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		String str = dateFormat.format(date);
		return str;
	}

	/**
	 * SHA-256加密
	 * 
	 * @param string
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static final String sha_256(String string)
			throws NoSuchAlgorithmException {
		StringBuffer result = new StringBuffer();
		byte[] bs = string.getBytes();
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(bs);
		String tmp = null;
		for (byte b : messageDigest.digest()) {
			tmp = Integer.toHexString(b & 0xFF);
			if (tmp.length() == 1) {
				result.append("0");
			}
			result.append(tmp);
		}
		return result.toString();
	}

	/**
	 * 获取UUID
	 * 
	 * @return 随机生成的 UUID
	 */
	public static final String getUUID() {
		UUID uuid = UUID.randomUUID();
		String s = uuid.toString();
		return s.replace("-", "");
	}

	/**
	 * 获取以q_为首的查询条件到map中
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final Map<String, String> formQueryToken(
			HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		// 查询所有form中元素.
		Enumeration<String> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			// 得到下一个元素
			String paramName = (String) params.nextElement();
			// 比较paramName的开始
			if (paramName.startsWith("q_")) {
				map.put(paramName.substring(2, paramName.length()), request
						.getParameter(paramName).trim());
			}
		}
		return map;
	}

	/**
	 * 从数据库获取序列
	 * 
	 * @param name
	 * @return
	 */
	public static final long sequences(String name) {
		long sequences = 0;
		ApplicationContext applicationContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(BasicInfo.getRequest()
						.getSession().getServletContext());
		DataSource dataSource = (DataSource) applicationContext
				.getBean("dataSource");
		Connection connection = null;
		Statement statement = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			connection
					.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			connection.setReadOnly(true);
			connection.setAutoCommit(false);
			ResultSet resultSet = statement.executeQuery("select " + name
					+ ".nextval as seq from dual");
			if (resultSet.next()) {
				sequences = resultSet.getLong("seq");
			}
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
			}
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		return sequences;
	}

	/**
	 * 计算文件大小
	 * 
	 * @param file
	 * @return
	 */
	public static final String fileSize(MultipartFile file) {
		return fileSize((CommonsMultipartFile) file);
	}

	/**
	 * 计算文件大小
	 * 
	 * @param file
	 * @return
	 */
	public static final String fileSize(CommonsMultipartFile file) {
		if (file == null) {
			return "0B";
		}
		int size = file.getBytes().length;
		int unit = 1024;
		float dSize = size;
		String un = "B";
		if (dSize > unit) {
			dSize = Math.round(dSize / unit * 100) / 100;
			un = "KB";
			if (dSize > unit) {
				dSize = Math.round(dSize / unit * 100) / 100;
				un = "MB";
			}
		}
		StringBuffer result = new StringBuffer();
		result.append(dSize).append(un);
		return result.toString();
	}

}
