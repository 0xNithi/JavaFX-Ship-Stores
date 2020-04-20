/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controllers;

import app.models.Member;
import app.models.Suppliers;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;
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
import javafx.scene.control.MenuButton;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author bossn
 */
public class ManageMemberInsertController implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private TextField name;
    @FXML
    private MenuButton btnType;
    @FXML
    private Button btnFile;
    @FXML
    private TextArea detail;
    @FXML
    private Button btnSubmit;
    @FXML
    private Button btnCancel;

    private ObservableList<Member> dataList;
    private String type;
    private File file;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        FileChooser fileChooser = new FileChooser();

        this.btnFile.setOnAction(e -> {
            this.file = fileChooser.showOpenDialog(this.btnFile.getScene().getWindow());
            this.btnFile.setText((this.file != null ? this.file.getName() : "Choose file"));
        });

    }

    @FXML
    void setShip(ActionEvent event) {
        this.type = "ship";
        this.btnType.setText(this.type);
        this.btnFile.setDisable(true);
        this.detail.setDisable(true);
    }

    @FXML
    void setSuppliers(ActionEvent event) {
        this.type = "suppliers";
        this.btnType.setText(this.type);
        this.btnFile.setDisable(false);
        this.detail.setDisable(false);
    }

    @FXML
    void setAdmin(ActionEvent event) {
        this.type = "admin";
        this.btnType.setText(this.type);
        this.btnFile.setDisable(true);
        this.detail.setDisable(true);
    }

    @FXML
    private void submit(ActionEvent event) {

        Boolean bool = false;

        if (this.type.equals("suppliers") && (this.detail.getText().equals("") || this.file == null)) {
            bool = true;
        }

        if (!this.username.getText().isEmpty() && !this.password.getText().isEmpty() && !this.name.getText().isEmpty() && !this.type.isEmpty() && !bool) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ship Store - Manage Member Insert");
            alert.setHeaderText("Are you sure you want to insert ?");
            alert.setContentText(
                    "Username: " + this.username.getText()
                    + ", Name: " + this.name.getText()
                    + ", Type: " + this.type
            );

            ButtonType btnCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getDialogPane().getButtonTypes().add(btnCancel);

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/resources/images/warning.png"));

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                this.insert(this.username.getText(), this.password.getText(), this.name.getText(), this.type);

                this.btnSubmit.getScene().getWindow().hide();

            }

        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ship Store - Manage Member Insert Warning!");
            alert.setHeaderText("Please complete all information.");
            alert.setContentText(
                    "Please enter the "
                    + (this.username.getText().isEmpty() ? "Username" : (this.password.getText().isEmpty() ? "Password" : (this.name.getText().isEmpty() ? "Name" : "Type")))
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

            File fin = new File("dist/data/users.dat");
            FileInputStream fis = new FileInputStream(fin);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            for (Object ln : br.lines().toArray()) {

                String[] n = ln.toString().split(",");
                id = Integer.parseInt(n[0]);

            }

            id++;

            File file = new File("dist/data/users.dat");
            FileWriter writer = new FileWriter(file, true);
            writer.write("\n" + id + "," + a + "," + Base64.getEncoder().encodeToString(b.getBytes()) + "," + c + "," + d);

            if (this.type.equals("suppliers")) {
                file = new File("dist/images/users/" + this.name.getText() + ".png");
                Files.copy(this.file.toPath(), file.toPath());

                file = new File("dist/data/suppliers_info.dat");
                writer = new FileWriter(file, true);
                writer.write("\n" + this.name.getText() + ':' + this.detail.getText());
            }
            writer.close();

            this.dataList.add(new Member(id, a, Base64.getEncoder().encodeToString(b.getBytes()), c, d));

        } catch (FileNotFoundException ex) {

            Logger.getLogger(ManageMemberInsertController.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {

            Logger.getLogger(ManageMemberInsertController.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    void setDataList(ObservableList<Member> dataListMember) {
        this.dataList = dataListMember;
    }

}
