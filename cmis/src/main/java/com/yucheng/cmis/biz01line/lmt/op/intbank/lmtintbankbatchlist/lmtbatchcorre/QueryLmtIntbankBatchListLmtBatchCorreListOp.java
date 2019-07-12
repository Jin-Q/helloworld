package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtintbankbatchlist.lmtbatchcorre;

import java.sql.Connection;
import java.util.HashMap;

import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.util.TableModelUtil;	
import com.ecc.emp.dbmodel.service.RecordRestrict;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.lmt.component.intbank.LmtIntbankComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryLmtIntbankBatchListLmtBatchCorreListOp extends CMISOperation {
	
	private final String modelId = "LmtBatchCorre";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String batch_cus_no_value = null;
		try{
			connection = this.getConnection(context);
	
			if(context.containsKey("cus_id")){ 
				String cus_id = (String)context.getDataValue("cus_id");
				batch_cus_no_value =(String)context.getDataValue("batch_cus_no");
				Map<String,String> map = new HashMap<String,String>();
				map.put("batch_cus_no", batch_cus_no_value);
				map.put("cus_id", cus_id);
				
				LmtIntbankComponent libc = (LmtIntbankComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtIntbank", context, connection);
				libc.insertcus2batchlist(map);
			}								
			String conditionStr = "where batch_cus_no = '" + batch_cus_no_value+"' order by batch_cus_no desc";
			//记录级权限
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
		    int size = 10;
		    PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));		
		    TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
            String cus_id = null;
		    IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
		    KeyedCollection  kColl =null;
		    for(int i=0;i<iColl.size();i++){
				 kColl = (KeyedCollection)iColl.get(i);
				 cus_id = (String)kColl.getDataValue("cus_id");
				 String condition = "where cus_id='"+cus_id+"'";
				 String userid=(String)context.getDataValue("currentUserId");
				 String orgid = (String)context.getDataValue("organNo");
				 KeyedCollection kColl_cus =dao.queryFirst("CusSameOrg", null, condition, connection);
				 String same_org_cnname =(String)kColl_cus.getDataValue("same_org_cnname");
				 String same_org_type =(String)kColl_cus.getDataValue("same_org_type");
				 String cust_level =(String)kColl_cus.getDataValue("cust_level");
				 String assets =(String)kColl_cus.getDataValue("assets");
				 String paid_cap_amt =(String)kColl_cus.getDataValue("paid_cap_amt");
					
				 kColl.addDataField("same_org_cnname", same_org_cnname);
				 kColl.addDataField("same_org_type", same_org_type);
				 kColl.addDataField("cust_level", cust_level);
				 kColl.addDataField("assets", assets);
				 kColl.addDataField("paid_cap_amt", paid_cap_amt);
				 kColl.addDataField("input_id", userid);
				 kColl.addDataField("input_br_id", orgid);				
				 iColl.setDataElement(kColl);
		 }

		SInfoUtils.addSOrgName(iColl, new String[]{"input_br_id"});
		SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
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
