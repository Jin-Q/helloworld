package com.yucheng.cmis.biz01line.lmt.dao.LmtGuar;

import java.sql.Connection;
import java.util.Map;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.DaoException;

public class LmtGuarDao extends CMISDao {
	
	/**
	 * @return guar_no 担保品编号 以逗号隔开的字符串
	 * @param serno 流水号 当前进行授信的流水号
	 * @throws DaoException
	 * **/
	
	public String queryGuarNo(String serno) throws DaoException{
		IndexedCollection iColl = new IndexedCollection();
		String guar_no = "";
		Connection connection =null;
		try{
			connection = this.getConnection();		
			iColl = (IndexedCollection)SqlClient.queryList4IColl("queryGuarNo", serno, connection);				
			for(int i=0;i<iColl.size();i++)
			{
				String no ="";
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				no ="'"+ kColl.getDataValue("guaranty_no").toString();		
				guar_no += no+"',";
			}		
		}catch(Exception e){
			throw new DaoException("获取担保编号失败"+e.getMessage());
		}
		return guar_no;		
	}
	/**
	 * @return cud_id 保证人客户码
	 * @param serno 关系表中的流水号
	 * @throws DaoException
	 * @根据serno从关系表r_lmt_guarntr_info 中获取符合条件的cud_id
	 * */
	public String queryCusId(String serno) throws DaoException{
		IndexedCollection iColl = new IndexedCollection();
		String cus_id = "";
		Connection connection =null;
		try{
			connection = this.getConnection();		
			iColl = (IndexedCollection)SqlClient.queryList4IColl("queryGuarntrNo", serno, connection);				
			for(int i=0;i<iColl.size();i++)
			{
				String no ="";
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				no = "'"+kColl.getDataValue("guar_id").toString();		
				cus_id += no+"',";
			}		
		}catch(Exception e){
			throw new DaoException("获取保证人失败"+e.getMessage());
		}
		return cus_id;		
	}
	
	/**
	 * 修改授信担保合同关系表中的担保等级
	 * @return 修改结果
	 * @throws Exception
	 */
	public int updateRLmtGuarLvlYB(Map<String, String> map,Connection connection) throws Exception{
		int result = SqlClient.update("updateRLmtGuarLvlYB", map, null, null, connection);
		return result;  
	}
	/**
	 * 修改授信担保合同关系表中的担保等级
	 * @return 修改结果
	 * @throws Exception
	 */
	public int updateRLmtGuarLvlZGE(Map<String, String> map,Connection connection) throws Exception{
		int result = SqlClient.update("updateRLmtGuarLvlZGE", map, null, null, connection);
		return result;    
	}

}
