package com.yucheng.cmis.biz01line.iqp.op.pub.oversee.iqpoverseecusinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpOverseeCusinfoListOp extends CMISOperation {


	private final String modelId = "IqpOverseeCusinfo";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			
			//根据年均业务收入按降序排列
			String serno = context.getDataValue("serno").toString();
			if(conditionStr==null||"".equals(conditionStr.trim())){
				conditionStr ="where serno='"+serno+"'" + " order by biz_yearn desc ";
			}else{
				conditionStr +="and serno='"+serno+"'"+ "order by biz_yearn desc";
			}
			
			int size = 15;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);

			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			IndexedCollection ic = new IndexedCollection(modelId);
			if(iColl.size()>5){
				//取前五条数据
				for(int i=0; i<5; i++){
					KeyedCollection kColl = (KeyedCollection)iColl.get(i);
					ic.add(kColl);
				}
				iColl = ic;
			}
			
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
