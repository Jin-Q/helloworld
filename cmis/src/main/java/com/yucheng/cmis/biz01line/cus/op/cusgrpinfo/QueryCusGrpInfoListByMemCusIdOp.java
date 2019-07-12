package com.yucheng.cmis.biz01line.cus.op.cusgrpinfo;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.group.component.CusGrpInfoComponent;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpInfo;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryCusGrpInfoListByMemCusIdOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cusId = (String)context.getDataValue("CusGrpInfo.cus_id");
            
			//转换类
			ComponentHelper cHelper = new ComponentHelper();
	
		    //构件业务处理类
			CusGrpInfoComponent cusGrpInfoComponent = (CusGrpInfoComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSGRPINFO,context,connection);
			List<CusGrpInfo> rt=new ArrayList<CusGrpInfo>();
			rt = cusGrpInfoComponent.findCusGrpInfoByMemCusId(cusId);
			IndexedCollection iColl = cHelper.domain2icol(rt,"CusGrpInfo",CMISConstance.CMIS_LIST_IND);
			
			String[] args=new String[] { "parent_cus_id" };                                                                       
			String[] modelIds=new String[]{"CusBase"};                                                               
			String[] modelForeign=new String[]{"cus_id"};                                                            
			String[] fieldName=new String[]{"cus_name"};                                                             
			//详细信息翻译时调用			                                                                               
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			SInfoUtils.addSOrgName(iColl, new String[] { "manager_br_id"});
			SInfoUtils.addUSerName(iColl, new String[] { "manager_id" });

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
