package com.yucheng.cmis.biz01line.arp.op.arpcolldebtapp;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.arp.component.ArpPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryArpCollDebtAppListOp extends CMISOperation {


	private final String modelId = "ArpCollDebtApp";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
									+"order by serno desc";
			
		
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			int size = 10;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>();
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			SInfoUtils.addUSerName(iColl, new String[] { "manager_id"});
			SInfoUtils.addSOrgName(iColl, new String[] { "manager_br_id"});

			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[] { "cus_id" };
			String[] fieldName=new String[]{"cus_name"};
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
			/*** 从明细表中合计，并写入列表icoll ***/
			ArpPubComponent cmisComponent = (ArpPubComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance("ArpPubComponent",context,connection);
			for(int n=0;n<iColl.size();n++){
				KeyedCollection kColl = (KeyedCollection) iColl.get(n);
				KeyedCollection kColl_trans = new KeyedCollection("TransValue");
				kColl_trans.addDataField("serno", kColl.getDataValue("serno"));
				kColl_trans = cmisComponent.delReturnSql("SumArpCollDebtRe", kColl_trans);				
				if(kColl_trans != null){
					kColl.addDataField("guar_amt", kColl_trans.getDataValue("results"));
				}
			}
			
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