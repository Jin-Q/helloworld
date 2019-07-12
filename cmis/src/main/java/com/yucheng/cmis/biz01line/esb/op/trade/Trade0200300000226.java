package com.yucheng.cmis.biz01line.esb.op.trade;

import java.sql.Connection;

import javax.sql.DataSource;

import com.dc.eai.data.CompositeData;
import com.ecc.emp.component.factory.EMPFlowComponentFactory;
import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.interfaces.ESBInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * 垫款信息查询
 * @author xc
 * 根据客户号查询客户垫款信息查询
 * 测试DBUtil.java
 */
public class Trade0200300000226 extends TranService {
	
	public KeyedCollection doExecute(CompositeData CD, Connection connection) throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		String client_no = "";
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			client_no = (String)reqBody.getDataValue("CLIENT_NO");//客户码
			
			/** 垫款信息查询 */
			String sql = "select t.pad_type,t.bill_no,t.pad_amt,t.pad_bal,t.pad_date,t.five_class from acc_pad t where 1=1 ";
			
			boolean flag = true;
			if(client_no!=null&&!"".equals(client_no)){
				sql = sql + " and t.cus_id='"+client_no+"' ";
				flag = false;
			}
			if(flag){
				sql = sql + " and 1=2 ";
			}
			
			IndexedCollection IColl =null ;
			
			EMPFlowComponentFactory factory = (EMPFlowComponentFactory) EMPFlowComponentFactory.getComponentFactory("CMISBiz");
			Context context = factory.getContextNamed(factory.getRootContextName());
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			
			IColl = TableModelUtil.buildPageData(null, dataSource, sql);
			
			/** 组装返回报文 */
			retKColl.setDataValue("ret_msg", "【垫款信息查询】交易处理完成");
			retKColl.put("client_no", client_no);
			retKColl.put("IColl", IColl);
			
			EMPLog.log("outReport", EMPLog.INFO, 0, "【垫款信息查询】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【垫款信息查询】业务处理失败！客户码为："+client_no);
			e.printStackTrace();
			EMPLog.log("outReport", EMPLog.ERROR, 0, "【垫款信息查询】交易处理失败，客户号为："+client_no+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}
}
