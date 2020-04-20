/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controllers;

import app.Main;
import app.models.Member;
import app.models.ShipStores;
import app.models.Suppliers;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author bossn
 */
public class MainController implements Initializable {

    @FXML
    private Button btnSuppliers;
    @FXML
    private Button btnShipStores;
    @FXML
    private Button btnAdministration;
    @FXML
    private Button btnManageMember;
    @FXML
    private HBox optionsBar;
    @FXML
    private TextField search;
    @FXML
    private Button btnInsert;
    @FXML
    private MenuButton btnMenu;
    @FXML
    private AnchorPane holderPane;

    private final Stage insert = new Stage();

    private ObservableList<Suppliers> dataListSupliers;
    private ObservableList<ShipStores> dataListShipStores;
    private SuppliersController suppliersController;
    private ManageMemberController manageMemberController;
    
    private String name;
    private String type;
    private ObservableList<Member> dataListMember;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.createPage();

        this.optionsBar.setVisible(false);

    }

    public void setNode(Node node) {

        this.holderPane.getChildren().clear();
        this.holderPane.getChildren().add((Node) node);

    }

    public void createPage() {

        try {

            setNode((AnchorPane) FXMLLoader.load(getClass().getResource("/resources/view/Dashboard.fxml")));

        } catch (IOException ex) {

            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    @FXML
    public void dashboard(ActionEvent e) {

        try {

            Stage stage = (Stage) this.btnMenu.getScene().getWindow();
            stage.setTitle("Ship Store - Dashboard");

            this.setNode((AnchorPane) FXMLLoader.load(getClass().getResource("/resources/view/Dashboard.fxml")));

            this.optionsBar.setVisible(false);

        } catch (IOException ex) {

            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    @FXML
    public void suppliers(ActionEvent e) {

        try {

            Stage stage = (Stage) this.btnMenu.getScene().getWindow();
            stage.setTitle("Ship Store - Suppliers");

            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/resources/view/Suppliers.fxml"));
            this.setNode((AnchorPane) fxml.load());

            this.optionsBar.setVisible(true);

            this.suppliersController = (SuppliersController) fxml.getController();
            this.suppliersController.setSearch(this.search);
            this.suppliersController.setName(this.name);
            this.suppliersController.setType(this.type);
            
            this.dataListSupliers = this.suppliersController.getDataList();

            this.btnInsert.setVisible((this.type.equals("admin") || this.type.equals("suppliers") ? true : false));
            this.btnInsert.setText("Insert");
            this.btnInsert.setOnAction(this::suppliersInsert);

        } catch (IOException ex) {

            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    @FXML
    public void ship_stores(ActionEvent e) {

        try {

            Stage stage = (Stage) this.btnMenu.getScene().getWindow();
            stage.setTitle("Ship Store - Ship Stores");

            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/resources/view/ShipStores.fxml"));
            this.setNode((AnchorPane) fxml.load());

            this.optionsBar.setVisible(true);

            ShipStoresController shipStoresController = (ShipStoresController) fxml.getController();
            shipStoresController.setName(this.name);
            shipStoresController.setType(this.type);
            shipStoresController.setSearch(this.search);
            this.dataListShipStores = shipStoresController.getDataList();

            this.btnInsert.setVisible((this.type.equals("admin") || this.type.equals("ship") ? true : false));
            this.btnInsert.setText("Request");
            this.btnInsert.setOnAction(this::shipStoresRequest);

        } catch (IOException ex) {

            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    @FXML
    public void administration(ActionEvent e) {

        try {

            Stage stage = (Stage) this.btnMenu.getScene().getWindow();
            stage.setTitle("Ship Store - Administration");

            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/resources/view/Administration.fxml"));
            this.setNode((AnchorPane) fxml.load());

            this.optionsBar.setVisible(true);

            AdministrationController administrationController = (AdministrationController) fxml.getController();
            administrationController.setName(this.name);
            administrationController.setType(this.type);
            administrationController.setSearch(this.search);

            this.btnInsert.setVisible(false);

        } catch (IOException ex) {

            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);

        }
        
    }
    
    @FXML
    public void manage_member (ActionEvent e) {

        try {

            Stage stage = (Stage) this.btnMenu.getScene().getWindow();
            stage.setTitle("Ship Store - Manage Member");

            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/resources/view/ManageMember.fxml"));
            this.setNode((AnchorPane) fxml.load());

            this.optionsBar.setVisible(true);

            this.manageMemberController = (ManageMemberController) fxml.getController();
            this.manageMemberController.setSearch(this.search);
            
            this.dataListMember = this.manageMemberController.getDataList();

            this.btnInsert.setVisible(true);
            this.btnInsert.setText("Insert");
            this.btnInsert.setOnAction(this::memberInsert);

        } catch (IOException ex) {

            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);

        }

    }
    
    @FXML
    void about(ActionEvent event) {

        try {

            Stage stage = (Stage) this.btnMenu.getScene().getWindow();
            stage.setTitle("Ship Store - About");

            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/resources/view/About.fxml"));
            this.setNode((AnchorPane) fxml.load());

            this.optionsBar.setVisible(false);


        } catch (IOException ex) {

            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);

        }
        
    }

    @FXML
    public void logout(ActionEvent e) {

        try {

            this.btnMenu.getScene().getWindow().hide();

            Stage login = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/resources/view/Login.fxml"));
            Scene scene = new Scene(root);
            login.setScene(scene);
            login.getIcons().add(new Image("/resources/images/icon.png"));
            login.setTitle("Ship Store - Login");
            login.setResizable(false);
            login.show();

        } catch (IOException ex) {

            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    @FXML
    public void exit(ActionEvent e) {

        Platform.exit();

    }

    private void suppliersInsert(ActionEvent e) {

        if (!this.insert.isShowing()) {

            try {

                FXMLLoader fxml = new FXMLLoader(getClass().getResource("/resources/view/SuppliersInsert.fxml"));
                Scene scene = new Scene(fxml.load());
                this.insert.setScene(scene);
                this.insert.getIcons().add(new Image("/resources/images/icon.png"));
                this.insert.setTitle("Ship Store - Suppliers Insert");
                this.insert.setResizable(false);
                this.insert.show();

                SuppliersInsertController controller = (SuppliersInsertController) fxml.getController();
                controller.setDataList(this.dataListSupliers);
                controller.setSuppliersController(this.suppliersController);

            } catch (IOException ex) {

                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);

            }

        } else {

            this.insert.hide();

        }

    }

    private void shipStoresRequest(ActionEvent e) {

        if (!this.insert.isShowing()) {

            try {

                FXMLLoader fxml = new FXMLLoader(getClass().getResource("/resources/view/ShipStoresInsert.fxml"));
                Scene scene = new Scene(fxml.load());
                this.insert.setScene(scene);
                this.insert.getIcons().add(new Image("/resources/images/icon.png"));
                this.insert.setTitle("Ship Store - Ship Stores Request");
                this.insert.setResizable(false);
                this.insert.show();

                ShipStoresInsertController controller = (ShipStoresInsertController) fxml.getController();
                controller.setDataList(this.dataListShipStores);
                controller.setName(this.name);

            } catch (IOException ex) {

                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);

            }

        } else {

            this.insert.hide();

        }

    }
    
    private void memberInsert(ActionEvent e) {

        if (!this.insert.isShowing()) {

            try {

                FXMLLoader fxml = new FXMLLoader(getClass().getResource("/resources/view/ManageMemberInsert.fxml"));
                Scene scene = new Scene(fxml.load());
                this.insert.setScene(scene);
                this.insert.getIcons().add(new Image("/resources/images/icon.png"));
                this.insert.setTitle("Ship Store - Manage Member Insert");
                this.insert.setResizable(false);
                this.insert.show();

                ManageMemberInsertController controller = (ManageMemberInsertController) fxml.getController();
                controller.setDataList(this.dataListMember);

            } catch (IOException ex) {

                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);

            }

        } else {

            this.insert.hide();

        }

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;

        switch (this.type) {
            case "ship":
                this.btnAdministration.setDisable(true);
                this.btnManageMember.setVisible(false);
                break;
            case "suppliers":
                this.btnShipStores.setDisable(true);
                this.btnManageMember.setVisible(false);
                break;
        }
    }

}
