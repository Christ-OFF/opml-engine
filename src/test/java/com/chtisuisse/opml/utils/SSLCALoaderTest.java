package com.chtisuisse.opml.utils;

import org.junit.Assert;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import java.lang.reflect.Method;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateFactory;
import java.util.Collections;

/**
 * Created by Christophe on 16.04.2016.
 */
public class SSLCALoaderTest {

    /**
     * Helper method to access private method
     * @return
     * @throws NoSuchMethodException
     */
    private Method getMethodLoadDefaultKeyStore() throws NoSuchMethodException {
        Method method = SSLCALoader.class.getDeclaredMethod("loadDefaultKeyStore");
        method.setAccessible(true);
        return method;
    }

    /**
     * I know I am testing a static private method
     * I could have refactored but I want to practice
     * @throws Exception
     */
    @Test
    public void should_load_keytore() throws Exception {
        Method method = getMethodLoadDefaultKeyStore();
        KeyStore result = (KeyStore) method.invoke(SSLCALoader.class);
        Assert.assertNotNull("KeyStore should have been loaded",result);
    }



    @Test
    public void keystore_should_have_more_certificate() throws Exception {
        Method methodAddCertificate = getMethodAddCertificate();
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Method methodKeystore = getMethodLoadDefaultKeyStore();
        KeyStore keyStore = (KeyStore) methodKeystore.invoke(SSLCALoader.class);
        int originalSize = keyStore.size();
        methodAddCertificate.invoke(SSLCALoader.class,cf,"test.der","test",keyStore);
        Assert.assertTrue("Keystore should have one more cartificate",keyStore.size()==originalSize+1);
    }

    /**
     * Helper method to access private method
     * @return
     * @throws NoSuchMethodException
     */
    private Method getMethodAddCertificate() throws NoSuchMethodException {
        Method method = SSLCALoader.class.getDeclaredMethod("addCertificate", CertificateFactory.class,String.class,String.class, KeyStore.class);
        method.setAccessible(true);
        return method;
    }

}