package com.yucheng.cmis.biz01line.mort.mortguarantybaseinfo;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateMortGuarantyBaseInfoRecordOp extends CMISOperation {
	

	private final String modelId = "MortGuarantyBaseInfo";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			String guarantyNo="";
			connection = this.getConnection(context);
			//定义获取押品基本信息的kColl
			KeyedCollection mortGuarantyBaseInfoKcoll = null;
			//定义获取权证信息的kColl
			KeyedCollection mortGuarantyCertiInfoKcoll = null;
			//定义获取共有人信息的kColl
			KeyedCollection mortCommenOwnerKcoll = null;
			TableModelDAO dao = this.getTableModelDAO(context);
			//获取押品基本信息为数据库操作做数据收集
			mortGuarantyBaseInfoKcoll = (KeyedCollection)context.getDataElement(modelId);
			//新增共有人信息
			guarantyNo=(String) mortGuarantyBaseInfoKcoll.getDataValue("guaranty_no");
			String flag = (String) mortGuarantyBaseInfoKcoll.getDataValue("is_common_owner");
			String state = (String)mortGuarantyBaseInfoKcoll.getDataValue("guaranty_info_status");
			if(state!=null&&"4".equals(state)){
				mortGuarantyBaseInfoKcoll.put("guaranty_info_status", "2");//出库核销做修改，更改押品信息状态为登记完成
			}
			if(flag.equals("1")){
				if(context.containsKey("MortCommenOwnerList")){
				IndexedCollection iColl = (IndexedCollection)context.getDataElement("MortCommenOwnerList");
					if(null != iColl && iColl.size()>0){
						String optType="";
						for (Iterator<KeyedCollection> iterator = iColl.iterator(); iterator.hasNext();) {
							mortCommenOwnerKcoll = (KeyedCollection) iterator.next();
							optType=(String) mortCommenOwnerKcoll.getDataValue("optType");
							mortCommenOwnerKcoll.setName("MortCommenOwner");
							if("add".equals(optType)){
								mortCommenOwnerKcoll.setDataValue("guaranty_no",guarantyNo);
								dao.insert(mortCommenOwnerKcoll, connection);
							}else if("del".equals(optType)){
								if(mortCommenOwnerKcoll.containsKey("commen_owner_no")&&mortCommenOwnerKcoll.getDataValue("commen_owner_no")!=null&&!"".equals(mortCommenOwnerKcoll.getDataValue("commen_owner_no"))){
									Map pkMap = new HashMap();
									pkMap.put("commen_owner_no",mortCommenOwnerKcoll.getDataValue("commen_owner_no").toString());
									pkMap.put("guaranty_no",guarantyNo);
									dao.deleteByPks("MortCommenOwner", pkMap, connection);
									//删除
								}
								//dao.update(mortCommenOwnerKcoll, connection);
							}
						}
					}
					//获取权证基本信息为数据库操作做数据收集
					mortGuarantyCertiInfoKcoll = (KeyedCollection)context.getDataElement("MortGuarantyCertiInfo");
					mortGuarantyCertiInfoKcoll.setName("MortGuarantyCertiInfo");
					
					//用传进来会乱码，原权证编号和权证类型改为从表中取  modify by zhaozq 20140711
					KeyedCollection certiKcollold = dao.queryFirst("MortGuarantyCertiInfo", null, " where guaranty_no = '"+guarantyNo+"' and is_main_warrant = '1' ", connection);
					String warrantNoOld = "";
					String warrantTypeOld = "";
					if(certiKcollold!=null){
						if(certiKcollold.getDataValue("warrant_no")!=null){
							warrantNoOld = (String) certiKcollold.getDataValue("warrant_no");
						}
						if(certiKcollold.getDataValue("warrant_type")!=null){
							warrantTypeOld = (String) certiKcollold.getDataValue("warrant_type");
						}
					}
					//用传进来会乱码，原权证编号和权证类型改为从表中取  modify by zhaozq 20140711
					
					warrantNoOld =warrantNoOld.trim();
					warrantTypeOld=warrantTypeOld.trim();
					//更新权证基本信息
					if(null!=mortGuarantyCertiInfoKcoll.getDataValue("warrant_no")){
						Map<String,String> map = new HashedMap();
						map.put("warrant_no",warrantNoOld);
						map.put("warrant_type",warrantTypeOld);
						if(warrantNoOld.equals(mortGuarantyCertiInfoKcoll.getDataValue("warrant_no"))&&warrantTypeOld.equals(mortGuarantyCertiInfoKcoll.getDataValue("warrant_type"))){//更新
							dao.update(mortGuarantyCertiInfoKcoll, connection);
						}else{
							dao.deleteByPks("MortGuarantyCertiInfo",map,connection);
							KeyedCollection newKc = (KeyedCollection) mortGuarantyCertiInfoKcoll.clone();
							dao.insert(newKc, connection);
						}
					}
					//更新押品基本信息
					
					int cout = dao.update(mortGuarantyBaseInfoKcoll, connection);
					context.addDataField("flag", "success");
					context.addDataField("msg","true");
				}else{
					context.addDataField("msg","fail");
					context.addDataField("flag", "fail");
				}
			}else{
				//更新押品基本信息
				int cout = dao.update(mortGuarantyBaseInfoKcoll, connection);
				//获取权证基本信息为数据库操作做数据收集
				mortGuarantyCertiInfoKcoll = (KeyedCollection)context.getDataElement("MortGuarantyCertiInfo");
				mortGuarantyCertiInfoKcoll.setName("MortGuarantyCertiInfo");

				//用传进来会乱码，原权证编号和权证类型改为从表中取  modify by zhaozq 20140711
				KeyedCollection certiKcollold = dao.queryFirst("MortGuarantyCertiInfo", null, " where guaranty_no = '"+guarantyNo+"' and is_main_warrant = '1' ", connection);
				String warrantNoOld = "";
				String warrantTypeOld = "";
				if(certiKcollold!=null){
					if(certiKcollold.getDataValue("warrant_no")!=null){
						warrantNoOld = (String) certiKcollold.getDataValue("warrant_no");
					}
					if(certiKcollold.getDataValue("warrant_type")!=null){
						warrantTypeOld = (String) certiKcollold.getDataValue("warrant_type");
					}
				}
				//用传进来会乱码，原权证编号和权证类型改为从表中取  modify by zhaozq 20140711
				
				warrantNoOld =warrantNoOld.trim();
				warrantTypeOld=warrantTypeOld.trim();
				//更新权证基本信息
				if(null!=mortGuarantyCertiInfoKcoll.getDataValue("warrant_no")){
					Map<String,String> map = new HashedMap();
					map.put("warrant_no",warrantNoOld);
					map.put("warrant_type",warrantTypeOld);
					if(warrantNoOld.equals(mortGuarantyCertiInfoKcoll.getDataValue("warrant_no"))&&warrantTypeOld.equals(mortGuarantyCertiInfoKcoll.getDataValue("warrant_type"))){//更新
						dao.update(mortGuarantyCertiInfoKcoll, connection);
					}else{
						dao.deleteByPks("MortGuarantyCertiInfo",map,connection);
						KeyedCollection newKc = (KeyedCollection) mortGuarantyCertiInfoKcoll.clone();
						dao.insert(newKc, connection);
					}
				}
				context.addDataField("flag", "success");
				context.addDataField("msg","true");
			}
			
		}catch (EMPException ee) {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0,"保存押品信息失败"+ee.getMessage(),null);
			throw ee;
		} catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0,"保存押品信息失败"+e.getMessage(),null);
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
