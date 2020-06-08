package org.project.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    //Элемент в этой коллеккции является инстансом Role
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    //Таблица, которая связывает user и его роль
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    //Нумеруется построчно
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    //Связь одни ко многим
    @OneToMany(fetch = FetchType.EAGER)
    //Таблица, которая связывает пользователя с диском
    @JoinTable(name = "user_current_disk", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "disk_id"))
    private Set<Disk> currentDisks = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_old_disks", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "disk_id"))
    private Set<Disk> oldDisks = new HashSet<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Disk> getCurrentDisks() {
        return currentDisks;
    }

    public void setCurrentDisks(Set<Disk> currentDisks) {
        this.currentDisks = currentDisks;
    }

    public Set<Disk> getOldDisks() {
        return oldDisks;
    }

    public void setOldDisks(Set<Disk> oldDisks) {
        this.oldDisks = oldDisks;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
