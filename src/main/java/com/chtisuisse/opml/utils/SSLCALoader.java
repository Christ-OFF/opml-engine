package com.chtisuisse.opml.utils;

import com.jayway.jsonpath.internal.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

/**
 * Thic class will load some CA that are missing from the official CACerts
 * Created by Christophe on 10.04.2016.
 */
class SSLCALoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(SSLCALoader.class);
    /**
     * Yes Java has a default keystore password
     */
    public static final String DEFAULT_KEYSTORE_PASSWORD = "changeit";

    /**
     * This is a utility class
     */
    private SSLCALoader() {
    }

    /**
     * We load the default java keystore
     */
    private static final KeyStore loadDefaultKeyStore() {
        InputStream keyStoreInputStream = null;
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            Path ksPath = Paths.get(System.getProperty("java.home"), "lib", "security", "cacerts");
            keyStoreInputStream = Files.newInputStream(ksPath);
            keyStore.load(keyStoreInputStream, DEFAULT_KEYSTORE_PASSWORD.toCharArray());
            return keyStore;
        } catch (KeyStoreException | NoSuchAlgorithmException | IOException | CertificateException e) {
            LOGGER.warn("Unable to load keystore. Trying to do without it", e);
            return null;
        } finally {
            IOUtils.closeQuietly(keyStoreInputStream);
        }
    }

    /**
     * We need to always do it
     */
    public static void loadAdditionnalCertificates() {

        try {
            KeyStore keyStore = loadDefaultKeyStore();

            if (keyStore!=null) {

                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                // Load all certificates
                addCertificate(cf, "lets-encrypt-x3-cross-signed.der", "DSTRootCAX3", keyStore);
                addCertificate(cf, "StartComCertificationAuthority.der", "StartCom", keyStore);


                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                tmf.init(keyStore);
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, tmf.getTrustManagers(), null);
                SSLContext.setDefault(sslContext);
            }
        } catch (Exception e) {
            LOGGER.warn("Error while adding certificates ", e);
        }
    }

    /**
     * The method doing the load job
     *
     * @param cf
     * @param fileName
     * @param alias
     * @param keystore
     * @throws CertificateException
     * @throws KeyStoreException
     */
    private static void addCertificate(CertificateFactory cf, String fileName, String alias, KeyStore keystore) throws KeyStoreException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream caInput = classloader.getResourceAsStream(fileName);
        try {
            Certificate crt = cf.generateCertificate(caInput);
            keystore.setCertificateEntry(alias, crt);
        } catch (CertificateException e) {
            LOGGER.error("Unable to load " + fileName, e);
        } finally {
            IOUtils.closeQuietly(caInput);
        }
    }

}
