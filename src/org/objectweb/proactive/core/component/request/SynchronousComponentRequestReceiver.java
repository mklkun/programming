/* 
 * ################################################################
 * 
 * ProActive: The Java(TM) library for Parallel, Distributed, 
 *            Concurrent computing with Security and Mobility
 * 
 * Copyright (C) 1997-2006 INRIA/University of Nice-Sophia Antipolis
 * Contact: proactive@objectweb.org
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *  
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *  
 *  Initial developer(s):               The ProActive Team
 *                        http://www.inria.fr/oasis/ProActive/contacts.html
 *  Contributor(s): 
 * 
 * ################################################################
 */ 
package org.objectweb.proactive.core.component.request;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.core.body.request.Request;
import org.objectweb.proactive.core.body.request.RequestReceiverImpl;
import org.objectweb.proactive.core.component.body.ComponentBody;
import org.objectweb.proactive.core.util.log.Loggers;
import org.objectweb.proactive.core.util.log.ProActiveLogger;


/**
 * This is an extension of the RequestReceiverImpl class, which allows the
 * shortcutting of functional requests : when crossing a composite component
 * that has such a request receiver, a shortcut notification is sent to the
 * emitter, and the request is directly transferred to the following linked
 * interface. This means that we stay in the rendez-vous until the request
 * reaches its final destination (a primitive component where the request can be
 * executed, or a component that does not have such a synchronous request
 * receiver).
 *
 * @author Matthieu Morel
 */
public class SynchronousComponentRequestReceiver extends RequestReceiverImpl {
    protected static Logger logger = ProActiveLogger.getLogger(Loggers.COMPONENTS_REQUESTS);
    public final static int SHORTCUT = 1;

    public SynchronousComponentRequestReceiver() {
        super();
    }

    public int receiveRequest(Request r, Body bodyReceiver)
        throws IOException {
        if (r instanceof ComponentRequest) {
            if (!((ComponentRequest) r).isControllerRequest()) {
                if ("true".equals(System.getProperty(
                                "proactive.components.use_shortcuts"))) {
                    if (!((ComponentBody) bodyReceiver).getProActiveComponentImpl()
                              .getInputInterceptors().isEmpty() ||
                            !((ComponentBody) bodyReceiver).getProActiveComponentImpl()
                                  .getOutputInterceptors().isEmpty()) {
                        if (logger.isDebugEnabled()) {
                            logger.debug(
                                "shortcut is stopped in this component, because functional invocations are intercepted");
                        }

                        // no shortcut if there is an interception
                        return super.receiveRequest(r, bodyReceiver);
                    }

                    ((ComponentRequest) r).shortcutNotification(r.getSender(),
                        bodyReceiver.getRemoteAdapter());

                    // TODO_M leave a ref of the shortcut
                    if (logger.isDebugEnabled()) {
                        logger.debug("directly executing request " +
                            r.getMethodCall().getName() +
                            ((r.getMethodCall().getComponentInterfaceName() != null)
                            ? (" on interface " +
                            r.getMethodCall().getComponentInterfaceName()) : ""));
                    }
                }
                bodyReceiver.serve(r);
                // TODO_M check with FT
                return SynchronousComponentRequestReceiver.SHORTCUT;
            }
        }

        // normal object invocations and controller requests are not subject to shortcuts
        return super.receiveRequest(r, bodyReceiver);
    }
}
