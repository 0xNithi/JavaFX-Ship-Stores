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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author bossn
 */
public class SuppliersInfoController implements Initializable {

    @FXML
    private ImageView image;
    @FXML
    private Text name;
    @FXML
    private TextArea detail;
    @FXML
    private Button btnClose;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        this.detail.setWrapText(true);
        
    }

    @FXML
    private void close(ActionEvent event) {

        this.btnClose.getScene().getWindow().hide();

    }

    public void init(Suppliers suppliers) {

        File file = new File("dist/images/users/" + suppliers.getSource() + ".png");
        Image image = new Image(file.toURI().toString());
        this.image.setImage(image);
        
        this.name.setText(suppliers.getSource());
        
        try {

            File fin = new File("dist/data/suppliers_info.dat");
            FileInputStream fis = new FileInputStream(fin);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            for (Object line : br.lines().toArray()) {

                String[] cols = line.toString().split(":");

                if (cols[0].equals(suppliers.getSource())) {
        
                    this.detail.setText('\t' + cols[1]);
                    break;
                    
                }
            }

            br.close();
            fis.close();
            
        } catch (FileNotFoundException ex) {
            
            Logger.getLogger(SuppliersInfoController.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            
            Logger.getLogger(SuppliersInfoController.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
    }

}
