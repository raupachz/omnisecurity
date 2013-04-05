/*
 * Copyright 2013 OmniFaces.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.omnifaces.security.jaspic;


import static javax.security.auth.message.AuthStatus.SEND_FAILURE;

import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;

public class AuthResult {

	private AuthStatus authStatus = SEND_FAILURE;
	private Exception exception;

	public AuthStatus getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(AuthStatus authStatus) {
		this.authStatus = authStatus;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}
	
	public boolean isFailed() {
		return authStatus == SEND_FAILURE;
	}
	
	public void add(AuthResult authResult) {
		if (!authResult.isFailed()) {
			authStatus = authResult.getAuthStatus();
		} else if (authResult.getException() != null) {
			if (exception == null) {
				exception = authResult.getException();
			} else {
				exception.addSuppressed(authResult.getException());
			}
		}
	}
	
	public AuthStatus throwOrReturnStatus() throws AuthException {
		maybeThrow();		
		return authStatus;
	}
	
	public AuthStatus throwOrFail() throws AuthException {
		maybeThrow();
		return SEND_FAILURE;
	}
	
	private void maybeThrow() throws AuthException {
		if (exception != null) {
			AuthException authException = new AuthException();
			authException.initCause(exception);
			throw authException;
		}
	}
	
}
