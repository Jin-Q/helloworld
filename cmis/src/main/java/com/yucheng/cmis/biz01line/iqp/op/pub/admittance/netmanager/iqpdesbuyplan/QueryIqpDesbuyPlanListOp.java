package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpdesbuyplan;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpDesbuyPlanListOp extends CMISOperation {


	private final String modelId = "IqpDesbuyPlan";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String net_agr_no = "";
			String mem_cus_id = "";
			String conditionStr = "";
			try {
				net_agr_no = (String)context.getDataValue("net_agr_no");
			} catch (Exception e) {
				throw new Exception("网络编号数据异常，请检查!");
			}
			
			if(context.containsKey("mem_cus_id")){
				mem_cus_id = (String)context.getDataValue("mem_cus_id");
				if(mem_cus_id != null && !"".equals(mem_cus_id)){
					conditionStr = "where net_agr_no='"+net_agr_no+"' and cus_id='"+mem_cus_id+"'";
				}else{
					conditionStr = "where net_agr_no='"+net_agr_no+"' "; 
				}
			}else{
				conditionStr = "where net_agr_no='"+net_agr_no+"' "; 
			}
			int size = 10;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));				
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			//详细信息翻译时调用			
			String[] args=new String[] { "for_manuf"};
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
            SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
            
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(iColl, new String[]{"input_br_id"});
			SInfoUtils.addPrdPopName("IqpMortCatalogMana", iColl, "commo_name", "catalog_no", "catalog_name", "->", connection, dao);
			
			iColl.setName(iColl.getName()+"List");
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
