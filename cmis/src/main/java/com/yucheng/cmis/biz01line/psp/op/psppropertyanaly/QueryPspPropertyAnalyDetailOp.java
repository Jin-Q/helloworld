package com.yucheng.cmis.biz01line.psp.op.psppropertyanaly;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryPspPropertyAnalyDetailOp  extends CMISOperation {
	
	private final String modelId = "PspPropertyAnaly";
	private final String modelIdReal = "PspPropertyRealpro";
	private final String modelIdEquip = "PspPropertyEquip";
	private final String modelIdTraffic = "PspPropertyTraffic";
	private final String modelIdLogo = "PspPropertyLogo";
	private final String modelIdPatent = "PspPropertyPatent";
	private final String modelIdStock = "PspPropertyStockright";
	private final String modelIdSea = "PspPropertySearight";
	private final String modelIdForest = "PspPropertyForestright";

	private final String pk_id_name = "property_id";
	
	private boolean updateCheck = false;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String returnFlag = null;
		try{
			connection = this.getConnection(context);
			String property_type = null;
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String pk_id_value = null;
			try {
				pk_id_value = (String)context.getDataValue(pk_id_name);
			} catch (Exception e) {}
			if(pk_id_value == null || pk_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk_id_name+"] cannot be null!");
			
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, pk_id_value, connection);
			KeyedCollection kCollDet = null;
			
			property_type = (String)kColl.getDataValue("property_type");
			if("11".equals(property_type)||"12".equals(property_type)||"13".equals(property_type)){//房地产
				returnFlag = "realPro";
				kCollDet = dao.queryDetail(modelIdReal, pk_id_value, connection);
			}else if("20".equals(property_type)){//设备及器材
				returnFlag = "equip";
				kCollDet = dao.queryDetail(modelIdEquip, pk_id_value, connection);
			}else if("30".equals(property_type)){//交通工具
				returnFlag = "traffic";
				kCollDet = dao.queryDetail(modelIdTraffic, pk_id_value, connection);
			}else if("40".equals(property_type)){//商标使用权
				returnFlag = "logo";
				kCollDet = dao.queryDetail(modelIdLogo, pk_id_value, connection);
			}else if("50".equals(property_type)){//专利使用权
				returnFlag = "patent";
				kCollDet = dao.queryDetail(modelIdPatent, pk_id_value, connection);
			}else if("60".equals(property_type)){//股权
				returnFlag = "stock";
				kCollDet = dao.queryDetail(modelIdStock, pk_id_value, connection);
			}else if("70".equals(property_type)){//海域使用权
				returnFlag = "sea";
				kCollDet = dao.queryDetail(modelIdSea, pk_id_value, connection);
			}else if("80".equals(property_type)){//林权
				returnFlag = "forest";
				kCollDet = dao.queryDetail(modelIdForest, pk_id_value, connection);
			}
			
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"input_br_id"});
			
			this.putDataElement2Context(kColl, context);
			this.putDataElement2Context(kCollDet, context);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return returnFlag;
	}
}
