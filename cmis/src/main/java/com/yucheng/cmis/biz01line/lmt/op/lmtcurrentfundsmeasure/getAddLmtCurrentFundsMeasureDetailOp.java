package com.yucheng.cmis.biz01line.lmt.op.lmtcurrentfundsmeasure;


import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusRelTree.component.CusBaseRelComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cuscom.component.CusComComponent;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class getAddLmtCurrentFundsMeasureDetailOp  extends CMISOperation {
	
	private final String modelId = "LmtCurrentFundsMeasure";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String cus_id = "";
		String serno = "";
		try{
			connection = this.getConnection(context);
			
			serno = context.getDataValue("serno").toString();
			cus_id = context.getDataValue("cus_id").toString();
			if(cus_id==null||"".equals(cus_id)||serno==null||"".equals(serno)){
				throw new Exception("[serno,cus_id]不能为空！");
			}
		
			String openDay = (String)context.getDataValue(PUBConstant.OPENDAY); 
			String openYear = openDay.substring(0, 4);
			int iopenYear = Integer.parseInt(openYear)-1;
			openYear = iopenYear+"";
			String lastYear = openYear+"12";
			
			int iiopenYear = Integer.parseInt(openYear)-1;
			String lastOpenYear = iiopenYear+"";
			String lastlastYear = iiopenYear+"12";

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno, connection);
			//先查询是否进行过测算
			if(kColl.getDataValue("serno")==null||"".equals(kColl.getDataValue("serno"))){
				CusComComponent comcomponent = (CusComComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSCOM, context, connection);
				//取上年财报，若没有则取上上年财报
				String sResult = comcomponent.queryFncStat(cus_id,lastYear);//0 没有 1有
				kColl.setDataValue("serno", serno);
				kColl.setDataValue("cus_id", cus_id);
				if("1".equals(sResult)){
					context.addDataField("is_exist", sResult);
					Map modelMap=new HashMap();
					modelMap.put("IN_客户码", cus_id);
					modelMap.put("IN_年份", openYear);
					Map outMap=new HashMap();
					CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
					ShuffleServiceInterface shuffleService = null;
					shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
					outMap=shuffleService.fireTargetRule("LMTFUNDSMEASURE", "GETITEMSVALUE", modelMap);
					double f = new BigDecimal(outMap.get("OUT_上年度销售收入").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					kColl.setDataValue("last_year_income", f);
					f = new BigDecimal(outMap.get("OUT_上年度销售利润率").toString()).divide(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					kColl.setDataValue("last_year_profit_rate", f);
					f = new BigDecimal(outMap.get("OUT_营运资金周转次数").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					kColl.setDataValue("operation_turnover_count", f);
					f = new BigDecimal(outMap.get("OUT_预收账款周转天数").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					kColl.setDataValue("pre_receive_turnover_time", f);
					f = new BigDecimal(outMap.get("OUT_存货周转天数").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					kColl.setDataValue("goods_turnover_time", f);
					f = new BigDecimal(outMap.get("OUT_应收账款周转天数").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					kColl.setDataValue("need_receive_turnover_time", f);
					f = new BigDecimal(outMap.get("OUT_应付账款周转天数").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					kColl.setDataValue("need_pay_turnover_time", f);
					f = new BigDecimal(outMap.get("OUT_预付账款周转天数").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					kColl.setDataValue("pre_pay_turnover_time", f);
					f = new BigDecimal(outMap.get("OUT_借款人自有资金").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					/*double f = Double.parseDouble(outMap.get("OUT_上年度销售收入").toString()) ;
					kColl.setDataValue("last_year_income", f);
					f = Double.parseDouble(outMap.get("OUT_上年度销售利润率").toString())/100 ;
					kColl.setDataValue("last_year_profit_rate", f);
					f = Double.parseDouble(outMap.get("OUT_营运资金周转次数").toString()) ;
					kColl.setDataValue("operation_turnover_count", f);
					f = Double.parseDouble(outMap.get("OUT_预收账款周转天数").toString()) ;
					kColl.setDataValue("pre_receive_turnover_time", f);
					f = Double.parseDouble(outMap.get("OUT_存货周转天数").toString()) ;
					kColl.setDataValue("goods_turnover_time", f);
					f = Double.parseDouble(outMap.get("OUT_应收账款周转天数").toString()) ;
					kColl.setDataValue("need_receive_turnover_time", f);
					f = Double.parseDouble(outMap.get("OUT_应付账款周转天数").toString()) ;
					kColl.setDataValue("need_pay_turnover_time", f);
					f = Double.parseDouble(outMap.get("OUT_预付账款周转天数").toString()) ;
					kColl.setDataValue("pre_pay_turnover_time", f);
					f = Double.parseDouble(outMap.get("OUT_借款人自有资金").toString()) ;*/
					if(f<0){
						f=0;
					}
					kColl.setDataValue("borrower_monetary_fund", f);
					double fown = new BigDecimal(outMap.get("OUT_所有者权益").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

					kColl.setDataValue("owner_int", fown);
					
					double finv = new BigDecimal(outMap.get("OUT_无形资产").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					kColl.setDataValue("invisible_fund", finv);
					
					double fdef = new BigDecimal(outMap.get("OUT_递延资产").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					kColl.setDataValue("defer_fund", fdef);
					/*kColl.setDataValue("borrower_monetary_fund", f);
					double fown = Double.parseDouble(outMap.get("OUT_所有者权益").toString()) ;
					kColl.setDataValue("owner_int", fown);
					double finv = Double.parseDouble(outMap.get("OUT_无形资产").toString()) ;
					kColl.setDataValue("invisible_fund", finv);
					double fdef = Double.parseDouble(outMap.get("OUT_递延资产").toString()) ;
					kColl.setDataValue("defer_fund", fdef);*/
					//有形净资产
					double fv = fown - finv - fdef;	
					kColl.setDataValue("visible_fund", fv);
					String com_cll_type = outMap.get("OUT_所属行业").toString();
					kColl.setDataValue("com_cll_type", com_cll_type);
					f = new BigDecimal(outMap.get("OUT_实有负债额").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					//f = Double.parseDouble(outMap.get("OUT_实有负债额").toString()) ;
					kColl.setDataValue("real_liab_fund", f);
				}else{
					sResult = comcomponent.queryFncStat(cus_id,lastlastYear);//0 没有 1有
					if("1".equals(sResult)){
						context.addDataField("is_exist", sResult);
						Map modelMap=new HashMap();
						modelMap.put("IN_客户码", cus_id);
						modelMap.put("IN_年份", lastOpenYear);
						Map outMap=new HashMap();
						CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
						ShuffleServiceInterface shuffleService = null;
						shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
						outMap=shuffleService.fireTargetRule("LMTFUNDSMEASURE", "GETITEMSVALUE", modelMap);
						double f = new BigDecimal(outMap.get("OUT_上年度销售收入").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						kColl.setDataValue("last_year_income", f);
						f = new BigDecimal(outMap.get("OUT_上年度销售利润率").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						kColl.setDataValue("last_year_profit_rate", f);
						f = new BigDecimal(outMap.get("OUT_营运资金周转次数").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						kColl.setDataValue("operation_turnover_count", f);
						f = new BigDecimal(outMap.get("OUT_预收账款周转天数").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						kColl.setDataValue("pre_receive_turnover_time", f);
						f = new BigDecimal(outMap.get("OUT_存货周转天数").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						kColl.setDataValue("goods_turnover_time", f);
						f = new BigDecimal(outMap.get("OUT_应收账款周转天数").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						kColl.setDataValue("need_receive_turnover_time", f);
						f = new BigDecimal(outMap.get("OUT_应付账款周转天数").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						kColl.setDataValue("need_pay_turnover_time", f);
						f = new BigDecimal(outMap.get("OUT_预付账款周转天数").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						kColl.setDataValue("pre_pay_turnover_time", f);
						f = new BigDecimal(outMap.get("OUT_借款人自有资金").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						/*double f = Double.parseDouble(outMap.get("OUT_上年度销售收入").toString()) ;
						kColl.setDataValue("last_year_income", f);
						f = Double.parseDouble(outMap.get("OUT_上年度销售利润率").toString()) ;
						kColl.setDataValue("last_year_profit_rate", f);
						f = Double.parseDouble(outMap.get("OUT_营运资金周转次数").toString()) ;
						kColl.setDataValue("operation_turnover_count", f);
						f = Double.parseDouble(outMap.get("OUT_预收账款周转天数").toString()) ;
						kColl.setDataValue("pre_receive_turnover_time", f);
						f = Double.parseDouble(outMap.get("OUT_存货周转天数").toString()) ;
						kColl.setDataValue("goods_turnover_time", f);
						f = Double.parseDouble(outMap.get("OUT_应收账款周转天数").toString()) ;
						kColl.setDataValue("need_receive_turnover_time", f);
						f = Double.parseDouble(outMap.get("OUT_应付账款周转天数").toString()) ;
						kColl.setDataValue("need_pay_turnover_time", f);
						f = Double.parseDouble(outMap.get("OUT_预付账款周转天数").toString()) ;
						kColl.setDataValue("pre_pay_turnover_time", f);
						f = Double.parseDouble(outMap.get("OUT_借款人自有资金").toString()) ;*/
						if(f<0){
							f=0;
						}
						kColl.setDataValue("borrower_monetary_fund", f);
						double fown = new BigDecimal(outMap.get("OUT_所有者权益").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

						kColl.setDataValue("owner_int", fown);
						
						double finv = new BigDecimal(outMap.get("OUT_无形资产").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						kColl.setDataValue("invisible_fund", finv);
						
						double fdef = new BigDecimal(outMap.get("OUT_递延资产").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						kColl.setDataValue("defer_fund", fdef);
						//有形净资产
						double fv = fown - finv - fdef;	
						kColl.setDataValue("visible_fund", fv);
						String com_cll_type = outMap.get("OUT_所属行业").toString();
						if(com_cll_type!=null&&com_cll_type.length()>1){
							com_cll_type = com_cll_type.substring(1);
						}
						kColl.setDataValue("com_cll_type", com_cll_type);
						f = new BigDecimal(outMap.get("OUT_实有负债额").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

						kColl.setDataValue("real_liab_fund", f);
					}else{
						context.addDataField("is_exist", "0");
					}
				}
				kColl.setName("LmtCurrentFundsMeasure");
			}else{
				context.addDataField("is_exist", "1");
			}
			Map<String,String> map = new HashMap<String,String>();
			map.put("com_cll_type", "STD_GB_4754-2011");
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			if(kColl.getDataValue("com_cll_type")!=null&&!"".equals(kColl.getDataValue("com_cll_type"))){
				//树形菜单服务
				SInfoUtils.addPopName(kColl, map, service);
			}else{
				//直接从客户信息中获取行业
				CusBaseRelComponent cusBaseRelComponent = (CusBaseRelComponent) CMISComponentFactory
						.getComponentFactoryInstance().getComponentInstance(
							PUBConstant.CusComRelComponent,context,connection);
				CusBaseComponent CusBaseComponent = (CusBaseComponent) cusBaseRelComponent.getComponent(PUBConstant.CUSBASE);
				CusCom cusCom=CusBaseComponent.getCusCom(cus_id);
				if(cusCom!=null&&cusCom.getComCllType()!=null&&!cusCom.getComCllType().equals("")){
					    kColl.put("com_cll_type", cusCom.getComCllType());
						//树形菜单服务
						SInfoUtils.addPopName(kColl, map, service);
				}
					
			}
			this.putDataElement2Context(kColl, context);
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
