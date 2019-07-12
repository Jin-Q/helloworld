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
 * 贴现票据查询
 * @author xc
 * 测试DBUtil.java
 */
public class Trade0300300000231 extends TranService {
	
	public KeyedCollection doExecute(CompositeData CD, Connection connection) throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		String client_no ="",discount_status,start_date,end_date;
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			client_no = (String)reqBody.getDataValue("CLIENT_NO");//客户码
			discount_status = (String)reqBody.getDataValue("DISCOUNT_STATUS");
			start_date  = TagUtil.formatDate2Ten((String)reqBody.getDataValue("START_DATE"));
			end_date	=  TagUtil.formatDate2Ten((String)reqBody.getDataValue("END_DATE"));
			/** 
			 * 参数：客户码，贴现状态，起始日期，到期日期
			 * */
			String sql = "select a.dscnt_type,c.drft_amt,a.pad_amt,a.dscnt_int,a.dscnt_date,c.porder_end_date,b.security_rate,a.porder_no,b.assure_main," +
					" a.accp_status,(select t.acct_no from iqp_cus_acct t where t.cont_no = b.cont_no and t.acct_attr = '03') as paorg_acct_no" +
					" from acc_drft a, ctr_loan_cont b, iqp_bill_detail c " +
					" where a.cont_no = b.cont_no and a.PORDER_NO = c.porder_no and a.accp_status != '0' ";
	
			boolean flag = true;
			if(discount_status!=null&&!discount_status.equals("")){
				sql = sql + " and a.accp_status = '"+discount_status+"' ";
				flag = false;
			}
			if(client_no!=null&&!"".equals(client_no)){
				sql = sql + " and b.cus_id='"+client_no+"' ";
				flag = false;
			}
			if(start_date!=null&&!"".equals(start_date)){
				sql = sql + " and a.dscnt_date>= '"+start_date+"' ";
				flag = false;
			}
			if(end_date!=null&&!"".equals(end_date)){
				sql = sql + " and a.dscnt_date<= '"+end_date+"' ";
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
			retKColl.setDataValue("ret_msg", "【贴现票据查询】交易处理完成");
			retKColl.put("client_no", client_no);
			retKColl.put("IColl", IColl);
			
			EMPLog.log("outReport", EMPLog.INFO, 0, "【贴现票据查询】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【贴现票据查询】业务处理失败！客户号号为："+client_no);
			e.printStackTrace();
			EMPLog.log("outReport", EMPLog.ERROR, 0, "【贴现票据查询】交易处理失败，客户号为："+client_no+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}
}
