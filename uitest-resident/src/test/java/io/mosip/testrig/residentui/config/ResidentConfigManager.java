package io.mosip.testrig.residentui.config;

import io.mosip.testrig.apirig.utils.ConfigManager;
import io.mosip.testrig.uitestrig.core.UiConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Resident Portal config — five-layer loading (lowest → highest priority):
 *   1. Kernel.properties      (apitest-commons JAR — common MOSIP defaults)
 *   2. uitest.properties      (uitest-commons JAR  — UI/browser defaults)
 *   3. valueMapping.properties (this module         — default identity field values for
 *                                dynamic UIN generation; see ResidentUtil.buildAddIdentityRequest)
 *   4. resident.properties    (this module         — env-specific overrides)
 *   5. Environment variables / Rancher overrides
 *
 * Loading order ensures each layer correctly overrides the one below it.
 * Call ResidentConfigManager.init() once at suite setup before anything reads config.
 */
public class ResidentConfigManager extends UiConfigManager {

    private static final Logger logger = LoggerFactory.getLogger(ResidentConfigManager.class);

    public static void init() {
        Map<String, Object> moduleProps = new HashMap<>();
        // Layer 2: uitest-commons UI defaults — loaded first so later layers override them
        moduleProps.putAll(loadProperties("config/uitest.properties"));
        // Layer 3: default identity field values (dateOfBirth, email, phone, postalCode, ...)
        moduleProps.putAll(loadProperties("config/valueMapping.properties"));
        // Layer 4: module-specific overrides
        moduleProps.putAll(loadProperties("config/resident.properties"));
        moduleProps.putAll(loadProperties("config/bioValue.properties"));
        // ConfigManager.init(): loads Kernel.properties first (layer 1), then putAll(moduleProps)
        // so layers 2-4 override Kernel; env vars (layer 5) override everything inside init().
        ConfigManager.init(moduleProps);
    }

    private static Map<String, Object> loadProperties(String resourcePath) {
        Properties props = new Properties();
        try (InputStream in = ResidentConfigManager.class.getClassLoader()
                .getResourceAsStream(resourcePath)) {
            if (in != null) {
                props.load(in);
                logger.info("Loaded {} — {} properties", resourcePath, props.size());
            } else {
                logger.warn("{} not found on classpath", resourcePath);
            }
        } catch (IOException e) {
            logger.error("Failed to load {}: {}", resourcePath, e.getMessage());
        }
        Map<String, Object> map = new HashMap<>();
        for (String key : props.stringPropertyNames()) {
            map.put(key, props.getProperty(key));
        }
        return map;
    }

    public static String getExternalEmail()     { return getproperty("externalemail"); }
    public static String getTempEmail()         { return getproperty("tempemail"); }
    public static String getUin()               { return getproperty("uin"); }
    public static String getPerpetualVid()      { return getproperty("perpetualvid"); }
    public static String getTemporaryVid()      { return getproperty("temporaryvid"); }
    public static String getOneTimeVid()        { return getproperty("onetimevid"); }
    public static boolean isIdentityGenManual() { return "yes".equalsIgnoreCase(getproperty("identityGenManual")); }

    /** Max minutes to poll for a pending demographic-update request to clear before giving up. */
    public static int getPacketUpdateWaitMinutes() {
        String val = getproperty("packetUpdateWait");
        return (val == null || val.isBlank()) ? 2 : Integer.parseInt(val.trim());
    }
}
