package com.yucheng.cmis.biz01line.esb.op.trade;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.ecc.emp.component.factory.EMPFlowComponentFactory;
import com.ecc.emp.core.Context;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.dao.SqlClient;

public class TradeCorrectInform extends ESBTranService {
	
	private static final Logger logger = Logger.getLogger(TradeCorrectInform.class);
	
	@Override
	public KeyedCollection doExecute(KeyedCollection kColl, Connection conn) throws Exception {
		logger.info("----------------- 进入交易冲正接口  start --------------------");
		KeyedCollection retKColl = new KeyedCollection();
		KeyedCollection reqBody = (KeyedCollection)kColl.get("BODY");
		EMPFlowComponentFactory factory = (EMPFlowComponentFactory) EMPFlowComponentFactory.getComponentFactory("CMISBiz");
		Context context = factory.getContextNamed(factory.getRootContextName());
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		
		try{
			logger.info("----------------- 获取接口入参  start --------------------");
			String dblNo = (String)reqBody.getDataValue("DblNo");//借据号
			String txnTp = (String)reqBody.getDataValue("TxnTp");//交易类型
			String acctTxnDt = (String)reqBody.getDataValue("AcctTxnDt");//账户交易日期
			String cnclFlg = (String)reqBody.getDataValue("CnclFlg");//撤销标志
			logger.info("----------------- 获取接口入参  end --------------------");
			
			String openday  = (String)SqlClient.queryFirst("querySysInfo", null, null, conn);

			logger.info("------------------------  生成贷款合同  start  -----------------------");
			
			logger.info("------------------------  生成贷款合同  end  -----------------------");
			
			logger.info("------------------------  生成贷款台账  start  -----------------------");
			
			logger.info("------------------------  生成贷款台账  end  -----------------------");

			logger.info("------------------------  更新授权协议  start  -----------------------");
			
			logger.info("------------------------  更新授权协议  end  -----------------------");
			
			logger.info("------------------------  更新授权台账  start  -----------------------");
			
			logger.info("------------------------  更新授权台账  end  -----------------------");
			
			retKColl.setDataValue("RetCd", "000000");
			retKColl.setDataValue("RetInf", "【交易冲正接口】业务处理成功");
		}catch(Exception e){
			retKColl.setDataValue("RetCd", "999999");
			retKColl.setDataValue("RetInf", "【交易冲正接口】交易处理失败，错误信息："+e.getMessage());
			e.printStackTrace();
		}
		logger.info("----------------- 进入交易冲正接口  end --------------------");
		return retKColl;
	}
}
