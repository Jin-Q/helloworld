package com.yucheng.cmis.biz01line.report;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * 
 *@author lisj
 *@time 2015-3-5
 *@description TODO 需求编号:【XD150107002】新增信贷业务纸质档案封面的导出与打印功能
 *@version v1.0
 * <pre>
 * 修改记录
 *    修改后版本：     修改人：     修改日期：     修改内容： 
 *    1.00.01      lisj      20150723   需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更
 * </pre>
 */
public class CoverPrintOp  extends CMISOperation {
	
	public String doExecute(Context context) throws EMPException {
		try{
			String print_type = null;
			String agr_no = null;
			String cus_id = null;
			String guaranty_no = null;
			String task_id = null;
			String cont_no = null;
			String prd_id = null;
			String po_no = null; // 保理池/应收账款池/票据池 编号
			String jural_flag="";//法律要件资料标志
			Connection connection = this.getConnection(context);
			try {
				print_type = (String)context.getDataValue("print_type");
				if(context.containsKey("agr_no") && !"".equals(context.getDataValue("agr_no"))){
					agr_no = context.getDataValue("agr_no").toString();
				}
				if(context.containsKey("cus_id") && !"".equals(context.getDataValue("cus_id"))){
					cus_id = context.getDataValue("cus_id").toString();
				}
				if(context.containsKey("guaranty_no") && !"".equals(context.getDataValue("guaranty_no"))){
					guaranty_no = context.getDataValue("guaranty_no").toString();
				}
				if(context.containsKey("task_id") && !"".equals(context.getDataValue("task_id"))){
					task_id = context.getDataValue("task_id").toString();
				}
				if(context.containsKey("cont_no") && !"".equals(context.getDataValue("cont_no"))){
					cont_no = context.getDataValue("cont_no").toString();
				}
				if(context.containsKey("prd_id") && !"".equals(context.getDataValue("prd_id"))){
					prd_id = context.getDataValue("prd_id").toString();
				}
				if(context.containsKey("jural_flag") && !"".equals(context.getDataValue("jural_flag"))){
					jural_flag = context.getDataValue("jural_flag").toString();
				}
				if(context.containsKey("po_no") && !"".equals(context.getDataValue("po_no"))){
					po_no = context.getDataValue("po_no").toString();
				}
			} catch (Exception e) {}
			if(print_type == null || print_type.length() == 0)
				throw new EMPJDBCException("The value of pk["+print_type+"] cannot be null!");
			
			context.put("print_type",print_type);
			context.put("agr_no", agr_no);
			context.put("cus_id", cus_id);
			context.put("task_id", task_id);
			context.put("cont_no", cont_no);
			context.put("guaranty_no", guaranty_no);
			context.put("prd_id", prd_id);
			context.put("jural_flag", jural_flag);
			context.put("po_no", po_no);
			
			if(print_type!=null && !"".equals(print_type) && (print_type.indexOf("04")>=0)){
			  IndexedCollection lmtAgrInfo = new IndexedCollection();
			  DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			  String conditionSelect = "select a.limit_code, a.crd_amt, a.start_date, a.end_date," +
			                           "(select cus_name from cus_base cb where cb.cus_id = a.cus_id) cus_name," +
			  		                   "(select prdname from Prd_Basicinfo where prdid = a.limit_name) as prd_name," +
			  		                   "(select cnname from s_dic where opttype = 'STD_ZB_ASSURE_MEANS' and enname = a.guar_type) as guar_type " +
			  		                   "from lmt_agr_details a where a.lmt_status <> '30' and a.agr_no ='"+agr_no+"'";
			  if(!"".equals(jural_flag) && "lmtjoint".equals(jural_flag)){//联保
				  conditionSelect += " and a.cus_id = '"+cus_id+"' ";
			  }
			  
			  lmtAgrInfo = TableModelUtil.buildPageData(null, dataSource, conditionSelect);
			  lmtAgrInfo.setName("LmtAgrInfoList");
			  this.putDataElement2Context(lmtAgrInfo, context);
			}
			
			if(print_type!=null && !"".equals(print_type) && (print_type.indexOf("01")>=0)){
				IndexedCollection lmtAppJuralInfo = new IndexedCollection();
				KeyedCollection parm = new KeyedCollection();
				parm.put("agrNo", agr_no);
				parm.put("cusId", cus_id);
				//单一法人及个人协议保证信息
				if(!"".equals(jural_flag) && "lmtapp".equals(jural_flag)){
					lmtAppJuralInfo = SqlClient.queryList4IColl("queryLmtAppJuralInfo", parm, connection);
					lmtAppJuralInfo.setName("lmtJuralInfoList");
					this.putDataElement2Context(lmtAppJuralInfo, context);
				}else if(!"".equals(jural_flag) && "lmtgrp".equals(jural_flag)){//集团
					lmtAppJuralInfo = SqlClient.queryList4IColl("queryLmtAppJuralInfo", parm, connection);
					lmtAppJuralInfo.setName("lmtJuralInfoList");
					this.putDataElement2Context(lmtAppJuralInfo, context);
				}else if(!"".equals(jural_flag) && "lmtjoint".equals(jural_flag)){//联保
					lmtAppJuralInfo = SqlClient.queryList4IColl("queryLmtJointJuralInfo", parm, connection);
					lmtAppJuralInfo.setName("lmtJuralInfoList");
					this.putDataElement2Context(lmtAppJuralInfo, context);
				}
			}
			/**add by lisj 2015-7-14  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 begin**/
			if(print_type!=null && !"".equals(print_type) && (print_type.indexOf("13")>=0)){
				IndexedCollection lmtAppJuralInfo4clc = new IndexedCollection();
				lmtAppJuralInfo4clc = SqlClient.queryList4IColl("queryLmtAppJuralInfo4clc", cont_no, connection);
				lmtAppJuralInfo4clc.setName("lmtJuralInfo4ClcList");
				this.putDataElement2Context(lmtAppJuralInfo4clc, context);
		    }
			
			if(print_type!=null && !"".equals(print_type) && (print_type.indexOf("14")>=0)){
				IndexedCollection iqpPoolManaInfo = new IndexedCollection();
				KeyedCollection parm = new KeyedCollection();
				parm.put("po_no", po_no);
				iqpPoolManaInfo = SqlClient.queryList4IColl("queryIqpPoolManaInfoList", parm, connection);
				iqpPoolManaInfo.setName("iqpPoolManaInfoList");
				this.putDataElement2Context(iqpPoolManaInfo, context);
		    }
			/**add by lisj 2015-7-14  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 end**/
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
		}
		return "0";
	}
}
