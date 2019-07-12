package com.yucheng.cmis.biz01line.grt.op.grtloanrgur;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryGrtCtrLimitListOp extends CMISOperation {
	private final String modelId = "GrtLoanRGur";
	private final String modelIdLimitRel = "CtrLimitLmtRel";
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
            String serno=null;
            String lmt_code_no_str ="";
            String guar_cont_no_str ="";
            IndexedCollection LmtiColl = new IndexedCollection();
			try {
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {
                throw new Exception("业务流水号为空!");				
			}
			
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			String condition = "where limit_serno='"+serno+"'";
			IndexedCollection iColl = dao.queryList(modelIdLimitRel, condition, connection);
			for (int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String lmt_code_no = (String)kColl.getDataValue("lmt_code_no");
				lmt_code_no_str += "'"+lmt_code_no+"',";
			}
			if(lmt_code_no_str.length()>1){
				lmt_code_no_str = lmt_code_no_str.substring(0, lmt_code_no_str.length()-1);
				/**调用担保模块接口*/ 
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				LmtServiceInterface service = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
				LmtiColl = service.searchGuarContByLimitCode(lmt_code_no_str, pageInfo,dataSource );		
			}
			
			for(int i=0;i<LmtiColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)LmtiColl.get(i);
				String guar_cont_no = (String)kColl.getDataValue("guar_cont_no");
				if(i < LmtiColl.size()-1){
					guar_cont_no_str += "'"+guar_cont_no+"',";
				}else{
					guar_cont_no_str += "'"+guar_cont_no+"'";
				}
			}
			
			
			IndexedCollection ContiColl = new IndexedCollection();
			if(guar_cont_no_str.length()>1){
				/**调用担保模块接口*/ 
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				GrtServiceInterface service = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "grt");
				ContiColl = service.getGuarContInfoList(guar_cont_no_str, pageInfo, this.getDataSource(context));		
			}
			
			for(int i=0;i<LmtiColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)LmtiColl.get(i);
				String guar_cont_no_Rel = (String)kColl.getDataValue("guar_cont_no");
				for(int j=0;j<ContiColl.size();j++){
					KeyedCollection GuarContkColl = (KeyedCollection)ContiColl.get(j);
					String guar_cont_no = (String)GuarContkColl.getDataValue("guar_cont_no");
					if(guar_cont_no_Rel.equals(guar_cont_no)){
						GuarContkColl.addDataField("limit_code", kColl.getDataValue("limit_code")); 
						GuarContkColl.addDataField("is_per_gur", kColl.getDataValue("is_per_gur"));
						GuarContkColl.addDataField("this_guar_amt", kColl.getDataValue("guar_amt"));
						GuarContkColl.addDataField("is_add_guar", kColl.getDataValue("is_add_guar"));
						GuarContkColl.addDataField("corre_rel", kColl.getDataValue("corre_rel"));
					}
				}
			}
			ContiColl.setName("LmtGrtLoanRGurList"); 
			this.putDataElement2Context(ContiColl, context);

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
