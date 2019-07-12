package com.yucheng.cmis.biz01line.lmt.op.lmtinduslistapply;

import java.sql.Connection;
import java.util.LinkedList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class ImportLmtIndusListApplyRecordsOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String flag = "";
			String fail_cusId = "";
			String repeat_cusId = "";
			
			KeyedCollection Return = (KeyedCollection)context.getDataElement("Return");
			/*** 由于存在手动录入的情况，还是对cus_ids进行各种处理 ***/
			String cus_ids = Return.getDataValue("cus_ids").toString();
			String serno = Return.getDataValue("serno").toString();
			cus_ids = cus_ids.replaceAll(" ", "");
			cus_ids = cus_ids.replaceAll("\n", "");
			cus_ids = cus_ids.replaceAll("\t", "");
			if(cus_ids.endsWith(",")){
				cus_ids = cus_ids.substring(0,cus_ids.length()-1);
			}
			String cus_id[] = cus_ids.split(",");
			cus_id = removeRepeat(cus_id);
			String cus_check[] = new String[cus_id.length];
			String list_check[] = new String[cus_id.length];
			dealChecks(list_check, cus_check, cus_id, context, connection);
			
			/*** 将通过校验的数据存入名单表，并将错误的cus_id返回 ***/
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = new KeyedCollection();
			String menuId = context.getDataValue("menuId").toString();
			if(menuId.equals("indus_crd_agr")){
				kColl.setName("LmtIndusListMana");
				kColl.addDataField("agr_no", serno);
				kColl.addDataField("status", "003");
			}else{
				kColl.setName("LmtIndusListApply");
				kColl.addDataField("serno", serno);
				kColl.addDataField("status", "001");
			}
			kColl.addDataField("memo", "");
			kColl.addDataField("is_do_limit", "2");
			kColl.addDataField("cus_id", "");
			String repeat_list = "已存在于行业名单中的客户码：";
			String id_list = "不存在的客户码：";
			String status_list = "非正式户的客户码：";
			String line_list = "个人条线的客户码：";
			int success_list = 0;
			
			for(int i =0 ;i<cus_id.length;i++){
				if(cus_check[i].equals("right") && list_check[i].equals("right")){	//通过校验，存入名单表
					kColl.setDataValue("cus_id", cus_id[i]);
					dao.insert(kColl, connection);
					flag = dealFlag(flag, "T");
					success_list++;
				}else if(list_check[i].equals("repeat_id")){	//整合已存在于名单的客户码
					repeat_list = repeat_list + cus_id[i] + ",";
					flag = dealFlag(flag, "F");
				}else if(cus_check[i].equals("wrong_id")){	//整合不存在的客户码
					id_list = id_list + cus_id[i] + ",";
					flag = dealFlag(flag, "F");
				}else if(cus_check[i].equals("wrong_status")){	//整合非正式客户的客户码
					status_list = status_list + cus_id[i] + ",";
					flag = dealFlag(flag, "F");
				}else if(cus_check[i].equals("wrong_line")){	//整合个人条线的客户码
					line_list = line_list + cus_id[i] + ",";
					flag = dealFlag(flag, "F");
				}else{	//理论上不会有此情况，如果有，就出错了
					throw new EMPException("未知的错误，请检查类：ImportLmtIndusListApplyRecordsOp.java");
				}
			}
			
			if(flag.indexOf("T")==-1){	//一笔记录都没插入。新增全部失败，flag直接设为fail
				flag = PUBConstant.FAIL;
			}else if(flag.indexOf("F")==-1){	//有成功记录，且没有失败记录。新增全部成功，flag直接设为success
				flag = PUBConstant.SUCCESS;
			}else{	//剩下的成功失败的记录都有，flag设为half_success的同时，将失败的客户码信息返回前台页面
				flag = "half_success";
			}
			repeat_cusId = repeat_list;
			fail_cusId = id_list+"\n"+status_list+"\n"+line_list;
			
			context.addDataField("flag", flag);
			context.addDataField("success_amount", "新增成功的客户记录数："+success_list+"条");
			context.addDataField("fail_cusId", fail_cusId);
			context.addDataField("repeat_cusId", repeat_cusId);
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
	
	public String dealFlag(String flag , String item) {
		if(flag.indexOf(item) == -1){	//符合条件则在flag中加入标识，不重复添加
			flag=flag+item;
		}
		return flag;
	}
	
	public void dealChecks(String[] list_check, String[] cus_check,
			String[] cus_id, Context context, Connection connection)throws EMPException {
		/*** 对传入的cus_id进行校验，包括对客户码的校验和行业名单校验 ***/
		LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory
		.getComponentFactoryInstance().getComponentInstance("LmtPubComponent",context,connection);			
		String[] args = new String[] { "cus_id","cus_id","cus_id"};
		String[] modelIds = new String[] { "CusBase","CusBase","CusBase"};
		String[] modelForeign = new String[] { "cus_id","cus_id","cus_id"};
		String[] fieldName = new String[] { "cus_name" ,"cus_status","belg_line"};
		String[] resultName = new String[] { "cus_name" ,"cus_status","belg_line"};
		for(int i=0;i<cus_id.length;i++){
			/*** 客户码校验，1.客户码存在、2.正式客户、 3.非个人条线***/
			KeyedCollection kColl = new KeyedCollection();
			kColl.addDataField("cus_id", cus_id[i]);
			SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context,modelIds, modelForeign, fieldName ,resultName);
			if(kColl.getDataValue("cus_name").equals("")){
				cus_check[i] = "wrong_id";	//客户名称不存在，没有此客户
			}else if(!kColl.getDataValue("cus_status").equals("20")){
				cus_check[i] = "wrong_status";	//不是正式户
			}else if(kColl.getDataValue("belg_line").equals("BL300") ){
				cus_check[i] = "wrong_line";	//个人条线，条线不对
			}else{
				cus_check[i] = "right";	//正确
			}
			
			/*** 行业授信名单不重复校验，若已存在于名单中则标识为repeat ***/
			if(lmtComponent.getAgrno("indusList", cus_id[i]).equals("0")){
				list_check[i] = "right";	//正确
			}else{
				list_check[i] = "repeat_id";	//已存在于名单
			}
		}
	}
	
	/*** 数组去重复 ***/
	public String[] removeRepeat( String[] str ) {
		LinkedList<String> list = new LinkedList<String>();
		for(int i = 0 ;i < str.length ; i++){
			if(!list.contains(str[i])){
				list.add(str[i]);
			}
		}
		return (String[])list.toArray(new String[list.size()]);		
	}
}