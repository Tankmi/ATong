package com.jemer.atong.net.select_photo;

/**
 * 选择图片
 * @author ZhuTao
 * @date 2018/11/29
 * @params
*/

public class PhotoEntity {

	public String name;                 // 图片名
	public String path;                 // 图片路径
	public long time;                   // 图片添加时间
	public Boolean checked;             //checkbox  选中状态

	public PhotoEntity(String path, String name, long time , Boolean checked) {
		this.path = path;
		this.name = name;
		this.time = time;
		this.checked =checked;
	}


	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	@Override
	public String toString() {
		return "PhotoInfo{" +
				"name='" + name + '\'' +
				", path='" + path + '\'' +
				", time=" + time +
				'}';
	}

	@Override
	public boolean equals(Object object) {
		try {
			PhotoEntity other = (PhotoEntity) object;
			return this.path.equalsIgnoreCase(other.path);
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		return super.equals(object);
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
