package com.yucheng.cmis.biz01line.mort.mortguarantyevalvalue;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckGuarantyIsFinishOp  extends CMISOperation {
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String guaranty_no = "";
		String model="";
		String guaranty_type = "";
		try{
			connection = this.getConnection(context);
			guaranty_no = (String) context.getDataValue("guaranty_no");
			guaranty_type = (String) context.getDataValue("guaranty_type");
			//根据押品类型判断押品的详细信息所存储的表模型
			//标准仓单
			if(guaranty_type.equals("Z010101")){
				model = "MortStandardDepotBill";
			}
			//住宅  20150814  Edited By FCL 原有自建房更改为城市自建房，新增农村自建房C010106
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
			//XD150714054  2015-08-18 Edited by FCL 新增土地承包经营权
			if(guaranty_type.equals("Z079902")){
				model = "MortLandMgrRightDetail";
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
			if(guaranty_type.equals("A060101")||guaranty_type.equals("A060201")||guaranty_type.equals("A060301")){
				model = "MortStockRight";
			}
			//股票/基金
			if(guaranty_type.equals("A050101")||guaranty_type.equals("A050301")||guaranty_type.equals("A050401")){
				model = "MortStockFund";
			}
			//其他金融产品质押
			if(guaranty_type.equals("A080101")||guaranty_type.equals("A080102")||guaranty_type.equals("A080103")){
				model = "MortFinanceOthers";
			}
			//应收账款其他可转让的权利
			if(guaranty_type.equals("B030101")||guaranty_type.equals("B030201")||guaranty_type.equals("B030301")||guaranty_type.equals("B030401")||guaranty_type.equals("B040101")||guaranty_type.equals("B040102")||guaranty_type.equals("B040103")||guaranty_type.equals("B040104")||guaranty_type.equals("B040201")
					||guaranty_type.equals("B040202")||guaranty_type.equals("B040203")||guaranty_type.equals("B040301")||guaranty_type.equals("B040401")||guaranty_type.equals("B050101")||guaranty_type.equals("B060101")||guaranty_type.equals("B060102")||guaranty_type.equals("B060201")||guaranty_type.equals("B060301")){
				model = "MortAccountsReceOthers";
			}
			//交易类应收账款
			if(guaranty_type.equals("B020101")){
				model = "MortAccountsReceivable";
			}
			//其他所有权
			if(guaranty_type.equals("Z070301")||guaranty_type.equals("Z080101")||guaranty_type.equals("Z080201")||guaranty_type.equals("Z080301")||guaranty_type.equals("Z070101")||guaranty_type.equals("Z079901")||guaranty_type.equals("Z080401")
					||guaranty_type.equals("Z089901")){
				model = "MortOwnershipOthers";
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
			//拼接where条件
			String conStr = "where guaranty_no = '"+guaranty_no+"'";
			TableModelDAO dao = this.getTableModelDAO(context);
			//查询是否已经录入相关押品的详细信息
			IndexedCollection iC = dao.queryList(model, conStr, connection);
			if(iC.size()==0){
				context.addDataField("flag","fail");
			}else{
				context.addDataField("flag","true");
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
