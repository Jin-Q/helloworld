//弹出式  窗口组件
cWinUtils = function() {
	return {
		onDomReady : function(callbackFn, param, win, scope) {
			win = win ? win : window;
			if (Ext.isIE) {
				win.onreadystatechange = function() {
					if (win.readyState && win.readyState == 'complete') {
						param[2] = this.contentWindow;// 当前页面容器 兼容页面跳转
						callbackFn.apply(scope, param);
					}
				}
			} else {
				win.onload = function() {
					param[2] = this.contentWindow;// 当前页面容器 兼容页面跳转
					callbackFn.apply(scope, param);
				}
			}
		},
		open : function(src, title, width, height, modal, winObj, maximized, id) {
			if (!width)
				width = "80%";
			if (!height) {
				height = document.body.clientHeight * 0.8;
			}
			if (modal == undefined || modal == null)
				modal = true;
			if (!id)
				id = Ext.id(null, 'openwin_');
			else {
				id = 'openwin_' + id;
			}
			var html = '<iframe  id="frame_'
					+ id
					+ '"  src="'
					+ src
					+ '" frameborder="0" scrolling="Auto"  height="100%"  width="100%" ></iframe>';
			var cwindow = Ext.getCmp(id);
			var callbackFn = function(id, obj, contentWindow) {
				this.body.unmask();
				// if (!title)this.setTitle(t.contentWindow.document.title);
				// 记录 原始top.opener
				var oldTopOpener = null, oldOpener = null;
				if (obj) {
					if (contentWindow.EMPTools) {
						contentWindow.EMPTools.getWindowOpener = function() {
							return obj;
						};
					}
					oldOpener = contentWindow.opener;
					contentWindow.opener = obj;
					// contentWindow.window.opener=obj;
					oldTopOpener = contentWindow.top.opener;
					contentWindow.top.opener = obj;
				}
				contentWindow.close = function() {
					if (obj) {
						// 还原 原始top.opener 防止 top对象错乱
						contentWindow.top.opener = oldTopOpener;
						contentWindow.opener = oldOpener;
					}
					cWinUtils.close(id);
				}
			}
			if (!cwindow) {
				var tools = [{
					id : 'refresh',
					qtip : '刷 新',
					handler : function(e, t, p) {
						var f = Ext.getDom("frame_" + id);
						if (f) {
							p.body
									.mask("<span class='waitCls'>正在刷新页面  请稍后...</span>");
							f.contentWindow.location.reload();
						}
					}
				}];
				cwindow = new Ext.Window({
							id : id,
							width : width,
							tools : tools,
							maximized : maximized,
							maximizable : true,
							height : height,
							modal : modal,
							autoScroll : true,
							plain : true
						});
			} else {
				cwindow.body.update(html);
				cwindow.body
						.mask("<span class='waitCls'>正在重新加载页面  请稍后...</span>");
				var t = Ext.getDom("frame_" + id);
				if (t)
					cWinUtils.onDomReady(callbackFn, [id, winObj, t], t,
							cwindow)
			}
			if (title&&!/[a-zA-Z0-9_]*/.test(title))
				cwindow.setTitle(title.replace('popwin_popwin_', ""));
			cwindow.show(null, function(p) {
						p.body.update(html);
						p.body
								.mask("<span class='waitCls'>正在加载页面  请稍后...</span>");
						var t = Ext.getDom("frame_" + id);
						if (t)
							cWinUtils
									.onDomReady(callbackFn, [id, winObj], t, p)
					});
			return cwindow;
		},
		max : function(src, title, width, height, modal, winobj, id) {
			try {
				var cwindow = Ext.getCmp(id);
				if (cwindow) {
					cwindow.maximize();
				} else {
					cwindow = this.open(src, title, width, height, modal,
							winobj, true, id);
				}
			} catch (e) {
			}
		},
		close : function(id, callback) {
			try {
				var cwindow = Ext.getCmp(id);
				if (cwindow) {
					cwindow.close();
				}
				if (callback) {
					callback.call(this);
				}
			} catch (e) {
			}
		}
	}
}();
