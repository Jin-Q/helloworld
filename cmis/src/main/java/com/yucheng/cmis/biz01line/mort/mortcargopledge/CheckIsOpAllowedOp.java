package com.yucheng.cmis.biz01line.mort.mortcargopledge;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckIsOpAllowedOp extends CMISOperation {
	
	//operation TableModel
	private final String modelIdReplList = "MortCargoReplList";//货物清单
	private final String modelId = "MortCargoPledge";//货物清单
	private final String modelIdRepl = "MortCargoRepl";//货物置换流水
	private final String modelIdStorage = "MortCargoStorage";//货物入库流水
	private final String modelIdExware = "MortCargoExware";//货物出库流水
	private final String modelIdBail = "MortBailDeliv";//提货清单
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		
		try{
			connection = this.getConnection(context);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			try {
					String guaranty_no =(String) context.getDataValue("guaranty_no");
					String condStr = "where cargo_id in (select cargo_id from Mort_Cargo_Pledge where guaranty_no ='"+guaranty_no+"')";
					String condStr2 = "where cargo_id in (select cargo_id from mort_cargo_repl_list where guaranty_no ='"+guaranty_no+"' and oper='2')";//补货入库时
					String condStr1 = "where guaranty_no='"+guaranty_no+ "' and status = '00'";
					//遍历该押品编号下存在的所有货物信息
					IndexedCollection ic = dao.queryList(modelId, condStr, connection);
					//遍历该押品编号下存在的所有货物信息
					IndexedCollection icC = dao.queryList(modelIdReplList, condStr2, connection);
					//遍历该押品编号下存在的所有提货信息
					IndexedCollection icD = dao.queryList(modelIdBail, condStr1, connection);
					if(icD.size()==0){
						//不存在货物记录时，无法进行入库，出库，货物置换，保证金提货等操作
						if(null==ic){
							context.addDataField("flag", "fail");
							context.addDataField("msg", "不存在货物记录时，无法进行入库，出库，货物置换，保证金提货等操作！");
						}else{
							int count = 0;//用来记录遍历的货物记录的条数
							for(int i=0;i<ic.size();i++){
								KeyedCollection kc = (KeyedCollection) ic.get(i);
								if("04".equals(kc.getDataValue("cargo_status"))){//入库待记账的货物记录（入库或者货物置换时，可以产生）
									condStr = "where guaranty_no='"+guaranty_no+"' and status = '00'";
									IndexedCollection icc = dao.queryList(modelIdRepl, condStr,connection);//查询货物置换表是否存在未记账的记录
									IndexedCollection icS = dao.queryList(modelIdStorage, condStr,connection);//查询货物入库表是否存在未记账的记录
									if(icc.size()!=0){//存在未完成的货物置换操作时产生
										context.addDataField("flag", "fail");
										context.addDataField("msg", "此押品存在未记账的“货物置换”操作，暂不能做其他操作！");
										break;
									}else if(icS.size()!=0){//存在未完成的入库操作时产生
										context.addDataField("flag", "fail");
										context.addDataField("msg", "此押品存在未记账的“入库”操作，暂不能做其他操作！");
										break;
									}else{
										count++;
									}
								}else if("05".equals(kc.getDataValue("cargo_status"))){//出库待记账的货物记录（出库或者货物置换时，可以产生）
									condStr = "where guaranty_no='"+guaranty_no+"' and status = '00'";
									IndexedCollection icc = dao.queryList(modelIdRepl, condStr,connection);//查询货物置换表是否存在未记账的记录
									IndexedCollection icE = dao.queryList(modelIdExware, condStr,connection);//查询货物出库表是否存在未记账的记录
									if(icc.size()!=0){//存在未完成的货物置换操作时产生
										context.addDataField("flag", "fail");
										context.addDataField("msg", "此押品存在未记账的“货物置换”操作，暂不能做其他操作！");
										break;
									}else if(icE.size()!=0){//存在未完成的出库操作时产生
										context.addDataField("flag", "fail");
										context.addDataField("msg", "此押品存在未记账的“出库”操作，暂不能做其他操作！");
										break;
									}else{
										count++;
									}
								}else{
									count++;//对符合的记录做累加
								}
							}
							if(count==ic.size()){//遍历结束后不存在
								context.addDataField("flag", "success");
								context.addDataField("msg", "");
							}
						}
					}else{
						context.addDataField("flag", "fail");
						context.addDataField("msg", "此押品存在未记账的“保证金提货”操作，暂不能做其他操作！");
					}
					
					
			} catch (Exception e) {}
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
}
