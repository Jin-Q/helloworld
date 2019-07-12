package com.yucheng.cmis.biz01line.iqp.op.iqpguarchangeapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpCreditChangeAppComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class AddIqpGuarChangeAppListOp extends CMISOperation {
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			String condition = "";
			try {
				queryData = (KeyedCollection)context.getDataElement("CtrLoanCont");
			} catch (Exception e) {}
			
			condition = TableModelUtil.getQueryCondition_bak("CtrLoanCont", queryData, context, false, true, false);
			
			
			/** 记录集权限 */
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			condition = recordRestrict.judgeQueryRestrict("CtrLoanCont", condition, context, connection);
			int size =15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			//added by yangzy 2015/04/17 需求：XD150325024，集中作业扫描岗权限改造 start
			String currentUserId = "";
			if(context.containsKey("currentUserId")&&context.getDataValue("currentUserId")!=null&&!"".equals(context.getDataValue("currentUserId"))){
				currentUserId = (String) context.getDataValue("currentUserId");
				if(condition!=null&&!"".equals(condition)){
					condition += " and serno in (select serno from cus_manager where manager_id = '"+currentUserId+"' and is_main_manager = '1') ";
				}
			}
			//added by yangzy 2015/04/17 需求：XD150325024，集中作业扫描岗权限改造 end
			IqpCreditChangeAppComponent cmisComponent = (IqpCreditChangeAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPCREDITCHANGEAPPCOMPONENT, context, connection);                        
			IndexedCollection iColl = cmisComponent.getIqpGuarChangeList(pageInfo,condition);
			
			iColl.setName("CtrLoanContList");
			
			String[] args=new String[] {"cus_id","prd_id"};
			String[] modelIds=new String[]{"CusBase","PrdBasicinfo"};
			String[]modelForeign=new String[]{"cus_id","prdid"};
			String[] fieldName=new String[]{"cus_name","prdname"}; 
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
		    SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id"});
		    
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
