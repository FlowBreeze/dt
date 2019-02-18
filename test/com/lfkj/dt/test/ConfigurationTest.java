package com.lfkj.dt.test;

import com.lfkj.dt.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static com.lfkj.dt.Constant.CONFIG_FILE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConfigurationTest {

    private Configuration configuration;

    @Before
    public void loadConfiguration() {
        configuration = Configuration.loadOrDefaultConfiguration();
    }

    @Test
    public void testSave() throws IOException {
        configuration.save();
        assertTrue(new File(CONFIG_FILE_NAME).exists());
        try {
            configuration.store(null, null);
        } catch (Exception e) {
            assertEquals(UnsupportedOperationException.class, e.getClass());
        }
    }

    @Test
    public void testValues() {
        System.out.println(configuration.useSelection());
        System.out.println(configuration.selectionTiggerByMouse());
        System.out.println(configuration.doubleClickInterval());
        System.out.println(configuration.drugSelectInterval());
        System.out.println(configuration.selectionTiggerByKey());
        System.out.println(Arrays.toString(configuration.selectionTiggerKey()));
        System.out.println(configuration.useClipBoard());
        System.out.println(Arrays.toString(configuration.clipboardTiggerKey()));
        System.out.println(Arrays.toString(configuration.tabs()));
        System.out.println(configuration.hideStrategy());
        System.out.println(configuration.showPosition());
        System.out.println(configuration.offsetX());
        System.out.println(configuration.offsetY());

        System.out.println(configuration.iCiBaKey());
        System.out.println(configuration.baiduAppId());
        System.out.println(configuration.baiduPrivateKey());
    }
}
