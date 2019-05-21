package com.jemer.atong.entity.history;

import java.util.List;

public class HistoryEntity {
	public int code;
	public String cost;
	public String msg;
	public Data data;

	public static class Data {
		public List<EyesightHis> list;
		public static class EyesightHis {
			public String lefteye;
			public String leftresult;
			public String righteye;
			public String rightresult;
			public String createtime;
		}
	}


}
