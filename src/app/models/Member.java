/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author bossn
 */
public class Member {
    
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty username;
    private final SimpleStringProperty password;
    private final SimpleStringProperty name;
    private final SimpleStringProperty type;

    public Member(int id, String username, String password, String name, String type) {
        this.id = new SimpleIntegerProperty(id);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
    }

    public Integer getId() {
        return id.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getPassword() {
        return password.get();
    }

    public String getName() {
        return name.get();
    }

    public String getType() {
        return type.get();
    }
    
    public void setId(int id) {
        this.id.set(id);
    }
    
    public void setUsername(String username) {
        this.username.set(username);
    }
    
    public void setPassword(String password) {
        this.password.set(password);
    }
    
    public void setName(String name) {
        this.name.set(name);
    }
    
    public void setType(String type) {
        this.type.set(type);
    }
}
