package com.yucheng.cmis.biz01line.cus.op.cussameorg;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;

   /**
    * 同业客户生成Excel
    * */
public class ExpBatchToExcelOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			// 输出的SQL语句
//			String sql = "select a.CUS_ID,a.SAME_ORG_CNNAME,a.SAME_ORG_TYPE,a.CUST_LEVEL,"
//					+ "a.ASSETS,a.PAID_CAP_AMT, a.CRD_GRADE from Cus_Same_org a ";
			String conditionStr = " where 1=1 ";
			KeyedCollection queryKcoll = (KeyedCollection)context.getDataElement("CusSameOrg");
			String same_org_cnname =(String) queryKcoll.getDataValue("same_org_cnname");
			String same_org_no =(String) queryKcoll.getDataValue("same_org_no");
			String swift_no = "";
			if(queryKcoll.containsKey("swift_no")){
				swift_no =(String) queryKcoll.getDataValue("swift_no");
			}
			String crd_grade =(String) queryKcoll.getDataValue("crd_grade");
			if(!same_org_cnname.equals("")){
				conditionStr = conditionStr + " and same_org_cnname like '%"+same_org_cnname+"%'";
			}
			if(!same_org_no.equals("")){
				conditionStr = conditionStr + " and same_org_no = '"+same_org_no+"'";
			}
			if(!swift_no.equals("")){
				conditionStr = conditionStr + " and cus_name = '"+swift_no+"'";
			}
			if(!crd_grade.equals("")){
				conditionStr = conditionStr + " and crd_grade = '"+crd_grade+"'";
			}
			
			conditionStr = conditionStr + " order by cus_id ";
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict("CusSameOrg", conditionStr, context, connection);
//			sql = sql + conditionStr;
			EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.INFO, 0, "conditionStr=" + conditionStr, null);
//			EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.INFO, 0, "sql=" + sql, null);
			// 有什么字段，必须于查询语句中的字段匹配
//			Map<String, String> tableFields = new HashMap<String, String>();
//			tableFields.put("cus_id", "客户码");
//			tableFields.put("same_org_cnname", "同业机构(行)名称");
//			tableFields.put("same_org_type", "同业机构类型");
//			tableFields.put("cust_level", "监管评级");
//			tableFields.put("assets", "总资产(万元)");
//			tableFields.put("paid_cap_amt", "实收资本(万元)");
//			tableFields.put("crd_grade", "信用等级");
//			
//			// 哪些字段是为数字类型的(右对齐)
//			Map<String, String> moneyFields = new HashMap<String, String>();
//			moneyFields.put("assets", "Currency");
//			moneyFields.put("paid_cap_amt", "Currency");
//			
//			// 哪些列需要翻译的
//			Map<String, String> dicFields = new HashMap<String, String>();
//			dicFields.put("CUST_LEVEL", "STD_ZB_CUSTD_RATE");//监管等级
//			dicFields.put("CRD_GRADE", "STD_ZB_FINA_GRADE");//信用等级
//		
//			// 写excel文件
//			HttpServletRequest request = null;
//			File tmpFile = null;
//			FileInputStream fis = null;
//			ExportUtil f = new ExportUtil(connection);
//			tmpFile = f.exportData(sql, tableFields, moneyFields, dicFields,context);
//			
//			if (tmpFile != null) {
//				fis = new FileInputStream(tmpFile);
//				tmpFile.deleteOnExit();
//				request = (HttpServletRequest) context
//						.getDataValue(EMPConstance.SERVLET_REQUEST);
//				request.setAttribute("inputStream", fis);
//				request.setAttribute("filename", ExportUtil.getExportName());
//			} else {
//				return "0";
//			}
			TableModelDAO dao = this.getTableModelDAO(context);
			List<String> tableFields = new ArrayList<String>();
			tableFields.add("cus_id");
			tableFields.add("same_org_cnname");
			tableFields.add("same_org_type");
			tableFields.add("cust_level");
			tableFields.add("assets");
			tableFields.add("paid_cap_amt");
			tableFields.add("reg_cap_amt");
			tableFields.add("crd_grade");
			tableFields.add("country");
			
			IndexedCollection iColl = dao.queryList("CusSameOrg", tableFields, conditionStr, connection);
			iColl.setName("CusSameOrgList");
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
