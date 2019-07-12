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
 * 网银接口贷款信息查询
 * @author xc
 * 测试DBUtil.java
 */
public class Trade0200300000247 extends TranService {
	
	public KeyedCollection doExecute(CompositeData CD, Connection connection) throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		String client_no ="",clearance_flag,start_date,end_date;
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			client_no = (String)reqBody.getDataValue("CLIENT_NO");//客户码
			/**
			 * 结清标志：
				1-未结清，
				9-已结清
				0-全部
				（仅查询三年内已结清贷款）
			*/
			clearance_flag = (String)reqBody.getDataValue("CLEARANCE_FLAG");
			start_date  = TagUtil.formatDate2Ten((String)reqBody.getDataValue("START_DATE"));
			end_date	=  TagUtil.formatDate2Ten((String)reqBody.getDataValue("END_DATE"));
			/** 
			 * 参数：客户码，结清标志，起始日期，到期日期
			 * */
			
			String sql = "select a.bill_no,a.prd_id,b.acct_no as loan_acct_no,a.LOAN_AMT,a.LOAN_BALANCE," +
			"a.DISTR_DATE,a.END_DATE,'' as GUARANTEE_MODE,a.ACC_STATUS as status,a.ACC_STATUS,b.acct_no," +
			"(select c.assure_main from ctr_loan_cont c where c.cont_no=a.cont_no) as dbfs " +
			" from acc_loan a ,iqp_cus_acct b where a.cont_no= b.cont_no and b.acct_attr='03'" +
			" and a.acc_status != '0' and (a.acc_status != '9' or " +
			" (a.acc_status = '9' and to_date(a.DISTR_DATE,'yyyy-mm-dd') > (select add_months(to_date(openday, 'yyyy-mm-dd'), -36) from pub_sys_info)))";
			
			boolean flag = true;
			if(clearance_flag!=null&&!clearance_flag.equals("")){
				if(!"0".equals(clearance_flag)){
					sql = sql + " and a.ACC_STATUS = '"+clearance_flag+"' ";
					flag = false;
				}
			}
			
			if(client_no!=null&&!"".equals(client_no)){
				sql = sql + " and a.cus_id= '"+client_no+"' ";
				flag = false;
			}
			if(start_date!=null&&!"".equals(start_date)){
				sql = sql + " and a.DISTR_DATE>= '"+start_date+"' ";
				flag = false;
			}
			if(end_date!=null&&!"".equals(end_date)){
				sql = sql + " and a.DISTR_DATE<= '"+end_date+"' ";
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
			retKColl.setDataValue("ret_msg", "【贷款信息查询】交易处理完成");
			retKColl.put("client_no", client_no);
			retKColl.put("IColl", IColl);
			
			EMPLog.log("outReport", EMPLog.INFO, 0, "【贷款信息查询】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【贷款信息查询】业务处理失败！客户号号为："+client_no);
			e.printStackTrace();
			EMPLog.log("outReport", EMPLog.ERROR, 0, "【贷款信息查询】交易处理失败，客户号为："+client_no+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}
}
