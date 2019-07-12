package com.yucheng.cmis.platform.workflow.demo.component;

import com.yucheng.cmis.platform.workflow.demo.dao.WfiDemoDao;
import com.yucheng.cmis.platform.workflow.domain.WfiDemo;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * 流程示例组件类
 * @author liuhw
 *
 */
public class WfiDemoComponent extends CMISComponent {

	public boolean addWfiDemo(WfiDemo wfiDemo) throws ComponentException {
		boolean result = false;
		WfiDemoDao dao = (WfiDemoDao) this.getDaoInstance("wfiDemoDao");
		int count = dao.addWfiDemo(wfiDemo);
		if(count == 1)
			result = true;
		return result;
	}
}
