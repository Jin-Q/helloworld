package com.yucheng.cmis.biz01line.grt.op.grtloanrgur;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.util.TableModelUtil;

public class CreditChangeQueryGrtLoanRGurListOp extends CMISOperation {
	private final String modelId = "GrtLoanRGur";
	private final String modelIdGuarCont = "GrtGuarCont";
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
            String serno="";
			try {
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {}
			if(serno == null || "".equals(serno)){
				throw new Exception("变更申请流水号为空!");
			}
			
			/*********把关联关系为正常的更新为续作--start*************/
			if(context.containsKey("op")){
				String op = (String)context.getDataValue("op");
				if("update".equals(op)){
					IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
					.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
					int updateCount = cmisComponent.updateGrtLoanRGurCorreRel(serno, connection);	
					if(updateCount<0){ 
						throw new Exception("更新关联关系失败!");
					}
				}
			}
			/*****************----end------***********************/ 

			String conditionStr ="where serno='"+serno+"' order by guar_lvl asc";
			int size = 100;     
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			List<String> list = new ArrayList<String>(); 
			list.add("pk_id");
			list.add("serno");
			list.add("guar_cont_no");
			list.add("cont_no");
			list.add("guar_amt");
			list.add("is_per_gur");
			list.add("guar_lvl");
			list.add("is_add_guar");
			list.add("corre_rel");
			//页面上显示一般担保合同记录和最高担保额记录
			IndexedCollection iColl = dao.queryList(modelId,list,conditionStr,pageInfo,connection);
			IndexedCollection iCollYB = new IndexedCollection();
			IndexedCollection iCollZGE = new IndexedCollection();
			String guar_cont_no_str ="";
			IndexedCollection ContiColl =null;
			//把担保合同编号拼装成一个String
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String guar_cont_no = (String)kColl.getDataValue("guar_cont_no");
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
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String guar_cont_no_Rel = (String)kColl.getDataValue("guar_cont_no");
				for(int j=0;j<ContiColl.size();j++){
					KeyedCollection GuarContkColl = (KeyedCollection)ContiColl.get(j);
					String guar_cont_no = (String)GuarContkColl.getDataValue("guar_cont_no");
					if(guar_cont_no_Rel.equals(guar_cont_no)){
						String guar_cont_type = (String)GuarContkColl.getDataValue("guar_cont_type");
						/**修复担保信息页签pk_id值报NULL值BUG，与2015-6-25 上线 begin**/
						GuarContkColl.put("pk_id", kColl.getDataValue("pk_id"));
						GuarContkColl.put("serno", kColl.getDataValue("serno"));  
						GuarContkColl.put("corre_rel", kColl.getDataValue("corre_rel")); 
						GuarContkColl.put("is_per_gur", kColl.getDataValue("is_per_gur"));
						GuarContkColl.put("this_guar_amt", kColl.getDataValue("guar_amt"));
						GuarContkColl.put("is_add_guar", kColl.getDataValue("is_add_guar"));
						GuarContkColl.put("guar_lvl", kColl.getDataValue("guar_lvl"));
						/**修复担保信息页签pk_id值报NULL值BUG，与2015-6-25 上线 end**/
						//判断担保合同类型
						if("00".equals(guar_cont_type)){
							iCollYB.addDataElement(GuarContkColl);
						}else if("01".equals(guar_cont_type)){ 
							iCollZGE.addDataElement(GuarContkColl);  
						}  
					}
				}
			}
			iCollYB.setName("GrtLoanRGurListYb");
			iCollZGE.setName("GrtLoanRGurListZge"); 
			this.putDataElement2Context(iCollYB, context);
			this.putDataElement2Context(iCollZGE, context);

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
