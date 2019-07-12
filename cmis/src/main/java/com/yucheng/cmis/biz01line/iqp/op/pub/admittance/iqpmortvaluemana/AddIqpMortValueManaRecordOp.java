package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.iqpmortvaluemana;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddIqpMortValueManaRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpMortValueMana";
	
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
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			//获取流水号
			String value_no = CMISSequenceService4JXXD.querySequenceFromDB("VALUEMANA_NO", "all", connection, context);
			kColl.setDataValue("value_no", value_no);
			String catalog_no =(String) kColl.getDataValue("catalog_no");
			String produce_vender =(String) kColl.getDataValue("produce_vender");//生产厂家
			String produce_area =(String) kColl.getDataValue("produce_area");//产地
			String sale_area =(String) kColl.getDataValue("sale_area");//销售区域
			//遍历该押品编号下存在的所有货物信息
			IndexedCollection ic = dao.queryList(modelId, "where 1=1", connection);
			//不存在货物记录时，可以新增任何押品所属目录下的货物
			if(null==ic){
				dao.insert(kColl, connection);
				context.addDataField("flag", "Y");
				context.addDataField("msg", "Y");
				context.addDataField("value_no", value_no);
			}else{
				int count = 0;//用来记录是否已存在将要新增的所属押品目录下的记录
				for(int i=0;i<ic.size();i++){
					KeyedCollection kc = (KeyedCollection) ic.get(i);
					if(catalog_no.trim().equals(kc.getDataValue("catalog_no"))&&
							(produce_vender.trim().equals(kc.getDataValue("produce_vender")))&&
							(produce_area.trim().equals(kc.getDataValue("produce_area")))&&
							(sale_area.trim().equals(kc.getDataValue("sale_area"))))
					{
						context.addDataField("flag","N");
						context.addDataField("msg","已存在此押品目录下，相同生产厂家、产地、销售区域的价格信息，不可以重复进行新增！");
						context.addDataField("value_no","");
						break;
					}else{
						count++;
					}
				}
				if(count==ic.size()){//遍历结束后不存在
					dao.insert(kColl, connection);
					context.addDataField("flag","Y");
					context.addDataField("msg","");
					context.addDataField("value_no", value_no);
				}
			}
			
			
		}catch(Exception e){
			context.addDataField("value_no", "");
			context.addDataField("msg", e.getMessage());
			context.addDataField("flag","N");
			try {
				connection.rollback();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
