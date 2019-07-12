package com.yucheng.cmis.biz01line.iqp.op.iqpaccaccp.iqpaccpdetail;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;	

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.util.TableModelUtil;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryIqpAccAccpIqpAccpDetailListOp extends CMISOperation {
	
	private final String modelId = "IqpAccpDetail";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno_value = (String)context.getDataValue("IqpAccAccp.serno");
			
			if(serno_value==null){
				throw new EMPException("parent primary key not found!");
			}
			
			/**modified by lisj 2015-8-31 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			String cont="";
			if(context.containsKey("cont")){
				cont = (String)context.getDataValue("cont");
			}
			String modify_rel_serno = "";
			
			if(context.containsKey("modify_rel_serno")){
				modify_rel_serno = (String)context.getDataValue("modify_rel_serno");
			}
			
			String conditionStr = "";
			
		int size = 10;

		PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
		List<String> list = new ArrayList<String>();
		list.add("serno");
		list.add("clt_person");
		list.add("clt_acct_no");
		list.add("paorg_no");
		list.add("paorg_name");
		list.add("drft_amt");
		list.add("term_type");
		list.add("term");
		list.add("pk1");
		IndexedCollection iColl = new  IndexedCollection();
		if(!"".equals(cont) && "modify".equals(cont)){
			list.add("modify_rel_serno");
			conditionStr += "where serno = '" + serno_value+"' and modify_rel_serno='"+modify_rel_serno+"' order by serno desc";
			iColl = dao.queryList("IqpAccpDetailTmp",list ,conditionStr,pageInfo,connection);
			iColl.setName("IqpAccpDetailList");
		}else{
			conditionStr += "where serno = '" + serno_value+"' order by serno desc";
			iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
		}
		this.putDataElement2Context(iColl, context);
		TableModelUtil.parsePageInfo(context, pageInfo);
		/**modified by lisj 2015-8-31 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
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
