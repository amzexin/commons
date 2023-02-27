package io.github.cnzexin.hbasedemo;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * HBaseDDLTest
 *
 * @author Zexin Li
 * @date 2023-02-27 17:26
 */
public class HBaseDDLTest {

    private static Logger logger = LoggerFactory.getLogger(HBaseConnection.class);

    private HBaseDDL hBaseDDL = new HBaseDDL();

    @Test
    public void testListNamespace() throws IOException {
        List<String> namespaces = hBaseDDL.listNamespace();
        logger.info("namespaces = {}", namespaces);
    }

    @Test
    public void testCreateNamespace() throws IOException {
        hBaseDDL.createNamespace("test");
    }

}
