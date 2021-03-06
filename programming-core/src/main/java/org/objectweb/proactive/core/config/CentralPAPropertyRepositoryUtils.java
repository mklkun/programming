/*
 * ProActive Parallel Suite(TM):
 * The Open Source library for parallel and distributed
 * Workflows & Scheduling, Orchestration, Cloud Automation
 * and Big Data Analysis on Enterprise Grids & Clouds.
 *
 * Copyright (c) 2007 - 2017 ActiveEon
 * Contact: contact@activeeon.com
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation: version 3 of
 * the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 */
package org.objectweb.proactive.core.config;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * CentralPAPropertyRepositoryUtils
 *
 * @author The ProActive Team
 **/
public class CentralPAPropertyRepositoryUtils {

    /**
     * A validator which accepts only a list with unique elements
     */
    public static PropertyListValidator IS_SET = new PropertyListValidator() {
        @Override
        public void accept(ArrayList<String> input) {
            HashSet<String> uniqueSet = new HashSet<String>(input);
            if (uniqueSet.contains("")) {
                throw new IllegalArgumentException("Empty element found in list : " + input);
            }
            // if we have more inputs than the unique set created with the inputs, then there is at least one duplicate
            if (input.size() > uniqueSet.size()) {
                throw new IllegalArgumentException("Duplicate element found in list : " + input);
            }
        }
    };

    /**
     * A validator filter special for additional protocols, it accepts a list of unique elements which doesn't
     * contain the main protocol
     */
    public static PropertyListValidator ADDITIONAL_PROTOCOLS_VALIDATOR = new AdditionalProtocolsValidator(CentralPAPropertyRepository.PA_COMMUNICATION_PROTOCOL);

    /**
     * A validator filter which accepts the list only if it does not contain the content of the given properties
     */
    public static class AdditionalProtocolsValidator implements PropertyListValidator {
        PAPropertyString mainProtocolProp;

        public AdditionalProtocolsValidator(PAPropertyString mainProtocolProp) {
            this.mainProtocolProp = mainProtocolProp;
        }

        @Override
        public void accept(ArrayList<String> input) {

            if (mainProtocolProp.isSet()) {
                String mainProtocol = mainProtocolProp.getValue();
                HashSet<String> uniqueSet = new HashSet<String>(input);
                uniqueSet.add(mainProtocol);
                if (input.size() + 1 > uniqueSet.size()) {
                    throw new IllegalArgumentException("Duplicate element found in list : " + mainProtocol + " & " +
                                                       input);
                }

                if (uniqueSet.contains("pnp") && uniqueSet.contains("pnpssl")) {
                    throw new IllegalArgumentException("Protocols PNP and PNPSSL cannot be both used in the same ProActive Node as they share the same registry");
                }
            }
        }

    }

    /**
     * A validator filter which is a conjunction of several validators
     */
    public static class AndValidator implements PropertyListValidator {
        PropertyListValidator[] validators;

        public AndValidator(PropertyListValidator... validators) {
            this.validators = validators;
        }

        @Override
        public void accept(ArrayList<String> input) {
            for (PropertyListValidator validator : validators) {
                validator.accept(input);
            }
        }
    }
}
