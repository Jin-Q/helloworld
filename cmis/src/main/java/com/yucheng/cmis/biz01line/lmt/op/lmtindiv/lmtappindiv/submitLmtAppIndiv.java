package com.yucheng.cmis.biz01line.lmt.op.lmtindiv.lmtappindiv;

import java.sql.Connection;
import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.utils.LmtUtils;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class submitLmtAppIndiv  extends CMISOperation {
	
	private final String modelId = "LmtAppIndiv";
	
	private final String serno_name = "serno";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			
			String openDate = context.getDataValue("OPENDAY").toString();
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl_agr = dao.queryDetail(modelId, serno_value, connection);
			
			kColl_agr.setDataValue("over_date", context.getDataValue("OPENDAY"));
			kColl_agr.setDataValue("approve_status", "997");
			//修改申请表的办结日期、申请状态
			dao.update(kColl_agr, connection);
			
			String lmt_agr_no = "";
			if(!"".equals(kColl_agr.getDataValue("app_type")) && "01".equals(kColl_agr.getDataValue("app_type"))){  //新增
				//生成授信协议编号
				lmt_agr_no = CMISSequenceService4JXXD.querySequenceFromDB("LMT_AGR", "all", connection, context);
				kColl_agr.setDataValue("agr_no", lmt_agr_no);
				kColl_agr.setDataValue("totl_start_date", openDate);
				kColl_agr.setDataValue("self_start_date", openDate);
				String totlEndDate = LmtUtils.computeEndDate(openDate, kColl_agr.getDataValue("term_type").toString(), kColl_agr.getDataValue("term").toString());
				kColl_agr.setDataValue("totl_end_date", totlEndDate);
				String selfEndDate = LmtUtils.computeEndDate(openDate, "002", kColl_agr.getDataValue("self_term").toString());
				kColl_agr.setDataValue("self_end_date", selfEndDate);
			}else if(!"".equals(kColl_agr.getDataValue("app_type")) && "02".equals(kColl_agr.getDataValue("app_type"))){   //变更
				lmt_agr_no = kColl_agr.getDataValue("agr_no").toString();
				//授信总额期限
				String totlStartDate = kColl_agr.getDataValue("totl_start_date").toString();
				String term = kColl_agr.getDataValue("term").toString();
				String termType = kColl_agr.getDataValue("term_type").toString();
				String totlEndDate = LmtUtils.computeEndDate(totlStartDate, term, termType);
				kColl_agr.setDataValue("totl_end_date", totlEndDate);
				//自助额度期限
				String selfStartDate = kColl_agr.getDataValue("self_start_date").toString();
				String selfTerm = kColl_agr.getDataValue("self_term").toString();
				String selfEndDate = LmtUtils.computeEndDate(selfStartDate, "002", selfTerm);
				kColl_agr.setDataValue("self_end_date", selfEndDate);
			}
			
			kColl_agr.removeDataElement("org_crd_totl_amt");   //清除变更前授信总额
			kColl_agr.removeDataElement("org_self_amt");    //清除变更前循环授信总额
			kColl_agr.removeDataElement("org_crd_one_amt");    //清除变更前一次性授信总额
			kColl_agr.removeDataElement("approve_status");  //清除审批状态
			kColl_agr.removeDataElement("app_date");	//申请日期
			kColl_agr.removeDataElement("over_date");  //清除到期日期
//			kColl_agr.removeDataElement("app_type");  //申请类型
			
			kColl_agr.removeDataElement("term");  //期限
			kColl_agr.removeDataElement("term_type");  //期限类型
			kColl_agr.removeDataElement("self_term");  //自助期限
			
			kColl_agr.setName("LmtAgrIndiv");
			
			if(!"".equals(kColl_agr.getDataValue("app_type")) && "01".equals(kColl_agr.getDataValue("app_type"))){  //新增
				kColl_agr.removeDataElement("app_type");  //清除申请类型
				dao.insert(kColl_agr, connection); 
			}else if(!"".equals(kColl_agr.getDataValue("app_type")) && "02".equals(kColl_agr.getDataValue("app_type"))){   //变更
				kColl_agr.removeDataElement("app_type");  //清除申请类型
				dao.update(kColl_agr, connection);
			}
			
			String cus_id = kColl_agr.getDataValue("cus_id").toString();
			String condition = " WHERE SERNO='"+serno_value+"'";
			IndexedCollection iColl = dao.queryList("LmtAppDetails", condition, connection);
			String endDate = "";
			
			for (Iterator<KeyedCollection> iterator = iColl.iterator(); iterator.hasNext();) {
				KeyedCollection kColl_details = (KeyedCollection) iterator.next();
				kColl_details.addDataField("lmt_status", "10");  //额度状态默认为[正常]
				kColl_details.addDataField("enable_amt", kColl_details.getDataValue("crd_amt"));  //启用金额默认等于授信金额
				kColl_details.addDataField("cus_id", cus_id);
				kColl_details.addDataField("agr_no", lmt_agr_no);  //授信协议编号
				
				String update_flag = kColl_details.getDataValue("update_flag").toString();
				if("01".equals(update_flag)){  //新增时
					endDate = LmtUtils.computeEndDate(openDate, kColl_details.getDataValue("term_type").toString(), kColl_details.getDataValue("term").toString());
					kColl_details.setDataValue("start_date", openDate);
					kColl_details.setDataValue("end_date", endDate);
				}else{   //变更
					if("1".equals(kColl_details.getDataValue("is_adj_term"))){  //并且调整期限
						String star_date = kColl_details.getDataValue("start_date").toString();
						endDate = LmtUtils.computeEndDate(star_date, kColl_details.getDataValue("term_type").toString(), kColl_details.getDataValue("term").toString());
						kColl_details.setDataValue("end_date", endDate);
					}
				}
				
				kColl_details.removeDataElement("update_flag");   //清除修改类型
				
				kColl_details.setName("LmtAgrDetails");
				
				if("01".equals(update_flag)){  //新增时
					kColl_details.removeDataElement("org_limit_code");  //清除台账授信额度编号
					dao.insert(kColl_details, connection);
				}else{    //变更
					kColl_details.setDataValue("limit_code", kColl_details.getDataValue("org_limit_code"));  //将原授信额度编号赋给授信额度编号，用于做主键更新
					kColl_details.removeDataElement("org_limit_code");  //清除台账授信额度编号
					dao.update(kColl_details, connection);
				}
			}
			
			this.putDataElement2Context(iColl, context);
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
