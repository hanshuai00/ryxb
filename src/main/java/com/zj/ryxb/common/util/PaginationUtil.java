
package com.zj.ryxb.common.util;

import java.io.Serializable;

/** 
 * @ClassName: PaginationUtil 
 * @Description: (分页工具类)  
 */
public class PaginationUtil implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * @Title: getPageInfo 
	 * @Description: (得到分页的HTML代码)
	 * @param total 查询出总数据量
	 * @param pageNo 页码
	 * @param pageRows 每页显示行数
	 * @return
	 */
	public static String getPageInfo(Integer total,Integer pageNo,Integer pageRows){
		StringBuffer buffer=new StringBuffer();
		//默认展示10
		Integer pageCount=4;
		//能分的页数
		Integer pageSum=total%pageRows==0?total/pageRows:total/pageRows+1;
		//当前页码
		Integer page=pageNo==null?1:pageNo;
		//开始的页码
		Integer startPage=page-2>0?page-2:1;
		//结束的页码
		Integer endPage=startPage+pageCount-1<pageSum?startPage+pageCount-1:pageSum;
		
		if(total >0){
			if(page==1){//如果为第一页
				buffer.append("<a class='next_page_left_lp disabled'>&nbsp;</a><a class='next_page_left disabled'>&nbsp;</a>");
			}else{//<li><a href='javascript:;' onclick='_serPrevious();'>上一页</a></li>
				buffer.append("<a class='next_page_left_lp' onclick='_serFirst();' title='第一页'>&nbsp;</a><a class='next_page_left' onclick='_serPrevious();' title='上一页'>&nbsp;</a>");
			}
			for(int i=startPage;i<=endPage;i++){
				if(i==page){
					buffer.append("<a class='current'>").append(i).append("</a>");
				}else{
					buffer.append("<a href='javascript:;' onclick='_serPagination(").append(i).append(");'>").append(i).append("</a>");
				}
			}
			if(page.equals(pageSum) || pageSum==0){//如果为最后一页
				buffer.append("<a class='next_page_right disabled'>&nbsp;</a><a class='next_page_right_lp disabled'>&nbsp;</a>");
			}else{//<li><a href='javascript:;' onclick='_serNext();'>下一页</a></li>
				buffer.append("<a class='next_page_right' onclick='_serNext();'  title='下一页'>&nbsp;</a><a class='next_page_right_lp' onclick='_serLast();'  title='最后一页'>&nbsp;</a>");
			}
			buffer.append("<a class='totalpage' style='display:none'>").append(pageSum).append("</a>");
			
		}
		
		
		return buffer.toString();
	}
}
