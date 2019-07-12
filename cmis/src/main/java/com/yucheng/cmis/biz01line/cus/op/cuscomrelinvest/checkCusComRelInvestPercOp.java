package com.yucheng.cmis.biz01line.cus.op.cuscomrelinvest;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.cuscom.component.CusComRelApitalComponent;
import com.yucheng.cmis.biz01line.cus.cuscom.component.CusComRelInvestComponent;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusComRelInvest;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class checkCusComRelInvestPercOp extends CMISOperation {
	
//	private final String modelId = "CusComRelInvest";
	private final String modelId = "CusComRelApital";
	
	/**
	 */
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String flag = "可以新增";
		try{
			connection = this.getConnection(context);
			String cusId = (String)context.getDataValue("cus_id");//投资人
			String op = (String)context.getDataValue("op");
			String cusIdRel = (String)context.getDataValue("cus_id_rel");//被投资人
			String tzje = (String)context.getDataValue("invt_amt");
			String cur_type = (String)context.getDataValue("cur_type");//币种
			double invt_amt = Double.parseDouble(tzje);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			KeyedCollection kColl = service.getHLByCurrType(cur_type, context, connection);
			if("success".equals(kColl.getDataValue("flag"))){
				double sld = ((BigDecimal)kColl.getDataValue("sld")).doubleValue();//汇率信息
				invt_amt = invt_amt*sld;//本次出资金额折合人民币
			}else {
//				throw new Exception((String)kColl.getDataValue("msg"));
				flag = "汇率表中未取到币种【"+cur_type+"】匹配汇率，请确认汇率表汇率配置";
				context.addDataField("flag", flag);
				return "0";
			}
			
			//格式保留两位小数
			DecimalFormat df = new DecimalFormat("##########0.00");
		    //获得被投资人 资本构成信息
			CusComRelInvest cusComRelInvest = new CusComRelInvest();
			CusComRelInvestComponent cusComRelInvestComponent = (CusComRelInvestComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSCOMRELINVEST,context,connection);

			CusComRelApitalComponent cusComRelApitalComponent = (CusComRelApitalComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSCOMRELAPITAL,context,connection);
			
			Map map = cusComRelApitalComponent.getRegAmt(cusIdRel);//获得该客户的注册资本及币种
			String zcbz = (String) map.get("paid_cap_cur_type");
			double zczb = (Double) map.get("paid_cap_amt");
			if(zcbz==null || "".equals(zcbz)){//被投资人注册资本信息没完善
				flag = "请先完善被投资人注册资本信息，并保存！";
				context.addDataField("flag", flag);
				return "0";
			}
			KeyedCollection kColl1 = service.getHLByCurrType(zcbz, context, connection);
			if("success".equals(kColl1.getDataValue("flag"))){
				double sld = ((BigDecimal)kColl1.getDataValue("sld")).doubleValue();//汇率信息
				zczb = zczb*sld;//本次出资金额折合人民币
			}else {
//				throw new Exception((String)kColl1.getDataValue("msg"));
				flag = "汇率表中未取到币种【"+zcbz+"】匹配汇率，请确认汇率表汇率配置";
				context.addDataField("flag", flag);
				return "0";
			}
			cusComRelInvest =cusComRelInvestComponent.getCusComRelInvest(cusId,cusIdRel);//获得被投资人资本构成信息
			if(op.equals("add")){
				if(cusComRelInvest.getCusId() != null){
					flag = "客户【"+cusIdRel+"】的资本构成中已经存在客户【"+cusId+"】";
				}else{
//					double zczb = cusComRelApitalComponent.getRegAmt(cusIdRel);//获得该客户的注册资本
//					double xyzh = cusComRelApitalComponent.getSumInvrtAmt(cusIdRel);//获得该客户的现有注册资本总和
//					if(invt_amt > BigDecimalUtil.sub(zczb, xyzh, 4, BigDecimal.ROUND_HALF_UP)){
//						flag = "客户【"+cusIdRel+"】注册资本总和【"+df.format(zczb)+"】万元，已有出资金额【"+df.format(xyzh)+"】万元，可以新增出资金额【"+df.format(BigDecimalUtil.sub(zczb, xyzh, 4, BigDecimal.ROUND_HALF_UP))+"】万元，本次出资金额【"+df.format(invt_amt)+"】万元";//超比例
//					}
					if(zczb<=0){
						flag = "客户【"+cusIdRel+"】注册资本总和【"+df.format(zczb)+"】万元，请先去客户基本信息中录入！";
					}
					double otherAmt = 0;//出去本次其他资本构成
					String condition = " where cus_id='"+cusIdRel+"' and cus_id_rel<>'"+cusId+"'";
					IndexedCollection iColl = dao.queryList(modelId, condition, connection);
					for(int i=0;i<iColl.size();i++){
						KeyedCollection kCollTmp = (KeyedCollection)iColl.get(i);
						String curTypeO = (String)kCollTmp.getDataValue("cur_type");
						double amtO = Double.parseDouble((String)kCollTmp.getDataValue("invt_amt"));
						KeyedCollection kCollO = service.getHLByCurrType(curTypeO, context, connection);
						if("success".equals(kCollO.getDataValue("flag"))){
							double sld = ((BigDecimal)kCollO.getDataValue("sld")).doubleValue();//汇率信息
							otherAmt += amtO*sld;//除本次其他出资金额折合人民币
						}else{
//							throw new Exception((String)kCollO.getDataValue("msg"));
							flag = "汇率表中未取到币种【"+curTypeO+"】匹配汇率，请确认汇率表汇率配置";
							context.addDataField("flag", flag);
							return "0";
						}
					}
					if(otherAmt+invt_amt>zczb){
						flag = "客户【"+cusIdRel+"】的资本构成已超过实收资本！";
					}
				}
			}else{
//				double zczb = cusComRelApitalComponent.getRegAmt(cusIdRel);//获得该客户的注册资本
//				double xyzh = cusComRelApitalComponent.getSumInvrtAmt(cusIdRel);//获得该客户的现有注册资本总和
//				double old = cusComRelInvest.getComInvAmt();//修改前的投资金额
//				xyzh = BigDecimalUtil.sub(xyzh, old, 4, BigDecimal.ROUND_HALF_UP);
//				if(invt_amt > BigDecimalUtil.sub(zczb, xyzh, 4, BigDecimal.ROUND_HALF_UP)){
//					flag = "客户【"+cusIdRel+"】注册资本总和【"+df.format(zczb)+"】万元，已有出资金额【"+df.format(xyzh)+"】万元，可以修改出资金额为【"+df.format(BigDecimalUtil.sub(zczb, xyzh, 4, BigDecimal.ROUND_HALF_UP))+"】万元，本次出资金额【"+df.format(invt_amt)+"】万元";//超比例
//				}
				if(zczb<=0){
					flag = "客户【"+cusIdRel+"】注册资本总和【"+df.format(zczb)+"】万元，请先去客户基本信息中录入！";
				}
				double otherAmt = 0;//出去本次其他资本构成
				String condition = " where cus_id='"+cusIdRel+"' and cus_id_rel<>'"+cusId+"'";
				IndexedCollection iColl = dao.queryList(modelId, condition, connection);
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kCollTmp = (KeyedCollection)iColl.get(i);
					String curTypeO = (String)kCollTmp.getDataValue("cur_type");
					double amtO = Double.parseDouble((String)kCollTmp.getDataValue("invt_amt"));
					KeyedCollection kCollO = service.getHLByCurrType(curTypeO, context, connection);
					if("success".equals(kCollO.getDataValue("flag"))){
						double sld = ((BigDecimal)kCollO.getDataValue("sld")).doubleValue();//汇率信息
						otherAmt += amtO*sld;//除本次其他出资金额折合人民币
					}else{
//						throw new Exception((String)kCollO.getDataValue("msg"));
						flag = "汇率表中未取到币种【"+curTypeO+"】匹配汇率，请确认汇率表汇率配置";
						context.addDataField("flag", flag);
						return "0";
					}
				}
				if(otherAmt+invt_amt>zczb){
					flag = "客户【"+cusIdRel+"】的资本构成已超过实收资本！";
				}
			}
		}catch (EMPException ee) {
			flag = "检查失败";
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		context.addDataField("flag",flag);
		return "0";
	}
}
