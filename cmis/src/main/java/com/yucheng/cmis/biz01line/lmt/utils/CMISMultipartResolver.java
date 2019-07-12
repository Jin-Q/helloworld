package com.yucheng.cmis.biz01line.lmt.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.log.EMPLog;
import com.ecc.emp.web.multipart.EMPMultipartHttpServletRequest;
import com.ecc.emp.web.multipart.MultipartHttpServletRequest;
import com.ecc.emp.web.multipart.MultipartResolver;
import com.ecc.emp.web.multipart.UploadStatusListener;
import com.ecc.emp.web.multipart.implement.EMPMultipartFile;
import com.yucheng.cmis.util.UNIDProducer;
 
/**
 * <p> EMP 提供的信贷管理系统档案文件上传处理器</p>
 * <p> 如果上传的页面是‘信贷档案表’，则自动将文件上传至文档服务器下的/${联社机构码}/${文档类型}/${文档第一次上传时年月}/目录下</p>
 * <p> 否则文件上传至文件服务器下的/${文档第一次上传时年月}/目录下</p>
 * <p> 要求在‘信贷档案表’页面中，一定要有crd_dzdaxx__YWLX、crd_dzdaxx__WDBH、crd_dzdaxx__JGM</p>
 */
public class CMISMultipartResolver implements MultipartResolver { 
	    /**
		* 上传文件存放的根路径(当前应用所在机器)
		*/
		private String tempFileRootPath; 

        /** 上传文件最大大小 */        
        private long maxFileSize;
        /** 目录划分粒度 */
        private int pathGrading = 3;
        
        private static String FILEPATHSPLIT = ",";
        private static final String DOC_TYPE_TEMP = "990";  /** 文档类型 990 系统内部—临时文件 */
        /**
         * 日期目录格式
         */
        private String dateDirFormat = "yyyy_MM_dd";

        /**
         * 信贷文档表名
         */
        private String tableName ="DocBasicinfo";
        
        
   public CMISMultipartResolver() {
       ResourceBundle res = ResourceBundle.getBundle("cmis");
       tempFileRootPath = res.getString("tempFileRootPath");
       maxFileSize = Long.parseLong(res.getString("maxFileSize"));
   }
   
   /**
    * @param request
    * @return boolean
    */
   public boolean isMultipart(HttpServletRequest request) 
   {
           
                String contentType = request.getContentType();
                if( contentType == null )
                        return false;
                
                int i = contentType.indexOf("boundary=");
                if (i != -1) {
                        return true;
                }
           
                return false;
   }
   
   
        /**
         * http 协议格式：
         * contentType= "multipart/form-data; boundary=---------------------------7d41cca2001fc"
         * 每一个数据域：
         * -----------------------------7d41cca2001fc\r\n
         * Content-Disposition: form-data; name=CTPKey.PAGEID\r\n
         * 
         * 66\r\n
         * -----------------------------7d41cca2001fc\r\n
         * 对文件内容：
         * Content-Disposition: form-data; name="uploadFile"; filename="C:\\文本文档.txt"\r\n
         * Content-Type: text/plain\r\n
         * 
         * 文件内容
         * -----------------------------7d41cca2001fc\r\n
         * 结尾：
         * -----------------------------7d41cca2001fc--\r\n
         * 其中7d41cca2001fc是一个随机字符串
         * 
         * Creation date: (2001-1-2 13:18:59)
         * @param request javax.servlet.http.HttpServletRequest
         * @exception java.io.IOException The exception description.
         */
   
