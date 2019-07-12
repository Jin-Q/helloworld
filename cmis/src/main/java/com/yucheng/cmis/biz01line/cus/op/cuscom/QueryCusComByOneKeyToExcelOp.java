package com.yucheng.cmis.biz01line.cus.op.cuscom;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusRelTree.component.CusBaseRelComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.cus.group.component.CusGrpInfoComponent;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpInfo;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * @author lisj
 * @time 2014年11月25日
 * @description 需求编号：【XD141107075】
 * 				对公客户管理一键查询导出功能Op
 */
public class QueryCusComByOneKeyToExcelOp extends CMISOperation {
	
	private final String modelId = "CusBase";
	private final String modelIdCom = "CusCom";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String cus_id_value=null;
		try {
			cus_id_value = (String)context.getDataValue("cus_id");
		} catch (Exception e) {}
		if(cus_id_value == null || cus_id_value.length() == 0)
			throw new EMPJDBCException("主键cus_id的值不能为null！");	
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, cus_id_value, connection);
			KeyedCollection kCollCom = dao.queryDetail(modelIdCom, cus_id_value, connection);
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			//关联关系智能搜索导出列表，只显示第一层
			CusBaseRelComponent cusBaseRelComponent=null;
			IndexedCollection cusRelInfoList  = new IndexedCollection();
			cusBaseRelComponent = (CusBaseRelComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(
					PUBConstant.CusComRelComponent,context,connection);
			CusBaseComponent CusBaseComponent = (CusBaseComponent) cusBaseRelComponent.getComponent(PUBConstant.CUSBASE);
			CusCom cusCom=CusBaseComponent.getCusCom(cus_id_value);
			if(cusCom==null||cusCom.getCusId()==null||cusCom.getCusId().equals(""))
				throw new EMPException("对公客户信息中不存在该企业！");
			CusBase cusBase=CusBaseComponent.getCusBase(cus_id_value);
			String cusType = cusBase.getCusType();
			cusRelInfoList = cusBaseRelComponent.searchRelCusList(cus_id_value, cusType);		
			
			//基本信息法人代表
			String ceoName ="";
			String conSelect4CeoName = "select p4.cus_name ceo_name from cus_base p4,"
								+"(select p2.cus_id_rel,p2.com_mrg_typ from cus_base p1,cus_com_manager p2"
								+" where p1.cus_id =p2.cus_id and p1.cus_id='"+cus_id_value+"') p3"
								+" where p3.cus_id_rel =p4.cus_id and p3.com_mrg_typ='02'";
			IndexedCollection iCollSelect4CeoName = TableModelUtil.buildPageData(null, dataSource, conSelect4CeoName);
			
			if(iCollSelect4CeoName.size()>0){
				KeyedCollection kCollSelect = (KeyedCollection)iCollSelect4CeoName.get(0);
				ceoName =(String) kCollSelect.getDataValue("ceo_name");
			}		
			//资本构成
			String conSelect4CusComRelApital =" select t2.cert_type  cert_typ,"
												+"	t2.cert_code  cert_code,"
												+"	t2.cus_name   invt_name,"
												+"	t1.cus_id_rel cus_id_rel,"
												+"	t1.invt_amt   invt_amt,"
												+"	t1.invt_perc  invt_perc,"
												+"  t1.cus_id     cus_id"
												+"	from (select p1.cus_id, p1.cus_id_rel, p1.invt_amt, p1.invt_perc from Cus_Com_Rel_Apital p1 "
												+"	where p1.cus_id = '"+cus_id_value+"') t1, cus_base t2 where t1.cus_id_rel = t2.cus_id";
			IndexedCollection iCollSelect4CusComRelApital = TableModelUtil.buildPageData(null, dataSource, conSelect4CusComRelApital);
			iCollSelect4CusComRelApital.setName("CusComRelApitalList");
			//所在关联集团
			ComponentHelper cHelper = new ComponentHelper();
			CusGrpInfoComponent cusGrpInfoComponent = (CusGrpInfoComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSGRPINFO,context,connection);
			List<CusGrpInfo> cusGrpInfo=new ArrayList<CusGrpInfo>();
			cusGrpInfo = cusGrpInfoComponent.findCusGrpInfoByMemCusId(cus_id_value);
			IndexedCollection iCollSelect4CusGrpInfo = cHelper.domain2icol(cusGrpInfo,"CusGrpInfo",CMISConstance.CMIS_LIST_IND);
			iCollSelect4CusGrpInfo.setName("CusGrpInfoList");
			//授信审批历史
			String conSelect4LmtApplyList ="select * from lmt_apply la where la.cus_id='"+cus_id_value+"'";
			IndexedCollection iCollSelect4LmtApplyList = TableModelUtil.buildPageData(null, dataSource, conSelect4LmtApplyList);
			iCollSelect4LmtApplyList.setName("LmtApplyList");
			
			/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 begin */
			//联保授信审批历史
			String conSelect4LmtAppJointCoopList ="SELECT * FROM Lmt_App_Joint_Coop la WHERE  la.APPROVE_STATUS!='000' and la.coop_type = '010' and la.serno in ( SELECT t.serno FROM Lmt_App_Name_List t WHERE t.sub_type='03' and t.cus_id='"+cus_id_value+"') order by la.app_date desc";
			IndexedCollection iCollSelect4LmtAppJointCoopList = TableModelUtil.buildPageData(null, dataSource, conSelect4LmtAppJointCoopList);
			iCollSelect4LmtAppJointCoopList.setName("LmtAppJointCoopList");
			
			
			//集团（关联）授信审批历史
			String conSelect4LmtGrpApplyList ="SELECT * FROM Lmt_App_Grp t WHERE t.APPROVE_STATUS!='000' and t.serno in( select la.grp_serno from lmt_apply la  where la.cus_id='"+cus_id_value+"' )  order by t.app_date desc";
			IndexedCollection iCollSelect4LmtGrpApplyList = TableModelUtil.buildPageData(null, dataSource, conSelect4LmtGrpApplyList);
			iCollSelect4LmtGrpApplyList.setName("LmtGrpApplyList");
			/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 end */

			//授信额度信息（授信额度台账信息）
			String conSelect4LmtAgrInfoList ="select p2.serno,                           "
											+"       p1.agr_no,                          "
											+"       p1.cus_id,                          "
											+"		 p1.cur_type,						 "
											+"       p1.crd_amt,                         "
											+"       p1.enable_amt,                      "
											+"       p1.start_date,                      "
											+"       p1.end_date,                        "
											+"	     p1.lmt_status,                      "
											+"		 p2.manager_id,                      "
											+"		 p2.manager_br_id                    "
											+"  from lmt_agr_details p1, lmt_agr_info p2 "
											+"  where p1.cus_id = p2.cus_id              "
											+"   and p1.agr_no = p2.agr_no               "
											+"	 and p1.lmt_status in ('10','20')        "
											+"   and p1.cus_id = '"+cus_id_value+"'      ";
			IndexedCollection iCollSelect4LmtAgrInfoList = TableModelUtil.buildPageData(null, dataSource, conSelect4LmtAgrInfoList);
			iCollSelect4LmtAgrInfoList.setName("LmtAgrInfoList");	
			//台账信息
			String conSelect4AccViewList ="select cus_id, table_model, sum(bill_bal) bill_bal"
										+"  from ( select daorg_cusid as cus_id,                        "
										+"                (CASE                                         "
										+"                  WHEN ACCP_STATUS = '1' THEN                 "
										+"                   drft_amt                                   "
										+"                  ELSE                                        "
										+"                   0.00                                       "
										+"                END) as bill_bal,                             "
										+"                'AccAccp' as table_model                     "
										+"          from acc_accp                                       "
										+"         where accp_status in ('1', '6')                      "
										+"        union all                                             "
										+"        select discount_per as cus_id,                        "
										+"               (CASE                                          "
										+"                 WHEN ACCP_STATUS = '1' THEN                  "
										+"                  rpay_amt                                    "
										+"                 ELSE                                         "
										+"                  0.00                                        "
										+"               END) as bill_bal,                              "
										+"               'AccDrft' as table_model                       "
										+"          from acc_drft                                       "
										+"         where accp_status in ('1', '6')                      "
										+"        union all                                             "
										+"        select cus_id,                                        "
										+"               loan_balance as bill_bal,                      "
										+"               'AccLoan' as table_model                    "
										+"          from acc_loan                                       "
										+"         where prd_id not in ('400021', '400020')             "
										+"           and acc_status in ('1', '6')                       "
										+"        union all                                             "
										+"        select cus_id,                                        "
										+"               loan_balance as bill_bal,                      "
										+"               'AccCvrg' as table_model                      "
										+"          from acc_loan                                       "
										+"         where prd_id in ('400021', '400020')                 "
										+"           and acc_status in ('1', '6'))                      "
										+"			where cus_id='"+cus_id_value+                     "'"
										+"          group by cus_id, table_model			"; 
			IndexedCollection iCollSelect4AccViewList = TableModelUtil.buildPageData(null, dataSource, conSelect4AccViewList);
			if(iCollSelect4AccViewList.size() >0){
				for(int i=0;i<iCollSelect4AccViewList.size();i++){
					KeyedCollection kCollSelect = (KeyedCollection)iCollSelect4AccViewList.get(i);
					if("AccLoan".equals(kCollSelect.getDataValue("table_model"))){
						kCollSelect.addDataField("table_name", "贷款台账");
					}else if("AccAccp".equals(kCollSelect.getDataValue("table_model"))){
						kCollSelect.addDataField("table_name", "银承台账");
					}else if("AccDrft".equals(kCollSelect.getDataValue("table_model"))){
						kCollSelect.addDataField("table_name", "票据流水台帐");
					}else if("AccCvrg".equals(kCollSelect.getDataValue("table_model"))){
						kCollSelect.addDataField("table_name", "保函台账");
					}	
				}
			}
			iCollSelect4AccViewList.setName("AccViewList"); 

			//在我行担保情况
			String conSelect4GrtGuaranteeList ="select t.guar_cont_no,                                   "
												+"       t.cus_id,                                       "
												+"       t.guar_amt,                                     "
												+"       t.guar_way,                                     "
												+"       t.guar_start_date,                              "
												+"       t.guar_end_date,                                "
												+"       t.guar_cont_state                               "
												+"  from grt_guar_cont t                                 "
												+" where t.guar_cont_state ='01' and t.guar_cont_no in   "
												+"       (select p1.guar_cont_no                         "
												+"          from grt_guaranty_re p1                      "
												+"         where exists (select 1                        "
												+"                  from mort_guaranty_base_info p2      "
												+"                 where p1.guaranty_id = p2.guaranty_no "
												+"                   and p2.cus_id = '"+cus_id_value+"') "
												+"        union                                          "
												+"        select p1.guar_cont_no                         "
												+"          from grt_guaranty_re p1                      "
												+"         where exists (select 1                        "
												+"                  from grt_guarantee p2                "
												+"                 where p1.guaranty_id = p2.GUAR_ID     "
												+"                   and p2.cus_id = '"+cus_id_value+"'))";
			IndexedCollection iCollSelect4GrtGuaranteeList = TableModelUtil.buildPageData(null, dataSource, conSelect4GrtGuaranteeList);
			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			/**modified by lisj 2015-3-24 修复已占用金额BUG，于2015-3-26上线 begin**/
			if(iCollSelect4GrtGuaranteeList.size() >0){
				for(int i=0;i<iCollSelect4GrtGuaranteeList.size();i++){
					KeyedCollection kCollSelect = (KeyedCollection)iCollSelect4GrtGuaranteeList.get(i);
					kCollSelect.addDataField("guar_cus_id", cus_id_value);//担保人ID
					//计算占用金额
					String guar_cont_no = (String) kCollSelect.getDataValue("guar_cont_no");
					KeyedCollection GGCKColl = dao.queryDetail("GrtGuarCont", guar_cont_no, connection);
					BigDecimal used_amt = new BigDecimal(0.00);
					if(GGCKColl.containsKey("guar_cont_type")&&GGCKColl.getDataValue("guar_cont_type")!=null&&"00".equals(GGCKColl.getDataValue("guar_cont_type").toString())){
					    String conditionStr = "";
						conditionStr = "where guar_cont_no='"+guar_cont_no+"' and is_add_guar='2' and corre_rel in('1','5')";
						/**查询关联表中此担保合同已已经引入的金额*/
						IndexedCollection iColl =  dao.queryList("GrtLoanRGur", conditionStr, connection);
						for(int t=0;t<iColl.size();t++){
						   KeyedCollection kColl1 = (KeyedCollection)iColl.get(t);
						   String is_per_gur = (String)kColl1.getDataValue("is_per_gur");
						   if(is_per_gur != null && !"".equals(is_per_gur)){
							   String pk_id = (String)kColl1.getDataValue("pk_id");
							   String cont_no = (String)kColl1.getDataValue("cont_no");
							   if(cont_no != null && !"".equals(cont_no)){
								   String res = iqpLoanAppComponent.caculateGuarAmtSp(null, cont_no,pk_id);
								   if("2".equals(res)){
									   used_amt = used_amt.add(new BigDecimal(kColl1.getDataValue("guar_amt").toString()));
								   }else{
									   used_amt = used_amt.add(new BigDecimal(0));
								   }
							   }else{
								   String sernoSelect = (String)kColl1.getDataValue("serno");
								   String res = iqpLoanAppComponent.caculateGuarAmtSp(sernoSelect, null,pk_id);
								   if("2".equals(res)){
									   used_amt = used_amt.add(new BigDecimal(kColl1.getDataValue("guar_amt").toString()));
								   }else{
									   used_amt = used_amt.add(new BigDecimal(0));
								   }
							   }
						   }
						}
					}else{
						//最高额担保
						CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
						IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
						used_amt = BigDecimalUtil.replaceNull(service.getAmtForGuarCont(guar_cont_no, context, connection));
					}
					
					kCollSelect.addDataField("occupy_amt", used_amt);//占用金额
				}
			}
			/**modified by lisj 2015-3-24 修复已占用金额BUG，于2015-3-26上线  end**/
			iCollSelect4GrtGuaranteeList.setName("GrtGuaranteeList");
				
			Map<String,String> map = new HashMap<String,String>();
			map.put("reg_addr", "STD_GB_AREA_ALL");//注册登记地址
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(kCollCom, map, service);

			
			/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 begin */
			String[] args=new String[] { "bas_acc_bank","parent_cus_id","cus_id","guar_cus_id","grp_no"};
			String[] modelIds=new String[]{"PrdBankInfo","CusBase","CusBase","CusBase","CusGrpInfo"};
			String[] modelForeign=new String[]{"bank_no","cus_id","cus_id","cus_id","grp_no"};
			String[] fieldName=new String[]{"bank_name","cus_name","cus_name","cus_name","grp_name"};
			//详细信息翻译时调用
			SystemTransUtils.dealName(kCollCom, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SystemTransUtils.dealName(iCollSelect4CusGrpInfo, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SystemTransUtils.dealName(iCollSelect4LmtApplyList, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SystemTransUtils.dealName(iCollSelect4LmtAgrInfoList, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
		    SystemTransUtils.dealName(iCollSelect4GrtGuaranteeList, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			SystemTransUtils.dealName(iCollSelect4LmtGrpApplyList, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SystemTransUtils.dealName(iCollSelect4LmtAppJointCoopList, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);

		    SInfoUtils.addSOrgName(iCollSelect4CusGrpInfo, new String[] { "manager_br_id"});
			SInfoUtils.addUSerName(iCollSelect4CusGrpInfo, new String[] { "manager_id" });
			SInfoUtils.addSOrgName(iCollSelect4LmtApplyList, new String[] { "manager_br_id"});
			SInfoUtils.addUSerName(iCollSelect4LmtApplyList, new String[] { "manager_id" });			
			SInfoUtils.addSOrgName(iCollSelect4LmtAgrInfoList, new String[] { "manager_br_id"});
			SInfoUtils.addUSerName(iCollSelect4LmtAgrInfoList, new String[] { "manager_id" });
			SInfoUtils.addSOrgName(kColl, new String[]{"main_br_id","input_br_id"});
			SInfoUtils.addUSerName(kColl, new String[]{"cust_mgr","input_id"});
			
			SInfoUtils.addSOrgName(iCollSelect4LmtGrpApplyList, new String[] { "manager_br_id"});
			SInfoUtils.addUSerName(iCollSelect4LmtGrpApplyList, new String[] { "manager_id" });
			
			SInfoUtils.addSOrgName(iCollSelect4LmtAppJointCoopList, new String[] { "manager_br_id"});
			SInfoUtils.addUSerName(iCollSelect4LmtAppJointCoopList, new String[] { "manager_id" });
			
			IndexedCollection cusBaseInfo = new IndexedCollection();
			KeyedCollection cusBaseInfoKColl = new KeyedCollection();
			cusBaseInfoKColl.put("ceo_name", ceoName);//法人代表
			cusBaseInfoKColl.put("cus_name", kColl.getDataValue("cus_name"));
			cusBaseInfoKColl.put("reg_addr_displayname", kCollCom.getDataValue("reg_addr_displayname"));
			cusBaseInfoKColl.put("reg_cap_amt", kCollCom.getDataValue("reg_cap_amt"));
			cusBaseInfoKColl.put("reg_code", kCollCom.getDataValue("reg_code"));
			cusBaseInfoKColl.put("cert_code", kColl.getDataValue("cert_code"));
			cusBaseInfoKColl.put("nat_tax_reg_code", kCollCom.getDataValue("nat_tax_reg_code"));
			cusBaseInfoKColl.put("loc_tax_reg_code", kCollCom.getDataValue("loc_tax_reg_code"));
			cusBaseInfoKColl.put("loan_card_id", kColl.getDataValue("loan_card_id"));
			cusBaseInfo.addDataElement(cusBaseInfoKColl);
			cusBaseInfo.setName("cusBaseInfo");
			this.putDataElement2Context(cusBaseInfo, context);
			this.putDataElement2Context(cusRelInfoList, context);
			this.putDataElement2Context(iCollSelect4CusComRelApital, context);
			this.putDataElement2Context(iCollSelect4CusGrpInfo, context);
			this.putDataElement2Context(iCollSelect4LmtApplyList, context);
			this.putDataElement2Context(iCollSelect4LmtAgrInfoList, context);
			this.putDataElement2Context(iCollSelect4AccViewList, context);
			this.putDataElement2Context(iCollSelect4GrtGuaranteeList, context);
			this.putDataElement2Context(iCollSelect4LmtGrpApplyList, context);
			this.putDataElement2Context(iCollSelect4LmtAppJointCoopList, context);
			/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 end */
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
