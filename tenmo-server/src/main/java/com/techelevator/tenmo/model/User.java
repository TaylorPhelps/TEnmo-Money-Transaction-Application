package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Legacy code that was already implemented for the user. Updated to work with JPA.
 *  @author Sadiq Manji, Ja'Michael Garcia
 *  @version 1.1
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tenmo_user")
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user_id")
   @SequenceGenerator(name = "seq_user_id", sequenceName = "seq_user_id", allocationSize = 1)
   @Column(name = "user_id", nullable = false)
   private Integer id;
   @Column(length = 50, nullable = false, unique = true)
   private String username;
   @JsonIgnore // prevent from being sent to client
   @Transient
   private String password;
   @JsonIgnore
   @Transient
   private boolean activated;
   @Transient
   private Set<Authority> authorities = new HashSet<>();
   @Column(name = "password_hash", nullable = false, length = 200)
   private String passwordHash;
   @Column(name = "role",length = 20)
   private String role;

   public User(int id, String username, String password, String authorities) {
      this.id = id;
      this.username = username;
      this.password = password;
      if (authorities != null) this.setAuthorities(authorities);
      this.activated = true;
   }

   public void setAuthorities(String authorities) {
      String[] roles = authorities.split(",");
      for (String role : roles) {
         this.authorities.add(new Authority("ROLE_" + role));
      }
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      User user = (User) o;
      return id == user.id &&
              activated == user.activated &&
              Objects.equals(username, user.username) &&
              Objects.equals(password, user.password) &&
              Objects.equals(authorities, user.authorities);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, username, password, activated, authorities);
   }

   @Override
   public String toString() {
      return "User{" +
              "id=" + id +
              ", username='" + username + '\'' +
              ", activated=" + activated +
              ", authorities=" + authorities +
              '}';
   }
}
