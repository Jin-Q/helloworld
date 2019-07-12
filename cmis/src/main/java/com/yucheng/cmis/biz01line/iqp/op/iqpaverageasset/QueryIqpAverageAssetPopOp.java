package com.yucheng.cmis.biz01line.iqp.op.iqpaverageasset;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.iqp.component.IqpAverageAssetComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpAverageAssetPopOp extends CMISOperation {

	private final String modelId = "IqpAverageAsset";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			IndexedCollection iColl = new IndexedCollection();
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String conditionStr = "";
		    if(queryData != null){
		    	String bill_no = (String)queryData.getDataValue("bill_no");
		    	String cont_no = (String)queryData.getDataValue("cont_no");
		    	String average_status = (String)queryData.getDataValue("average_status");
		    	String cus_id = (String)queryData.getDataValue("cus_id");
		    	if((bill_no != null && !"".equals(bill_no))||(cont_no != null && !"".equals(cont_no))||(average_status != null && !"".equals(average_status))||(cus_id != null && !"".equals(cus_id))){
		    		conditionStr = "where d.bill_no='"+bill_no+"' or d.cont_no='"+cont_no+"' or d.cus_id='"+cus_id+"' or average_status='"+average_status+"'";	
		    	}
		    }
			//String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
		
			int size = 15;
		    
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			IqpAverageAssetComponent iqpAverageAssetComponent = (IqpAverageAssetComponent)CMISComponentFactory.getComponentFactoryInstance()
			                                                     .getComponentInstance("IqpAverageAssetComponent", context, connection);
			iColl = iqpAverageAssetComponent.getIqpAverageAssetListPop(conditionStr, pageInfo, dataSource);
		    
			iColl.setName("IqpAverageAssetList");
			String[] args=new String[] {"cus_id","prd_id","repay_type" };
			String[] modelIds=new String[]{"CusBase","PrdBasicinfo","PrdRepayMode"};
			String[]modelForeign=new String[]{"cus_id","prdid","repay_mode_id"};
			String[] fieldName=new String[]{"cus_name","prdname","repay_mode_dec"};
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id","fina_br_id"});
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
