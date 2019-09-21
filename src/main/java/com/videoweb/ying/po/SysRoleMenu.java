package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;



@TableName("sys_role_menu")
@SuppressWarnings("serial")
public class SysRoleMenu extends BaseModel {

      /**
     * 角色ID
     */
         	   @TableField("role_id")
        	private Integer roleId;
      /**
     * 菜单表ID
     */
         	   @TableField("menu_id")
        	private Integer menuId;


	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

}