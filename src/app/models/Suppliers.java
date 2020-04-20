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
public class Suppliers {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty source;
    private final SimpleStringProperty type;
    private final SimpleStringProperty name;
    private final SimpleStringProperty qty;

    public Suppliers(int id, String source, String type, String name, String qty) {

        this.id = new SimpleIntegerProperty(id);
        this.source = new SimpleStringProperty(source);
        this.type = new SimpleStringProperty(type);
        this.name = new SimpleStringProperty(name);
        this.qty = new SimpleStringProperty(qty);
        
    }

    public Integer getId() {

        return this.id.get();

    }

    public String getSource() {

        return this.source.get();

    }

    public String getType() {

        return this.type.get();

    }

    public String getName() {

        return this.name.get();

    }

    public String getQty() {

        return this.qty.get();

    }
    
    public void setId(int id) {

        this.id.set(id);

    }

    public void setSource(String source) {

        this.source.set(source);

    }

    public void setType(String type) {

        this.type.set(type);

    }

    public void setName(String name) {

        this.name.set(name);

    }

    public void setQty(String qty) {

        this.qty.set(qty);

    }
    
}
