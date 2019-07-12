/**
 * 接入影像
 * 
 * Author: Logicm
 * Date: 2010-03-16
 */

//下面这段逻辑总是被执行，保证了在未接入影像的窗口不显示
try{
	//为未定义visibleImage的页面补获异常==>设置visibleImage的值
	if(visibleImage); 
}catch(e){
	visibleImage=false; 
}

//如果visibleImage属性为空，则隐藏影像窗体的代码总是被执行，可以保证未接入影像的窗体不显示
if(visibleImage != true){
	try{
		top.document.all('rightTd').style.display = "none";//影像图口
		top.document.all('slide_right').style.display = "none";//右侧分栏
		var yq = top.document.all('Page_left');//左侧 的二级菜单
		var obj = top.document.all('mainframe');//主窗体

		//重新分布左侧栏和主窗口的大小分布
			//判断左侧 分栏是否打开
		if(yq.style.display=="none"){
			obj.style.width="100%";
		}
		else{
			obj.style.width="90%";
		}
		
	}catch(e){
	}
}


/**
 * 接入影像主方法：通用方法
 * 
 * 在主页面的右侧的iframe区域展示影像页面
 * 
 * @param appl_seq 申请编号 
 * @param sid EMP_SID
 * */
function doImageManager(appl_seq, sid){
   top.rightFrame.location = "editExtInf.do?EMP_SID=" + sid + "&appl_seq=" + appl_seq;
    top.rightTd.style.display = 'block';
    top.slide_right.style.display = 'block'; 
    top.rightTd.style.width = '30%';
    top.mainframe.style.width = '70%'; 
}

/**
 * 
 * 客户信息接入影像：
 * 由于客户的综合信息可能会被做为一个弹出窗在其它页面调用，所以影像的窗口不能嵌在主页面的iframe上
 * 这里是嵌在queryCusComTree.jsp中iframe中
 * 
 * @param appl_seq 申请编号 
 * @param _sid EMP_SID
 */
function doImageCusTreePage(appl_seq, sid){
	parent.downFrame.location = "editExtInf.do?EMP_SID=" + sid + "&appl_seq=" + appl_seq;
	parent.down_div.style.display = 'block'; 
	parent.down_div.style.width = '100%';  
	parent.down_div.style.height = '500px'; 
	parent.Page_right.style.width = '60%';  
}
