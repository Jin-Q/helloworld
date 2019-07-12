package com.yucheng.cmis.biz01line.pvp.op.pvpassetstrsf;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddPvpAssetstrsfRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String contModelId = "CtrAssetstrsfCont";
	
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
//			recordRestrict.judgeUpdateRestrict(contModelId, context, connection);
			
			KeyedCollection kColl = null;
			KeyedCollection pvpKColl = new KeyedCollection();
			String cont_no ="";
			String input_br_id ="";
			String input_date ="";
			String input_id="";
			String prd_id="";
			String manager_br_id="";
			try {
				/**从合同表中获取的数据 */
				cont_no = (String)context.getDataValue("cont_no");
				prd_id =(String)context.getDataValue("prd_id");
				input_id = (String)context.getDataValue("input_id");
				input_br_id = (String)context.getDataValue("input_br_id");
				input_date = (String)context.getDataValue("input_date");
				manager_br_id =(String)context.getDataValue("manager_br_id");
				pvpKColl.addDataField("cont_no", cont_no);
				pvpKColl.addDataField("prd_id", prd_id);
				pvpKColl.addDataField("input_id", input_id);
				pvpKColl.addDataField("input_br_id", input_br_id);
				pvpKColl.addDataField("input_date", input_date);
				pvpKColl.addDataField("approve_status", "000");//出账状态
				pvpKColl.addDataField("manager_br_id", manager_br_id);
				pvpKColl.setName("PvpLoanApp");
			} catch (Exception e) {}
			if(cont_no == null)
				throw new EMPJDBCException("The values cannot be empty!");
//			String[] args=new String[] { "prd_id" };
//			String[] modelIds=new String[]{"PrdBasicinfo"};
//			String[] fieldName=new String[]{"prdname"};
//			String[] modelForeign=new String[]{"prdid"};
//			/** 获取借据编号，只做测试使用 */
//			PvpComponent cmisComponent = (PvpComponent)CMISComponentFactory.getComponentFactoryInstance()
//				.getComponentInstance(PvpConstant.PVPCOMPONENT, context, connection);
//			String billNo = cmisComponent.getBillNoByContNo(cont_no);
//			pvpKColl.addDataField("bill_no", billNo);
			
			TableModelDAO dao = this.getTableModelDAO(context);
            //通过合同编号查询合同详细信息
			KeyedCollection contKColl = dao.queryAllDetail(contModelId, cont_no, connection);	
            //在出账pvpKColl中放入相关信息
			String takeover_total_amt=(String) contKColl.getDataValue("takeover_total_amt");
			String toorg_no=(String) contKColl.getDataValue("toorg_no");
			String toorg_name=(String) contKColl.getDataValue("toorg_name");
			pvpKColl.addDataField("cont_amt", takeover_total_amt);
			pvpKColl.addDataField("pvp_amt", takeover_total_amt);
			pvpKColl.addDataField("cont_balance", takeover_total_amt);
			pvpKColl.addDataField("cus_id", toorg_no); 
			pvpKColl.addDataField("toorg_name", toorg_name); 
			pvpKColl.addDataField("cur_type", contKColl.getDataValue("acct_curr")); 
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			pvpKColl.addDataField("serno", serno);
			dao.insert(pvpKColl, connection);
//            //详细信息翻译时调用	
//			SystemTransUtils.dealName(pvpKColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
//			this.putDataElement2Context(pvpKColl, context);
//			
//			/** 组织机构、登记机构翻译 */
//			SInfoUtils.addUSerName(pvpKColl, new String[]{"input_id"});
//			SInfoUtils.addSOrgName(pvpKColl, new String[]{"manager_br_id","input_br_id"});
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