   /**
    * @param request
    * 注：只支持一次只上传一个文件
    */
   public MultipartHttpServletRequest resolverMultipart(HttpServletRequest request, UploadStatusListener listener )throws Exception 
   {
       
           String[] file = null;
           
           /**  当前WEB请示的编码 */
           String encoding = request.getCharacterEncoding();
     
           if(encoding == null )
                   encoding = "utf-8";
           
                String endBoundary = "";
                String contentType = request.getContentType();
                //EMPLog.log(EMPConstance.EMP_MULTIPART, EMPLog.INFO, 0, "contentType:\n"  + contentType  );
                
                int i = contentType.indexOf("boundary=");
                if (i != -1) {
                        endBoundary = "--" + contentType.substring(i + 9) + "--";

                }
                String boundary = "";
                int length = request.getContentLength();
                if (length == 0) { //NO content posted!!
                        return null;
                }
                
           Map parameters = new HashMap();
           Map fileMap = new HashMap();
           FileOutputStream fo = null;
                   
                
                if( maxFileSize > 0 && length > maxFileSize )
                {
                        this.addParameter(this.getTableName()+"__sys_info", "文件超过大小上限" + maxFileSize/1024 + "K", parameters);
                        this.addParameter(this.getTableName()+"__sys_state", "500", parameters);                        
                        //throw new EMPException("File size over the setting max size of [" +  maxFileSize + "]!");
                } else {
                
                if( listener != null )
                        listener.start( length );
                
                
                java.io.InputStream is = request.getInputStream();

                int bufferSizeInt = 1024; 

                //buffer used for read in a line content
                byte[] buffer = new byte[bufferSizeInt];
                int readLen = 0;
                int totalLen = 0;
                int bytesReaded = 0;
                
                //buffer used for save the content for each paramater of file
                byte[] contentBuffer = new byte[bufferSizeInt];
                int contentLen = 0;

                //      java.io.BufferedReader in = new java.io.BufferedReader( new java.io.InputStreamReader(request.getInputStream()) );
                try 
                {
                        String readLine = "";
                        //the first line should be the boundary line!
                        LineInfo lInfo = readLine(buffer, is);
                        readLen = lInfo.len;
                        
                        bytesReaded += readLen;
                        if( listener != null )
                                listener.bytesRead( bytesReaded );
                        
                        if( encoding == null )
                                boundary = new String(buffer, 0, readLen);
                        else
                                boundary = new String(buffer, 0, readLen, encoding);
                        
                        //System.err.print( boundary );
                        //                      boundary = in.readLine();

                        boolean isEnd = false;
                        while (!isEnd)  ////the /r/n/r/n indicates the begining of content 
                        { 
                                //first of all we should read in the header
                                //now was the description of content such as:   
                                // Content-Disposition: form-data; name="subj"
                                //or Content-Disposition: form-data; name="atth1"; filename="D:\download\java\index.wml.wml"
                                
                                boolean isFile = false;

                                lInfo = readLine(buffer, is);
                                readLen = lInfo.len;
                                if (readLen == 0) 
                                {
                                        break;
                                }

                                bytesReaded += readLen;

                                String descript = new String(buffer, 0, readLen, encoding);                             
                                String parameterName = "";
                                String fileName = "";
                                int nb = descript.indexOf("name=");
                                if (nb != -1) 
                                {
                                        int ne = descript.indexOf("\"", nb + 6);
                                        parameterName = descript.substring(nb + 6, ne);
                                }
                                //System.err.println("parameterName=" + parameterName);
                                //EMPLog.log(EMPConstance.EMP_MULTIPART, EMPLog.INFO, 0, "descript:\n"  + descript + "=" + parameterName);
                                
                                int fb = descript.indexOf("filename=");
                                if (fb != -1) 
                                {
                                        int fe = descript.indexOf("\"", fb + 10);
                                        fileName = descript.substring(fb + 10, fe);
                                        
                                        //open the fileOutputStream to write
                                        //EMPLog.log(EMPConstance.EMP_MULTIPART, EMPLog.INFO, 0, "Receive upload file:"  + fileName  );
                                        
                                        if(fileName != null && !fileName.trim().equals("")){
                                                /** 
                                                 * 从页面上传过来的与目录相关的参数
                                                 * 注意：要求在页面上这三个域在上传文件域之前
                                                 */
/* */                      
						                        //String st_YWLX = this.getParameter(this.getTableName()+"__biz_type","",parameters);
						                        String st_doc_type = this.getParameter(this.getTableName()+".doc_type","",parameters);
						                        String st_org_no  = this.getParameter(this.getTableName()+".register_org_no","",parameters);
						                        String st_doc_no = this.getParameter(this.getTableName()+".doc_no","",parameters); 
						                                            
						                        //System.err.println(this.getTableName() + "  st_YWLX: " + st_doc_type + "  st_JGM: " + st_org_no + " st_WDBH:" + st_doc_no);
						 
						                        
						                        if(st_doc_no == null||st_doc_no.trim().equals("")){
						                                UNIDProducer uniCode = new UNIDProducer();
						                                st_doc_no = uniCode.getUNID();
						                                this.addParameter(this.getTableName()+"__doc_no", st_doc_no, parameters);
						                        }
                                                fileName = java.net.URLDecoder.decode(fileName);
                                                
                                                if(st_doc_type == null || st_doc_type.trim().equals("")){
                                                	/** 当没有选择文档类型时，则认为是临时文件 */
                                                	st_doc_type = DOC_TYPE_TEMP;
                                                }
                                                file = this.getFileName(st_org_no, st_doc_type, st_doc_no, fileName );
                                                //System.err.println("file[0] _111=="+file[0]);
                                                EMPMultipartFile empMultiPartFile = new EMPMultipartFile(parameterName, file[0], fileName );
                                                fileMap.put( parameterName, empMultiPartFile);
                                                
                                                this.addParameter(parameterName, file[0], parameters);
                                                this.addParameter(this.getTableName()+"__file_path", file[1], parameters);
                                                this.addParameter(this.getTableName()+"__file_name", file[2], parameters);
                                                //增加文件大小的参数
                                                this.addParameter(this.getTableName()+"__file_size", (length/1024)+"", parameters);
                                                fo = openFileForWrite( file[0] );
                                                isFile = true;
                                        }
                                }

                                //skip the next lines untill read the "/r/n" line
                                while (true) 
                                {
                                        lInfo = readLine(buffer, is);
                                        readLen = lInfo.len;
                                        bytesReaded += readLen;
                                        String tmp = new String(buffer, 0, readLen, encoding);
                                        
                                        if (tmp.trim().length() == 0) {
                                                break;
                                        }
                                }

                                if( listener != null )
                                        listener.bytesRead( bytesReaded );

                                contentLen = 0;
                         
                                while (true) //read the content untill reach the boundary or endBoundary 
                                { 

                                        lInfo = readLine(buffer, is);
                                        readLen = lInfo.len;
                                        //System.err.println(new String(buffer, 0, readLen, encoding));
                                        
                                        bytesReaded += readLen;
                                        if( listener != null )
                                                listener.bytesRead( bytesReaded );
        
//                                      Thread.sleep(20);
                                        
                                        if (readLen <= 0) 
                                        {
                                                if (contentLen > 0 && isFile) 
                                                {
                                                        writeToFile(fo, contentBuffer, contentLen);
                                                        contentLen = 0;
                                                }
                                                break;
                                                //throw new Exception("上传文件["+ fileName + "]异常中断,网络传输异常！");
                                        }

                                        if( lInfo.isLine )
                                        {
                                                //now conver it into String to check if we have encounter the mark String
                                                readLine = new String(buffer, 0, readLen, encoding);
                                                
                                          
                                                if (readLine.equals(boundary) || readLine.trim().equals(endBoundary) 
                                                                || readLine.indexOf(boundary) >= 0 || readLine.trim().indexOf(endBoundary) >= 0) 
                                                { //now we have reach the end of the content!
                                                        

                                                        if (isFile) 
                                                        {
                                                                if( contentLen > 2 )
                                                                        writeToFile(fo, contentBuffer, contentLen - 2);
                                                                
                                                                        try {
                                                                                EMPLog.log(EMPConstance.EMP_MULTIPART, EMPLog.INFO, 0, "Receive upload file:"  + fileName + " finished!" );
                                                                                
                                                                                fo.close();
                                                                                isFile = false;
                                                                        } 
                                                                        catch (Exception ee) {
                                                                                EMPLog.log(EMPConstance.EMP_MULTIPART, EMPLog.ERROR, 0, "Receive upload file:"  + fileName + " failed to close!", ee );
                                                                        }
                                                                        
//                                                                      closeOpenedFile( fo );
                                                                        //files.put(fileName, content);
                                                        } 
                                                        else 
                                                        {
                                                                if(contentLen > 2){
                                                                        
                                                                   String content = new String(contentBuffer, 0, contentLen-2, encoding);
                                                                
                                                                   //因为可能页面中会传送同名的参数，所以要将这个参数处理成数组 jinzw 050719
                                                                   
                                                                   addParameter(parameterName, content, parameters);
                                                                }
                                                        }
                                                        break;
                                                }
                                        }

                                        if ((contentLen + readLen) > contentBuffer.length) 
                                        {
                                                //re malloc the buffer size
                                                if (isFile) 
                                                { //buffer over flow so write it to file
                                                        writeToFile(fo, contentBuffer, contentLen);
                                                        writeToFile(fo, buffer, readLen);
                                                        contentLen = 0;
                                                }
                                                else
                                                {
                                                        //re malloc the buffer size
                                                        byte[] tmp = new byte[contentBuffer.length + 1024];
                                                        System.arraycopy(contentBuffer, 0, tmp, 0, contentLen);
                                                        contentBuffer = tmp;
                                                }
                                                        
                                        }
                                        else
                                        {
                                                //copy the read content to buffer
                                                System.arraycopy(buffer, 0, contentBuffer, contentLen, readLen);
                                                contentLen = contentLen + readLen;
                                                totalLen = totalLen + readLen;
                                        }
                                }
                        }

                        try {
                                if(fo != null) fo.close();
                        } 
                        catch (Exception ee) {
                                ee.printStackTrace();
                        }

                        this.addParameter(this.getTableName()+"__sys_info", "文件上传处理完毕", parameters);
                        this.addParameter(this.getTableName()+"__sys_state", "200", parameters);
                } catch (Exception e) {
                        try {
                                if(fo != null) 
                                fo.close();
                        } catch (Exception ee) {
                                ee.printStackTrace();
                        }
                        e.printStackTrace();
                        this.addParameter(this.getTableName()+"__sys_info", "文件服务器出现未知异常，请与管理员联系" , parameters);
                        this.addParameter(this.getTableName()+"__sys_state", "500", parameters);
                }
                
                }
                if( listener != null )
                        listener.done();
                 
                EMPMultipartHttpServletRequest multiRequest = new  EMPMultipartHttpServletRequest(request, parameters, fileMap);
                return multiRequest;
   }
   
   
   
   
        /**
         * 从输入流中读入一行内容(碰到\r\n)
         * @param buf byte[]
         * @param is java.io.InputStream
         * @return int
         * @exception java.io.IOException
         */
        public LineInfo readLine(byte[] buf, java.io.InputStream is )throws java.io.IOException 
        {

//              byte[] buffer = new byte[1024];
//              int bufLen = 1024;
        
                boolean isLine = false;
                
                int maxLen = buf.length;
                
                //int lineLength = 0;
                int off = 0;
                
                while (true) 
                {
                        int chr = 0;
                        try 
                        {
                                chr = is.read();
                        } catch (IOException e) 
                        {
                                throw e;
                        }
                        if (chr == '\n' )       //reach the end 
                        {
                                buf[off++] = (byte) chr;
                                isLine = true;
                                break;
                        } 
                        else if (chr == -1) 
                        {
                                break;
                        } 
                        else 
                        {
                                buf[off++] = (byte) chr;
                        }
                        if( off >= maxLen )
                                break;
                }
                LineInfo lInfo = new LineInfo();
                
                lInfo.isLine = isLine;
                lInfo.len = off;
                lInfo.lineBuf = buf;
                //System.err.println(">>>>" + new String(buf) + "<<<<");
//              try{
//                      Thread.sleep( 20 );
//              }catch(Exception e)
//              {
//                      
//              }
                return lInfo;
        }
   
   
        private void writeToFile(FileOutputStream fo, byte[] content, int len) throws Exception {
                try {
                
//                      System.out.println("Write to file len: " + len );
                        
                        fo.write(content, 0, len);
                        fo.flush();
                        
                } 
                catch (Exception e) 
                {
                        throw e;
                }
        }
        
