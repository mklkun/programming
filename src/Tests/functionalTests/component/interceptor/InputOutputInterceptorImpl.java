/*
 * ################################################################
 *
 * ProActive: The Java(TM) library for Parallel, Distributed,
 *            Concurrent computing with Security and Mobility
 *
 * Copyright (C) 1997-2009 INRIA/University of Nice-Sophia Antipolis
 * Contact: proactive@ow2.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version
 * 2 of the License, or any later version.
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
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 * $$PROACTIVE_INITIAL_DEV$$
 */
package functionalTests.component.interceptor;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.factory.InstantiationException;
import org.objectweb.fractal.api.type.TypeFactory;
import org.objectweb.proactive.core.ProActiveRuntimeException;
import org.objectweb.proactive.core.component.controller.AbstractProActiveController;
import org.objectweb.proactive.core.component.type.ProActiveTypeFactoryImpl;
import org.objectweb.proactive.core.mop.MethodCall;

import functionalTests.component.controller.DummyController;


/**
 * @author The ProActive Team
 *
 */
public class InputOutputInterceptorImpl extends AbstractProActiveController implements InputOutputInterceptor {

    /**
     *
     */

    /**
     * 
     */
    private static final long serialVersionUID = 41L;

    /**
     * @param owner
     */
    public InputOutputInterceptorImpl(Component owner) {
        super(owner);
    }

    @Override
    protected void setControllerItfType() {
        try {
            setItfType(ProActiveTypeFactoryImpl.instance().createFcItfType(
                    InputOutputInterceptor.INPUT_OUTPUT_INTERCEPTOR_NAME,
                    InputOutputInterceptor.class.getName(), TypeFactory.SERVER, TypeFactory.MANDATORY,
                    TypeFactory.SINGLE));
        } catch (InstantiationException e) {
            throw new ProActiveRuntimeException("cannot create controller " + this.getClass().getName());
        }
    }

    public void setDummyValue(String value) {
        try {
            ((DummyController) getFcItfOwner().getFcInterface(DummyController.DUMMY_CONTROLLER_NAME))
                    .setDummyValue(value);
        } catch (NoSuchInterfaceException e) {
            e.printStackTrace();
        }
    }

    public String getDummyValue() {
        try {
            return ((DummyController) getFcItfOwner().getFcInterface(DummyController.DUMMY_CONTROLLER_NAME))
                    .getDummyValue();
        } catch (NoSuchInterfaceException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void afterInputMethodInvocation(MethodCall methodCall) {
        //System.out.println("after method invocation");
        setDummyValue(getDummyValue() + InputOutputInterceptor.AFTER_INPUT_INTERCEPTION);
    }

    public void beforeInputMethodInvocation(MethodCall methodCall) {
        //        System.out.println("before method invocation");
        setDummyValue(getDummyValue() + InputOutputInterceptor.BEFORE_INPUT_INTERCEPTION);
    }

    public void afterOutputMethodInvocation(MethodCall methodCall) {
        setDummyValue(getDummyValue() + InputOutputInterceptor.AFTER_OUTPUT_INTERCEPTION);
    }

    public void beforeOutputMethodInvocation(MethodCall methodCall) {
        setDummyValue(getDummyValue() + InputOutputInterceptor.BEFORE_OUTPUT_INTERCEPTION);
    }
}
