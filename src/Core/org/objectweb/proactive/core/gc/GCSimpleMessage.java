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
package org.objectweb.proactive.core.gc;

import java.io.Serializable;

import org.objectweb.proactive.core.UniqueID;


public class GCSimpleMessage implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 41L;
    private final transient Referenced referenced;
    private final UniqueID sender;
    private final boolean consensus;
    private final Activity lastActivity;

    GCSimpleMessage(Referenced referenced, UniqueID sender, boolean consensus, Activity lastActivity) {
        this.referenced = referenced;
        this.sender = sender;
        this.consensus = consensus;
        this.lastActivity = lastActivity;
    }

    @Override
    public String toString() {
        String s = sender.shortString();
        return "GCMSG[" + s + "(" + lastActivity + "):" + this.consensus + "]";
    }

    UniqueID getSender() {
        return this.sender;
    }

    boolean getConsensus() {
        return this.consensus;
    }

    Activity getLastActivity() {
        return this.lastActivity;
    }

    Referenced getReferenced() {
        return this.referenced;
    }
}
