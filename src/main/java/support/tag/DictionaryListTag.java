package support.tag;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.zj.ryxb.common.util.CacheUtil;
import com.zj.ryxb.model.BDictionary;
import com.zj.ryxb.service.DictionaryService;

@Controller
public class DictionaryListTag extends BodyTagSupport {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private DictionaryService dictionaryService;
	/**
	 * 父编码CODE值
	 */
	private String kindCode;
	/**
	 * 字典集合别名
	 */
	private String varDictionarylist;
	
	private Iterator<BDictionary> iterator;

	@Override
	public int doStartTag() throws JspException {
		if (varDictionarylist == null || "".equals(varDictionarylist)) {
			varDictionarylist = "dictionaryList"; 
		}
		if (kindCode != null && !"".equals(kindCode)) {
			List<BDictionary> dictionarylists = CacheUtil.getCodelist(kindCode);			
			if (dictionarylists != null &&dictionarylists.size() > 0) {
				iterator = dictionarylists.iterator();
				pageContext.setAttribute(varDictionarylist, iterator.next(), PageContext.PAGE_SCOPE);
				return EVAL_BODY_INCLUDE;
			}
		}
		return SKIP_BODY;
	}


	@Override
	public int doEndTag() throws JspException {
		if (bodyContent != null) {
			try {
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
			} catch (IOException e) {
				throw new JspException(e);
			}
		}
		return EVAL_PAGE;
	}
	
	@Override
	public int doAfterBody() throws JspException {
		if (iterator != null && iterator.hasNext()) {
			pageContext.setAttribute(varDictionarylist, iterator.next(), PageContext.PAGE_SCOPE);
			return 2;
		}
		return SKIP_BODY;
	}

	
	

	/**
	 * @Description: 属性 varDictionarylist 的get方法
	 * @return varDictionarylist
	 */
	public String getVarDictionarylist() {
		return varDictionarylist;
	}

	/**
	 * @Description: 属性 varDictionarylist 的set方法
	 * @param varDictionarylist
	 */
	public void setVarDictionarylist(String varDictionarylist) {
		this.varDictionarylist = varDictionarylist;
	}

	/**
	 * @Description: 属性 iterator 的get方法
	 * @return iterator
	 */
	public Iterator<BDictionary> getIterator() {
		return iterator;
	}

	/**
	 * @Description: 属性 iterator 的set方法
	 * @param iterator
	 */
	public void setIterator(Iterator<BDictionary> iterator) {
		this.iterator = iterator;
	}


	public String getKindCode() {
		return kindCode;
	}


	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

}
