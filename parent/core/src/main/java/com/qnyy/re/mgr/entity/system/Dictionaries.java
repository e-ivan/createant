package com.qnyy.re.mgr.entity.system;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 
* 类名称：数据字典
* 类描述： 
* @author FH QQ 313596790[青苔]
* 作者单位： 
* 联系方式：
* 修改时间：2015年12月16日
* @version 2.0
 */
@Getter@Setter
public class Dictionaries {

	private String NAME;			//名称
	private String NAME_EN;			//英文名称
	private String BIANMA;			//编码
	private String ORDER_BY;		//排序	
	private String PARENT_ID;		//上级ID
	private String BZ;				//备注
	private String TBSNAME;			//关联表
	private String DICTIONARIES_ID;	//主键
	private String target;
	private Dictionaries dict;
	private List<Dictionaries> subDict;
	private boolean hasDict = false;
	private String treeurl;
}
