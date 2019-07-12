package com.yucheng.cmis.biz01line.fnc.op.importreports;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;

public class GetReportViewPageOp extends CMISOperation {


	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = null;
		try{
			connection = this.getConnection(context);
			String serno = null;
			String reportType = null;
			
			try{
				serno = (String)context.getDataValue("serno");
				reportType = (String)context.getDataValue("report_type");
			}catch (Exception e) {}
			if(serno == null || "".equals(serno)){
				EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "业务流水号为空！");
				throw new EMPException("业务流水号为空！");
			}
			
			if(reportType == null || "".equals(reportType)){
				EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "报表类型为空！");
				throw new EMPException("报表类型为空！");
			}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			if("01".equals(reportType)){
				getAssetAndDebt(serno, connection, context, dao);
				flag ="01";
			}else if("02".equals(reportType)){
				getProfitAndLoss(serno, connection, context, dao);
				flag = "02";
			}else if("03".equals(reportType)){
				getCurrentFlow(serno, connection, context, dao);
				flag = "03";
			}else if("04".equals(reportType)){
				getDiInvestigation(serno, connection, context, dao);
				flag = "04";
			}else if("05".equals(reportType)){
				getPlantInvestigation(serno, connection, context, dao);
				flag = "05";
			}else if("06".equals(reportType)){
				getBreedInvestigation(serno, connection, context, dao);
				flag = "06";
			}else{
				EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "报表类型错误！");
				throw new EMPException("报表类型错误！");
			}
		}catch (EMPException ee) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "查看报表错误！");
			throw ee;
		} catch(Exception e){
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "查看报表错误！");
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return flag;
	}	
	/**
	 * 查询资产负债表数据
	 * @param serno
	 * @param connection
	 * @param context
	 * @param dao
	 * @throws EMPJDBCException
	 * @throws InvalidArgumentException
	 */
	public void getAssetAndDebt(String serno,Connection connection,Context context,TableModelDAO dao) throws EMPJDBCException, InvalidArgumentException{
		IndexedCollection iColl = null;
		KeyedCollection kColl = null;
		String sernoCondition = "where serno='"+serno +"' ";
		String condition = null;
		String modelId = "IqpMeFncBs";
		
		condition = sernoCondition + " and fnc_flag ='160'";
		iColl = dao.queryList(modelId, condition, connection);
		iColl.setName("CurrentAndDepositList");
		this.putDataElement2Context(iColl, context);
		
		condition = sernoCondition + " and fnc_flag ='161'";
		iColl = dao.queryList(modelId, condition, connection);
		iColl.setName("AccountReceivableList");
		this.putDataElement2Context(iColl, context);
		
		condition = sernoCondition + " and fnc_flag ='162'";
		iColl = dao.queryList(modelId, condition, connection);
		iColl.setName("AccountPrePayableList");
		this.putDataElement2Context(iColl, context);
		
		condition = sernoCondition + " and fnc_flag ='163'";
		iColl = dao.queryList(modelId, condition, connection);
		iColl.setName("StockList");
		this.putDataElement2Context(iColl, context);
		
		condition = sernoCondition + " and fnc_flag ='164'";
		iColl = dao.queryList(modelId, condition, connection);
		iColl.setName("FixedAssetList");
		this.putDataElement2Context(iColl, context);
		
		condition = sernoCondition + " and fnc_flag ='165'";
		iColl = dao.queryList(modelId, condition, connection);
		iColl.setName("OtherAssetList");
		this.putDataElement2Context(iColl, context);
		
		condition = sernoCondition + " and fnc_flag ='166'";
		iColl = dao.queryList(modelId, condition, connection);
		iColl.setName("OtherAssetAndNontableAssetList");
		this.putDataElement2Context(iColl, context);
		
		condition = sernoCondition + " and fnc_flag ='167'";
		iColl = dao.queryList(modelId, condition, connection);
		iColl.setName("HouseList");
		this.putDataElement2Context(iColl, context);
		
		condition = sernoCondition + " and fnc_flag ='168'";
		iColl = dao.queryList(modelId, condition, connection);
		iColl.setName("MotorCarList");
		this.putDataElement2Context(iColl, context);
		
		condition = sernoCondition + " and fnc_flag ='169'";
		iColl = dao.queryList(modelId, condition, connection);
		iColl.setName("BankAccountList");
		this.putDataElement2Context(iColl, context);
		
		condition = sernoCondition + " and fnc_flag ='170'";
		iColl = dao.queryList(modelId, condition, connection);
		iColl.setName("PayableVendorAccountList");
		this.putDataElement2Context(iColl, context);
		
		condition = sernoCondition + " and fnc_flag ='171'";
		iColl = dao.queryList(modelId, condition, connection);
		iColl.setName("PrereceiveableAccountList");
		this.putDataElement2Context(iColl, context);
		
		condition = sernoCondition + " and fnc_flag ='172'";
		iColl = dao.queryList(modelId, condition, connection);
		iColl.setName("BankDebtList");
		this.putDataElement2Context(iColl, context);
		
		condition = sernoCondition + " and fnc_flag ='173'";
		iColl = dao.queryList(modelId, condition, connection);
		iColl.setName("PayableOtherAccountList");
		this.putDataElement2Context(iColl, context);
		
		condition = sernoCondition + " and fnc_flag ='174'";
		kColl = dao.queryFirst(modelId,null, condition, connection);
		kColl.setName("Other");
		this.putDataElement2Context(kColl, context);
		
		condition = sernoCondition + " and fnc_type ='152'";
		kColl = dao.queryFirst(modelId,null, condition, connection);
		kColl.setName("TableInfo");
		this.putDataElement2Context(kColl, context);
	}
	/**
	 * 查询损益表数据
	 * @param serno
	 * @param connection
	 * @param context
	 * @param dao
	 * @throws EMPJDBCException
	 * @throws InvalidArgumentException
	 * @throws ObjectNotFoundException
	 */
	public void getProfitAndLoss(String serno,Connection connection,Context context,TableModelDAO dao) throws EMPJDBCException, InvalidArgumentException, ObjectNotFoundException{
		IndexedCollection iColl = null;
		KeyedCollection kColl = null;
		String sernoCondition = "where serno='"+serno +"' ";
		String condition = null;
		String modelId = "IqpMeFncPl";
		
		condition = sernoCondition + " and category <> '19' order by category,item_no";
		iColl = dao.queryList(modelId, condition, connection);
		iColl.setName("DataList");
		this.putDataElement2Context(iColl, context);
		
		condition = sernoCondition + " and category ='19'";
		kColl = dao.queryFirst(modelId,null, condition, connection);
		kColl.setName("TableInfo");
		this.putDataElement2Context(kColl, context);
		context.put("mon_1", kColl.getDataValue("mon_1"));
		context.put("mon_2", kColl.getDataValue("mon_2"));
		context.put("mon_3", kColl.getDataValue("mon_3"));
		context.put("mon_4", kColl.getDataValue("mon_4"));
		context.put("mon_5", kColl.getDataValue("mon_5"));
		context.put("mon_6", kColl.getDataValue("mon_6"));
		context.put("mon_7", kColl.getDataValue("mon_7"));
		context.put("mon_8", kColl.getDataValue("mon_8"));
		context.put("mon_9", kColl.getDataValue("mon_9"));
		context.put("mon_10", kColl.getDataValue("mon_10"));
		context.put("mon_11", kColl.getDataValue("mon_11"));
		context.put("mon_12", kColl.getDataValue("mon_12"));
	}
	/**
	 * 查询现金流量表数据
	 * @param serno
	 * @param connection
	 * @param context
	 * @param dao
	 * @throws EMPJDBCException
	 * @throws InvalidArgumentException
	 * @throws ObjectNotFoundException
	 */
	public void getCurrentFlow(String serno,Connection connection,Context context,TableModelDAO dao) throws EMPJDBCException, InvalidArgumentException, ObjectNotFoundException{
		IndexedCollection iColl = null;
		KeyedCollection kColl = null;
		String sernoCondition = "where serno='"+serno +"' ";
		String condition = null;
		String modelId = "IqpMeFncCf";
		
		condition = sernoCondition + " and category <> '28' order by category";
		iColl = dao.queryList(modelId, condition, connection);
		iColl.setName("DataList");
		this.putDataElement2Context(iColl, context);
		
		condition = sernoCondition + " and category ='28'";
		kColl = dao.queryFirst(modelId,null, condition, connection);
		kColl.setName("TableInfo");
		this.putDataElement2Context(kColl, context);
		context.put("mon_pre_5", kColl.getDataValue("mon_pre_5"));
		context.put("mon_pre_4", kColl.getDataValue("mon_pre_4"));
		context.put("mon_pre_3", kColl.getDataValue("mon_pre_3"));
		context.put("mon_pre_2", kColl.getDataValue("mon_pre_2"));
		context.put("mon_pre_1", kColl.getDataValue("mon_pre_1"));
		context.put("mon_pre_0", kColl.getDataValue("mon_pre_0"));
		context.put("mon_aft_0", kColl.getDataValue("mon_aft_0"));
		context.put("mon_aft_1", kColl.getDataValue("mon_aft_1"));
		context.put("mon_aft_2", kColl.getDataValue("mon_aft_2"));
		context.put("mon_aft_3", kColl.getDataValue("mon_aft_3"));
		context.put("mon_aft_4", kColl.getDataValue("mon_aft_4"));
		context.put("mon_aft_5", kColl.getDataValue("mon_aft_5"));
		context.put("mon_aft_6", kColl.getDataValue("mon_aft_6"));
		context.put("mon_aft_7", kColl.getDataValue("mon_aft_7"));
		context.put("mon_aft_8", kColl.getDataValue("mon_aft_8"));
		context.put("mon_aft_9", kColl.getDataValue("mon_aft_9"));
		context.put("mon_aft_10", kColl.getDataValue("mon_aft_10"));
		context.put("mon_aft_11", kColl.getDataValue("mon_aft_11"));
		context.put("mon_aft_12", kColl.getDataValue("mon_aft_12"));
	}
	/**
	 * 查询抵好贷调查表数据
	 * @param serno
	 * @param connection
	 * @param context
	 * @param dao
	 * @throws EMPJDBCException
	 * @throws InvalidArgumentException
	 * @throws ObjectNotFoundException
	 */
	public void getDiInvestigation(String serno,Connection connection,Context context,TableModelDAO dao) throws EMPJDBCException, InvalidArgumentException, ObjectNotFoundException{
		IndexedCollection iColl = null;
		KeyedCollection kColl = null;
		String sernoCondition = "where serno='"+serno +"' ";
		String modelId = "IqpMeFncDi";
		
		kColl = (KeyedCollection)dao.queryFirst(modelId, null, sernoCondition, connection);
		kColl.setName("MainInfo");
		this.putDataElement2Context(kColl, context);
		
		iColl = dao.queryList("IqpMeFncDiMtg", sernoCondition, connection);
		iColl.setName("MtgList");
		this.putDataElement2Context(iColl, context);
		
		iColl = dao.queryList("IqpMeFncDiParty", sernoCondition, connection);
		iColl.setName("PartyList");
		this.putDataElement2Context(iColl, context);
	}
	/**
	 * 查询种植情况调查表数据
	 * @param serno
	 * @param connection
	 * @param context
	 * @param dao
	 * @throws EMPJDBCException
	 * @throws InvalidArgumentException
	 * @throws ObjectNotFoundException
	 */
	public void getPlantInvestigation(String serno,Connection connection,Context context,TableModelDAO dao) throws EMPJDBCException, InvalidArgumentException, ObjectNotFoundException{
		IndexedCollection iColl = null;
		KeyedCollection kColl = null;
		String sernoCondition = "where serno='"+serno +"' ";
		String condition = null;
		String modelId = "IqpMeFncPlant";
		
		condition = sernoCondition + " and category = '01'";
		iColl = dao.queryList(modelId, condition, connection);
		iColl.setName("MainInfoList");
		this.putDataElement2Context(iColl, context);
		
		condition = sernoCondition + " and category = '02'";
		iColl = dao.queryList(modelId, condition, connection);
		iColl.setName("CostList");
		this.putDataElement2Context(iColl, context);
		
		condition = sernoCondition + " and category = '03'";
		iColl = dao.queryList(modelId, condition, connection);
		iColl.setName("PredictionList");
		this.putDataElement2Context(iColl, context);
		
		condition = sernoCondition + " and category = '04'";
		kColl = (KeyedCollection)dao.queryFirst(modelId, null, condition, connection);
		kColl.setName("TableInfo");
		this.putDataElement2Context(kColl, context);
		
	}
	/**
	 * 查询 养殖情况调查表数据
	 * @param serno
	 * @param connection
	 * @param context
	 * @param dao
	 * @throws EMPJDBCException
	 * @throws InvalidArgumentException
	 * @throws ObjectNotFoundException
	 */
	public void getBreedInvestigation(String serno,Connection connection,Context context,TableModelDAO dao) throws EMPJDBCException, InvalidArgumentException, ObjectNotFoundException{
		IndexedCollection iColl = null;
		KeyedCollection kColl = null;
		String sernoCondition = "where serno='"+serno +"' ";
		String condition = null;
		String modelId = "IqpMeFncBreed";
		
		condition = sernoCondition + " and category = '01'";
		iColl = dao.queryList(modelId, condition, connection);
		iColl.setName("MainInfoList");
		this.putDataElement2Context(iColl, context);
		
		condition = sernoCondition + " and category = '02'";
		iColl = dao.queryList(modelId, condition, connection);
		iColl.setName("CostList");
		this.putDataElement2Context(iColl, context);
		
		condition = sernoCondition + " and category = '03'";
		iColl = dao.queryList(modelId, condition, connection);
		iColl.setName("PredictionList");
		this.putDataElement2Context(iColl, context);
		
		condition = sernoCondition + " and category = '04'";
		kColl = (KeyedCollection)dao.queryFirst(modelId, null, condition, connection);
		kColl.setName("TableInfo");
		this.putDataElement2Context(kColl, context);
		
	}
}
