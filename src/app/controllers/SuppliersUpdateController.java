/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controllers;

import app.models.Suppliers;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author bossn
 */
public class SuppliersUpdateController implements Initializable {

    @FXML
    private TextField id;
    @FXML
    private TextField source;
    @FXML
    private TextField type;
    @FXML
    private TextField name;
    @FXML
    private TextField qty;
    @FXML
    private Button btnSubmit;
    @FXML
    private Button btnCancel;
    
    private Suppliers suppliers;
    private ObservableList<Suppliers> dataList;
    private SuppliersController suppliersController;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void submit(ActionEvent event) {

        if (!this.source.getText().isEmpty() && !this.type.getText().isEmpty() && !this.name.getText().isEmpty() && !this.qty.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ship Store - Suppliers Update");
            alert.setHeaderText("Are you sure you want to update ?");
            alert.setContentText(
                    "Sourse: " + this.source.getText()
                    + ", Type: " + this.type.getText()
                    + ", Name: " + this.name.getText()
                    + ", Quantity: " + this.qty.getText()
            );

            ButtonType btnCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getDialogPane().getButtonTypes().add(btnCancel);

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/resources/images/warning.png"));

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                this.update(this.id.getText(), this.source.getText(), this.type.getText(), this.name.getText(), this.qty.getText());

                this.btnSubmit.getScene().getWindow().hide();

            }

        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ship Store - Suppliers Update Warning!");
            alert.setHeaderText("Please complete all information.");
            alert.setContentText(
                    "Please enter the "
                    + (this.source.getText().isEmpty() ? "Source" : (this.type.getText().isEmpty() ? "Type" : (this.name.getText().isEmpty() ? "Name" : "Quantity")))
            );

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/resources/images/warning.png"));

            alert.show();

        }
    }

    @FXML
    private void cancel(ActionEvent event) {

        this.btnCancel.getScene().getWindow().hide();

    }

    public void update(String id, String source, String type, String name, String qty) {

        try {
            File fin = new File("dist/data/suppliers.dat");
            FileInputStream fis = new FileInputStream(fin);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            StringBuffer inputBuffer = new StringBuffer();
            for (Object line : br.lines().toArray()) {

                String[] row = line.toString().split(",");

                if (row[0].equals(id)) {
                    
                    String str = row[0] + ',' + source + ',' + type + ',' + name + ',' + qty + '\n';
                    inputBuffer.append(str);
                    
                    for (int i = 0; i < this.dataList.size(); i++) {
                        
                        if (this.dataList.get(i) == this.suppliers) {
                            
                            this.dataList.set(i, new Suppliers(Integer.parseInt(id), source, type, name, qty));
                            this.suppliersController.UpdateChartData(this.suppliers, this.dataList.get(i));
                        }
                        
                    }

                } else {

                    inputBuffer.append(line.toString()).append('\n');

                }
            }
            
            inputBuffer.deleteCharAt(inputBuffer.length() - 1);

            FileOutputStream fileOut = new FileOutputStream("dist/data/suppliers.dat");
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();
            
        } catch (FileNotFoundException ex) {
            
            Logger.getLogger(SuppliersUpdateController.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            
            Logger.getLogger(SuppliersUpdateController.class.getName()).log(Level.SEVERE, null, ex);
            
        }

    }
    
    public void setDataList(ObservableList<Suppliers> dataList) {
        
        this.dataList = dataList;
        
    }
    
    public void setSuppliers(Suppliers suppliers) {
        
        this.suppliers = suppliers;
        
        this.id.setText(this.suppliers.getId().toString());
        this.source.setText(this.suppliers.getSource());
        this.type.setText(this.suppliers.getType());
        this.name.setText(this.suppliers.getName());
        this.qty.setText(this.suppliers.getQty().toString());
        
    }
    
    public void setSuppliersController(SuppliersController controller) {
        
        this.suppliersController = controller;
        
    }

}
