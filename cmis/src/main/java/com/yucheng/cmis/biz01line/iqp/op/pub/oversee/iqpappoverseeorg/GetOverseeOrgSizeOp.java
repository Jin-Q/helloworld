package com.yucheng.cmis.biz01line.iqp.op.pub.oversee.iqpappoverseeorg;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
/**
 * 判断企业规模 2013-12-25 by 肖迪
 * @author QZCB
 *
 */
public class GetOverseeOrgSizeOp extends CMISOperation {
	private static final Logger logger = Logger.getLogger(GetOverseeOrgSizeOp.class);
	
	public String doExecute(Context context) throws EMPException {
		
		/*  10  大型
			20  中型
			30  小型
			*/
		Connection connection = null;
		try {
			String cus_id = (String) context.getDataValue("cus_id");
			if(cus_id == null) cus_id = "";
			context.addDataField("com_scale", "");
			//调用规则
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ShuffleServiceInterface shuffleService = null;
			try {
				shuffleService = (ShuffleServiceInterface) serviceJndi .getModualServiceById("shuffleServices", "shuffle");
			} catch (Exception e) {
				EMPLog.log("shuffle", EMPLog.ERROR, 0, "getModualServiceById error!", e);
				throw new EMPException(e);
			}
			Map<String,String> inputValueMap = new HashMap<String,String>();
			inputValueMap.put("IN_客户码", cus_id);
			Map<String,String> resultMap = new HashMap<String,String>();
//			CusComComponent cusComComp = (CusComComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("CusCom", context, connection);
			resultMap = shuffleService.fireTargetRule("CUS_CON_SIZE_IDENTY","JGJGQYGMRD",inputValueMap);
			String out_msg = "";
			String out_flag = "";
			if(resultMap.containsKey("OUT_FLAG") && "N".equalsIgnoreCase(resultMap.get("OUT_FLAG"))){   //规则测试成功标志
				out_flag = (String)resultMap.get("OUT_FLAG");   //获取规则返回信息
				out_msg = (String)resultMap.get("OUT_MSG");   //获取规则返回码
			}
			String size = (String)resultMap.get("OUT_监管规模");
			
			logger.info("调用规程测算企业规模返回码："+out_flag+"返回信息："+out_msg);
			context.setDataValue("com_scale",size);
			context.addDataField("out_flag",out_flag);
			context.addDataField("out_msg",out_msg);
		} catch (Exception e) {
			e.printStackTrace();
			context.setDataValue("com_scale","30");
			context.addDataField("out_flag","N");
			context.addDataField("out_msg","调用规则测算企业规模出错，错误原因："+e.getCause().getMessage());
			//throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";

	}
}
