package com.yucheng.cmis.biz01line.cus.interfaces.impl;


import com.yucheng.cmis.biz01line.cus.group.component.CusGrpInfoComponent;
import com.yucheng.cmis.biz01line.cus.group.component.CusGrpMemberComponent;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpInfo;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpMember;
import com.yucheng.cmis.biz01line.cus.interfaces.CusGrpIface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.PUBConstant;
 
public class CusGrpIfaceImpl extends CMISComponent implements CusGrpIface {


	public CusGrpInfo getCusGrpInfo(String grpNo) throws Exception {
		CusGrpInfoComponent cusGrpInfoComponent = (CusGrpInfoComponent)this.getOtherComponentInstance(PUBConstant.CUSGRPINFO);
		return cusGrpInfoComponent.getCusGrpInfoDomainByGrpNo(grpNo);
	}
	
	public boolean checkIsGrpMember(String cusId) throws Exception{
		CusGrpMemberComponent CusGrpMemberComponent = (CusGrpMemberComponent)this.getOtherComponentInstance(PUBConstant.CUSGRPMEMBER);
		CusGrpMember cgMember = (CusGrpMember)CusGrpMemberComponent.cheakCusGrpMember(cusId);
		if(cgMember.getCusId()==null||"".equals(cgMember.getCusId().trim())){
			return false;
		}else{
			return true;
		}
	}
	
	public String getIsGrpMember(String cusId) throws Exception{
		CusGrpMemberComponent CusGrpMemberComponent = (CusGrpMemberComponent)this.getOtherComponentInstance(PUBConstant.CUSGRPMEMBER);
		CusGrpMember cgMember = (CusGrpMember)CusGrpMemberComponent.cheakCusGrpMember(cusId);
		if(cgMember.getCusId()==null||"".equals(cgMember.getCusId().trim())){
			return "";
		}else{
			return cgMember.getGrpNo();
		}
	}
	
}
