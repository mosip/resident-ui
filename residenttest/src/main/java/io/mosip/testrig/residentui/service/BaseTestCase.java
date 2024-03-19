package io.mosip.testrig.residentui.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import io.mosip.testrig.residentui.authentication.fw.util.RestClient;
import io.mosip.testrig.residentui.kernel.util.CommonLibrary;
import io.mosip.testrig.residentui.kernel.util.ConfigManager;
import io.mosip.testrig.residentui.kernel.util.KernelAuthentication;
import io.mosip.testrig.residentui.utility.GlobalConstants;
//import io.mosip.testrig.residentui.utility.GlobalMethods;
import io.mosip.testrig.residentui.utility.TestRunner;
import io.restassured.response.Response;

//import org.apache.log4j.Logger;

public class BaseTestCase {
	private static final org.slf4j.Logger logger= org.slf4j.LoggerFactory.getLogger(BaseTestCase.class);
	public static String environment;
	public static List<String> languageList = new ArrayList<>();
	public static String ApplnURI;
	public static String ApplnURIForKeyCloak;
	public static String testLevel;
	public static Properties props = getproperty(
			TestRunner.getResourcePath() + "/config/application.properties");
	public static Properties propsKernel = getproperty(
			TestRunner.getResourcePath() + "/config/"+TestRunner.GetKernalFilename());
	public static Properties propsMap = getproperty(
			TestRunner.getResourcePath() + "/config/valueMapping.properties");
	public static Properties propsBio = getproperty(
			TestRunner.getResourcePath() + "/config/bioValue.properties");
	public static String SEPRATOR = "";
	public static String currentModule = "residentui";
	public final static String COOKIENAME = "Authorization";
	public static CommonLibrary kernelCmnLib = null;
	public static KernelAuthentication kernelAuthLib = null;
	public String adminCookie = null;
	public String idrepoCookie = null;
	public static Map<?, ?> queries;
	public static Map<?, ?> residentQueries;
	public static Map<?, ?> partnerQueries;
	public static String uinEmail;
	public static String uinPhone;


	public static void main( String[] args ) {



	}

	public static String getOSType() {
		String type = System.getProperty("os.name");
		if (type.toLowerCase().contains("windows")) {
			SEPRATOR = "\\\\";
			return "WINDOWS";
		} else if (type.toLowerCase().contains("linux") || type.toLowerCase().contains("unix")) {
			SEPRATOR = "/";
			return "OTHERS";
		}
		return null;
	}

	public static List<String> getLanguageList() {
		if (!languageList.isEmpty()) {
			return languageList;
		}

		String section = "";
		String optionalLanguages = null;
		String mandatoryLanguages = null;
		if (isTargetEnvLTS())
			section = "/mosip-config/application-default.properties";
		else
			section = "/mosip-config/sandbox/admin-mz.properties";
		try {

			optionalLanguages = getValueFromActuators(propsKernel.getProperty("actuatorMasterDataEndpoint"), section,
					"mosip.optional-languages");

			logger.info("optionalLanguages from env:" + optionalLanguages);

			mandatoryLanguages = getValueFromActuators(propsKernel.getProperty("actuatorMasterDataEndpoint"), section,
					"mosip.mandatory-languages");

			logger.info("mandatoryLanguages from env:" + mandatoryLanguages);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mandatoryLanguages != null && !mandatoryLanguages.isBlank())
			languageList.addAll(Arrays.asList(mandatoryLanguages.split(",")));

		if (optionalLanguages != null && !optionalLanguages.isBlank())
			languageList.addAll(Arrays.asList(optionalLanguages.split(",")));
		logger.info("languageList from env:" + languageList);
		return languageList;

	}
	public static JSONArray idaActuatorResponseArray = null;
	public static String getValueFromActuators(String endPoint, String section, String key) {

		String url = ApplnURI + endPoint;
		String value = null;
		try {
			if (idaActuatorResponseArray == null) {
				Response response = null;
				org.json.JSONObject responseJson = null;
				response = RestClient.getRequest(url, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);


				responseJson = new org.json.JSONObject(response.getBody().asString());
				idaActuatorResponseArray = responseJson.getJSONArray("propertySources");
			}
			logger.info("idaActuatorResponseArray="+idaActuatorResponseArray);

			for (int i = 0, size = idaActuatorResponseArray.length(); i < size; i++) {
				org.json.JSONObject eachJson = idaActuatorResponseArray.getJSONObject(i);
				if (eachJson.get("name").toString().contains(section)) {
					value = eachJson.getJSONObject(GlobalConstants.PROPERTIES).getJSONObject(key)
							.get(GlobalConstants.VALUE).toString();
					logger.info("value="+value);
					break;
				}
			}

			return value;
		} catch (Exception e) {
			logger.error(GlobalConstants.EXCEPTION_STRING_2 + e);
			return value;
		}

	}
	
