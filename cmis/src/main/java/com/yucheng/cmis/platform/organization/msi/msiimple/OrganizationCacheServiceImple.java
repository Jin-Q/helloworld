package com.yucheng.cmis.platform.organization.msi.msiimple;

import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DataElement;
import com.ecc.emp.data.DataField;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.platform.organization.exception.OrganizationException;
import com.yucheng.cmis.platform.organization.initializer.OrganizationInitializer;
import com.yucheng.cmis.platform.organization.msi.OrganizationCacheServiceInterface;
import com.yucheng.cmis.pub.CMISModualService;

/**
 * 
 * 组织机构管理模块对外提供的缓存服务
 * <p>
 * 	提供机构、用户、岗位的ID-NAME的缓存服务
 * 
 * @author yuhq
 * @version 1.0
 * @since 1.0
 *
 */
public class OrganizationCacheServiceImple extends CMISModualService implements OrganizationCacheServiceInterface {

	/**
	 * 将iColl中的机构码转为机构名称
	 * <p>在IndexedCollection中新增key=args[i]_displayname,value=args[i]的机构名称的DataField
	 * 
	 * @param iColl IndexedCollection
	 * @param args iColl中放机构号的key
	 * @throws OrganizationException 异常
	 */
	public IndexedCollection addOrgName(IndexedCollection iColl, String[] args)throws OrganizationException{
		if(iColl == null) return iColl;
		if(OrganizationInitializer.orgsMap == null) return iColl;
		
		try {
			this.dealAddName(iColl, args, OrganizationInitializer.orgsMap);
		} catch (Exception e) {
			throw new OrganizationException(e);
		}
		
		return iColl;
		
	}
	
	
	/**
	 * 
	 * 将kColl中的机构码转为机构名称
	 * <p>在KeyedCollection中新增key=args[i]_displayname,value=args[i]的机构名称的DataField
	 * 
	 * @param iColl IndexedCollection
	 * @param args iColl中放机构号的key
	 * @throws OrganizationException 异常
	 */
	public KeyedCollection addOrgName(KeyedCollection kColl, String[] args)throws OrganizationException{
		if(kColl == null) return kColl;
		if(OrganizationInitializer.orgsMap == null) return kColl;
		
		try {
			this.dealAddName(kColl, args, OrganizationInitializer.orgsMap);
		} catch (Exception e) {
			throw new OrganizationException(e);
		}
		
		return kColl;
	}
	
	/**
	 * 将iColl中的用户码转为用户名称
	 * <p>在IndexedCollection中新增key=args[i]_displayname,value=args[i]的用户名称的DataField
	 * 
	 * @param iColl IndexedCollection
	 * @param args iColl中放用户码的key
	 * @throws OrganizationException 异常
	 */
	public IndexedCollection addUserName(IndexedCollection iColl, String[] args)throws OrganizationException{
		if(iColl == null) return iColl;
		if(OrganizationInitializer.usersMap == null) return iColl;
		
		try {
			this.dealAddName(iColl, args, OrganizationInitializer.usersMap);
		} catch (Exception e) {
			throw new OrganizationException(e);
		}
		
		return iColl;
		
	}
	
	/**
	 * 将kColl中的用户码转为用户名称
	 * <p>在KeyedCollection中新增key=args[i]_displayname,value=args[i]的用户名称的DataField
	 * 
	 * @param iColl IndexedCollection
	 * @param args iColl中放用户码的key
	 * @throws OrganizationException 异常
	 */
	public KeyedCollection addUserName(KeyedCollection kColl, String[] args) throws OrganizationException{
		if(kColl == null) return kColl;
		if(OrganizationInitializer.usersMap == null) return kColl;
		
		try {
			this.dealAddName(kColl, args, OrganizationInitializer.usersMap);
		} catch (Exception e) {
			throw new OrganizationException(e);
		}
		
		return kColl;
	}
	
