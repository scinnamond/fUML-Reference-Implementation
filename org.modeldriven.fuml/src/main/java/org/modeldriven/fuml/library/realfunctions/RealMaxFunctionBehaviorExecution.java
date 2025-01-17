/*
 * Copyright 2008 Lockheed Martin Corporation, except as stated in the file 
 * entitled Licensing-Information. 
 * 
 * All modifications copyright 2009-2012 Data Access Technologies, Inc.
 * 
 * Licensed under the Academic Free License version 3.0 
 * (http://www.opensource.org/licenses/afl-3.0.php), except as stated 
 * in the file entitled Licensing-Information. 
 *
 * Contributors:
 *   MDS - initial API and implementation
 *
 */

package org.modeldriven.fuml.library.realfunctions;

import java.util.List;

import fuml.Debug;

public class RealMaxFunctionBehaviorExecution extends
        RealFunctionBehaviorExecution {

    public Float doRealFunction(List<Float> arguments) {

    	float x1 = arguments.get(0);
    	float x2 = arguments.get(1);
    	
    	float x = (x1 >= x2)? x1: x2;
    	
     	Debug.println("[doBody] Real Max result = " + x);
    	return x;
    }

    public fuml.semantics.values.Value new_() {
        // Create a new instance of this kind of function behavior execution.
        return new RealMaxFunctionBehaviorExecution();
    }

} 