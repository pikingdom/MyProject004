package com.nd.hilauncherdev.kitset.util.elec;

public class PsRow {
	private int rootpid = -1;
	private String user;
	private int pid = -1;
	private String ppid;
	private int mem;
	private String cmd;

	public PsRow(String line) {
		if (line == null)
			return;
		String[] p = line.split("[\\s]+");
		if (p.length != 9)
			return;
		user = p[0];
		pid = Integer.parseInt(p[1]);
		ppid = p[2];
		cmd = p[8];
		mem = StrUtil.parseInt(p[4]);
		if (isRoot()) {
			rootpid = pid;
		}
	}

	public String getCmd() {
		return cmd;
	}

	public int getMem() {
		return mem;
	}

	public int getPid() {
		return pid;
	}

	public String getPpid() {
		return ppid;
	}

	public int getRootpid() {
		return rootpid;
	}

	public String getUser() {
		return user;
	}

	public boolean isMain() {
		return ppid.equals(rootpid) && user.startsWith("app_");
	}

	public boolean isRoot() {
		return "zygote".equals(cmd);
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public void setMem(int mem) {
		this.mem = mem;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public void setPpid(String ppid) {
		this.ppid = ppid;
	}

	public void setRootpid(int rootpid) {
		this.rootpid = rootpid;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String toString() {
		final String TAB = ";";
		String retValue = "";
		retValue = "PsRow ( " + super.toString() + TAB + "pid = " + this.pid
				+ TAB + "cmd = " + this.cmd + TAB + "ppid = " + this.ppid + TAB
				+ "user = " + this.user + TAB + "mem = " + this.mem + " )";

		return retValue;
	}

}
