package com.myplantdiary.enterprise.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "messages")
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String messages;

    public Long getId() {
        return id;
    }
    public String getEmail(){
        return email;
    }
    public String getMessages(){
        return messages;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public Messages(Long id, String email, String messages){
        this.id = id;
        this.email = email;
        this.messages = messages;
    }
    public Messages()
    {

    }
}
