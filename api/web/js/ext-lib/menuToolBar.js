var MailMenuBar = Class.create({
	initialize: function(opt, menuList, sideItem){	
		this.opt = opt;
		this.menuList = menuList;
		this.sideItem = sideItem;
		this.ItemHash = new Hash();
		this.groupHash = new Hash();
		this.sideMenuVisble = true;
	},
	getMode: function(){
		return this.opt.mode;
	},
	makeToolbar:function(makeId){
		var mainTab,tabWrapper,mList,tabLink,drowMenuItem;
		var i=0;
		this.clearToolBar();
		if(skin != "skin3"){
			mainTab = jQuery("#"+this.opt.tabID);
			tabWrapper = jQuery("<div class='mail_body_tab_wrapper'>");
			mList = this.menuList;
			
			for ( i = 0; i < mList.length; i++) {
				tabLink =  jQuery("<a href='javascript:;'></a>");
				if(makeId == mList[i].id){
					drowMenuItem = mList[i].groupItem;
					tabLink.addClass("on");
					var toolbarL = jQuery("#"+this.opt.contentID).parent();
					tabLink.dblclick(function(){toolbarL.toggle();jQuery(window).trigger("resize");});
				} else {
					var mid = mList[i].id;
					var func = mList[i].linkFunc;
					tabLink.click(function(){func(mid);});
					tabLink.addClass("off");
				}
				tabLink.append("<span>"+mList[i].name+"</span>");				
				tabWrapper.append(tabLink);
			}
			var tabWrapperOut = jQuery("<div class='mail_body_tab_wrapper_out'>");
	        mainTab.append(tabWrapperOut.append(tabWrapper));		
			for (i = 0; i < drowMenuItem.length; i++) {			
				this.makeGroup(drowMenuItem[i],makeId+"_"+i);
			}
			
			this.makeSideItem();
		}else{
			mainTab = jQuery("#"+this.opt.tabID);
			tabWrapper = jQuery("<div class='mail_body_tab_wrapper'>");
			mList = this.menuList;
			tabLink =  jQuery("<a href='javascript:;'></a>");
			this.makeSkin3Top(mList[0].groupItem[0],makeId+"Top_0","top");
			if(mList.length > 1){
				this.makeSkin3Top(mList[1].groupItem[0],makeId+"Bottom_0","bottom");
			}
			
			if(makeId == "write"){				
				this.makeAutoSaveItem();						
			}
			
		}
				
	},
	makeSkin3Top:function(items,gid,position){
		if(!items){return;}
		var mainToolbar;
		if(position == "top"){
			mainToolbar = jQuery("#"+this.opt.tabID);
		}else{
			mainToolbar = jQuery("#"+this.opt.contentID);
		}
		var mainGroup = jQuery("<div></div>").addClass("menu_group");
		var toolItem;
		for ( var i = 0; i < items.length; i++) {
			
			if(items[i].type == "B"){
				toolItem = items[i].Item;				
				mainGroup.append(toolItem.makeTopItem());
				/*if(items[i].Bar == "Y"){
					var barItem = "<div id='"+toolItem.getId()+"_bar' style='float:left;padding:0 5px'>|</div>";
					bodyGroup.append(barItem);
				}*/
				this.ItemHash.set(toolItem.getId(), toolItem);				
				
			}else if(items[i].type == "T"){
				var wrapItem = jQuery("<div></div>").addClass("stacked_sub_text");
				toolItem = items[i].Item;
				for ( var j = 0; j < toolItem.length; j++) {
					wrapItem.append(toolItem[j].makeItem());
					this.ItemHash.set(toolItem[j].getId(), toolItem[j]);
				}
				mainGroup.append(wrapItem);
			}
				
			
		}
		
		mainGroup.attr("id",gid);
		mainToolbar.append(mainGroup);
		this.groupHash.set(gid,gid);
		
	},
	makeGroup:function(items,gid){
		if(!items){return;}
		var mainToolbar = jQuery("#"+this.opt.contentID);
		var mainGroup = jQuery("<div></div>").addClass("menu_group");
		var bodyGroup =  jQuery("<span></span>").addClass("g_body");
		var toolItem;
		for ( var i = 0; i < items.length; i++) {
			if(items[i].type == "B"){
				toolItem = items[i].Item;
				bodyGroup.append(toolItem.makeItem());
				this.ItemHash.set(toolItem.getId(), toolItem);
			} else if(items[i].type == "S"){
				var wrapItem = jQuery("<div></div>").addClass("stacked_sub_unit");
				toolItem = items[i].Item;				
				for ( var j = 0; j < toolItem.length; j++) {
					wrapItem.append(toolItem[j].makeItem());
					this.ItemHash.set(toolItem[j].getId(), toolItem[j]);
				}								
				bodyGroup.append(wrapItem);				
			} else if(items[i].type == "T"){
				var wrapItem = jQuery("<div></div>").addClass("stacked_sub_text");
				toolItem = items[i].Item;
				for ( var j = 0; j < toolItem.length; j++) {
					wrapItem.append(toolItem[j].makeItem());
					this.ItemHash.set(toolItem[j].getId(), toolItem[j]);
				}
				bodyGroup.append(wrapItem);	
			}
		}
		
		mainGroup.append(bodyGroup);
		mainGroup.attr("id",gid);
		mainToolbar.append(mainGroup);
		this.groupHash.set(gid,gid);
		
	},
	makeAutoSaveItem:function(){
		if(this.sideItem){
			jQuery("#autoSaveArea").empty();
			/*var mainToolbar = jQuery(".mail_body_tabmenu");
			var mainGroup = jQuery("<div></div>").css({"float":"right","padding":"7px"});
			mainGroup.attr("id","toolbarSide");
			var bodyGroup =  jQuery("<span></span>");
			
			bodyGroup.append(this.sideItem);
			
			mainGroup.append(this.sideItem);
			mainToolbar.append(mainGroup);*/
			
			var autoSaveArea = jQuery("#autoSaveArea").append(this.sideItem);
			
			
		}
		if(this.sideMenuVisble){
			jQuery("#autoSaveArea").show();
		} else {
			jQuery("#autoSaveArea").hide();
		}		
	},
	makeSideItem:function(){
		if(this.sideItem){
			var mainToolbar = jQuery("#"+this.opt.contentID);
			var mainGroup = jQuery("<div></div>").addClass("menu_side_group");
			mainGroup.attr("id","toolbarSide");
			var bodyGroup =  jQuery("<span></span>").addClass("g_body");
			
			bodyGroup.append(this.sideItem);
			
			mainGroup.append(bodyGroup);
			mainToolbar.append(mainGroup);
		}
		
		if(this.sideMenuVisble){
			jQuery("#toolbarSide").show();
		} else {
			jQuery("#toolbarSide").hide();
		}		
	},	
	clearToolBar: function(){
		this.ItemHash = new Hash();
		this.groupHash = new Hash();
		jQuery("#"+this.opt.tabID).empty();
		jQuery("#"+this.opt.contentID).empty();		
	},
	disableToolBarItem:function(key){
		var item = this.ItemHash.get(key);
		item.disableItem(true);
	},
	enableToolBarItem:function(key){
		var item = this.ItemHash.get(key);
		item.disableItem(false);
	},
	setPageNavi : function(mode, param){
		jQuery("#"+this.opt.navigationID).pageNavigation(mode,param);
	},
	setPageMenuNavi : function(mode, param){
		jQuery("#"+this.opt.navigationMenuID).pageNavigation(mode,param);
	},
	setPageNaviBottom : function(mode, param){	
		jQuery("#"+this.opt.navigationBottomID).pageBottomNavigation(mode,param);
	},
	toggleSideItem:function(flag){
		this.sideMenuVisble = flag;
		if(flag){
			jQuery("#toolbarSide").show();
		} else {
			jQuery("#toolbarSide").hide();
		}
	},
	hideMenu:function(key){
		var item = this.ItemHash.get(key);
		item.hideMenu();
	},
	showMenu:function(key){
		var item = this.ItemHash.get(key);
		item.showMenu();
	},
	hideSkin3MenuBar:function(key){
		var item = this.ItemHash.get(key);
		item.hideSkin3MenuBar();
	},
	showSkin3MenuBar:function(key){
		var item = this.ItemHash.get(key);
		item.showSkin3MenuBar();
	},
	initToolBar:function(){
		var itemKeys = this.ItemHash.keys();
		var groupKeys = this.groupHash.keys();
		
		for(var i = 0 ; i < itemKeys.length ; i++){
			this.enableToolBarItem(itemKeys[i]);
			this.showMenu(itemKeys[i]);
		}		
		for(var j = 0 ; j < groupKeys.length ; j++){			
			jQuery("#"+groupKeys[j]).show();			
		}		
	},
	initToolBarSkin3:function(){
		var itemKeys = this.ItemHash.keys();
		var groupKeys = this.groupHash.keys();
		
		for(var i = 0 ; i < itemKeys.length ; i++){
			this.enableToolBarItem(itemKeys[i]);
			this.showMenu(itemKeys[i]);
			this.showSkin3MenuBar(itemKeys[i]);
		}		
		for(var j = 0 ; j < groupKeys.length ; j++){			
			jQuery("#"+groupKeys[j]).show();			
		}		
	}
});


