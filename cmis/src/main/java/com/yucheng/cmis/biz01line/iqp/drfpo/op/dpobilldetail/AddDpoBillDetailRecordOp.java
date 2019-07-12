package com.yucheng.cmis.biz01line.iqp.drfpo.op.dpobilldetail;

import java.net.URLDecoder;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddDpoBillDetailRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpBillDetailInfo";
	private final String modelId1 = "IqpCorreInfo";
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		//定义标志位，用来接收前台传来的标志信息；1--可以进行票据新增，2--不能进行票据新增只能新增关系信息
		String flag = "";
		String drfpo_no="";
		String status="";
		String porder_no="";
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			KeyedCollection CorreKC = new KeyedCollection(modelId1);
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
				
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			TableModelDAO dao = this.getTableModelDAO(context);
			flag = (String) kColl.getDataValue("flag");
			status = (String) kColl.getDataValue("status");
			//获取关联关系表中所需要的字段值
			drfpo_no = (String) context.getDataValue("drfpo_no");
			porder_no = (String) kColl.getDataValue("porder_no");
			//中文转码
			drfpo_no = URLDecoder.decode(drfpo_no,"UTF-8");
			porder_no = URLDecoder.decode(porder_no,"UTF-8");
			CorreKC.addDataField("drfpo_no",drfpo_no);
			CorreKC.addDataField("porder_no",porder_no);
			CorreKC.addDataField("status","00");
			if("1".equals(flag)){//可以进行新增票据信息
				dao.insert(kColl, connection);
				dao.insert(CorreKC, connection);
			}else{//不能新增票据信息，只能新增票据与票据池的关联信息
				if(status.equals("06")){//当前票据为解质押状态时
					kColl.setDataValue("status","01");//重置票据状态为登记状态
					dao.update(kColl, connection);
				}
					dao.insert(CorreKC, connection);
			}
			context.addDataField("flag", "success");
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
