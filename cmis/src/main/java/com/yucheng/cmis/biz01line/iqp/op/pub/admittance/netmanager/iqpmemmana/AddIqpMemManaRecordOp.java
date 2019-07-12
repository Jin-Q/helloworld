package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpmemmana;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddIqpMemManaRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpMemMana";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			String mem_cus_id = (String)kColl.getDataValue("mem_cus_id");
			String type = (String)kColl.getDataValue("mem_manuf_type");//成员厂商类别
			String net_agr_no=(String)kColl.getDataValue("net_agr_no");//网络协议编号
			String serno=CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);//获取流水号，主键
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition = "where mem_cus_id='"+mem_cus_id+"' and mem_manuf_type='"+type+"' and net_agr_no='"+net_agr_no+"'";
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			String paramStr = "mem_cus_id="+mem_cus_id+"&mem_manuf_type="+type+"&serno="+serno;
			if(iColl.isEmpty())
			{
				kColl.setDataValue("serno", serno);
				dao.insert(kColl, connection);
				context.addDataField("flag", "success");
				context.addDataField("id", paramStr);//用作跳转参数 
				context.addDataField("msg", "保存成功！");
			}else
			{
				context.addDataField("flag", "fail");
				if("01".equals(type))
				{
					context.addDataField("msg","该客户已以供应商身份存在网络中");
				}else
				{
					context.addDataField("msg", "该客户已以经销商身份存在网络中");
				}
				context.addDataField("id", "");
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
