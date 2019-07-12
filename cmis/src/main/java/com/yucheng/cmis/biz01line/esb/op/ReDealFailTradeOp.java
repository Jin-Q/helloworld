package com.yucheng.cmis.biz01line.esb.op;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.jfree.xml.generator.model.KeyDescription;

import com.dc.eai.data.CompositeData;
import com.dcfs.esb.pack.standardxml.XmlStaxParse;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.esb.interfaces.ESBInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.TradeInterface;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 
 * @author Administrator
 * 描述：对日终期间接收的实时通知交易重新处理	
 * 
 *
 */
public class ReDealFailTradeOp extends CMISOperation {
	
	public static final String ESB_INTERFACE_CLASS = "com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple";
	TradeInterface tradeInterface;
	ESBInterface esbinterface;

	public String doExecute(Context context) throws EMPException {
		
		CompositeData respCD = null;
		Connection connection = null;
		String openDay = null;
		String serviceCode = null;
		String serviceSence = null;
		String serno = null;
		String tran_date = null;
		String pk1 = null;
		String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());//精确到毫秒
		
		Map<String, String> paramValue = new HashMap<String, String>();
		try {
			connection = super.getConnection(context);
			
			openDay = (String)SqlClient.queryFirst("querySysInfo", null, null, connection);
			context.put("OPENDAY", openDay);
			
			esbinterface = (ESBInterface)Class.forName(ESB_INTERFACE_CLASS).newInstance();
			connection = this.getConnection(context);
			CompositeData data = new CompositeData();
			openDay = (String)context.getDataValue("OPENDAY");
			String openDay1 = openDay.replace("-", "");
			IndexedCollection iColl = (IndexedCollection)SqlClient.queryList4IColl("queryDaybatTrandInfo", openDay1, connection);
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String FileName = (String)kColl.getDataValue("locate_file");
				serviceCode = (String)kColl.getDataValue("service_code");
				serviceSence = (String)kColl.getDataValue("service_scene");
				serno = (String)kColl.getDataValue("consumer_seq_no");
				tran_date = (String)kColl.getDataValue("tran_date");
				tran_date = tran_date.substring(0,4)+"-"+tran_date.substring(4,6)+"-"+tran_date.substring(6,8);
				pk1 = (String)kColl.getDataValue("pk1");
				data = this.convertFileToCd(FileName, data);
				
				KeyedCollection condition = new KeyedCollection();
				condition.addDataField("SERVICE_CODE",serviceCode);
				condition.addDataField("SERVICE_SCENE",serviceSence);
				/** 通过交易码查询所配置的交易实现类，决定所作的交易处理 */
				String impleClass = (String)SqlClient.queryFirst("queryEsbConfig", condition, null, connection);
				tradeInterface = (TradeInterface)Class.forName(impleClass.trim()).newInstance();
				if(tran_date.equals(openDay)){//交易日期与当前营业日期相同则执行业务逻辑处理
					KeyedCollection resultKColl = tradeInterface.doExecute(data,connection);
					respCD = esbinterface.getRespSysHeadCD(serviceCode, serviceSence, openDay, nowDate, serno, "S", (String)resultKColl.getDataValue("ret_code"), (String)resultKColl.getDataValue("ret_msg"));
					paramValue.put("is_succ", "1");
					paramValue.put("resolve_type", "1");
					paramValue.put("deal_flag", "2");
					paramValue.put("trand_msg", "日间处理成功");
					SqlClient.update("updateDaybatTrandInfo", pk1, paramValue, null, connection);
				}
			}
			
		} catch (Exception e) {
			try {
				respCD = esbinterface.getRespSysHeadCD(serviceCode, serviceSence, openDay, nowDate, serno, "S", TradeConstance.RETCODE1,"发生异常，先返回成功，信贷后续处理");
				paramValue.put("is_succ", "2");
				paramValue.put("resolve_type", "");
				paramValue.put("deal_flag", "2");
				paramValue.put("trand_msg", "日终结束后重新处理再次发生异常，待下一个日终同步");
				SqlClient.update("updateDaybatTrandInfo", pk1, paramValue, null, connection);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

	/**
	 * 
	 * @param fileName
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public CompositeData convertFileToCd(String fileName,CompositeData data) throws Exception {
		
		File file = new File( fileName);
		XmlStaxParse parse = new XmlStaxParse();
		FileInputStream in;
		byte[] bts = null;
		try {
			in = new FileInputStream(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int intLength = 0;
			while ((intLength = in.read()) != -1) {
				out.write(intLength);
			}
			bts = out.toByteArray();
		} catch (Exception e1) {
		}
		parse.parse(bts, data);
		return data;
	}
}
