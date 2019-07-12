package com.yucheng.cmis.biz01line.cont.dao;

import java.sql.Connection;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.collections.map.HashedMap;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.util.TableModelUtil;

public class IqpAppendTermsDao extends CMISDao{
	/**
	 * 附加条款tab页，遍历iColl中的kColl,根据kColl中的操作参数进行相关才做
	 * @param iColl 附加条款
	 * @param serno 业务编号
	 * @throws ComponentException
	 */
	public int insertIqpAppendTermsByIColl(String serno, IndexedCollection iColl) throws Exception{
		for(int i=0;i<iColl.size();i++){
			KeyedCollection kColl = (KeyedCollection)iColl.get(i);
			String optype = (String)kColl.getDataValue("optType");
			Map<String,String> insertMap = new HashedMap();
			insertMap.put("serno", serno);
			insertMap.put("fee_code", (String)kColl.getDataValue("fee_code"));
			insertMap.put("fee_desc", (String)kColl.getDataValue("fee_desc"));
			insertMap.put("benmark_amt", (String)kColl.getDataValue("benmark_amt"));
			insertMap.put("calc_express", (String)kColl.getDataValue("calc_express"));
			insertMap.put("fee_type", (String)kColl.getDataValue("fee_type"));
			insertMap.put("fee_amt", (String)kColl.getDataValue("fee_amt"));
			if((String)kColl.getDataValue("fee_code")!=""){  
				int deleteResult = SqlClient.executeUpd("deleteIqpAppendTremsByIColl", insertMap, null, null, this.getConnection());
				/** 新增操作*/
				if("add".equals(optype)||"".equals(optype)){
				     int insertResult = SqlClient.insert("insertIqpAppendTremsByIColl", insertMap, this.getConnection());
				}
			}
		}	
		return 1;
	}
	
	/**更新业务担保合同状态
	 * @param cont_no 合同状态
	 */
	public int updateGrtLoanRGur(String cont_no,DataSource dataSource,Connection connection) throws Exception{
		int i=0;
		try {
			String sql_select =SqlClient.joinQuerySql("updateGrtLoanRGur",cont_no,null);
			SqlClient.deleteBySql(sql_select, connection); 
		} catch (Exception e) {
			throw new Exception("更新业务担保合同状态失败!");
		}
		return i;
	}
}
