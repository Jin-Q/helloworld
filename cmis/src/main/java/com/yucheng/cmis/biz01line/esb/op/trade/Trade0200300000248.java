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
 * 网银接口委托贷款查询
 * @author xc
 * 测试DBUtil.java
 */
public class Trade0200300000248 extends TranService {
	
	public KeyedCollection doExecute(CompositeData CD, Connection connection) throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		String client_no ="",buss_type,start_date,end_date;
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			client_no = (String)reqBody.getDataValue("CLIENT_NO");//客户码
			buss_type = (String)reqBody.getDataValue("BUSS_TYPE");
			start_date  = TagUtil.formatDate2Ten((String)reqBody.getDataValue("START_DATE"));
			end_date	=  TagUtil.formatDate2Ten((String)reqBody.getDataValue("END_DATE"));
			/** 
			 * 参数：客户码，业务类型(01-借款方，02-委托方)，起始日期，到期日期
			 * */
			String sql = "select  t.reality_ir_y,t.loan_amt,t.distr_date,t.end_date,a.assure_main,t.five_class," +
					" (select c1.cus_name from cus_base c1 where c1.cus_id = a.cus_id) dr_name," +
					" (select c2.cus_name from cus_base c2 where c2.cus_id = b.csgn_cus_id) consign_name," +
					" (select c.acct_no from iqp_cus_acct c where c.cont_no = a.cont_no and c.acct_attr = '03') as acct_no" +
					" from acc_loan t, ctr_loan_cont a, iqp_csgn_loan_info b" +
					" where t.cont_no = a.cont_no and a.serno = b.serno and t.prd_id in ('100063', '100065') and t.acc_status != '0' ";

			boolean flag = true;
			if(buss_type!=null&&!"".equals(buss_type)){
				if(buss_type.equals("01")){
					if(client_no!=null&&!"".equals(client_no)){
						sql = sql + " and t.cus_id='"+client_no+"' ";
						flag = false;
					}
				}else{
					if(client_no!=null&&!"".equals(client_no)){
						sql = sql + " and b.csgn_cus_id='"+client_no+"' ";
						flag = false;
					}
				}
			}else{
				sql = sql + " and (t.cus_id='"+client_no+"' or b.csgn_cus_id='"+client_no+"') ";
			}
			
			if(start_date!=null&&!"".equals(start_date)){
				sql = sql + " and t.distr_date>= '"+start_date+"' ";
				flag = false;
			}
			if(end_date!=null&&!"".equals(end_date)){
				sql = sql + " and t.distr_date<= '"+end_date+"' ";
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
			
			KeyedCollection param = new KeyedCollection();
			param.put("CLIENT_NO", client_no);
			param.put("START_DATE",start_date);
			param.put("END_DATE", end_date);
			
			/** 组装返回报文 */
			retKColl.setDataValue("ret_msg", "【委托贷款查询】交易处理完成");
			retKColl.put("client_no", client_no);
			retKColl.put("IColl", IColl);
			
			EMPLog.log("outReport", EMPLog.INFO, 0, "【委托贷款查询】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【委托贷款查询】业务处理失败！客户号号为："+client_no);
			e.printStackTrace();
			EMPLog.log("outReport", EMPLog.ERROR, 0, "【委托贷款查询】交易处理失败，客户号为："+client_no+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}
}
