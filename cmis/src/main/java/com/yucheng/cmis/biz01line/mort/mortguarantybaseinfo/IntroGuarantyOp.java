package com.yucheng.cmis.biz01line.mort.mortguarantybaseinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class IntroGuarantyOp  extends CMISOperation {
	
	private final String modelId = "GrtGuarantyRe";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String guaranty_id = (String) context.getDataValue("guaranty_no");
		String guar_cont_no = (String) context.getDataValue("guar_cont_no");
		try{
			connection = this.getConnection(context);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			String conditionStr = "";
			/** 押品引入时不能存在   00-登记 01-有效   状态的担保合同   */
			//conditionStr += "where guaranty_id='"+guaranty_id+"'";
			//IndexedCollection ic = dao.queryList(modelId, conditionStr, connection);
			
			conditionStr += " WHERE GUAR_CONT_STATE IN('00','01') AND GUAR_CONT_NO IN(SELECT GUAR_CONT_NO FROM GRT_GUARANTY_RE WHERE GUARANTY_ID='"+guaranty_id+"')";
			IndexedCollection ic = dao.queryList("GrtGuarCont", conditionStr, connection);
			if(ic.size()!=0){
				//所选择押品如果已经引用过，则提示用户不能重复引入
				context.addDataField("flag", "fail");
				context.addDataField("msg", "此押品已与状态为[登记]或[有效]担保合同关联，请重新选择！");
				//否则进行引入
			}else{
				conditionStr ="where guaranty_no='"+guaranty_id+"'";
				IndexedCollection iColl = dao.queryList("MortGuarantyEvalValue", conditionStr, connection);
				if(iColl.size()!=0){
					KeyedCollection kColl = new KeyedCollection(modelId);
					kColl.addDataField("guaranty_id", guaranty_id);
					kColl.addDataField("guar_cont_no", guar_cont_no);
				    dao.insert(kColl, connection);
					context.addDataField("flag", "success");
					context.addDataField("msg", "已成功录入！");
				}else{
					context.addDataField("flag", "fail");
					context.addDataField("msg", "此押品没有录入评估价值信息，请录入后再进行引入操作！");
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
