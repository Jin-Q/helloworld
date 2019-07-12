package com.yucheng.cmis.biz01line.cont.op.ctrloancont;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class GetAllCtrDetailViewOp  extends CMISOperation {

	private final String cont_no_name = "cont_no";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String retValue = "";
		try{
			connection = this.getConnection(context);
			String cont_no_value = null;
			KeyedCollection kColl = null;
			String prd_id = null;
			String menuIdTab = null;
			if(context.containsKey("cont_no")){
				cont_no_value = (String)context.getDataValue(cont_no_name);
				cont_no_value = new String(cont_no_value.getBytes("ISO8859-1"),"UTF-8");
			}
		    
			TableModelDAO dao = this.getTableModelDAO(context); 
			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);	
			String tableName = (String)iqpLoanAppComponent.getCtrName(cont_no_value);	
			if("CtrLoanCont".equals(tableName)){
				kColl = dao.queryDetail("CtrLoanCont", cont_no_value, connection);
				prd_id = (String)kColl.getDataValue("prd_id");
				String biz_type = (String)kColl.getDataValue("biz_type");
				
				if("8".equals(biz_type) && ("300021".equals(prd_id) || "300020".equals(prd_id))){
					KeyedCollection kCollSub = dao.queryDetail("CtrDiscCont", cont_no_value, connection);
					kColl.addDataElement(kCollSub);
					kColl = this.getCtrLoanCont4Disc(kColl, dao, context, connection);
					menuIdTab="yztqueryCtrLoanContHistoryList";
					retValue = "DiscCtrLoanCont";
				}else if("8".equals(biz_type) && (!"300021".equals(prd_id) && !"300020".equals(prd_id))){
					KeyedCollection kCollSub = dao.queryDetail("CtrLoanContSub", cont_no_value, connection);
					kColl.addDataElement(kCollSub);
					kColl = this.getCtrLoanCont(kColl, dao, context, connection);
					menuIdTab="yztqueryCtrLoanContHistoryList";
					retValue = "CtrLoanCont";
				}else if("300021".equals(prd_id) || "300020".equals(prd_id)){
					KeyedCollection kCollSub = dao.queryDetail("CtrDiscCont", cont_no_value, connection);
					kColl.addDataElement(kCollSub);
					kColl = this.getCtrLoanCont4Disc(kColl, dao, context, connection);
					menuIdTab="queryCtrLoanContHistoryList";
					retValue = "DiscCtrLoanCont";
				}else{
					KeyedCollection kCollSub = dao.queryDetail("CtrLoanContSub", cont_no_value, connection);
					kColl.addDataElement(kCollSub);
					kColl = this.getCtrLoanCont(kColl, dao, context, connection);
					menuIdTab="queryCtrLoanContHistoryList";
					retValue = "CtrLoanCont";
				}
				
			}else if("CtrAssetstrsfCont".equals(tableName)){
				kColl = dao.queryDetail("CtrAssetstrsfCont", cont_no_value, connection);
				/** 组织机构、登记机构翻译 */
				SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
				SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","input_br_id"});
				menuIdTab="queryCtrAssetstrsfHistoryList";
				retValue = "CtrAssetstrsfCont";
            }else if("CtrRpddscntCont".equals(tableName)){
            	kColl = dao.queryDetail("CtrRpddscntCont", cont_no_value, connection);
            	kColl= this.getCtrRpddscntCont(kColl, dao, context, connection);
            	menuIdTab="queryCtrRpddscntContHistoryList";
				retValue = "CtrRpddscntCont";
	        }else if("CtrAssetTransCont".equals(tableName)){
	        	kColl = dao.queryDetail("CtrAssetTransCont", cont_no_value, connection);
            	kColl= this.getCtrAssetTransCont(kColl, dao, context, connection);
            	menuIdTab="ZCLZHTCX";
				retValue = "CtrAssetTransCont";
	        }else if("CtrAssetProCont".equals(tableName)){
	        	kColl = dao.queryDetail("CtrAssetProCont", cont_no_value, connection);
            	kColl= this.getCtrAssetProCont(kColl, dao, context, connection);
            	menuIdTab="dqzczqhxm";
				retValue = "CtrAssetProCont";
	        }else{
	        	throw new Exception("通过合同编号查询合同失败，原因：该合同编号在指定的合同表中没有记录!");
	        }
	
            this.putDataElement2Context(kColl, context);
            HttpServletRequest request = (HttpServletRequest) context.getDataValue(EMPConstance.SERVLET_REQUEST);
			request.setAttribute("menuIdTab", menuIdTab);
			
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
	
	public KeyedCollection getCtrLoanCont4Disc(KeyedCollection kColl,TableModelDAO dao,Context context,Connection connection) throws Exception{
		try {
			//从关系表中取授信台账编号 
			kColl = this.getLimitNo(kColl, dao, connection);
			String cus_id = (String) kColl.getDataValue("cus_id");
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			CusServiceInterface serviceCus = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			//从cus_base表中获得客户基本信息
			CusBase cus = serviceCus.getCusBaseByCusId(cus_id, context, connection);
			String line = (String)cus.getBelgLine();
			
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kColl;
	}
	
	public KeyedCollection getCtrLoanCont(KeyedCollection kColl,TableModelDAO dao,Context context,Connection connection) throws Exception{
		try {
			//从关系表中取授信台账编号 
			kColl = this.getLimitNo(kColl, dao, connection);			
			
			String cus_id = (String) kColl.getDataValue("cus_id");
			String prd_id = (String) kColl.getDataValue("prd_id");
			String serno = (String) kColl.getDataValue("serno");
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			CusServiceInterface serviceCus = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			//从cus_base表中获得客户基本信息
			CusBase cus = serviceCus.getCusBaseByCusId(cus_id, context, connection);
			String line = (String)cus.getBelgLine();
	    	
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kColl;
	}
	

	public KeyedCollection getCtrRpddscntCont(KeyedCollection kColl,TableModelDAO dao,Context context,Connection connection) throws Exception{
		try {
			String limit_acc_no = null;
			String limit_credit_no = null;
			String serno = (String)kColl.getDataValue("serno");
			String limit_ind = (String)kColl.getDataValue("limit_ind");
			if("2".equals(limit_ind) || "3".equals(limit_ind)){
				KeyedCollection kCollRLmt = dao.queryDetail("RBusLmtInfo", serno, connection);
				limit_acc_no = (String)kCollRLmt.getDataValue("agr_no");
				kColl.put("limit_acc_no", limit_acc_no);
			}else if("4".equals(limit_ind)){
				KeyedCollection kCollRLmt = dao.queryFirst("RBusLmtcreditInfo",null, serno, connection);
				limit_credit_no = (String)kCollRLmt.getDataValue("agr_no");
				kColl.put("limit_credit_no", limit_credit_no);
			}
			
			String[] args=new String[] {"prd_id" };
			String[] modelIds=new String[]{"PrdBasicinfo"};
			String[]modelForeign=new String[]{"prdid"};
			String[] fieldName=new String[]{"prdname"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","input_br_id"});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kColl;
	}
	public KeyedCollection getCtrAssetTransCont(KeyedCollection kColl,TableModelDAO dao,Context context,Connection connection) throws Exception{
		try {
			String[] args=new String[] {"toorg_no" };
			String[] modelIds=new String[]{"CusSameOrg"};
			String[]modelForeign=new String[]{"same_org_no"};
			String[] fieldName=new String[]{"same_org_cnname"};
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
		    
		    /** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","input_br_id"});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kColl;
	}
	public KeyedCollection getCtrAssetProCont(KeyedCollection kColl,TableModelDAO dao,Context context,Connection connection) throws Exception{
		try {
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","input_br_id","pro_org"});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kColl;
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
}
