package com.qnyy.re.mgr.entity.system;

import lombok.Getter;
import lombok.Setter;

/**
 * 
* 类名称：角色
* 类描述： 
* @author FH QQ 313596790[青苔]
* 作者单位： 
* 联系方式：
* 创建时间：2014年3月10日
* @version 1.0
 */
@Setter@Getter
public class Role {
	private String ROLE_ID;
	private String ROLE_NAME;
	private String RIGHTS;
	private String PARENT_ID;
	private String ADD_QX;
	private String DEL_QX;
	private String EDIT_QX;
	private String CHA_QX;
}
