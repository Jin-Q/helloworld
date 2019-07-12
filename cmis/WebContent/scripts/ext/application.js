/**
 * Ext布局模式
 * 
 * @copyright (c) 2011-04-01, by mohen
 * @author zhouxuan
 * @version 1.0
 */
/** *********可控参数***************** */
// 是否是单tab 模式
var isSingle = false;
// 是否单击节点时 强制刷新页面 跟旧版系统相同
var isRefresh = false;
// 是否应用Ext弹出提示框 和弹出层
var isOverWrite = true;
// 是否开启 智能提示 关闭可节省内存
var isOpenTip = false;
/** *********页面初始化加载组件***************** */
Ext.onReady(function() {
	if (isOpenTip)
		Ext.QuickTips.init();
	exitHandler = function() {
		Ext.MessageBox.confirm('系统提示', '是否确定退出系统?', function(o) {
					if (o == 'yes')
						relogin();
				}, this);
	}
	var menu = changeMainCss();
	var toolbar = new Ext.Toolbar({
				items : ['-',
						'->', '-', {
							text : loginInfo,
							iconCls : 'user'
						}, '-', {
							text : bizInfo,
							iconCls : 'time'
						}, '-', {
							text : "系统主题",
							iconCls : "color_wheel",
							menu : menu,
							toggleGroup : "menu",
							enableToggle : true
						}, '-', {
							text : '注销',
							iconCls : 'cancel',
							toggleGroup : "menu",
							enableToggle : true,
							handler : exitHandler
						}, '-']
			});
	var bbar = new Ext.Toolbar({
		items : [
				'-',
				'<div id="pathBar" style="font-size:12px;color:blue">当前位置:<font color="red" id="nav"></font></div>',
				'-', '->', '-']
	})
	new Ext.Viewport({
				layout : 'border',
				items : [{
							region : 'north', // position for region
							height : 75,
							collapsible : true,
							plain : true,
							border : true,
							applyTo : 'topPanel',
							margins : '1 2 0 2',
							bbar : toolbar
						}, {
							region : 'center',
							id : 'mainCenter',
							xtype : 'centerpanel',
							activeItem : 0,
							bbar : bbar,
							contentEl : 'center',
							margins : '0 2 2 2'
						}/*
							 * , { region : 'south', // position for region
							 * height : 15, xtype : 'container', plain : true,
							 * html:'<div id="pathBar"
							 * style="font-size:12px;color:blue">当前位置:<font
							 * color="red" id="nav"></font></div>', margins :
							 * '0 2 2 2' }
							 */]
			});
});
/**
 * 按钮权限函数
 * 
 * @param {}
 *            menuid
 * @param {}
 *            opid
 * @return {Boolean}
 */
function checkPermission(menuid, opid) {
	if (!operations) {
		cextUtils.CreateTaskMgrMsg('对不起!系统会话已经超时,请重新登录！', 9, relogin, relogin);
		return false;
	}
	var item = operations[menuid];
	if (item) {
		if (item[opid]) {
			return true;
		}
	}
	return false;
};
// 当前激活面板 是否已经被 锁屏 标志
var isMask = false;
// 当前激活面板ID
var APanelId = "maincenter";
// 开始整个页面遮罩
function setMainFrameWait() {
	var center = Ext.getCmp('mainCenter');
	center.body.mask("<span class='waitCls'>正在加载主页面请稍后...</span>");
}
/**
 * 关闭整个页面遮罩
 */
function removeMainFrameWait() {
	var center = Ext.getCmp('mainCenter');
	center.body.unmask();
}
/**
 * 打开弹出层 window
 * 
 * @param {}
 *            win 请求弹出页面的window对象
 * @param {}
 *            url 新页面URL
 * @param {}
 *            modal 是否需要遮罩
 * @param {}
 *            id 窗口ID
 * @param {}
 *            title 窗口标题
 * @param {}
 *            width 宽度 默认 浏览器可见 宽度80%
 * @param {}
 *            height 高度 默认 浏览器可见 高度80%
 * @param {}
 *            maximized 是否可以最大化
 * @return {}
 */
