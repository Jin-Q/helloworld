package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqppsalecontgood;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpPsaleContGoodListOp extends CMISOperation {


	private final String modelId = "IqpPsaleContGood";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			 String psale_cont = null;
				
			 try {
				psale_cont = (String)context.getDataValue("psale_cont");
			 } catch (Exception e) {
				throw new Exception("购销合同编号为空!");
			 }

		
			String conditionStr = "where psale_cont='"+psale_cont+"' order by psale_cont desc,commo_name desc";
			
			int size = 5;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List list = new ArrayList();
			list.add("commo_name");
			list.add("qnt");
			list.add("unit_price");
			list.add("total");
			list.add("psale_cont");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			SInfoUtils.addPrdPopName("IqpMortCatalogMana", iColl, "commo_name", "catalog_no", "catalog_name", "->", connection, dao); //翻译上级目录
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);

			
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
