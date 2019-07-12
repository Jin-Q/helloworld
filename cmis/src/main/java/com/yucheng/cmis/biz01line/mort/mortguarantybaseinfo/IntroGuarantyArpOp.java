package com.yucheng.cmis.biz01line.mort.mortguarantybaseinfo;

import java.sql.Connection;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class IntroGuarantyArpOp  extends CMISOperation {
	
	private final String modelId = "ArpCollDebtRe";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String guaranty_no = (String) context.getDataValue("guaranty_no");
		String serno = (String) context.getDataValue("serno");
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			conditionStr += "where serno in (select serno from Arp_Coll_Debt_App where approve_status not in ('990','998')) and guaranty_no='"+guaranty_no+"'";
			IndexedCollection ic = dao.queryList(modelId, conditionStr, connection);
			if(context.containsKey("flag")){
				Map<String,String> map = new HashedMap();
				map.put("guaranty_no", guaranty_no);
				map.put("serno", serno);
				int count = dao.deleteAllByPks(modelId, map, connection);
				if(count==1){
					context.setDataValue("flag", "success");
					context.addDataField("msg", "取消引入成功！");
				}else{
					context.setDataValue("flag", "fail");
					context.addDataField("msg", "取消引入失败！");
				}
			}else{
				if(ic.size()!=0){
					//所选择押品如果已经引用过，则提示用户不能重复引入
					context.addDataField("flag", "fail");
					context.addDataField("msg", "此押品已存在引入，请重新选择！");
					//否则进行引入
				}else{
					conditionStr ="where guaranty_no='"+guaranty_no+"'";
					IndexedCollection iColl = dao.queryList("MortGuarantyEvalValue", conditionStr, connection);
					if(iColl.size()!=0){
						String rel = (String) context.getDataValue("rel");
						KeyedCollection kColl = new KeyedCollection(modelId);
						kColl.addDataField("guaranty_no", guaranty_no);
						kColl.addDataField("serno", serno);
						kColl.addDataField("rel",rel);
						kColl.addDataField("status","00");
					    dao.insert(kColl, connection);
						context.addDataField("flag", "success");
						context.addDataField("msg", "已成功录入！");
					}else{
						context.addDataField("flag", "fail");
						context.addDataField("msg", "此押品没有录入评估价值信息，请录入后再进行引入操作！");
					}
					
				}
			}
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
