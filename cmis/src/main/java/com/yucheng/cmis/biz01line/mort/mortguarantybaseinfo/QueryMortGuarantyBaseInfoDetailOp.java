package com.yucheng.cmis.biz01line.mort.mortguarantybaseinfo;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.mort.component.MortCommenOwnerComponent;
import com.yucheng.cmis.biz01line.mort.morttool.MORTConstant;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryMortGuarantyBaseInfoDetailOp  extends CMISOperation {
	
	private final String modelId = "MortGuarantyBaseInfo";
	private final String dicTreeTypeId = "MORT_TYPE";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String guaranty_no = (String) context.getDataValue("guaranty_no");
		try{
			connection = this.getConnection(context);
			//树形菜单服务
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			try {
			} catch (Exception e) {}
			if(guaranty_no == null || guaranty_no.length() == 0)
				throw new EMPJDBCException("The value of pk["+guaranty_no+"] cannot be null!");
			
			
			//构建查询共有人信息的组件
			MortCommenOwnerComponent commenOwner = (MortCommenOwnerComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(
					MORTConstant.MORTCOMMENOWNERCOMPONENT, context, connection);
		    
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, guaranty_no, connection);
			//判断是否有共有人，如果有共有人则将共有人相关信息放到context中
			String is_common_owner = (String) kColl.getDataValue("is_common_owner");
			if(is_common_owner.equals("1")){
				IndexedCollection iColl = commenOwner.getCommenList(guaranty_no);
				iColl.setName("MortCommenOwnerList");
				this.putDataElement2Context(iColl, context);
			}
			//判断是否有主权证信息，如果有主权证信息则将主权信息放入context中
			String is_warrant_full = (String) kColl.getDataValue("is_warrant_full");
			if(is_warrant_full.equals("1")){
				//调用组件方法查询相关联的主权证信息
				KeyedCollection kc = commenOwner.getMortGuarantyCertiInfoDetail(guaranty_no);
				kc.setName("MortGuarantyCertiInfo");
				SInfoUtils.addSOrgName(kc, new String[] {"keep_org_no","hand_org_no"});
				this.putDataElement2Context(kc, context);
				//added by yangzy 2015/05/19 押品信息修改页面关于主权证修改校验 start
				if(kc!=null&&kc.size()>0&&kc.containsKey("warrant_state")&&kc.getDataValue("warrant_state")!=null&&!"".equals(kc.getDataValue("warrant_state"))){
					String warrantState = (String) kc.getDataValue("warrant_state");
					if(!"1".equals(warrantState)&&!"6".equals(warrantState)&&!"7".equals(warrantState)){
						context.addDataField("is_warrant_upflag","2");//主权证不能修改
					}
				}
				//added by yangzy 2015/05/19 押品信息修改页面关于主权证修改校验 end
			}
			//获取客户码翻译时用
			String cus_id = (String) kColl.getDataValue("cus_id");
			String guaranty_type = (String)kColl.getDataValue("guaranty_type");
			KeyedCollection KC = (KeyedCollection)(context.getParent().getDataElement());
			String menuId = (String)KC.getDataValue("menuId");
			context.put("menuId",menuId);
			context.put("flag","zcdj");
			if("hwdj".equals(menuId)){
				SInfoUtils.addPrdPopName4Mort("IqpMortCatalogMana", kColl, "guaranty_type", "catalog_no", "catalog_name", "->", connection, dao);  //翻译目录路径
	    		context.put("flag","hwdj");
			}else if("hwgl".equals(menuId)){
				SInfoUtils.addPrdPopName4Mort("IqpMortCatalogMana", kColl, "guaranty_type", "catalog_no", "catalog_name", "->", connection, dao);  //翻译目录路径				
			}else{
				Map<String,String> map = new HashMap<String,String>();
				map.put("guaranty_type","MORT_TYPE");
		    	SInfoUtils.addPopName(kColl, map, service);
			}
			
	    	
			//从客户表获取相关信息
			CusBaseComponent cusBase = (CusBaseComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(
					PUBConstant.CUSBASE, context, connection);
			CusBase cus = cusBase.getCusBase(cus_id);
			kColl.addDataField("cus_name", cus.getCusName());//客户名称
			kColl.addDataField("cus_type", cus.getCusType());//客户类型
			kColl.addDataField("cert_type", cus.getCertType());//证件类型
			kColl.addDataField("cert_code", cus.getCertCode());//证件号码
			//翻译机构和登记人
			SInfoUtils.addUSerName(kColl, new String[] { "manager_id" ,"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[] { "manager_br_id" ,"input_br_id","keep_org_no","hand_org_no"});
			//货物管理时，货物信息所关联的协议信息
			if(context.containsKey("agr_type")&&context.containsKey("agr_no")){
				String agr_type = context.getDataValue("agr_type").toString();
				String agr_no = context.getDataValue("agr_no").toString();
				if(agr_type.equals("00")){//保兑仓
					context.addDataField("depot_agr_no", agr_no);
					context.addDataField("coop_agr_no","");
					context.addDataField("oversee_agr_no","");
				}else if(agr_type.equals("01")){//银企商
					context.addDataField("depot_agr_no","");
					context.addDataField("coop_agr_no",agr_no);
					context.addDataField("oversee_agr_no","");
				}else if(agr_type.equals("02")){//监管协议
					context.addDataField("depot_agr_no", "");
					context.addDataField("coop_agr_no","");
					context.addDataField("oversee_agr_no",agr_no);
				}
			}else{
				context.addDataField("depot_agr_no","");
				context.addDataField("coop_agr_no","");
//				context.addDataField("oversee_agr_no","");
			}
			
			this.putDataElement2Context(kColl, context);
			
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
