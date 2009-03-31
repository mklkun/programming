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
package org.objectweb.proactive.core.component.controller;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.Type;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.api.control.LifeCycleController;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.proactive.core.ProActiveRuntimeException;
import org.objectweb.proactive.core.body.migration.MigrationException;
import org.objectweb.proactive.core.component.Constants;
import org.objectweb.proactive.core.component.identity.ProActiveComponent;
import org.objectweb.proactive.core.node.Node;
import org.objectweb.proactive.core.util.log.Loggers;
import org.objectweb.proactive.core.util.log.ProActiveLogger;


/**
 * Base class for all component controllers.
 *
 * @author The ProActive Team
 *
 */
public abstract class AbstractProActiveController implements ProActiveController, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 41L;
    private boolean isInternal = true;
    private InterfaceType interfaceType;
    final protected static Logger controllerLogger = ProActiveLogger
            .getLogger(Loggers.COMPONENTS_CONTROLLERS);

    protected ProActiveComponent owner;

    /**
     * Constructor for AbstractProActiveController.
     * 
     * @param owner the component that wants this controller is in the {@link Constants} class.
     */
    public AbstractProActiveController(Component owner) {
        this.owner = (ProActiveComponent) owner;
        setControllerItfType();
    }

    /**
     * Default void implementation of the the initController method. A controller requiring 
     * initialization *after* all interfaces are instantiated have to override this method.
     * 
     * @see org.objectweb.proactive.core.component.controller.ProActiveController#initController()
     */
    public void initController() {
    }

    /*
     * see {@link org.objectweb.fractal.api.Interface#isFcInternalItf()}
     */
    public boolean isFcInternalItf() {
        return isInternal;
    }

    /*
     * see {@link org.objectweb.fractal.api.Interface#getFcItfName()}
     */
    public String getFcItfName() {
        return interfaceType.getFcItfName();
    }

    /*
     * see {@link org.objectweb.fractal.api.Interface#getFcItfType()}
     */
    public Type getFcItfType() {
        return interfaceType;
    }

    public Component getFcItfOwner() {
        return owner;
    }

    /*
     * some control operations are to be performed while the component is stopped
     */
    protected void checkLifeCycleIsStopped() throws IllegalLifeCycleException {
        try {
            if (!((LifeCycleController) getFcItfOwner().getFcInterface(Constants.LIFECYCLE_CONTROLLER))
                    .getFcState().equals(LifeCycleController.STOPPED)) {
                throw new IllegalLifeCycleException(
                    "this control operation should be performed while the component is stopped");
            }
        } catch (NoSuchInterfaceException nsie) {
            throw new ProActiveRuntimeException("life cycle controller interface not found");
        }
    }

    protected void setItfType(InterfaceType itfType) {
        this.interfaceType = itfType;
    }

    protected abstract void setControllerItfType();

    protected String getHierarchicalType() {
        return owner.getComponentParameters().getHierarchicalType();
    }

    protected boolean isPrimitive() {
        return Constants.PRIMITIVE.equals(getHierarchicalType());
    }

    protected boolean isComposite() {
        return Constants.COMPOSITE.equals(getHierarchicalType());
    }

    /**
     * If a controller holds references to active objects which are dependent on it, it needs to
     * trigger the migration of these active objects. This is done by overriding this method.
     * @param node
     * @throws MigrationException
     */
    public void migrateDependentActiveObjectsTo(Node node) throws MigrationException {
        // nothing by default
    }
}
