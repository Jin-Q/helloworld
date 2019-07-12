package com.yucheng.cmis.biz01line.lmt.op.bizarea.lmtappjoinback;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryLmtAppJoinBackDetailOp  extends CMISOperation {
	
	private final String modelId = "LmtAppJoinBack";
	private final String modelIdAgr = "LmtAgrBizArea";
	private final String modelIdComn = "LmtAgrBizAreaComn";  //一般
	private final String modelIdCore = "LmtAgrBizAreaCore";  //核心企业供应商类
	private final String modelIdSupmk = "LmtAgrBizAreaSupmk";  //超市百货供应商类

	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String serno = null;
			String agr_no = null;
			try {
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {}
			if(serno == null || serno.length() == 0)
				throw new EMPJDBCException("The value of pk[serno] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno, connection);
			this.putDataElement2Context(kColl, context);
			
			agr_no = (String)kColl.getDataValue("agr_no");//协议编号
			if(agr_no == null || agr_no.length() == 0)
				throw new EMPJDBCException("The value of pk[agr_no] cannot be null!");
			
			String biz_area_type = "";//商圈类型
			KeyedCollection kCollAgr = dao.queryDetail(modelIdAgr, agr_no, connection);
			if(kCollAgr.containsKey("biz_area_type")){
				biz_area_type = (String)kCollAgr.getDataValue("biz_area_type");//商圈类型
			}
			if(biz_area_type==null||"".equals(biz_area_type)){
				throw new Exception("商圈类型为空！");
			}
			
			//客户状态为1：有效        2：无效
			String condSql = " WHERE AGR_NO = '" + agr_no + "' AND CUS_STATUS='1'";
			IndexedCollection icTotCus = dao.queryList("LmtNameList", condSql, connection);
			//总户数
			kCollAgr.addDataField("totl_cus", icTotCus.size());
			this.putDataElement2Context(kCollAgr, context);
			
			IndexedCollection iCollSupmk = null;
			KeyedCollection kCollComn = new KeyedCollection(modelIdComn);
			KeyedCollection kCollCore = new KeyedCollection(modelIdCore);
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
			
			//翻译
			if(kCollAgr.getDataValue("belg_org") == null){
				SInfoUtils.addSOrgName(kCollAgr, new String[] { "input_br_id","manager_br_id"});
			}else{
				SystemTransUtils.containCommaORG2CN("bln_org",kCollAgr,context);
			}
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id","manager_br_id"});
			SInfoUtils.addUSerName(kColl, new String[] { "input_id","manager_id" });
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