var MenuItem = Class.create({
	initialize : function(id, itemClass, name, type, link, isSubMenu){
		this.id = id;
		this.itemClass = itemClass;
		this.name = name;
		this.type = type;
		this.link = link;
		this.isSubMenu = isSubMenu;
		this.flag;
		this.blankImgSrc = "/design/common/image/blank.gif";
	},
	getId : function(){
		return this.id;
	},
	makeItem : function(){
		var item;	
		
		var linkTag = jQuery("<a href='javascript:;'></a>");
		linkTag.attr("id",this.id + "_item_link");
		linkTag.attr("sid",this.id);
		var linkFunc = this.link;
		linkTag.click(function(e){linkFunc(jQuery(this));});
		linkTag.addClass("micon");
		var wspan = jQuery("<span></span>");
		wspan.addClass("micon_body");
		var imgtag = jQuery("<img>");
		imgtag.addClass("menu_icon_img");
		imgtag.attr("id",this.id + "_item_img");		
		imgtag.removeAttr("height");
		imgtag.removeAttr("width");
		imgtag.attr("src",this.blankImgSrc);		
		imgtag.addClass((this.type == "B")?"itemBSize":"itemSSize");
		imgtag.addClass(this.itemClass);
		var tspan = jQuery("<span></span>");
		tspan.addClass("menu_text");
		tspan.addClass("jpf");
		tspan.append(this.name);
		var rspan = jQuery("<span></span>").addClass("micon_right");
		
		wspan.append(imgtag);
		wspan.append(tspan);
		
		wspan.hover(
				function(){
					jQuery(this).addClass("micon_body_over");
					jQuery(this).parent().find(".micon_right").addClass("micon_body_over");
				}
				,function(){
					jQuery(this).removeClass("micon_body_over");
					jQuery(this).parent().find(".micon_right").removeClass("micon_body_over");
				}
		);
		linkTag.append(wspan);
		linkTag.append(rspan);
		
		
		
		if(this.type == "B"){
			item = jQuery("<div></div>");
			item.attr("id",this.id + "_item");
			item.addClass("menu_basic_unit");
			item.append(linkTag);
		} else if(this.type == "S"){
			item = jQuery("<div></div>").append(linkTag);
		}
		
		if(this.isSubMenu){
			item.append(jQuery("<div></div>").attr("id",this.id + "_sub").css({"position":"relative"}));
		}
		
		this.flag = false; 
		return item;		
	},	
	makeTopItem : function(){
		var item;	
		
		//var linkTag = jQuery("<a href='#'></a>");
		var linkTag = jQuery("<span></span>");
		linkTag.attr("id",this.id + "_item_link");
		linkTag.attr("sid",this.id);
		var linkFunc = this.link;
		linkTag.click(function(e){linkFunc(jQuery(this));});
		linkTag.addClass("micon");
			
		/*var imgtag = jQuery("<img>");
		imgtag.addClass("menu_icon_img");
		imgtag.attr("id",this.id + "_item_img");		
		imgtag.removeAttr("height");
		imgtag.removeAttr("width");
		imgtag.attr("src",this.blankImgSrc);		
		imgtag.addClass((this.type == "B")?"itemBSize":"itemSSize");
		imgtag.addClass(this.itemClass);*/
		var tspan;
		if(this.type.indexOf("M") > -1){
			linkTag.addClass("tabLayer");
			tspan = jQuery("<span></span>");
		}else{
			tspan = jQuery("<div class='button'></div>");
		}
		tspan.addClass("menu_text");
		tspan.addClass("jpf");
		if(this.isSubMenu && this.id != "forward"){
			tspan.addClass("menuArrow");
			tspan.hover(function(){jQuery(this).css("background-color","#DADADA");},
					function(){jQuery(this).css("background-color","#F6F8FA");});
		}
		
		tspan.append("<span>"+this.name+"</span>");
		if(this.id == "forward"){
			tspan.append("<img src='/design/skin3/image/tab/ic_arrow_down.gif' style='vertical-align:middle'>");
		}else{
			tspan.append("<img src='/design/common/image/blank.gif' style='vertical-align:middle'>");
		}
		//var rspan = jQuery("<span></span>").addClass("micon_right");
		
		//wspan.append(imgtag);
		
		
		linkTag.append(tspan);
		
		
		item = jQuery("<div></div>");
		item.attr("id",this.id + "_item");
		if(this.type.indexOf("B") > -1){
			item.addClass("menu_basic_unit");
		}else if(this.type.indexOf("O") > -1){
			item.addClass("menu_option_unit");
		}
		if(this.id != "forward"){
			if(this.isSubMenu){
				item.append(jQuery("<div></div>").attr("id",this.id + "_sub").css({"position":"relative"}));
			}
		}
		
		item.append(linkTag);
		
		if(this.type.indexOf("S") > -1){
			var barTag = jQuery("<span class='tabLayer topPadding'>|</span>");
			barTag.attr("id",this.id + "_item_link_bar");
			item.append(barTag);
		}
		if(this.id == "forward"){
			if(this.isSubMenu){
				item.append(jQuery("<div></div>").attr("id",this.id + "_sub").css({"position":"relative"}));
			}
		}	
		this.flag = false; 
		return item;		
	},
	disableItem : function(disable){
		var linkTag = jQuery("#"+this.id + "_item_link");
		var imgObj = jQuery("#"+this.id + "_item_img");
		if(this.flag == disable){
			return;
		}
		
		this.flag =  disable;		
		if(disable){
			linkTag.unbind("click");
			linkTag.removeClass("micon");
			linkTag.addClass("micoff");
			linkTag.css("cursor","text");
			imgObj.removeClass(this.itemClass);
			imgObj.addClass(this.itemClass+"_un");			
		} else { 
			var linkFunc = this.link;			
			linkTag.removeClass("micoff");			
			linkTag.click(function(e){linkFunc(jQuery(this));});
			linkTag.addClass("micon");
			linkTag.css("cursor","pointer");
			imgObj.removeClass(this.itemClass+"_un");
			imgObj.addClass(this.itemClass);			
		}		
	},
	hideMenu:function(){
		var linkTag = jQuery("#"+this.id + "_item_link");
		linkTag.hide();
		if(skin == "skin3"){
			jQuery("#"+this.id + "_item").css("margin-right","0px");
		}
		
	},
	showMenu:function(){
		var linkTag = jQuery("#"+this.id + "_item_link");
		linkTag.show();
		if(skin == "skin3"){
			jQuery("#"+this.id + "_item").css("margin-right","4px");
		}
	},
	hideSkin3MenuBar:function(){
		var barTag = jQuery("#"+this.id + "_item_link_bar");
		barTag.hide();
		
	},
	showSkin3MenuBar:function(){
		var barTag = jQuery("#"+this.id + "_item_link_bar");
		barTag.show();
	}
});

