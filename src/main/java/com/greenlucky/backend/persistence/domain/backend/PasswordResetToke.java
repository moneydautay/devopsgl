package com.greenlucky.backend.persistence.domain.backend;

import com.greenlucky.backend.persistence.converters.LocalDatetimeAttributeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by Mr Lam on 7/11/2016.
 */
@Entity
public class PasswordResetToke implements Serializable{

    /** The Serial Version UID for Serializable classes */
    private static final long serialVersionUID = 1L;
    
    /** The application logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordResetToke.class);
    private static final Integer DEFAULT_TOKEN_LENGTH_IN_MINUTES = 120;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "expiry_date")
    @Convert(converter = LocalDatetimeAttributeConverter.class)
    private LocalDateTime expiryDate;

    public PasswordResetToke() {
    }

    public PasswordResetToke(String token, User user, LocalDateTime createDateTime, int expirationInMinutes) {
        if(token == null || user == null || createDateTime == null)
            throw new IllegalArgumentException("Token, User and creation date time can't be not null.");
        if(expirationInMinutes == 0){
            LOGGER.warn("The token expiration in minutes length is zero. Assign the default value {}", DEFAULT_TOKEN_LENGTH_IN_MINUTES);
            expirationInMinutes = DEFAULT_TOKEN_LENGTH_IN_MINUTES;
        }
        this.token = token;
        this.user = user;
        this.expiryDate = createDateTime.plusMinutes(expirationInMinutes);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PasswordResetToke that = (PasswordResetToke) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PasswordResetToke{");
        sb.append("id=").append(id);
        sb.append(", token='").append(token).append('\'');
        sb.append(", user=").append(user);
        sb.append(", expiryDate=").append(expiryDate);
        sb.append('}');
        return sb.toString();
    }
}
