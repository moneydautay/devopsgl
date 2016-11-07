package com.greenlucky.test.intergation;

import com.greenlucky.backend.persistence.domain.backend.PasswordResetToken;
import com.greenlucky.backend.persistence.domain.backend.User;
import com.greenlucky.backend.persistence.responsitories.PasswordResetTokenRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Mr Lam on 7/11/2016.
 */
public class PasswordResetTokenRepositoryTest extends AbstractIntegrationTest{

    @Value("${token.expiration.length.minutes}")
    private int expirationTimeMinutes;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Rule public TestName testName = new TestName();


    @Before
    public void init(){
        Assert.assertFalse(expirationTimeMinutes==0);
    }

    @Test
    public void testTokenExpirationLength() throws Exception{
        User user = createBasicUser(testName);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());

        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
        String token = UUID.randomUUID().toString();

        LocalDateTime expectTime = now.plusMinutes(expirationTimeMinutes);

        PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);

        LocalDateTime actualTime = passwordResetToken.getExpiryDate();
        Assert.assertNotNull(actualTime);
        Assert.assertEquals(expectTime, actualTime);
    }

    @Test
    public void testFindTokenByTokenValue() throws Exception{
        User user = createBasicUser(testName);
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

        createPasswordResetToken(token, user,now);
        PasswordResetToken retrievedPasswordResetToken = passwordResetTokenRepository.findByToken(token);
        Assert.assertNotNull(retrievedPasswordResetToken);
        Assert.assertNotNull(retrievedPasswordResetToken.getId());
        Assert.assertNotNull(retrievedPasswordResetToken.getUser());
    }

    @Test
    public void testDeleteToken() throws Exception{
        User user = createBasicUser(testName);
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

        PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);
        long tokenId = passwordResetToken.getId();
        passwordResetTokenRepository.delete(tokenId);

        PasswordResetToken shouldNotExistToken = passwordResetTokenRepository.findOne(tokenId);

        Assert.assertNull(shouldNotExistToken);
    }

    @Test
    public void testCascadeDeleteFromUserEntity() throws Exception{
        User user = createBasicUser(testName);
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

        PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);
        long tokenId = passwordResetToken.getId();
        passwordResetTokenRepository.delete(tokenId);

        Set<PasswordResetToken> shouldNotExistTokens = passwordResetTokenRepository.findAllByUserId(user.getId());

        Assert.assertTrue(shouldNotExistTokens.isEmpty());
    }

    @Test
    public void testMultipleTokensAreReturnedWhenQueringUserId() throws Exception{
        User user = createBasicUser(testName);
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

        String token1 = UUID.randomUUID().toString();
        String token2 = UUID.randomUUID().toString();
        String token3 = UUID.randomUUID().toString();

        Set<PasswordResetToken> passwordResetTokens = new HashSet<>();
        passwordResetTokens.add(createPasswordResetToken(token1, user, now));
        passwordResetTokens.add(createPasswordResetToken(token2, user, now));
        passwordResetTokens.add(createPasswordResetToken(token3, user, now));

        passwordResetTokenRepository.save(passwordResetTokens);

        User foundUser = userRepository.findOne(user.getId());

        Set<PasswordResetToken> actualTokens = passwordResetTokenRepository.findAllByUserId(foundUser.getId());
        Assert.assertTrue(actualTokens.size() == passwordResetTokens.size());

        List<String> tokensAsList = passwordResetTokens.stream().map(ptr->ptr.getToken()).collect(Collectors.toList());
        List<String> actualsTokensAsList = actualTokens.stream().map(ptr->ptr.getToken()).collect(Collectors.toList());
        Assert.assertEquals(tokensAsList, actualsTokensAsList);
    }

    private PasswordResetToken createPasswordResetToken(String token, User user, LocalDateTime now) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user, now, expirationTimeMinutes);
        passwordResetTokenRepository.save(passwordResetToken);
        Assert.assertNotNull(passwordResetToken.getId());
        return passwordResetToken;
    }


}