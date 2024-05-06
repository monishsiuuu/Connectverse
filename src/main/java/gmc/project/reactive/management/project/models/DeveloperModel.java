package gmc.project.reactive.management.project.models;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;

@Data
public class DeveloperModel implements Serializable {
	
	public DeveloperModel() {}
	
	public DeveloperModel(String principalString) {
		
		Pattern namePattern = Pattern.compile("name=([^,]+)");
		Pattern emailPattern = Pattern.compile("email=([^,]+)");
		Pattern picturePattern = Pattern.compile("picture=([^,]+)");

		Matcher nameMatcher = namePattern.matcher(principalString);
		Matcher emailMatcher = emailPattern.matcher(principalString);
		Matcher pictureMatcher = picturePattern.matcher(principalString);

		if (nameMatcher.find())
			this.name = nameMatcher.group(1).trim();
		if (emailMatcher.find())
			this.email = emailMatcher.group(1).trim().replace("}]", "");
		if (pictureMatcher.find())
			this.profilePicUrl = pictureMatcher.group(1).trim();
	}
	
	private static final long serialVersionUID = -1459377816706640970L;
	
	private String id;
	
	private String profilePicUrl;
	
	private String name;
	
	private String username;
	
	private String password;
	
	private String email;
	
	private String githubProfile;
	
	private String linkedInProfile;
	
	private String authProvider = "Google";

}
