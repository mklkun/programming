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
package org.objectweb.proactive.core.jmx.notification;

import java.io.Serializable;

import org.objectweb.proactive.core.UniqueID;


/**
 * Used in the JMX notifications
 * @author The ProActive Team
 */
public class BodyNotificationData implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 420L;

    /** The unique id of the body */
    private UniqueID id;

    /** The jobID */
    private String jobID;

    /** The nodeUrl */
    private String nodeUrl;

    /** The className */
    private String className;

    public BodyNotificationData() {
        // No args constructor
    }

    /**
     * Creates a new BodyNotificationData.
     * @param bodyID Id of the new active object.
     * @param jobID Id of the job of the active object.
     * @param nodeURL Url of the node containing this active object.
     * @param className Name of the classe used to create the active object.
     */
    public BodyNotificationData(UniqueID bodyID, String jobID, String nodeURL, String className) {
        this.id = bodyID;
        this.jobID = jobID;
        this.nodeUrl = nodeURL;
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public UniqueID getId() {
        return id;
    }

    public String getJobID() {
        return jobID;
    }

    public String getNodeUrl() {
        return nodeUrl;
    }
}
