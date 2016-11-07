package com.greenlucky.test.intergation;

import com.greenlucky.backend.persistence.domain.backend.PasswordResetToke;
import com.greenlucky.backend.persistence.domain.backend.User;
import com.greenlucky.backend.persistence.responsitories.PasswordResetTokeRepository;
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
public class PasswordResetTokeRepositoryTest extends AbstractIntegrationTest{

    @Value("${token.expiration.length.minutes}")
    private int expirationTimeMinutes;

    @Autowired
    private PasswordResetTokeRepository passwordResetTokeRepository;

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

        PasswordResetToke passwordResetToke = createPasswordResetToken(token, user, now);

        LocalDateTime actualTime = passwordResetToke.getExpiryDate();
        Assert.assertNotNull(actualTime);
        Assert.assertEquals(expectTime, actualTime);
    }

    @Test
    public void testFindTokenByTokenValue() throws Exception{
        User user = createBasicUser(testName);
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

        createPasswordResetToken(token, user,now);
        PasswordResetToke retrievedPasswordResetToken = passwordResetTokeRepository.findByToken(token);
        Assert.assertNotNull(retrievedPasswordResetToken);
        Assert.assertNotNull(retrievedPasswordResetToken.getId());
        Assert.assertNotNull(retrievedPasswordResetToken.getUser());
    }

    @Test
    public void testDeleteToken() throws Exception{
        User user = createBasicUser(testName);
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

        PasswordResetToke passwordResetToke = createPasswordResetToken(token, user, now);
        long tokenId = passwordResetToke.getId();
        passwordResetTokeRepository.delete(tokenId);

        PasswordResetToke shouldNotExistToken = passwordResetTokeRepository.findOne(tokenId);

        Assert.assertNull(shouldNotExistToken);
    }

    @Test
    public void testCascadeDeleteFromUserEntity() throws Exception{
        User user = createBasicUser(testName);
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

        PasswordResetToke passwordResetToke = createPasswordResetToken(token, user, now);
        long tokenId = passwordResetToke.getId();
        passwordResetTokeRepository.delete(tokenId);

        Set<PasswordResetToke> shouldNotExistTokens = passwordResetTokeRepository.findAllByUserId(user.getId());

        Assert.assertTrue(shouldNotExistTokens.isEmpty());
    }

    @Test
    public void testMultipleTokensAreReturnedWhenQueringUserId() throws Exception{
        User user = createBasicUser(testName);
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

        String token1 = UUID.randomUUID().toString();
        String token2 = UUID.randomUUID().toString();
        String token3 = UUID.randomUUID().toString();

        Set<PasswordResetToke> passwordResetTokes = new HashSet<>();
        passwordResetTokes.add(createPasswordResetToken(token1, user, now));
        passwordResetTokes.add(createPasswordResetToken(token2, user, now));
        passwordResetTokes.add(createPasswordResetToken(token3, user, now));

        passwordResetTokeRepository.save(passwordResetTokes);

        User foundUser = userRepository.findOne(user.getId());

        Set<PasswordResetToke> actualTokes = passwordResetTokeRepository.findAllByUserId(foundUser.getId());
        Assert.assertTrue(actualTokes.size() == passwordResetTokes.size());

        List<String> tokensAsList = passwordResetTokes.stream().map(ptr->ptr.getToken()).collect(Collectors.toList());
        List<String> actualsTokensAsList = actualTokes.stream().map(ptr->ptr.getToken()).collect(Collectors.toList());
        Assert.assertEquals(tokensAsList, actualsTokensAsList);
    }

    private PasswordResetToke createPasswordResetToken(String token, User user, LocalDateTime now) {
        PasswordResetToke passwordResetToke = new PasswordResetToke(token, user, now, expirationTimeMinutes);
        passwordResetTokeRepository.save(passwordResetToke);
        Assert.assertNotNull(passwordResetToke.getId());
        return passwordResetToke;
    }


}