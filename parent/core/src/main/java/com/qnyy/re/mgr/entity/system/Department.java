package com.qnyy.re.mgr.entity.system;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 
* 类名称：组织机构
* 类描述： 
* @author FH QQ 313596790[青苔]
* 作者单位： 
* 联系方式：
* 修改时间：2015年12月16日
* @version 2.0
 */
@Getter@Setter
public class Department {

	private String NAME;			//名称
	private String NAME_EN;			//英文名称
	private String BIANMA;			//编码
	private String PARENT_ID;		//上级ID
	private String HEADMAN;			//负责人
	private String TEL;				//电话
	private String FUNCTIONS;		//部门职能
	private String BZ;				//备注
	private	String ADDRESS;			//地址
	private String DEPARTMENT_ID;	//主键
	private String target;
	private Department department;
	private List<Department> subDepartment;
	private boolean hasDepartment = false;
	private String treeurl;
}