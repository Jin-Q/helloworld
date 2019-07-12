package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqppsalecont;

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

public class QueryIqpPsaleContListOp extends CMISOperation {


	private final String modelId = "IqpPsaleCont";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);		
//			KeyedCollection queryData = null;
//			try {
//				queryData = (KeyedCollection)context.getDataElement(this.modelId);
//			} catch (Exception e) {}
//			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
//			String net_agr_no=(String)context.getDataValue("net_agr_no");//网络协议编号
//			if(context.containsKey("mem_manuf_type")&&null!=context.getDataValue("mem_manuf_type")&&!"".equals(context.getDataValue("mem_manuf_type"))){
//				
//				String mem_manuf_type=(String)context.getDataValue("mem_manuf_type");// 01为供应商  02 为经销商
//				String mem_cus_id =(String)context.getDataValue("mem_cus_id");
//				if("01".equals(mem_manuf_type)){
//					conditionStr = conditionStr+"where barg_cus_id='"+mem_cus_id+"' and net_agr_no='"+net_agr_no+"' order by psale_cont desc";
//				}else{
//					conditionStr = conditionStr+"where buyer_cus_id='"+mem_cus_id+"'and net_agr_no='"+net_agr_no+"' order by psale_cont desc";
//				}	
//			}else{
//				conditionStr=conditionStr+"where net_agr_no='"+net_agr_no+"' order by psale_cont desc";
//			}
			String net_agr_no = "";
			String mem_cus_id = "";
			String conditionStr = "";
			try {
				net_agr_no = (String)context.getDataValue("net_agr_no");
			} catch (Exception e) {
				throw new Exception("业务流水号数据异常，请检查!");
			}
			
			if(context.containsKey("mem_cus_id")){
				mem_cus_id = (String)context.getDataValue("mem_cus_id");
				if(mem_cus_id != null && !"".equals(mem_cus_id)){
					conditionStr = "where net_agr_no='"+net_agr_no+"' and mem_cus_id='"+mem_cus_id+"'";
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
			iColl.setName(iColl.getName()+"List");
			//详细信息翻译时调用			
			String[] args=new String[] { "mem_cus_id","buyer_cus_id","barg_cus_id"};
			String[] modelIds=new String[]{"CusBase","CusBase","CusBase"};
			String[] modelForeign=new String[]{"cus_id","cus_id","cus_id"};
			String[] fieldName=new String[]{"cus_name","cus_name","cus_name"};
            SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(iColl, new String[]{"input_br_id"});
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
