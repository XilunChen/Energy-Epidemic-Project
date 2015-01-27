package opentsdbquery.system;

import java.io.InputStream;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class OpenTSDBHttpConnection {
	private String baseUrl;
	
	public OpenTSDBHttpConnection(String base_url) {
		baseUrl = base_url;
	}
	
	public String getMin(String start, String end, String metric, String tag) throws Exception {
		URL url = new URL(baseUrl+"api/query?start="+start+"&end="+end
				+"&m=min:"+metric+"{"+tag+"}");
		try (InputStream is = url.openStream();
				JsonReader rdr = Json.createReader(is)) {
			JsonArray queries = rdr.readArray();
			JsonObject result = queries.getValuesAs(JsonObject.class).get(0);
			
			return result.getJsonObject("dps").toString();
		}
	}
	
	public String getSum(String start, String end, String metric, String tag) throws Exception {
		URL url = new URL(baseUrl+"api/query?start="+start+"&end="+end
				+"&m=sum:"+metric+"{"+tag+"}");
		try (InputStream is = url.openStream();
				JsonReader rdr = Json.createReader(is)) {
			JsonArray queries = rdr.readArray();
			JsonObject result = queries.getValuesAs(JsonObject.class).get(0);
			
			return result.getJsonObject("dps").toString();
		}
	}
	
	public String getMax(String start, String end, String metric, String tag) throws Exception {
		URL url = new URL(baseUrl+"api/query?start="+start+"&end="+end
				+"&m=max:"+metric+"{"+tag+"}");
		try (InputStream is = url.openStream();
				JsonReader rdr = Json.createReader(is)) {
			JsonArray queries = rdr.readArray();
			JsonObject result = queries.getValuesAs(JsonObject.class).get(0);
			
			return result.getJsonObject("dps").toString();
		}
	}
	
	public String getAvg(String start, String end, String metric, String tag) throws Exception {
		URL url = new URL(baseUrl+"api/query?start="+start+"&end="+end
				+"&m=avg:"+metric+"{"+tag+"}");
		try (InputStream is = url.openStream();
				JsonReader rdr = Json.createReader(is)) {
			JsonArray queries = rdr.readArray();
			JsonObject result = queries.getValuesAs(JsonObject.class).get(0);
			
			return result.getJsonObject("dps").toString();
		}
	}
	
	public String getDev(String start, String end, String metric, String tag) throws Exception {
		URL url = new URL(baseUrl+"api/query?start="+start+"&end="+end
				+"&m=dev:"+metric+"{"+tag+"}");
		try (InputStream is = url.openStream();
				JsonReader rdr = Json.createReader(is)) {
			JsonArray queries = rdr.readArray();
			JsonObject result = queries.getValuesAs(JsonObject.class).get(0);
			
			return result.getJsonObject("dps").toString();
		}
	}
}
