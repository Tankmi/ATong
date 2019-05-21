package com.jemer.atong.entity.history;

import java.io.Serializable;

public class PointLineTableBean implements Serializable {
	public String time;
	public String data;

	public PointLineTableBean(String time, String data) {
		this.time = time;
		this.data = data;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
