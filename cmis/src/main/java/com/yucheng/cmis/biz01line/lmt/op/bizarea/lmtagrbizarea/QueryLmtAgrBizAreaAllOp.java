package com.yucheng.cmis.biz01line.lmt.op.bizarea.lmtagrbizarea;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryLmtAgrBizAreaAllOp  extends CMISOperation {
	
	private final String modelId = "LmtAgrBizArea";
	private final String modelIdComn = "LmtAgrBizAreaComn";  //一般
	private final String modelIdCore = "LmtAgrBizAreaCore";  //核心企业供应商类
	private final String modelIdSupmk = "LmtAgrBizAreaSupmk";  //超市百货供应商类

	private final String app_id_name = "agr_no";
	
	private boolean updateCheck = false;
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String biz_area_type = null;
		try{
			connection = this.getConnection(context);
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String agr_no = null;
			try {
				agr_no = (String)context.getDataValue(app_id_name);
			} catch (Exception e) {}
			if(agr_no == null || agr_no.length() == 0)
				throw new EMPJDBCException("The value of pk["+app_id_name+"] cannot be null!");

			IndexedCollection iCollSupmk = null;
			KeyedCollection kCollComn = new KeyedCollection(modelIdComn);
			KeyedCollection kCollCore = new KeyedCollection(modelIdCore);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, agr_no, connection);
			if(kColl.containsKey("biz_area_type")){
				biz_area_type = (String)kColl.getDataValue("biz_area_type");//商圈类型
			}
			if(biz_area_type==null||"".equals(biz_area_type)){
				throw new Exception("商圈类型为空！");
			}
			
			if("0".equals(biz_area_type)){//一般
				kCollComn = (KeyedCollection)dao.queryDetail(modelIdComn, agr_no, connection);
				this.putDataElement2Context(kCollComn, context);
			}else if("1".equals(biz_area_type)){//核心企业供应商类
				kCollCore = (KeyedCollection)dao.queryDetail(modelIdCore, agr_no, connection);
				this.putDataElement2Context(kCollCore, context);
			}else if("2".equals(biz_area_type)){//超市类
				iCollSupmk = dao.queryList(modelIdSupmk, " where agr_no = '" + agr_no + "'", connection);
				iCollSupmk.setName(modelIdSupmk+"List");
				this.putDataElement2Context(iCollSupmk, context);
			}
			
			//根据商圈编号 agr_no 计算已分配额度 (按照业务来) already_used 与总户数 totl_cus 
			//总户数直接查名单表     
			//客户状态为1：有效        2：无效
			String condSql = " WHERE AGR_NO = '" + agr_no + "' AND CUS_STATUS='1'";
			IndexedCollection icTotCus = dao.queryList("LmtNameList", condSql, connection);
			//总户数
			kColl.addDataField("totl_cus", icTotCus.size());
			
			//剩余额度
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface serviceIqp = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			KeyedCollection kCollTemp = serviceIqp.getAgrUsedInfoByArgNo((String)kColl.getDataValue("agr_no"), "03", connection, context);
			String lmt_amt = kCollTemp.getDataValue("lmt_amt").toString();
			double bal_amt = Double.parseDouble((String)kColl.getDataValue("lmt_totl_amt")) - Double.parseDouble(lmt_amt);
			kColl.addDataField("already_used", bal_amt);
			
			//翻译
			if(null != kColl.getDataValue("belg_org")){
				SystemTransUtils.containCommaORG2CN("belg_org",kColl,context);
			}
			
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id","manager_br_id"});
			SInfoUtils.addUSerName(kColl, new String[] { "input_id","manager_id" });
			this.putDataElement2Context(kColl, context);
			
			//修改页面获取不到biz_area_type问题，start 
			if(context.containsKey("biz_area_type")){
				context.setDataValue("biz_area_type", biz_area_type);
			}else{
				context.addDataField("biz_area_type", biz_area_type);
			}
			//修改页面获取不到biz_area_type问题，end
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
