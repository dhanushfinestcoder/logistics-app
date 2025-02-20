package com.example.logistics_application.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Users
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Uid;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String UemailId;
    @Column(unique = true, nullable = false)
    private String Upass;
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Role role;


}
