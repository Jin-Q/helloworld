
if (!EMP.widget.RelatedTabGroup) {
	
	EMP.widget.RelatedTabGroup = function(tabGroupName) {
		
		this.id = tabGroupName;
		
		this.tabs = {};
		this.tabsDiv = null;
		this.mainTab = null;
		this.mainDiv = null;
		this.loading = null;
	};
	
	EMP.widget.RelatedTabGroup.prototype._parseParams = function(tag){
		
		this.tabsDiv = tag;
		this.tabsDiv.className = "relatedtabs_tabs";
		
		this.mainTab = tag.getAttribute("mainTab");
		
		this.mainDiv = document.getElementById('relatedtabs_'+this.id+'_main');
		this.mainDiv.className = "relatedtabs_main";
		
		this.loading = document.createElement("DIV");
		this.loading.style.display="none";
		if(this.mainDiv.childNodes.length > 0){
			this.mainDiv.insertBefore(this.loading,this.mainDiv.childNodes[0]);
		}else{
			this.mainDiv.appendChild(this.loading);
		}
		var textEl = document.createTextNode("Loading...");
		this.loading.appendChild(textEl);
	};
	
	EMP.widget.RelatedTabGroup.prototype._addTab = function(tabName, tabDef){
		var tab = null;
		var tabTag = document.getElementById(tabName+"_div");
		if(tabTag){
			tab = new EMP.widget.RelatedTab(this);
			tab.id = tabName;
			tab.label = tabTag.getAttribute("label");
			tab.url = tabTag.getAttribute("url");
			tab.hidden = tabTag.getAttribute("hidden");
			tab.needFlush = tabTag.getAttribute("needFlush");
			var initial = tabTag.getAttribute("initial");
			if(initial && (initial == "true" || initial == true))
				tab.initial = true;
		}else{
			tabTag = document.createElement('div');
			this.mainDiv.appendChild(tabTag);
			tabTag.id = tabName+"_div";
			
			tab = new EMP.widget.RelatedTab(this);
			tab.id = tabName;
			tab.label = tabDef.label;
			tab.url = tabDef.url;
			tab.hidden = tabDef.hidden;
			tab.needFlush = tabDef.needFlush;
			tab.initial = tabDef.initial;
		}
		tab.tag = tabTag;
		
		var aEl = document.createElement("A");
		this.tabsDiv.appendChild(aEl);
		aEl.href = "#";
		
		EMPTools.addEvent(aEl,"click",tab._clickLink,tab);
		
		var textEl = document.createTextNode(tab.label);
		aEl.appendChild(textEl);		
		aEl.onfocus = new Function("this.blur()");
		aEl.id=tab.id;
		tab.a = aEl;
		
		if(tabName == this.mainTab){
			tab.a.className = "activated";
			tab.initial = true;//如果是主页签页，则缺省是加载的
			tabTag.style.display = "";
		}else{
			tab.a.className = "";
			tabTag.style.display = "none";
		}
		if(tab.initial && tab.url){
			tab.refresh();
		}
		if(!tab.url){
			tab.loaded = true;
		}
		this.tabs[tabName] = tab;
		
		tab.renderHidden(tab.hidden);//处理隐藏效果
	};
	
};

if (!EMP.widget.RelatedTab) {	
	EMP.widget.RelatedTab = function(tabGroup){
		this.id = null;
		this.label = null;
		this.tag = null;
		
		this.relatedTabGroup = tabGroup;
		
		this.needFlush = false;
		this.initial = false;
		
		this.loaded = false;
	};
	
	EMP.widget.RelatedTab.prototype._clickLink = function(){
	
		if (this.url && !this.iframe) {
			this.refresh();	
		}else if(this.needFlush != null && (this.needFlush==true || this.needFlush=="true")){
			if( this.url != null && this.url != "" ){
				this.refresh();
			}
		}
		
		for(var tabName in this.relatedTabGroup.tabs){
			
			var tab = this.relatedTabGroup.tabs[tabName];
			
			if(tab.id != this.id){
				tab.tag.style.display = "none";
				tab.a.className = "";
			}
			this.tag.style.display = "";
			this.a.className = "activated";
			this.relatedTabGroup.curIndex = this.id;
		}	
	};
	
	
	EMP.widget.RelatedTab.prototype.refresh = function(){
		
		this.relatedTabGroup.loading.style.display="";
		
		if(!this.iframe){
			var iframe = document.createElement("IFRAME");
			this.iframe = iframe;
			iframe.src = this.url;
			if(this.id)
				iframe.id = this.id;
			iframe.setAttribute("frameborder","0",0);
			iframe.className = "relatedtabs_main_iframe";
			iframe.tab = this;
			
			this.tag.appendChild(iframe);
		}else{
			this.iframe.src = this.url;
		}
		
		//IE Only 根据内容调整iFrame高度
		EMPTools.addEvent(this.iframe,"readystatechange",this._readyStateChange,this);
		
	};
	
	EMP.widget.RelatedTab.prototype._readyStateChange = function(){
		if(this.iframe.readyState=='complete') { 
			this.loaded = true;
			this.relatedTabGroup.loading.style.display="none";
		}
	};
	
	EMP.widget.RelatedTab.prototype.renderHidden = function(hidden){
		this.hidden = hidden;
		if(this.hidden) { 
			this.a.style.display = "none";
		}else{
			this.a.style.display = "";
		}
	};
	
}	
	