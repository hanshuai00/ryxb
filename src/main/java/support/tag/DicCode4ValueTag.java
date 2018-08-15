package support.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.zj.ryxb.common.util.CacheUtil;

/**
 * Lable标签——list内容转换为Label方式显示
 * @author zxf
 */
public class DicCode4ValueTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 父类code值（数据类型）
	 */
	private String kindCode;
	/**
	 * 子类code值（数据值）
	 */
	private String dicCode;


	@Override
	public int doEndTag() throws JspException {	 		
		if (null == kindCode || "".equals(kindCode) || null == dicCode || "".equals(dicCode)) {
			return EVAL_PAGE;
		}
		String dicValue = CacheUtil.getValueByCode(kindCode, dicCode);	
		JspWriter out = pageContext.getOut();
		if (dicValue != null) {
			try {
				out.print(dicValue);
			} catch (IOException e) {
				throw new JspException(e);
			}
		}
		return EVAL_PAGE;
	}
	
	
	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

	/**
	 * @param dicCode the dicCode to set
	 */
	public void setDicCode(String dicCode) {
		this.dicCode = dicCode;
	}


}
