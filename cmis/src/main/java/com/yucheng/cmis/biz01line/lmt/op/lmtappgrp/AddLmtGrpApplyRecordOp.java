package com.yucheng.cmis.biz01line.lmt.op.lmtappgrp;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddLmtGrpApplyRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "LmtAppGrp";
	private final String modelIdGrpMem = "CusGrpMember";
	private final String modelIdAgr = "LmtAgrInfo";
	private final String modelIdApp = "LmtApply";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String grp_no = null;
		String grp_serno = null;
		String currentUserId = "";
		String sOrgno = "";
		String openDay = "";
		
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			//集团编号
			grp_no = kColl.getDataValue("grp_no").toString();
//			grp_serno = kColl.getDataValue("serno").toString();
			String manager_br_id = kColl.getDataValue("manager_br_id").toString();
			grp_serno = CMISSequenceService4JXXD.querySequenceFromSQ("SQ", "all", manager_br_id, connection, context);
			kColl.put("serno", grp_serno);
			
			currentUserId = context.getDataValue("currentUserId").toString();
			sOrgno = context.getDataValue("organNo").toString();
			openDay = context.getDataValue("OPENDAY").toString();
			
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			kColl.put("input_id", currentUserId);
			dao.insert(kColl, connection);
			
			//变更保存时将原有授信协议复制到申请历史表
			if(kColl.containsKey("app_type") && "02".equals(kColl.getDataValue("app_type"))){
				LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
				lmtComponent.createLmtApplyRecord((String)kColl.getDataValue("grp_agr_no"), grp_serno);
			}
			
			List<String> list = new ArrayList<String>();
			list.add("cus_id");
			//根据集团编号获取成员信息
			String condition = " where grp_no='"+grp_no+"'";
			IndexedCollection iColl4Mem = dao.queryList(modelIdGrpMem, list, condition, connection);
			for(int i=0;i<iColl4Mem.size();i++){
				KeyedCollection kCollMem = (KeyedCollection) iColl4Mem.get(i);
				String cus_id = kCollMem.getDataValue("cus_id").toString();
				CusBaseComponent cbc = (CusBaseComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSBASE, context,connection);
				CusBase cusBase = cbc.getCusBase(cus_id);
				String mainBrId = cusBase.getMainBrId();
				String conditionStr1 = " WHERE CUS_ID='"+cus_id+"' AND AGR_STATUS ='002' AND to_char(add_months(to_date(END_DATE,'yyyy-mm-dd'),6),'yyyy-mm-dd') >= (SELECT OPENDAY FROM PUB_SYS_INFO) ";  //到期协议不过渡
				IndexedCollection iCollAgr = dao.queryList(modelIdAgr, null, conditionStr1, connection);
				String serno = CMISSequenceService4JXXD.querySequenceFromSQ("SQ", "all", mainBrId, connection, context);
				//生成成员授信申请
				KeyedCollection kCollApp = new KeyedCollection();
				kCollApp.setName(modelIdApp);
				kCollApp.put("serno", serno);
				kCollApp.put("grp_serno", grp_serno);
				kCollApp.put("cus_id", cus_id);
				kCollApp.put("cur_type", "CNY");
				kCollApp.put("crd_totl_amt", 0);
				kCollApp.put("crd_cir_amt", 0);
				kCollApp.put("crd_one_amt", 0);
				kCollApp.put("app_date", openDay);
				kCollApp.put("flow_type", "01");
				kCollApp.put("manager_id", cusBase.getCustMgr());
				kCollApp.put("manager_br_id", cusBase.getMainBrId());
				kCollApp.put("input_id", currentUserId);
				kCollApp.put("input_br_id", sOrgno);
				kCollApp.put("input_date", openDay);
				kCollApp.put("approve_status", "000");
				kCollApp.put("lrisk_type", "20");
				
				if(iCollAgr.size()>0){
					//过度协议信息到申请表
					KeyedCollection kCollAgr = (KeyedCollection) iCollAgr.get(0);
					String arg_no = kCollAgr.getDataValue("agr_no").toString();
					
					//汇总循环额度、一次性额度
					LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
					KeyedCollection kColl_details = lmtComponent.selectLmtAppIndivAmt(arg_no,"LMT_AGR_DETAILS");
					if(null!=kColl_details){
						kCollApp.put("crd_totl_amt", kColl_details.getDataValue("total_amt"));
						kCollApp.put("crd_cir_amt", kColl_details.getDataValue("crd_cir_amt"));
						kCollApp.put("crd_one_amt", kColl_details.getDataValue("crd_one_amt"));
					}
					
					kCollApp.put("app_type", "02");
					kCollApp.put("biz_type", kCollAgr.getDataValue("biz_type"));
					kCollApp.put("agr_no", arg_no);
					kCollApp.put("org_crd_totl_amt", kCollAgr.getDataValue("crd_totl_amt"));
					kCollApp.put("org_crd_cir_amt", kCollAgr.getDataValue("crd_cir_amt"));
					kCollApp.put("org_crd_one_amt", kCollAgr.getDataValue("crd_one_amt"));
					
					/** 查询该协议下  原有授信 授信类别 给集团授信赋默认值    */
					list.clear();
					list.add("lmt_type");
					condition = " WHERE AGR_NO='"+arg_no+"' AND CRD_AMT > 0 ";
					kColl = dao.queryFirst("LmtAgrDetails", list, condition, connection);
					
					kCollApp.put("lmt_type", kColl.getDataValue("lmt_type"));  //将原有授信类别赋值给申请
					/**END*/
					
//					String conditionStr2 =  " where agr_nSQ50100420130709011728 SQ50100420130709011728 SQ50100420130709011728 o='"+arg_no+"' and lmt_status='10' and add_months(to_date(end_date, 'yyyy-mm-dd'), 6) >= to_date('"+openDay+"','yyyy-mm-dd')";
//					IndexedCollection iCollAgrDet = dao.queryList(modelIdAgrDet, null, conditionStr2, connection);
//					for(int j=0;j<iCollAgrDet.size();j++){
//						//若存在台账则过度信息到申请分项
//						KeyedCollection kCollAgrDet = (KeyedCollection) iCollAgrDet.get(j);
//						KeyedCollection kCollAppDet = new KeyedCollection();
//						String limit_code = CMISSequenceService4JXXD.querySequenceFromED("ED", "all", connection, context);
//						kCollAppDet.setName(modelIdAppDet);
//						kCollAppDet.put("limit_code", limit_code);
//						kCollAppDet.put("serno", serno);
//						kCollAppDet.put("sub_type", kCollAgrDet.getDataValue("sub_type"));
//						kCollAppDet.put("limit_type", kCollAgrDet.getDataValue("limit_type"));
//						kCollAppDet.put("limit_name", kCollAgrDet.getDataValue("limit_name"));
//						kCollAppDet.put("prd_id", kCollAgrDet.getDataValue("prd_id"));
//						kCollAppDet.put("ori_crd_amt", kCollAgrDet.getDataValue("crd_amt"));
//						kCollAppDet.put("crd_amt", kCollAgrDet.getDataValue("crd_amt"));
//						kCollAppDet.put("guar_type", kCollAgrDet.getDataValue("guar_type"));
//						kCollAppDet.put("term_type", kCollAgrDet.getDataValue("term_type"));
//						kCollAppDet.put("term", kCollAgrDet.getDataValue("term"));
//						kCollAppDet.put("org_limit_code", kCollAgrDet.getDataValue("limit_code"));
//						kCollAppDet.put("lrisk_type",kCollAgrDet.getDataValue("lrisk_type"));
//						kCollAppDet.put("cur_type", "CNY");
//						dao.insert(kCollAppDet, connection);
//					}
				}else{
					kCollApp.put("app_type", "01");
					kCollApp.put("biz_type", "01");
				}
				dao.insert(kCollApp, connection);
				
				//变更保存时将原有授信台账复制到申请分项历史表
				LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
				if(kCollApp.containsKey("agr_no")){
					lmtComponent.createLmtAppDetailsRecord((String)kCollApp.getDataValue("agr_no"), serno,"20");
					lmtComponent.createLmtAppDetailsHisRecord((String)kCollApp.getDataValue("agr_no"), serno, "20");
				}
			}
			
			context.put("flag", "success");
			context.put("serno", grp_serno);
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
