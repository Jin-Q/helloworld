package com.yucheng.cmis.operation;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DuplicatedDataNameException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;

public class CMISShowTreeDic extends CMISOperation {
	
	private String addressTypeName = "DQBM";
	
	private String organnoName = "S_distno";
	
	private boolean dynamic = true;
	
	public String doExecute(Context context) throws EMPException {
		
		CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
		String dicTreeTypeId = (String) context.getDataValue("dicTreeTypeId");
		String parentNodeId = null;
		try{
			parentNodeId = (String) context.getDataValue("parentNodeId");
		}catch(Exception e){
		}
		String jsonStr = null;
		if(this.dynamic == false || parentNodeId == null){
			if(addressTypeName != null && addressTypeName.equals(dicTreeTypeId)){
				String organno = (String)context.getDataValue(organnoName);
				jsonStr = service.toJsonStringById(dicTreeTypeId, organno, this.dynamic);
			}else{
				jsonStr = service.toJsonStringByType(dicTreeTypeId, this.dynamic);
			}
			if(jsonStr == null){
				throw new EMPException("Can not find the dictree's json-string typeof["+dicTreeTypeId+"]!");
			}
		}else{
			jsonStr = service.toChildrenJsonStringById(dicTreeTypeId, parentNodeId);
			if(jsonStr == null){
				throw new EMPException("Can not find the dictree's json-string typeof["+dicTreeTypeId+":"+parentNodeId+"]!");
			}
		}
		//System.err.println("返回的json字符串是："+jsonStr);
		try {
			context.addDataField(CMISConstance.ATTR_RET_JSONDATANAME, jsonStr);
		} catch (DuplicatedDataNameException e) {
			context.setDataValue(CMISConstance.ATTR_RET_JSONDATANAME, jsonStr);
		}

		return "0";
	}


	public String getAddressTypeName() {
		return addressTypeName;
	}

	public void setAddressTypeName(String addressTypeName) {
		this.addressTypeName = addressTypeName;
	}

	public String getOrgannoName() {
		return organnoName;
	}

	public void setOrgannoName(String organnoName) {
		this.organnoName = organnoName;
	}
	
	public boolean isDynamic() {
		return dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}
}
