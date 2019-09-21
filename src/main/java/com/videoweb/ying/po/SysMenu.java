package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;



@TableName("sys_menu")
@SuppressWarnings("serial")
public class SysMenu extends BaseModel {

      /**
     * 菜单上级id
     */
         	   @TableField("parent_id")
        	private Integer parentId;
      /**
     * 菜单名字
     */
            	private String name;
      /**
     * 菜单链接地址
     */
            	private String url;
      /**
     * 授权(多个用逗号分隔，如：user:list,user:create) 
     */
            	private String permission;
      /**
     * 类型0:目录1：菜单2：按钮
     */
         	   @TableField("menu_type")
        	private Integer menuType;
      /**
     * 菜单图标 
     */
            	private String icon;
      /**
     * 菜单排序
     */
         	   @TableField("sort_no")
        	private Integer sortNo;
      /**
     * 展开状态  0收缩  1  展开
     */
         	   @TableField("is_expend")
        	private Integer isExpend;
      /**
     * 叶子结点
     */
         	   @TableField("is_show")
        	private Integer isShow;
      /**
     * 是否可用  0 不可用 1可用
     */
         	   @TableField("is_enable")
        	private Integer isEnable;


	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Integer getMenuType() {
		return menuType;
	}

	public void setMenuType(Integer menuType) {
		this.menuType = menuType;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Integer getIsExpend() {
		return isExpend;
	}

	public void setIsExpend(Integer isExpend) {
		this.isExpend = isExpend;
	}

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public Integer getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

}