package com.yucheng.cmis.biz01line.mort.mortplant;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class AddMortPlantLeadOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "MortPlant";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String guaranty_no = "";
		String conditionStr = "";
		try {
			connection = this.getConnection(context);
			guaranty_no=(String) context.getDataValue("guaranty_no");
			TableModelDAO dao = this.getTableModelDAO(context);
			conditionStr = "where guaranty_no ='"+guaranty_no+"'";
			IndexedCollection iColl = dao.queryList(modelId, conditionStr, connection);
			KeyedCollection kColl = new KeyedCollection(modelId);
			if(iColl.size()!=0){
			   kColl = (KeyedCollection) iColl.get(0);
			   Map<String,String> map = new HashMap<String,String>();
				map.put("guaranty_addr", "STD_GB_AREA_ALL");//押品坐落地址
				//树形菜单服务
				CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
				SInfoUtils.addPopName(kColl, map, service);
			}else{
			   kColl.addDataField("guaranty_no", guaranty_no);
			}
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
