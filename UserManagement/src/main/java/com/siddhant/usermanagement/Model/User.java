package com.siddhant.usermanagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable=false, length=40)
    private String firstName;

    @Column(nullable = false, length=40)
    private String lastName;

    @Column(nullable = false, unique=true,length=80)
    private String email;

    @Column(nullable = false)
    private String password;

}
