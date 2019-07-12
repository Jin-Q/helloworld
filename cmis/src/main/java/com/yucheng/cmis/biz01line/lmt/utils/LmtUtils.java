package com.yucheng.cmis.biz01line.lmt.utils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.DateUtil4CMIS;

/**
 * 额度申请工具类
 * @author 唐顺岩
 * @date 2013-07-18
 */
public class LmtUtils {
	/**
	 * 根据系统营业日期、期限时间类型、期限计算额度到期日
	 * @param openDate 系统营业日期
	 * @param term_type 期限时间类型
	 * @param term 期限
	 * @return 额度到期日期
	 */
	public static String computeEndDate(String openDate,String term_type,String term)throws EMPException{
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 String return_str = "";
		 try{
			 int term_int = 0;
			 if(null!= term && !"".equals(term)){
				 term_int = Integer.parseInt(term);  //期限转换为int类型
			 }else{
				 throw new EMPException("授信期限数据为空，请核实！"); 
			 }
			 openDate = openDate.replaceAll("-", "/");
			 Date date = new Date(openDate);
			 
			 if("001".equals(term_type)){  //期限时间类型为：年
				 return_str  = sdf.format(DateUtil4CMIS.addYears(date,term_int));
			 }
			 if("002".equals(term_type)){  //期限时间类型为：月
				 return_str  = sdf.format(DateUtil4CMIS.addMonths(date, term_int));
			 }
			 if("003".equals(term_type)){  //期限时间类型为：日
				 return_str  = sdf.format(DateUtil4CMIS.addDays(date, term_int));
			 }
		 }catch(Exception e){
			 throw new EMPException(e.getMessage());
		 }
		return return_str;
	}
	
	/**
	 * 将IndexedCollection 根据END_DATE进行END_DATE的升序排序 
	 * @param iColl 需要排序的IndexedCollection
	 * @return 排序好IndexedCollection
	 */
	public static IndexedCollection sort(IndexedCollection iColl){
		 Collections.sort(iColl, new Comparator<KeyedCollection>() {   
	            public int compare(KeyedCollection a, KeyedCollection b) {
	            	int return_int=0;
	              try {
					String one_end_date = a.getDataValue("end_date").toString();
					String two_end_date = b.getDataValue("end_date").toString();
					one_end_date = one_end_date.replaceAll("-", "/");
					two_end_date = two_end_date.replaceAll("-", "/");
					
					Date date_one = new Date(one_end_date);  //将到期日期转换为日期类型
					Date date_tow = new Date(two_end_date);
					long compute_value =date_one.getTime() - date_tow.getTime();  //得到两个到期日期想减值
					if(compute_value > 0){   //如果想减值大于0说明第一个大
						return_int = -1;   //返回时按降序排列
					}else if(compute_value < 0){
						return_int = 1;
					}else{
						return_int = 0;
					}
				} catch (ObjectNotFoundException e) {
					e.printStackTrace();
					// throw new EMPException("授信期限数据为空，请核实！"); 
				} catch (InvalidArgumentException e) {
					e.printStackTrace();
				}   
	              return return_int ;
	            }
	         }); 
		 return iColl;
	}
	
	/**计算授信启用金额
	 * 当授信项下所关联的担保合同总额小于或等于授信金额时，启用金额等于担保合同总额；
	 * 当授信项下所关联的担保合同总额大于授信金额时，启用金额等于授信金额
	 * */
	public static BigDecimal computLmtEnableAmt(String serno_value, KeyedCollection kColl_details, Context context, Connection connection, TableModelDAO dao) throws EMPException {
		EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------计算授信启用金额 开始---------------", null);
		BigDecimal eable_amt = new BigDecimal("0");
		try{
			String conditionStr = " WHERE LIMIT_CODE='"+kColl_details.getDataValue("org_limit_code")+"' AND SERNO='"+serno_value+"'";
			
			IndexedCollection iColl_RLmtAppGuarCont = dao.queryList("RLmtAppGuarCont", null,conditionStr,connection);
			//把担保合同编号拼装成一个String
			String guar_cont_no_str = "";
			for(int i=0;i<iColl_RLmtAppGuarCont.size();i++){
				KeyedCollection kColl_rlmtappguarcont = (KeyedCollection)iColl_RLmtAppGuarCont.get(i);
				String guar_cont_no = (String)kColl_rlmtappguarcont.getDataValue("guar_cont_no");
				guar_cont_no_str += "'"+guar_cont_no+"',";
			}
			IndexedCollection ContiColl =null;
			if(guar_cont_no_str.length()>1){
				guar_cont_no_str = guar_cont_no_str.substring(0, guar_cont_no_str.length()-1);
				/**调用担保模块接口*/
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				GrtServiceInterface service = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "grt");
				DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
				//获取所有授信关联担保合同
				ContiColl = service.getGuarContInfoList(guar_cont_no_str, null, dataSource);
				
				for (Iterator iterator2 = ContiColl.iterator(); iterator2.hasNext();) {
					KeyedCollection kColl_guar = (KeyedCollection) iterator2.next();
					if("01".equals(kColl_guar.getDataValue("guar_cont_state"))){ 
						//可用金额累加
						eable_amt = eable_amt.add(new BigDecimal((String)kColl_guar.getDataValue("guar_amt")));
					}
				}
				EMPLog.log("MESSAGE", EMPLog.DEBUG, 0, "***********已签订担保合同金额为："+eable_amt +"***********授信金额为："+kColl_details.getDataValue("crd_amt"));
				//累加后的担保合同金额跟授信金额比较取最小值
				if(eable_amt.compareTo(new BigDecimal((String)kColl_details.getDataValue("crd_amt")))>0){
					eable_amt = new BigDecimal((String)kColl_details.getDataValue("crd_amt"));
				}
			}
		}catch(Exception e){
			EMPLog.log("MESSAGE", EMPLog.INFO, 0, "根据授信与担保合同关系表计算授信启用金额错误，错误描述："+e.getMessage(), null);
			throw new EMPException("根据授信与担保合同关系表计算授信启用金额错误，错误描述："+e.getMessage());
		}
		EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------计算授信启用金额 结束---------------", null);
		return eable_amt;
	}
	
	public static void main(String[] args) {
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
