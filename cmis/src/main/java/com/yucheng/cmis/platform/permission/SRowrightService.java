package com.yucheng.cmis.platform.permission;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.dbmodel.EMPRestrictException;
import com.ecc.emp.dbmodel.TableModel;
import com.ecc.emp.dbmodel.TableModelField;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.service.TableModelLoader;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.exception.OrganizationException;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.permission.domain.SRowright;
import com.yucheng.cmis.platform.permission.initializer.PermissionInitializer;
import com.yucheng.cmis.pub.CMISConstant;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class SRowrightService extends RecordRestrict {

	public String judgeQueryRestrict(String modelId, String condition,
			Context context, Connection con) throws EMPRestrictException {
		try {
			condition = this.getRestrict(context, con, this.dao, modelId,
					condition, PermissionContents.READ);
		} catch (EMPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new EMPRestrictException(e.getMessage());
		}
		return condition;

	}

	public String judgeDeleteRestrict(String modelId, Context context,
			Connection con) throws EMPRestrictException {
		if (context != null) {
			try {
				if (context.containsKey("updateCheck")
						&& context.getDataValue("updateCheck") != null
						&& !(((Boolean) context.getDataValue("updateCheck"))
								.booleanValue())) {
					/**
					 * 
					 * action.xml中配置了updateCheck=true的mvc请求才检查
					 */
					EMPLog.log(EMPConstance.EMP_MVC, EMPLog.INFO, 0,
							"没有配置updateCheck或者updateCheck=false");
					return "";
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			this.getRestrict(context, con, this.dao, modelId, "",
					PermissionContents.WRITE);
		} catch (EMPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new EMPRestrictException(e.getMessage());
		}
		return "";
	}

	public String judgeUpdateRestrict(String modelId, Context context,
			Connection con) throws EMPRestrictException {
		if (context != null) {
			try {
				//if (context.containsKey("updateCheck") && context.getDataValue("updateCheck") != null && !(((Boolean) context.getDataValue("updateCheck")).booleanValue())) {
				if (null == context.getDataValue("updateCheck") || context.getDataValue("updateCheck").equals("false")) {
					/** action.xml中配置了updateCheck=true的mvc请求才检查  */
					EMPLog.log(EMPConstance.EMP_MVC, EMPLog.INFO, 0,
							"没有配置updateCheck或者updateCheck=false");
					return "";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			//如果为查看操作并且，记录集权限控制为否时不校验
//			if(!(context.containsKey("restrict_tab") && "view".equalsIgnoreCase((String)context.getDataValue("op")))){
			if(context.containsKey("restrict_tab") || (context.containsKey("op")&&"view".equalsIgnoreCase((String)context.getDataValue("op")))){
				//若含有restrict_tab或者op为view则不校验记录集权限
			}else{
//				if("view".equalsIgnoreCase((String)context.getDataValue("op"))){
//					this.getRestrict(context, con, this.dao, modelId, "",PermissionContents.READ);
//				}else{
//					this.getRestrict(context, con, this.dao, modelId, "",PermissionContents.WRITE);
//				}
				this.getRestrict(context, con, this.dao, modelId, "",PermissionContents.WRITE);
			}
		} catch (EMPException e) {
			e.printStackTrace();
			throw new EMPRestrictException(e.getMessage());
		}
		return "";
	}

	/*
	 * context中参数获取数值
	 */
	public String getTempValue(Context context, Connection connection, String temp) throws EMPException {
		if ((temp == null) || (temp.trim().equals(""))){
			return "";
		}
		String tempValue = "";
		if (context.containsKey(temp)){
			tempValue = (String) context.getDataValue(temp);
			//记录集权限，岗位为空处理
			if(temp.equals("dutys")&&"".equals(tempValue)){
				tempValue = "''";
			}
		}
		if (temp.equals("S_orgchilds")) {// 本机构以及下级机构的字符串 在加载机构时产生

			String s_organno = (String) context.getDataValue("organNo");
			// 调用组织机构管理模块对外提供的服务：取得机构向下的所有子机构
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory
					.getInstance();
			OrganizationServiceInterface orgService = null;
			try {
				orgService = (OrganizationServiceInterface) serviceJndi
						.getModualServiceById("organizationServices",
								CMISConstant.ORGANIZATION_MODUAL_ID);
			} catch (Exception e) {
				EMPLog.log(CMISConstance.CMIS_PERMISSION, EMPLog.ERROR, 0,
						"getTempValue error!", e);
				throw new EMPException(e);
			}
			//tempValue = (String) context.getDataValue("organNo");
			//tempValue = orgService.getOrganchilds(s_organno);
			try {
				List<SOrg> sorgList = orgService.getAllSubOrgs(s_organno, connection);
				for(int i=0;i<sorgList.size();i++){
					SOrg sorg = sorgList.get(i);
					tempValue +=  "'"+sorg.getOrganno()+"',";
				}
				tempValue = tempValue.substring(0,tempValue.length()-1);
			} catch (OrganizationException e) {
				e.printStackTrace();
			}
		}
		return tempValue;
	}

	/*
	 * desp:根据menuId 、当前用户s_user表中的actorright 获取记录集权限 组合在condition条件中
	 * 请放在op中condition的最后一步 context op传入 connection op传入 dao op传入 modelId
	 * 资源对应表模型 condition 原查询条件 restrictType 权限类型 PUBConstant.READ 读
	 * 、PUBConstant.WRITE 写 权限模板的写法 机构、用户、本机构及下级机构 input_br_id='$S_organno$' and
	 * input_id='$currentUserId$' and input_br_id in($S_orgchilds$)
	 * 
	 * 返回中存在 "WHERE"
	 */
	public String getRestrict(Context context, Connection connection,
			TableModelDAO dao, String modelId, String condition,
			String restrictType) throws EMPException {
		String rightTemp;
		if ((restrictType == null) || (restrictType.trim().equals("")))
			throw new CMISException("请传入记录集权限类型");
		String menuId = null;

		menuId = getTempValue(context, connection, "menuId");
		SRowright sRowright = PermissionInitializer.SROWRIGHTMAP.get(menuId);
		if (sRowright == null) {
			if (condition == null || condition.trim().equals("")) {
				condition = " WHERE 1=1 ";
			} else {
				String conditionUp = condition.toUpperCase();
				int local = conditionUp.indexOf("WHERE");
				if (local >= 0) {
					condition = condition.substring(local + 5, conditionUp
							.length());
					condition = " WHERE (1=1) and " + condition;
				} else if (conditionUp.trim().startsWith("ORDER")
						|| conditionUp.trim().startsWith("AND")) {
					condition = " WHERE (1=1) " + condition;
				} else {
					condition = " WHERE (1=1) and " + condition;
				}
			}

			return condition;
		}

		if (restrictType.trim().equals("READ"))
			rightTemp = sRowright.getReadtemp();
		else if (restrictType.trim().equals("WRITE"))
			rightTemp = sRowright.getWritetemp();
		else {
			throw new CMISException("无效的记录集权限类型【" + restrictType + "】");
		}

		String _condiStr = rightTemp;
		int beg = -1;
		int end = -1;
		while ((rightTemp != null) && (rightTemp.lastIndexOf("$") > end)) {
			beg = rightTemp.indexOf("$", end + 1);
			end = rightTemp.indexOf("$", beg + 1);
			String var = rightTemp.substring(beg + 1, end);

			String varValue = getTempValue(context, connection, var);
			_condiStr = _condiStr.replaceAll("\\$" + var + "\\$", varValue);
		}

		if (_condiStr != null && !_condiStr.trim().equals("")) {
			if ((condition != null) && (!(condition.trim().equals("")))) {
				String conditionUp = condition.toUpperCase();
				int local = conditionUp.indexOf("WHERE");
				if (local >= 0) {
					condition = condition.substring(local + 5, conditionUp
							.length());
					condition = " WHERE ( " + _condiStr + " ) and " + condition;
				} else if (conditionUp.trim().startsWith("ORDER")
						|| conditionUp.trim().startsWith("AND")) {
					condition = " WHERE ( " + _condiStr + " ) " + condition;
				} else
					condition = " WHERE ( " + _condiStr + " ) and " + condition;
			} else {
				condition = " WHERE ( " + _condiStr + " ) ";
			}
		}

		if (restrictType.trim().equals("WRITE")) {// 写权限判断
			judgeWriteRestrict(context, modelId, condition, connection, dao);
		}
		return condition;
	}

	/*
	 * desp：如果是写权限需要根据权限模板和表pk进行判断 如果查询结果不为空则可以操作 反正不可以，直接抛出异常到页面
	 */
	private boolean judgeWriteRestrict(Context context, String modelId,
			String condition, Connection connection, TableModelDAO dao)
			throws CMISException, EMPJDBCException, ObjectNotFoundException,
			InvalidArgumentException {
		TableModelLoader modelLoader = (TableModelLoader)context.getService(CMISConstance.ATTR_TABLEMODELLOADER);
		TableModel model = modelLoader.getTableModel(modelId);
		if (model == null)
			throw new CMISException("【" + modelId + "】表模型不存在!");

		// 根据表模型配置的pk从当前context中表模型KCOLL或者pk参数字段获取pk值
		KeyedCollection modelKcoll = null;

		try {
			modelKcoll = (KeyedCollection) context.getDataElement(modelId);
		} catch (Exception e) {
		}

		/** 在不是查询操作时，用主键取当前等待操作的一条记录 */

		HashMap pk_values = new HashMap();
		Iterator it = model.getModelFields().values().iterator();
		String pk_one = "";
		while (it.hasNext()) {
			if (condition == null || condition.trim().equals(""))
				condition = "Where 1=1 ";
			TableModelField field = (TableModelField) it.next();
			if (!field.isPK())
				continue;
			String pk_value = null;
			try {
				if (modelKcoll == null)
					pk_value = (String) context.getDataValue(field.getId());
				else
					pk_value = (String) modelKcoll.getDataValue(field.getId());
			} catch (Exception e) {
			}
			if (pk_value == null)
				throw new CMISException("[" + modelId + "]表模型主键["
						+ field.getId() + "]值为空!");
			else {
				pk_values.put(field.getId(), pk_value);
				if (field.isCharType())
					condition += " and " + field.getId() + "='" + pk_value
							+ "'";
				else
					condition += " and " + field.getId() + "=" + pk_value + "";
				pk_one = field.getId();
			}
		}

		KeyedCollection kColl = dao.queryFirst(modelId, null, condition,
				connection);
		if (kColl == null || kColl.getDataValue(pk_one) == null
				|| kColl.getDataValue(pk_one).equals(""))
			throw new CMISException("当前用户没有权限操作此记录!");
		return true;
	}

}