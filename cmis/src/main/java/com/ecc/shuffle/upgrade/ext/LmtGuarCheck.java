package com.ecc.shuffle.upgrade.ext;

import java.util.Map;

public class LmtGuarCheck implements ShuffleExtIF{

	public LmtGuarCheck(){    }
	
	public Object doExecute(Map paramMap, Map resourceMap, Map valueMap) throws Exception {
		System.out.println((new StringBuilder("paramMap=")).append(paramMap).toString());
        System.out.println((new StringBuilder("valueMap=")).append(valueMap).toString());
        System.out.println((new StringBuilder("resourceMap=")).append(resourceMap).toString());
		return null;
	}

}
