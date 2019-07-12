package com.yucheng.cmis.biz01line.lmt.riskmanage;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * @description 需求:XD140925064,生活贷需求开发，检查生活贷业务申请信息
 * @author lisj
 * @time 2014-12-1 19:57:07
 *
 */
public class CheckLifeLoansInfo implements RiskManageInterface {

	public Map<String, String> getResultMap(String tableName, String serno, Context context, Connection connection) throws Exception {
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		Map<String,String> outMap=new HashMap<String,String>();
		String flag = "pass";
		BigDecimal ir_float_rate = new BigDecimal("0.00");//利率浮动比
		String prd_id ="";//产品编号
		String conditionStr ="select t1.prd_id,t2.ir_float_rate " +
							 "from iqp_loan_app t1,iqp_loan_app_sub t2 " +
							 "where t1.serno = t2.serno and t1.serno='"+serno+"'";
		IndexedCollection  iColl = TableModelUtil.buildPageData(null, dataSource, conditionStr);
		//校验业务利率浮动比
		for(int i=0;i<iColl.size();i++){
			KeyedCollection kColl = (KeyedCollection)iColl.get(i);
			prd_id = (String) kColl.getDataValue("prd_id");
			ir_float_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("ir_float_rate"));		
		}
		if(prd_id.equals("100080")){	
			if(ir_float_rate.compareTo(new BigDecimal("0.70")) < 0){
				flag ="该业务利率浮动比小于基准利率浮动比70%";
			}
		}else if(prd_id.equals("100081")){
			if(ir_float_rate.compareTo(new BigDecimal("0.80")) < 0){
				flag ="该业务利率浮动比小于基准利率浮动比80%";
			}
		}else if(prd_id.equals("100082")){
			if(ir_float_rate.compareTo(new BigDecimal("0.90")) < 0){
				flag ="该业务利率浮动比小于基准利率浮动比90%";
			}
		}else if(prd_id.equals("100083")){
			if(ir_float_rate.compareTo(new BigDecimal("1.00")) < 0){
				flag ="该业务利率浮动比小于基准利率浮动比100%";
			}
		}else{
			flag ="notBeLongTo";
		}
		if(flag.equals("pass")){
			outMap.put("OUT_是否通过", "通过");
			outMap.put("OUT_提示信息", "业务基准利率浮动比检查通过");
		}else if(flag.equals("notBeLongTo")){
			outMap.put("OUT_是否通过", "通过");
			outMap.put("OUT_提示信息", "该业务不属于生活贷业务");
		}else{
			outMap.put("OUT_是否通过", "不通过");
			outMap.put("OUT_提示信息", flag);
		}
		return outMap;
	}

}
