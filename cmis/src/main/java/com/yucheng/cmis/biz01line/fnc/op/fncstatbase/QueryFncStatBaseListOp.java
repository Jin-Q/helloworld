package com.yucheng.cmis.biz01line.fnc.op.fncstatbase;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryFncStatBaseListOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "FncStatBase";
	
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			//获得查询的过滤数据
			KeyedCollection queryData = null;
			String isSmp = null;//财务简表标志
			
			//获取可以分页的OracleDao对象
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			if(context.containsKey("isSmp")){
				isSmp = (String)context.getDataValue("isSmp");
			}
			
			if(queryData!=null){
				String stat_prd = queryData.containsKey("stat_prd")?(String)queryData.getDataValue("stat_prd"):"";
				String stat_prd_year = queryData.containsKey("stat_prd_year")?(String)queryData.getDataValue("stat_prd_year"):"";
				String stat_prd_style = queryData.containsKey("stat_prd_style")?(String)queryData.getDataValue("stat_prd_style"):"";
				
				if(stat_prd!=null && !"".equals(stat_prd) && stat_prd_year!=null && !"".equals(stat_prd_year)){
					if(!"4".equals(stat_prd_style)){
						queryData.put("stat_prd", null);
						queryData.put("stat_prd_year", null);
						TableModelUtil.setCustomizeQueryConditionB(" stat_prd = '"+stat_prd_year+stat_prd+"'",context);
					}
				}else if(stat_prd_year!=null && !"".equals(stat_prd_year) && (stat_prd==null||"".equals(stat_prd))){
					if(!"4".equals(stat_prd_style)){
						queryData.put("stat_prd", null);
						queryData.put("stat_prd_year", null);
						TableModelUtil.setCustomizeQueryConditionB(" stat_prd like '"+stat_prd_year+"__'",context);
					}
				}else if(stat_prd!=null&&!"".equals(stat_prd)&&(stat_prd_year==null||"".equals(stat_prd_year))){
					if(!"4".equals(stat_prd_style)){
						queryData.put("stat_prd", null);
						queryData.put("stat_prd_year", null);
						TableModelUtil.setCustomizeQueryConditionB(" stat_prd like '____"+stat_prd+"'",context);
					}
				}
				
//				if(stat_prd_style.equals("1")){//月报
//					queryData.put("stat_prd", null);
//					TableModelUtil.setCustomizeQueryConditionB(" substr(stat_prd,5,2) = '"+stat_prd.substring(4)+"'",context);
//				}
//				if(stat_prd_style.equals("2")){//季报
//					queryData.put("stat_prd", null);
//					TableModelUtil.setCustomizeQueryConditionB(" substr(stat_prd,5,2) = '"+stat_prd.substring(4)+"'",context);
//				}
//				if(stat_prd_style.equals("3")){//半年报
//					queryData.put("stat_prd", null);
//					TableModelUtil.setCustomizeQueryConditionB(" substr(stat_prd,5,2) = '"+stat_prd.substring(4)+"'",context);			
//				}
//				if(stat_prd_style.equals("4")){//年报
////					TableModelUtil.setCustomizeQueryConditionB(
////							" substr(stat_prd,5,2) = '"+stat_prd.substring(4)+"'",
////							context);
//				}
			}
			
			//获得查询条件，交集、精确查询，忽略空值
//		    cus_id = context.getDataValue("cus_id").toString();
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			if(conditionStr!=null && !"".equals(conditionStr)){
				if(isSmp!=null && !"".equals(isSmp)){
					conditionStr += " and fnc_type='PB0003' order by stat_prd";
				}else {
					conditionStr += " and fnc_type<>'PB0003' order by stat_prd";
				}
			}else{
				if(isSmp!=null && !"".equals(isSmp)){
					conditionStr = " where fnc_type='PB0003' order by stat_prd";
				}else {
					conditionStr += " where fnc_type<>'PB0003' order by stat_prd";
				}
			}
//			conditionStr = StringUtil.transConditionStr(conditionStr, "cus_name");
//			RecordRestrict recordRestrict = this.getRecordRestrict(context);
//			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			
			int size = PUBConstant.MAXLINE;
			//设置只在第一次查询总记录数
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			List<String> list = new ArrayList<String>();
			list.add("cus_id");
			list.add("stat_prd_style");
			list.add("stat_prd");
			list.add("stat_style");
			list.add("stat_bs_style_id");
			list.add("state_flg");
			list.add("stat_pl_style_id");
			list.add("stat_cf_style_id");
			list.add("stat_fi_style_id");
			list.add("stat_soe_style_id");
			list.add("stat_sl_style_id");
			list.add("input_id");
			list.add("input_br_id");
			list.add("input_date");
//			list.add("style_id1");
//			list.add("style_id2");			
//			list.add("stat_acc_style_id");
//			list.add("stat_de_style_id");
			list.add("fnc_type");//财报类型
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			String status="";
//			String fncType = "";//财务报表类型
//			String fncTypeDisname = "";//财务报表类型显示
			for(int i=0;i<iColl.size();i++){
				status=(String)((KeyedCollection)(iColl.get(i))).getDataValue("state_flg");
				if("2".equals(status.substring(8))){
					((KeyedCollection)(iColl.get(i))).addDataField("state_flg_name","完成");
				}else{
					((KeyedCollection)(iColl.get(i))).addDataField("state_flg_name","草稿");
				}
				//获取财务报表类型
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String cusId = (String)kColl.getDataValue("cus_id");
	/*			String statBsStyleId = (String)kColl.getDataValue("stat_bs_style_id");
				String statPlStyleId = (String)kColl.getDataValue("stat_pl_style_id");
				String statCfStyleId = (String)kColl.getDataValue("stat_cf_style_id");
				String statFiStyleId = (String)kColl.getDataValue("stat_fi_style_id");
				
				String condition = " where fnc_bs_style_id = '"+statBsStyleId+"' and fnc_pl_style_id = '"+statPlStyleId+"' and fnc_cf_style_id = '"+statCfStyleId+"' and fnc_fi_style_id = '"+statFiStyleId+"'";
				//转化财务报表类型(根据资产负债表，损益表，现金流量表，财务指标表四个条件查询)
				KeyedCollection kc1 = new KeyedCollection();
				kc1 = dao.queryFirst("FncConfTemplate", null, condition, connection);
				fncType = (String) kc1.getDataValue("fnc_id");
				fncTypeDisname = (String) kc1.getDataValue("fnc_name");
				//显示报表类型和报表类型名称	
				kColl.put("fnc_type",fncType);
				kColl.put("fnc_type_displayname",fncTypeDisname);*/
				//展示客户证件类型、证件号码
				String certType = "";
				String certCode = "";
				KeyedCollection kc2 = new KeyedCollection();
				kc2 = dao.queryDetail("CusBase", cusId, connection);
				certType = (String)kc2.getDataValue("cert_type");
				certCode = (String)kc2.getDataValue("cert_code");
				
				kColl.put("cert_type",certType);
				kColl.put("cert_code",certCode);
			}
			SInfoUtils.addSOrgName(iColl, new String[]{"input_br_id"});
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);

		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null){
				this.releaseConnection(context, connection);
			}
		}
		return "0";
	}

}
