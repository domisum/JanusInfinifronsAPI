package io.domisum.janus.api;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.ezhttp.EzHttpRequestEnvoy;
import io.domisum.lib.ezhttp.request.EzHttpRequest;
import io.domisum.lib.ezhttp.request.EzUrl;
import io.domisum.lib.ezhttp.request.EzUrl.Parameter;
import io.domisum.lib.ezhttp.response.bodyreaders.EzHttpStringBodyReader;

import java.io.File;
import java.io.IOException;

@API
public class JanusApiHttp
		implements JanusApi
{
	
	// CONSTANTS
	public static final int PORT = 8381;
	
	
	// API
	@Override
	public boolean isUpdateAvailable()
			throws IOException
	{
		var buildDirectory = new File("something").getAbsoluteFile().getParentFile();
		String projectName = buildDirectory.getParentFile().getName();
		String buildName = buildDirectory.getName();
		
		var baseUrl = new EzUrl("http://localhost:"+PORT);
		var endpoint = new EzUrl(baseUrl, "/updateAvailable");
		var url = new EzUrl(endpoint, new Parameter("project", projectName), new Parameter("build", buildName));
		var request = EzHttpRequest.get(url);
		
		var envoy = new EzHttpRequestEnvoy<>(request, new EzHttpStringBodyReader());
		var ioResponse = envoy.send();
		var response = ioResponse.getOrThrowWrapped("Could not check for self update: Connection to Janus failed");
		response.getSuccessBodyOrThrowHttpIoException("Unexpected Janus HTTP response");
		String responseString = response.getSuccessBody();
		boolean updateAvailable = Boolean.parseBoolean(responseString);
		
		return updateAvailable;
	}
	
}