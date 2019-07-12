package com.yucheng.cmis.biz01line.mort.cargo.mortbaildeliv;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.mort.component.MortCommenOwnerComponent;
import com.yucheng.cmis.biz01line.mort.morttool.MORTConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class TallyMortBailDelivRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "MortBailDeliv";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
   	Connection connection = null;
		String serno = "";
		String guaranty_no = "";
		try{
			connection = this.getConnection(context);
			KeyedCollection kcB = (KeyedCollection) context.getDataElement(modelId);
			//构建组件类
			MortCommenOwnerComponent mortRe = (MortCommenOwnerComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(
					MORTConstant.MORTCOMMENOWNERCOMPONENT, context, connection);
			TableModelDAO dao = this.getTableModelDAO(context);
			try {
				guaranty_no = (String) kcB.getDataValue("guaranty_no");
				serno = (String) kcB.getDataValue("serno");
			} catch (Exception e) {}
			kcB.put("tally_date",context.getDataValue(PUBConstant.OPENDAY));
			kcB.put("status","01");
			 
			if(serno.equals("")||serno==null){//新增
				serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
				kcB.put("serno",serno);
				if(kcB == null || kcB.size() == 0)
					throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
				dao.insert(kcB, connection);
				mortRe.updateMortDelivListOper(serno,guaranty_no,"2");
			}else{//修改
				if(kcB == null || kcB.size() == 0)
					throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
				dao.update(kcB, connection);
			}
			mortRe.updatemortCargoPledge(serno);//批量更改相应提货流水下的货物在库总价值以及其在库数量
			mortRe.updateMortDelivList("05","03", serno);//批量更改提货清单里的记录
			dao.update(kcB, connection);
			context.addDataField("flag","success");
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
