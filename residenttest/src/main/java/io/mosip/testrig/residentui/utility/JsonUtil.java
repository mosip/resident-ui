package io.mosip.testrig.residentui.utility;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.testrig.residentui.kernel.util.ConfigManager;

import org.apache.commons.io.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtil {
	private static final org.slf4j.Logger logger= org.slf4j.LoggerFactory.getLogger(JsonUtil.class);

	private static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
	}

	public static String convertJavaToJson(Object object) {

		String jsonResult = "";
		try {
			jsonResult = mapper.writeValueAsString(object);
		} catch (JsonParseException e) {
			logger.error("", e);
		} catch (JsonMappingException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
		return jsonResult;
	}

	public static <T> T convertJsonintoJava(String jsonString, Class<T> cls) {
		T payload = null;
		try {
			payload = mapper.readValue(jsonString, cls);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return payload;
	}

	public static String JsonObjSimpleParsing(String jsonIdentity, String idfield)
			throws Exception {
		String val =null;
		JSONObject json = new JSONObject(jsonIdentity);

		JSONObject identity = json.getJSONObject("identity");

		JSONArray identityitems = identity.getJSONArray(idfield);

		for (int i = 0, size = identityitems.length(); i < size; i++) {
			JSONObject idItem = identityitems.getJSONObject(i);
			String lang = idItem.getString("language");
			val = idItem.getString("value");
			if (lang.equalsIgnoreCase(ConfigManager.getloginlang()))
				return val;
		}
		return "sin";
	}


	/**
	 * Direct String
	 * 
	 * @param json
	 * @param jsonObjName
	 * @param idfield
	 * @return
	 * @throws Exception
	 */
	public static String JsonObjParsing(String jsonIdentity, String idfield) throws Exception {
		String value = null;
		JSONObject json = new JSONObject(jsonIdentity);
		JSONObject identity = json.getJSONObject("identity");

		value = identity.getString(idfield);

		return value;
	}

	public static double JsonObjDoubleParsing(String jsonIdentity, String idfield) throws Exception {
		double value = 0;
		JSONObject json = new JSONObject(jsonIdentity);
		JSONObject identity = json.getJSONObject("identity");

		value = identity.getDouble(idfield);

		return value;
	}

	public static List<String> JsonObjArrayListParsing(String jsonIdentity, String idfield) throws Exception {
		List<String> list = new LinkedList<String>();
		JSONObject json = new JSONObject(jsonIdentity);

		JSONObject identity = json.getJSONObject("identity");

		JSONArray identityitems = identity.getJSONArray(idfield);
		if (identityitems != null) {
			for (int i = 0; i < identityitems.length(); i++) {
				list.add(identityitems.getString(i));
			}
		}
		return list;

	}
	public static String JsonObjArrayListParsing2(String idfield) throws Exception {
		String a="";
		if(idfield.equals("hierarchyLevel0")) 
			a=ConfigManager.gethierarchyLevel0();
		else if(idfield.equals("hierarchyLevel1")) 
			a=ConfigManager.gethierarchyLevel1();

		else if(idfield.equals("hierarchyLevel2")) 
			a=ConfigManager.gethierarchyLevel2();

		else if(idfield.equals("hierarchyLevel3")) 
			a=ConfigManager.gethierarchyLevel3();

		else if(idfield.equals("hierarchyLevel4")) 
			a=ConfigManager.gethierarchyLevel4();

		JSONArray jsonArray = new JSONArray(a);



		for (int i = 0, size = jsonArray.length(); i < size; i++) {
			JSONObject idItem = jsonArray.getJSONObject(i);
			String lang = idItem.getString("language");
			String val = idItem.getString("value");
			if (lang.equals(ConfigManager.getloginlang())) {
				return val;
			}

		}
		return "";


	}
	public static String  readJsonFileText(String document) {

		String jsonTxt = null;
		File f=null;

		try {

			if (TestRunner.checkRunType().equalsIgnoreCase("JAR")) {
				f = new File(TestRunner.getResourcePath() + "/" +document);
			} else if (TestRunner.checkRunType().equalsIgnoreCase("IDE")) {


				f = new File(System.getProperty("user.dir") +  System.getProperty("path.config")+ "/"+document);
			}
			if (f.exists()) {
				InputStream is = new FileInputStream(f);
				jsonTxt = IOUtils.toString(is, "UTF-8");
				logger.info("readJsonFileText");
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return jsonTxt;
	}


}
