package com.yucheng.cmis.biz01line.image.op.ImageModuleManage;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.util.TableModelUtil;

public class DelImageGuaranteeNoActionOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection kColl = new KeyedCollection();
			IndexedCollection iColl_list = new IndexedCollection();
			
			if(context.containsKey("ImageGuaranteeNo")){
				KeyedCollection queryData = (KeyedCollection)context.getDataElement("ImageGuaranteeNo");
				if(queryData.containsKey("cus_id")){
					kColl.addDataField("cus_id", queryData.getDataValue("cus_id"));	//客户编号
					iColl_list = getImageNo(context, kColl );
				}
			}
			
			iColl_list.setName("ImageGuaranteeNoList");
			this.putDataElement2Context(iColl_list, context);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)this.releaseConnection(context, connection);
		}
		return "0";
	}
	
	private IndexedCollection getImageNo( Context context , KeyedCollection kColl ) throws Exception {
		IndexedCollection iColl_list = new IndexedCollection();
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
		KeyedCollection retKColl = serviceRel.tradeXYYPBHHQ(kColl, context, this.getConnection(context));	//调用影像核对接口
		
		if(retKColl.getDataValue("RET_CODE").equals("000000")){
			KeyedCollection kColl_BODY = (KeyedCollection) retKColl.getDataElement("BODY");
			String CLIENT_NO = kColl_BODY.getDataValue("CLIENT_NO").toString();
			
			iColl_list = (IndexedCollection) kColl_BODY.getDataElement("PLEDGE_NO_ARRAY");
			for (int i=0 ; i< iColl_list.size();i++){
				KeyedCollection kColl_list = (KeyedCollection) iColl_list.get(i);
				String PLEDGE_NO = kColl_list.getDataValue("PLEDGE_NO").toString();
				String sql = "select count(*)cc from Mort_Guaranty_Base_Info m where guaranty_info_status != '4' "
					+ " and  m.image_guaranty_no = '"+PLEDGE_NO+"'";
				IndexedCollection iColl_his = TableModelUtil.buildPageData(null, this.getDataSource(context), sql);
				if(iColl_his.size() > 0){
					KeyedCollection kColl_his = (KeyedCollection) iColl_his.get(0);
					if((kColl_his.getDataValue("cc")).equals("1")){
						iColl_list.remove(i);
						i--;
					}else{
						kColl_list.addDataField("cus_id", CLIENT_NO);
					}
				}
			}
		}
		
		return iColl_list;
	}

}