package com.yucheng.cmis.biz01line.authorize.op.pvpauthorize;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.dc.eai.data.CompositeData;
import com.dcfs.esb.client.ESBClient;
import com.dcfs.esb.pack.standardxml.PackUtil;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.interfaces.ClientTradeInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 
 * 定时调度自动业务出账授权
 * @author yangzy
 * added by yangzy 2015/04/07 需求：XD150325024，集中作业扫描岗权限改造—自动授权
 */
public class GetAutoPvpAuthorizeOp extends CMISOperation {
	ClientTradeInterface clientTradeInterface;
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		connection = this.getConnection(context);
		try {
			TableModelDAO dao = this.getTableModelDAO(context);
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0,"GetAutoPvpAuthorizeOp+++++++++++++++++++++++++++++++",null);
			IndexedCollection iColl = (IndexedCollection)SqlClient.queryList4IColl("getPvpAutoAuthorizeInfo", null, connection);
			String openDay = (String)SqlClient.queryFirst("querySysInfo", null, null, connection);
			if(iColl!=null&&iColl.size()>0){
				for(int i=0;i<iColl.size();i++){//循环遍历授权信息表
					boolean tran_flag = true; //交易处理标识
					boolean result = false;//反馈信息标识
					KeyedCollection retKColl;
					KeyedCollection kColl = (KeyedCollection)iColl.get(i);
					String tran_id = (String) kColl.getDataValue("tran_id");
					String authorize_no = (String) kColl.getDataValue("authorize_no");
					String tran_serno = (String) kColl.getDataValue("tran_serno");
				    String status = (String) kColl.getDataValue("status");
					Map<String, String> param = new HashMap<String, String>();
					param.put("authorize_no", authorize_no);
					param.put("status", status);
					param.put("tran_date", openDay);
					SqlClient.insert("insertPvpAutoAuthorize", param, connection);
										
					String serviceCode = tran_id.substring(0, 11);
					String senceCode = tran_id.substring(11, 13);
					
					/** 加载转发机制 */
					KeyedCollection kc = new KeyedCollection();
					kc.addDataField("SERVICE_CODE", serviceCode);
					kc.addDataField("SERVICE_SCENE", senceCode);
					/** 通过交易码查询所配置的交易实现类，决定所作的交易处理 */
					String impleClass = (String)SqlClient.queryFirst("queryEsbConfig", kc, null, connection);
					context.put("service_code",serviceCode);
					context.put("sence_code",senceCode);
					context.put("tran_serno",tran_serno);
					context.put("OPENDAY",openDay);
					context.put("currentUserId","admin");
					context.put("currentUserName","超级管理员");
					context.put("organNo","9350000000");
					
					if(impleClass == null || impleClass.trim().length() == 0){
						tran_flag = false;
					}else{
						try {
							clientTradeInterface = (ClientTradeInterface)Class.forName(impleClass.trim()).newInstance();
						} catch (Exception e) {
							tran_flag = false;
						}
					}
					if(tran_flag){
						CompositeData reqCD = null;
						try {
							reqCD = clientTradeInterface.doExecute(context, connection);
						} catch (Exception e) {
							KeyedCollection retKCollF = new KeyedCollection();
							retKCollF.addDataField("RET_CODE","9999");
							retKCollF.addDataField("RET_MSG","定时调度自动业务出账授权["+tran_serno+"]出错，错误原因："+e.getMessage());
							retKCollF.setName("SYS_HEAD");
							dulReturnMsg(retKCollF,"01",authorize_no, context, connection);
							tran_flag = false;
						}
						if(tran_flag){
							/** 打印后台发送日志 */
							EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(reqCD), "UTF-8"));
							/** 执行发送操作 */
							CompositeData retCD = ESBClient.request(reqCD);
							System.out.println("****************************************");
							System.out.println(new String(PackUtil.pack(reqCD), "UTF-8"));
							/** 打印后台反馈日志 */
							EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
							/** 解析反馈报文 */
							ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
							/** 解析反馈报文头 */
							retKColl = esbInterfacesImple.getRespSysHeadCD(retCD);
							/** 判断报文发送成功与否 */
							if(TagUtil.haveSuccess(retKColl, context)){//成功
								result=true;
								/** 判断是否为授权交易，如果为授权交易则需要更新授权表信息 */
								if(("02001000001".equals(serviceCode)&&"04".equals(senceCode))||
								   ("02001000001".equals(serviceCode)&&"07".equals(senceCode))||
								   ("02001000001".equals(serviceCode)&&"08".equals(senceCode))||
								   ("02001000001".equals(serviceCode)&&"09".equals(senceCode))||
								   ("03001000008".equals(serviceCode)&&"01".equals(senceCode))||
								   ("02002000011".equals(serviceCode)&&"02".equals(senceCode))||
								   ("02001000003".equals(serviceCode)&&"02".equals(senceCode))||
								   ("06001000003".equals(serviceCode)&&"01".equals(senceCode))||
								   (TradeConstance.SERVICE_CODE_GJ.equals(serviceCode) && TradeConstance.SERVICE_SCENE_GJSQ.equals(senceCode))||
								   (TradeConstance.SERVICE_CODE_ZYGL.equals(serviceCode) && TradeConstance.SERVICE_SCENE_ZCZRSQ.equals(senceCode))){
									dulReturnMsg(retKColl,"02",authorize_no, context, connection);
								}else if("02002000007".equals(serviceCode)&&"01".equals(senceCode)){
									//XD150520037_信贷系统利率调整修改优化
									dulReturnMsg(retKColl,"02",authorize_no, context, connection);
									clientTradeInterface.doSuccess(context, connection);
								}
							}else {//失败
								result=false;
								/** 判断是否为授权交易，如果为授权交易则需要更新授权表信息 */
								if(("02001000001".equals(serviceCode)&&"04".equals(senceCode))||
								   ("02001000001".equals(serviceCode)&&"07".equals(senceCode))||
								   ("02001000001".equals(serviceCode)&&"08".equals(senceCode))||
								   ("02001000001".equals(serviceCode)&&"09".equals(senceCode))||
								   ("03001000008".equals(serviceCode)&&"01".equals(senceCode))||
								   ("02002000011".equals(serviceCode)&&"02".equals(senceCode))||
								   ("02001000003".equals(serviceCode)&&"02".equals(senceCode))||
								   ("06001000003".equals(serviceCode)&&"01".equals(senceCode))||
								   (TradeConstance.SERVICE_CODE_GJ.equals(serviceCode)&& TradeConstance.SERVICE_SCENE_GJSQ.equals(senceCode))||
								   (TradeConstance.SERVICE_CODE_ZYGL.equals(serviceCode) && TradeConstance.SERVICE_SCENE_ZCZRSQ.equals(senceCode))||
								   //XD150520037_信贷系统利率调整修改优化
								   ("02002000007".equals(serviceCode)&&"01".equals(senceCode))){
									dulReturnMsg(retKColl,"01",authorize_no, context, connection);
								}
							}
						}
					}else{
						KeyedCollection retKCollF = new KeyedCollection();
						retKCollF.addDataField("RET_CODE","9999");
						retKCollF.addDataField("RET_MSG","交易码【"+serviceCode+"】，交易场景【"+senceCode+"】未获取到配置的业务实现类/非自动授权类交易！");
						retKCollF.setName("SYS_HEAD");
						dulReturnMsg(retKCollF,"00",authorize_no, context, connection);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			EMPLog.log("GetAutoPvpAuthorizeOp", EMPLog.ERROR, 0, "定时调度自动业务出账授权出错，错误原因："+e.getCause().getMessage());
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

	/**
	 * 针对授权信息更新本地授权信息更新操作实现方法
	 * @param retKColl 反馈状态
	 * @param context 上下文
	 * @param connection 数据库连接
	 */
	private void dulReturnMsg(KeyedCollection retKColl,String status,String authorizeNo, Context context, Connection connection) throws EMPException {
		TableModelDAO dao = this.getTableModelDAO(context);
		IndexedCollection pvpIColl = dao.queryList("PvpAuthorize", " where authorize_no = '"+authorizeNo+"' and status <> '02'", connection);
		String sendNum = "";//发送次数
		for(int i=0;i<pvpIColl.size();i++){
			KeyedCollection kc = (KeyedCollection)pvpIColl.get(i);
			/** 发送次数+1 */
			sendNum = (String)kc.getDataValue("send_times");
			if(sendNum == null || sendNum.trim().length() == 0){
				sendNum = "0";
			}
			int sendTimes = Integer.parseInt(sendNum);
			sendNum = String.valueOf(sendTimes+1);
			kc.setDataValue("status", status);
			kc.setDataValue("send_times", sendNum);
			kc.setDataValue("tran_date", context.getDataValue(CMISConstance.OPENDAY));//授权发送时更新交易日期
			kc.setDataValue("return_code", retKColl.getDataValue("RET_CODE"));
			kc.setDataValue("return_desc", retKColl.getDataValue("RET_MSG"));
			dao.update(kc, connection);
		}
	}
}
