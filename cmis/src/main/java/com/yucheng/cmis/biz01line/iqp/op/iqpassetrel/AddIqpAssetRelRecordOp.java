package com.yucheng.cmis.biz01line.iqp.op.iqpassetrel;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class AddIqpAssetRelRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpAssetRel";
	private final String modelIdMain = "IqpAsset";
	private final String modelIdIqp = "IqpAssetstrsf";
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			String asset_no ="";
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
				asset_no = (String)kColl.getDataValue("asset_no");
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			String condition ="where asset_no='"+asset_no+"'";
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			/**add by lisj 2014-12-3  校验存量资产清单数据的币种是否一致  begin**/
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			String stockCurType="";
			String cur_Type =(String)kColl.getDataValue("cur_type");
			if(cur_Type!=null && !"".equals(cur_Type)){
			String conSelect ="select cur_type from Iqp_Asset_Rel iar where iar.asset_no='"+asset_no+"'";
			IndexedCollection iCollSelect4CurType = TableModelUtil.buildPageData(null, dataSource, conSelect);
				if(iCollSelect4CurType !=null && iCollSelect4CurType.size()>0){
					KeyedCollection kCollSelect = (KeyedCollection)iCollSelect4CurType.get(0);
					stockCurType =(String) kCollSelect.getDataValue("cur_type");
				}
			}
			if(stockCurType == null || "".equals(stockCurType) || cur_Type.equals(stockCurType)){
				dao.insert(kColl, connection);
			
			/**
			 * 查资产数量，更新到对应资产包中
			 * 补充:新增资产清单后，后台自动计算资产包中的资产数量、资产总额和转让总额。
			 * 备注：资产包中的资产总额即为资产清单中贷款余额总额。
			 *       转让时，一般都是按照贷款余额全额转让。
			 */
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			double astAmtTotal = 0;//资产总额
		    double tableoverAmtTotal = 0;//转让总额
		    double takeoverTotalInt = 0;//转让利息
			if(iColl != null && iColl.size() > 0){
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kc = (KeyedCollection)iColl.get(i);
					String loanbalTotal = (String)kc.getDataValue("loan_bal");
					String takeoverAmt = (String)kc.getDataValue("takeover_amt");
					String takeoverInt = (String)kc.getDataValue("takeover_int");
					if(takeoverInt == null){
						takeoverInt = "0";
					}
					astAmtTotal += Double.parseDouble(loanbalTotal);
					tableoverAmtTotal += Double.parseDouble(takeoverAmt);
					takeoverTotalInt += Double.parseDouble(takeoverInt);
				}
			}
			KeyedCollection IqpAssetKColl = dao.queryDetail(modelIdMain, asset_no, connection);
			IqpAssetKColl.setDataValue("asset_qnt",iColl.size()); 
			IqpAssetKColl.setDataValue("asset_total_amt", astAmtTotal);//资产总额
			IqpAssetKColl.setDataValue("takeover_total_amt", tableoverAmtTotal);//转让总额
			IqpAssetKColl.setDataValue("takeover_total_int", takeoverTotalInt);//转让利息
			dao.update(IqpAssetKColl, connection);
			
			//实时更新业务申请中的信息
			IndexedCollection IqpIColl = dao.queryList(modelIdIqp, "where asset_no = '"+asset_no+"'", connection);
			if(IqpIColl.size()>0){
				KeyedCollection IqpKColl = (KeyedCollection)IqpIColl.get(0);
				IqpKColl.put("takeover_qnt",iColl.size()); //转让笔数
				IqpKColl.put("asset_total_amt", astAmtTotal);//资产总额
				IqpKColl.put("takeover_total_amt", tableoverAmtTotal);//转让总额
				IqpKColl.put("takeover_int", takeoverTotalInt);//转让利息
				dao.update(IqpKColl, connection);
			}
				context.addDataField("flag", "success");
			}else{
				context.addDataField("flag", "CurTypeInConf");
			}
			/**add by lisj 2014-12-3 校验存量资产清单数据的币种是否一致  end**/
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
