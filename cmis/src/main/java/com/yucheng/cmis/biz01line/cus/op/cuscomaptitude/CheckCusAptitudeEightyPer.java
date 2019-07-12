package com.yucheng.cmis.biz01line.cus.op.cuscomaptitude;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.TranslateDic;

public class CheckCusAptitudeEightyPer implements RiskManageInterface {

	/**
	 * 校验客户资本构成是否超过注册资本的80%
	 */
	public Map<String, String> getResultMap(String tableName, String serno, Context context, Connection connection) throws Exception {
		//获取数据库处理dao接口
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);

		Map<String,String> outMap=new HashMap<String,String>();
		KeyedCollection kColl = dao.queryDetail(tableName, serno, connection);
		String cus_id = (String)kColl.getDataValue("cus_id");//客户码
		/**根据客户码查询该客户的资本构成信息**/
		BigDecimal invt_amt_all = new BigDecimal(0);
		boolean infoFlag = true;
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
		String condition = " where cus_id='"+cus_id+"'";
		IndexedCollection iCollApi = dao.queryList("CusComRelApital", condition, connection);
		for(int i=0;i<iCollApi.size();i++){
			KeyedCollection kCollApi = (KeyedCollection)iCollApi.get(i);
			String cur_type = (String)kCollApi.getDataValue("cur_type");
			BigDecimal invt_amt = new BigDecimal((String)kCollApi.getDataValue("invt_amt"));
			KeyedCollection kCollRate = service.getHLByCurrType(cur_type, context, connection);
			if("success".equals(kCollRate.getDataValue("flag"))){
				BigDecimal sld = (BigDecimal)kCollRate.getDataValue("sld");//汇率信息
				invt_amt_all = invt_amt_all.add(invt_amt.multiply(sld));//计算资本构成总额
			}else{
				infoFlag = false;
				KeyedCollection kCollDic = (KeyedCollection)context.getDataElement(CMISConstance.ATTR_DICTDATANAME);
				TranslateDic trans = new TranslateDic();
				String cnName = trans.getCnnameByOpttypeAndEnname(kCollDic, "STD_ZX_CUR_TYPE", cur_type);
				outMap.put("OUT_是否通过", "不通过");
				outMap.put("OUT_提示信息", "币种["+cnName+"]对应的汇率信息不存在，请检查");
				break;
			}
		}
		if(infoFlag==true){
			/**根据客户码查询客户实收注册资本币种、金额**/
			KeyedCollection kCollCom = dao.queryDetail("CusCom", cus_id, connection);
			String com_cur_type = (String)kCollCom.getDataValue("paid_cap_cur_type");//实收注册资本币种
			BigDecimal paid_cap_amt = new BigDecimal((String)kCollCom.getDataValue("paid_cap_amt"));//实收注册资本
			KeyedCollection kCollRateCom = service.getHLByCurrType(com_cur_type, context, connection);
			BigDecimal com_sld = (BigDecimal)kCollRateCom.getDataValue("sld");//汇率信息
			paid_cap_amt = paid_cap_amt.multiply(com_sld);
			
			if(paid_cap_amt.doubleValue()==0){
				outMap.put("OUT_是否通过", "不通过");
				outMap.put("OUT_提示信息", "客户实收注册资金为空或者为0，请检查");
			}else{
				/* modified by yangzy 2015/04/09/ 出资比例计算小数处理 start */
				double per = invt_amt_all.divide(paid_cap_amt,6,BigDecimal.ROUND_HALF_EVEN).doubleValue();
				/* modified by yangzy 2015/04/09/ 出资比例计算小数处理 end */
				if(per>0.8){
					outMap.put("OUT_是否通过", "通过");
					outMap.put("OUT_提示信息", "出资金额/实收资本金额大于80%");
				}else{
					outMap.put("OUT_是否通过", "不通过");
					outMap.put("OUT_提示信息", "出资金额/实收资本金额不大于80%，请检查");
				}
			}
		}
		
		return outMap;
	}
}
