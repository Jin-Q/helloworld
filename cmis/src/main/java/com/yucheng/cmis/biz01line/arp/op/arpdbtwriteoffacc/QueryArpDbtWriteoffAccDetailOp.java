package com.yucheng.cmis.biz01line.arp.op.arpdbtwriteoffacc;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryArpDbtWriteoffAccDetailOp  extends CMISOperation {
	
	private final String modelId = "ArpDbtWriteoffAcc";
	

	private final String pk_serno_name = "pk_serno";
	
	
	private boolean updateCheck = true;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String pk_serno_value = null;
			try {
				pk_serno_value = (String)context.getDataValue(pk_serno_name);
			} catch (Exception e) {}
			if(pk_serno_value == null || pk_serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk_serno_name+"] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, pk_serno_value, connection);
			
			/*** 初步处理，从cus_base取需要字段，并得到条线 ***/
			String[] args = new String[] { "cus_id","cus_id","cus_id"};
			String[] modelIds = new String[] { "CusBase","CusBase","CusBase"};
			String[] modelForeign = new String[] { "cus_id","cus_id","cus_id"};
			String[] fieldName = new String[] { "belg_line","cus_name","cust_mgr" };
			String[] resultName = new String[] { "belg_line","cus_name","cust_mgr" };
			SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			
			/*** 取条线并判断客户类型，再进一步处理客户信息 ***/
			String belg_line = kColl.getDataValue("belg_line").toString();
			Map<String,String> map = new HashMap<String, String>();
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			if(belg_line.equals("BL300")){	//个人客户处理
				args=new String[]{"cus_id","cus_id","cus_id"};
				modelIds=new String[]{"CusIndiv","CusIndiv","CusIndiv"};
				modelForeign=new String[]{"cus_id","cus_id","cus_id"};
				fieldName=new String[]{"mobile","indiv_rsd_addr","street3"};
				resultName=new String[]{"mobile","indiv_rsd_addr","street3"};
				SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
				
				map.put("indiv_rsd_addr", "STD_GB_AREA_ALL");
				SInfoUtils.addPopName(kColl, map, service);
			}else{	//对公客户处理
				args=new String[]{"cus_id","cus_id","cus_id"};
				modelIds=new String[]{"CusCom","CusCom","CusCom"};
				modelForeign=new String[]{"cus_id","cus_id","cus_id"};
				fieldName=new String[]{"legal_phone","acu_addr","street"};
				resultName=new String[]{"legal_phone","acu_addr","street"};
				SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
				
				map.put("acu_addr", "STD_GB_AREA_ALL");
				SInfoUtils.addPopName(kColl, map, service);
			}
			
			/*** 初步处理，从Acc_Loan取核销借据信息 ***/
			args = new String[] { "bill_no","bill_no","bill_no","bill_no","bill_no","bill_no","bill_no","prd_id" ,"bill_no","bill_no","bill_no","bill_no"};
			modelIds = new String[] { "AccLoan","AccLoan","AccLoan","AccLoan","AccLoan","AccLoan","AccLoan","PrdBasicinfo" ,"AccLoan","AccLoan","AccLoan","AccLoan"};
			modelForeign = new String[] { "bill_no","bill_no","bill_no","bill_no","bill_no","bill_no","bill_no","prdid" ,"bill_no","bill_no","bill_no","bill_no"};
			fieldName = new String[] { "prd_id","loan_amt","loan_balance","inner_owe_int","out_owe_int","distr_date","end_date","prdname" ,"manager_br_id","fina_br_id","acc_status","writeoff_date"};
			resultName = new String[] { "prd_id","loan_amt","loan_balance","inner_owe_int","out_owe_int","distr_date","end_date","prd_type","bill_manager_br_id","fina_br_id","acc_status","bill_writeoff_date"};
		    SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName,resultName);

			SInfoUtils.addSOrgName(kColl, new String[] { "bill_manager_br_id" ,"fina_br_id","writeoff_org"});
			SInfoUtils.addUSerName(kColl, new String[] { "cust_mgr"});			
			this.putDataElement2Context(kColl, context);			
			
			//显示台账信息
			String bill_no = (String) kColl.getDataValue("bill_no");
			KeyedCollection accpadkcoll = dao.queryDetail("AccPad", bill_no, connection);
			if(null!=accpadkcoll.getDataValue("bill_no")&&!"".equals(accpadkcoll.getDataValue("bill_no"))){
				String[] args2=new String[] {"cus_id","prd_id" };
				String[] modelIds2=new String[]{"CusBase","PrdBasicinfo"};
				String[]modelForeign2=new String[]{"cus_id","prdid"};
				String[] fieldName2=new String[]{"cus_name","prdname"};
			    //详细信息翻译时调用			
			    SystemTransUtils.dealName(accpadkcoll, args2, SystemTransUtils.ADD, context, modelIds2,modelForeign2, fieldName2);
				SInfoUtils.addSOrgName(accpadkcoll, new String[]{"fina_br_id"});  
				SInfoUtils.addSOrgName(accpadkcoll, new String[]{"manager_br_id"});
				this.putDataElement2Context(accpadkcoll, context);
				context.addDataField("acc", "accpad");
			}else{
				//判断是否是资产买入或者是资产证券化
				String condition = "where bill_no='"+bill_no+"' and asset_type='02'";
				
				String isSpecialAcc = ""; 
				KeyedCollection kCollAccAssetstrsf = dao.queryDetail("AccAssetstrsf", bill_no, connection);
				KeyedCollection kCollAccAssetTrans = dao.queryFirst("AccAssetTrans", null, condition, connection);
				String bill_no_select_4strsf = (String)kCollAccAssetstrsf.getDataValue("bill_no");
				String bill_no_select_4trans = (String)kCollAccAssetTrans.getDataValue("bill_no");
				//资产买入
				if(bill_no_select_4strsf != null && !"".equals(bill_no_select_4strsf)){
					isSpecialAcc = "AccAssetstrsf";
				}
				//资产证券化
				if(bill_no_select_4trans != null && !"".equals(bill_no_select_4trans)){
					isSpecialAcc = "AccAssetTrans";
				}
				context.put("isSpecialAcc", isSpecialAcc);
				
				KeyedCollection accloankcoll = dao.queryDetail("AccLoan", bill_no, connection);
				String[] args1=new String[] {"cus_id","prd_id" ,"cont_no"};
				String[] modelIds1=new String[]{"CusBase","PrdBasicinfo","CtrLoanCont"};
				String[]modelForeign1=new String[]{"cus_id","prdid","cont_no"};
				String[] fieldName1=new String[]{"cus_name","prdname","serno"};
				String[] resultName1=new String[]{"cus_id_displayname","prd_id_displayname","fount_serno"};
			    //详细信息翻译时调用
			    SystemTransUtils.dealPointName(accloankcoll, args1, SystemTransUtils.ADD, context, modelIds1,modelForeign1, fieldName1,resultName1);
				SInfoUtils.addSOrgName(accloankcoll, new String[]{"fina_br_id"});  
				SInfoUtils.addSOrgName(accloankcoll, new String[]{"manager_br_id"});
				this.putDataElement2Context(accloankcoll, context);
				context.addDataField("acc", "accloan");
			}
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