	/**
	 * 将iColl中的岗位码转为岗位名称
	 * 
	 * 在IndexedCollection中新增key=args[i]_displayname,value=args[i]的岗位名称的DataField
	 * 
	 * @param iColl IndexedCollection
	 * @param args iColl中放岗位码的key
	 * @throws OrganizationException 异常
	 */
	public IndexedCollection addDutyName(IndexedCollection iColl, String[] args)throws OrganizationException{
		if(iColl == null) return iColl;
		if(OrganizationInitializer.dutysMap == null) return iColl;
		
		try {
			this.dealAddName(iColl, args, OrganizationInitializer.dutysMap);
		} catch (Exception e) {
			throw new OrganizationException(e);
		}
		
		return iColl;
	}
	
	/**
	 * 将kColl中的岗位码转为岗位名称
	 * 
	 * 在KeyedCollection中新增key=args[i]_displayname,value=args[i]的岗位名称的DataField
	 * 
	 * @param iColl IndexedCollection
	 * @param args iColl中放岗位码的key
	 * @throws OrganizationException 异常
	 */
	public KeyedCollection addDutyName(KeyedCollection kColl, String[] args)throws OrganizationException{
		if(kColl == null) return kColl;
		if(OrganizationInitializer.dutysMap == null) return kColl;
		
		try {
			this.dealAddName(kColl, args, OrganizationInitializer.dutysMap);
		} catch (Exception e) {
			throw new OrganizationException(e);
		}
		
		return kColl;
	}
	
	/**
	 * 将iColl中含 arg的所有DataField转为中文
	 * @param icol IndexedCollection
	 * @param arg DataField中待转的key
	 * @param map Key-Value
	 * @throws EMPException
	 */
	private static void dealAddName(IndexedCollection iCol, String[] arg, Map<String, String> map) throws Exception {
		for (int i = 0; i < iCol.size(); i++) {
			KeyedCollection kCol = (KeyedCollection) iCol.get(i);
			for (int j = 0; j < kCol.size(); j++) {
				DataElement element = (DataElement) kCol.getDataElement(j);
				if (element instanceof DataField) {
					DataField aField = (DataField) element;
					for (int k = 0; k < arg.length; k++) {
						if (arg[k].equals(aField.getName())) {
							String value = map.get((String) (aField.getValue()));
							kCol.addDataField(arg[k]+ "_displayname", value!=null?value : aField.getValue());
						}
					}
				}
			}
		}
	}
	
	/**
	 * 将kColl中含 arg的所有DataField转为中文
	 * @param kColl KeyedCollection
	 * @param arg DataField中待转的key
	 * @param map Key-Value
	 * @throws EMPException
	 */
	private static void dealAddName(KeyedCollection kColl, String[] arg, Map<String, String> map) throws Exception {
		for (int j = 0; j < kColl.size(); j++) {
			DataElement element = (DataElement) kColl.getDataElement(j);
			if (element instanceof DataField) {
				DataField aField = (DataField) element;
				for (int k = 0; k < arg.length; k++) {
					if (arg[k].equals(aField.getName())) {
						String value = map.get((String) (aField.getValue()));
						kColl.addDataField(arg[k]+ "_displayname", value);
					}
				}
			}
		}
	}
	
	
	
	
	
	/**
	 * 根据用户码获得用户名
	 * @param userId 用户码
	 * @return 用户名
	 * @throws OrganizationException
	 */
	public String getUserName(String userId)throws OrganizationException{
		if(OrganizationInitializer.isInit)
			return OrganizationInitializer.usersMap.get(userId);


		return null;
	}
	
	
	/**
	 * 根据机构码获得用户名
	 * @param userId 机构码
	 * @return 机构名
	 * @throws OrganizationException
	 */
	public String getOrgName(String orgId)throws OrganizationException{
		if(OrganizationInitializer.isInit)
			return OrganizationInitializer.orgsMap.get(orgId);


		return null;
	}
	
	
	/**
	 * 根据岗位码获得用户名
	 * @param userId 岗位码
	 * @return 岗位名
	 * @throws OrganizationException
	 */
	public String getDutyName(String dutyId)throws OrganizationException{
		if(OrganizationInitializer.isInit)
			return OrganizationInitializer.dutysMap.get(dutyId);
		
		return null;
	}

}
