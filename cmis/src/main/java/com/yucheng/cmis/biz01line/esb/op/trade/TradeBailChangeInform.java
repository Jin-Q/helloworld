package com.yucheng.cmis.biz01line.esb.op.trade;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import com.ecc.emp.core.Context;
import com.dc.eai.data.CompositeData;
import com.ecc.emp.component.factory.EMPFlowComponentFactory;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.esb.interfaces.ESBInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 保证金变动通知
 * @author Pansq
 * 通过授权编号查询授权表获取到保证金追加/提取申请的流水号，再查询申请表获取变更后的保证金比例，变更后的保证金金额
 * 将变更后的保证金比例和金额更新到合同表中
 * 根据授权编号更新授权表的状态为授权已确认
 * 
 */
public class TradeBailChangeInform extends TranService {

	@Override
	public KeyedCollection doExecute(CompositeData CD, Connection connection)
			throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		KeyedCollection calcResult = null;
		String GEN_GL_NO = "";
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			GEN_GL_NO = (String)reqBody.getDataValue("GEN_GL_NO");//授权编号
			String CONTRACT_NO = (String)reqBody.getDataValue("CONTRACT_NO");//合同号
			String GUARANTEE_ACCT_NO = (String)reqBody.getDataValue("GUARANTEE_ACCT_NO");//保证金账号
			String TRAN_TYPE = (String)reqBody.getDataValue("TRAN_TYPE");//交易类型
			BigDecimal newAmtRate = new BigDecimal(0);//变更后的保证金比例
			BigDecimal newAmt = new BigDecimal(0);//变更后的保证金金额
			/** added by yangzy 2015/07/20 需求：XD150407026， 更新存量外币业务的汇率 start **/
			BigDecimal newSecurityExchangeRate = new BigDecimal(0);//变更后的保证金汇率
			/** added by yangzy 2015/07/20 需求：XD150407026， 更新存量外币业务的汇率 end **/
			//通过授权编号查询出变更后的保证金比例
			calcResult = (KeyedCollection)SqlClient.queryFirst("queryBailRateByAuthNo", GEN_GL_NO, null, connection);
			String serno_bg = (String) calcResult.getDataValue("serno");//追加申请的流水号
			String prd_id = (String) calcResult.getDataValue("prd_id");//产品编号
			//更新保证金比例到合同表
			/** modified by yangzy 2015/07/20 需求：XD150407026， 更新存量外币业务的汇率 start **/
			if(calcResult!=null){
				newAmtRate = (BigDecimal)calcResult.getDataValue("adjusted_bail_perc");
				newAmt = (BigDecimal)calcResult.getDataValue("adjusted_bail_amt");
				newSecurityExchangeRate = (BigDecimal)calcResult.getDataValue("security_exchange_rate");
				Map param = new HashMap();
				param.put("newAmtRate", newAmtRate);
				param.put("newAmt", newAmt);
				param.put("newSecurityExchangeRate", newSecurityExchangeRate);
				SqlClient.update("updateContSecurityRate", CONTRACT_NO, param, null, connection);
			}
			/** modified by yangzy 2015/07/20 需求：XD150407026， 更新存量外币业务的汇率 end **/
			//根据授权编号更新授权表的状态为授权已确认
			SqlClient.update("updatePvpAuthorizeStatusByGenNo", GEN_GL_NO, "04", null, connection);
			
			//如果是新增加的保证金则需要往保证金信息表(pub_bail_info)中增加一条数据，把追加申请的保证金信息拷贝一份
			if(TRAN_TYPE!=null&&TRAN_TYPE.equals("02")){
				SqlClient.insert("insertPubBailInfoToIqp", serno_bg, null, null, connection);
			}
			
			//成功后通知国结系统
			/**调用ESB接口，发送报文*/
			try{
				if(prd_id.equals("500032")||prd_id.equals("700020")||prd_id.equals("700021")||prd_id.equals("400020")){
					//判断是否为贸易融资业务
					EMPFlowComponentFactory factory = (EMPFlowComponentFactory) EMPFlowComponentFactory.getComponentFactory("CMISBiz");
					Context context = factory.getContextNamed(factory.getRootContextName());
					CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
					ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
					serviceRel.tradeBZJZJTZGJ(GEN_GL_NO, context, connection);
				}
			}catch (Exception e) {
				e.printStackTrace();
				EMPLog.log("TradeBailChangeInform", EMPLog.INFO, 0, "信贷保证金追加实时通知国结失败："+e.getMessage(), null);
				//通知失败，不进行处理
			}
			
			EMPLog.log("TradeBailChangeInform", EMPLog.INFO, 0, "【保证金变动通知】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【保证金变动通知】 ,业务处理失败！授权编号为："+GEN_GL_NO);
			e.printStackTrace();
			EMPLog.log("TradeBailChangeInform", EMPLog.ERROR, 0, "【保证金变动通知】交易处理失败，授权编号为："+GEN_GL_NO+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}

}
