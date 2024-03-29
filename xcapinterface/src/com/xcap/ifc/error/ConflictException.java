/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.xcap.ifc.error;

public abstract class ConflictException extends RequestException {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConflictException() {
		super();
	}
	
	public ConflictException(String msg) {
		super(msg);
	}
	
	public ConflictException(String msg, Throwable t) {
		super(msg,t);
	}
	
	public int getResponseStatus() {
		return 409;
	}
	
	protected abstract String getConflictError();
	
	public String getResponseContent() {
		StringBuffer sb = new StringBuffer("<?xml version='1.0' encoding='UTF-8'?><xcap-error xmlns='urn:ietf:params:xml:ns:xcap-error'>");
		sb.append(getConflictError());
		sb.append("</xcap-error>");
		return sb.toString();
	}
		
}
