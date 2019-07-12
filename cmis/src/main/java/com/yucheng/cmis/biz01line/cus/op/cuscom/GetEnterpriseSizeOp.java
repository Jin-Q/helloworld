package com.yucheng.cmis.biz01line.cus.op.cuscom;

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
import com.yucheng.cmis.pub.PUBConstant;
/**
 * 判断企业规模 2013-10-10 by 唐顺岩
 * @author QZCB
 *
 */
public class GetEnterpriseSizeOp extends CMISOperation {
	private static final Logger logger = Logger.getLogger(GetEnterpriseSizeOp.class);
	
	public String doExecute(Context context) throws EMPException {
		
		/*  10  大型
			20  中型
			30  小型
			31  微型
			90  其他
			*/
		Connection connection = null;
		try {
			String cus_id = (String) context.getDataValue("cus_id");
			if(cus_id == null) cus_id = "";
			String com_cll_type = (String)context.getDataValue("com_cll_type");
			if(com_cll_type == null) com_cll_type = "";
			String com_empStr  = (String)context.getDataValue("com_employee");
			int com_employee = 0;
			if(com_empStr == null || "".equals(com_empStr))
				com_employee = 0;
			else
				com_employee = Integer.parseInt(com_empStr);
			connection = this.getConnection(context);
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
			inputValueMap.put("IN_行业编号", com_cll_type);
			inputValueMap.put("IN_从业人数", com_employee+"");
			String openDay = (String)context.getDataValue(PUBConstant.OPENDAY);
			inputValueMap.put("IN_OPENDAY", openDay.replaceAll("-", "") );
			Map<String,String> resultMap = new HashMap<String,String>();
//			CusComComponent cusComComp = (CusComComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("CusCom", context, connection);
			resultMap = shuffleService.fireTargetRule("CUS_CON_SIZE_IDENTY","CUS_CON_SIZE_IDENTY",inputValueMap);
			String out_msg = "";
			String out_flag = "";
			if(resultMap.containsKey("OUT_FLAG") && "N".equalsIgnoreCase(resultMap.get("OUT_FLAG"))){   //规则测试成功标志
				out_flag = (String)resultMap.get("OUT_FLAG");   //获取规则返回信息
				out_msg = (String)resultMap.get("OUT_MSG");   //获取规则返回码
			}
			String size = (String)resultMap.get("OUT_企业规模");
			
			logger.info("调用规程测算企业规模返回码："+out_flag+"返回信息："+out_msg);
			context.setDataValue("com_scale",size);
			context.addDataField("out_flag",out_flag);
			context.addDataField("out_msg",out_msg);
		} catch (Exception e) {
			e.printStackTrace();
			context.setDataValue("com_scale","31");
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
