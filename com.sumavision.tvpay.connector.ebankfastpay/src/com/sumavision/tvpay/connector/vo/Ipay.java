package com.sumavision.tvpay.connector.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ipay")
public class Ipay {
	
	private Head head;
	
	private Body body;

	public Head getHead() {
		return head;
	}

	public void setHead(Head head) {
		this.head = head;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

}
