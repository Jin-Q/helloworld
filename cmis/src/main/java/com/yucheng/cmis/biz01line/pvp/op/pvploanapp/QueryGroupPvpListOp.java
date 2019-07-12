package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryGroupPvpListOp extends CMISOperation {
	private final String modelId = "PvpLoanApp";
	private final String modelIdCont = "CtrLoanCont";
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String conditionStr ="";
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelIdCont);
			} catch (Exception e) {}
			/** 模糊查询 */
			conditionStr = TableModelUtil.getQueryCondition_bak(modelId, queryData, context, false, true, false);
			/** 记录集权限 */
			/**add by lisj 2014年10月31日 修复前台点击查看合同信息JS导致menuId丢失 begin**/
			context.put("menuId", "GroupPvp");
			/**add by lisj 2014年10月31日  修复前台点击查看合同信息JS导致menuId丢失   end**/
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection); 
			
			//modify by liuhongyu on 2019-04-13 以挂起的不出现在出账队列当中
			String sqlStr = "and cont_no not in (select cont_no from pvp_hang_up where hang_status = '100')";
			conditionStr +=sqlStr;
			
			conditionStr +=" and cont_balance > 0 ORDER BY NVL(CONT_NUMBER,-1) DESC ";
			/**modified by yangzy 2014年10月31日  出账队列不分页   start**/
			//int size = 15;
		    PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "5000");
			//PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			/**modified by yangzy 2014年10月31日  出账队列不分页   end**/
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);

			IndexedCollection iColl4Temp = dao.queryList(modelIdCont,null ,conditionStr,pageInfo,connection);
			IndexedCollection iColl = new IndexedCollection();
			/**----------实时计算合同的合同余额--start---------------*/
			Double pvp_amt_other =0.00;
			
			Double cont_balance =0.00;
			for(int i=0; i<iColl4Temp.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl4Temp.get(i);
				String manager_br_id = (String)kColl.getDataValue("manager_br_id"); //管理机构
				String contBalan = TagUtil.replaceNull4String(kColl.getDataValue("cont_balance"));
				if(contBalan != null && !"".equals(contBalan)){
					cont_balance = Double.valueOf(contBalan);
				}
				/***
				String cont_no = (String)kColl.getDataValue("cont_no");
				//统计审批中的待发起的出账占用的合同额度
				String condition ="where cont_no='"+cont_no+"' and approve_status in ('111','000')";
				IndexedCollection iCollPvp = dao.queryList(modelId, condition, connection);
				Double pvp_amt_all =0.00;//审批中的出账占用合同金额
				for(int j=0;j<iCollPvp.size();j++){
					KeyedCollection kCollPvp = (KeyedCollection)iCollPvp.get(j);
					String pvp_amt = TagUtil.replaceNull4String(kCollPvp.getDataValue("pvp_amt"));
					if(pvp_amt != null && !"".equals(pvp_amt)){
						pvp_amt_other = Double.valueOf(pvp_amt);
					}
					pvp_amt_all += pvp_amt_other;
				}
				cont_balance -=pvp_amt_all;
				*/
				kColl.setDataValue("cont_balance", cont_balance);
				//放款金额默认为合同金额  2014-10-09 唐顺岩
				kColl.put("pvp_amt", cont_balance);
				
				//查询机构存贷比
				BigDecimal loadeporat;
				DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
				String conditionSelect = "select * from org_loadeporat  where org_no='"+manager_br_id+"'";
				IndexedCollection iCollSelect = TableModelUtil.buildPageData(null, dataSource, conditionSelect);
				if(iCollSelect.size()>0){
					KeyedCollection kCollSelect = (KeyedCollection)iCollSelect.get(0);
					loadeporat = BigDecimalUtil.replaceNull(kCollSelect.getDataValue("loadeporat"));
				}else{
					loadeporat = new BigDecimal(0);
				}				
				/**add by lisj 2014年11月20日 需求:【XD140818051】增加查询【期限】，【是否联保】字段 begin**/
				String contNo = (String) kColl.getDataValue("cont_no");
				String contTerm ="";
				String jointGuarFlag="";
				String conSelect4ContTerm = "select p3.cont_term || '(' || (select s.cnname from s_dic s "      
                                   +" where s.opttype = 'STD_ZB_TERM_TYPE' "          
                                   +" and s.enname = p3.term_type) || ')' as cont_term "
								   +" from ctr_loan_cont_sub p3 where p3.cont_no ='"+contNo+"'";
				
				String conSelect4JointGuarFlag= "select case when P2.agr_no is not null then '1' else '2' end as joint_guar_flag from ctr_loan_cont P1, "
								   +"(select rbli.cont_no, lad.agr_no from r_bus_lmt_info rbli, lmt_agr_details lad where rbli.agr_no = lad.limit_code"
								   +" and lad.sub_type = '03') P2  where p1.cont_no = p2.cont_no(+) and p1.cont_no ='"+contNo+"'";

				IndexedCollection iCollSelect4ContTerm = TableModelUtil.buildPageData(null, dataSource, conSelect4ContTerm);
				IndexedCollection iCollSelect4JonintGuarFlag = TableModelUtil.buildPageData(null, dataSource, conSelect4JointGuarFlag);
				if(iCollSelect4ContTerm.size()>0){
					KeyedCollection kCollSelect = (KeyedCollection)iCollSelect4ContTerm.get(0);
					contTerm =(String) kCollSelect.getDataValue("cont_term");
				}
				if(iCollSelect4JonintGuarFlag.size()>0){
					KeyedCollection kCollSelect = (KeyedCollection)iCollSelect4JonintGuarFlag.get(0);
					jointGuarFlag =(String) kCollSelect.getDataValue("joint_guar_flag");
				}
				kColl.put("cont_term", contTerm);
				kColl.put("joint_guar_flag", jointGuarFlag);
				kColl.put("dep_ln_rate", loadeporat);
				iColl.addDataElement(kColl);
				/**add by lisj 2014年11月20日 需求:【XD140818051】增加查询【期限】，【是否联保】字段 begin**/
			
			}
			/**----------实时计算合同的合同余额----end-------------*/
			iColl.setName(iColl4Temp.getName()+"List");
			
			String[] args=new String[] { "prd_id","cus_id","cont_no" };
			String[] modelIds=new String[]{"PrdBasicinfo","CusBase","CtrLoanContSub"};
			String[]modelForeign=new String[]{"prdid ","cus_id","cont_no"};
			String[] fieldName=new String[]{"prdname","cus_name","is_close_loan"};
			String[] resultName = new String[] { "prdname","cus_name","is_close_loan" };
			//详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName); 
		    SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName, resultName);
		    this.putDataElement2Context(iColl, context);
			
			
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(iColl, new String[]{"input_br_id"});
			/** 查询放款额度控制 */
			String openDay = (String)context.getDataValue(CMISConstance.OPENDAY);
			List list = new ArrayList();
			list.add("limit_amt");
			list.add("out_limit_amt");
			KeyedCollection limitKColl = dao.queryFirst("PvpLimitSet", list, " where open_day = '"+openDay+"'", connection);
			
			this.putDataElement2Context(limitKColl, context);
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
