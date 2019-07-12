package com.yucheng.cmis.biz01line.cont.op.ctrloancont;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpBatchComponent;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.biz01line.prd.domain.PrdBasicinfo;
import com.yucheng.cmis.biz01line.prd.msi.PrdServiceInterface;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryAllCtrLoanContDetailOp  extends CMISOperation {

	private final String modelIdCtr = "CtrLoanCont";
	private final String modelIdIqp = "IqpLoanApp";
	private final String modelIdFollow = "IqpDiscApp";
	private final String cusModel ="CusBase";
	private final String modelIdStar = "WfiWorklistTodo";
	private final String modelIdEnd = "WfiWorklistEnd";

	private final String cont_no_name = "cont_no";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String retValue = "";
		try{
			connection = this.getConnection(context);
			String cont_no_value = null;
			String serno = null;
			KeyedCollection kColl = null;
			String prd_id = null;
			String menuIdTab = null;
			if(context.containsKey("cont_no")){
				cont_no_value = (String)context.getDataValue(cont_no_name);
				cont_no_value = new String(cont_no_value.getBytes("ISO8859-1"),"UTF-8");
			}else if(context.containsKey("serno")){
				serno = (String)context.getDataValue("serno");
			}
		    
			TableModelDAO dao = this.getTableModelDAO(context); 
			
            if(cont_no_value == null || "".equals(cont_no_value)){
            	IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
				.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
				String tableName = iqpLoanAppComponent.getIqpName(serno);
				if("IqpCreditChangeApp".equals(tableName)){
					kColl = dao.queryDetail("IqpCreditChangeApp", serno, connection);
					String[] args=new String[] {"cus_id","prd_id" };
					String[] modelIds=new String[]{"CusBase","PrdBasicinfo"};
					String[]modelForeign=new String[]{"cus_id","prdid"}; 
					String[] fieldName=new String[]{"cus_name","prdname"};    
				    //详细信息翻译时调用			
				    SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
				    /** 组织机构、登记机构翻译 */
					SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
					SInfoUtils.addSOrgName(kColl, new String[]{"input_br_id","manager_br_id"}); 
					retValue = "IqpCreditChangeApp";
					menuIdTab="queryIqpCreditChangeHisList";
				}else if("IqpGuarantChangeApp".equals(tableName)){
					kColl = dao.queryDetail("IqpGuarantChangeApp", serno, connection);
					String[] args=new String[] {"cus_id","prd_id" };
					String[] modelIds=new String[]{"CusBase","PrdBasicinfo"};
					String[]modelForeign=new String[]{"cus_id","prdid"}; 
					String[] fieldName=new String[]{"cus_name","prdname"};    
				    //详细信息翻译时调用			
				    SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
				    /** 组织机构、登记机构翻译 */
					SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
					SInfoUtils.addSOrgName(kColl, new String[]{"input_br_id","manager_br_id"}); 
					retValue = "IqpGuarantChangeApp";
					menuIdTab="queryIqpGuarantChangeAppHistory";
				}else if("IqpGuarChangeApp".equals(tableName)){
					kColl = dao.queryDetail("IqpGuarChangeApp", serno, connection);
					/**查询流程实例号
					 * 首先查询待办流程列表，如果无结果则去查询办结流程列表，如果无结果不作处理
					 * */
					String instanceId = "";
					String condition = "where pk_value='"+serno+"'";
					IndexedCollection icollWfi = dao.queryList(modelIdStar, condition, connection);
					if(icollWfi.size()>0){
						KeyedCollection kCollWfi = (KeyedCollection)icollWfi.get(0);
						instanceId = (String)kCollWfi.getDataValue("instanceid");
					}else{
						IndexedCollection icollWfiEnd = dao.queryList(modelIdEnd, condition, connection);
						if(icollWfiEnd.size()>0){
							KeyedCollection kCollWfiEnd = (KeyedCollection)icollWfiEnd.get(0);
							instanceId = (String)kCollWfiEnd.getDataValue("instanceid");
						}
					} 
					context.addDataField("instanceIdPvp", instanceId);
					
					String[] args=new String[] {"cus_id","prd_id" };
					String[] modelIds=new String[]{"CusBase","PrdBasicinfo"};
					String[]modelForeign=new String[]{"cus_id","prdid"}; 
					String[] fieldName=new String[]{"cus_name","prdname"};    
				    //详细信息翻译时调用			
				    SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
				    /** 组织机构、登记机构翻译 */
					SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
					SInfoUtils.addSOrgName(kColl, new String[]{"input_br_id","manager_br_id"}); 
					retValue = "IqpGuarChangeApp";
					menuIdTab="queryIqpGuarChangeAppHis";
				}else{
					kColl = dao.queryAllDetail(modelIdIqp, serno, connection);
	            	KeyedCollection kCollSub = (KeyedCollection)kColl.getDataElement("IqpLoanAppSub");
	            	//从关系表中取授信台账编号 
	    			kColl = this.getLimitNo4Iqp(kColl, dao, connection);
	    			
	    			prd_id = (String)kColl.getDataValue("prd_id");
	    			if("300020".equals(prd_id) || "300021".equals(prd_id)){
	    				//从关系表中取授信台账编号 
	    				kColl = this.getLimitNo(kColl, dao, connection);
	    				
	    				KeyedCollection kCollFollow = (KeyedCollection) kColl.getDataElement(modelIdFollow);
	    				/**获取业务关联批次下的实付总金额和总利息*/
	    				IqpBatchComponent batchComponent = (IqpBatchComponent)CMISComponentFactory.getComponentFactoryInstance()
	    				.getComponentInstance(AppConstant.IQPBATCHCOMPONENT, context, connection);
	    				IndexedCollection ic = batchComponent.getBatchTotalInfoBySerno(serno);
	    				KeyedCollection kCollForBatch = null;
	    				if(!ic.isEmpty()){
	    					kCollForBatch = (KeyedCollection) ic.get(0);
	    				}
	    				if(kCollForBatch!=null){
	    					kCollFollow.setDataValue("disc_rate", kCollForBatch.getDataValue("int"));//总利息
	    					kCollFollow.setDataValue("net_pay_amt", kCollForBatch.getDataValue("rpay_amt"));//实付金额
	    					kCollFollow.setDataValue("bill_qty", kCollForBatch.getDataValue("bill_qnt"));//票据数量
	    					kCollFollow.setDataValue("disc_date", kCollForBatch.getDataValue("fore_disc_date"));//票据数量
	    					kColl.setDataValue("apply_amount", kCollForBatch.getDataValue("bill_total_amt"));//票据总金额
	    				}
	    				
	    				String cus_id = (String)kColl.getDataValue("cus_id");
	    				KeyedCollection prdkColl = dao.queryDetail("PrdBasicinfo", prd_id, connection);
	    				KeyedCollection cuskColl = dao.queryDetail(cusModel, cus_id, connection);
	    				String currency =(String)prdkColl.getDataValue("currency");
	    				String guarway =(String)prdkColl.getDataValue("guarway");
	    				
	    				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
	    				CusServiceInterface serviceCus = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
	    				//从cus_base表中获得客户基本信息
	    				CusBase cus = serviceCus.getCusBaseByCusId(cus_id, context, connection);
	    				String line = (String)cus.getBelgLine();
	    				
	    				/** 翻译字典项 */
	    				Map<String,String> map = new HashMap<String, String>();
	    				map.put("IqpDiscApp.agriculture_type", "STD_ZB_FARME");
	    				map.put("IqpDiscApp.ensure_project_loan", "STD_ZB_DKGS5");
	    				map.put("IqpDiscApp.estate_adjust_type", "STD_ZB_TRD_TYPE");
	    				map.put("IqpDiscApp.strategy_new_loan", "STD_ZB_ZLXXCYLX");
	    				map.put("IqpDiscApp.new_prd_loan", "STD_ZB_XXCYDK");
	    				//map.put("IqpDiscApp.green_prd", "STD_ZB_LSCP");
	    				map.put("IqpDiscApp.loan_direction", "STD_GB_4754-2011");
	    				map.put("IqpDiscApp.loan_belong1", "STD_ZB_DKGS1");
	    				map.put("IqpDiscApp.loan_belong2", "STD_ZB_DKGS2");
	    				map.put("IqpDiscApp.loan_belong3", "STD_ZB_DKGS3");
	    				map.put("IqpDiscApp.loan_use_type", "STD_ZB_DKYT");
	    				if("BL100".equals(line) || "BL200".equals(line)){
	    					map.put("IqpDiscApp.loan_type", "STD_COM_POSITIONTYPE");
	    				}else if("BL300".equals(line)){
	    					map.put("IqpDiscApp.loan_type", "STD_PER_POSITIONTYPE");
	    				}
	    				
	    				CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
	    				SInfoUtils.addPopName(kColl, map, service);
	    				
	    			    this.putDataElement2Context(kColl, context);
	    				this.putDataElement2Context(prdkColl, context);
	    				this.putDataElement2Context(cuskColl, context);
	    				context.addDataField("currency", currency);
	    				context.addDataField("guarway", guarway);
	    				
	    				/** 组织机构、登记机构翻译 */
	    				SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
	    				SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","input_br_id","in_acct_br_id"});
	    				retValue = "IqpDiscApp";
	    			}else{
	    				String cus_id = (String)kColl.getDataValue("cus_id");
	        			String is_close_loan = (String)kCollSub.getDataValue("is_close_loan");
	        			KeyedCollection prdkColl = dao.queryDetail("PrdBasicinfo", prd_id, connection);
	        			String currency =(String)prdkColl.getDataValue("currency");
	        			String guarway =(String)prdkColl.getDataValue("guarway");
	        			
	        			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
	        			CusServiceInterface serviceCus = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
	        			//从cus_base表中获得客户基本信息
	        			CusBase cus = serviceCus.getCusBaseByCusId(cus_id, context, connection);
	        			String line = (String)cus.getBelgLine();
	        			/** 翻译字典项 */
	        			Map<String,String> map = new HashMap<String, String>();
	        			map.put("IqpLoanAppSub.agriculture_type", "STD_ZB_FARME");
	        			map.put("IqpLoanAppSub.ensure_project_loan", "STD_ZB_DKGS5");
	        			map.put("IqpLoanAppSub.estate_adjust_type", "STD_ZB_TRD_TYPE");
	        			map.put("IqpLoanAppSub.strategy_new_loan", "STD_ZB_ZLXXCYLX");
	        			map.put("IqpLoanAppSub.new_prd_loan", "STD_ZB_XXCYDK");
	        			//map.put("IqpLoanAppSub.green_prd", "STD_ZB_LSCP");
	        			map.put("IqpLoanAppSub.loan_direction", "STD_GB_4754-2011");
	        			map.put("IqpLoanAppSub.loan_belong1", "STD_ZB_DKGS1");
	        			map.put("IqpLoanAppSub.loan_belong2", "STD_ZB_DKGS2");
	        			map.put("IqpLoanAppSub.loan_belong3", "STD_ZB_DKGS3");
	        			if("BL100".equals(line) || "BL200".equals(line)){
	        				map.put("IqpLoanAppSub.loan_type", "STD_COM_POSITIONTYPE");
	        			}else if("BL300".equals(line)){
	        				map.put("IqpLoanAppSub.loan_type", "STD_PER_POSITIONTYPE");
	        			}
	        			
	        			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
	        			SInfoUtils.addPopName(kColl, map, service);
	        			
	        			String[] args=new String[] {"cus_id","cus_id","repay_type","repay_type" };
	        			String[] modelIds=new String[]{"CusBase","CusBase","PrdRepayMode","PrdRepayMode"};
	        			String[]modelForeign=new String[]{"cus_id","cus_id","repay_mode_id","repay_mode_id"}; 
	        			String[] fieldName=new String[]{"cus_name","belg_line","repay_mode_dec","repay_mode_type"};
	        			String[] resultName = new String[] { "cus_id_displayname","belg_line","repay_type_displayname","repay_mode_type"};
	        		    //详细信息翻译时调用	
	        			SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
	        			
	        			SystemTransUtils.dealPointName(kCollSub, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
	        			String belg_line =(String)kColl.getDataValue("belg_line");  
	        			
	        			this.putDataElement2Context(kColl, context);
	        			this.putDataElement2Context(prdkColl, context);
	        			context.addDataField("currency", currency);
	        			context.addDataField("guarway", guarway);
	        			context.addDataField("belg_line", belg_line);
	        			context.addDataField("repay_type", kColl.getDataValue("IqpLoanAppSub.repay_type"));
	        			
	        			/** 组织机构、登记机构翻译 */
	        			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
	        			SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","input_br_id","in_acct_br_id"});
	        			
	        			if(prd_id.equals("200024")){
	        				//获取明细金额数量汇总
	        				String condition = " where serno = '"+serno+"'";
	        				IndexedCollection accpIColl = dao.queryList("IqpAccpDetail", condition, connection);
	        				BigDecimal drft_amt_total = new BigDecimal("0");
	        				for(int i=0;i<accpIColl.size();i++){
	        					KeyedCollection accpKColl = (KeyedCollection) accpIColl.get(i);
	        					BigDecimal drft_amt = new BigDecimal(accpKColl.getDataValue("drft_amt")+"");
	        					drft_amt_total = drft_amt_total.add(drft_amt);
	        				}
	        				kColl.setDataValue("apply_amount", drft_amt_total);
	        				KeyedCollection kColl4Accp = dao.queryAllDetail("IqpAccAccp", serno, connection);
	        				if(kColl4Accp!=null&&kColl4Accp.containsKey("is_elec_bill")&&kColl4Accp.getDataValue("is_elec_bill")!=null){
	        					if("1".equals(kColl4Accp.getDataValue("is_elec_bill"))){
	        						context.put("is_elec_bill", "1");
	        					}else{
	        						context.put("is_elec_bill", "2");
	        					}
	        				}
	        			}
	        			/**个人客户查询半年日均 -----start-----*/
	        			String flag="success";
	        			String mes = "";
	        			PrdServiceInterface servicePrd = (PrdServiceInterface)serviceJndi.getModualServiceById("prdServices", "prd");
	        			ESBServiceInterface serviceEsb = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
	        			PrdBasicinfo prdBasicinfo = servicePrd.getPrdBasicinfoList(prd_id, connection);
	        			String supcatalog = (String)prdBasicinfo.getSupcatalog();
	        			context.put("supcatalog", supcatalog);
	        			if("PRD20120802659".equals(supcatalog) && "1".equals(is_close_loan) ){//如果是个人经营性贷款
	        				String spouse_cus_id ="";
	        				IndexedCollection iCollCus = serviceCus.getIndivSocRel(cus_id, "1", connection);
	        				if(iCollCus.size()>0){
	                           KeyedCollection kCollCus = (KeyedCollection)iCollCus.get(0);
	                           spouse_cus_id = (String)kCollCus.getDataValue("cus_id_rel");
	        				}
	        				/*** 调用核心实时接口半年日均 ***/
	        				KeyedCollection retKColl = null;
	        				KeyedCollection BODY = new KeyedCollection("BODY");
	        				try{
	        					retKColl = serviceEsb.tradeBNRJ(cus_id, spouse_cus_id, context, connection);
	        					if(TagUtil.haveSuccess(retKColl, context)){//成功
	        						BODY = (KeyedCollection)retKColl.getDataElement("BODY");
	        						BigDecimal day_eql_bal = BigDecimalUtil.replaceNull(BODY.getDataValue("DAY_EQL_BAL"));
	        						BigDecimal mate_day_eql_bal = BigDecimalUtil.replaceNull(BODY.getDataValue("MATE_DAY_EQL_BAL"));
	        					    BigDecimal totalAmt = day_eql_bal.add(mate_day_eql_bal);
	        					    context.put("totalAmt", totalAmt+"");
	        					    context.put("supcatalog", supcatalog);
	        					}else{
	        						flag = "error";
	        						mes =(String)retKColl.getDataValue("RET_MSG");
	        					}
	        				}catch(Exception e){
	        					flag = "error";
	        					mes = "ESB通讯接口【获取半年日均】交易失败："+e.getMessage();
	        				}
	        				context.put("flag", flag);
	        				context.put("mes", mes);
	        			}
	        			/**个人客户查询半年日均 -----end-----*/
	        			retValue = "IqpLoanApp";
	    			}
	    			menuIdTab="queryIqpLoanAppHistoryList";
				}
			}else{
				kColl = dao.queryAllDetail(modelIdCtr, cont_no_value, connection);
				serno = (String) kColl.getDataValue("serno");
				String cus_id = (String) kColl.getDataValue("cus_id");
				prd_id = (String) kColl.getDataValue("prd_id");
				
				//从关系表中取授信台账编号 
				kColl = this.getLimitNo(kColl, dao, connection);
				
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				CusServiceInterface serviceCus = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
				//从cus_base表中获得客户基本信息
				CusBase cus = serviceCus.getCusBaseByCusId(cus_id, context, connection);
				String line = (String)cus.getBelgLine();
				if("300020".equals(prd_id) || "300021".equals(prd_id)){
					/** 翻译字典项 */
					Map<String,String> map = new HashMap<String, String>();
					map.put("CtrDiscCont.agriculture_type", "STD_ZB_FARME");
					map.put("CtrDiscCont.ensure_project_loan", "STD_ZB_DKGS5");
					map.put("CtrDiscCont.estate_adjust_type", "STD_ZB_TRD_TYPE");
					map.put("CtrDiscCont.strategy_new_loan", "STD_ZB_ZLXXCYLX");   
					map.put("CtrDiscCont.new_prd_loan", "STD_ZB_XXCYDK");
					//map.put("CtrDiscCont.green_prd", "STD_ZB_LSCP");
					map.put("CtrDiscCont.loan_direction", "STD_GB_4754-2011");
					map.put("CtrDiscCont.loan_belong1", "STD_ZB_DKGS1");
					map.put("CtrDiscCont.loan_belong2", "STD_ZB_DKGS2");
					map.put("CtrDiscCont.loan_belong3", "STD_ZB_DKGS3");
					map.put("CtrDiscCont.loan_use_type", "STD_ZB_DKYT"); 
					if("BL100".equals(line) || "BL200".equals(line)){
						map.put("CtrDiscCont.loan_type", "STD_COM_POSITIONTYPE");
					}else if("BL300".equals(line)){
						map.put("CtrDiscCont.loan_type", "STD_PER_POSITIONTYPE");
					}
					CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
					SInfoUtils.addPopName(kColl, map, service);
					
					String[] args=new String[] { "prd_id" };
					String[] cusArgs = new String[]{"cus_id"};
					String[] treeArgs=new String[] { "estate_adjust_type","strategy_new_loan","new_prd_loan","green_prd" };
					String[] modelIds=new String[]{"PrdBasicinfo"};
					String[] cusModelIds = new String[]{"CusBase"};
					String[] treeModelIds=new String[]{"STreedic"};
					String[] modelForeign=new String[]{"prdid"};
					String[] treeModelForeign=new String[]{"enname"};
					String[] fieldName=new String[]{"prdname"};
					String[] cusFieldName=new String[]{"cus_name"};
					String[] treeFieldName=new String[]{"cnname"};
					
		            //详细信息翻译时调用			
				    SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
				    SystemTransUtils.dealName(kColl, cusArgs, SystemTransUtils.ADD, context, cusModelIds,cusArgs, cusFieldName);
				    SystemTransUtils.dealName(kColl, treeArgs, SystemTransUtils.ADD, context, treeModelIds,treeModelForeign, treeFieldName);
				    
				    /** 组织机构、登记机构翻译 */
					SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
					SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","input_br_id","in_acct_br_id"});
					retValue = "disCtrLoanCont";
				}else{
					/** 翻译字典项 */
					Map<String,String> map = new HashMap<String, String>();
					map.put("CtrLoanContSub.agriculture_type", "STD_ZB_FARME");
					map.put("CtrLoanContSub.ensure_project_loan", "STD_ZB_DKGS5"); 
					map.put("CtrLoanContSub.estate_adjust_type", "STD_ZB_TRD_TYPE");
					map.put("CtrLoanContSub.strategy_new_type", "STD_ZB_ZLXXCYLX");   
					map.put("CtrLoanContSub.new_prd_loan", "STD_ZB_XXCYDK");
					//map.put("CtrLoanContSub.green_prd", "STD_ZB_LSCP");
					map.put("CtrLoanContSub.loan_direction", "STD_GB_4754-2011");
					map.put("CtrLoanContSub.loan_belong1", "STD_ZB_DKGS1");
					map.put("CtrLoanContSub.loan_belong2", "STD_ZB_DKGS2");
					map.put("CtrLoanContSub.loan_belong3", "STD_ZB_DKGS3");
					if("BL100".equals(line) || "BL200".equals(line)){
						map.put("CtrLoanContSub.loan_type", "STD_COM_POSITIONTYPE");
					}else if("BL300".equals(line)){
						map.put("CtrLoanContSub.loan_type", "STD_PER_POSITIONTYPE");
					}
					CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
					SInfoUtils.addPopName(kColl, map, service);
					
					String[] args=new String[] {"cus_id","cus_id","repay_type","repay_type","prd_id" };
					String[] modelIds=new String[]{"CusBase","CusBase","PrdRepayMode","PrdRepayMode","PrdBasicinfo"};
					String[]modelForeign=new String[]{"cus_id","cus_id","repay_mode_id","repay_mode_id","prdid"}; 
					String[] fieldName=new String[]{"cus_name","belg_line","repay_mode_dec","repay_mode_type","prdname"};
					String[] resultName = new String[] { "cus_id_displayname","belg_line","repay_type_displayname","repay_mode_type","prd_id_displayname"};
				    //详细信息翻译时调用	
					SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
					KeyedCollection kCollSub = (KeyedCollection)kColl.getDataElement("CtrLoanContSub");
					SystemTransUtils.dealPointName(kCollSub, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
					String belg_line =(String)kColl.getDataValue("belg_line");
				    /** 组织机构、登记机构翻译 */
					SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
					SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","input_br_id","in_acct_br_id"});
					
					context.addDataField("belg_line", belg_line);
					context.addDataField("repay_type", kColl.getDataValue("CtrLoanContSub.repay_type"));
					retValue = "ctrLoanCont";
				}
				menuIdTab="queryCtrLoanContHistoryList";
			}
            this.putDataElement2Context(kColl, context);
            HttpServletRequest request = (HttpServletRequest) context.getDataValue(EMPConstance.SERVLET_REQUEST);
			request.setAttribute("menuIdTab", menuIdTab);
			/** 如果是信用证业务，则查询溢装比例 */
			if("700020".equals(prd_id) || "700021".equals(prd_id)){
				KeyedCollection kCollCredit = dao.queryDetail("IqpCredit", serno, connection);
				if(kCollCredit != null){
					BigDecimal floodact_perc = BigDecimalUtil.replaceNull(kCollCredit.getDataValue("floodact_perc"));
					context.put("floodact_perc", floodact_perc);
				}else{
					context.put("floodact_perc", "0");
				}
			}else{
				context.put("floodact_perc", "0");
			}
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return retValue;
	}
	/**计算剩余额度公用方法*/
	public BigDecimal caculate(BigDecimal total_amt,KeyedCollection kCollRel){
		BigDecimal lmt_amt = null;
		BigDecimal remain_amount = null;
		try {
			lmt_amt = new BigDecimal(kCollRel.getDataValue("lmt_amt")+"");
			remain_amount = total_amt.subtract(lmt_amt);
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
		return remain_amount;
	}
	
	//'1':'不使用额度', '2':'使用循环额度', '3':'使用一次性额度', '4':'使用第三方额度', '5':'使用循环额度+第三方额度', '6':'使用一次性额度+第三方额度'
	public KeyedCollection getLimitNo(KeyedCollection kColl,TableModelDAO dao,Connection connection) throws Exception{
		try {
			String cont_no = (String)kColl.getDataValue("cont_no");
			String limit_ind = (String)kColl.getDataValue("limit_ind");
			if("1".equals(limit_ind)){
				return kColl;
			}else if("2".equals(limit_ind) || "3".equals(limit_ind)){
                String condition = "where cont_no='"+cont_no+"'";
                KeyedCollection kCollRel = dao.queryFirst("RBusLmtInfo", null, condition, connection);
                String agr_no = (String)kCollRel.getDataValue("agr_no");
                kColl.put("limit_acc_no", agr_no);
                return kColl;
			}else if("4".equals(limit_ind)){
				String condition = "where cont_no='"+cont_no+"'";
                KeyedCollection kCollRel = dao.queryFirst("RBusLmtcreditInfo", null, condition, connection);
                String agr_no = (String)kCollRel.getDataValue("agr_no");
                kColl.put("limit_credit_no", agr_no);
                return kColl;
			}else if("5".equals(limit_ind) || "6".equals(limit_ind)){
				String condition = "where cont_no='"+cont_no+"'";
                KeyedCollection kCollRel = dao.queryFirst("RBusLmtInfo", null, condition, connection);
                KeyedCollection kCollCreditRel = dao.queryFirst("RBusLmtcreditInfo", null, condition, connection);
                String agr_no = (String)kCollRel.getDataValue("agr_no");
                String agr_no_credit = (String)kCollCreditRel.getDataValue("agr_no");
                kColl.put("limit_acc_no", agr_no);
                kColl.put("limit_credit_no", agr_no_credit);
                return kColl;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kColl;
	}
	public KeyedCollection getLimitNo4Iqp(KeyedCollection kColl,TableModelDAO dao,Connection connection) throws Exception{
		try {
			String serno = (String)kColl.getDataValue("serno");
			String limit_ind = (String)kColl.getDataValue("limit_ind");
			if("1".equals(limit_ind)){
				return kColl;
			}else if("2".equals(limit_ind) || "3".equals(limit_ind)){
                String condition = "where serno='"+serno+"'";
                KeyedCollection kCollRel = dao.queryFirst("RBusLmtInfo", null, condition, connection);
                String agr_no = (String)kCollRel.getDataValue("agr_no");
                kColl.put("limit_acc_no", agr_no);
                return kColl;
			}else if("4".equals(limit_ind)){
				String condition = "where serno='"+serno+"'";
                KeyedCollection kCollRel = dao.queryFirst("RBusLmtcreditInfo", null, condition, connection);
                String agr_no = (String)kCollRel.getDataValue("agr_no");
                kColl.put("limit_credit_no", agr_no);
                return kColl;
			}else if("5".equals(limit_ind) || "6".equals(limit_ind)){
				String condition = "where serno='"+serno+"'";
                KeyedCollection kCollRel = dao.queryFirst("RBusLmtInfo", null, condition, connection);
                KeyedCollection kCollCreditRel = dao.queryFirst("RBusLmtcreditInfo", null, condition, connection);
                String agr_no = (String)kCollRel.getDataValue("agr_no");
                String agr_no_credit = (String)kCollCreditRel.getDataValue("agr_no");
                kColl.put("limit_acc_no", agr_no);
                kColl.put("limit_credit_no", agr_no_credit);
                return kColl;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kColl;
	}
}
