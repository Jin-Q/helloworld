package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.iqpmortcatalogmana;


import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.component.CatalogManaComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddIqpMortCatalogManaRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpMortCatalogMana";
	
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
			
			String catalog_no_value = "";
			TableModelDAO dao = this.getTableModelDAO(context);
			
			if("1".equals(kColl.getDataValue("catalog_lvl"))){   //押品目录层级为1时，目录编号生成
				//获取流水号
				catalog_no_value = CMISSequenceService4JXXD.querySequenceFromDB("CATALOG_NO", "all", connection, context);
			}else{
				CatalogManaComponent catalogManaComponent = (CatalogManaComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("CatalogManaComponent", context, connection);
				String max_catalog_no = catalogManaComponent.searchMaxCatalogNo(kColl.getDataValue("sup_catalog_no").toString());  //得到最大目录编号
				if(null == max_catalog_no || "".equals(max_catalog_no.trim())){
					catalog_no_value = kColl.getDataValue("sup_catalog_no").toString()+"01";
				}else{
					max_catalog_no = max_catalog_no.substring(2,max_catalog_no.length());
					BigInteger b = new BigInteger(max_catalog_no);
					BigInteger a = new BigInteger("1");
					BigInteger c = b.add(a);
					catalog_no_value = "Z0"+c+"";
				}
				
				
			}
			
			kColl.setDataValue("catalog_no", catalog_no_value);  //流水号每次自动生成
			
			//目录路径去除虚拟的“押品目录”+自己本身
			String catalog_path =  kColl.getDataValue("catalog_path").toString().replaceAll("ALL", "")+catalog_no_value;  
			kColl.setDataValue("catalog_path",catalog_path);
			//add a record
			dao.insert(kColl, connection);
			
			context.addDataField("flag", "Y");
			context.addDataField("msg", "Y");
			context.addDataField("catalog_no", catalog_no_value);
			
			context.addDataField("operate", "updateIqpMortCatalogManaRecord.do");
		}catch(Exception e){
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
	public static void main(String args[]){
		BigInteger b = new BigInteger("90124010101");
		BigInteger a = new BigInteger("1");
		BigInteger c = b.add(a);
		System.out.println(c);
	}
}
