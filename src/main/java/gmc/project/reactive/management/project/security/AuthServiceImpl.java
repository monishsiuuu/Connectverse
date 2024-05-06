
package gmc.project.reactive.management.project.security;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.function.Supplier;

import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import gmc.project.reactive.management.project.dao.DeveloperDao;
import gmc.project.reactive.management.project.entities.DeveloperEntity;
import gmc.project.reactive.management.project.models.DeveloperModel;
import reactor.core.publisher.Mono;

@Service
public class AuthServiceImpl implements AuthService {

	private static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=400x400&chld=M%%7C0&cht=qr&chl=";
	
	private static String APP_NAME = "GMC - Navin@3d";

	@Autowired
	private DeveloperDao developerDao;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private boolean isValidLong(String code) {
		try {
			Long.parseLong(code);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	private String generateQRUrl(String userId, String secureCode) {
		try {
			return QR_PREFIX + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", APP_NAME,
					userId, secureCode, APP_NAME), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "try again...";
		}
	}

	@Override
	public Mono<UserDetails> findByUsername(String username) {
		if (username.contains(OTP_SEPERATOR)) {
			String userName = username.split(OTP_SEPERATOR)[0];
			Mono<DeveloperEntity> foundDeveloper = developerDao.findByEmail(userName);
			String otp = username.split(OTP_SEPERATOR)[1];
			return foundDeveloper.map(developer -> {
				if (developer.getEnabledM2F()) {
					Totp totp = new Totp(developer.getM2FId());
					if (!isValidLong(otp) || !totp.verify(otp)) {
						return User.withUsername(userName).password("").authorities(new ArrayList<>())
								.accountExpired(true).credentialsExpired(true).disabled(true).accountLocked(true)
								.build();
					}
				}
				return User.withUsername(developer.getId()).password(developer.getPassword()).authorities(new ArrayList<>())
						.accountExpired(false).credentialsExpired(false).disabled(false).accountLocked(false).build();
			});
		}
		return developerDao.findByEmail(username)
				.map(u -> User.withUsername(u.getId()).password(u.getPassword()).authorities(new ArrayList<>())
						.accountExpired(false).credentialsExpired(false).disabled(false).accountLocked(false).build());
	}

	@Override
	public Mono<Boolean> isM2FEnabled(String userId) {
		return developerDao.existsByEmailAndEnabledM2F(userId, true);
	}

	@Override
	public Mono<String> toggleM2F(String userId, Boolean status) {
		if (status) {
			String secureId = Base32.random();
			developerDao.m2FStatus(userId, secureId, status).subscribe();
			Supplier<String> qrSupplier = () -> generateQRUrl(userId, secureId);
			return Mono.fromSupplier(qrSupplier);
		} else
			return developerDao.m2FStatus(userId, "", status).map(nothing -> "");
	}
	
	private DeveloperEntity assignValuesToEntity(DeveloperEntity entity, DeveloperModel model) {
		entity.setName(model.getName());
		entity.setProfilePicUrl(model.getProfilePicUrl());
		entity.setEmail(model.getEmail());
		entity.setAuthProvider(model.getAuthProvider());
		if (model.getAuthProvider().equals("Native"))
			entity.setPassword(bCryptPasswordEncoder.encode(model.getPassword()));
		return entity;
	}

	@Override
	public Mono<DeveloperModel> registerUser(DeveloperModel developerModel) {
		return developerDao.existsByEmail(developerModel.getEmail()).flatMap(isPresent -> {
			if(isPresent)
				return developerDao.findByEmail(developerModel.getEmail()).flatMap(foundUser -> {
					return developerDao.save(assignValuesToEntity(foundUser, developerModel)).map(savedUser -> {
						developerModel.setId(savedUser.getId());
						return developerModel;
					});
				});
			return developerDao.save(assignValuesToEntity(new DeveloperEntity(), developerModel)).map(saveduser -> {
				developerModel.setId(saveduser.getId());
				return developerModel;
			});
		});
	}

	@Override
	public Mono<DeveloperModel> completeProfile(DeveloperModel developerModel) {
		Mono<DeveloperEntity> requestingDeveloper = developerDao.findById(developerModel.getId());
		Mono<DeveloperEntity> updatedDeveloper = requestingDeveloper.mapNotNull(developer -> {
			developer.setProfilePicUrl(developerModel.getProfilePicUrl());
			developer.setUsername(developerModel.getUsername());
			developer.setGithubProfile(developerModel.getGithubProfile());
			developer.setLinkedInProfile(developerModel.getLinkedInProfile());
			return developer;
		});
		Mono<DeveloperEntity> savedDeveloper = updatedDeveloper.flatMap(developer -> developerDao.save(developer));
		Mono<DeveloperModel> returnValue = savedDeveloper.map(saved -> {
			developerModel.setEmail(saved.getEmail());
			developerModel.setName(saved.getName());
			developerModel.setAuthProvider(saved.getAuthProvider());
			return developerModel;
		});
		return returnValue;
	}
	
	@Override
	public Mono<Boolean> registerMany(Iterable<DeveloperEntity> developerEntities) {
		return developerDao.saveAll(developerEntities).flatMap(data -> Mono.just(true)).next();
	}

}
