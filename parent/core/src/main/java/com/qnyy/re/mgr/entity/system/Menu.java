package com.qnyy.re.mgr.entity.system;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
/**
 * 
* 类名称：菜单
* 类描述： 
* @author FH QQ 313596790[青苔]
* 作者单位： 
* 联系方式：
* 创建时间：2015年7月27日
* @version 2.0
 */
@Getter@Setter
public class Menu {
	
	private String MENU_ID;		//菜单ID
	private String MENU_NAME;	//菜单名称
	private String MENU_URL;	//链接
	private String PARENT_ID;	//上级菜单ID
	private String MENU_ORDER;	//排序
	private String MENU_ICON;	//图标
	private String MENU_TYPE;	//类型
	private String MENU_STATE;	//菜单状态
	private String target;
	private Menu parentMenu;
	private List<Menu> subMenu;
	private boolean hasMenu = false;
}
