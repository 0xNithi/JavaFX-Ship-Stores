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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
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
public class SuppliersInsertController implements Initializable {

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
    
    private ObservableList<Suppliers> dataList;
    private SuppliersController suppliersController;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void submit(ActionEvent event) {

        if (!this.source.getText().isEmpty() && !this.type.getText().isEmpty() && !this.name.getText().isEmpty() && !this.qty.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ship Store - Suppliers Insert");
            alert.setHeaderText("Are you sure you want to insert ?");
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

                this.insert(this.source.getText(), this.type.getText(), this.name.getText(), this.qty.getText());
                
                this.btnSubmit.getScene().getWindow().hide();

            }

        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ship Store - Suppliers Insert Warning!");
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

    private void insert(String a, String b, String c, String d) {

        int id = 0;
        try {

            File fin = new File("dist/data/suppliers.dat");
            FileInputStream fis = new FileInputStream(fin);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            for (Object ln : br.lines().toArray()) {

                String[] n = ln.toString().split(",");
                id = Integer.parseInt(n[0]);

            }

            id++;

        } catch (IOException e) {

            e.printStackTrace();

        }

        File file = new File("dist/data/suppliers.dat");
        FileWriter writer;
        try {

            writer = new FileWriter(file, true);
            writer.write("\n" + id + "," + a + "," + b + "," + c + "," + d);
            writer.close();
            
            this.dataList.add(new Suppliers(id, a, b, c, d));
            this.suppliersController.ChartData(a, b, Integer.parseInt(d));

        } catch (IOException e) {

            e.printStackTrace();

        }
    }
    
    public void setDataList(ObservableList<Suppliers> dataList) {
        
        this.dataList = dataList;
        
    }
    
    public void setSuppliersController(SuppliersController controller) {
        
        this.suppliersController = controller;
        
    }

}
