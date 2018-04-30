package com.nel;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;

public class NEL {

	private static Logger logger = Logger.getLogger(NEL.class);
	
	private static final String DBPEDIA = "http://dbpedia.org/ontology/";
	private static final String SCHEMA = "http://schema.org/";
	
	public static void main(String[] args) throws Exception {
		NEL n = new NEL();
		String sentence = "Terrorist attacks by E.T.A. have declined in recent years and the number of its hardcore militants is thought to have fallen from the hundreds of 15 years ago to several score.";
		System.out.println(sentence);
		List<NE> nes = n.extractEntitiesSpotlight(sentence, "0.7");
		logger.info(sentence);
		logger.info("NEs");
		for(NE ne : nes) {
			System.out.println(ne.printNE());
		}
		
		if(nes.size() > 1) {
			for(int i = 0; i < nes.size() ;i++){
				System.out.println(sentence.substring(nes.get(i).getEnd(), nes.get(++i).getBegin()));
			}
		}

	}
	
	@SuppressWarnings("deprecation")
	public List<NE> extractEntitiesSpotlight(String paragraph, String confidence) throws Exception {
	    	List<NE> listEntDesc = new ArrayList<NE>();
	        //HttpPost post = new HttpPost("http://api.dbpedia-spotlight.org/rest/annotate");
			HttpPost post = new HttpPost("http://model.dbpedia-spotlight.org/en/annotate");
			DefaultHttpClient cliente = new DefaultHttpClient();
	        List<NameValuePair> formparams = new ArrayList<NameValuePair>();

	        formparams.add(new BasicNameValuePair("text", paragraph));
	        formparams.add(new BasicNameValuePair("confidence", confidence));
	        post.setHeader("Accept","application/json");
	        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams);
	        entity.setContentEncoding(HTTP.UTF_8);
	        post.setEntity(entity);
	        HttpResponse respons;
	        try{
	        	respons = cliente.execute(post);
	        }catch(Exception e){
	        	logger.fatal("No response from DBpedia WS");
	        	return listEntDesc;
	        }
	        InputStreamReader input = new InputStreamReader(respons.getEntity().getContent());
	        
	        JsonReader jrdr = Json.createReader(input);
	        JsonObject obj = jrdr.readObject();
	        int entityCounter = 0;
	        List<String> listEntities = new ArrayList<String>();
	        JsonArray entities = obj.getJsonArray("Resources");
	        if(entities != null){
	        	for(JsonObject entt : entities.getValuesAs(JsonObject.class)){
	        		NE entDesc = new NE();
	        		String context = obj.getString("@text");
	            	String URI = entt.getString("@URI");
	            	String similarityScore = entt.getString("@similarityScore");
	            	String surfaceForm = entt.getString("@surfaceForm");
	            	String types = entt.getString("@types");
	            	int beginIndex = context.indexOf(surfaceForm);
	            	int endIndex = beginIndex + surfaceForm.length();
	            	
	            	entDesc.setMention(surfaceForm);
	            	entDesc.setTaConfidence(Double.parseDouble(similarityScore));
	            	entDesc.setBegin(beginIndex);
	            	entDesc.setEnd(endIndex);
	            	entDesc.setTaIdentRef(URI);
	            	//setting types
	            	if(!types.isEmpty() && types.length() > 1){
	            		List<String> taClassRef = new ArrayList<String>();
	            		for(String type : types.split(","))
	            			taClassRef.add(spotlightRedefineTypes(type));        
	            		if(!taClassRef.isEmpty())
	            			entDesc.setTaClassRef(taClassRef);
	            	}
	            	entityCounter++;
	            	listEntities.add(URI);
	            	listEntDesc.add(entDesc);
	            }
	        }
	        return listEntDesc;
	    }
	
	private String spotlightRedefineTypes(String origType){
		String newType = "";
		String[] leftSideString = origType.split(":");
		switch (leftSideString[0]) {
		case "DBpedia":
			newType = origType.replace("DBpedia:", DBPEDIA);
			break;
		case "Schema":
			newType = origType.replace("Schema:", SCHEMA);
			break;
		default:
			newType = origType;
		}
		
		return newType;
	}

}
