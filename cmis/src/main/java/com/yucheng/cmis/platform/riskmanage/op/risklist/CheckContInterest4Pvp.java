package com.yucheng.cmis.platform.riskmanage.op.risklist;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
/**
 * 无间贷还息情况检查
*@author wangj
*@time 2015-7-01
*@description TODO 需求编号：XD150611043,关于在信贷管理系统中新增相关业务检查并进行信息提示需求
*@version v1.0
*
 */
public class CheckContInterest4Pvp implements RiskManageInterface {
	private static final Logger logger = Logger.getLogger(CheckContInterest4Pvp.class);
	private final String modelId = "CtrLoanContSub";
	
	public Map<String, String> getResultMap(String tableName, String serno, Context context,Connection connection) throws Exception {
		Map<String,String> param = new HashMap<String,String>();
		try {
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			IndexedCollection iColl = dao.queryList(modelId,"where is_close_loan='1' and cont_no =(SELECT pla.cont_no FROM Pvp_Loan_App pla WHERE pla.serno='"+serno+"') ", connection);
			if(iColl!=null&&iColl.size()>0){
				KeyedCollection kColl=(KeyedCollection) iColl.get(0);
				/**调用ESB接口，发送报文*/
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
				KeyedCollection retKColl=serviceRel.trade0200200000205(kColl, context, connection);
				
				if(TagUtil.haveSuccess(retKColl,context)){
					KeyedCollection kColl_BODY = (KeyedCollection) retKColl.getDataElement("BODY");
					BigDecimal thisTermInterest = BigDecimalUtil.replaceNull(kColl_BODY.getDataValue("THIS_TERM_INTEREST"));//本期利息
					BigDecimal thisTermDupInt = BigDecimalUtil.replaceNull(kColl_BODY.getDataValue("THIS_TERM_DUP_INT"));//本期复利
					thisTermInterest=thisTermInterest.add(thisTermDupInt);//本期利息+本期复利>0
					if(thisTermInterest.compareTo(new BigDecimal(0))<1){
						param.put("OUT_是否通过","通过");
						param.put("OUT_提示信息","无间贷还息检查通过！");
					}else{
						param.put("OUT_是否通过","不通过");
						param.put("OUT_提示信息","客户利息未还清，请向柜台确认！");
					}
				}
			}else{
				param.put("OUT_是否通过","通过");
				param.put("OUT_提示信息","该出账不属于无间贷,检查通过！");
			}
			
		} catch (Exception e) {
			logger.error("无间贷还息情况检查失败！"+e.getMessage());
			throw new EMPException(e);
		}
		return param;
	}

}
