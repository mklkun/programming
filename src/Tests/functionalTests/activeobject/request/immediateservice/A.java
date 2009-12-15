/*
 * ################################################################
 *
 * ProActive: The Java(TM) library for Parallel, Distributed,
 *            Concurrent computing with Security and Mobility
 *
 * Copyright (C) 1997-2009 INRIA/University of 
 * 						   Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2. 
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 * $$PROACTIVE_INITIAL_DEV$$
 */
package functionalTests.activeobject.request.immediateservice;

import java.io.Serializable;

import org.objectweb.proactive.api.PAActiveObject;
import org.objectweb.proactive.core.util.wrapper.BooleanWrapper;


public class A implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 420L;

    private Thread myServiceThread;

    /**
     *
     */
    DummyObject dum;

    public A() {
    }

    public int init() {
        PAActiveObject.setImmediateService("getBooleanSynchronous");
        PAActiveObject.setImmediateService("getBooleanAsynchronous");
        PAActiveObject.setImmediateService("getObject");
        this.myServiceThread = Thread.currentThread();
        return 0;
    }

    public A(String name) {
        this.dum = new DummyObject(name);
    }

    public DummyObject getObject() {
        return dum;
    }

    public boolean getBooleanSynchronous() {
        return (!Thread.currentThread().equals(myServiceThread) && myServiceThread != null);
    }

    public BooleanWrapper getBooleanAsynchronous() {
        return new BooleanWrapper(!Thread.currentThread().equals(myServiceThread) && myServiceThread != null);
    }

    public boolean getExceptionMethodArgs() {
        try {
            PAActiveObject.setImmediateService("getObject", new Class<?>[] { Integer.class });
        } catch (NoSuchMethodError e) {
            return true;
        } catch (Throwable t) {
            return false;
        }
        return false;
    }

    public boolean getExceptionMethodName() {
        try {
            PAActiveObject.setImmediateService("britney");
        } catch (NoSuchMethodError e) {
            return true;
        } catch (Throwable t) {
            return false;
        }
        return false;
    }

}
