package com.yucheng.cmis.biz01line.image.op.pubAction;

import java.util.ResourceBundle;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;

public class ImagePubFunction extends CMISOperation {
	
	private String UID="admin1";	//影像的一些权限参数
	private String PWD="123456";	//影像的一些权限参数
	private String AppID="App1001";	//影像的一些权限参数
	private String[] right= {"1111","0101"};	//操作权限：扫描[1111]，查看[0101]
	
	public String doExecute(Context context) throws EMPException {
		return "0";
	}
	
	/**
	 * 拼接第一部分url，公共部分组装
	 * 字段包括：HostPath,UID,PWD,AppID,UserID,UserName,OrgID,OrgName
	 */
	public String delFirstPartUrl(String url ,Context context) throws EMPException {
		String UserID = context.getDataValue("currentUserId").toString();	//登录用户id
		String UserName = context.getDataValue("currentUserName").toString();	//登录用户名称
		String OrgID = context.getDataValue("organNo").toString();	//登录机构id
		String OrgName = context.getDataValue("organName").toString();	//登录机构名称
		
		/*** 影像主机地址配置在cmis.properties中的imageHostPath ***/
		ResourceBundle res = ResourceBundle.getBundle("cmis");
		String imageHostPath = res.getString("imageHostPath");
		
		url = url+imageHostPath;
		url = url+"?UID="+UID;
		url = url+"&PWD="+PWD;
		url = url+"&AppID="+AppID;
		url = url+"&UserID="+UserID;
		url = url+"&UserName="+UserName;
		url = url+"&OrgID="+OrgID;
		url = url+"&OrgName="+OrgName;
		return url;
	}	

