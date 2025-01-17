/*
 * Copyright 2008 Lockheed Martin Corporation, except as stated in the file 
 * entitled Licensing-Information. All modifications copyright 2009 Data Access Technologies, Inc. Licensed under the Academic Free License 
 * version 3.0 (http://www.opensource.org/licenses/afl-3.0.php), except as stated 
 * in the file entitled Licensing-Information. 
 *
 * Contributors:
 *   MDS - initial API and implementation
 *
 */
package org.modeldriven.fuml.environment;

import org.modeldriven.fuml.FumlException;

public class EnvironmentException extends FumlException {
	
	public EnvironmentException(Throwable t) {
		super(t);
	}
    public EnvironmentException(String msg) {
        super(msg);
    }

}
