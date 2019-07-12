package com.yucheng.cmis.biz01line.image.op.pubAction;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.image.component.ImagePubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class ImagePubAction extends CMISOperation {
	
	public String doExecute(Context context) throws EMPException {
		return "0";
	}
	
	/*** 返回无分页icoll的命名sql调用，kcoll中应该包括submitType***/
	public IndexedCollection delSqlReturnIcoll(KeyedCollection kColl , Context context) throws EMPException {
		ImagePubComponent cmisComponent = (ImagePubComponent) CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance("ImagePubComponent", context , this.getConnection(context));
		IndexedCollection Results = new IndexedCollection();
		Results =  cmisComponent.delSqlReturnIcoll(kColl);
		return Results;
	}
	
	/*** 返回kcoll的命名sql调用，kcoll中应该包括submitType ***/
	public KeyedCollection delSqlReturnKcoll(KeyedCollection kColl ,Context context) throws EMPException {
		ImagePubComponent cmisComponent = (ImagePubComponent) CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance("ImagePubComponent", context, this.getConnection(context));
		KeyedCollection Results = new KeyedCollection();
		Results = cmisComponent.delSqlReturnKcoll(kColl);
		return Results;
	}
	
	/*** 无返回值的命名sql调用，kcoll中应该包括submitType ***/
	public void delSqlNoReturn(KeyedCollection kColl ,Context context) throws EMPException {
		ImagePubComponent cmisComponent = (ImagePubComponent) CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance("ImagePubComponent", context, this.getConnection(context));
		cmisComponent.delSqlNoReturn(kColl);
	}
	
	/*** 带默认值的kColl取值 ***/
	public Object getDataDefvalue(KeyedCollection kColl, String name ,Object Defvalue ) throws EMPException {
		Object result = kColl.getDataValue(name);
		result = (result==null||result.equals(""))?Defvalue:result;
		return result;
	}
	
	/** 
	 * 通用取影像资料信息，并返回image_table，多次改动最终版
	 * 这个方法只返回image_table字段，因为经过多次调整只有这个字段有意义了，返回值是经过空值处理的。
	 * 由image_crd_relation表唯一索引info_type,prd_id,cus_type确定image_table
	 */
	public String getAllImageInfo(String info_type,String prd_id,String cus_type,Context context) throws EMPException {
		String image_info = "";
		KeyedCollection kColl = new KeyedCollection();

		kColl.addDataField("info_type", info_type);
		kColl.addDataField("prd_id", prd_id);
		kColl.addDataField("cus_type", cus_type);
		kColl.addDataField("submitType", "getAllImageInfo");
		
		kColl = delSqlReturnKcoll(kColl, context);
		image_info = getDataDefvalue(kColl, "image_table", "").toString();
		return image_info;
	}
	
	/** 
	 * 通用取影像资料信息，并返回所有字段kColl，多次改动最终版
	 * 以防以后用到，还是提供这样一个方法，取所有image_crd_relation表信息。
	 * 直接返回查询结果的kColl，使用更灵活，要自己处理空值等问题
	 * 由image_crd_relation表唯一索引info_type,prd_id,cus_type确定image_table
	 */
	public KeyedCollection getAllImageKcoll(String info_type,String prd_id,String cus_type,Context context) throws EMPException {
		KeyedCollection kColl = new KeyedCollection();

		kColl.addDataField("info_type", info_type);
		kColl.addDataField("prd_id", prd_id);
		kColl.addDataField("cus_type", cus_type);
		kColl.addDataField("submitType", "getAllImageInfo");
		
		kColl = delSqlReturnKcoll(kColl, context);
		return kColl;
	}
		
}