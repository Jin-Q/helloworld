package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpappdesbuyplan;

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
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpAppDesbuyPlanListOp extends CMISOperation {


	private final String modelId = "IqpAppDesbuyPlan";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno = "";
			String mem_cus_id = "";
			String conditionStr = "";
			try {
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {
				throw new Exception("业务流水号数据异常，请检查!");
			}
			
			if(context.containsKey("mem_cus_id")){
				mem_cus_id = (String)context.getDataValue("mem_cus_id");
				if(mem_cus_id != null && !"".equals(mem_cus_id)){
					conditionStr = "where serno='"+serno+"' and cus_id='"+mem_cus_id+"'";
				}else{
					conditionStr = "where serno='"+serno+"' "; 
				}
			}else{
				conditionStr = "where serno='"+serno+"' "; 
			}
			
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			//详细信息翻译时调用			
			String[] args=new String[] { "for_manuf",};
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
            SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(iColl, new String[]{"input_br_id"});
			SInfoUtils.addPrdPopName("IqpMortCatalogMana", iColl, "commo_name", "catalog_no", "catalog_name", "->", connection, dao);
			
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