	/**
	 * 拼接第二部分url
	 * 接口2.2、2.3:客户资料扫描接口处理,客户资料查看接口处理(扫描与查看共用一个方法)
	 * 1.拼装info段，目前的处理方式参照老系统：info1客户信息+追加字段
	 * 2.扫描追加抵质押资料预扫描标志,查看追加客户编号
	 */
	public String delSecondBasicUrl(String url ,KeyedCollection kColl , Context context,int interface_type) throws EMPException {
		String cus_type = kColl.getDataValue("cus_type").toString();	//客户条线
		String cus_id = kColl.getDataValue("cus_id").toString();	//客户码
		String serno = kColl.getDataValue("serno").toString();	//业务编号
		String prd_id = kColl.getDataValue("prd_id").toString();	//产品编号,区分担保和客户
		String prd_stage = kColl.getDataValue("prd_stage").toString();	//业务阶段，担保会把影像押品编号传这个字段
		
		/*** 1.权限right段处理 ***/
		url = url+"&right="+right[interface_type];	//扫描取right[0],查看取right[1]
		
		/*** 2.取影像系统image_table，包括客户和担保两块 ***/
		ImagePubAction cmisOp = new ImagePubAction();
		String basic_image_table = cmisOp.getAllImageInfo(prd_id, "999999",  cus_type, context);
		
		/*** 3.业务信息段处理 ***/
		if(prd_id.equals("ASSURE")){
			url +="&info1="+basic_image_table+":"+prd_stage+";"+cus_id+"@";	//由于影像押品编号调整，押品现在要传入影像押品编号
			/***影像应需要求：查看保证人、抵质押物所有权人的基础资料  王青  start  ***/
			String cus_tab = cus_type.equals("BL300")?"NEW_JCZL":"NEW_COMJCZL";
			url +="&info2="+ cus_tab+":"+cus_id+";"+cus_id+"@";
			/***影像应需要求：查看保证人、抵质押物所有权人的基础资料  王青  end  ***/
		}else{
			url +="&info1="+basic_image_table+":"+serno+";"+cus_id+"@";
		}
		if("BZR".equals(prd_stage)){// 保证人影像查看不用客户全视图
			url += "&wholeViewCusNo=";
		}else{
			/*** 4.追加字段处理：扫描追加抵质押资料预扫描标志,查看追加客户编号 ***/
			if(interface_type ==1 && prd_id.equals("BASIC")){
				url += "&wholeViewCusNo="+cus_id;
			}else if(interface_type ==2 && prd_id.equals("BASIC")){
				url += "&wholeViewCusNo=";
			}else if(interface_type ==0){
				url += "&preScan=true";
			}
		}
		
		return url;
	}
	/**
	 * 拼接第二部分url
	 * 接口2.2、2.3:客户资料扫描接口处理,客户资料查看接口处理(扫描与查看共用一个方法)
	 * 1.拼装info段，目前的处理方式参照老系统：info1客户信息+追加字段
	 * 2.扫描追加抵质押资料预扫描标志,查看追加客户编号
	 */
	public String delSecondBasicUrl(String url ,KeyedCollection kColl , Context context,int interface_type,int flag) throws EMPException {
		String cus_type = kColl.getDataValue("cus_type").toString();	//客户条线
		String cus_id = kColl.getDataValue("cus_id").toString();	//客户码
		String serno = kColl.getDataValue("serno").toString();	//业务编号
		String prd_id = kColl.getDataValue("prd_id").toString();	//产品编号,区分担保和客户
		String prd_stage = kColl.getDataValue("prd_stage").toString();	//业务阶段，担保会把影像押品编号传这个字段
		
		/*** 1.权限right段处理 ***/
		url = url+"&right="+right[interface_type];	//扫描取right[0],查看取right[1]
		
		/*** 2.取影像系统image_table，包括客户和担保两块 ***/
		ImagePubAction cmisOp = new ImagePubAction();
		String basic_image_table = cmisOp.getAllImageInfo(prd_id, "999999",  cus_type, context);
		
		/*** 3.业务信息段处理 ***/
		if(prd_id.equals("ASSURE")){
			url +="&info1="+basic_image_table+":"+prd_stage+";"+cus_id+"@";	//由于影像押品编号调整，押品现在要传入影像押品编号
		}else{
			url +="&info1="+basic_image_table+":"+serno+";"+cus_id+"@";
		}
		
		/*** 4.追加字段处理：扫描追加抵质押资料预扫描标志,查看追加客户编号 ***/
		if(interface_type ==1&&flag!=2 && prd_id.equals("BASIC")){
			url += "&wholeViewCusNo="+cus_id;
		}else if(interface_type ==1 && flag==2 && prd_id.equals("BASIC")){
			url += "&wholeViewCusNo=";
		}else if(interface_type ==0){
			url += "&preScan=true";
		}
		return url;
	}
	/**
	 * 拼接第二部分url
	 * 接口2.4、2.5:业务资料扫描接口处理,业务资料查看接口处理(扫描与查看共用一个方法)
	 * 1.拼装info段，目前的处理方式参照老系统：info11客户信息+info21业务+info3i担保(多条，所有担保信息)+追加字段
	 * 2.台账时不传info11客户信息，申请与出账时仅客户条线为个人时传info11客户信息
	 * 3.最后追加全视图数据
	 */
	public String delSecondBusUrl(String url ,KeyedCollection kColl , Context context,int interface_type) throws EMPException {
		String prd_stage = kColl.getDataValue("prd_stage").toString();	//业务阶段
		String cus_type = kColl.getDataValue("cus_type").toString();	//客户条线
		String cus_id = kColl.getDataValue("cus_id").toString();	//客户码
		String prd_id = kColl.getDataValue("prd_id").toString();	//产品编号
		String serno = kColl.getDataValue("serno").toString();	//业务编号	
		ImagePubAction cmisOp = new ImagePubAction();
		
		/*** 1.权限right段处理 ***/
		url = url+"&right="+right[interface_type];	//扫描取right[0],查看取right[1]		
		/*** 3.业务信息段处理 ***/
		if(prd_id.equals("600020") || prd_id.equals("300022")|| prd_id.equals("300023")
				|| prd_id.equals("300024")){	//资产转让、转贴现单独处理
			String business_image_table = cmisOp.getAllImageInfo("BUSINESS", prd_id,  "BL100", context);
			url +="&info2="+business_image_table+":"+serno+";"+cus_id+"@" + prd_stage;
			
			if(interface_type ==1 ){
				//url += "&wholeViewData="+cus_id;
			}			
		}else{
			/*** 2.取影像系统image_table，包括业务、担保、客户信息 ***/
			String business_image_table = cmisOp.getAllImageInfo("BUSINESS", prd_id,  cus_type, context);
			String basic_image_table = cmisOp.getAllImageInfo("BASIC", "999999",  cus_type, context);
			String assure_image_table = cmisOp.getAllImageInfo("ASSURE", "999999", cus_type, context);
			
			/*** 3.1 添加基本资料，扫描非台账，个人时传info11客户信息，所有查看时也传 ***/
			if((!prd_stage.equals("2") && cus_type.equals("BL300"))|| interface_type==1){
				url +="&info1="+basic_image_table+":"+cus_id+";"+cus_id+"@"+ prd_stage;
			}
			
			/*** 3.2 添加业务资料info2 ***/
			url +="&info2="+business_image_table+":"+serno+";"+cus_id+"@" + prd_stage;
			
			/*** 3.2 添加担保资料info3i，传所有担保信息，是多记录处理 ***/
			KeyedCollection kColl_trans = new KeyedCollection();
			kColl_trans.addDataField("serno", serno);
			kColl_trans.addDataField("submitType", "GetGuarantyId");
			IndexedCollection iColl_assure = cmisOp.delSqlReturnIcoll(kColl_trans, context);
			if(iColl_assure.size() > 0){	//没有押品就没有这些字段了
				String info_append = "";	//拼接追加字段，取押品所有权人客户码
				for(int i = 0 ; i < iColl_assure.size();i++){
					KeyedCollection kColl_assure = (KeyedCollection) iColl_assure.get(i);
					Object guar_flag = kColl_assure.getDataValue("guar_flag");
					if(guar_flag!= null){	//保证传客户资料表，经影像确认，保证人业务编号传保证人cus_id
						url +="&info3"+i+"="+guar_flag+":"+kColl_assure.getDataValue("guaranty_id")+";"+kColl_assure.getDataValue("cus_id")+"@"+ prd_stage;
					}else{	//抵质押用抵质押表
						Object bus_line = kColl_assure.getDataValue("bus_line");
						url +="&info3"+i+"="+assure_image_table+":"+kColl_assure.getDataValue("guaranty_id")+";"+kColl_assure.getDataValue("cus_id")+"@"+ prd_stage;
						/*** 20140321调整，影像应喜要求：押品所有权人也要传 ***/
						url +="&info3"+i+2+"="+bus_line+":"+kColl_assure.getDataValue("cus_id")+";"+kColl_assure.getDataValue("cus_id")+"@"+ prd_stage;
					}
					info_append +=  kColl_assure.getDataValue("guaranty_id")+"@"+kColl_assure.getDataValue("cus_id")+",";
				}
				
				/*** 4.追加字段处理：全视图数据，根据押品加入所有权人客户码 ***/
				info_append = info_append.substring(0, info_append.length()-1);
				url += "&wholeViewData="+cus_id+":"+serno+":"+info_append;
			}
		}
		return url;
	}
	
