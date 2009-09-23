package performanceTests.dataspace.remote;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URISyntaxException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.InitActive;
import org.objectweb.proactive.api.PAActiveObject;
import org.objectweb.proactive.api.PAFuture;
import org.objectweb.proactive.core.ProActiveException;
import org.objectweb.proactive.core.node.Node;
import org.objectweb.proactive.core.util.TimeoutAccounter;
import org.objectweb.proactive.core.util.wrapper.BooleanWrapper;
import org.objectweb.proactive.extensions.dataspaces.api.DataSpacesFileObject;
import org.objectweb.proactive.extensions.dataspaces.api.PADataSpaces;
import org.objectweb.proactive.extensions.dataspaces.core.DataSpacesNodes;
import org.objectweb.proactive.extensions.dataspaces.core.naming.NamingServiceDeployer;
import org.objectweb.proactive.extensions.dataspaces.exceptions.ConfigurationException;
import org.objectweb.proactive.extensions.dataspaces.exceptions.NotConfiguredException;
import org.objectweb.proactive.extensions.dataspaces.exceptions.SpaceNotFoundException;
import org.objectweb.proactive.extensions.vfsprovider.FileSystemServerDeployer;

import functionalTests.GCMFunctionalTestDefaultNodes;


public abstract class AbstractPAProviderRemoteBenchmark extends GCMFunctionalTestDefaultNodes {
    final Class<? extends AbstractPAProviderRemoteBenchmark> cl;

    static {
        System.setProperty("proactive.test.timeout", "600000");
    }

    FileSystemServerDeployer fsDeployer;
    NamingServiceDeployer namingServiceDeployer;
    AO ao;

    public AbstractPAProviderRemoteBenchmark(Class<? extends AbstractPAProviderRemoteBenchmark> cl) {
        super(1, 1);
        this.cl = cl;
    }

    @Before
    public void before() throws IOException, ProActiveException, URISyntaxException {
        // Start the PAProvider, the naming service and register the application
        fsDeployer = new FileSystemServerDeployer("/dev/", true);
        namingServiceDeployer = new NamingServiceDeployer();

        final long applicationId = 0xcafe;
        namingServiceDeployer.getLocalNamingService().registerApplication(applicationId, null);

        Node node = super.getANode();
        ao = (AO) PAActiveObject.newActive(AO.class.getName(), new Object[] { fsDeployer.getVFSRootURL(),
                namingServiceDeployer.getNamingServiceURL(), applicationId }, node);
    }

    public static class AO implements Serializable, InitActive {
        String vfsRootUrl;
        String namingServiceUrl;
        long appId;

        public AO() {
        }

        public AO(String vfsRootUrl, String namingServiceUrl, long appId) {
            this.vfsRootUrl = vfsRootUrl;
            this.namingServiceUrl = namingServiceUrl;
            this.appId = appId;
        }

        public void initActivity(Body body) {
            try {
                // node is configured without scratch
                Node node = PAActiveObject.getNode();
                DataSpacesNodes.configureNode(node, null);
                DataSpacesNodes.configureApplication(node, appId, namingServiceUrl);

                PADataSpaces.addDefaultInput(vfsRootUrl, null);

            } catch (Throwable t) {
                logger.error("ERROR", t);
            }
        }

        public BooleanWrapper test() throws SpaceNotFoundException, NotConfiguredException,
                ConfigurationException, IOException {
            final DataSpacesFileObject fo = PADataSpaces.resolveDefaultInput("/zero");
            final InputStream is = fo.getContent().getInputStream();

            for (int bs = 1; bs < 1 << 25; bs <<= 1) {

                long before = System.currentTimeMillis();

                long count = 0;
                byte[] buf = new byte[bs];
                TimeoutAccounter ta = TimeoutAccounter.getAccounter(10000);
                while (!ta.isTimeoutElapsed()) {
                    is.read(buf);
                    count++;
                }

                final long tx = bs * count;
                final long after = System.currentTimeMillis();
                final double bw = ((tx * 1000.0) / (after - before)) / (1 << 20);
                System.out.printf(
                        "Block size: %8d bytes, bandwidth: %8.2f MiB/s, TX: %5d MiB, Time elapsed: %d ms\n",
                        bs, bw, (tx / (1 << 20)), (after - before));
            }

            is.close();

            return new BooleanWrapper(true);
        }

    }

    @Test
    public void test() throws SpaceNotFoundException, NotConfiguredException, ConfigurationException,
            IOException {
        PAFuture.waitFor(ao.test());
    }

    @After
    public void after() throws ProActiveException {
        this.stopDataSpace();
    }

    private void stopDataSpace() throws ProActiveException {
        namingServiceDeployer.terminate();
        fsDeployer.terminate();
    }
}