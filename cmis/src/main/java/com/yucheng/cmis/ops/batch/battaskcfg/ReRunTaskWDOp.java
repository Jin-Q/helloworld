package com.yucheng.cmis.ops.batch.battaskcfg;

import java.io.IOException;
import java.util.ResourceBundle;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DuplicatedDataNameException;
import com.ecc.emp.data.InvalidArgumentException;
import com.runqian.base4.util.Logger;
import com.yucheng.cmis.operation.CMISOperation;

public class ReRunTaskWDOp extends CMISOperation {
	
	/**
	 * 远程调用shell脚本执行网贷跑批任务
	 */
	public String doExecute(Context context) throws EMPException {
		try {
			ResourceBundle res = ResourceBundle.getBundle("cmis");
			String shellPath = "";
			shellPath = res.getString("batch.wangdai.path");//配置在cmis中的网贷获取shell脚本的路径
			Process process;
			process = Runtime
					.getRuntime()
					.exec(shellPath);
					//.exec("ssh root@20.128.1.177 sh /app/appopt/clpm_daybat/bin/continue.sh");
			//测试使用
			//.exec("ssh root@20.128.1.177 sh /app/hello.sh > /app/hello.txt");
			int ret = process.waitFor();
			if (ret == 0) {
				Logger.info("跑批成功");
				context.addDataField("flag", "success");
			}else{
				Logger.info("跑批失败");
				context.addDataField("flag", "failed");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		} catch (DuplicatedDataNameException e) {
			e.printStackTrace();
		}
		return "0";
	}
}
