package com.yucheng.cmis.biz01line.iqp.op.iqpbailsubdis;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddIqpBailSubDisRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpBailSubDis";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			int bfCount=0;//提交的提取或追加明细的数量
			int count = 0;//不符合条件的可以新增提取或追加明细的数量
			String serno ="";
			KeyedCollection kColl = null;
			IndexedCollection iColl = null;
				TableModelDAO dao = this.getTableModelDAO(context);
				kColl = (KeyedCollection)context.getDataElement(modelId);
				if(null==kColl.getDataValue("serno")||"".equals(kColl.getDataValue("serno"))){
				    serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
				}else{
					serno = (String) kColl.getDataValue("serno");
				}
				if(context.containsKey("IqpBailSubDisDetailList")){
					iColl = (IndexedCollection) context.getDataElement("IqpBailSubDisDetailList");
					bfCount = iColl.size();
					if(bfCount!=0){
						KeyedCollection kc = new KeyedCollection();
						for(int i=0;i<iColl.size();i++){
							kc = (KeyedCollection) iColl.get(i);
							//如果追加或者提取的金额为0，则视为此保证金账号下没有做追加或者提取操作。
							if("0".equals(kc.getDataValue("adjust_amt"))){
								count++;
							}else{
								if(null==kColl.getDataValue("serno")||"".equals(kColl.getDataValue("serno"))){//新增（申请信息和明细信息）
									kc.setDataValue("serno",serno);
									kc.setName("IqpBailSubDisDetail");
									//新增追加申请或者提取申请
									dao.insert(kc, connection);
								}else if (null==kc.getDataValue("serno")||"".equals(kc.getDataValue("serno"))){//修改时的明细新增
									kc.setDataValue("serno",serno);
									kc.setName("IqpBailSubDisDetail");
									//更新追加申请或者提取申请明细
									dao.insert(kc, connection);
								}else{//修改时的明细修改
									kc.setName("IqpBailSubDisDetail");
									//更新追加申请或者提取申请明细
									dao.update(kc, connection);
								}
							}
						}
					}else{
						throw new EMPJDBCException("追加或者提取明细信息不能为空！");
					}
					if(null==kColl.getDataValue("serno")||"".equals(kColl.getDataValue("serno"))){
						kColl.setDataValue("serno",serno);
						//新增追加申请或者提取申请
						if(count==bfCount){
							context.addDataField("flag","fail");
						}else{
							context.addDataField("flag","success");
							dao.insert(kColl, connection);
						}
					}else{
						if(count==bfCount){
							context.addDataField("flag","fail");
						}else{
							context.addDataField("flag","success");
							kColl.remove("cont_amt");
							dao.update(kColl, connection);
						}
					}
				}else{
					KeyedCollection kCollPubBailInfo = (KeyedCollection)kColl.getDataElement("PubBailInfo");
					if(null==kCollPubBailInfo.getDataValue("serno")||"".equals(kCollPubBailInfo.getDataValue("serno"))){
						kCollPubBailInfo.put("serno",serno);
						kColl.put("serno",serno);
						dao.insert(kCollPubBailInfo, connection);
						dao.insert(kColl, connection);
						context.addDataField("flag", "success");
					}else{
						dao.update(kCollPubBailInfo, connection);
						dao.update(kColl, connection);
						context.addDataField("flag", "success");
					}
				}
		 context.put("serno",serno);
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
