package com.yucheng.cmis.biz01line.lmt.op.lmtagrjointcoop;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

/**
 * 冻结联保：
 * @author QZCB
 * 
 */
public class FreezeOrUnfreezeLmtAgrJointCoopOp  extends CMISOperation {
	
	private final String modelIdApp = "LmtAppJointCoop";
	private final String modelId = "LmtAgrJointCoop";
	private final String modelIdAppNl = "LmtAppNameList";
	private final String modelIdNl = "LmtNameList";
	private final String modelIdAppDet = "LmtAppDetails";
	private final String modelIdDet = "LmtAgrDetails";

	private final String agr_no_name = "agr_no";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String agr_no_value = null;
			String app_type = null;
			String flagInfo = null;
			String serno = null;
			try {
				agr_no_value = (String)context.getDataValue(agr_no_name);
			} catch (Exception e) {}
			if(agr_no_value == null || agr_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+agr_no_name+"] cannot be null!");
			
			try {
				app_type = (String)context.getDataValue("app_type");//03冻结 04解冻
			} catch (Exception e) {}
			if(app_type == null || app_type.length() == 0)
				throw new EMPJDBCException("申请类型不能为空!");

			/**检查该笔联保协议是否存在在途的冻结申请*/
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition = " where agr_no='"+agr_no_value+"' and app_type='"+app_type+"' and approve_status not in ('997','998')";//在途的冻结
			IndexedCollection iColl = dao.queryList(modelIdApp, condition, connection);
			if(iColl.size()>0){
				flagInfo = PUBConstant.EXISTS;
			}else{
				/**复制协议信息到申请表*/
				serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);//生成新的流水号
				String conTmp = " where agr_no='"+agr_no_value+"'";//查询协议信息
				IndexedCollection iCollAgr = dao.queryList(modelId, conTmp, connection);
				if(iCollAgr.size()>0){
					KeyedCollection kCollAgr = (KeyedCollection)iCollAgr.get(0);
					kCollAgr.put("serno", serno);
					kCollAgr.put("app_date", context.getDataValue("OPENDAY"));
					kCollAgr.put("app_type", app_type);//申请类型
					kCollAgr.put("input_id", context.getDataValue("currentUserId"));
					kCollAgr.put("input_br_id", context.getDataValue("organNo"));
					kCollAgr.put("input_date", context.getDataValue("OPENDAY"));
					kCollAgr.put("approve_status", "000");//默认待发起
					kCollAgr.remove("agr_status");//删除字段
					
					kCollAgr.setName(modelIdApp);
					dao.insert(kCollAgr, connection);
					
					/**复制协议名单到申请名单*/
					IndexedCollection iCollNl = dao.queryList(modelIdNl, conTmp, connection);
					for(int i=0;i<iCollNl.size();i++){
						KeyedCollection kCollNl = (KeyedCollection)iCollNl.get(i);
						kCollNl.remove("agr_no");
						kCollNl.remove("cus_status");
						kCollNl.put("serno", serno);
						
						kCollNl.setName(modelIdAppNl);
						dao.insert(kCollNl, connection);
					}
					
					/**复制台账信息到申请分项中*/
					IndexedCollection iCollDet = dao.queryList(modelIdDet, conTmp, connection);
					for(int i=0;i<iCollDet.size();i++){
						KeyedCollection kCollDet = (KeyedCollection)iCollDet.get(i);
						String limit_code = CMISSequenceService4JXXD.querySequenceFromED("ED", "all", connection, context);
						kCollDet.put("serno", serno);
						kCollDet.put("org_limit_code", kCollDet.getDataValue("limit_code"));
						kCollDet.put("froze_amt", kCollDet.getDataValue("crd_amt"));
						kCollDet.put("limit_code", limit_code);
						
						kCollDet.setName(modelIdAppDet);
						dao.insert(kCollDet, connection);
					}
				}
				flagInfo = PUBConstant.SUCCESS;
			}
			
			context.addDataField("flag", flagInfo);
			context.addDataField("serno", serno);
		}catch (EMPException ee) {
			context.addDataField("flag", PUBConstant.FAIL);
			throw ee;
		} catch(Exception e){
			context.addDataField("flag", PUBConstant.FAIL);
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
