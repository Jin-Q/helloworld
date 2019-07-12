package com.yucheng.cmis.platform.flashcharts.op;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.flashcharts.FCFCharHelper;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.util.TableModelUtil;

/**
 * 三类FLASH图形展示
 * 
 * @author JackYu
 *
 */
public class ShowFlashDemoOp extends CMISOperation{

	
	@Override
	public String doExecute(Context context) throws EMPException {

		
		Connection connection = null;
		try{
			
			//数据准备
			connection = this.getConnection(context);
			IndexedCollection iColl1 = preSingleFlashData(connection,"01");
			IndexedCollection iColl2 = preSingleFlashData(connection,"02");
			
			
			
			//单系图形
			String singleXML = FCFCharHelper.createXMLDataSingle(iColl1, "xName", "yName", "单系图形展示", "信贷产品年销售额(单位：万元)");
//			//多系图形
			String multiXML = FCFCharHelper.createXMLDataMulti(iColl1, iColl2, "xName", "yName", "多系图形展示", "产品年销售额(单位：万元)", "信贷产品", "网银产品");
//			//组合图形
			String complexXML = FCFCharHelper.createXMLDataComplex(iColl1, iColl2, "xName", "yName", "组合系图形展示", "产品年销售额(单位：万元)", "信贷产品", "网银产品");
//			
			context.addDataField("singleXML", singleXML);
			context.addDataField("multiXML", multiXML);
			context.addDataField("complexXML", complexXML);
			
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
	
	private IndexedCollection preSingleFlashData(Connection con, String flag) throws Exception{
		IndexedCollection singleIColl = new IndexedCollection();
		
		List<KeyedCollection> list = (List<KeyedCollection>)SqlClient.queryList("demo_flash_01", flag, con);
		for (int i = 0; i < list.size(); i++) {
			KeyedCollection tmp =list.get(i);
			
			KeyedCollection kColl = new KeyedCollection();
			kColl.addDataField("xName", "2001年");
			kColl.addDataField("yName", tmp.getDataValue("year_2001"));
			singleIColl.add(kColl);
			
			kColl = new KeyedCollection();
			kColl.addDataField("xName", "2002年");
			kColl.addDataField("yName", tmp.getDataValue("year_2002"));
			singleIColl.add(kColl);
			
			kColl = new KeyedCollection();
			kColl.addDataField("xName", "2003年");
			kColl.addDataField("yName", tmp.getDataValue("year_2003"));
			singleIColl.add(kColl);
			
			kColl = new KeyedCollection();
			kColl.addDataField("xName", "2004年");
			kColl.addDataField("yName", tmp.getDataValue("year_2004"));
			singleIColl.add(kColl);
			
			kColl = new KeyedCollection();
			kColl.addDataField("xName", "2005年");
			kColl.addDataField("yName", tmp.getDataValue("year_2005"));
			singleIColl.add(kColl);
			
			kColl = new KeyedCollection();
			kColl.addDataField("xName", "2006年");
			kColl.addDataField("yName", tmp.getDataValue("year_2006"));
			singleIColl.add(kColl);
			
			kColl = new KeyedCollection();
			kColl.addDataField("xName", "2007年");
			kColl.addDataField("yName", tmp.getDataValue("year_2007"));
			singleIColl.add(kColl);
			
			kColl = new KeyedCollection();
			kColl.addDataField("xName", "2008年");
			kColl.addDataField("yName", tmp.getDataValue("year_2008"));
			singleIColl.add(kColl);
			
			kColl = new KeyedCollection();
			kColl.addDataField("xName", "2009年");
			kColl.addDataField("yName", tmp.getDataValue("year_2009"));
			singleIColl.add(kColl);
			
			kColl = new KeyedCollection();
			kColl.addDataField("xName", "2010年");
			kColl.addDataField("yName", tmp.getDataValue("year_2010"));
			singleIColl.add(kColl);
			
		}
		
		return singleIColl;
	}

}
