package com.yucheng.cmis.biz01line.lmt.op.bizarea.lmtappbizarea;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class UpdateLmtAppBizAreaRecordOp extends CMISOperation {
	
	private final String modelId = "LmtAppBizArea";
	private final String modelIdComn = "LmtAppBizAreaComn";
	private final String modelIdCore = "LmtAppBizAreaCore";
	private final String modelIdSupmk = "LmtAppBizAreaSupmk";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection kColl = null;
			KeyedCollection kCollComn = null;
			KeyedCollection kCollCore = null;
			IndexedCollection iCollSupmk = null;
			
			if(context.containsKey(modelId)){
				kColl = (KeyedCollection)context.getDataElement(modelId);
			}
			if(kColl==null||kColl.getDataValue("serno")==null||"".equals(kColl.getDataValue("serno"))){
				throw new Exception("商圈准入申请信息为空！");
			}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.update(kColl, connection);
			String serno = (String)kColl.getDataValue("serno");
			String biz_area_type = (String)kColl.getDataValue("biz_area_type");//商圈类型
			if("0".equals(biz_area_type)){
				kCollComn = (KeyedCollection)context.getDataElement(modelIdComn);
				dao.update(kCollComn, connection);
			}else if("1".equals(biz_area_type)){
				kCollCore = (KeyedCollection)context.getDataElement(modelIdCore);
				dao.update(kCollCore, connection);
			}else if("2".equals(biz_area_type)){
				iCollSupmk = (IndexedCollection)context.getDataElement(modelIdSupmk + "List");
				//删除原有信息插入新信息
				LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
				Map<String,String> map = new HashMap<String, String>();
				map.put("serno", serno);
				lmtComponent.deleteByField(modelIdSupmk, map);
				
				for(int i=0; i<iCollSupmk.size(); i++){
					kColl = (KeyedCollection)iCollSupmk.get(i);
					kColl.setId(modelIdSupmk);
					String supkm_serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", null, connection, context);
					kColl.setDataValue("supmk_serno", supkm_serno);
					dao.insert(kColl, connection);
				}
			}
			
			context.addDataField("flag", PUBConstant.SUCCESS);
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
