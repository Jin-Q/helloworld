package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpnetmaginfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.RestrictUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpNetMagInfoListOp extends CMISOperation {


	private final String modelId = "IqpNetMagInfo";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};

			KeyedCollection queryData = null;
			String cus_id ="";
			String net_agr_no_str ="";
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, false, false);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			
			
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			
			if(context.containsKey("cus_id")){
				cus_id = (String)context.getDataValue("cus_id");
				String biz_type = (String)context.getDataValue("biz_type");
				String condit = "where mem_cus_id='"+cus_id+"' and status='1'";
				IndexedCollection iCollMem = dao.queryList("IqpMemMana", condit, connection);
				for(int i=0;i<iCollMem.size();i++){
					KeyedCollection kColl = (KeyedCollection)iCollMem.get(i);
					String net_agr_no = (String)kColl.getDataValue("net_agr_no");
					String lmt_type = (String)kColl.getDataValue("lmt_type");//授信业务品种
					//如果授信业务品种中不包含业务模式
					if(lmt_type.contains(biz_type)){
						if(i==(iCollMem.size()-1)){
							net_agr_no_str +="'"+net_agr_no+"'";
						}else{
							net_agr_no_str +="'"+net_agr_no+"',";
						}
					}
				}
				if("".equals(net_agr_no_str)){
					net_agr_no_str ="''";
				}
				if("".equals(conditionStr)){
					conditionStr = "where net_agr_no in ("+net_agr_no_str+") order by net_agr_no desc";
				}else{
					conditionStr += "and net_agr_no in ("+net_agr_no_str+") order by net_agr_no desc";
				}
			}else{
				if("".equals(conditionStr)){
					conditionStr = " order by net_agr_no desc";
				}else{
					conditionStr += " order by net_agr_no desc";
				}
			}
			
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			//详细信息翻译时调用			
            SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			iColl.setName(iColl.getName()+"List");
			SInfoUtils.addSOrgName(iColl, new String[]{"input_br_id"});
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
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
