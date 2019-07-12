package com.yucheng.cmis.biz01line.dth.op.dthApproveQuery;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.biz01line.dth.op.pubAction.DthPubAction;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class GetDthApproveAnalyPage extends CMISOperation {
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String retValue = "";
		try{
			connection = this.getConnection(context);
            String analy_type = context.getDataValue("analy_type").toString();
            String begin_date = context.getDataValue("begin_date").toString();
            String end_date = context.getDataValue("end_date").toString();
            String right_type = context.getDataValue("right_type").toString();//XD150629048  分支机构报备
			DthPubAction cmisOp = new DthPubAction();

			/*** 1.调用命名sql先取出客户表与授信表中的数据 ***/
			String sql  = "";//XD150629048  分支机构报备 2015-08-27 Edited by FCL
//			if("01".equals(right_type)){
				sql=getSql(analy_type);
//			}else{
//				sql = getSql4Branch(analy_type);//裕民银行无支行
//			}
			String condition = " ";
			if(!begin_date.equals("")){
				condition = condition + " and commenttime >= '"+begin_date+"' ";
			}
			if(!end_date.equals("")){
				condition = condition + " and commenttime <= '"+end_date+"' ";
			}
			
			sql = sql+condition;
			PageInfo pageInfo = null;			
			if(!context.containsKey("excel_type")){
				pageInfo= TableModelUtil.createPageInfo(context, "one", "10");
			}			
			IndexedCollection iColl_wf = TableModelUtil.buildPageData(pageInfo, this.getDataSource(context), sql);			

			/*** 2.审批意见有加密，要单独处理***/
			for(int i = 0 ; i < iColl_wf.size() ; i++){
				KeyedCollection kColl_wf = (KeyedCollection) iColl_wf.get(i);
				
				/*** 3.授信下业务品种与担保类型特殊翻译处理 ***/
				if(analy_type.equals("002")){
					if((kColl_wf.getDataValue("prd_id").toString()).length()>2){	//处理产品编号时要手工去重复
						String[] prd_ids = (kColl_wf.getDataValue("prd_id").toString()).split(",");
						prd_ids = cmisOp.removeRepeat(prd_ids);
						String prd_id = "";
						for(int m = 0 ; m < prd_ids.length ; m++){
							prd_id = prd_id + prd_ids[m]+",";
						}
						kColl_wf.setDataValue("prd_id", prd_id.substring(0, prd_id.length()-1));
						SInfoUtils.getPrdPopName(kColl_wf, "prd_id", connection);  	//翻译产品
						String prd_id_show = kColl_wf.getDataValue("prd_id_displayname").toString();
						prd_id_show = prd_id_show.length()>=10?prd_id_show.substring(0, 10):prd_id_show;
						kColl_wf.addDataField("prd_id_show", prd_id_show);
					}
					if((kColl_wf.getDataValue("guar_type").toString()).length()>2){
						cmisOp.getDicsName(kColl_wf, "guar_type","STD_ZB_ASSURE_MEANS", connection);  	//翻译担保方式
					}else{
						if(!kColl_wf.containsKey("prd_id_displayname")){
							kColl_wf.addDataField("prd_id_displayname", "");
						}
						if(!kColl_wf.containsKey("guar_type_displayname")){
							kColl_wf.addDataField("guar_type_displayname", "");
						}						
					}
				}
				
				/*** 审批意见截取 ***/
				String commentcontent_show = (String)kColl_wf.getDataValue("commentcontent");
				if(!"".equals(commentcontent_show) && !"null".equals(commentcontent_show)){
					commentcontent_show = commentcontent_show.length()>=10?commentcontent_show.substring(0, 10):commentcontent_show;
				}
				kColl_wf.addDataField("commentcontent_show", commentcontent_show);
			}
			
			if(context.containsKey("excel_type")){
				if(analy_type.equals("002")){
					retValue = "lmt_excel";	//生成授信excel
				}else{
					retValue = "ccr_excel";	//生成评级excel
				}
			}else{
				retValue = "not_excel";	//非生成excel
				TableModelUtil.parsePageInfo(context, pageInfo);
			}

			SInfoUtils.addSOrgName(iColl_wf, new String[]{"orgid"});
			this.putDataElement2Context(iColl_wf, context);

		}catch (EMPException ee) {
			ee.printStackTrace();
			throw ee;
		} catch(Exception e){
			e.printStackTrace();
			throw new EMPException(e);
		} finally {
			if (connection != null)this.releaseConnection(context, connection);
		}
		return retValue;
	}
	/**
	 * XD150820062		审批官报备新增审批时效
	 * 2015-08-26 Edited by FCL
	 * @param type
	 * @return
	 */
	private String getSql(String type) {
		String sql  = "";
		if(type.equals("001")){		//001评级
			sql = "select row_number()over(PARTITION BY c.instanceid ORDER BY c.commenttime )as num, "
				+ "  substr(commenttime,0,10) as commenttime,c.userid,c.username,c.commentid,r.approve_status,r.cus_id," 
				+ " (select model_name from Ind_Model where model_no = d.model_no ) as flag, "
				+ " case when (d.last_adjusted_grade is null) then '101' else '102' end as app_type, "
				+ " d.auto_grade,d.adjusted_grade,d.auto_score,cus.cus_name,cus.belg_line,cus.com_cll_type ,w.instanceid,w.bizseqno,w.orgid, "
				+ " (select cnname from s_treedic where opttype = 'STD_GB_4754-2011' and enname = substr(cus.com_cll_type,2, 2))com_cll_big_type,  " //modified by yangzy 2014/12/11 应授信管理部要求行业大类取第二级
				+ " (select cnname from s_treedic where opttype = 'STD_GB_4754-2011' and enname = substr(cus.com_cll_type,0,1))com_cll_door_type," 
				//+ "  utl_raw.cast_to_varchar2(utl_encode.base64_decode(utl_raw.cast_to_raw(c.commentcontent))) as commentcontent  "
				+ " c.commentcontent commentcontent,(round((to_date(c.commenttime,'yyyy-MM-dd hh24:mi:ss')-to_date(c.endtime,'yyyy-MM-dd hh24:mi:ss')))-(select count(*) from WF_FREEDATE where curdate>=REPLACE(substr(c.endtime,0,10),'-','') and curdate<=REPLACE(substr(c.commenttime,0,10),'-','') and WORKDAYFLG='N')) as spsj "   //流程审批意见不加密  2014-09-16 傅承良
				+ " from Ccr_App_Info r ,  wf_main_recordend w ,ccr_app_detail d ,(select b.cus_id, b.cus_name,b.belg_line,   "
				+ " (select com_cll_type from cus_com where cus_id = b.cus_id) as com_cll_type from cus_base b ) cus ,  "
				+ " (select commentid,instanceid,userid,username,commenttime,LAG(COMMENTTIME, 1, COMMENTTIME) OVER(PARTITION BY INSTANCEID ORDER BY COMMENTTIME) as endtime,commentcontent from wf_commentend ) c  "
				+ " where r.cus_id = cus.cus_id  and r.serno = w.bizseqno and r.approve_status in ('997','998')   "
				+ " and r.serno = d.serno and c.instanceid = w.instanceid  "
				+ " and wfsign in ('ccr_app_info','ccr_app_info_f','Lmt_Sig_Lmt','CcrAppFinGuar','CcrAppFinGuar_F','ccr_rat_direct','ccr_rat_direct_f')  "
				+ " and userid in (select actorno from s_dutyuser where dutyno in ('D0003','D0001','D0002','D0021','D0020','S0002','D0017')) ";
		}else{	//授信
			sql = "select row_number()over(PARTITION BY c.instanceid ORDER BY c.commenttime )as num, "
				+ " substr(commenttime,0,10) as commenttime,c.userid,c.username,c.commentid,l.crd_totl_amt as port_amt,(round((to_date(c.commenttime,'yyyy-MM-dd hh24:mi:ss')-to_date(c.endtime,'yyyy-MM-dd hh24:mi:ss')))-(select count(*) from WF_FREEDATE where curdate>=REPLACE(substr(c.endtime,0,10),'-','') and curdate<=REPLACE(substr(c.commenttime,0,10),'-','') and WORKDAYFLG='N')) as spsj, "
				+ " l.serno,l.app_type,l.cus_id,cus.cus_name,l.crd_totl_amt,nvl(l.org_crd_totl_amt,0)org_crd_totl_amt, "
				+ " (crd_totl_amt-nvl(org_crd_totl_amt,0))change_amt,l.approve_status, "
				+ " (select nvl(to_char(wmsys.wm_concat(prd_id)),' ')  from lmt_app_details where serno = l.serno) as prd_id , "
				+ " (select nvl(to_char(wmsys.wm_concat(distinct(guar_type))),' ') from lmt_app_details where serno = l.serno) as guar_type , "
				+ " cus.belg_line,cus.com_cll_type ,w.instanceid,w.bizseqno,w.wfsign,w.orgid,  "
				+ " (select cnname from s_treedic where opttype = 'STD_GB_4754-2011' and enname = substr(cus.com_cll_type,2, 2))com_cll_big_type, " //modified by yangzy 2014/12/11 应授信管理部要求行业大类取第二级
				+ " (select cnname from s_treedic where opttype = 'STD_GB_4754-2011' and enname = substr(cus.com_cll_type,0,1))com_cll_door_type, "
				//+ "  utl_raw.cast_to_varchar2(utl_encode.base64_decode(utl_raw.cast_to_raw(c.commentcontent))) as commentcontent,  "
				+ " c.commentcontent commentcontent,  "   //流程审批意见不加密  2014-08-08  唐顺岩
				+ " case when (l.app_type = '01') then '201' when (l.app_type = '02' and crd_totl_amt-nvl(org_crd_totl_amt,0) > 0) then '202' "
				+ " when (l.app_type = '02' and crd_totl_amt-nvl(org_crd_totl_amt,0) < 0) then '204' "
				+ " when (l.app_type = '02' and crd_totl_amt-nvl(org_crd_totl_amt,0) = 0) then '203' else '' end as report_type "
				+ " from wf_main_recordend w ,(select b.cus_id, b.cus_name,b.belg_line, "
				+ " (select com_cll_type from cus_com where cus_id = b.cus_id) as com_cll_type from cus_base b ) cus, "
				+ " (select serno,app_type,cus_id,crd_totl_amt,org_crd_totl_amt,approve_status from lmt_apply union "
				+ " select serno,app_type,cus_id,crd_totl_amt,org_crd_totl_amt,approve_status from lmt_app_indiv ) l,"
				+ " (select commentid,instanceid,userid,username,commenttime,commentcontent,LAG(COMMENTTIME, 1, COMMENTTIME) OVER(PARTITION BY INSTANCEID ORDER BY COMMENTTIME) as endtime from wf_commentend ) c "
				+ " where l.cus_id = cus.cus_id and wfsign in ('Lmt_Apply','Lmt_Apply_Other') and l.serno = w.bizseqno "
				+ " and l.approve_status in ('997','998') and c.instanceid = w.instanceid "
				+ " and userid in (select actorno from s_dutyuser where dutyno in ('D0003','D0001','D0002','D0021','D0020','S0002','D0017')) ";
		}
		return sql;
	}

	/**
	 * XD150629048  分支机构报备
	 * 2015-08-26 Edited by FCL
	 * @param type
	 * @return
	 */
	private String getSql4Branch(String type){
		String sql  = "";
		if(type.equals("001")){		//001评级
			sql = "select row_number()over(PARTITION BY c.instanceid ORDER BY c.commenttime )as num, "
				+ "  substr(commenttime,0,10) as commenttime,c.userid,c.username,c.commentid,r.approve_status,r.cus_id," 
				+ " (select model_name from Ind_Model where model_no = d.model_no ) as flag, "
				+ " case when (d.last_adjusted_grade is null) then '101' else '102' end as app_type, "
				+ " d.auto_grade,d.adjusted_grade,d.auto_score,cus.cus_name,cus.belg_line,cus.com_cll_type ,w.instanceid,w.bizseqno,w.orgid, "
				+ " (select cnname from s_treedic where opttype = 'STD_GB_4754-2011' and enname = substr(cus.com_cll_type,2, 2))com_cll_big_type,  " //modified by yangzy 2014/12/11 应授信管理部要求行业大类取第二级
				+ " (select cnname from s_treedic where opttype = 'STD_GB_4754-2011' and enname = substr(cus.com_cll_type,0,1))com_cll_door_type," 
				//+ "  utl_raw.cast_to_varchar2(utl_encode.base64_decode(utl_raw.cast_to_raw(c.commentcontent))) as commentcontent  "
				+ " c.commentcontent commentcontent,(round((to_date(c.commenttime,'yyyy-MM-dd hh24:mi:ss')-to_date(c.endtime,'yyyy-MM-dd hh24:mi:ss')))-(select count(*) from WF_FREEDATE where curdate>=REPLACE(substr(c.endtime,0,10),'-','') and curdate<=REPLACE(substr(c.commenttime,0,10),'-','') and WORKDAYFLG='N')) as spsj "   //流程审批意见不加密  2014-09-16 傅承良
				+ " from Ccr_App_Info r ,  wf_main_recordend w ,ccr_app_detail d ,(select b.cus_id, b.cus_name,b.belg_line,   "
				+ " (select com_cll_type from cus_com where cus_id = b.cus_id) as com_cll_type from cus_base b ) cus ,  "
				+ " (select commentid,instanceid,userid,username,commenttime,LAG(COMMENTTIME, 1, COMMENTTIME) OVER(PARTITION BY INSTANCEID ORDER BY COMMENTTIME) as endtime,commentcontent from wf_commentend ) c  "
				+ " where r.cus_id = cus.cus_id  and r.serno = w.bizseqno and r.approve_status in ('997','998')   "
				+ " and r.serno = d.serno and c.instanceid = w.instanceid  "
				+ " and wfsign in ('ccr_app_info','ccr_app_info_f','Lmt_Sig_Lmt','CcrAppFinGuar','CcrAppFinGuar_F','ccr_rat_direct','ccr_rat_direct_f')  "
				+ " and userid in (select actorno from s_dutyuser where dutyno in ('S0200', 'S0202', 'S0203', 'S0226')) "
				+ " AND C.INSTANCEID IN (SELECT INSTANCEID FROM (SELECT A.COMMENTID,A.INSTANCEID,A.NODEID,A.NODENAME,A.COMMENTTIME,A.USERID,"
				+ " ROW_NUMBER() OVER(PARTITION BY A.INSTANCEID ORDER BY A.COMMENTTIME DESC) AS MM FROM WF_COMMENTEND A WHERE 1 = 1) D "
				+ " WHERE D.MM = 1 AND D.USERID IN (SELECT ACTORNO FROM S_DUTYUSER WHERE DUTYNO IN ('S0200', 'S0202', 'S0203', 'S0226')))";
		}else{	//授信
			sql = "select row_number()over(PARTITION BY c.instanceid ORDER BY c.commenttime )as num, "
				+ " substr(commenttime,0,10) as commenttime,c.userid,c.username,c.commentid,l.crd_totl_amt as port_amt,(round((to_date(c.commenttime,'yyyy-MM-dd hh24:mi:ss')-to_date(c.endtime,'yyyy-MM-dd hh24:mi:ss')))-(select count(*) from WF_FREEDATE where curdate>=REPLACE(substr(c.endtime,0,10),'-','') and curdate<=REPLACE(substr(c.commenttime,0,10),'-','') and WORKDAYFLG='N')) as spsj, "
				+ " l.serno,l.app_type,l.cus_id,cus.cus_name,l.crd_totl_amt,nvl(l.org_crd_totl_amt,0)org_crd_totl_amt, "
				+ " (crd_totl_amt-nvl(org_crd_totl_amt,0))change_amt,l.approve_status, "
				+ " (select nvl(to_char(wmsys.wm_concat(prd_id)),' ')  from lmt_app_details where serno = l.serno) as prd_id , "
				+ " (select nvl(to_char(wmsys.wm_concat(distinct(guar_type))),' ') from lmt_app_details where serno = l.serno) as guar_type , "
				+ " cus.belg_line,cus.com_cll_type ,w.instanceid,w.bizseqno,w.wfsign,w.orgid,  "
				+ " (select cnname from s_treedic where opttype = 'STD_GB_4754-2011' and enname = substr(cus.com_cll_type,2, 2))com_cll_big_type, " //modified by yangzy 2014/12/11 应授信管理部要求行业大类取第二级
				+ " (select cnname from s_treedic where opttype = 'STD_GB_4754-2011' and enname = substr(cus.com_cll_type,0,1))com_cll_door_type, "
				//+ "  utl_raw.cast_to_varchar2(utl_encode.base64_decode(utl_raw.cast_to_raw(c.commentcontent))) as commentcontent,  "
				+ " c.commentcontent commentcontent,  "   //流程审批意见不加密  2014-08-08  唐顺岩
				+ " case when (l.app_type = '01') then '201' when (l.app_type = '02' and crd_totl_amt-nvl(org_crd_totl_amt,0) > 0) then '202' "
				+ " when (l.app_type = '02' and crd_totl_amt-nvl(org_crd_totl_amt,0) < 0) then '204' "
				+ " when (l.app_type = '02' and crd_totl_amt-nvl(org_crd_totl_amt,0) = 0) then '203' else '' end as report_type "
				+ " from wf_main_recordend w ,(select b.cus_id, b.cus_name,b.belg_line, "
				+ " (select com_cll_type from cus_com where cus_id = b.cus_id) as com_cll_type from cus_base b ) cus, "
				+ " (select serno,app_type,cus_id,crd_totl_amt,org_crd_totl_amt,approve_status from lmt_apply union "
				+ " select serno,app_type,cus_id,crd_totl_amt,org_crd_totl_amt,approve_status from lmt_app_indiv ) l,"
				+ " (select commentid,instanceid,userid,username,commenttime,commentcontent,LAG(COMMENTTIME, 1, COMMENTTIME) OVER(PARTITION BY INSTANCEID ORDER BY COMMENTTIME) as endtime from wf_commentend ) c "
				+ " where l.cus_id = cus.cus_id and wfsign in ('Lmt_Apply','Lmt_Apply_Other') and l.serno = w.bizseqno "
				+ " and l.approve_status in ('997','998') and c.instanceid = w.instanceid "
				+ " and userid in (select actorno from s_dutyuser where dutyno in ('S0200', 'S0202', 'S0203', 'S0226')) "
				+ " AND C.INSTANCEID IN (SELECT INSTANCEID FROM (SELECT A.COMMENTID,A.INSTANCEID,A.NODEID,A.NODENAME,A.COMMENTTIME,A.USERID,"
				+ " ROW_NUMBER() OVER(PARTITION BY A.INSTANCEID ORDER BY A.COMMENTTIME DESC) AS MM FROM WF_COMMENTEND A WHERE 1 = 1) D "
				+ " WHERE D.MM = 1 AND D.USERID IN (SELECT ACTORNO FROM S_DUTYUSER WHERE DUTYNO IN ('S0200', 'S0202', 'S0203', 'S0226')))";
		}
		return sql;
	}
}