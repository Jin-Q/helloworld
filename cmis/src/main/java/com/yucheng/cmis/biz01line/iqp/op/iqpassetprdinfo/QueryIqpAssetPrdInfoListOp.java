package com.yucheng.cmis.biz01line.iqp.op.iqpassetprdinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpAssetPrdInfoListOp extends CMISOperation {


	/**添加项目金额的值，将其传入到后台并用于返回页面     2014-08-06 邓亚辉*/
	private final String modelId = "IqpAssetPrdInfo";
	private final String iqpassetproappModel = "IqpAssetProApp";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String pro_amt = null;
			String asset_perc = null;
			String asset_scale = null;
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String serno = (String) context.getDataValue("serno");
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			
			if(conditionStr.equals("")){
				conditionStr = " where serno = '"+serno+"'";
			}else{
				conditionStr = conditionStr + " and serno = '"+serno+"'";
			}
			conditionStr = conditionStr+"order by serno desc,prd_id desc";
			
			
			int size = 10;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			IndexedCollection iColl4Show = new IndexedCollection();
			iColl4Show.setName(iColl.getName()+"list");
			for(int i = 0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection) iColl.get(i);
				//将项目金额查出
			    if(serno != null && serno != ""){
			    	IndexedCollection iapaIcoll = dao.queryList(iqpassetproappModel,  " where serno='"+serno+"'", connection);
			    	if(iapaIcoll != null && iapaIcoll.size() > 0){
			    		KeyedCollection kc = (KeyedCollection) iapaIcoll.get(0);
			    		pro_amt=(String) kc.getDataValue("pro_amt");
//			    		context.addDataField("pro_amt", pro_amt);
			    	}
			    }
			    //计算资产规模
//			    KeyedCollection kColl2 = dao.queryFirst(modelId, null, conditionStr, connection);
			    asset_perc=(String) kColl.getDataValue("asset_perc");
			    if(asset_perc != null && pro_amt != null){
			    	 asset_scale = Double.toString((Double.parseDouble(asset_perc) * Double.parseDouble(pro_amt))); 
			    	 kColl.put("asset_scale", asset_scale);
			    }else{
			    	kColl.put("asset_scale", "0.0");
			    }
			    
			    //iColl4Show.addDataElement(kColl);
			    System.out.println("asset_perc:"+asset_perc);
//			    if(kColl2 != null && kColl2.size() >0){
//			    	asset_perc = (String) kColl2.getDataValue("asset_perc");
//				    asset_scale = Double.toString((Double.parseDouble(asset_perc) * Double.parseDouble(pro_amt))); 
//				    
//			    }
			    kColl.put("asset_scale", asset_scale);
			    iColl4Show.addDataElement(kColl);
			}
			//this.putDataElement2Context(iColl4Show, context);
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
