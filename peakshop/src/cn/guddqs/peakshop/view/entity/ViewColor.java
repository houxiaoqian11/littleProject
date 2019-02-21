package cn.guddqs.peakshop.view.entity;

import cn.guddqs.peakshop.entity.Color;

public class ViewColor extends Color{

	private Boolean checked;
	
	public ViewColor(Color color){
		this.setId(color.getId());
		this.setName(color.getName());
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

}
