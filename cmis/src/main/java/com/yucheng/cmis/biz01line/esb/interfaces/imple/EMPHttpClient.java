/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.yucheng.cmis.biz01line.esb.interfaces.imple;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DataElement;
import com.ecc.emp.data.DataElementSerializer;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.format.EMPFormatException;
import com.ecc.emp.format.FormatElement;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;

/** @deprecated */
public class EMPHttpClient {
	private String cookies = null;
	private String retCode;
	private char delim = '|';

	private boolean sessionEstablished = false;
	private String hostAddr;
	private String sessionId = "-1";
	private String reqURI;

	public DataElement establishSession(DataElement reqDataElement,
			String serviceId) throws Exception {
		return establishSession(reqDataElement, null, serviceId);
	}

	public DataElement establishSession(DataElement reqDataElement,
			FormatElement formatElement, String serviceId) throws Exception {
		this.sessionEstablished = false;
		DataElement element = executeServerHttpService(reqDataElement,
				formatElement, serviceId);
		if ("0".equals(this.retCode)) {
			this.sessionEstablished = true;
		}
		return element;
	}

	public DataElement endSession(DataElement reqDataElement, String serviceId)
			throws Exception {
		return endSession(reqDataElement, null, serviceId);
	}

	public DataElement endSession(DataElement reqDataElement,
			FormatElement formatElement, String serviceId) throws Exception {
		DataElement element = executeServerHttpService(reqDataElement,
				formatElement, serviceId);

		if ("0".equals(this.retCode)) {
			this.sessionEstablished = false;
		}
		return element;
	}

	public DataElement executeServerHttpService(DataElement reqDataElement,
			String serviceId) throws Exception {
		return executeServerHttpService(reqDataElement, null, serviceId);
	}

	public DataElement executeServerHttpService(DataElement reqDataElement,
			FormatElement formatElement, String serviceId) throws Exception {
		URL url = new URL(this.hostAddr + this.reqURI);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		if (this.cookies != null) {
			connection.setRequestProperty("cookie", this.cookies);
		}
		connection
				.setRequestProperty("content-type", "application/json; charset=utf-8");

		connection.setDoInput(true);
		connection.setDoOutput(true);

		connection.setRequestMethod("POST");
		OutputStream out = connection.getOutputStream();

		String reqData = getRequestData(reqDataElement, formatElement,
				serviceId);
		out.write(reqData.getBytes("UTF-8"));

		int retCode = connection.getResponseCode();
		if (retCode != 200) {
			throw new EMPException("Server not available! retCode=" + retCode);
		}

		Map heads = connection.getHeaderFields();
		Object[] headNames = heads.keySet().toArray();
		for (int i = 0; i < headNames.length; ++i) {
			if (headNames[i] == null) {
				continue;
			}
			if ("Set-Cookie".equalsIgnoreCase((String) headNames[i])) {
				this.cookies = connection.getHeaderField(headNames[i]
						.toString());
			}
			System.out.println(headNames[i] + "="
					+ connection.getHeaderField(headNames[i].toString()));
		}

		int len = connection.getContentLength();
		byte[] retData = readContent(connection.getInputStream(), len);
		String retStr = new String(retData, "UTF-8");

		System.out.println("Ret Value:\n " + retStr);
		DataElement element = parseReturnValue(retStr, formatElement);
		return element;
	}

	private DataElement parseReturnValue(String src, FormatElement formatElement)
			throws EMPException {
		int idx1 = 0;
		int idx2 = src.indexOf(this.delim, idx1);
		if ((idx1 == -1) || (idx2 == -1))
			throw new EMPException("Invalid ret message!: " + src);
		this.retCode = src.substring(idx1, idx2);

		idx1 = idx2 + 1;
		idx2 = src.indexOf(this.delim, idx1);
		if ((idx1 == -1) || (idx2 == -1))
			throw new EMPException("Invalid ret message!: " + src);
		this.sessionId = src.substring(idx1, idx2);

		String data = src.substring(idx2 + 2);
		if ("exception".equals(this.retCode)) {
			throw new EMPException("ServerException:" + data);
		}

		DataElement element = null;
		if (formatElement != null) {
			element = new KeyedCollection();
			element.setAppend(true);
			try {
				formatElement.unFormat(data, element);
			} catch (Exception e) {
				throw new EMPException(e.toString());
			}
		} else {
			element = DataElementSerializer.serializeFrom(data);
		}

		return element;
	}

	private byte[] readContent(InputStream in, int len) throws IOException {
		if (len <= 0) {
			byte[] buf = new byte[2048];
			byte[] readBuf = new byte[1024];
			int totalLen = 0;
			while (true) {
				int readLen = in.read(readBuf);
				if (readLen <= 0)
					break;
				if (totalLen + readLen > buf.length) {
					byte[] tmp = new byte[buf.length + readLen + 1024];
					System.arraycopy(buf, 0, tmp, 0, totalLen);
					buf = tmp;
				}
				System.arraycopy(readBuf, 0, buf, totalLen, readLen);
				totalLen += readLen;
			}

			byte[] retBuf = new byte[totalLen];
			System.arraycopy(buf, 0, retBuf, 0, totalLen);
			return retBuf;
		}
		int totalLen = 0;
		byte[] buf = new byte[len];
		while (totalLen < len) {
			int readLen = in.read(buf, totalLen, len - totalLen);
			if (readLen <= 0)
				break;
			totalLen += readLen;
		}
		return buf;
	}

	private String getRequestData(DataElement reqDataElement,
			FormatElement formatElement, String serviceId)
			throws EMPFormatException {
		StringBuffer buf = new StringBuffer();

		buf.append(this.sessionId);
		buf.append(this.delim);

		buf.append(serviceId);
		buf.append(this.delim);
		buf.append("#");

		String reqData = null;
		if (formatElement != null)
			reqData = (String) formatElement.format(reqDataElement);
		else {
			reqData = DataElementSerializer.doSerialize(reqDataElement);
		}
		buf.append(reqData);
		return buf.toString();
	}

	public static void main(String[] arg) {
		try {
			EMPHttpClient client = new EMPHttpClient();

			client.setHostAddr("http://localhost:8080");
			client.setReqURI("/EMPDemo/httpAccess");

			KeyedCollection kColl = new KeyedCollection();

			kColl.addDataField("UserID", "User00");
			kColl.addDataField("password", "pass");

			System.out.println("Execute testEMPLogic.signOn with:\n" + kColl);
			DataElement element = client
					.establishSession(kColl, null, "signOn");
			System.out.println("RetValue:\n" + element.toString());
			try {
				kColl = new KeyedCollection();
				kColl.addDataField("Amount", "345067.75");
				kColl.addDataField("utilityValue", "3");
				kColl.addDataField("AcctNo", "4344334-4334-4334");
				kColl.addDataField("inAccount", "8888-888888-8888");

				System.out.println("Execute transfer with:\n" + kColl);
				element = client.executeServerHttpService(kColl, null,
						"transfer");
				System.out.println("RetValue:\n" + element.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}

			kColl = new KeyedCollection();

			kColl.addDataField("UserID", "User00");
			kColl.addDataField("password", "pass");

			System.out.println("Execute signOff with:\n" + kColl);
			element = client.endSession(kColl, null, "signOff");
			System.out.println("RetValue:\n" + element.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getRetCode() {
		return this.retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getHostAddr() {
		return this.hostAddr;
	}

	public void setHostAddr(String hostAddr) {
		this.hostAddr = hostAddr;
	}

	public String getReqURI() {
		return this.reqURI;
	}

	public void setReqURI(String reqURI) {
		this.reqURI = reqURI;
	}
}