	public static String GethierarchyLevelName(int locationHierarchyLevels) {
		kernelAuthLib = new KernelAuthentication();
		String token = kernelAuthLib.getTokenByRole("admin");
		String url = ApplnURI + props.getProperty("locationHierarchyLevels");
		Response response = RestClient.getRequestWithCookie(url+locationHierarchyLevels+"/"+getlang(), MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON,"Authorization", token);
		org.json.JSONObject responseJson = new org.json.JSONObject(response.asString());
		org.json.JSONObject responseObj = responseJson.getJSONObject("response");
		JSONArray responseArray = responseObj.getJSONArray("locationHierarchyLevels");
		org.json.JSONObject idItem = responseArray.getJSONObject(0);
		String hierarchyLevelName = idItem.getString("hierarchyLevelName");
		return hierarchyLevelName;

	}
	public static String GethierarchyName(int locationHierarchyLevels) {
		kernelAuthLib = new KernelAuthentication();
		String token = kernelAuthLib.getTokenByRole("admin");
		String url = ApplnURI + props.getProperty("locationhierarchy");
		Response response = RestClient.getRequestWithCookie(url+GethierarchyLevelName(locationHierarchyLevels), MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON,"Authorization", token);
		org.json.JSONObject responseJson = new org.json.JSONObject(response.asString());
		org.json.JSONObject responseObj = responseJson.getJSONObject("response");
		JSONArray responseArray = responseObj.getJSONArray("locations");

		for (int i = 0, size = responseArray.length(); i < size; i++) {
			org.json.JSONObject idItem = responseArray.getJSONObject(i);
			String lang = idItem.getString("langCode");
			String hierarchyName = idItem.getString("name");
			if (lang.equals(getlang())) {
				return hierarchyName;
			}

		}
		return null;

	}
	public static String getlang() {
		String language=ConfigManager.getloginlang();
		if(language.equalsIgnoreCase("sin")) {
			return "eng";
		}else {
			return language;
		}
	}
	public static Properties getproperty(String path) {
		Properties prop = new Properties();

		try {
			File file = new File(path);
			prop.load(new FileInputStream(file));
		} catch (IOException e) {
			logger.error("Exception " + e.getMessage());
		}
		return prop;
	}

	public static void initialize() {
		PropertyConfigurator.configure(getLoggerPropertyConfig());
		kernelAuthLib = new KernelAuthentication();
		kernelCmnLib = new CommonLibrary();
		queries = kernelCmnLib.readProperty("adminQueries");
		partnerQueries = kernelCmnLib.readProperty("partnerQueries");
		residentQueries = kernelCmnLib.readProperty("residentServicesQueries");
		/**
		 * Make sure test-output is there
		 */

		getOSType();
		logger.info("We have created a Config Manager. Beginning to read properties!");

		environment = ConfigManager.getiam_apienvuser();
		logger.info("Environemnt is  ==== :" + environment);
		ApplnURI = ConfigManager.getiam_apiinternalendpoint();
		logger.info("Application URI ======" + ApplnURI);
		ApplnURIForKeyCloak = ConfigManager.getIAMUrl();
		logger.info("Application URI ======" + ApplnURIForKeyCloak);
		testLevel = System.getProperty("env.testLevel");
		logger.info("Test Level ======" + testLevel);
		// languageList =Arrays.asList(System.getProperty("env.langcode").split(","));

		// langcode = System.getProperty("env.langcode");
		logger.info("Test Level ======" + languageList);

		logger.info("Configs from properties file are set.");

	}

	private static String targetEnvVersion = "";

	public static boolean isTargetEnvLTS() {

		if (targetEnvVersion.isEmpty()) {

			Response response = null;
			org.json.JSONObject responseJson = null;
			String url = ApplnURI + "/v1/auditmanager/actuator/info";
			try {
				response = RestClient.getRequest(url, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
				//	GlobalMethods.reportResponse(response.getHeaders().asList().toString(), url, response);

				responseJson = new org.json.JSONObject(response.getBody().asString());

				targetEnvVersion = responseJson.getJSONObject("build").getString("version");

			} catch (Exception e) {
				logger.error(GlobalConstants.EXCEPTION_STRING_2 + e);
			}
		}
		return targetEnvVersion.contains("1.2");
	}

	private static Properties getLoggerPropertyConfig() {
		Properties logProp = new Properties();
		logProp.setProperty("log4j.rootLogger", "INFO, Appender1,Appender2");
		logProp.setProperty("log4j.appender.Appender1", "org.apache.log4j.ConsoleAppender");
		logProp.setProperty("log4j.appender.Appender1.layout", "org.apache.log4j.PatternLayout");
		logProp.setProperty("log4j.appender.Appender1.layout.ConversionPattern", "%-7p %d [%t] %c %x - %m%n");
		logProp.setProperty("log4j.appender.Appender2", "org.apache.log4j.FileAppender");
		logProp.setProperty("log4j.appender.Appender2.File", "src/logs/mosip-api-test.log");
		logProp.setProperty("log4j.appender.Appender2.layout", "org.apache.log4j.PatternLayout");
		logProp.setProperty("log4j.appender.Appender2.layout.ConversionPattern", "%-7p %d [%t] %c %x - %m%n");
		return logProp;
	}

	public static JSONObject getRequestJson(String filepath) {
		return kernelCmnLib.readJsonData(filepath, true);

	}
}