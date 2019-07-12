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
 * 网银接口银承用信查询
 * @author xc
 * 测试DBUtil.java
 */
public class Trade0200300000249 extends TranService {
	
	public KeyedCollection doExecute(CompositeData CD, Connection connection) throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		String client_no ="",bill_status,start_date,end_date;
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			client_no = (String)reqBody.getDataValue("CLIENT_NO");//客户码
			/**
			 * 结清标志：
				1-未结清，
				9-已结清
				0-全部
			*/
			bill_status = (String)reqBody.getDataValue("BILL_STATUS");
			start_date  = TagUtil.formatDate2Ten((String)reqBody.getDataValue("START_DATE"));
			end_date	=  TagUtil.formatDate2Ten((String)reqBody.getDataValue("END_DATE"));
			/** 
			 * 参数：客户码，结清标志，起始日期，到期日期
			 * */
			String sql = "select t.drft_amt,t.bill_no,t.isse_date,t.porder_end_date,ctr.security_rate,t.porder_no,ctr.assure_main,t.accp_status," +
					" (select iqp.acct_no from iqp_cus_acct iqp where ctr.cont_no = iqp.cont_no and iqp.acct_attr = '03') as acct_no" +
					" from acc_accp t, ctr_loan_cont ctr" +
					" where t.cont_no = ctr.cont_no and t.accp_status != '0' ";
			
			boolean flag = true;
			if(bill_status!=null&&!bill_status.equals("")){
				sql = sql + " and t.accp_status = '"+bill_status+"' ";
				flag = false;
			}
			if(client_no!=null&&!"".equals(client_no)){
				sql = sql + " and t.daorg_cusid= '"+client_no+"' ";
				flag = false;
			}
			if(start_date!=null&&!"".equals(start_date)){
				sql = sql + " and t.isse_date>= '"+start_date+"' ";
				flag = false;
			}
			if(end_date!=null&&!"".equals(end_date)){
				sql = sql + " and t.isse_date<= '"+end_date+"' ";
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
			retKColl.setDataValue("ret_msg", "【银承用信查询】交易处理完成");
			retKColl.put("client_no", client_no);
			retKColl.put("IColl", IColl);
			
			EMPLog.log("outReport", EMPLog.INFO, 0, "【银承用信查询】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【银承用信查询】业务处理失败！客户号号为："+client_no);
			e.printStackTrace();
			EMPLog.log("outReport", EMPLog.ERROR, 0, "【银承用信查询】交易处理失败，客户号为："+client_no+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}
}