	/**
	 * 拼接第二部分url
	 * 接口2.6、2.7:贷后资料扫描接口处理,贷后资料查看接口处理(扫描与查看共用一个方法)
	 * 1.拼装info段，目前的处理方式参照老系统：info1贷后信息
	 * 2.暂无追加字段
	 */
	public String delSecondPostUrl(String url ,KeyedCollection kColl , Context context,int interface_type) throws EMPException {
		String cus_type = kColl.getDataValue("cus_type").toString();	//客户条线
		String cus_id = kColl.getDataValue("cus_id").toString();	//客户码
		String serno = kColl.getDataValue("serno").toString();	//业务编号
		
		/*** 1.权限right段处理 ***/
		url = url+"&right="+right[interface_type];	//扫描取right[0],查看取right[1]
		
		/*** 2.取影像系统image_table，贷后资料 ***/
		ImagePubAction cmisOp = new ImagePubAction();
		String postloan_image_table = cmisOp.getAllImageInfo("POSTLOAN","999999", cus_type, context);
		
		/*** 3.业务信息段处理 ***/
		url +="&info1="+postloan_image_table+":"+serno+";"+cus_id+"@"; //贷后信息
		
		return url;
	}

	/**
	 * 拼接第二部分url
	 * 2.8.	条码打印接口
	 * 根据文档，只要加info段就行了
	 */
	public String delSecondPrintUrl(String url ,KeyedCollection kColl , Context context) throws EMPException {
		String prd_stage = kColl.getDataValue("prd_stage").toString();	//业务阶段
		String cus_type = kColl.getDataValue("cus_type").toString();	//客户条线
		String cus_id = kColl.getDataValue("cus_id").toString();	//客户码
		String prd_id = kColl.getDataValue("prd_id").toString();	//产品编号
		String serno = kColl.getDataValue("serno").toString();	//业务编号
		
		//right这个字段不需要了
		ImagePubAction cmisOp = new ImagePubAction();
		String print_image_table = "";
		
		if(prd_id.equals("BASIC") || prd_id.equals("ASSURE") || prd_id.equals("POSTLOAN")){	//客户、押品、贷后打印
			print_image_table = cmisOp.getAllImageInfo(prd_id,"999999",  cus_type, context);	//基础资料.image_table
		}else if(prd_id.equals("600020") || prd_id.equals("300022")|| prd_id.equals("300023")
				|| prd_id.equals("300024")){	//资产转让、转贴现单独处理
			print_image_table = cmisOp.getAllImageInfo("BUSINESS", prd_id,  "BL100", context);
		}else{
			print_image_table = cmisOp.getAllImageInfo("BUSINESS", prd_id,  cus_type, context);
		}
		url +="&info1="+print_image_table+":"+serno+";"+cus_id+"@"+prd_stage;
		
		return url;
	}
	
}