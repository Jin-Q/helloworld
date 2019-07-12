package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddPvpLoanAppRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "PvpLoanApp";
	private final String contModelId = "CtrLoanCont";
	private final String contSubModelId = "CtrLoanContSub";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
//			RecordRestrict recordRestrict = this.getRecordRestrict(context);
//			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			
			KeyedCollection kColl = null;
			KeyedCollection pvpKColl = new KeyedCollection();
			String cont_no ="";
			String input_br_id ="";
			String input_date ="";
			String approve_status ="";
			String input_id="";
			String prd_id="";
			String cus_id="";
			String manager_br_id="";
			String in_acct_br_id="";
			try {
				/**从合同表中获取的数据 */
				cont_no = (String)context.getDataValue("cont_no");
				prd_id =(String)context.getDataValue("prd_id");
				input_id = (String)context.getDataValue("input_id");
				input_br_id = (String)context.getDataValue("input_br_id");
				input_date = (String)context.getDataValue("input_date");
				approve_status = (String)context.getDataValue("approve_status");
				cus_id = (String)context.getDataValue("cus_id");
				manager_br_id =(String)context.getDataValue("manager_br_id");
				in_acct_br_id = (String)context.getDataValue("in_acct_br_id");
				pvpKColl.addDataField("cont_no", cont_no);
				pvpKColl.addDataField("prd_id", prd_id);
				pvpKColl.addDataField("cus_id", cus_id);
				pvpKColl.addDataField("input_id", context.getDataValue("currentUserId").toString());
				pvpKColl.addDataField("input_br_id", context.getDataValue("organNo").toString());
				pvpKColl.addDataField("input_date", context.getDataValue("OPENDAY").toString());
				pvpKColl.addDataField("approve_status", "000");//审批状态
				pvpKColl.addDataField("manager_br_id", manager_br_id);
				//pvpKColl.addDataField("in_acct_br_id", in_acct_br_id);
				pvpKColl.setName("PvpLoanApp");
			} catch (Exception e) {}
			if(cont_no == null)
				throw new EMPJDBCException("The values cannot be empty!");
//			String[] args=new String[] { "prd_id" };
//			String[] pvpArgs=new String[] { "cus_id" };
//			String[] modelIds=new String[]{"PrdBasicinfo"};
//			String[] pvpModelIds=new String[]{"CusBase"};
//			String[] fieldName=new String[]{"prdname"};
//			String[] modelForeign=new String[]{"prdid"};
//			String[] pvpFieldName=new String[]{"cus_name"};
//			/** 获取借据编号，只做测试使用 */
//			PvpComponent cmisComponent = (PvpComponent)CMISComponentFactory.getComponentFactoryInstance()
//				.getComponentInstance(PvpConstant.PVPCOMPONENT, context, connection);
//			String billNo = cmisComponent.getBillNoByContNo(cont_no);
//			pvpKColl.addDataField("bill_no", billNo);
			
			TableModelDAO dao = this.getTableModelDAO(context);
            //通过合同编号查询合同详细信息
			KeyedCollection contKColl = dao.queryAllDetail(contModelId, cont_no, connection);	
			//通过合同编号查询合同子表详细信息
			KeyedCollection contSubKColl = dao.queryAllDetail(contSubModelId, cont_no, connection);
            //在出账pvpKColl中放入相关信息
			if(contSubKColl.containsKey("is_delay")&&contSubKColl.getDataValue("is_delay")!=null&&!"".equals(contSubKColl.getDataValue("is_delay"))){
				pvpKColl.put("is_delay", contSubKColl.getDataValue("is_delay").toString());
			}
			String cont_amt=(String) contKColl.getDataValue("cont_amt");
			pvpKColl.addDataField("cont_amt", cont_amt);
			pvpKColl.addDataField("pvp_amt", cont_amt);
			pvpKColl.addDataField("cont_balance", cont_amt);
			pvpKColl.addDataField("cur_type", contKColl.getDataValue("cont_cur_type"));
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			pvpKColl.addDataField("serno", serno);
			dao.insert(pvpKColl, connection);
//            //详细信息翻译时调用	
//			SystemTransUtils.dealName(contKColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
//			SystemTransUtils.dealName(contKColl, pvpArgs, SystemTransUtils.ADD, context, pvpModelIds,pvpArgs, pvpFieldName);
//			this.putDataElement2Context(contKColl, context);
//			this.putDataElement2Context(pvpKColl, context);
//			
//			/** 组织机构、登记机构翻译 */
//			SInfoUtils.addUSerName(pvpKColl, new String[]{"input_id"});
//			SInfoUtils.addSOrgName(pvpKColl, new String[]{"manager_br_id","input_br_id","in_acct_br_id"});
			if(context.containsKey("flag")){
				context.setDataValue("flag", "success");
			}else{
				context.addDataField("flag", "success");
			}
			if(context.containsKey("serno")){
				context.setDataValue("serno", serno);
			}else{
				context.addDataField("serno", serno);
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
