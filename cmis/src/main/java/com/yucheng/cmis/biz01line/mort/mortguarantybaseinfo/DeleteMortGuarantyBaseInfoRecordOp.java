package com.yucheng.cmis.biz01line.mort.mortguarantybaseinfo;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.biz01line.mort.component.MortCommenOwnerComponent;
import com.yucheng.cmis.biz01line.mort.morttool.MORTConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class DeleteMortGuarantyBaseInfoRecordOp extends CMISOperation {

	private final String modelId = "MortGuarantyBaseInfo";
	private final String guaranty_no_name = "guaranty_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String guaranty_type = null;
		//详细信息所存储的表模型 
		String model = "";
		try{
			connection = this.getConnection(context);			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);
			String guaranty_no_value = null;
			String flag = "";
			try {
				guaranty_no_value = (String)context.getDataValue(guaranty_no_name);
				guaranty_type = (String) context.getDataValue("guaranty_type");
			} catch (Exception e) {}
			if(guaranty_no_value == null || guaranty_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+guaranty_no_name+"] cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);
			
			//获取详细信息所存储的表模型
			//标准仓单
			if(guaranty_type.equals("Z010101")){
				model = "MortStandardDepotBill";
			}
			//住宅 2015-08-14 Edited By FCL 原自建房变更为城市自建房 新增农村自建房
			if(guaranty_type.equals("C010101")||guaranty_type.equals("C010102")||guaranty_type.equals("C010103")||guaranty_type.equals("C010105")||guaranty_type.equals("C010106")||guaranty_type.equals("C010199")){
				model = "MortRealHouseDetail";
			}
			//车库
			if(guaranty_type.equals("C010104")){
				model = "MortCarBarnDetail";
			}
			//商铺、商场
			if(guaranty_type.equals("C020201")){
				model = "MortMarketStore";
			}
			//标准工业厂房
			if(guaranty_type.equals("C020301")||guaranty_type.equals("C020302")||guaranty_type.equals("C020303")||guaranty_type.equals("C020304")){
				model = "MortPlant";
			}
			//酒店、写字楼
			if(guaranty_type.equals("C020101")||guaranty_type.equals("C020103")||guaranty_type.equals("C020105")||guaranty_type.equals("C020106")||guaranty_type.equals("C020107")){
				model = "MortHotelOffice";
			}
			//土地使用权
			if(guaranty_type.equals("C030101")||guaranty_type.equals("C040101")||guaranty_type.equals("C040201")||guaranty_type.equals("C040301")||guaranty_type.equals("C040401")){
				model = "MortUsufructLand";
			}
			//XD150714054  2015-08-18 Edited by FCL 新增土地承包经营权
			if(guaranty_type.equals("Z079902")){
				model = "MortLandMgrRightDetail";
			}
			//在建工程
			if(guaranty_type.equals("C050101")){
				model = "MortBuildingProject";
			}
			//机动车辆
			if(guaranty_type.equals("Z040101")||guaranty_type.equals("Z040102")||guaranty_type.equals("Z040103")||guaranty_type.equals("Z040104")){
				model = "MortTrafficCar";
			}
			//船舶
			if(guaranty_type.equals("Z040201")||guaranty_type.equals("Z040202")||guaranty_type.equals("Z040203")||guaranty_type.equals("Z040299")){
				model = "MortTrafficShip";
			}
			//飞机
			if(guaranty_type.equals("Z040301")||guaranty_type.equals("Z040302")||guaranty_type.equals("Z040303")){
				model = "MortTrafficOthers";
			}
			//森林使用权
			if(guaranty_type.equals("Z070201")){
				model = "MortUsufructForest";
			}
			//海域使用权
			if(guaranty_type.equals("Z070401")){
				model = "MortUsufructSea";
			}
			//机器设备
			if(guaranty_type.equals("Z030101")||guaranty_type.equals("Z030102")||guaranty_type.equals("Z030103")||guaranty_type.equals("Z030104")||guaranty_type.equals("Z030105")||guaranty_type.equals("Z030106")||guaranty_type.equals("Z030107")||guaranty_type.equals("Z030108")||guaranty_type.equals("Z030199")
					||guaranty_type.equals("Z030201")||guaranty_type.equals("Z030202")||guaranty_type.equals("Z030203")||guaranty_type.equals("Z030204")||guaranty_type.equals("Z030205")||guaranty_type.equals("Z030206")||guaranty_type.equals("Z030207")||guaranty_type.equals("Z030208")||guaranty_type.equals("Z030209")
					||guaranty_type.equals("Z030299")){
				model = "MortMachineEquipment";
			}
			//存单质押
			if(guaranty_type.equals("A010101")||guaranty_type.equals("A010102")||guaranty_type.equals("A010201")){
				model = "MortDepositReceipt";
			}
			//贵金属
			if(guaranty_type.equals("A020101")||guaranty_type.equals("A020201")){
				model = "MortNobleMetal";
			}
			//债券抵押
			if(guaranty_type.equals("A030101")||guaranty_type.equals("A030102")||guaranty_type.equals("A030103")||guaranty_type.equals("A030201")||guaranty_type.equals("A030202")
					||guaranty_type.equals("A030301")||guaranty_type.equals("A030302")||guaranty_type.equals("A030303")||guaranty_type.equals("A030304")||guaranty_type.equals("A030305")
					||guaranty_type.equals("A030306")||guaranty_type.equals("A030401")||guaranty_type.equals("A030402")||guaranty_type.equals("A030403")||guaranty_type.equals("A030404")
					||guaranty_type.equals("A030501")||guaranty_type.equals("A030502")||guaranty_type.equals("A030503")||guaranty_type.equals("A030504")||guaranty_type.equals("A030505")
					||guaranty_type.equals("A030601")||guaranty_type.equals("A030602")||guaranty_type.equals("A030701")||guaranty_type.equals("A050201")){
				model = "MortBondPledge";
			}
			//保单
			if(guaranty_type.equals("A070101")){
				model = "MortInsuranceSlip";
			}
			//票据
			if(guaranty_type.equals("A040101")||guaranty_type.equals("A040201")||guaranty_type.equals("A040301")){
				model = "MortBill";
			}
			//提单
			if(guaranty_type.equals("Z010201")){
				model = "MortLadingBill";
			}
			//出口退税账户
			if(guaranty_type.equals("B010101")){
				model = "MortExportReturnTax";
			}
			//股权
			if(guaranty_type.equals("A040101")||guaranty_type.equals("A040201")||guaranty_type.equals("A040301")||guaranty_type.equals("A060101")){
				model = "MortStockRight";
			}
			//股票/基金
			if(guaranty_type.equals("A050101")||guaranty_type.equals("A050301")||guaranty_type.equals("A050401")){
				model = "MortStockFund";
			}
			//其他金融产品质押
			if(guaranty_type.equals("A080101")||guaranty_type.equals("A080102")||guaranty_type.equals("A080103")||guaranty_type.equals("A080104")){
				model = "MortFinanceOthers";
			}
			//应收账款其他可转让的权利
			if(guaranty_type.equals("B030101")||guaranty_type.equals("B030201")||guaranty_type.equals("B030301")||guaranty_type.equals("B030401")||guaranty_type.equals("B040101")||guaranty_type.equals("B040102")||guaranty_type.equals("B040103")||guaranty_type.equals("B040104")||guaranty_type.equals("B040201")
					||guaranty_type.equals("B040202")||guaranty_type.equals("B040203")||guaranty_type.equals("B040301")||guaranty_type.equals("B040401")||guaranty_type.equals("B050101")||guaranty_type.equals("B060101")||guaranty_type.equals("B060102")||guaranty_type.equals("B060201")||guaranty_type.equals("B060301")){
				model = "MortAccountsReceOthers";
			}
			//交易类应收账款
			if(guaranty_type.equals("B020101")){
				model = "accounts_receivable";
			}
			//其他所有权
			if(guaranty_type.equals("Z070301")||guaranty_type.equals("Z080101")||guaranty_type.equals("Z080201")||guaranty_type.equals("Z080301")||guaranty_type.equals("Z070101")||guaranty_type.equals("Z079901")||guaranty_type.equals("Z080401")
					||guaranty_type.equals("Z089901")){
				model = "MortAccountsReceOthers";
			}
			//保函
			if(guaranty_type.equals("Z080601")){
				model = "MortBackletter";
			}
			//备用信用证
			if(guaranty_type.equals("Z080501")){
				model = "MortReserveCredit";
			}
			//其他质押品
			if(guaranty_type.equals("Z999999")){
				model = "MortImpawnOthers";
			}
			//汽车合格证
			if(guaranty_type.equals("T010101")){
				model = "MortCarCertification";
			}
			//其他托管品
			if(guaranty_type.equals("T060101")){
				model = "MortManagedProductOthers";
			}
			//其他抵押品
			if(guaranty_type.equals("Z989999")||guaranty_type.equals("Z989998")){
				model = "MortMortOthers";
			}
			//汽车履约保证保险
			if(guaranty_type.equals("T020101")||guaranty_type.equals("T030101")){
				model = "MortCarPledgeInsurance";
			}
			//货物质押
			if(guaranty_type.substring(0,5).equals("Z0901")){
				model = "MortCargoPledge";
			}
			IndexedCollection ic  = dao.queryList("GrtGuarantyRe", "where guaranty_id = '"+guaranty_no_value+"'", connection);
			if(ic.size()!=0){
				context.addDataField("delet","flase");
				return "0";
			}
			if(!"".equals(model)){
				//根据押品编号删除详细信息表中的记录
				Map<String,String> refFields = new HashMap<String,String>();
				refFields.put("guaranty_no", guaranty_no_value);
				LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
				lmtComponent.deleteByField(model,refFields);
				if("MortLandMgrRightDetail".equals(model)){//XD150714054  2015-08-18 Edited by FCL 新增土地承包经营权
					lmtComponent.deleteByField("MortLandBelongs",refFields);
				}
			}
			int count=dao.deleteByPk(modelId, guaranty_no_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			//构建组件类
			MortCommenOwnerComponent commenOwner = (MortCommenOwnerComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(
					MORTConstant.MORTCOMMENOWNERCOMPONENT, context, connection);
			//删除货物与监管协议的关联记录（商链通押品管理时）
			if(context.containsKey("flag")){
				commenOwner.deleteCarOverReByGuarantyNo(guaranty_no_value);
			}
			commenOwner.deleteAllByGuarantyNo(guaranty_no_value);
			context.addDataField("delet","true");
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
