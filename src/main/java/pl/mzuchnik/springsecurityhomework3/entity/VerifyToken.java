package pl.mzuchnik.springsecurityhomework3.entity;

import javax.persistence.*;

@Entity
public class VerifyToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private User user;

    @Column(name = "token_type")
    @Enumerated(value = EnumType.STRING)
    private VerifyTokenType verifyTokenType;

    public VerifyToken() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public VerifyTokenType getVerifyTokenType() {
        return verifyTokenType;
    }

    public void setVerifyTokenType(VerifyTokenType verifyTokenType) {
        this.verifyTokenType = verifyTokenType;
    }
}
