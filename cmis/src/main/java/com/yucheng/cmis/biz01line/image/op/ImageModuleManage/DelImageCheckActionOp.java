package com.yucheng.cmis.biz01line.image.op.ImageModuleManage;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.image.op.pubAction.ImagePubAction;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class DelImageCheckActionOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			KeyedCollection kColl = new KeyedCollection();
			ImagePubAction cmisOp = new ImagePubAction();
			String flag = "";
			String call_flag = "true";

			/*** 处理传送参数，组合到kColl ***/
			String image_action = context.getDataValue("image_action").toString();
			String cus_id = context.getDataValue("cus_id").toString();
			String prd_stage = context.getDataValue("prd_stage").toString();
			String prd_id = context.getDataValue("prd_id").toString();
			String serno = context.getDataValue("serno").toString();
			
			kColl.addDataField("cus_id", cus_id);	//客户编号
			kColl.addDataField("prd_stage", prd_stage);	//业务阶段
			kColl.addDataField("prd_id", prd_id);	//产品编号
			kColl.addDataField("serno", serno);	//业务编号
			kColl.addDataField("image_action",image_action);	//影像接口调用类型

			if(image_action.equals("Check3131")){	//3.1.3.1 客户资料归档接口
				KeyedCollection kColl_Cus = dao.queryDetail("CusBase", serno, connection);
				
				kColl.addDataField("cus_name", kColl_Cus.getDataValue("cus_name"));
				kColl.addDataField("main_br_id", kColl_Cus.getDataValue("main_br_id"));
				kColl.addDataField("cust_mgr", kColl_Cus.getDataValue("cust_mgr"));
				kColl.addDataField("input_id", kColl_Cus.getDataValue("input_id"));
				kColl.addDataField("input_br_id", kColl_Cus.getDataValue("input_br_id"));
				kColl.addDataField("input_date", kColl_Cus.getDataValue("input_date"));
				kColl.addDataField("cus_type", kColl_Cus.getDataValue("belg_line"));
				kColl.addDataField("OPENDAY", kColl_Cus.getDataValue("last_update_date"));
				kColl.addDataField("currentUserId", context.getDataValue("currentUserId"));	//取当前用户id为核对人ID
				kColl.addDataField("currentUserName", context.getDataValue("currentUserName"));	//取当前用户name为核对人name				
				
				SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id","main_br_id" });
				SInfoUtils.addUSerName(kColl, new String[] { "input_id","cust_mgr" });
			}else if(image_action.equals("Check3132")){	//3.1.3.2 业务资料归档接口 
				String str = (prd_id.substring(0,1));
				if(serno.indexOf(",") >= 0){	//展期要同时传原业务编号和展期编号，中间以","隔开
					String[] sernos = serno.split(",");
					KeyedCollection kColl_trans = new KeyedCollection();
					kColl_trans.addDataField("submitType", "GetExtensionInfo");
					/**modified by lisj 2015-7-30 修复展期影像核对报错bug,于2015-7-30上线 begin**/
					kColl_trans.addDataField("info_type", sernos[0]);	//取展期信息时，要用展期业务编号
					/**modified by lisj 2015-7-30 修复展期影像核对报错bug,于2015-7-30上线 end**/
					KeyedCollection kColl_extension = cmisOp.delSqlReturnKcoll(kColl_trans, context);

					
					kColl.addDataField("cus_name", kColl_extension.getDataValue("cus_name"));
					kColl.addDataField("agr_no", kColl_extension.getDataValue("agr_no"));
					kColl.addDataField("cn_cont_no", kColl_extension.getDataValue("cn_cont_no"));
					kColl.addDataField("sign_date", kColl_extension.getDataValue("sign_date"));
					kColl.addDataField("status", kColl_extension.getDataValue("status"));
					kColl.addDataField("extension_amt", kColl_extension.getDataValue("extension_amt"));
					kColl.addDataField("manager_br_id", kColl_extension.getDataValue("manager_br_id"));
					kColl.addDataField("input_id", kColl_extension.getDataValue("input_id"));
					kColl.addDataField("manager_id", kColl_extension.getDataValue("manager_id"));
					kColl.addDataField("ill_flag", kColl_extension.getDataValue("ill_flag"));
					kColl.addDataField("clearance_date", kColl_extension.getDataValue("clearance_date"));
					kColl.addDataField("assure_main", kColl_extension.getDataValue("assure_main"));
					kColl.addDataField("fount_bill_no", kColl_extension.getDataValue("fount_bill_no"));
					kColl.addDataField("fount_cont_no", kColl_extension.getDataValue("fount_cont_no"));
					kColl.addDataField("fount_start_date", kColl_extension.getDataValue("fount_start_date"));
					kColl.addDataField("fount_end_date", kColl_extension.getDataValue("fount_end_date"));
					kColl.addDataField("extension_date", kColl_extension.getDataValue("extension_date"));
					kColl.addDataField("currentUserId", context.getDataValue("currentUserId"));
					kColl.addDataField("currentUserName", context.getDataValue("currentUserName"));
					
					SInfoUtils.addSOrgName(kColl, new String[] { "manager_br_id" });
					SInfoUtils.addUSerName(kColl, new String[] { "input_id","manager_id" });
				}else if(str.equals("1") || str.equals("2") || str.equals("4") 
						||str.equals("5") || str.equals("7")|| str.equals("8")){	//公用合同主表的产品一起组公共部分
					
					KeyedCollection kColl_trans = new KeyedCollection();
					kColl_trans.addDataField("submitType", "GetPubBusinessInfo");
					kColl_trans.addDataField("info_type", serno);
					KeyedCollection kColl_iqp = cmisOp.delSqlReturnKcoll(kColl_trans, context);
					
					kColl.addDataField("cus_name", kColl_iqp.getDataValue("cus_name"));
					kColl.addDataField("cont_no", kColl_iqp.getDataValue("cont_no"));
					kColl.addDataField("cn_cont_no", kColl_iqp.getDataValue("cn_cont_no"));
					kColl.addDataField("ser_date", kColl_iqp.getDataValue("ser_date"));
					kColl.addDataField("cont_status", kColl_iqp.getDataValue("cont_status"));
					kColl.addDataField("cont_amt", kColl_iqp.getDataValue("cont_amt"));
					kColl.addDataField("manager_br_id", kColl_iqp.getDataValue("manager_br_id"));
					kColl.addDataField("manager_id", kColl_iqp.getDataValue("manager_id"));
					kColl.addDataField("input_id", kColl_iqp.getDataValue("input_id"));
					kColl.addDataField("ill_flag", kColl_iqp.getDataValue("ill_flag"));
					kColl.addDataField("cancel_date", kColl_iqp.getDataValue("cancel_date"));
					kColl.addDataField("currentUserId", context.getDataValue("currentUserId"));
					kColl.addDataField("currentUserName", context.getDataValue("currentUserName"));
										
					if(str.equals("1") ||str.equals("5") || str.equals("7")|| str.equals("8")){	//产品编号1开头为贷款类
						/*** 20140328需求，国业按普通贷款报文格式处理  ***/
						kColl.addDataField("assure_main", kColl_iqp.getDataValue("assure_main"));
						kColl.addDataField("cont_start_date", kColl_iqp.getDataValue("cont_start_date"));
						kColl.addDataField("cont_end_date", kColl_iqp.getDataValue("cont_end_date"));
					}else if(str.equals("2")){	//产品编号2开头为银承
						KeyedCollection kColl_Accp = dao.queryDetail("IqpAccAccp", serno, connection);
						kColl.addDataField("bill_qty", kColl_Accp.getDataValue("bill_qty"));
						kColl.addDataField("assure_main", kColl_iqp.getDataValue("assure_main"));
						kColl.addDataField("security_rate", kColl_iqp.getDataValue("security_rate"));
					}else if(str.equals("4")){	//产品编号4开头为保函
						KeyedCollection kColl_Guarant = dao.queryDetail("IqpGuarantInfo", serno, connection);
						kColl.addDataField("guarant_type", kColl_Guarant.getDataValue("guarant_type"));
						kColl.addDataField("assure_main", kColl_iqp.getDataValue("assure_main"));
						kColl.addDataField("security_rate", kColl_iqp.getDataValue("security_rate"));
					}
					
					SInfoUtils.addSOrgName(kColl, new String[] { "manager_br_id" });
					SInfoUtils.addUSerName(kColl, new String[] { "input_id","manager_id" });
				}else if(prd_id.equals("300020") || prd_id.equals("300021")){	//贴现
					KeyedCollection kColl_trans = new KeyedCollection();
					kColl_trans.addDataField("submitType", "GetCtrDiscInfo");
					kColl_trans.addDataField("info_type", serno);
					KeyedCollection kColl_iqp = cmisOp.delSqlReturnKcoll(kColl_trans, context);
					
					kColl.addDataField("cus_name", kColl_iqp.getDataValue("cus_name"));
					kColl.addDataField("cont_no", kColl_iqp.getDataValue("cont_no"));
					kColl.addDataField("cn_cont_no", kColl_iqp.getDataValue("cn_cont_no"));
					kColl.addDataField("ser_date", kColl_iqp.getDataValue("ser_date"));
					kColl.addDataField("cont_status", kColl_iqp.getDataValue("cont_status"));
					kColl.addDataField("cont_amt", kColl_iqp.getDataValue("cont_amt"));
					kColl.addDataField("manager_br_id", kColl_iqp.getDataValue("manager_br_id"));
					kColl.addDataField("manager_id", kColl_iqp.getDataValue("manager_id"));
					kColl.addDataField("input_id", kColl_iqp.getDataValue("input_id"));
					kColl.addDataField("ill_flag", kColl_iqp.getDataValue("ill_flag"));
					kColl.addDataField("cancel_date", kColl_iqp.getDataValue("cancel_date"));
					kColl.addDataField("currentUserId", context.getDataValue("currentUserId"));
					kColl.addDataField("currentUserName", context.getDataValue("currentUserName"));
					kColl.addDataField("bill_type", kColl_iqp.getDataValue("bill_type"));
					kColl.addDataField("bill_qty", kColl_iqp.getDataValue("bill_qty"));

					SInfoUtils.addSOrgName(kColl, new String[] { "manager_br_id" });
					SInfoUtils.addUSerName(kColl, new String[] { "input_id","manager_id" });
				}else if(prd_id.equals("300022") || prd_id.equals("300023") || prd_id.equals("300024")){	//转贴现
					KeyedCollection kColl_trans = new KeyedCollection();
					kColl_trans.addDataField("submitType", "GetRpddscntInfo");
					kColl_trans.addDataField("info_type", serno);
					KeyedCollection kColl_rpddscnt = cmisOp.delSqlReturnKcoll(kColl_trans, context);
					
					kColl.addDataField("this_acct_name", kColl_rpddscnt.getDataValue("this_acct_name"));
					kColl.addDataField("cont_no", kColl_rpddscnt.getDataValue("cont_no"));
					kColl.addDataField("cn_cont_no", kColl_rpddscnt.getDataValue("cn_cont_no"));
					kColl.addDataField("rpddscnt_date", kColl_rpddscnt.getDataValue("rpddscnt_date"));
					kColl.addDataField("cont_status", kColl_rpddscnt.getDataValue("cont_status"));
					kColl.addDataField("bill_total_amt", kColl_rpddscnt.getDataValue("bill_total_amt"));
					kColl.addDataField("manager_br_id", kColl_rpddscnt.getDataValue("manager_br_id"));
					kColl.addDataField("manager_id", kColl_rpddscnt.getDataValue("manager_id"));
					kColl.addDataField("input_id", kColl_rpddscnt.getDataValue("input_id"));
					kColl.addDataField("ill_flag", kColl_rpddscnt.getDataValue("ill_flage"));
					kColl.addDataField("bill_type", kColl_rpddscnt.getDataValue("bill_type"));
					kColl.addDataField("bill_qty", kColl_rpddscnt.getDataValue("bill_qnt"));
					kColl.addDataField("currentUserId", context.getDataValue("currentUserId"));
					kColl.addDataField("currentUserName", context.getDataValue("currentUserName"));

					SInfoUtils.addSOrgName(kColl, new String[] { "manager_br_id" });
					SInfoUtils.addUSerName(kColl, new String[] { "input_id","manager_id" });
				}else if(str.equals("6")){	//产品编号6开头为资产转受让
					KeyedCollection kColl_trans = new KeyedCollection();
					kColl_trans.addDataField("submitType", "GetAssetstrsfInfo");
					kColl_trans.addDataField("info_type", serno);
					KeyedCollection kColl_assetstrsf = cmisOp.delSqlReturnKcoll(kColl_trans, context);
					
					kColl.addDataField("this_acct_name", kColl_assetstrsf.getDataValue("this_acct_name"));
					kColl.addDataField("cont_no", kColl_assetstrsf.getDataValue("cont_no"));
					kColl.addDataField("cn_cont_no", kColl_assetstrsf.getDataValue("cn_cont_no"));
					kColl.addDataField("takeover_date", kColl_assetstrsf.getDataValue("takeover_date"));
					kColl.addDataField("cont_status", kColl_assetstrsf.getDataValue("cont_status"));
					kColl.addDataField("asset_total_amt", kColl_assetstrsf.getDataValue("asset_total_amt"));
					kColl.addDataField("manager_id", kColl_assetstrsf.getDataValue("manager_id"));
					kColl.addDataField("manager_br_id", kColl_assetstrsf.getDataValue("manager_br_id"));
					kColl.addDataField("input_id", kColl_assetstrsf.getDataValue("input_id"));
					kColl.addDataField("ill_flag", kColl_assetstrsf.getDataValue("ill_flage"));
					kColl.addDataField("takeover_total_amt", kColl_assetstrsf.getDataValue("takeover_total_amt"));
					kColl.addDataField("takeover_qnt", kColl_assetstrsf.getDataValue("takeover_qnt"));
					kColl.addDataField("takeover_type", kColl_assetstrsf.getDataValue("takeover_type"));
					kColl.addDataField("currentUserId", context.getDataValue("currentUserId"));
					kColl.addDataField("currentUserName", context.getDataValue("currentUserName"));
					
					SInfoUtils.addSOrgName(kColl, new String[] { "manager_br_id" });
					SInfoUtils.addUSerName(kColl, new String[] { "input_id","manager_id" });
				}
			}else if(image_action.equals("Check3133")){	//3.1.3.3 担保资料归档接口
				KeyedCollection kColl_trans = new KeyedCollection();
				/**modified  by yezm 2015-8-14  需求编号：【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程 begin**/
				if("PO".equals(prd_id)){//应收账款池/保理池影像核对
					kColl_trans.addDataField("submitType", "GetActrecpoMana");
				}else if ("PJ".equals(prd_id)){//票据池影像核对
					kColl_trans.addDataField("submitType", "GetDrfpoMana");
				}else if("BZRYXHD".equals(prd_id)){//保证人影像核对
					kColl_trans.addDataField("submitType", "GetGrtGuarantee");
				}else{
					kColl_trans.addDataField("submitType", "GetGuarantyInfo");
				}
				kColl_trans.addDataField("info_type", serno);	//serno传的值即是押品编号
				KeyedCollection kColl_Mort = cmisOp.delSqlReturnKcoll(kColl_trans, context);
				
				if(kColl_Mort != null){
					if("PO".equals(prd_id)){//应收账款池/保理池影像核对
						kColl.addDataField("guaranty_no", kColl_Mort.getDataValue("image_guaranty_no"));
						kColl.addDataField("guaranty_name", kColl_Mort.getDataValue("po_cus_name"));
						kColl.addDataField("guar_way", kColl_Mort.getDataValue("guar_way"));
						kColl.addDataField("guaranty_type", kColl_Mort.getDataValue("guar_model"));
						kColl.addDataField("guar_cont_no", kColl_Mort.getDataValue("guar_cont_no"));
						kColl.addDataField("guar_cont_type", kColl_Mort.getDataValue("guar_cont_type"));
						kColl.addDataField("guar_cont_cn_no", kColl_Mort.getDataValue("guar_cont_cn_no"));
						kColl.addDataField("grt_cus_id", kColl_Mort.getDataValue("grt_cus_id"));
						kColl.addDataField("grt_cus_name", kColl_Mort.getDataValue("grt_cus_name"));
						kColl.addDataField("mort_cus_id", kColl_Mort.getDataValue("po_cus_id"));
						kColl.addDataField("mort_cus_name", kColl_Mort.getDataValue("po_cus_name"));
						kColl.addDataField("guar_amt", kColl_Mort.getDataValue("guar_amt"));
						kColl.addDataField("guar_start_date", kColl_Mort.getDataValue("guar_start_date"));
						kColl.addDataField("guar_end_date", kColl_Mort.getDataValue("guar_end_date"));
						kColl.addDataField("input_id", kColl_Mort.getDataValue("input_id"));
						kColl.addDataField("input_br_id", kColl_Mort.getDataValue("input_br_id"));
						kColl.addDataField("manager_id", kColl_Mort.getDataValue("manager_id"));
						kColl.addDataField("manager_br_id", kColl_Mort.getDataValue("manager_br_id"));
						kColl.addDataField("input_date", kColl_Mort.getDataValue("input_date"));
						kColl.addDataField("currentUserId", context.getDataValue("currentUserId"));
						kColl.addDataField("currentUserName", context.getDataValue("currentUserName"));
						
						SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id","manager_br_id"});
						SInfoUtils.addUSerName(kColl, new String[] { "input_id" ,"manager_id"});
					}else if ("PJ".equals(prd_id)){//票据池影像核对
						kColl.addDataField("guaranty_no", kColl_Mort.getDataValue("image_guaranty_no"));
						kColl.addDataField("guaranty_name", kColl_Mort.getDataValue("pj_cus_name"));
						kColl.addDataField("guar_way", kColl_Mort.getDataValue("guar_way"));
						kColl.addDataField("guaranty_type", kColl_Mort.getDataValue("guar_model"));
						kColl.addDataField("guar_cont_no", kColl_Mort.getDataValue("guar_cont_no"));
						kColl.addDataField("guar_cont_type", kColl_Mort.getDataValue("guar_cont_type"));
						kColl.addDataField("guar_cont_cn_no", kColl_Mort.getDataValue("guar_cont_cn_no"));
						kColl.addDataField("grt_cus_id", kColl_Mort.getDataValue("grt_cus_id"));
						kColl.addDataField("grt_cus_name", kColl_Mort.getDataValue("grt_cus_name"));
						kColl.addDataField("mort_cus_id", kColl_Mort.getDataValue("pj_cus_id"));
						kColl.addDataField("mort_cus_name", kColl_Mort.getDataValue("pj_cus_name"));
						kColl.addDataField("guar_amt", kColl_Mort.getDataValue("guar_amt"));
						kColl.addDataField("guar_start_date", kColl_Mort.getDataValue("guar_start_date"));
						kColl.addDataField("guar_end_date", kColl_Mort.getDataValue("guar_end_date"));
						kColl.addDataField("input_id", kColl_Mort.getDataValue("input_id"));
						kColl.addDataField("input_br_id", kColl_Mort.getDataValue("input_br_id"));
						kColl.addDataField("manager_id", kColl_Mort.getDataValue("manager_id"));
						kColl.addDataField("manager_br_id", kColl_Mort.getDataValue("manager_br_id"));
						kColl.addDataField("input_date", kColl_Mort.getDataValue("input_date"));
						kColl.addDataField("currentUserId", context.getDataValue("currentUserId"));
						kColl.addDataField("currentUserName", context.getDataValue("currentUserName"));						
						
						SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id","manager_br_id"});
						SInfoUtils.addUSerName(kColl, new String[] { "input_id" ,"manager_id"});
					}else if("BZRYXHD".equals(prd_id)){//保证人影像核对
					/**modified  by yezm 2015-8-14  需求编号：【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程 end**/
						kColl.addDataField("guaranty_no", kColl_Mort.getDataValue("guar_id"));
						kColl.addDataField("guaranty_name", kColl_Mort.getDataValue("guarantee_cus_name"));
						kColl.addDataField("guar_way", kColl_Mort.getDataValue("guar_way"));
						kColl.addDataField("guaranty_type", kColl_Mort.getDataValue("guar_model"));
						kColl.addDataField("guar_cont_no", kColl_Mort.getDataValue("guar_cont_no"));
						kColl.addDataField("guar_cont_type", kColl_Mort.getDataValue("guar_cont_type"));
						kColl.addDataField("guar_cont_cn_no", kColl_Mort.getDataValue("guar_cont_cn_no"));
						kColl.addDataField("grt_cus_id", kColl_Mort.getDataValue("grt_cus_id"));
						kColl.addDataField("grt_cus_name", kColl_Mort.getDataValue("grt_cus_name"));
						kColl.addDataField("mort_cus_id", kColl_Mort.getDataValue("guarantee_cus_id"));
						kColl.addDataField("mort_cus_name", kColl_Mort.getDataValue("guarantee_cus_name"));
						kColl.addDataField("guar_amt", kColl_Mort.getDataValue("guar_amt"));
						kColl.addDataField("guar_start_date", kColl_Mort.getDataValue("guar_start_date"));
						kColl.addDataField("guar_end_date", kColl_Mort.getDataValue("guar_end_date"));
						kColl.addDataField("input_id", kColl_Mort.getDataValue("input_id"));
						kColl.addDataField("input_br_id", kColl_Mort.getDataValue("input_br_id"));
						kColl.addDataField("manager_id", kColl_Mort.getDataValue("manager_id"));
						kColl.addDataField("manager_br_id", kColl_Mort.getDataValue("manager_br_id"));
						kColl.addDataField("input_date", kColl_Mort.getDataValue("input_date"));
						kColl.addDataField("currentUserId", context.getDataValue("currentUserId"));
						kColl.addDataField("currentUserName", context.getDataValue("currentUserName"));
						
						SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id","manager_br_id"});
						SInfoUtils.addUSerName(kColl, new String[] { "input_id" ,"manager_id"});
					}else{
						kColl.addDataField("guaranty_no", kColl_Mort.getDataValue("image_guaranty_no"));
						kColl.addDataField("guaranty_name", kColl_Mort.getDataValue("guaranty_name"));
						kColl.addDataField("guar_way", kColl_Mort.getDataValue("guar_way"));
						kColl.addDataField("guaranty_type", kColl_Mort.getDataValue("guaranty_type"));
						kColl.addDataField("guar_cont_no", kColl_Mort.getDataValue("guar_cont_no"));
						kColl.addDataField("guar_cont_type", kColl_Mort.getDataValue("guar_cont_type"));
						kColl.addDataField("guar_cont_cn_no", kColl_Mort.getDataValue("guar_cont_cn_no"));
						kColl.addDataField("grt_cus_id", kColl_Mort.getDataValue("grt_cus_id"));
						kColl.addDataField("grt_cus_name", kColl_Mort.getDataValue("grt_cus_name"));
						kColl.addDataField("mort_cus_id", kColl_Mort.getDataValue("mort_cus_id"));
						kColl.addDataField("mort_cus_name", kColl_Mort.getDataValue("mort_cus_name"));
						kColl.addDataField("guar_amt", kColl_Mort.getDataValue("guar_amt"));
						kColl.addDataField("guar_start_date", kColl_Mort.getDataValue("guar_start_date"));
						kColl.addDataField("guar_end_date", kColl_Mort.getDataValue("guar_end_date"));
						kColl.addDataField("input_id", kColl_Mort.getDataValue("input_id"));
						kColl.addDataField("input_br_id", kColl_Mort.getDataValue("input_br_id"));
						kColl.addDataField("manager_id", kColl_Mort.getDataValue("manager_id"));
						kColl.addDataField("manager_br_id", kColl_Mort.getDataValue("manager_br_id"));
						kColl.addDataField("input_date", kColl_Mort.getDataValue("input_date"));
						kColl.addDataField("currentUserId", context.getDataValue("currentUserId"));
						kColl.addDataField("currentUserName", context.getDataValue("currentUserName"));
						
						SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id","manager_br_id"});
						SInfoUtils.addUSerName(kColl, new String[] { "input_id" ,"manager_id"});
					}
				}else{
					call_flag = "no_guaranty_no";
				}				
			}else if(image_action.equals("Check3134")){	//3.1.3.4 贷后资料归档接口
				KeyedCollection kColl_Psp = dao.queryDetail("PspCheckTask", serno, connection);
				
				kColl.addDataField("task_id", serno);
				kColl.addDataField("qnt", kColl_Psp.getDataValue("qnt"));
				kColl.addDataField("loan_totl_amt", kColl_Psp.getDataValue("loan_totl_amt"));
				kColl.addDataField("task_create_date", kColl_Psp.getDataValue("task_create_date"));
				kColl.addDataField("task_request_time", kColl_Psp.getDataValue("task_request_time"));
				kColl.addDataField("task_huser", kColl_Psp.getDataValue("task_huser"));
				kColl.addDataField("task_horg", kColl_Psp.getDataValue("task_horg"));
				kColl.addDataField("currentUserId", context.getDataValue("currentUserId"));
				kColl.addDataField("currentUserName", context.getDataValue("currentUserName"));
				
				String[] args=new String[] { "cus_id" };
				String[] modelIds=new String[]{"CusBase"};
				String[] modelForeign=new String[]{"cus_id"};
				String[] fieldName=new String[]{"cus_name"};
				SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
				SInfoUtils.addSOrgName(kColl, new String[] { "task_horg"});
				SInfoUtils.addUSerName(kColl, new String[] { "task_huser"});
			}
			
			KeyedCollection retKColl = new KeyedCollection();
			if(call_flag.equals("true")){
				retKColl = serviceRel.tradeXYHD(kColl, context, this.getConnection(context));	//调用影像核对接口
				if(!retKColl.getDataValue("RET_CODE").equals("000000")){
					flag = "ESB交易调用出错!";
					//flag = retKColl.getDataValue("RET_MSG").toString();
				}else{
					flag = retKColl.getDataValue("RET_MSG").toString();
				}
			}else if(call_flag.equals("no_guaranty_no")){
				flag = "此押品还未生成担保合同!";
			}else{
				flag = "此业务品种没有影像核对!";
			}
			
			context.addDataField("flag", PUBConstant.SUCCESS );
			context.addDataField("value", flag);
			context.addDataField("resultUrl", "");
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)this.releaseConnection(context, connection);
		}
		return "0";
	}

}