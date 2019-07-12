package com.yucheng.cmis.biz01line.esb.interfaces.imple;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import sun.net.www.http.HttpClient;

import com.ecc.emp.data.KeyedCollection;

public class EsbTest {
	public static void main(String[] args) throws Exception    {
		  
		String Json = "{"+
			"\"SYS_HEAD\": {"+
				"\"SvcCd\": \"40120002\","+
				"\"ScnCd\": \"01\","+
				"\"CnsmrCnlTp\": \"消费方渠道类型\","+
				"\"CnsmrSysId\": \"300200\","+
				"\"CnsmrSysSrlNo\": \"1170328160403023\","+
				"\"PvdrSysId\": \"400400\","+
				"\"PvdrSysSrlNo\": \"1170328160403023\","+
				"\"PvdrSrvInd\": \"提供方服务器标识\","+
				"\"SrcCnsmrSysId\": \"源消费方系统编号\","+
				"\"GlblSeqNo\": \"40040020190118173511123456789012\","+
				"\"TxnDt\": \"20190118\","+
				"\"TxnTm\": \"173511\","+
				 
				"\"FileFlg\": \"0\","+
						"\"FileNm\": \"文件名称\","+
				"\"SvcVerNo\": \"服务版本号\","+
				"\"TmlNo\": \"终端号\","+
				"\"MACVal\": \"MAC值\""+
			"},"+
			"\"APP_HEAD\": {"+
				"\"BrCd\": \"机构代码\","+
						"\"TlrNo\": \"柜员号\","+
						"\"TurnPgFlg\": \"翻页标志\","+
						"\"PgDsplLineNum\": \"每页显示条数\","+
						"\"TotLineNum\": \"总笔数\","+
						"\"SmzgFlg\": \"汇总标志\""+
							"},"+
			"\"BODY\": {"+
				"\"BsnSrlNo\": \"EDSQ201900016442\","+
						"\"CstNo\": \"9527\","+
						"\"CstNm\": \"榛勬稕\","+
						"\"IdentTp\": \"1\","+
						"\"IdentNo\": \"360102199307030038\","+
						"\"PdNo\": \"\","+
						"\"ModlNo\": \"\","+
						"\"BsnStgTp\": \"01\""+
			"}"+
		"}"; 
		
		String result = "";

        BufferedReader reader = null;

        try {

            URL url = new URL("http://172.16.200.1:8082/rcmp/RCMP_HTTP_ESB/");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");

            conn.setDoOutput(true);

            conn.setDoInput(true);

            conn.setUseCaches(false);

            conn.setRequestProperty("Connection", "Keep-Alive");

            conn.setRequestProperty("Charset", "UTF-8");

            // 设置文件类型:

            conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");

            // 设置接收类型否则返回415错误

            //conn.setRequestProperty("accept","*/*")此处为暴力方法设置接受所有类型，以此来防范返回415;

            conn.setRequestProperty("accept","application/json");

            // 往服务器里面发送数据

          
                byte[] writebytes = Json.getBytes();

                // 设置文件长度

                conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));

                OutputStream outwritestream = conn.getOutputStream();

                outwritestream.write(Json.getBytes());

                outwritestream.flush();

                outwritestream.close();

           

            if (conn.getResponseCode() == 200) {

                reader = new BufferedReader(

                        new InputStreamReader(conn.getInputStream()));

                result = reader.readLine();

            }
            System.out.println(result);

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

         

        }
		
		
    }	
}
