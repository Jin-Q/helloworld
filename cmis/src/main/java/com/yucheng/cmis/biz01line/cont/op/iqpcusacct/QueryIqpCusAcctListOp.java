package com.yucheng.cmis.biz01line.cont.op.iqpcusacct;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpCusAcctListOp extends CMISOperation {


	private final String modelId = "IqpCusAcct";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno = null;
			String cont_no = null;
			String conditionStr = null;
			try {
				if(context!=null&&context.containsKey("serno")){
					serno = (String)context.getDataValue("serno");
				}
				if(context!=null&&context.containsKey("cont_no")){
					cont_no = (String)context.getDataValue("cont_no");
				}
			} catch (Exception e) {} 
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			String modiflg ="";
			String modify_rel_serno ="";
			if(context.containsKey("modiflg")){
				modiflg = (String) context.getDataValue("modiflg");
			}
			if(context.containsKey("modify_rel_serno")){
				modify_rel_serno = (String) context.getDataValue("modify_rel_serno");
			}
			if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){
				conditionStr = "where modify_rel_serno= '"+modify_rel_serno+"' order by serno desc,cont_no desc";
			}else{
				if(!"".equals(cont_no) && cont_no != null){
			    	conditionStr = "where cont_no= '"+cont_no+"' order by serno desc,cont_no desc";
			    }else{
			    	conditionStr = "where serno= '"+serno+"' order by serno desc,cont_no desc";	
			    } 
			}
			
			int size = 10;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>();
			list.add("serno"); 
			list.add("pk_id");
			list.add("acct_attr");
			list.add("acct_no");
			list.add("acct_name");
			list.add("opac_org_no");
			list.add("opan_org_name");
			list.add("pay_amt");
			list.add("is_this_org_acct");
			IndexedCollection iColl = new IndexedCollection();
			if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){
				 iColl = dao.queryList("IqpCusAcctTmp",list ,conditionStr,pageInfo,connection);
				 iColl.setName("IqpCusAcctList");
			}else{
				 iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
				 iColl.setName(iColl.getName()+"List");
			}

			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);

			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
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
