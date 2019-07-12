package com.yucheng.cmis.biz01line.iqp.dao;

import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.ComponentException;

public class CusManagerDao extends CMISDao{
	/**
	 * 产品配置关联机构设置，遍历iColl中的kColl,根据kColl中的操作参数进行相关才做
	 * @param iColl 产品适用机构iColl
	 * @param prdId 产品编号
	 * @throws ComponentException
	 * @author ws
	 * @throws SQLException 
	 */
	public int insertCusManagerByIColl(String serno, IndexedCollection iColl,String cont_no) throws Exception{

			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String optype = (String)kColl.getDataValue("optType");
				Map<String,String> insertMap = new HashedMap();
				insertMap.put("serno", serno);
				insertMap.put("manager_id", (String)kColl.getDataValue("manager_id"));
				insertMap.put("is_main_manager", (String)kColl.getDataValue("is_main_manager"));
				insertMap.put("ser_rate", (String)kColl.getDataValue("ser_rate"));
				insertMap.put("input_id", (String)this.getContext().getDataValue(PUBConstant.currentUserId));
				insertMap.put("input_org", (String)this.getContext().getDataValue(PUBConstant.organNo));
				insertMap.put("input_date", (String)this.getContext().getDataValue(PUBConstant.OPENDAY));
				/**如果存在数据再做删除*/
				if(!"".equals((String)kColl.getDataValue("manager_id")) && (String)kColl.getDataValue("manager_id")!= null){  
					if(cont_no == null || "".equals(cont_no)){
						int deleteResult = SqlClient.delete("deleteCusManagerByIColl", insertMap, this.getConnection());
					}else{
						int deleteResult = SqlClient.delete("deleteCusManagerByContNo", insertMap, this.getConnection());
					}
					/** 新增操作*/
					if(optype.equals("add") ||optype.equals("")){   
						int insertResult = SqlClient.insert("insertCusManagerByIColl", insertMap, this.getConnection());
					}
				}
			}
		return 1;
	}
	
	
	
}
