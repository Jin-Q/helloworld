package com.yucheng.cmis.biz01line.lmt.op.bizarea.lmtappbizarea;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddLmtAppBizAreaRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "LmtAppBizArea";
	private final String modelIdComn = "LmtAppBizAreaComn";
	private final String modelIdCore = "LmtAppBizAreaCore";
	private final String modelIdSupmk = "LmtAppBizAreaSupmk";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			KeyedCollection kCollComn = null;
			KeyedCollection kCollCore = null;
			IndexedCollection iCollSupmk = null;
			String biz_area_type = null;
			if(context.containsKey(modelId)){
				kColl = (KeyedCollection)context.getDataElement(modelId);
			}
			if(kColl==null||kColl.size()==0){
				throw new Exception("商圈申请信息为空！");
			}
			//生成商圈准入申请流水号
			String manager_br_id = (String)kColl.getDataValue("manager_br_id");
			String serno = CMISSequenceService4JXXD.querySequenceFromSQ("SQ", "all", manager_br_id, connection, context);
			kColl.setDataValue("serno", serno);
			biz_area_type = (String) kColl.getDataValue("biz_area_type");//圈商类型
			if("0".equals(biz_area_type)){
				if(context.containsKey(modelIdComn)){
					kCollComn = (KeyedCollection)context.getDataElement(modelIdComn);
				}
				if(kCollComn==null||kCollComn.size()==0){
					throw new Exception("商圈申请中一般圈商类型信息为空！");
				}
				kCollComn.setDataValue("serno", serno);
			}else if("1".equals(biz_area_type)){
				if(context.containsKey(modelIdCore)){
					kCollCore = (KeyedCollection)context.getDataElement(modelIdCore);
				}
				if(kCollCore==null||kCollCore.size()==0){
					throw new Exception("商圈申请中核心企业供销商类型信息为空！");
				}
				kCollCore.setDataValue("serno", serno);
			}else if("2".equals(biz_area_type)){
				if(context.containsKey(modelIdSupmk + "List")){
					iCollSupmk = (IndexedCollection)context.getDataElement(modelIdSupmk + "List");
				}
				if(iCollSupmk==null||iCollSupmk.size()==0){
					throw new Exception("商圈申请中超市百货类信息为空！");
				}
			}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			if("0".equals(biz_area_type)){
				dao.insert(kCollComn, connection);
			}else if("1".equals(biz_area_type)){
				dao.insert(kCollCore, connection);
			}else if("2".equals(biz_area_type)){
				for(int i=0; i<iCollSupmk.size(); i++){
					KeyedCollection kCollSup = (KeyedCollection)iCollSupmk.get(i);
					kCollSup.setId(modelIdSupmk);
					kCollSup.setDataValue("serno", serno);
					String supkm_serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
					kCollSup.setDataValue("supmk_serno", supkm_serno);
					dao.insert(kCollSup, connection);
				}
			}
			context.addDataField("flag", PUBConstant.SUCCESS);
			context.addDataField("serno", serno);
		}catch (EMPException ee) {
			context.addDataField("flag", PUBConstant.FAIL);
			context.addDataField("serno", "");
			throw ee;
		} catch(Exception e){
			context.addDataField("flag", PUBConstant.FAIL);
			context.addDataField("serno", "");
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
		
	}
}
