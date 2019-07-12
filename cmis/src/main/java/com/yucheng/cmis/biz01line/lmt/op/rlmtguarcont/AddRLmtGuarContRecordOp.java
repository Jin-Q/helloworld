package com.yucheng.cmis.biz01line.lmt.op.rlmtguarcont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.util.TableModelUtil;

public class AddRLmtGuarContRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "RLmtAppGuarCont";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			String grt_type = null; 
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
				grt_type = (String)kColl.getDataValue("grt_type");  
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			
			/**担保等级默认赋值，大小为已有等级+1   --Start--*/ 
			String condition  = "where limit_code='"+kColl.getDataValue("limit_code")+"'"; 
			IndexedCollection iColl = (IndexedCollection)dao.queryList(modelId,condition, connection);
			String guar_cont_no_str ="";
			IndexedCollection ContiColl =null;
			int m = 0;
			int n = 0;
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			//把担保合同编号拼装成一个String 
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kCollGrt = (KeyedCollection)iColl.get(i);
				String guar_cont_no = (String)kCollGrt.getDataValue("guar_cont_no");
				guar_cont_no_str += "'"+guar_cont_no+"',";
			}
			if(guar_cont_no_str.length()>1){
				guar_cont_no_str = guar_cont_no_str.substring(0, guar_cont_no_str.length()-1);
				/**调用担保模块接口*/
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				GrtServiceInterface service = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "grt");
				ContiColl = service.getGuarContInfoList(guar_cont_no_str, pageInfo, this.getDataSource(context));		
			}
			
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kCollGrt = (KeyedCollection)iColl.get(i);
				String guar_cont_no_Rel = (String)kCollGrt.getDataValue("guar_cont_no");
				for(int j=0;j<ContiColl.size();j++){
					KeyedCollection GuarContkColl = (KeyedCollection)ContiColl.get(j);
					String guar_cont_no = (String)GuarContkColl.getDataValue("guar_cont_no");
					if(guar_cont_no_Rel.equals(guar_cont_no)){
						String guar_cont_type = (String)GuarContkColl.getDataValue("guar_cont_type");
						//判断担保合同类型
						if(guar_cont_type.equals("01")){
							m += 1; //如果是最高额担保，则记录
						}else if(guar_cont_type.equals("00")){
							n += 1; //如果是一般担保，则记录 
						}  
					}
				}
			}
			
			if(grt_type.equals("YB")){ 
				kColl.addDataField("guar_lvl", iColl.size()-m+1);
			}else if(grt_type.equals("ZGE")){  
				kColl.addDataField("guar_lvl", iColl.size()-n+1);  
			}
			/**---------------担保等级默认赋值--end-------------------*/
			
			
			dao.insert(kColl, connection);
			context.addDataField("flag", "success");
			
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
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
