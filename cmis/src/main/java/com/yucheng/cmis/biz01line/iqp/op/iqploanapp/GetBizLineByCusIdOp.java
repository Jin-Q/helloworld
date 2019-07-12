package com.yucheng.cmis.biz01line.iqp.op.iqploanapp;

import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class GetBizLineByCusIdOp extends CMISOperation {

	private static final String CUSBASE = "CusBase";
	@Override
	public String doExecute(Context context) throws EMPException {
		/** 通过客户ID获得客户所属业务线 */
		try {
			String cusId = "";
			String bizline = "";
			cusId = (String)context.getDataValue("cusid");
			TableModelDAO dao = this.getTableModelDAO(context);
			
			List file = new ArrayList<String>();
			file.add("cus_type");
			KeyedCollection kc = dao.queryFirst(CUSBASE, file, " where cus_id='"+cusId+"'", this.getConnection(context));
			if(kc == null){
				context.addDataField("flag", "failed");
				context.addDataField("msg", "获取客户基础信息失败！");
				context.addDataField("bizline", "");
				throw new Exception("获取客户基础信息失败！");
			}
			/**
			 * 根据客户类型判断客户所属客户类别
			 * 对公客户类型以2开头，个人客户以1开头
			 */
			List fileLine = new ArrayList<String>();
			fileLine.add("belg_line");
			String cus_type = (String)kc.getDataValue("cus_type");
			if(cus_type == null){
				context.addDataField("flag", "failed");
				context.addDataField("msg", "该客户未录入客户类型！");
				context.addDataField("bizline", "");
				throw new Exception("该客户未录入客户类型！");
			}
			/*if(cus_type.startsWith("2")){
				KeyedCollection comKColl = dao.queryFirst("CusCom", fileLine, " where cus_id='"+cusId+"'", this.getConnection(context));
				if(comKColl.size() == 0){
					context.addDataField("flag", "failed");
					context.addDataField("msg", "获取对公客户失败！");
					context.addDataField("bizline", "");
					throw new Exception("获取对公客户失败！");
				}
				bizline = (String)comKColl.getDataValue("belg_line");
			}else if(cus_type.startsWith("1")){
				KeyedCollection indivKColl = dao.queryFirst("CusIndiv", fileLine, " where cus_id='"+cusId+"'", this.getConnection(context));
				if(indivKColl.size() == 0){
					context.addDataField("flag", "failed");
					context.addDataField("msg", "获取个人客户失败！");
					context.addDataField("bizline", "");
					throw new Exception("获取个人客户失败！");
				}
				bizline = (String)indivKColl.getDataValue("belg_line");
			}*/
			KeyedCollection baseKColl = dao.queryFirst("CusBase", fileLine, " where cus_id='"+cusId+"'", this.getConnection(context));
			if(baseKColl.size() == 0){
				context.addDataField("flag", "failed");
				context.addDataField("msg", "获取客户失败！");
				context.addDataField("bizline", "");
				throw new Exception("获取客户失败！");
			}else {
				bizline = (String)baseKColl.getDataValue("belg_line");
			}
			if(bizline == null || bizline == "" || bizline.length() == 0){
				context.addDataField("flag", "failed");
				context.addDataField("msg", "该客户未录入所属业务线！");
				context.addDataField("bizline", "");
				throw new Exception("该客户未录入所属业务线！");
			}
			context.addDataField("flag", "success");
			context.addDataField("msg", "");
			context.addDataField("bizline", bizline);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0";
	}

}
