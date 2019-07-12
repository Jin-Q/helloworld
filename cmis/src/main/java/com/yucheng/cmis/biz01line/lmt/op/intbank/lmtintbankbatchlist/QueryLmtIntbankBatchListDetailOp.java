package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtintbankbatchlist;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtIntbankBatchListDetailOp extends CMISOperation {

	private final String modelId = "LmtIntbankBatchList";

	private final String serno_name = "serno";
	private final String batch_cus_no_name = "batch_cus_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		
		try{
			connection = this.getConnection(context);
			String condition="";
			String serno_value = null;			
			TableModelDAO dao = this.getTableModelDAO(context);
			
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);

			KeyedCollection kColl  = new KeyedCollection("LmtIntbankBatchList");
			
			String batch_cus_no_value = null;
			if(context.containsKey(serno_name)){
				try {
					serno_value = (String)context.getDataValue(serno_name);
				} catch (Exception e) {}
				if(serno_value == null || serno_value.length() == 0)
					throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
				kColl = dao.queryAllDetail(modelId, serno_value, connection);//批量包名单信息
				batch_cus_no_value = (String)kColl.getDataValue(batch_cus_no_name);
				SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
				SInfoUtils.addSOrgName(kColl, new String[]{"input_br_id"});
				this.putDataElement2Context(kColl, context);
			}
			

			
			//获取批量客户编号
			if(context.containsKey(batch_cus_no_name)){
				batch_cus_no_value = (String)context.getDataValue(batch_cus_no_name);
			}

			condition=" where batch_cus_no='"+batch_cus_no_value+"'";
						
			
			//进行分页
			 int size = 15;
			 PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			 IndexedCollection iColl_LmtBatchCorre = dao.queryList("LmtBatchCorre",null,condition,pageInfo,connection);
			 //获取客户相关信息进行页面显示
			 String cus_id = null;
			 KeyedCollection  kColl1 =null;
			 for(int i=0;i<iColl_LmtBatchCorre.size();i++){
				kColl1 = (KeyedCollection)iColl_LmtBatchCorre.get(i);
				cus_id = (String)kColl1.getDataValue("cus_id");
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
				KeyedCollection kColl_cus =service.getCusSameOrgKcoll(cus_id, context, connection);
				String same_org_cnname =(String)kColl_cus.getDataValue("same_org_cnname");
				String same_org_type =(String)kColl_cus.getDataValue("same_org_type");
				String cust_level =(String)kColl_cus.getDataValue("cust_level");
				String assets =(String)kColl_cus.getDataValue("assets");
				String paid_cap_amt =(String)kColl_cus.getDataValue("paid_cap_amt");
				String userid=(String)kColl_cus.getDataValue("input_id");
				String orgid = (String)kColl_cus.getDataValue("input_br_id");
				String input_date = (String)kColl_cus.getDataValue("input_date");
					
				kColl1.addDataField("same_org_cnname", same_org_cnname);
				kColl1.addDataField("same_org_type", same_org_type);
				kColl1.addDataField("cust_level", cust_level);
				kColl1.addDataField("assets", assets);
				kColl1.addDataField("paid_cap_amt", paid_cap_amt);
				kColl1.addDataField("input_id", userid);
				kColl1.addDataField("input_br_id", orgid);
				kColl1.addDataField("input_date", input_date);
				iColl_LmtBatchCorre.setDataElement(kColl1);
			 }
				SInfoUtils.addSOrgName(iColl_LmtBatchCorre, new String[]{"input_br_id"});
				SInfoUtils.addUSerName(iColl_LmtBatchCorre, new String[]{"input_id"});
			    iColl_LmtBatchCorre.setName(iColl_LmtBatchCorre.getName()+"List");
			    this.putDataElement2Context(iColl_LmtBatchCorre, context);
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
