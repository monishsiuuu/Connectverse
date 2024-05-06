package gmc.project.reactive.management.project.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "settings.app")
public class AppSettings {
	
	private Integer pageSize;
	
	private Boolean disableSecurity;
	
	private String batchFilesPath;

}