function openWindow(win, url, modal, title, id, width, height, maximized) {
	if (modal == undefined || modal == null)
		modal = true;
	// cWinUtils.open(src, title, width, height, modal,winObj,maximized,id)
	return cWinUtils.open(url, title, width, height, modal, win, maximized, id);
}
/**
 * 创建更换主题 菜单
 * 
 * @return {menu}
 */
var hasIcon = false;
function changeMainCss() {
	var theme = cextUtils.theme;
	cextUtils.applyTheme(baseUrl);
	var menu = new Ext.menu.Menu({
				items : [{
							text : theme[0].title,
							iconCls : "blueStyle",
							handler : function() {
								cextUtils.changeCss(0, baseUrl);
							}
						}, {
							text : theme[1].title,
							iconCls : "grayStyle",
							handler : function() {
								cextUtils.changeCss(1, baseUrl);
							}
						}, {
							text : theme[2].title,
							iconCls : "greenStyle",
							handler : function() {
								cextUtils.changeCss(2, baseUrl);
							}
						}, '-', {
							text : '关于',
							iconCls : 'info',
							handler : function() {
								cextUtils.MessageBoxAlert('版本信息', sysInfo,
										false, false, 600);
							}
						}]
			});
	return menu;
}
/**
 * 开始当前激活面板 遮罩
 * 
 * @param {}
 *            text
 */
function maskAPanel(text) {
	text = text ? text : "<span class='waitCls'>正在请求数据,请稍后....</span>";
	if (APanelId) {
		var p = Ext.getCmp(APanelId);
		p.ownerCt.body.mask(text);
		isMask = true;
	}
}
/**
 * 关闭当前激活面板 遮罩
 */
function unmaskAPanel() {
	if (APanelId) {
		var p = Ext.getCmp(APanelId);
		p.ownerCt.body.unmask();
		isMask = false;
	}
}
/**
 * 重写 window提示框为 Ext提示框
 * 
 * @param {}
 *            win
 */
function overwriteApi(win) {
	if (isOverWrite) {
//		win.alert = function(msg) {
//			cextUtils.MessageBoxAlert('系统提示信息', msg);
//		}
		win.open = function(url, title) {
			return openWindow(win, url, true, title);
		}
		//11-5-4  win.showModalDialog = win.open;
		/*
		 * win.confirm=function(msg){ try{
		 * cextUtils.MessageBoxConfirm('系统提示信息',msg,function(key){
		 * if(key=='yes')throw true;else throw false;//创建中断。。。
		 * 
		 * },win); }catch(e){ if(e==true) return e; } }
		 */
	}

}
/**
 * 是否是EXT布局模式
 * 
 * @return {Boolean}
 */
function isExt() {
	return true;
}
// 重新登录
function relogin() {
	window.location = exitUrl;
}
/**
 * 请求菜单数据
 * 
 * @param {panel}
 *            panel 主页面布局
 * @param {fn}
 *            fn 回调函数
 */
function ajaxMenuTree(panel, fn) {
	Ext.getBody().mask("<span class='waitCls'>正在请求访问权限....</span>");
	Ext.Ajax.request({
				url : treeUrl,
				success : function(response, opts) {
					Ext.getBody().unmask();
					try {
						var json = Ext.decode(response.responseText);
						menuTree = json.menuTree;
						var root = menuTree.children[0].children;
						fn.call(panel, root);
						operations = json.operations;
					} catch (e) {
						cextUtils.CreateTaskMgrMsg('对不起!系统会话已经超时,请重新登录！', 9,
								relogin, relogin);
					}
				},
				failure : function(response, opts) {
					cextUtils.CreateTaskMgrMsg('对不起!系统会话已经超时,请重新登录！', 9,
							relogin, relogin);
				}
			});
}
