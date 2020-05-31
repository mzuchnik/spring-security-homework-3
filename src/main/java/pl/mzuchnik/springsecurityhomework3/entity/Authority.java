package pl.mzuchnik.springsecurityhomework3.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
public class Authority implements GrantedAuthority{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private AuthorityType name;

    public Authority() {
    }

    public Authority(AuthorityType name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuthorityType getName() {
        return name;
    }

    public void setName(AuthorityType name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name.name();
    }

    @Override
    public String toString() {
        return "Authority{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
