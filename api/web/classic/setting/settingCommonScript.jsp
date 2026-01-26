setTopMenu('setting');

if(MENU_STATUS.calendar && MENU_STATUS.calendar == "on")jQuery("#scheduler_leftmenu").show();
else jQuery("#scheduler_leftmenu").hide();

var mainLayerPane = new LayerPane("s_mainBody","TM_m_mainBody");	
var menuLayerPane = new LayerPane("s_leftMenuContent","TM_m_leftMenu",220,220,350);
var contentLayerPaneWapper = new LayerPane("s_contentBodyWapper","",300,100,500);


mainSplitter = new SplitterManager(mainLayerPane,
									menuLayerPane,
									contentLayerPaneWapper,
									"sm","mainvsplitbar","hsplitbar");	
mainSplitter.setReferencePane(["s_contentBody","copyRight"]);	
mainSplitter.setSplitter("v",true);
jQuery(window).autoResize(jQuery("#s_mainBody"),"#copyRight");

if(IS_LMENU_USE){loadSideMenu();}
resizeLeftMenuSize();

var contentLayerPane = new LayerPane("s_contentBody","TM_m_contentBody");
var listLayerPane = new LayerPane("s_contentMain","TM_m_contentMain",300,0,0);
var previewLayerPane = new LayerPane("s_contentSub","TM_m_contentSub",400,0,0);		

contentSplitter = new SplitterManager(contentLayerPane,
		listLayerPane,
		previewLayerPane,
		"sc","vsplitbar","hsplitbar");

contentSplitter.setSplitter("n",false);
jQuery(window).autoResize(jQuery("#s_contentBody"),"#copyRight");
contentSplitter.setSplitter("n",false);

jQuery(window).resizeInnerFrame({resizeId:"#main_wrapper", 
		mainId:"#s_contentMain", 
		sideObjId:["#copyRight"], 
		isNoneWidthChk:false,				 
		extHeight:false});