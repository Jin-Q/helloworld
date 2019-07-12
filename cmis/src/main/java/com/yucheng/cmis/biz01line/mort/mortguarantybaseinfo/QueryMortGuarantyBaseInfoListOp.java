package com.yucheng.cmis.biz01line.mort.mortguarantybaseinfo;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryMortGuarantyBaseInfoListOp extends CMISOperation {


	private final String modelId = "MortGuarantyBaseInfo";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		IndexedCollection iColl = null;
		try{
			connection = this.getConnection(context);
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			List<String> list = new ArrayList<String>();
			list.add("guaranty_no");
			list.add("guaranty_name");
			list.add("cus_id");
			list.add("guaranty_cls");
			list.add("guaranty_type");
			list.add("guaranty_info_status");
			list.add("manager_id");
			list.add("manager_br_id");
			list.add("input_date");
			list.add("image_guaranty_no");
			KeyedCollection kColl = (KeyedCollection)(context.getParent().getDataElement());
			String menuId = (String)kColl.getDataValue("menuId");
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			if(menuId.equals("hwdj")){
				//供应链部分用到的押品
				//RecordRestrict recordRestrict = this.getRecordRestrict(context);
				//conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
				if(conditionStr.equals("")){
					conditionStr += "where guaranty_no in (select guaranty_no from iqp_cargo_oversee_re) order by guaranty_no desc";
				}else{
					conditionStr += "and guaranty_no in (select guaranty_no from iqp_cargo_oversee_re) order by guaranty_no desc";
				}
				conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
				iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			}else if(menuId.equals("hwgl")){
				//conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
				if(conditionStr.equals("")){
					conditionStr += "where guaranty_no in (select guaranty_no from iqp_cargo_oversee_re) and guaranty_info_status not in('1','4') order by guaranty_no desc";
				}else{
					conditionStr += "and guaranty_no in (select guaranty_no from iqp_cargo_oversee_re) and guaranty_info_status not in('1','4') order by guaranty_no desc";
				}
				conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
				iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			}else if(context.containsKey("Intro") && !"".equals(context.getDataValue("Intro"))){//引入担保品按钮时所需参数
				if("intro".equals(context.getDataValue("Intro"))){
					if(conditionStr.equals("")){
						conditionStr += "where (guaranty_info_status = '2' or guaranty_info_status = '1' or guaranty_info_status = '3')";
					}else{
						conditionStr += "and (guaranty_info_status = '2' or guaranty_info_status = '1' or guaranty_info_status = '3')";
					}
					iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
				}
			}else if(menuId.equals("stay_storage")){//待入库菜单数据
				if(conditionStr.equals("")){
					conditionStr += "where (guaranty_info_status = '2' or guaranty_info_status = '3')";
				}else{
					conditionStr += "and (guaranty_info_status = '2' or guaranty_info_status = '3')";
				}
				conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
				iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			}else if(context.containsKey("arpCD")){//以物抵债时押品引入数据
				String serno = (String) context.getDataValue("serno");
				String rel = (String) context.getDataValue("rel");
				if("1".equals(rel)){
					if(conditionStr.equals("")){
						conditionStr += "where guaranty_no in (select guaranty_id from grt_guaranty_re where guar_cont_no in ("
							+"select a.guar_cont_no from grt_guar_cont a where a.guar_model = '00' and a.guar_way in ('00', '01') and a.guar_cont_no in"
							+"(select b.guar_cont_no from grt_loan_r_gur b where b.cont_no in ("
							+"select c.cont_no from arp_busi_debt_info c where c.serno='"+serno+"'))))";
					}else{
						conditionStr += "and guaranty_no in (select guaranty_id from grt_guaranty_re where guar_cont_no in ("
							+"select a.guar_cont_no from grt_guar_cont a where a.guar_model = '00' and a.guar_way in ('00', '01') and a.guar_cont_no in"
							+"(select b.guar_cont_no from grt_loan_r_gur b where b.cont_no in ("
							+"select c.cont_no from arp_busi_debt_info c where c.serno='"+serno+"'))))";
					}
				}else if("2".equals(rel)){
					if(conditionStr.equals("")){
						conditionStr += "where guaranty_no not in (select guaranty_id from grt_guaranty_re union select guaranty_no from arp_coll_debt_re)";
					}else{
						conditionStr += "and guaranty_no not in (select guaranty_id from grt_guaranty_re union select guaranty_no from arp_coll_debt_re)";
					}
				}
				
				iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			}else if(menuId.equals("storage")){
				if(conditionStr.equals("")){
					conditionStr += "where guaranty_info_status = '3'";
				}else{
					conditionStr += "and guaranty_info_status = '3'";
				}
				conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
				iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			}else{
				//所有状态的押品
				if(conditionStr.indexOf("order by") < 0){	//没有排序的加上guaranty_no降序
					conditionStr = conditionStr+"order by guaranty_no desc";
				}
				conditionStr = recordRestrict.judgeQueryRestrict(this.modelId,conditionStr , context, connection);
				iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			}
			//押品登记完成状态的押品
			
			//担保品引入标志。
			
			//用来区分是一般情况下的押品新增还是供应链时的押品新增
			context.addDataField("menuId",menuId);
			Map<String,String> map = new HashMap<String,String>();
			map.put("guaranty_type","MORT_TYPE");
			//树形菜单服务
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			if("hwdj".equals(menuId)||"hwgl".equals(menuId)){
				SInfoUtils.addPrdPopName4Mort("IqpMortCatalogMana",iColl, "guaranty_type", "catalog_no", "catalog_name", "->", connection, dao);  //翻译目录路径
			}else{
				SInfoUtils.addPopName(iColl, map, service);
			}
	    	iColl.setName(iColl.getName()+"List");
			//客户名称翻译
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl,args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			SInfoUtils.addSOrgName(iColl, new String[] { "manager_br_id","input_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "manager_id","input_id"});
			
			/*** 2.审批意见有加密，要单独处理***/
			for(int i = 0 ; i < iColl.size() ; i++){
				KeyedCollection kColl_wf = (KeyedCollection) iColl.get(i);
				/*** 审批意见截取 ***/
				String commentcontent_show = kColl_wf.getDataValue("guaranty_type_displayname").toString();
				commentcontent_show = commentcontent_show.length()>=10?commentcontent_show.substring(0, 10):commentcontent_show;
				kColl_wf.addDataField("guaranty_type_show", commentcontent_show);
			}
			
			this.putDataElement2Context(iColl, context);
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
