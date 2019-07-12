package com.yucheng.cmis.biz01line.arp.op.arpbadassethandoverapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.biz01line.arp.component.ArpPubComponent;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryDealDetailListOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection conn = null;
		try {
			conn = this.getConnection(context);
			String bill_no = context.getDataValue("bill_no").toString();
			IndexedCollection iColl = new IndexedCollection("AccLoanList");
			String flag = PUBConstant.SUCCESS;
			
			int size = 10; 
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			/*** 调用核算实时接口取交易明细 ***/
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			KeyedCollection ESB_kColl = service.tradeJYLSYM(bill_no, context, conn,pageInfo);
			IndexedCollection iCollTemp = new IndexedCollection();
			if(ESB_kColl.getDataValue("RET_CODE").equals("000000")){
				String totalRowsR = (String)ESB_kColl.getDataValue("totalRowsR");//总条数 
				iColl = (IndexedCollection)ESB_kColl.getDataElement("BODY");
				if(iColl == null){
					iColl = new IndexedCollection("AccLoanList");
				}else{
					iColl.setName("AccLoanList");
					for (int i = 0; i < iColl.size(); i++) {
						KeyedCollection kColl = new KeyedCollection();
						kColl = (KeyedCollection) iColl.getElementAt(i);
						if(kColl.containsKey("RecDtlAmt")){
							if(!"".equals((String)kColl.getDataValue("RecDtlAmt"))){
								iCollTemp.add(kColl);
							}
						}
						//kColl.addDataField("BillNo", bill_no);
					}
					/*** 日期转10位 ***/
					/*ArpPubComponent cmisComponent = (ArpPubComponent)CMISComponentFactory
						.getComponentFactoryInstance().getComponentInstance("ArpPubComponent",context,conn);
					KeyedCollection trans_kcoll = new KeyedCollection("TransValue");
					trans_kcoll.addDataField("value", "");
					trans_kcoll.addDataField("results", "");
					for (int i = 0; i < iColl.size(); i++) {
						KeyedCollection kColl = new KeyedCollection();
						kColl = (KeyedCollection) iColl.getElementAt(i);
						trans_kcoll.setDataValue("value", kColl.getDataValue("TRAN_DATE"));
						trans_kcoll = cmisComponent.delReturnSql("changeDateToTen",trans_kcoll);
						kColl.setDataValue("TRAN_DATE", trans_kcoll.getDataValue("results"));
					}*/
				}
			}else{
				flag = ESB_kColl.getDataValue("RET_MSG").toString();
			}
			//页面展示集合
			pageInfo.setRecordSize(String.valueOf(iCollTemp.size()));

			IndexedCollection iCollForPage = new IndexedCollection();//分页时，展示在页面上的记录集合
			iCollForPage.setName("AccLoanList");
			for(int i=(pageInfo.pageIdx*pageInfo.pageSize-pageInfo.pageSize);i<pageInfo.pageSize*pageInfo.pageIdx;i++){
				if(iCollTemp.size()>i){
					   KeyedCollection kColl = (KeyedCollection)iCollTemp.get(i);
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
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		} finally {
			if (conn != null)
				this.releaseConnection(context, conn);
		}
		return "0";
	}

}