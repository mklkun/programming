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
package functionalTests.activeobject.generics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.objectweb.proactive.api.PAActiveObject;
import org.objectweb.proactive.api.PAGroup;
import org.objectweb.proactive.core.group.Group;
import org.objectweb.proactive.core.mop.Utils;
import org.objectweb.proactive.core.util.wrapper.IntWrapper;
import org.objectweb.proactive.core.util.wrapper.StringWrapper;

import functionalTests.FunctionalTest;


/**
 * Checks that ProActive correctly handles generics.
 *
 * 1. Stubs on generic return types are correctly typed with the parameterizing type.
 * 2. Groups correctly handle generics, including for reifiable parameterizing return types.
 *
 *
 * @author The ProActive Team
 */

public class Test extends FunctionalTest {
    @org.junit.Test
    public void action() throws Exception {
        //      new active with '_' in classname of a parametized type.
        //pa.stub.parameterized.nonregressiontest.activeobject.generics.Stub_Pair_Generics_[nonregressiontest_activeobject_generics_My_Type5org_objectweb_proactive_core_util_wrapper_IntWrapper]
        @SuppressWarnings("unchecked")
        Pair<My_DType, IntWrapper> p_ = PAActiveObject.newActive(Pair.class,
                                                                 new Class[] { My_DType.class, IntWrapper.class },
                                                                 new Object[] { new My_DType("toto"),
                                                                                new IntWrapper(12) });
        assertTrue(My_DType.class.isAssignableFrom(p_.getFirst().getClass()));
        assertTrue(IntWrapper.class.isAssignableFrom(p_.getSecond().getClass()));
        assertEquals("toto", p_.getFirst().toString());
        assertEquals(12, p_.getSecond().getIntValue());

        // new active with reifiable parameter types
        // test before non reifiable return types to verify caching of synchronous/asynchrous method calls 
        // works fine with parameterized types
        @SuppressWarnings("unchecked")
        Pair<StringWrapper, IntWrapper> b = PAActiveObject.newActive(Pair.class,
                                                                     new Class[] { StringWrapper.class,
                                                                                   IntWrapper.class },
                                                                     new Object[] { new StringWrapper("toto"),
                                                                                    new IntWrapper(12) });
        assertTrue(StringWrapper.class.isAssignableFrom(b.getFirst().getClass()));
        assertTrue(IntWrapper.class.isAssignableFrom(b.getSecond().getClass()));
        assertEquals("toto", b.getFirst().getStringValue());
        assertEquals(12, b.getSecond().getIntValue());

        // new active with non reifiable parameter types
        @SuppressWarnings("unchecked")
        Pair<String, Integer> a = PAActiveObject.newActive(Pair.class,
                                                           new Class[] { String.class, Integer.class },
                                                           new Object[] { "A", 42 });
        assertTrue(String.class.isAssignableFrom(a.getFirst().getClass()));
        assertTrue(Integer.class.isAssignableFrom(a.getSecond().getClass()));
        assertEquals("A", a.getFirst());
        assertTrue(42 == a.getSecond());

        // turn active
        Pair<Integer, String> pair = new Pair<Integer, String>(42, "X");
        assertTrue(42 == pair.getFirst());
        assertEquals("X", pair.getSecond());

        @SuppressWarnings("unchecked")
        Pair<Integer, String> activePair = (Pair<Integer, String>) PAActiveObject.turnActive(pair,
                                                                                             new Class[] { Integer.class,
                                                                                                           String.class });
        assertTrue(Integer.class.isAssignableFrom(activePair.getFirst().getClass()));
        assertTrue(String.class.isAssignableFrom(activePair.getSecond().getClass()));
        assertTrue(42 == activePair.getFirst());
        assertEquals("X", activePair.getSecond());

        // new active group with reifiable parameter types
        @SuppressWarnings("unchecked")
        Pair<StringWrapper, IntWrapper> gb = (Pair<StringWrapper, IntWrapper>) PAGroup.newGroup(Pair.class.getName(),
                                                                                                new Class[] { StringWrapper.class,
                                                                                                              IntWrapper.class },
                                                                                                new Object[][] { { new StringWrapper("A"),
                                                                                                                   new IntWrapper(1) },
                                                                                                                 { new StringWrapper("B"),
                                                                                                                   new IntWrapper(2) } });
        assertTrue(StringWrapper.class.isAssignableFrom(gb.getFirst().getClass()));
        assertTrue(IntWrapper.class.isAssignableFrom(gb.getSecond().getClass()));

        StringWrapper stringWrapperResult = gb.getFirst();
        Group<StringWrapper> stringWrapperResultGroup = PAGroup.getGroup(stringWrapperResult);
        IntWrapper intWrapperResult = gb.getSecond();
        Group<IntWrapper> intWrapperResultGroup = PAGroup.getGroup(intWrapperResult);

        assertEquals(new StringWrapper("A"), stringWrapperResultGroup.get(0));
        assertEquals(new StringWrapper("B"), stringWrapperResultGroup.get(1));
        assertEquals(new IntWrapper(1), intWrapperResultGroup.get(0));
        assertEquals(new IntWrapper(2), intWrapperResultGroup.get(1));

        // new active group with non reifiable parameter types (which is not allowed with groups)
        boolean invocationTargetException = false;
        @SuppressWarnings("unchecked")
        Pair<String, Integer> ga = (Pair<String, Integer>) PAGroup.newGroup(Pair.class.getName(),
                                                                            new Class[] { String.class, Integer.class },
                                                                            new Object[][] { { "A", 1 }, { "B", 2 } });
        try {
            // verify this invocation is not possible
            ga.getFirst();
        } catch (Throwable e) {
            invocationTargetException = true;
        }
        assertTrue(invocationTargetException);

        // test name escaping with generics, main test are done in nonregressiontest.stub.stubgeneration
        assertEquals("pa.stub._StubMy__P__DType", Utils.convertClassNameToStubClassName("My_P_DType", new Class[] {}));
        String escape = Utils.convertClassNameToStubClassName(Pair.class.getName(),
                                                              new Class[] { My_DType.class, IntWrapper.class });
        assertEquals(escape,
                     "pa.stub.parameterized.functionalTests.activeobject.generics._StubPair_GenericsfunctionalTests_Pactiveobject_Pgenerics_PMy__DType_Dorg_Pobjectweb_Pproactive_Pcore_Putil_Pwrapper_PIntWrapper");

        String[] unescape = Utils.getNamesOfParameterizingTypesFromStubClassName(escape);
        String[] result = new String[] { "functionalTests.activeobject.generics.My_DType",
                                         "org.objectweb.proactive.core.util.wrapper.IntWrapper" };
        assertTrue(Arrays.equals(unescape, result));
    }
}
