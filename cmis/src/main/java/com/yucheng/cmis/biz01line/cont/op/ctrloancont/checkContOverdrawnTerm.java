package com.yucheng.cmis.biz01line.cont.op.ctrloancont;

import java.sql.Connection;


import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.iqp.appPub.DateUtils;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.TimeUtil;
/**
 * 
*@author wangj
*@time 2015-6-08
*@description TODO 需求编号：【XD141222087】法人账户透支需求变更
*@version v1.0
*
 */
public class checkContOverdrawnTerm extends CMISOperation {
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String overdrawn_term = null;
			String cont_end_date = null;
			String cont_start_date = null;
			/**mofified by lisj 2015-6-30 需求编号：XD150123005 小微自助循环贷款改造 begin**/
			String overdrawn_type = null;
			String prd_id = null;
			String cont_term = null;
			String term_type = null;
			String contNo=null;
			String isFreeze="";
			try {
				overdrawn_term = (String)context.getDataValue("overdrawn_term");
				cont_end_date = (String)context.getDataValue("cont_end_date");
				cont_start_date = (String)context.getDataValue("cont_start_date");
				overdrawn_type = (String)context.getDataValue("overdrawn_type");
				prd_id = (String)context.getDataValue("prd_id");
				cont_term = (String)context.getDataValue("cont_term");
				term_type = (String)context.getDataValue("term_type");
				
				contNo = (String)context.getDataValue("cont_no");
				TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
				KeyedCollection kColl = dao.queryDetail("CtrLoanContSub",contNo, connection);
			    isFreeze=TagUtil.replaceNull4String(kColl.getDataValue("is_freeze"));
				
			} catch (Exception e) {}
			if(overdrawn_term == null || "".equals(overdrawn_term))
				throw new EMPJDBCException("The values overdrawn_term cannot be empty!");
			if(cont_end_date == null || "".equals(cont_end_date))
				throw new EMPJDBCException("The values cont_end_date cannot be empty!");
			if(cont_start_date == null || "".equals(cont_start_date))
				throw new EMPJDBCException("The values cont_start_date cannot be empty!");
			if(overdrawn_type == null || "".equals(overdrawn_type))
				throw new EMPJDBCException("The values overdrawn_type cannot be empty!");
			if(prd_id == null || "".equals(prd_id))
				throw new EMPJDBCException("The values prd_id cannot be empty!");
			if(cont_term == null || "".equals(cont_term))
				throw new EMPJDBCException("The values cont_term cannot be empty!");
			if(term_type == null || "".equals(term_type))
				throw new EMPJDBCException("The values term_type cannot be empty!");
			if("1".equals(isFreeze)){
				context.addDataField("flag", "isFreeze");
			}else{
			if("100051".equals(prd_id)){
				int term=Integer.valueOf(overdrawn_term);
				if(TimeUtil.getBetweenDays(cont_start_date, cont_end_date)<term){
	            	context.addDataField("flag", "error");
	            }else{
	            	context.addDataField("flag", "success");
	            }
			}else if("100088".equals(prd_id)){
				int term=Integer.valueOf(overdrawn_term);
				int cont_term_int = Integer.valueOf(cont_term);
				if("001".equals(overdrawn_type)){//期限类型：年
					if("001".equals(term_type)){
						if(term > 1){
							context.addDataField("flag", "errorTerm");
						}else if(term > cont_term_int){
							context.addDataField("flag", "overTerm");
						}else{
							context.addDataField("flag", "success");
						}
					}else if("002".equals(term_type)){
						if(term > 1){
							context.addDataField("flag", "errorTerm");
						}else if((term *12) > cont_term_int){
							context.addDataField("flag", "overTerm");
						}else{
							context.addDataField("flag", "success");
						}
					}else{//合同期限类型为日
						if(term > 1){
							context.addDataField("flag", "errorTerm");
						}else if(TimeUtil.compareDate(DateUtils.getAddDate("Y", cont_start_date,term),cont_end_date)>0){
							context.addDataField("flag", "overTerm");
						}else{
							context.addDataField("flag", "success");
						}
						
					}
				}else if("002".equals(overdrawn_type)){//期限类型：月
					if("001".equals(term_type)){
						if(term > 12){
							context.addDataField("flag", "errorTerm");
						}else if(term > (cont_term_int * 12)){
							context.addDataField("flag", "overTerm");
						}else{
							context.addDataField("flag", "success");
						}
					}else if("002".equals(term_type)){
						if(term > 12){
							context.addDataField("flag", "errorTerm");
						}else if(term > cont_term_int){
							context.addDataField("flag", "overTerm");
						}else{
							context.addDataField("flag", "success");
						}
					}else{//合同期限类型为日
						if(term > 12){
							context.addDataField("flag", "errorTerm");
						}else if(TimeUtil.compareDate(DateUtils.getAddDate("M", cont_start_date,term),cont_end_date)>0){
							context.addDataField("flag", "overTerm");
						}else{
							context.addDataField("flag", "success");
						}
					}
				}else{
					if(term >365||term <30){
						context.addDataField("flag", "errorTerm");
					}else if(TimeUtil.compareDate(DateUtils.getAddDate("D", cont_start_date,term),cont_end_date)>0){
						context.addDataField("flag", "overTerm");
		            }else{
		            	context.addDataField("flag", "success");
		            }
				}
			 }
			}
			/**mofified by lisj 2015-6-30 需求编号：XD150123005 小微自助循环贷款改造 end**/
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
