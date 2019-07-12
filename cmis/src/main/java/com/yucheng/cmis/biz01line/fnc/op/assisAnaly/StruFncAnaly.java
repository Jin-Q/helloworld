package com.yucheng.cmis.biz01line.fnc.op.assisAnaly;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cuscom.component.CusComComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class StruFncAnaly extends CMISOperation {
	private int fnc_length; //报表项数量
	private int records_length; //财报记录数量
	private String sql=""; 

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			delStruAnaly(context);
			context.addDataField("flag", PUBConstant.SUCCESS);
		}catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)this.releaseConnection(context, connection);
		}
		return "0";
	}
	
	/**** 结构分析规则调用处理 by GC 20130829*****/
	public void delStruAnaly(Context context) throws EMPException {
		String fncType = (String)context.getDataValue("fncType");	//财报显示类型
		String[] cus_id = ((String)context.getDataValue("cus_id")).split(",");	//客户号
		String[] stat_style = ((String)context.getDataValue("stat_style")).split(",");	//报表口径，本部/合并报表
		String[] stat_prd = ((String)context.getDataValue("stat_prd")).split(",");	//报表期间，六位年月
		String[] stat_prd_style = ((String)context.getDataValue("stat_prd_style")).split(",");	//报表周期类型，年报月报等
		
		int i = 0,j = 0;
		records_length = cus_id.length;
		DecimalFormat trans_num = new DecimalFormat("0.0000");
		
		/*** 1.验证财报类型一致 ***/
		sql = "select count(distinct(decode('"+fncType+"','bs',a.stat_bs_style_id , a.stat_pl_style_id)))cc "
				+ "from fnc_stat_base a where cus_id in ('"
				+((String)context.getDataValue("cus_id")).replaceAll(",", "','")+"') and stat_style in ('"
				+((String)context.getDataValue("stat_style")).replaceAll(",", "','")+"') and stat_prd in ('"
				+((String)context.getDataValue("stat_prd")).replaceAll(",", "','")+"') and stat_prd_style in ('"
				+((String)context.getDataValue("stat_prd_style")).replaceAll(",", "','")+"')";
		
		CusComComponent cmisComponent = (CusComComponent) CMISComponentFactory
		.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSCOM,context,this.getConnection(context));
		if(!cmisComponent.querySql(sql).equals("1")){
			context.addDataField("flag", PUBConstant.FAIL);
			return ;
		}
		
		/*** 2.取财报项名称与编号,并传向前台页面 ***/
		List<String> list = new ArrayList<String>();
		list.add("item_id");
		list.add("fnc_item_edit_typ");
		sql = " where style_id = (select decode('"+fncType+"','bs',a.stat_bs_style_id , a.stat_pl_style_id)cc "
				+ " from fnc_stat_base a where fnc_type='PB0001' and cus_id = '"+cus_id[0]+"' and stat_style = '"+stat_style[0]+"'" 
				+ " and stat_prd = '"+stat_prd[0]+"' and stat_prd_style = '"+stat_prd_style[0]+"')order by fnc_conf_order";
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		IndexedCollection icoll = dao.queryList("FncConfDefFmt", list, sql, this.getConnection(context));
		String[] args=new String[] { "item_id" };
		String[] modelIds=new String[]{"FncConfItems"};
		String[] modelForeign=new String[]{"item_id"};
		String[] fieldName=new String[]{"item_name"};
		SystemTransUtils.dealName(icoll, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
		this.putDataElement2Context(icoll, context);

		fnc_length = icoll.size();
		String[] item_name = new String[fnc_length];
		String[] item_no = new String[fnc_length];		
		String[] fnc_item_edit_typ = new String[fnc_length];
		for(i=0;i<fnc_length;i++){
			KeyedCollection kColl = (KeyedCollection) icoll.get(i);
			item_name[i] = kColl.getDataValue("item_id_displayname").toString();
			item_no[i] = kColl.getDataValue("item_id").toString();
			fnc_item_edit_typ[i] = kColl.getDataValue("fnc_item_edit_typ").toString();
		}
		
		/*** 3.取财报项的值 ***/
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		ShuffleServiceInterface shuffleService = null;
		try {
			shuffleService = (ShuffleServiceInterface) serviceJndi
					.getModualServiceById("shuffleServices", "shuffle");
		} catch (Exception e) {
			EMPLog.log("shuffle", EMPLog.ERROR, 0,"getModualServiceById error!", e);
			throw new EMPException(e);
		}
		
		/*** 4.处理在表中取到的基本项 ***/
		float[] item_value_end = new float[fnc_length]; //存放取到的期末数
		for(j = 0 ; j < records_length ;j++){
			for(i = 0 ; i < fnc_length ; i++){
				Map<String, String> modelMap=new HashMap<String, String>();
				modelMap.put("IN_客户编号", cus_id[j]);
				modelMap.put("IN_项目编号", item_no[i]);
				modelMap.put("IN_报表口径", stat_style[j]);
				modelMap.put("IN_报表周期类型", stat_prd_style[j]);
				modelMap.put("IN_报表期间", stat_prd[j]);
				Map<String, String> outMap=new HashMap<String, String>();
				try {
					outMap=shuffleService.fireTargetRule("FNCANALYSIS", "FncAnalyseAll", modelMap);
				} catch (Exception e1) {
					throw new ComponentException(CMISMessage.QUERYERROR,"获取模型失败");
				}
				if(fnc_item_edit_typ[i].equals("3")){	//现在负债表标题项不存数据库了，拿出来直接赋0
					item_value_end[i] = 0;
				}else{
					item_value_end[i] =Float.parseFloat(outMap.get("OUT_期末数"));
				}
				
			}
			KeyedCollection kColl_end = new KeyedCollection("StruAnaly"+j) ;
			for(i = 0 ; i< fnc_length ; i++){
				kColl_end.addDataField(item_no[i],trans_num.format(item_value_end[i]));
			}
			this.putDataElement2Context(kColl_end, context);
		}		
		
	}
}