package com.yucheng.cmis.biz01line.mort.mortguarantybaseinfo;

import java.net.URLDecoder;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckWarrantNoOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "MortGuarantyBaseInfo";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			try {
				String warrant_no =(String) context.getDataValue("warrant_no");
				
				//权证编号中文传输会乱码，所以使用编码传输，使用前先解码
				warrant_no = URLDecoder.decode(warrant_no,"UTF-8");
				
				String warrant_type =(String) context.getDataValue("warrant_type");
				Map<String,String> map = new HashedMap();
				map.put("warrant_no", warrant_no);
				map.put("warrant_type",warrant_type);
				if(context.containsKey("flag")){
					String warrantNoOld = (String) context.getDataValue("warrantNoOld");
					//权证编号中文传输会乱码，所以使用编码传输，使用前先解码
					warrantNoOld = URLDecoder.decode(warrantNoOld,"UTF-8");
					//modified by yangzy 2015/05/28 押品维护权证信息校验改造 start
					String condition = "";
					String guaranty_no = "";
					if(context.containsKey("guaranty_no")&&context.getDataValue("guaranty_no")!=null&&!"".equals(context.getDataValue("guaranty_no"))){
						guaranty_no = (String) context.getDataValue("guaranty_no");
						condition = " where (warrant_no = '"+warrant_no+"' and warrant_type = '"+warrant_type+"' and is_main_warrant = '2') or (warrant_no = '"+warrant_no+"' and warrant_type = '"+warrant_type+"' and guaranty_no <> '"+guaranty_no+"') ";
					}else{
						condition = " where warrant_no = '"+warrant_no+"' and warrant_type = '"+warrant_type+"' and is_main_warrant = '2' ";
					}
					//modified by yangzy 2015/05/28 押品维护权证信息校验改造 end
					IndexedCollection kc = dao.queryList("MortGuarantyCertiInfo", condition, this.getConnection(context));
					if(kc!=null&&kc.size()>0){
						context.addDataField("check", "false");
					}else{
						context.addDataField("check", "true");
					}	
				}else{
					
					KeyedCollection kc = dao.queryAllDetail("MortGuarantyCertiInfo", map, this.getConnection(context));
					if(null==(String)kc.getDataValue("warrant_no")){
						context.addDataField("check", "true");
					}else if(warrant_type.equals((String)kc.getDataValue("warrant_type"))){
						context.addDataField("check", "false");
					}else{
						context.addDataField("check", "true");
					}	
				}
					
			} catch (Exception e) {}
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
