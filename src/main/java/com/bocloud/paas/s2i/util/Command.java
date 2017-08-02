package com.bocloud.paas.s2i.util;

public interface Command {
	public final static String MOUNTALL = "/bin/mount -a";

	public final static String MAKEDIR = "mkdir -p ";

	public final static String MOVE = "mv -f ";

	public final static String COPY = "cp -r ";

	public final static String DELETE = "rm ";

}
