/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author bossn
 */
public class LoginController implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button btnLogin;
    @FXML
    private ProgressBar progress;
    
    private String name;
    private String type;

    //    @FXML
    //    private HBox alert;
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.progress.setVisible(false);
        this.progress.setProgress(0.0);
    }

    @FXML
    public void login(ActionEvent e) {

        this.progress.setVisible(true);
        this.progress.setProgress(-1.0);

        PauseTransition pt = new PauseTransition();
        pt.setDuration(Duration.seconds(2));
        pt.setOnFinished((event) -> {

            this.progress.setVisible(false);
            this.progress.setProgress(0.0);

            if (this.Authentication(this.username.getText(), this.password.getText())) {

                this.btnLogin.getScene().getWindow().hide();

                try {

                    Stage main = new Stage();
                    FXMLLoader fxml = new FXMLLoader(getClass().getResource("/resources/view/Main.fxml"));
                    Scene scene = new Scene(fxml.load());
                    main.setScene(scene);
                    main.getIcons().add(new Image("/resources/images/icon.png"));
                    main.setTitle("Ship Store - Dashboard");
                    main.setResizable(false);
                    main.show();
                
                    MainController controller = (MainController) fxml.getController();
                    controller.setName(this.name);
                    controller.setType(this.type);
                
                } catch (IOException ex) {

                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);

                }

            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Fail !");
                alert.setHeaderText(null);
                alert.setContentText("The username or password is incorrect.\nPlease fill out the information carefully.");

                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/resources/images/error.png"));

                alert.show();

            }
        });

        pt.play();

    }

    public boolean Authentication(String username, String password) {

        Boolean bool = false;

        try {

            File fin = new File("dist/data/users.dat");
            FileInputStream fis = new FileInputStream(fin);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            for (Object line : br.lines().toArray()) {

                String[] row = line.toString().split(",");

                if (row[1].equals(username)) {

                    if (Base64.getEncoder().encodeToString(password.getBytes()).equals(row[2])) {

                        this.name = row[3];
                        this.type = row[4];
                        bool = true;
                        break;

                    }

                    break;

                }
            }

            br.close();
            fis.close();

        } catch (FileNotFoundException ex) {

            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {

            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);

        }

        return bool;

    }

}
