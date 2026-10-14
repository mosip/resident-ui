package io.mosip.testrig.residentui.config;

import io.mosip.testrig.apirig.utils.AdminTestUtil;
import io.mosip.testrig.apirig.utils.KernelAuthentication;
import io.mosip.testrig.apirig.utils.RestClient;
import io.mosip.testrig.uitestrig.core.MosipTestDataManager;
import io.mosip.testrig.uitestrig.core.UiConfigManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ResidentUtil {

    private static final Logger logger = LoggerFactory.getLogger(ResidentUtil.class);
    private static final String COOKIE = "Authorization";
    private static final String ROLE_IDREPO = "idrepo";
    private static final String ROLE_ADMIN = "admin";

    public static String generateUIN() {
        KernelAuthentication auth = MosipTestDataManager.newKernelAuth();
        String applnUri = UiConfigManager.getproperty("apiInternalEndPoint");
        DateFormat fmt = new SimpleDateFormat("YYYYMMddHHmmss");
        String rid = "27847" + RandomStringUtils.randomNumeric(10) + fmt.format(Calendar.getInstance().getTime());

        String uin = fetchUnusedUIN(auth, applnUri);
        if (uin.isEmpty()) return "";

        String schemaJson = fetchMasterDataSchema(auth, applnUri);
        if (schemaJson.isEmpty()) return "";

        String requestJson = buildAddIdentityRequest(schemaJson, uin, rid);
        if (!activateUIN(auth, applnUri, requestJson)) return "";

        logger.info("Generated UIN ...{}", uin.length() > 4 ? uin.substring(uin.length() - 4) : uin);
        return uin;
    }

    public static String generateVID(String uin, String vidType) {
        if (uin.isEmpty() || vidType.isEmpty()) return "";
        KernelAuthentication auth = MosipTestDataManager.newKernelAuth();
        String applnUri = UiConfigManager.getproperty("apiInternalEndPoint");
        String vidUrl = UiConfigManager.getproperty("idRepoGenVidURL");
        String token = auth.getTokenByRole(ROLE_IDREPO);

        JSONObject request = new JSONObject();
        request.put("UIN", uin);
        request.put("vidType", vidType);

        JSONObject body = new JSONObject();
        body.put("id", "mosip.vid.create");
        body.put("metadata", new JSONObject());
        body.put("requesttime", AdminTestUtil.generateCurrentUTCTimeStamp());
        body.put("version", "v1");
        body.put("request", request);

        String responseStr = RestClient.postRequestWithCookie(
                applnUri + vidUrl, body.toString(),
                MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON,
                COOKIE, token).asString();

        JSONObject response = new JSONObject(responseStr);
        JSONObject data = response.optJSONObject("response");
        if (data != null && "ACTIVE".equalsIgnoreCase(data.optString("vidStatus"))) {
            logger.info("Generated {} VID", vidType);
            return data.getString("VID");
        }
        logger.error("VID generation failed for type {}: {}", vidType, responseStr);
        return "";
    }

    private static String fetchUnusedUIN(KernelAuthentication auth, String applnUri) {
        String token = auth.getTokenByRole(ROLE_IDREPO);
        try {
            String responseStr = RestClient.getRequestWithCookie(
                    applnUri + "/v1/idgenerator/uin",
                    MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON,
                    COOKIE, token).asString();
            return new JSONObject(responseStr).getJSONObject("response").getString("uin");
        } catch (Exception e) {
            logger.error("Failed to fetch unused UIN: {}", e.getMessage());
            return "";
        }
    }

    private static String fetchMasterDataSchema(KernelAuthentication auth, String applnUri) {
        String token = auth.getTokenByRole(ROLE_ADMIN);
        String schemaUrl = UiConfigManager.getproperty("masterSchemaURL");
        try {
            return RestClient.getRequestWithCookie(
                    applnUri + schemaUrl,
                    MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON,
                    COOKIE, token).asString();
        } catch (Exception e) {
            logger.error("Failed to fetch master data schema: {}", e.getMessage());
            return "";
        }
    }

    private static boolean activateUIN(KernelAuthentication auth, String applnUri, String inputJson) {
        String token = auth.getTokenByRole(ROLE_IDREPO);
        String addIdentityUrl = UiConfigManager.getproperty("addIdentityURL");
        String responseStr = RestClient.postRequestWithCookie(
                applnUri + addIdentityUrl, inputJson,
                MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON,
                COOKIE, token).asString();
        JSONObject response = new JSONObject(responseStr);
        JSONObject data = response.optJSONObject("response");
        boolean ok = data != null && "ACTIVATED".equalsIgnoreCase(data.optString("status"));
        if (!ok) logger.error("UIN activation failed: {}", responseStr);
        return ok;
    }

    public static int getHierarchyNumbers() {
        KernelAuthentication auth = MosipTestDataManager.newKernelAuth();
        String token = auth.getTokenByRole(ROLE_ADMIN);
        String url = UiConfigManager.getproperty("apiInternalEndPoint")
                + UiConfigManager.getproperty("locationHierarchyLevels") + getLoginLang();
        String responseStr = RestClient.getRequestWithCookie(url,
                MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, COOKIE, token).asString();
        int maxLevel = 0;
        try {
            JsonNode levels = new ObjectMapper().readTree(responseStr)
                    .path("response").path("locationHierarchyLevels");
            for (JsonNode level : levels) {
                int h = level.path("hierarchyLevel").asInt();
                if (h > maxLevel) maxLevel = h;
            }
        } catch (Exception e) {
            logger.error("Failed to get hierarchy numbers: {}", e.getMessage());
        }
        return maxLevel;
    }

    public static String getHierarchyName(int level) {
        String levelName = getHierarchyLevelName(level);
        if (levelName == null) return null;
        KernelAuthentication auth = MosipTestDataManager.newKernelAuth();
        String token = auth.getTokenByRole(ROLE_ADMIN);
        String url = UiConfigManager.getproperty("apiInternalEndPoint")
                + UiConfigManager.getproperty("locationhierarchy") + levelName;
        String responseStr = RestClient.getRequestWithCookie(url,
                MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, COOKIE, token).asString();
        JSONArray locations = new JSONObject(responseStr).getJSONObject("response").getJSONArray("locations");
        String lang = getLoginLang();
        for (int i = 0; i < locations.length(); i++) {
            JSONObject loc = locations.getJSONObject(i);
            if (lang.equals(loc.getString("langCode"))) return loc.getString("name");
        }
        return null;
    }

    private static String getHierarchyLevelName(int level) {
        KernelAuthentication auth = MosipTestDataManager.newKernelAuth();
        String token = auth.getTokenByRole(ROLE_ADMIN);
        String url = UiConfigManager.getproperty("apiInternalEndPoint")
                + UiConfigManager.getproperty("locationHierarchyLevels") + level + "/" + getLoginLang();
        String responseStr = RestClient.getRequestWithCookie(url,
                MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, COOKIE, token).asString();
        JSONArray levels = new JSONObject(responseStr).getJSONObject("response").getJSONArray("locationHierarchyLevels");
        return levels.getJSONObject(0).getString("hierarchyLevelName");
    }

    private static String getLoginLang() {
        String lang = UiConfigManager.getproperty("loginlang");
        return "sin".equalsIgnoreCase(lang) ? "eng" : lang;
    }

    private static String buildAddIdentityRequest(String schemaJson, String uin, String rid) {
        List<String> languages = MosipTestDataManager.getLanguageList();
        JSONObject schemaResponse = new JSONObject(schemaJson).getJSONObject("response");

        double schemaVersion;
        Object ver = schemaResponse.get("idVersion");
        schemaVersion = (ver instanceof BigDecimal) ? ((BigDecimal) ver).doubleValue() : (Double) ver;

        JSONObject schemaFileJson = new JSONObject(schemaResponse.getString("schemaJson"));
        JSONObject identityProps = schemaFileJson
                .getJSONObject("properties").getJSONObject("identity").getJSONObject("properties");
        JSONArray requiredProps = schemaFileJson
                .getJSONObject("properties").getJSONObject("identity").getJSONArray("required");

        JSONObject identityJson = new JSONObject();
        identityJson.put("UIN", uin);

        for (int i = 0; i < requiredProps.length(); i++) {
            String prop = requiredProps.getString(i);
            JSONObject propData = identityProps.getJSONObject(prop);
            if (propData.has("$ref") && propData.getString("$ref").contains("simpleType")) {
                JSONArray multiLang = new JSONArray();
                for (String lang : languages) {
                    multiLang.put(new JSONObject().put("language", lang)
                            .put("value", UiConfigManager.getproperty(prop) + lang));
                }
                identityJson.put(prop, multiLang);
            } else {
                switch (prop) {
                    case "proofOfIdentity" -> identityJson.put(prop, new JSONObject()
                            .put("format", "txt").put("type", "DOC001").put("value", "fileReferenceID"));
                    case "individualBiometrics" -> identityJson.put(prop, new JSONObject()
                            .put("format", "cbeff").put("version", 1).put("value", "fileReferenceID"));
                    case "IDSchemaVersion" -> identityJson.put(prop, schemaVersion);
                    default -> identityJson.put(prop, UiConfigManager.getproperty(prop));
                }
            }
        }

        JSONArray docs = new JSONArray();
        docs.put(new JSONObject().put("value", UiConfigManager.getproperty("BioValue"))
                .put("category", "individualBiometrics"));

        JSONObject body = new JSONObject();
        body.put("id", UiConfigManager.getproperty("id"));
        body.put("version", "v1");
        body.put("requesttime", AdminTestUtil.generateCurrentUTCTimeStamp());
        body.put("request", new JSONObject()
                .put("registrationId", rid)
                .put("identity", identityJson)
                .put("documents", docs));

        return body.toString();
    }
}
