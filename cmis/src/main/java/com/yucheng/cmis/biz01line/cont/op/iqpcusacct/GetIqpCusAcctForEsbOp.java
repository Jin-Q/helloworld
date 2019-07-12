package com.yucheng.cmis.biz01line.cont.op.iqpcusacct;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.NewStringUtils;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class GetIqpCusAcctForEsbOp extends CMISOperation {

	private final String acct_no_name = "acct_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			//midify by liuhongyu on 2019-04-24 前端新增流水号和账户属性2个参数
			String acct_attr = null;
			String serno = null;
			String acct_no = null;
			String flag="success";
			String mes = "";
			String cusId = "";
			if(context.containsKey("acct_attr")){
			    acct_attr = (String) context.getDataValue("acct_attr");
			}
			DataSource dataSource = (DataSource)context.getService(CMISConstance.ATTR_DATASOURCE);
			if(context.containsKey("serno")){
			    serno = (String)context.getDataValue("serno");
			    String sql = "select cus_id from Iqp_Loan_App where serno= '"+serno+"' ";
			    
			    IndexedCollection iColl = TableModelUtil.buildPageData(null, dataSource, sql);
			    KeyedCollection record = (KeyedCollection) iColl.get(0);
			    cusId = (String) record.getDataValue("cus_id");
			} 
			try {
				acct_no = (String)context.getDataValue(acct_no_name);
			} catch (Exception e) {}
			if(acct_no == null || acct_no.length() == 0)
				throw new EMPJDBCException("The value of acct_no cannot be null!");
			
			/*** 调用esb模块实时接口取交易明细 ***/
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			KeyedCollection retKColl = null;
			KeyedCollection BODY = new KeyedCollection("BODY");
			try{
				retKColl = service.tradeZHZH(acct_no, context, connection);
				if(TagUtil.haveSuccess(retKColl, context)){//成功
					BODY = (KeyedCollection)retKColl.getDataElement("BODY");
					
					if(NewStringUtils.isNotBlank(cusId)){
					
					//根据客户号查出核心客户号
					String sql1 = "select hx_cus_id from cus_base where cus_id='"+cusId+"'";
					IndexedCollection iColl1 = TableModelUtil.buildPageData(null, dataSource, sql1);
					KeyedCollection record1 = (KeyedCollection) iColl1.get(0);
					String hxCusId = (String) record1.getDataValue("hx_cus_id");
					//报文体中获取客户号
					String cstNo = (String) BODY.getDataValue("CstNo");
					//放款账号，收款收息账号判断报文体中的客户号是否和核心客户号是否一致
					if("01".equals(acct_attr)||"03".equals(acct_attr)){
						if(StringUtils.isNotEmpty(hxCusId)){
							if(!cstNo.equals(hxCusId)){
								mes = "核心客户号和客户号不一致"; 
								flag = "error";
							}
						}else{
							mes = "核心客户号不存在"; 
							flag = "error";
						}
					}	
					
					}
					
					
					
					SInfoUtils.addSOrgName(BODY, new String[]{"AcctBlngInstNo"});
				}else{
					flag = "error";
					mes =(String)retKColl.getDataValue("RET_STATUS");
//					mes =(String)retKColl.getDataValue("RET_MSG");
				}
			}catch(Exception e){
				flag = "error";
				mes = "ESB通讯接口【获取账户信息】交易失败："+e.getMessage();
			}
			context.addDataField("flag", flag);
			context.addDataField("mes", mes);
			putDataElement2Context(BODY, context);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
