package cn.guddqs.peakshop.view.entity;

public class OrderStatus {
	private Integer id;
	private String desc;
	
	public OrderStatus(Integer id, String desc) {
		this.id = id;
		this.desc = desc;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
