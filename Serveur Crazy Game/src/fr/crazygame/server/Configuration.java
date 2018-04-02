package fr.crazygame.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Configuration {
	@JsonProperty("Port")
	private final int port;
	@JsonProperty("RepLogs")
	private final String repLogs;
	@JsonProperty("TimeOut")
	private final long timeOut;
	
	private Configuration () {
		port = 7777;
		repLogs = "./Logs";
		timeOut = 300;
	}
	
	public int getPort() {
		return port;
	}

	public String getRepLogs() {
		return repLogs;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public static Configuration getConfig (Path p) throws IOException {
		String body;
		ObjectMapper mapper = new ObjectMapper();
		
		try (Stream <String> lines = Files.lines(p)) {
			body = lines.reduce((a, b) -> a + b).get();
		}
		
		return mapper.readValue(body, Configuration.class);
	}
}
