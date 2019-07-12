package com.yucheng.cmis.biz01line.ccr.op;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.exception.ComponentException;
/**
 * 异步查询客户信息是否在明细表中存在OP类
 * @author Administrator
 *
 */
public class CcrModelMatchOp extends CMISOperation {
	private static final Logger logger = Logger.getLogger(CMISComponent.class);
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String model_no="";
		String cus_id="";
		try{
			connection = this.getConnection(context);
			model_no = (String) context.getDataValue("model_no");
			cus_id = (String) context.getDataValue("cus_id");
			 // 调用规则管理模块对外提供的服务：根据用户号得到该用户拥有的所有角色
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory
					.getInstance();
			ShuffleServiceInterface shuffleService = null;
			try {
				shuffleService = (ShuffleServiceInterface) serviceJndi
						.getModualServiceById("shuffleServices",
								"shuffle");
			} catch (Exception e) {
				EMPLog.log("shuffle", EMPLog.ERROR, 0,
						"getModualServiceById error!", e);
				throw new EMPException(e);
			}
			/**
			 * 调用模型适用规则CCR_CusCCRMJudge取得模型ID以及会计年份、会计月份
			 */
			Map modelMap=new HashMap();
			modelMap.put("cus_id", cus_id);
			modelMap.put("model_no", model_no);
			Map outMap=new HashMap();
			try {
				outMap=shuffleService.fireTargetRule("CCR", "CusCCRMJudge", modelMap);
			} catch (Exception e1) {
				logger.error("获取模型信息失败\n"+e1);
				throw new ComponentException(CMISMessage.QUERYERROR,"获取模型失败");
			}
			String flag = (String)outMap.get("OUT_交易码");
			String msg = (String)outMap.get("OUT_交易说明");
			EMPLog.log("MESSAGE", EMPLog.INFO, 0, msg);
			String fncYear=(String) outMap.get("OUT_会计年份");
			String fncMonth=(String) outMap.get("OUT_会计月份");
			String statPrdStyle=(String) outMap.get("OUT_周期类型");
			String COM_STR_DATE=(String) outMap.get("T_COM_STR_DATE");
			EMPLog.log("MESSAGE", EMPLog.INFO, 0, "会计年份："+fncYear+"\n");
			EMPLog.log("MESSAGE", EMPLog.INFO, 0, "会计月份："+fncMonth+"\n");
			EMPLog.log("MESSAGE", EMPLog.INFO, 0, "报表周期类型："+statPrdStyle+"\n");
			EMPLog.log("MESSAGE", EMPLog.INFO, 0, "公司成立日期："+COM_STR_DATE+"\n");
			context.addDataField("flag", flag);
			context.addDataField("msg", msg);
			return null;
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}

	}

}
