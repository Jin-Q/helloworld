package com.yucheng.cmis.biz01line.arp.op.arpbondreducapp;

import java.sql.Connection;

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

public class QueryArpBondReducAppListOp extends CMISOperation {

	private final String modelId = "ArpBondReducApp";	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}		
			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false)
									+"order by serno desc";

			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			int size = 10;		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));			
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			
			/*** 从明细表中合计，并写入列表icoll ***/
			ArpPubComponent cmisComponent = (ArpPubComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance("ArpPubComponent",context,connection);
			for(int n=0;n<iColl.size();n++){
				KeyedCollection kColl = (KeyedCollection) iColl.get(n);
				KeyedCollection kColl_trans = new KeyedCollection("TransValue");
				kColl_trans.addDataField("serno", kColl.getDataValue("serno"));
				kColl_trans = cmisComponent.delReturnSql("SumBondReducDetail", kColl_trans);				
				if(kColl_trans != null){
					for(int i = 0; i < kColl_trans.size() ; i++){
						kColl.addDataElement(kColl_trans.getDataElement(i));
					}
				}
			}
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SInfoUtils.addSOrgName(iColl, new String[] { "manager_br_id"});
			SInfoUtils.addUSerName(iColl, new String[] { "manager_id"});
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