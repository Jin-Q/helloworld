package com.yucheng.cmis.biz01line.iqp.op.iqpbilldetail;

import java.sql.Connection;
import java.text.SimpleDateFormat;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckPorderEndDateOp extends CMISOperation {
	private final String batModel = "IqpBatchMng";
	private final String modelId = "IqpBatchBillRel";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			String porder_end_date = null;
			String fore_disc_date = null;
			String rebuy_date = null;
			String porder_no = null;
			String batch_no = null;
			String flag = null;
			String msg = null;
			String biz_type = null;
			
			porder_no = (String)context.getDataValue("porder_no");
			porder_end_date = (String)context.getDataValue("porder_end_date");//汇票到期日
			batch_no = (String)context.getDataValue("batch_no");//批次包
			if(porder_no == null || porder_no.length() == 0) {
				throw new EMPJDBCException("The value porder_no cannot be null!");
			}
			if(batch_no!=null && !"".equals(batch_no)){
				KeyedCollection kColl = dao.queryDetail(batModel, batch_no, connection);
				//取到预计转/贴现日期
				fore_disc_date = (String)kColl.getDataValue("fore_disc_date");
				biz_type = (String)kColl.getDataValue("biz_type");//业务类型
				if("04".equals(biz_type)){//卖出回购
					rebuy_date = (String)kColl.getDataValue("rebuy_date");//回购日期
				}
			}else{
				String condition = "where porder_no='"+porder_no+"' and batch_no in (select batch_no from Iqp_Batch_Mng where status <>'03')";
	            IndexedCollection iCollRel = dao.queryList(modelId, condition, connection);			
				if(iCollRel.size()>0){
					KeyedCollection kCollRel = (KeyedCollection)iCollRel.get(0);
					batch_no = (String)kCollRel.getDataValue("batch_no");
					KeyedCollection kColl = dao.queryDetail(batModel, batch_no, connection);
					//取到预计转/贴现日期
					fore_disc_date = (String)kColl.getDataValue("fore_disc_date");//预计转/贴现日期
					biz_type = (String)kColl.getDataValue("biz_type");//业务类型
					if("04".equals(biz_type)){//卖出回购
						rebuy_date = (String)kColl.getDataValue("rebuy_date");//回购日期
					}
				}
			}
			if(porder_end_date != null && !"".equals(porder_end_date) && fore_disc_date != null && !"".equals(fore_disc_date)){
				if(sdf.parse(porder_end_date).before(sdf.parse(fore_disc_date))){
					flag = "error";
					msg = "汇票到期日应大于预计转/贴现日期";
				}else{
					if("04".equals(biz_type)){
						if(porder_end_date != null && !"".equals(porder_end_date) && rebuy_date != null && !"".equals(rebuy_date)){
							if(sdf.parse(porder_end_date).before(sdf.parse(rebuy_date))){
								flag = "error";
								msg = "汇票到期日应大于回购日期";
							}else{
								flag = "success";
								msg = "通过";
							}
						}else{
							throw new Exception("回购日期或汇票到期日为空");
						}
					}else{
						flag = "success";
						msg = "通过";
					}
				}
			}
			context.addDataField("flag", flag);
			context.addDataField("msg", msg);
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
			context.addDataField("msg", ee.getMessage());
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
