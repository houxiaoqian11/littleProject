package cn.guddqs.peakshop.view.entity;

import cn.guddqs.peakshop.entity.Size;

public class ViewSize extends Size {

	public ViewSize(Size size){
		this.setId(size.getId());
		this.setName(size.getName());
	}
	
	private Boolean checked;

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	
}
