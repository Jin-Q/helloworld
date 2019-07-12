package com.yucheng.cmis.biz01line.lmt.op.lmtapply;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryRLmtGuarInfoListOp extends CMISOperation {
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String guar_no=null;
			
			String limit_code = "";
			if(context.containsKey("limit_code")){
				limit_code= (String)context.getDataValue("limit_code");
			}
			String conditionStr = " WHERE limit_code='"+limit_code+"'";
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList("RLmtGuarInfo", null,conditionStr,connection);
			//把担保合同编号拼装成一个String
			String guar_cont_no_str = "";
			int size = 15; 
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String guar_cont_no = (String)kColl.getDataValue("guaranty_no");
				guar_cont_no_str += "'"+guar_cont_no+"',";
			}
			IndexedCollection lmtGuarInfo = new IndexedCollection();
			if(guar_cont_no_str.length()>1){
				guar_cont_no_str = guar_cont_no_str.substring(0, guar_cont_no_str.length()-1);
				/**调用担保模块接口*/
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				GrtServiceInterface service = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "grt");
				lmtGuarInfo = service.getGuarantyInfoList(guar_cont_no_str, "1", pageInfo,this.getDataSource(context));
				
				if(lmtGuarInfo.size()>0){
					String[] args=new String[] { "cus_id" };
					String[] modelIds=new String[]{"CusBase"};
					String[] modelForeign=new String[] { "cus_id" };
					String[] fieldName=new String[]{"cus_name"};
					SystemTransUtils.dealName(lmtGuarInfo, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
					
					Map<String,String> map = new HashMap<String,String>();
					map.put("guaranty_type","MORT_TYPE");
					//树形菜单服务
					CMISTreeDicService tree_service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			    	SInfoUtils.addPopName(lmtGuarInfo, map, tree_service);
				}
			}
			
			lmtGuarInfo.setName("RLmtGuarInfoList");
			this.putDataElement2Context(lmtGuarInfo, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
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
