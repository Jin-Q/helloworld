package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import com.dc.eai.data.FieldType;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

import com.dc.eai.data.CompositeData;
import com.dc.eai.data.FieldType;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class QueryPvpLoanAppHistoryListOp extends CMISOperation {


	private final String modelId = "PvpLoanApp";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String conditionStr ="";
			KeyedCollection queryData = null;
			String biz_type = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			/**取得业务模式*/
			if(context.containsKey("biz_type")){
				biz_type = (String)context.getDataValue("biz_type");  
			}
		
			if(context.containsKey("menuId")){
				context.put("menuId", context.getDataValue("menuId"));
			}
			conditionStr = TableModelUtil.getQueryCondition_bak(modelId, queryData, context, false, true, false);
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			
//			if(conditionStr !=null && !(conditionStr.equals(""))){
//				conditionStr += " order by serno desc,cont_no desc";
//			}else{
//				conditionStr = "where prd_id not in ('300022','300023','300024','600020') and cont_no in (select b.cont_no from ctr_loan_cont b where b.biz_type='"+biz_type+"') order by serno,cont_no desc";
//			} 
			conditionStr += " order by input_date desc,serno desc,cont_no desc";
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,pageInfo,connection);
			
			
			String[] args=new String[] { "cus_id","cont_no","prd_id"};
			String[] modelIds=new String[]{"CusBase","CtrLoanCont","PrdBasicinfo"};
			String[] modelForeign=new String[]{"cus_id","cont_no","prdid"};
			String[] fieldName=new String[]{"cus_name","serno","prdname"};
			String[] resultName = new String[] { "cus_id_displayname","fount_serno","prd_id_displayname"};
			SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
		    /** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});  
			SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id","in_acct_br_id"});  
		
			
			KeyedCollection authKColl = dao.queryDetail("PvpAuthorize", "SQ50100120161122054904", connection);
			KeyedCollection reflectKColl = TagUtil.getReflectKColl(authKColl);
			//CompositeData bodyCD = new CompositeData();
			
			//bodyCD.addField("GUARANTEE_RECO_AMT", TagUtil.getEMPField(reflectKColl.getDataValue("GUARANTEE_RECO_AMT"), FieldType.FIELD_DOUBLE, 20, 2));
			
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
