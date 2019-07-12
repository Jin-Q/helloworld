package com.yucheng.cmis.biz01line.mort.mortguarantybaseinfo;

import java.sql.Connection;
import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.mort.agent.MortCommenOwnerAgent;
import com.yucheng.cmis.biz01line.mort.component.MortCommenOwnerComponent;
import com.yucheng.cmis.biz01line.mort.morttool.MORTConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddMortGuarantyBaseInfoRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "MortGuarantyBaseInfo";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		
		try{
			connection = this.getConnection(context);
			//定义获取货物与监管协议关系的kColl
			KeyedCollection iqpCargoOverseeReKcoll = new KeyedCollection("IqpCargoOverseeRe");
			//定义获取押品基本信息的kColl
			KeyedCollection mortGuarantyBaseInfoKcoll = null;
			//定义获取权证信息的kColl
			KeyedCollection mortGuarantyCertiInfoKcoll = null;
			//定义获取共有人信息的kColl
			KeyedCollection mortCommenOwnerKcoll = null;
			TableModelDAO dao = this.getTableModelDAO(context);
			//获取押品基本信息为数据库操作做数据收集
			mortGuarantyBaseInfoKcoll = (KeyedCollection)context.getDataElement(modelId);
			//获取货物与监管协议关系信息为数据库操作数据收集
			iqpCargoOverseeReKcoll.addDataField("agr_type",mortGuarantyBaseInfoKcoll.getDataValue("agr_type"));
			iqpCargoOverseeReKcoll.addDataField("agr_no",mortGuarantyBaseInfoKcoll.getDataValue("agr_no"));
			
			//新增共有人信息
			String flag = (String) mortGuarantyBaseInfoKcoll.getDataValue("is_common_owner");
			//主键生成器
			String guarantyNo = CMISSequenceService4JXXD.querySequenceFromDB("MT", "fromDate", connection, context);
			//判断是否需要对货物与监管协议表进行操作
			if(!iqpCargoOverseeReKcoll.getDataValue("agr_type").equals("")&&!iqpCargoOverseeReKcoll.getDataValue("agr_no").equals("")){//需要操作
				iqpCargoOverseeReKcoll.addDataField("guaranty_no", guarantyNo);
				//构建组件类
				MortCommenOwnerComponent reComponent = (MortCommenOwnerComponent) CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance(
						MORTConstant.MORTCOMMENOWNERCOMPONENT,context, connection);
				int res = reComponent.insertIqpCargoOverseeReRecord(iqpCargoOverseeReKcoll);
				if(res==0)
					throw new ComponentException("货物与监管协议关系记录新增失败");
			}
			if(context.containsKey("relation")){
				String relation = (String) context.getDataValue("relation");
				if(relation.equals("guar")){
					String guar_cont_no = (String) context.getDataValue("guar_cont_no");
					KeyedCollection kColl = new KeyedCollection("GrtGuarantyRe");
					kColl.addDataField("guar_cont_no", guar_cont_no);
					kColl.addDataField("guaranty_id", guarantyNo);
					dao.insert(kColl, connection);
				}
			}
			if(context.containsKey("rel")){//
				String rel = (String) context.getDataValue("rel");
				if(rel.equals("rel")){
					String serno = (String) context.getDataValue("serno");
					KeyedCollection kColl = new KeyedCollection("ArpCollDebtRe");
					kColl.addDataField("serno",serno);
					kColl.addDataField("guaranty_no", guarantyNo);
					kColl.addDataField("rel","2");
					kColl.addDataField("status","00");
					dao.insert(kColl, connection);
				}
			}
			if(flag.equals("1")){//押品有共有人信息
				if(context.containsKey("MortCommenOwnerList")){
				IndexedCollection iColl = (IndexedCollection)context.getDataElement("MortCommenOwnerList");
					if(null != iColl && iColl.size()>0){
						for (Iterator<KeyedCollection> iterator = iColl.iterator(); iterator.hasNext();) {
							mortCommenOwnerKcoll = (KeyedCollection) iterator.next();
							
							if(mortCommenOwnerKcoll.containsKey("optType")&&mortCommenOwnerKcoll.getDataValue("optType")!=null&&"add".equals(mortCommenOwnerKcoll.getDataValue("optType"))){
								mortCommenOwnerKcoll.setName("MortCommenOwner");
								mortCommenOwnerKcoll.setDataValue("guaranty_no",guarantyNo);
								dao.insert(mortCommenOwnerKcoll, connection);
							}
						}
					}
					//获取权证基本信息为数据库操作做数据收集
					mortGuarantyCertiInfoKcoll = (KeyedCollection)context.getDataElement("MortGuarantyBaseInfo.MortGuarantyCertiInfo");
					mortGuarantyCertiInfoKcoll.setName("MortGuarantyCertiInfo");
					
					//新增权证基本信息
					if(!"".equals(mortGuarantyCertiInfoKcoll.getDataValue("warrant_no"))){
						mortGuarantyCertiInfoKcoll.setDataValue("guaranty_no",guarantyNo);
						String hxSerno = CMISSequenceService4JXXD.querySequenceFromDB("HXSERNO", "fromDate", connection, context);
						hxSerno = guarantyNo + hxSerno;
						mortGuarantyCertiInfoKcoll.put("hx_serno", hxSerno);
						dao.insert(mortGuarantyCertiInfoKcoll, connection);
					}
					//新增押品基本信息
					mortGuarantyBaseInfoKcoll.setDataValue("guaranty_no", guarantyNo);
					int cout = dao.insert(mortGuarantyBaseInfoKcoll, connection);
					context.addDataField("flag", "success");
					context.addDataField("msg","true");
					//跳转至修改页面的关联字段
					context.addDataField("guaranty_no",guarantyNo);
				}else{
					context.addDataField("msg","fail");
					context.addDataField("flag", "fail");
					context.addDataField("guaranty_no","");
				}
			}else{
				//获取权证基本信息为数据库操作做数据收集
				mortGuarantyCertiInfoKcoll = (KeyedCollection)context.getDataElement("MortGuarantyBaseInfo.MortGuarantyCertiInfo");
				mortGuarantyCertiInfoKcoll.setDataValue("guaranty_no", guarantyNo);
				mortGuarantyCertiInfoKcoll.setName("MortGuarantyCertiInfo");
				//新增押品基本信息
				mortGuarantyBaseInfoKcoll.setDataValue("guaranty_no", guarantyNo);
				int cout = dao.insert(mortGuarantyBaseInfoKcoll, connection);
				//新增权证基本信息
				if(!"".equals(mortGuarantyCertiInfoKcoll.getDataValue("warrant_no"))){
					String hxSerno = CMISSequenceService4JXXD.querySequenceFromDB("HXSERNO", "fromDate", connection, context);
					hxSerno = guarantyNo + hxSerno;
					mortGuarantyCertiInfoKcoll.put("hx_serno", hxSerno);
					dao.insert(mortGuarantyCertiInfoKcoll, connection);
				}
				context.addDataField("flag", "success");
				context.addDataField("msg","true");
				//跳转至修改页面的关联字段
				context.addDataField("guaranty_no",guarantyNo);
			}
			
			context.addDataField("agr_type",iqpCargoOverseeReKcoll.getDataValue("agr_type"));//协议类型
			context.addDataField("agr_no",iqpCargoOverseeReKcoll.getDataValue("agr_no"));//协议编号
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
