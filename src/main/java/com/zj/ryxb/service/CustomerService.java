package com.zj.ryxb.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zj.ryxb.dao.BDistrictMapper;
import com.zj.ryxb.dao.CCustomerMapper;
import com.zj.ryxb.model.BBonus;
import com.zj.ryxb.model.BDistrict;
import com.zj.ryxb.model.CCustomer;

@Service
public class CustomerService{

	@Autowired
	CCustomerMapper customerMapper;
	@Autowired
	private BDistrictMapper districtMapper;
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public CCustomer findById(Long id){
		return customerMapper.selectByPrimaryKey(id);
	}

	public CCustomer customerLoginIn(Map<String,Object> map){
		return customerMapper.loginIn(map);
	}
	
	public CCustomer findCustomerByName(String name){
		return customerMapper.findCustomerByName(name);
	}
	
	public int insert(CCustomer record){
		return customerMapper.insert(record);
	}
	
	public int insertSelective(CCustomer record){
		return customerMapper.insertSelective(record);
	}
	
	public int update(CCustomer record){
		return customerMapper.updateByPrimaryKeySelective(record);
	}
	
	/**
	 * 根据父类id获取地区列表
	 */
	public List<BDistrict> selectByPrimaryKey(Long parentId){
		return districtMapper.selectByParentId(parentId);
	}
	/**
	 * 根据map条件分页查询列表
	 * @param map
	 */
	public List<CCustomer> queryListByPage(Map<String,Object> map){
		return customerMapper.queryListByPage(map);
	}
	/**
     * 根据map条件获取数据总条数
    * @Title: findTotalCount 
    * @param map
    * @return
     */
    public int findTotalCount(Map<String,Object> map){
    	return customerMapper.findTotalCount(map);
    }
    
    /**
     * 根据id删除会员——逻辑删除
     * @param id
     * @return
     */
    public int deleteCustomerById(String ids){
    	int m =0;
    	String[] str = ids.split(",");
    	for(int i =0;i<str.length;i++){
    		CCustomer record = new CCustomer();
    		record.setId(Long.valueOf(str[i]));
    		record.setEnabled(0);
    		m = customerMapper.updateByPrimaryKeySelective(record);
    	}
    	return m;
    }
    
    /**
     * 根据会员总充值金额更新会员等级
     * @param customerId 会员id
     * @param totalAmount 总充值金额
     * @return
     */
    public int updateUserLevel(Long customerId,double totalAmount){
    	int m =0;
    	CCustomer record = customerMapper.selectByPrimaryKey(customerId);
    	if(record!=null){
    		if(1000<=totalAmount && totalAmount<2000){
    			record.setUserLevel(2);
    		}else if(2000<=totalAmount && totalAmount<4000){
    			record.setUserLevel(3);
    		}else if(4000<=totalAmount && totalAmount<6000){
    			record.setUserLevel(4);
    		}else if(6000<=totalAmount && totalAmount<8000){
    			record.setUserLevel(5);
    		}else if(8000<=totalAmount && totalAmount<10000){
    			record.setUserLevel(6);
    		}else if(10000<=totalAmount){
    			record.setUserLevel(7);
    		}
    		m = customerMapper.updateByPrimaryKeySelective(record);
    	}
    	return m;
    }
    
    /**
     * 通过detail查询用户列表
     * @param map
     * @return
     */
    public List<CCustomer> selectByOderDetail(Map<String,Object> map){
    	return customerMapper.selectByOderDetail(map);
    }
}
