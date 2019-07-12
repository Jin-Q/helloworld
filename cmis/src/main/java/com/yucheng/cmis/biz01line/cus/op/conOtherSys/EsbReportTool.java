package com.yucheng.cmis.biz01line.cus.op.conOtherSys;

import com.dc.eai.data.CompositeData;
import com.ecc.emp.core.Context;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 *Esb报文处理工具类
 */
public class EsbReportTool {

	/**
	 * 组装Esb报文头并返回
	 * @param serviceCode 服务id， serviceScene 服务场景， consumerId 系统Id
	 * @return cd 报文
	 * @throws Exception 
	 */
	public static CompositeData produceHead(String serviceCode, String serviceScene, Context context) throws Exception{
		CompositeData reqCD = new CompositeData();
		String serno="164736056";
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
		/** 系统头 */
		reqCD.addStruct("SYS_HEAD", service.getSysHeadCD(serviceCode, serviceScene, TradeConstance.CONSUMERID, serno,
				(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
		/** 应用头 */
		reqCD.addStruct("APP_HEAD", service.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
				(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
		
		//打印组装出的cd报文
		System.out.println(reqCD);
		//打印由cd报文组出的xml报文
//		System.out.println(PackUtil.pack(reqCD));
		return reqCD;
	}
	
	/**
	 * 发送Esb报文并接收返回报文，同时将发送的报文和返回的报文分别记入日志文件
	 * @param cd 发送的报文
	 * @return
	 * @throws TimeoutException
	 */
//	public static CompositeData sendMess(CompositeData cd) throws TimeoutException{
//		//发送报文并记录日志
//		StringBuffer sbout = new StringBuffer();
//		StringBuffer sbin = new StringBuffer();
//		sbout.append("-------------------------begin:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"-------------------------------");
//		sbout.append(cd);
//		sbin.append("-------------------------begin:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"-------------------------------");
//		CompositeData resp = ESBClient.request(cd);//发送报文
//		sbout.append("-------------------------end:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"-------------------------------");
//		sbin.append(resp);
//		sbin.append("-------------------------end:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"-------------------------------");
//		//将发送报文和接收报文分别放入日志以便查询
//		EMPLog.log("outReport", EMPLog.INFO, 0, sbout.toString());
//		EMPLog.log("inReport", EMPLog.INFO, 0, sbin.toString());
//		
//		return resp;
//	}
	
	
	/**
	 * 将Ecif币种转为本系统币种
	 * @param eCurType
	 * @return
	 */
	public static String changeEcifCurType(String eCurType){
		String oCurType = "";
		if(eCurType!=null&&!"".equals(eCurType)){
			if("124".equals(eCurType)){//加拿大元
				oCurType = "CAD";
			}else if("156".equals(eCurType)){//人民币
				oCurType = "CNY";
			}else if("280".equals(eCurType)){//德国马克
				oCurType = "DEM";
			}else if("756".equals(eCurType)){//瑞士法郎
				oCurType = "SFR";
			}else if("840".equals(eCurType)){//美元
				oCurType = "USD";
			}else if("036".equals(eCurType)){//澳元
				oCurType = "AUD";
			}else if("250".equals(eCurType)){//法国法郎
				oCurType = "FRF";
			}else if("344".equals(eCurType)){//港币
				oCurType = "HKD";
			}else if("392".equals(eCurType)){//日元
				oCurType = "JPY";
			}else if("826".equals(eCurType)){//英镑
				oCurType = "GBP";
			}else if("978".equals(eCurType)){//欧元
				oCurType = "EUR";
			}
		}
		
		return oCurType;
	}
	
	/**
	 * 将Ecif证件类型转为本系统类型
	 * @param eCurType
	 * @return
	 */
	public static String changeEcifCertType(String eCertType){
		String oCertType = "";
		if(eCertType!=null&&!"".equals(eCertType)){
			if("100".equals(eCertType)){//身份证
				oCertType = "0";
			}else if("130".equals(eCertType)){//户口薄
				oCertType = "1";
			}else if("140".equals(eCertType)){//护照
				oCertType = "2";
			}else if("112".equals(eCertType)){//军官证
				oCertType = "3";
			}else if("111".equals(eCertType)){//士兵证
				oCertType = "4";
			}else if("161".equals(eCertType)){//港澳居民
				oCertType = "5";
			}else if("162".equals(eCertType)){//台湾同胞
				oCertType = "6";
			}else if("101".equals(eCertType)){//临时身份证
				oCertType = "7";
			}else if("181".equals(eCertType)){//外国人居留证
				oCertType = "8";
			}else if("122".equals(eCertType)){//警官证
				oCertType = "9";
			}else if("210".equals(eCertType)){//组织机构代码
				oCertType = "a";
			}else if("200".equals(eCertType)){//营业执照
				oCertType = "b";
			}else if("311".equals(eCertType)){//贷款卡
				oCertType = "c";
			}else if("011".equals(eCertType)){//其他证件
				oCertType = "X";
			}
		}
		
		return oCertType;
	}
}
