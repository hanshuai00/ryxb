package com.zj.ryxb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zj.ryxb.dao.BLoginLogMapper;
import com.zj.ryxb.model.BLoginLog;

@Service
public class LoginLogService{

	@Autowired
	BLoginLogMapper loginLogMapper;

	public int insert(BLoginLog record){
		return loginLogMapper.insert(record);
	}
}
