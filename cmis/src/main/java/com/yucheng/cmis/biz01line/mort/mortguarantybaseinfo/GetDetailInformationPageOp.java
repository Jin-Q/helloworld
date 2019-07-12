package com.yucheng.cmis.biz01line.mort.mortguarantybaseinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * 获取押品类型，并进行跳转
 * @author xiejx 2010-08-03
 *
 */

public class GetDetailInformationPageOp  extends CMISOperation {

	
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		//定义押品类型
		String guaranty_type="";
		//定义押品上级目录
		String guarLastNode = "";
		try{
			connection = this.getConnection(context);
			String guaranty_no  = (String)context.getDataValue("guaranty_no");
			if(guaranty_no == null){
				return "-1";
			}
			guaranty_no = guaranty_no.trim();
		TableModelDAO dao = this.getTableModelDAO(context);
		
		
		KeyedCollection baseInfoKcoll = dao.queryDetail("MortGuarantyBaseInfo", guaranty_no, connection);
		guaranty_type = (String)baseInfoKcoll.getDataValue("guaranty_type");
		//获取当前节点的上级节点
		
		//如果押品类型不是小项，提示维护押品类型
		KeyedCollection mortTypeKcoll = dao.queryDetail("MortCmMortType", guaranty_type, connection);
		String mortSortCd = (String)mortTypeKcoll.get("mort_sort_cd");
		//获取其上级结点
		guarLastNode = (String)mortTypeKcoll.get("par_mort_type_cd");
		if("Z0901".equals(guarLastNode.substring(0,5))){//货物质押时取其上级节点进行调整
			return "Z090100";
		}
		if(mortSortCd == null||"".equals(mortSortCd)){
			return "-2";
		}
		}catch(Exception e){
				EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0,"op层GetDetailInformationPageOp处理出错:" + e.getMessage(),e);	
				throw new EMPException(e.getMessage()); 
		} finally {
			if (connection != null)
				try {
					this.releaseConnection(context, connection);
				} catch (EMPJDBCException e) {
					EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0,"op层GetDetailInformationPageOp处理出错:" + e.getMessage());	
					throw new EMPException(e.getMessage()); 
				}
		}
		return guaranty_type;
	}
}
