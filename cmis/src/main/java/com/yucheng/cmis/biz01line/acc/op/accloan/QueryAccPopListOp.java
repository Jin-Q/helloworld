package com.yucheng.cmis.biz01line.acc.op.accloan;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.biz01line.acc.accPub.AccConstant;
import com.yucheng.cmis.biz01line.acc.component.AccComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryAccPopListOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cus_id = "";
			try{
				cus_id = (String)context.getDataValue("cus_id");
			}catch(Exception e){ 
				throw new Exception("客户码为空，请联系后台管理员!");
			} 
			String openDay = (String)context.getDataValue("OPENDAY");
			AccComponent cmisComponent = (AccComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AccConstant.ACCCOMPONENT, context, connection);
			IndexedCollection iColl = cmisComponent.getAccPop(cus_id,openDay, connection);
			
			iColl.setName("AccPopList"); 
			
			String[] args=new String[] {"cus_id","prd_id" };
			String[] modelIds=new String[]{"CusBase","PrdBasicinfo"};
			String[]modelForeign=new String[]{"cus_id","prdid"};
			String[] fieldName=new String[]{"cus_name","prdname"};
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			SInfoUtils.addSOrgName(iColl, new String[]{"fina_br_id"});  
			this.putDataElement2Context(iColl, context);

			
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
