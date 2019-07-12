package com.yucheng.cmis.biz01line.grt.op.grtloanrgur;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.arp.msi.ArpServiceInterface;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryGrtLoanRGurListOp extends CMISOperation {
	private final String modelId = "GrtLoanRGur";
	private final String modelIdRBus = "RBusLmtInfo";
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
            String serno=null;
            String cont_no=null;
            String conditionStr = null;
            String limit_ind = null;
            String agr_no = null;
            String res = null;
            //add by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 START
            String modify_rel_serno = "";
            //add by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 END
			try {
				if(context.containsKey("serno")){
					serno = (String)context.getDataValue("serno");
				}
				if(context.containsKey("cont_no")){
					cont_no = (String)context.getDataValue("cont_no");
				}
				if(context.containsKey("limit_ind")){
					limit_ind = (String)context.getDataValue("limit_ind");//授信额度使用标识
				}
				//add by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 START
				if(context.containsKey("modify_rel_serno")){
					modify_rel_serno = (String)context.getDataValue("modify_rel_serno");
				}
				//add by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 END
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			LmtServiceInterface lmtservice = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
			
			
			/*** 授信台账下有关联“追加担保合同”需提醒,查询授信台账编号 ***/
			if("2".equals(limit_ind) || "3".equals(limit_ind) || "5".equals(limit_ind)|| "6".equals(limit_ind)){
				String iqpCondition = " where serno = '"+serno+"'";
				IndexedCollection iqpIColl = dao.queryList(modelIdRBus, iqpCondition, connection);
				if(iqpIColl.size()>0){
					KeyedCollection iqpkColl = (KeyedCollection)iqpIColl.get(0);
					agr_no = (String)iqpkColl.getDataValue("agr_no");
					IndexedCollection rLmtiColl = lmtservice.getRLmtGuarContByLimitCode(agr_no, context, connection);
					if(rLmtiColl.size()>0){
						res = "have";
					}
				}
			}
			
			/*** 资产保全处理担保明细begin by GC 20131127 ***/
			if(context.containsKey("menuIdTab")){
				Object menuId = context.getDataValue("menuIdTab");
				if(menuId.equals("ArpLawLawsuitApp")||menuId.equals("ArpLawLawsuitHis")){
					cont_no = getArpCont(serno, context, connection,"app");
				}else if(menuId.equals("ArpLawLawsuitInfo")){
					String case_no = context.getDataValue("case_no").toString();
					cont_no = getArpCont(case_no, context, connection,"info");
				}
			}
			/*** 资产保全处理担保明细end by GC 20131127 ***/
			
			if(!"".equals(cont_no) && cont_no != null){ 
				conditionStr ="where cont_no in ('"+cont_no+"') order by guar_lvl asc";
			}else{
				conditionStr ="where serno='"+serno+"' order by guar_lvl asc";
			}			
			
			int size = 200;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			List<String> list = new ArrayList<String>(); 
			list.add("pk_id");
			list.add("serno");
			list.add("guar_cont_no");
			list.add("cont_no");
			list.add("guar_amt");
			list.add("is_per_gur");
			list.add("guar_lvl");
			list.add("corre_rel");
			list.add("is_add_guar");
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
				GrtServiceInterface service = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "grt");
				ContiColl = service.getGuarContInfoList(guar_cont_no_str, pageInfo, this.getDataSource(context));		
			}
			
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String guar_cont_no_Rel = (String)kColl.getDataValue("guar_cont_no");
				for(int j=0;j<ContiColl.size();j++){
					KeyedCollection GuarContkColl = (KeyedCollection)ContiColl.get(j);
					KeyedCollection GuarContkCollClone = (KeyedCollection)GuarContkColl.clone();
					String guar_cont_no = (String)GuarContkCollClone.getDataValue("guar_cont_no");
					if(guar_cont_no_Rel.equals(guar_cont_no)){
						String guar_cont_type = (String)GuarContkCollClone.getDataValue("guar_cont_type");
						GuarContkCollClone.put("pk_id", kColl.getDataValue("pk_id"));
						GuarContkCollClone.put("serno", kColl.getDataValue("serno")); 
						GuarContkCollClone.put("is_per_gur", kColl.getDataValue("is_per_gur"));
						//add by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 START
						if(!"".equals(modify_rel_serno)){
							String conditionStr1 = "where biz_serno='"+modify_rel_serno+"' and guar_cont_no='"+guar_cont_no+"'";
							IndexedCollection selectIColl = dao.queryList("GrtLoanRGurTmp", conditionStr1, connection); 
							if(selectIColl!=null&&selectIColl.size()>0){
								KeyedCollection kCollRelTmp = (KeyedCollection) selectIColl.get(0);
								GuarContkCollClone.put("this_guar_amt", kCollRelTmp.getDataValue("guar_amt"));
							}else{
								GuarContkCollClone.put("this_guar_amt", kColl.getDataValue("guar_amt"));
							}
						}else{
							GuarContkCollClone.put("this_guar_amt", kColl.getDataValue("guar_amt"));
						}
						//add by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 END
						GuarContkCollClone.put("is_add_guar", kColl.getDataValue("is_add_guar"));
						GuarContkCollClone.put("corre_rel", kColl.getDataValue("corre_rel"));
						GuarContkCollClone.put("guar_lvl", kColl.getDataValue("guar_lvl"));
						//判断担保合同类型
						if("00".equals(guar_cont_type)){
							iCollYB.addDataElement(GuarContkCollClone);
						}else if("01".equals(guar_cont_type)){ 
							iCollZGE.addDataElement(GuarContkCollClone);  
						}  
					}
				}
			}
			context.addDataField("res", res);
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
	
	/**** 资产保全处理担保明细 ****/
	public String getArpCont( String serno, Context context ,Connection connection ,String flag) throws Exception {
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		ArpServiceInterface service = (ArpServiceInterface)serviceJndi.getModualServiceById("ArpServices", "arp");
		
		/*** 取诉讼明细中的借据 ***/
		IndexedCollection iColl = new IndexedCollection();
		KeyedCollection trans_kColl = new KeyedCollection();
		trans_kColl.addDataField("type", "common");
		if(flag.equals("app")){
			trans_kColl.addDataField("tableName", "ArpLawLawsuitDetail");
			trans_kColl.addDataField("condition", "where serno = '"+serno+"'");
		}else if(flag.equals("info")){
			trans_kColl.addDataField("tableName", "ArpLawLawsuitDtmana");
			trans_kColl.addDataField("condition", "where case_no = '"+serno+"'");
		}
		
		iColl = service.getArpIcollByCondition(trans_kColl, context, connection);
		
		/*** 取其合同编号 ***/
		String[] args = new String[] { "bill_no" };
		String[] modelIds = new String[] { "AccLoan" };
		String[] modelForeign = new String[] { "bill_no" };
		String[] fieldName = new String[] { "cont_no" };
		String[] resultName = new String[] { "cont_no" };
		SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
		
		String cont_nos = "";
		for(int i =0;i<iColl.size();i++){
			trans_kColl = (KeyedCollection) iColl.getElementAt(i);
			cont_nos = cont_nos + trans_kColl.getDataValue("cont_no")+"','";
		}
		return cont_nos;				
	}

}
