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
public class ShipStores {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty date;
    private final SimpleStringProperty ship;
    private final SimpleStringProperty detail;
    private final SimpleStringProperty status;

    public ShipStores(int id, String date, String ship, String detail, String status) {
        
        this.id = new SimpleIntegerProperty(id);
        this.date = new SimpleStringProperty(date);
        this.ship = new SimpleStringProperty(ship);
        this.detail = new SimpleStringProperty(detail);
        this.status = new SimpleStringProperty(status);
        
    }

    public Integer getId() {

        return this.id.get();

    }

    public String getDate() {
        return this.date.get();
    }

    public String getShip() {
        return this.ship.get();
    }

    public String getDetail() {
        return this.detail.get();
    }
    
    public String getStatus() {
        return this.status.get();
    }
    
    public void setId(int id) {

        this.id.set(id);

    }

    public void setDate(String date) {

        this.date.set(date);

    }

    public void setShip(String ship) {

        this.ship.set(ship);

    }

    public void setDetail(String detail) {

        this.detail.set(detail);

    }
    
    public void setStatus (String status) {

        this.status.set(status);

    }

}
