package com.yucheng.cmis.biz01line.acc.op.accloan;

import java.sql.Connection;
import java.text.SimpleDateFormat;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.util.TableModelUtil;

public class SelectRepayPlayOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String bill_no = null;
			String flag = PUBConstant.SUCCESS;
			IndexedCollection iColl = new IndexedCollection("RepayPlayList");
			try {
				bill_no = (String)context.getDataValue("bill_no");
			} catch (Exception e) {}
			if(bill_no == null || bill_no.length() == 0)
				throw new EMPJDBCException("The value of bill_no cannot be null!");
			int size = 10; 
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			/*** 调用核算实时接口取还款计划 ***/
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			KeyedCollection ESB_kColl = service.tradeHKJHYM(bill_no, context, connection,pageInfo);
			String RET_CODE = (String)ESB_kColl.getDataValue("RET_CODE");
			if("000000".equals(RET_CODE)){
				String totalRowsR = (String)ESB_kColl.getDataValue("totalRowsR");//总条数 
				pageInfo.setRecordSize(totalRowsR);
				
				iColl = (IndexedCollection)ESB_kColl.getDataElement("BODY");
				iColl.setName("RepayPlayList");
				//格式化日期
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kColl = (KeyedCollection)iColl.get(i);
					String BegDt = (String)kColl.getDataValue("BegDt");
					String EndDt = (String)kColl.getDataValue("EndDt");
					if(BegDt != null && !"".equals(BegDt)){
						BegDt = new SimpleDateFormat("yyyy-mm-dd").format( new SimpleDateFormat("yyyymmdd").parse(BegDt));
						kColl.put("BegDt", BegDt);
					}
					if(EndDt != null && !"".equals(EndDt)){
						EndDt = new SimpleDateFormat("yyyy-mm-dd").format( new SimpleDateFormat("yyyymmdd").parse(EndDt));
						kColl.put("EndDt", EndDt);
					}
					kColl.put("BillNo", bill_no);
				}
			}else{
				flag = ESB_kColl.getDataValue("RET_MSG").toString();
			}
			//页面展示集合
			IndexedCollection iCollForPage = new IndexedCollection();//分页时，展示在页面上的记录集合
			iCollForPage.setName("RepayPlayList");
			for(int i=(pageInfo.pageIdx*pageInfo.pageSize-pageInfo.pageSize);i<pageInfo.pageSize*pageInfo.pageIdx;i++){
				if(iColl.size()>i){
					   KeyedCollection kColl = (KeyedCollection)iColl.get(i);
					   iCollForPage.addDataElement(kColl);
				}else{
					break;
				}
			}
			
			context.addDataField("flag", flag);
			this.putDataElement2Context(iCollForPage, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
		}catch (EMPException ee) {
			throw new EMPException(ee.getMessage());
		} catch(Exception e){
			throw new EMPException(e.getMessage());
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