var TextItem = Class.create({
	initialize : function(id, html){
		this.id = id;		
		this.html = html;
		this.flag;
	},
	getId : function(){
		return this.id;
	},
	makeItem : function(){
		var item;
		
		item = jQuery("<p></p>");		
		item.attr("id",this.id + "_item_link");		
		item.append(this.html);		
		this.flag = false; 
		return item;
	},	
	disableItem : function(disable){
		var itemTag = jQuery("#"+this.id + "_item_link");		
		if(this.flag == disable){
			return;
		}
		
		this.flag =  disable;		
		if(disable){
			itemTag.find("input").attr("disabled","true");		
		} else { 
			itemTag.find("input").attr("disabled","false");
		}
	}
});


var SubToolbarList = Class.create({
	initialize : function(opt){
		this.opt = opt;
	},
	getList:function(){
		var subList = this.opt.list;
		var wSize = this.opt.size;
		wSize = (wSize)?parseInt(wSize):0;
		
		var listWrapper = jQuery("<div>");
		listWrapper.addClass("TM_subMenuBG");
		if(wSize > 0){
			listWrapper.css("width",(wSize+5)+"px");
		}
		
		for ( var i = 0; i < subList.length; i++) {
			var linkFunc = subList[i].linkFunc;
			var linkParam = subList[i].param;
			var item = jQuery("<div>");
			item.addClass("sub_item");
			var link;
			if(linkParam == "split"){
				link = jQuery("<div></div>").css({"margin-left":"5px"});				
			}else if(linkParam == "preview"){
				link = jQuery("<span></span>");
				link.addClass("sitem");
				link.attr("param",linkParam);				
			}else{
				link = jQuery("<a href='javascript:;'></a>");
				link.addClass("sitem");
				link.attr("param",linkParam);		
				link.click(function(){linkFunc(jQuery(this))});
			}
			var linkSpan = jQuery("<span></span>");
			if(wSize > 0){
				item.css("width",(wSize+5)+"px");
				link.css("width",(wSize+5)+"px");
				if(linkParam == "split"){					
					item.css("width",(wSize-5)+"px");
					link.css({"width":(wSize-5)+"px","margin-left":"5px","height":"1px","border-top":"1px solid #3F5E8A"});
					linkSpan.addClass("item_body").addClass("jpf").css("line-height","1px");
				}else if(linkParam == "preview"){
					item.css({"height":"20px","line-height":"20px","margin-left":"5px","text-align":"left"});
					linkSpan.addClass("item_body").addClass("jpf").css("width",(wSize-5)+"px").append(subList[i].name);
				}else{
					linkSpan.addClass("item_body").addClass("jpf").css("width",(wSize-5)+"px")
					.hover(function(){jQuery(this).addClass("item_body_over");},
							function(){jQuery(this).removeClass("item_body_over");}).append(subList[i].name);
				}
				link.append(linkSpan);
			} else {
				if(linkParam == "preview"){
					linkSpan.addClass("item_body").addClass("jpf").append(subList[i].name);
				}else{
					linkSpan.addClass("item_body").addClass("jpf")
					.hover(function(){jQuery(this).addClass("item_body_over");},
							function(){jQuery(this).removeClass("item_body_over");}).append(subList[i].name);
				}
				link.append(linkSpan);
			}		
			/*if(linkParam == "preview"){
				item.css({"height":"20px","line-height":"20px"});				
			}*/
				
			item.append(link);
			listWrapper.append(item);			
			
		}		
		return listWrapper;
	}	
});


