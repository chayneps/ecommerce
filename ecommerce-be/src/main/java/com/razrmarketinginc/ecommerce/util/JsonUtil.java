package com.razrmarketinginc.ecommerce.util;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.FileSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import groovy.lang.GString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
public class JsonUtil {

	public static class InputStreamSerializer extends StdSerializer<InputStream> {

		/**
		 *
		 */
		private static final long serialVersionUID = -7205822087988939493L;

		public InputStreamSerializer() {
			super(InputStream.class);
		}

		public InputStreamSerializer(Class<InputStream> t) {
			super(t);
		}

		@Override
		public void serialize(InputStream inputStream, JsonGenerator jGen,
							  SerializerProvider provider) throws IOException {


			jGen.writeBinary(Base64Variants.getDefaultVariant(),inputStream,-1);



		}

	}

	public static class GStringSerializer extends StdSerializer<GString> {

		public GStringSerializer(){
			super(GString.class);

		}

		@Override
		public void serialize(GString value, JsonGenerator gen, SerializerProvider serializers)
				throws IOException, JsonProcessingException {
			gen.writeString(value.toString());
		}

	}


	public static final FileSerializer fileSerializer = new FileSerializer();
	public static final InputStreamSerializer inputStreamSerializer = new InputStreamSerializer();
	public static final GStringSerializer gStringSerializer = new GStringSerializer();



	private static final JsonMapper mapper= JsonMapper.builder()
			.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
			.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
			.enable(JsonParser.Feature.ALLOW_COMMENTS)
			.enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
			.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)
			.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
			.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
			.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES,true)
			.build();

	private static final YAMLMapper yamlMapper = YAMLMapper.builder()
			.enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
			.build();


	static {
		SimpleModule module = new SimpleModule();
		module.addSerializer(File.class,fileSerializer);
		module.addSerializer(InputStream.class,inputStreamSerializer);
		module.addSerializer(GString.class,gStringSerializer);
		mapper.registerModule(module);
	}



	public static ObjectMapper getMapper(){

		return mapper;

	}

	public static ObjectMapper getReadMapper(){

	    return mapper;

	}


	public static ObjectMapper getWriteMapper(){

	    return mapper;

	}

	public static String prettyPrint(String strJson){
    	return prettyMarshall(unmarshall(strJson,whatType(strJson)));
	}

	public static String prettyMarshall(Object obj){
    	try {
			return getWriteMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch(Exception e){
    		return null;
		}
	}

	public static <T,E> E serializedClone(T object, Class<E> type) throws IOException {
		return getReadMapper().readValue(
				getWriteMapper().writeValueAsString(object),type);
	}

	@SuppressWarnings("unchecked")
	public static <T> T serializedClone(T object) {
		try {
			return serializedClone(object,(Class<T>) object.getClass());
		} catch(Exception e) {
			return null;
		}
	}

	public static String marshall(Object objectToMarshall) {
		String jsonOutput = "";
		try {
			jsonOutput = getWriteMapper().writeValueAsString(objectToMarshall);
			return jsonOutput;

		} catch (Exception e) {
			log.debug("Error: {} Object to Marshal : {}",e.getMessage(),
					objectToMarshall.toString());
			return null;
		}

	}

	public static Object unmarshall(String jsonStr){

    	return unmarshall(jsonStr,whatType(jsonStr));
	}

	public static <T> T unmarshall(String jsonStr,Class<T> clazz) {
		jsonStr = StringUtils.trimToNull(jsonStr);
		if(clazz != null && jsonStr!=null) {
			T object = null;
			try {
				object = getReadMapper().readValue(jsonStr, clazz);
				return object;

			} catch (Exception e) {
				log.debug("Error: {} String to Unmarshal : {}", e.getMessage(), jsonStr);
			}
		}
		return null;

	}

	public static Class<?> whatType(String jsonStr){
		jsonStr = StringUtils.trimToNull(jsonStr);
		if(jsonStr!=null) {
			char char0 = jsonStr.charAt(0);
			if (char0 == '[')
				return List.class;
			else if (char0 == '{')
				return Map.class;
			else if (jsonStr.equals("true") || jsonStr.equals("false"))
				return Boolean.class;
			else if (jsonStr.substring(0, 1).matches("^[0-9.]+$"))
				return Number.class;
			else
				return String.class;
		}

		return null;

	}


	public static boolean isJsonValid(String str){
    	try {
			getReadMapper().readTree(str);
			return true;
		} catch (Exception e){
    		return false;
		}
	}

	public static String validateJson(String str){
    	if(isJsonValid(str))
    		return str;

    	throw new org.springframework.boot.json.JsonParseException();

	}

	public static String asYaml(Object object) {
    	try {
			// parse JSON
			//JsonNode jsonNodeTree = new ObjectMapper().readTree(jsonString);
			// save it as YAML
			String jsonAsYaml = yamlMapper.writeValueAsString(object);
			return jsonAsYaml;
		} catch(Exception e){
    		log.debug(e.getMessage());
    		return null;
		}

    }

    public static YAMLMapper getYamlMapper(){
    	return yamlMapper;
	}

}
