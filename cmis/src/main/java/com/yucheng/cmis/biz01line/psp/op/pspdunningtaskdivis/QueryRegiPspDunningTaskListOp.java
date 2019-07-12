package com.yucheng.cmis.biz01line.psp.op.pspdunningtaskdivis;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryRegiPspDunningTaskListOp extends CMISOperation {

	private final String modelId = "PspDunningTaskDivis";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			StringBuffer queryCond = new StringBuffer();
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
				if(queryData!=null){
					String cus_id = (String)queryData.getDataValue("cus_id");//客户码
					String acc_no = (String)queryData.getDataValue("acc_no");//借据编号
					String cont_no = (String)queryData.getDataValue("cont_no");//合同编号
					if(cus_id!=null && !"".equals(cus_id)){
						queryCond.append(" and cus_id='"+cus_id+"'");
					}
					if(acc_no!=null && !"".equals(acc_no)){
						queryCond.append(" and bill_no='"+acc_no+"'");
					}
					if(cont_no!=null && !"".equals(cont_no)){
						queryCond.append(" and cont_no='"+cont_no+"'");
					}
				}
			} catch (Exception e) {}
//			
//			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
//			//筛选当前登录人的任务
//			String currentUserId = (String)context.getDataValue("currentUserId");
////			if(conditionStr==null||"".equals(conditionStr)){
////				conditionStr = "WHERE EXE_ID='"+currentUserId+"' order by serno desc";
////			}else{
////				conditionStr += "AND EXE_ID='"+currentUserId+"' order by serno desc";
////			}
//			RecordRestrict recordRestrict = this.getRecordRestrict(context);
//			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			int size = 15;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
//			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
//			List<String> list = new ArrayList<String>();
//			list.add("serno");
//			list.add("cus_id");
//			list.add("acc_no");
//			list.add("cont_no");
//			list.add("task_create_date");
//			list.add("need_end_date");
//			list.add("exe_id");
//			list.add("divis_id");
//			list.add("input_date");
//			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
//			iColl.setName(iColl.getName()+"List");
//			SInfoUtils.addUSerName(iColl, new String[] { "exe_id","divis_id" });
//			//翻译客户码
//			String[] args=new String[] { "cus_id" };
//			String[] modelIds=new String[]{"CusBase"};
//			String[] modelForeign=new String[]{"cus_id"};
//			String[] fieldName=new String[]{"cus_name"};
//			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			String currentUserId = (String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			String organno = (String)context.getDataValue(CMISConstance.ATTR_ORGID);
			String roles = (String)context.getDataValue(CMISConstance.ATTR_ROLENO_LIST);
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			String sql = "select '逾期' du_type,al.manager_br_id,(select c.organname from s_org c where c.organno= al.manager_br_id) as manager_br_name," +
				"al.BILL_NO,al.CONT_NO,al.CUS_ID,(select cus.cus_name from cus_base cus where cus.cus_id=al.cus_id) cus_name ,"+
				"round(al.LOAN_AMT,2) LOAN_AMT,round(al.loan_balance,2) loan_balance,al.DISTR_DATE,al.end_date,al.INNER_OWE_INT,al.OUT_OWE_INT,"+
				"(select cnname from s_dic where opttype='STD_ZB_FIVE_SORT' and enname=al.five_class ) FIVE_CLASS "+
				//催收提醒：逾期后提醒，到期日期小于当前系统日期并且逾期金额大于零     2014-06-09
				"from acc_loan al where to_date(al.end_date,'yyyy-mm-dd')-(select to_date(openday,'yyyy-mm-dd') from pub_sys_info)<0 and al.overdue_balance>0 "+ 
				//"and to_date(al.end_date,'yyyy-mm-dd')-(select to_date(openday,'yyyy-mm-dd') from pub_sys_info)<16 and al.loan_balance>0 "+
				"and (al.cont_no in (select cm.cont_no from cus_manager cm where cm.is_main_manager='1'  and manager_id='"+currentUserId+"' or '1010' in ("+roles+")) or ((al.manager_br_id='"+organno+"' or (select suporganno from s_org where organno=al.manager_br_id)='"+organno+"') and ('2002' in ("+roles+") or '3004' in ("+roles+")))) "+ queryCond.toString()+
				" union "+
				"select '垫款' du_type,ap.MANAGER_BR_ID,(select c.organname from s_org c where c.organno= ap.manager_br_id) as manager_br_name,"+
				"ap.BILL_NO,ap.CONT_NO　,ap.CUS_ID,(select cus.cus_name from cus_base cus where cus.cus_id=ap.cus_id) cus_name"+ 
				",round(ap.pad_amt,2) LOAN_AMT ,round(ap.pad_bal,2) loan_balance ,ap.pad_date DISTR_DATE,'' end_date"+
				",0 INNER_OWE_INT,0 OUT_OWE_INT,(select cnname from s_dic where opttype='STD_ZB_FIVE_SORT' and enname=ap.five_class ) FIVE_CLASS "+
				"from acc_pad ap where ap.pad_bal>0 and (ap.cont_no in (select cm.cont_no from cus_manager cm where cm.is_main_manager='1' and manager_id='"+currentUserId+"' or '1010' in ("+roles+")) or ((ap.manager_br_id='"+organno+"' or (select suporganno from s_org where organno=ap.manager_br_id)='"+organno+"') and ('2002' in ("+roles+") or '3004' in ("+roles+"))))"+ queryCond.toString();
			IndexedCollection iCollDun = TableModelUtil.buildPageData(pageInfo, dataSource, sql);
			
			iCollDun.setName("PspDunningTaskDivisList");
			this.putDataElement2Context(iCollDun, context);
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
