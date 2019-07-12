package com.yucheng.cmis.biz01line.ind.msi.msiimple;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.ind.component.IndComponent;
import com.yucheng.cmis.biz01line.ind.msi.IndServiceInterface;
import com.yucheng.cmis.pub.CMISModualService;
import com.yucheng.cmis.pub.dao.SqlClient;

public class IndServiceImple extends CMISModualService implements IndServiceInterface {

	public KeyedCollection getIndGroup(String groupno, Connection con)
			throws Exception {
		KeyedCollection indgroup = null;
		try {
			indgroup = (KeyedCollection) SqlClient.queryFirst("queryIndGroupBygroupno", groupno,null,
					con);
		} catch (Exception e) {
			throw new Exception(e);
		}

		return indgroup;
	}

	public KeyedCollection getIndModel(String modelno, Connection con)
			throws Exception {
		KeyedCollection indgroup = null;
		try {
			indgroup = (KeyedCollection) SqlClient.queryFirst("queryIndModelBymodelno", modelno,null,
					con);
		} catch (Exception e) {
			throw new Exception(e);
		}

		return indgroup;
	}

	public ArrayList<HashMap> queryGroupIndexesWithShuffle(Map map)
			throws Exception {
		IndComponent indcom=new IndComponent();
		return indcom.queryGroupIndexesWithShuffle(map);
	}

}