        private FileOutputStream openFileForWrite(String fileName) throws Exception 
        {
                try 
                {
                        return  new FileOutputStream(fileName);
                } 
                catch (Exception e) 
                {
                        throw e;

                }
        }

   
        protected void addParameter(String key, String value, Map hParams) 
        {
                
                EMPLog.log(EMPConstance.EMP_MULTIPART, EMPLog.DEBUG, 0, "Add new parameter " + key + "=" + value );
                Object preValueObj = hParams.get(key);
                //System.err.println("addParameter: " + key + "=" + value);
                if (null == preValueObj) 
                {
                        hParams.put(key, new String[] {value});
                }
                else 
                {
                        int len = ((String[])preValueObj).length;
                        String[] sArr = new String[len+1];
                        System.arraycopy((String[])preValueObj, 0, sArr, 0, len);
                        sArr[len] = value.trim();
                        hParams.put(key, sArr);
                }
        }
        
        /**
         * <p>根据域的键值取得HTTP报文中的一个数据域的值</p>
         * <p>注：只有被解释过的域才能用此方法得到值</p>
         * @param key  域的键值
         * @param defaultval 缺省值 （当根据键值取不到数据时）
         * @param hParams HTTP报文解释结果缓存Map
         * @return HTTP报文中指定域键的值(建议只用于文本格式的数据)
         */
        protected String getParameter(String key, String defaultval, Map hParams)
        {
                String st_return = "";
                if(hParams.get(key) == null){
                        st_return = defaultval;
                }else{
                        String[] st_temp = (String[])hParams.get(key);
                        if(st_temp[0] != null){
                          st_return = st_temp[0];
                        }else{
                                st_return = defaultval;
                        }
                }
                
                return st_return;
        }
    
