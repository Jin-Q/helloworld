package com.yucheng.cmis.biz01line.iqp.drfpo.op.dpodrfpomana;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class IntroDpoDrfpoManaListOp extends CMISOperation {


	private final String modelId = "IqpDrfpoMana";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
	//	String cus_id="";
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
				
			} catch (Exception e) {}
			
		//	cus_id=(String) context.getDataValue("cus_id");
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
									+"";
			
			
			//RecordRestrict recordRestrict = this.getRecordRestrict(context);
			//conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			//拼接where条件，以客户为依据对票据池进行过滤
		//	conditionStr += "and cus_id = '"+cus_id+"'";
			/**modified by yangzy 2015/04/28 需求：XD150325024，集中作业扫描岗权限改造 begin **/
			if("".equals(conditionStr)){
				conditionStr += " where status = '01'";//必须为有效的票据池
			}else{
				conditionStr+=" and status  = '01'";
			}
			/**modified by yangzy 2015/04/28 需求：XD150325024，集中作业扫描岗权限改造 end **/
			int size = 10;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>();
			list.add("drfpo_no");
			list.add("cus_id");
			list.add("drfpo_type");
			list.add("status");
			list.add("bill_amt");
			list.add("input_id");
			list.add("input_br_id");
			list.add("input_date");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			SInfoUtils.addSOrgName(iColl, new String[] { "input_br_id"});
			SInfoUtils.addUSerName(iColl, new String[] { "input_id"});
			
			
			//客户名翻译
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
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
