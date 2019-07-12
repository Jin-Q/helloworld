package com.yucheng.cmis.biz01line.cus.op.cusblklist;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryCusBlkListDetailOp  extends CMISOperation {
	
	private final String modelId = "CusBlkList";
	private final String modelIdTemp = "CusBlkListTemp";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String serno = null;
		String cus_id =null;
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
				if(context.containsKey("serno")){
					serno = (String)context.getDataValue("serno");
				}else if(context.containsKey("cus_id")){
					cus_id = (String)context.getDataValue("cus_id");
				}
				

			TableModelDAO dao = this.getTableModelDAO(context);
			if(context.containsKey("serno")){
				kColl = dao.queryDetail(modelIdTemp, serno, connection);
				kColl.setName("CusBlkListTemp");
			}else if(context.containsKey("cus_id")){
				kColl = dao.queryDetail(modelId, cus_id, connection);
				kColl.setName("CusBlkList");
			}
			SInfoUtils.addUSerName(kColl, new String[] { "manager_id" ,"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[] { "manager_br_id" ,"input_br_id"});
			Map<String,String> map = new HashMap<String, String>();
			map.put("legal_addr", "STD_GB_AREA_ALL");
			//树形菜单服务
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(kColl, map, service);
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
