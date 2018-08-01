package com.nd.hilauncherdev.dynamic.execption;

public class WidgetClientExecption extends Exception {

	/** 
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;

	public WidgetClientExecption() {
		super();
	}

	public WidgetClientExecption(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public WidgetClientExecption(String detailMessage) {
		super(detailMessage);
	}

	public WidgetClientExecption(Throwable throwable) {
		super(throwable);
	}

}
