package top.onceio.mybatis.common;


public class BaseEntity {
	private Long id;
	private boolean rm;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public boolean isRm() {
		return rm;
	}
	public void setRm(boolean rm) {
		this.rm = rm;
	}

}
