package com.greenlucky.backend.persistence.domain.backend;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mr Lam on 5/11/2016.
 */
@Entity
@Table(name = "user")
public class User implements Serializable{

    /** The Serial Version UID for Serializable classes */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;

    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Length(max = 500)
    private String description;

    private String country;

    @Column(name = "profile_image_url")
    private String stripeImageUrl;

    @Column(name = "stripe_customer_id")
    private String stripeCustomerId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plan_id")
    private Plan planId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserRole> userRoles = new HashSet<UserRole>();

    public User(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStripeImageUrl() {
        return stripeImageUrl;
    }

    public void setStripeImageUrl(String stripeImageUrl) {
        this.stripeImageUrl = stripeImageUrl;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public Plan getPlanId() {
        return planId;
    }

    public void setPlanId(Plan planId) {
        this.planId = planId;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", phoneNumber='").append(phoneNumber).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append(", stripeImageUrl='").append(stripeImageUrl).append('\'');
        sb.append(", stripeCustomerId='").append(stripeCustomerId).append('\'');
        sb.append(", planId=").append(planId);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id == user.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
