package com.yucheng.cmis.biz01line.cus.op.cuscomrelapital;

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
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusComRelApital;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;
/**
 * 
 * <pre> 
 * Title:客户信息出资比例校验
 * Description: 客户信息出资比例校验
 * </pre>
 * @author 
 * 创建日期：
 * @version 1.00.00
 * <pre>
 *    修改后版本:        修改人：         修改日期:              修改内容: 
 *    1.00.01           yangzy     2014/09/29             出资资本校验改造
 * </pre>
 */
public class checkCusComRelApitalPercOp extends CMISOperation {

	private final String modelId = "CusComRelApital";
	
	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		String flag = "可以新增";
		try {
			connection = this.getConnection(context);

			String cusId = (String) context.getDataValue("cus_id");//客户ID
			String invtAmt = (String) context.getDataValue("invt_amt");//录入的出资金额
			String cusIdRel = (String)context.getDataValue("cus_id_rel");//关联客户ID
			String opType = (String)context.getDataValue("op");//操作类型
			String cur_type = (String)context.getDataValue("cur_type");//币种
			double invt_amt = Double.parseDouble(invtAmt);
			
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
			CusComRelApitalComponent cusComRelApitalComponent = (CusComRelApitalComponent) CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSCOMRELAPITAL, context, connection);
			Map map = cusComRelApitalComponent.getRegAmt(cusId);//获得该客户的注册资本及币种
			String zcbz = (String) map.get("paid_cap_cur_type");
			double zczb = (Double) map.get("paid_cap_amt");
			if(zcbz==null || "".equals(zcbz)){//投资人注册资本信息没完善
				flag = "请先完善投资人注册资本信息，并保存！";
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
			CusComRelApital ccra = cusComRelApitalComponent.getCusComRelApital(cusId,cusIdRel);
			System.out.println(ccra.getCusId());
			if(ccra.getCusId() != null){
				if(opType.equals("add")){
					flag = "该客户下已存在相同的资本构成！";
				}else{
//					double xyzh = cusComRelApitalComponent.getSumInvrtAmt(cusId);//获得该客户的现有注册资本总和
//					double xzje = BigDecimalUtil.sub(zczb, xyzh, 4, BigDecimal.ROUND_HALF_UP);//新增出资金额
//					double old = ccra.getInvtAmt();
//					xyzh = BigDecimalUtil.sub(xyzh, old, 4, BigDecimal.ROUND_HALF_UP);
//					if(invt_amt > xzje){
//						flag = "注册资本总和【"+df.format(zczb)+"】万元，已有出资金额【"+df.format(xyzh)+"】万元，可以修改出资金额为【"+df.format(BigDecimalUtil.sub(zczb, xyzh, 4, BigDecimal.ROUND_HALF_UP))+"】万元，本次出资金额【"+df.format(invt_amt)+"】万元";//超比例
//					}
					if(zczb<=0){
						flag = "注册资本总和【"+df.format(zczb)+"】万元，请先去客户基本信息中录入！";
					}
					double otherAmt = 0;//出去本次其他资本构成
					String condition = " where cus_id='"+cusId+"' and cus_id_rel<>'"+cusIdRel+"'";
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
					/* modified by yangzy 2014/09/29 出资资本校验改造 begin */
					double Amt = otherAmt+invt_amt;
					String Amt1 = df.format(Amt);
					String Amt2 = df.format(zczb);
					if(Double.parseDouble(Amt1)>Double.parseDouble(Amt2)){
						flag = "资本构成已超过实收资本！";
					}
					/* modified by yangzy 2014/09/29 出资资本校验改造 end */
				}
			}else{
//				double zczb = cusComRelApitalComponent.getRegAmt(cusId);//获得该客户的注册资本
//				double xyzh = cusComRelApitalComponent.getSumInvrtAmt(cusId);//获得该客户的现有注册资本总和
//				double xzje = BigDecimalUtil.sub(zczb, xyzh, 4, BigDecimal.ROUND_HALF_UP);//新增出资金额
//				if(invt_amt > xzje){
//					flag = "注册资本总和【"+df.format(zczb)+"】万元，已有出资金额【"+df.format(xyzh)+"】万元，可以新增出资金额【"+df.format(BigDecimalUtil.sub(zczb, xyzh, 4, BigDecimal.ROUND_HALF_UP))+"】万元，本次出资金额【"+df.format(invt_amt)+"】万元";//超比例
//				}
				if(zczb<=0){
					flag = "注册资本总和【"+df.format(zczb)+"】万元，请先去客户基本信息中录入！";
				}
				double otherAmt = 0;//出去本次其他资本构成
				String condition = " where cus_id='"+cusId+"' and cus_id_rel<>'"+cusIdRel+"'";
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
				/* modified by yangzy 2014/10/15 出资资本校验改造 begin */
				double Amt = otherAmt+invt_amt;
				String Amt1 = df.format(Amt);
				String Amt2 = df.format(zczb);
				if(Double.parseDouble(Amt1)>Double.parseDouble(Amt2)){
					flag = "资本构成已超过实收资本！";
				}
				/* modified by yangzy 2014/10/15 出资资本校验改造 end */
			}			
		} catch (EMPException ee) {
			flag = "操作失败！";
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		context.addDataField("flag", flag);
		return "0";
	}
}
