package com.yucheng.cmis.biz01line.lmt.op.rlmtguarcont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryRLmtGuarContListOp extends CMISOperation {

	private final String modelId = "RLmtAppGuarCont";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			String conditionStr = " WHERE 1=1 ";
			String limit_code = "";
			//台账上挂接担保合同
			if(context.containsKey("org_limit_code")){
				limit_code= (String)context.getDataValue("org_limit_code");
				conditionStr += " AND LIMIT_CODE='"+limit_code+"' AND SERNO= '" + context.getDataValue("serno") + "' ";
			}
			//协议管理上挂接担保合同
			if(context.containsKey("agr_no")){
				limit_code= (String)context.getDataValue("agr_no");
				conditionStr += " AND AGR_NO='"+limit_code+"' ";
			}			
			//变更申请时不过滤掉解除的担保合同
			if(!context.containsKey("app_type") || "01".equals(context.getDataValue("app_type"))){
				conditionStr += " AND CORRE_REL<>'3'";
			}
			conditionStr += " ORDER BY GUAR_LVL";
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);  
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,connection);
			//把担保合同编号拼装成一个String
			String guar_cont_no_str = "";
			int size = 99; 
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String guar_cont_no = (String)kColl.getDataValue("guar_cont_no");
				guar_cont_no_str += "'"+guar_cont_no+"',";
			}
			IndexedCollection ContiColl =null;
			if(guar_cont_no_str.length()>1){
				guar_cont_no_str = guar_cont_no_str.substring(0, guar_cont_no_str.length()-1);
				/**调用担保模块接口*/
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				GrtServiceInterface service = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "grt");
				ContiColl = service.getGuarContInfoList(guar_cont_no_str, pageInfo, this.getDataSource(context));		
			}
			
			IndexedCollection iCollYB = new IndexedCollection();
			IndexedCollection iCollZGE = new IndexedCollection();
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String guar_cont_no_Rel = (String)kColl.getDataValue("guar_cont_no");
				for(int j=0;j<ContiColl.size();j++){
					KeyedCollection GuarContkColl = (KeyedCollection)ContiColl.get(j);
					String guar_cont_no = (String)GuarContkColl.getDataValue("guar_cont_no");
					if(guar_cont_no_Rel.equals(guar_cont_no)){
						String guar_cont_type = (String)GuarContkColl.getDataValue("guar_cont_type");
						GuarContkColl.addDataField("limit_code", kColl.getDataValue("limit_code")); 
						GuarContkColl.addDataField("is_per_gur", kColl.getDataValue("is_per_gur"));
						GuarContkColl.addDataField("this_guar_amt", kColl.getDataValue("guar_amt"));
						GuarContkColl.addDataField("is_add_guar", kColl.getDataValue("is_add_guar"));
						GuarContkColl.addDataField("guar_lvl", kColl.getDataValue("guar_lvl"));
						GuarContkColl.addDataField("corre_rel", kColl.getDataValue("corre_rel"));
						GuarContkColl.addDataField("serno", kColl.getDataValue("serno"));
						//判断担保合同类型 
						if("00".equals(guar_cont_type)){
							iCollYB.addDataElement(GuarContkColl);
						}else if("01".equals(guar_cont_type)){
							iCollZGE.addDataElement(GuarContkColl);  
						}  
					}
				}
			}
			iCollYB.setName("RLmtGuarContListYb");
			iCollZGE.setName("RLmtGuarContListZge"); 
			this.putDataElement2Context(iCollYB, context);
			this.putDataElement2Context(iCollZGE, context);
			
			this.putDataElement2Context(iColl, context);
			
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		if(context.containsKey("app_type") && "02".equals(context.getDataValue("app_type"))){
			return "alteration";   //授信变更
		}else{
			return "app";   //授信新增
		}
	}

}
