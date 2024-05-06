package gmc.project.reactive.management.project.models;

import java.io.Serializable;

public record MailModel(String to, String subject, String body) implements Serializable {

}