        /**
         * <p>生成完整的文件名</p>
         * <p>目录名规则：/${联社机构码}/${业务类型}/${文档第一次上传时年月}/  如果为空则直接存在其上一级目录中</p>
         * <p>文件名规则：${文档编号}  如果‘文档编号’为空，则使用上传时的客户端的文档名</p>
         * @param JGM 机构码
         * @param docType 业务类型
         * @param WDBH 文档编号 （是全局唯一的编号）
         * @param uploadFileName 上传的文件名
         * @return 字符串数组 其中 下标 0: 完整的文件名（含完整的路径）1:相对路径 2:文件名 3: 新建的目录列表（相对路径）
         */
        private synchronized String[] getFileName(String orgNo, String docType, String docNo, String uploadFileName ){
                String[] sta_return = new String[4];
                String fileName = uploadFileName.replace('\\', '/'); //文件名
                String fileExt = "";//文件扩展名
                String fileAllName = "";//文件全名 
                String filePath = "";//文件路径
                String newPathlist = "";
                String rfilepath = ""; //相对路径
                int idx =fileName.lastIndexOf('/');//临时用
                if( idx != -1)
                        fileName =fileName.substring( idx + 1 );        
                
                if(docNo != null && !docNo.trim().equals("")){
                        /** 如果有文档编号，就以文档编号为实际物理名（文档编号是唯一的）*/
                        idx = fileName.lastIndexOf('.');
                        if( idx != -1){
                                fileExt = fileName.substring(idx);   
                        }else{
                                fileExt = "";
                        }
                        fileName = docNo + fileExt; 
                }
                EMPLog.log(EMPConstance.EMP_MULTIPART, EMPLog.ERROR, 0, "开始构建目录。。。。。。。。。。。。。。。。。"); 
                
                filePath = this.tempFileRootPath;
                /**
                 * @TODO 这里机构码是否需要只截取前N位为目录？
                 */
                if(orgNo == null || orgNo.trim().equals("") || orgNo.length() < 2){
                	orgNo = ""; //为空则不使用该目录
                } else {
                        
                        /** 截取机构码的前N位为第一级目录名*/
                        if(orgNo.length() >= 3){ 
                            /**@todo 这里需要从配置中取*/
                        	orgNo = orgNo.substring(0, 3);
                        } else {
                                EMPLog.log(EMPConstance.EMP_MULTIPART, EMPLog.ERROR, 0, 
                                       "机构码太短，不能用于构建目录");
                        }
                        
                        /** 注：一定要保证tempFileRootPath所指的目录存在*/
                        File upPath = new File(this.tempFileRootPath + "/" + orgNo + "/");
                        filePath += "/" + orgNo;
                        rfilepath += orgNo + "/";
                        if(!upPath.exists()){ /** 如果目录不存在，则生成目录*/
                                upPath.mkdir(); 
                                setFilePermission(filePath);
                                newPathlist += rfilepath + FILEPATHSPLIT; 
                        }
                }
                
                if(docType == null || docType.trim().equals("")){
                	docType = "";//为空则不使用该目录
                } else {
                        File upPath = new File(this.tempFileRootPath + "/" + orgNo + "/" + docType + "/");
                        filePath += "/" + docType;
                        rfilepath += docType + "/";
                        if(!upPath.exists()){ /** 如果目录不存在，则生成目录*/
                                upPath.mkdir(); 
                                setFilePermission(filePath);
                                newPathlist += rfilepath + FILEPATHSPLIT;
                        }                       
                }
                //filePath = this.tempFileRootPath + "/" + JGM + "/" + YWLX ;
                filePath +=  "/" + getFolderByMonth();
                rfilepath += getFolderByMonth() + "/";
                
                fileAllName =  filePath + "/" + fileName;
                sta_return[0] = fileAllName;//0: 完整的文件名（含完整的路径）         
                sta_return[2] = fileName;   //2:文件名
                
                File upPath = new File(filePath + "/");
                if(!upPath.exists()){ /** 如果目录不存在，则生成目录*/
                        upPath.mkdir(); 
                        setFilePermission(filePath);
                        newPathlist += rfilepath + FILEPATHSPLIT;
                }
                
                if(docNo == null || docNo.trim().equals("")){
                        /** 如果‘文档编号’为空，则使用上传时的客户端的文档名，此时需要判断是否重名*/
                        File file = new File( fileAllName );
                        
                        idx = 0;
                        while(file.exists())
                        {
                                fileAllName = filePath + "/"  + idx + "-" + fileName;
                                idx++;
                                
                                file = new File( fileAllName);
                        }
                }
//              sta_return[1]=filePath.substring(8);
                sta_return[1] = rfilepath;   //1:相对路径 
                sta_return[3] = newPathlist; //3：新建的目录
                return sta_return;              
        }
        
        
        /**
         * <p>设置文件或文件目录权限</p>
         * <p>注：权限设置为了同组可写</p>
         * @param Filepath
         * @todo 现只针对unix
         */
        private void setFilePermission(String path){
/*              try {
                        
                        Runtime.getRuntime().exec("chmod a+w " + path);
                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }*/
        }
        /**
         * <p>用于得到指定目录下按月的子目录（格式为YYYY_MM）</p>
         * @return 返回按月的子目录名
         */
        private String getFolderByMonth(){
                SimpleDateFormat dataFmt = new SimpleDateFormat(dateDirFormat);
                String date = dataFmt.format(new Date());
                date = date.substring(0,7);
                
                return date;
        }
        
   /**
    * @param request
    * @roseuid 450647D40138
    */
   public void cleanup(MultipartHttpServletRequest request) 
   {
    
   }
   
   
   private class LineInfo {
        
           byte[] lineBuf;
           int len;
           boolean isLine = false;
   }

  public void setDateDirFormat(String dateDirFormat) 
   {
           this.dateDirFormat = dateDirFormat;
   }

   
   public void setMaxFileSize(long maxsize)
   {
           this.maxFileSize = maxsize;
   }
   
   public long getMaxFileSize()
   {
           return this.maxFileSize;
   } 

	public String getTableName() {
	        return tableName;
	}
	
	public void setTableName(String tableName) {
	        this.tableName = tableName;
	}

	public int getPathGrading() {
		return pathGrading;
	}

	public void setPathGrading(int pathGrading) {
		this.pathGrading = pathGrading;
	}



}

