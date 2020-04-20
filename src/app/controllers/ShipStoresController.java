/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controllers;

import app.models.ShipStores;
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
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author bossn
 */
public class ShipStoresController implements Initializable {

    @FXML
    private TableView<ShipStores> table;
    @FXML
    private TableColumn<ShipStores, String> columnDate;
    @FXML
    private TableColumn<ShipStores, String> columnShip;
    @FXML
    private TableColumn<ShipStores, String> columnDetail;
    @FXML
    private TableColumn<ShipStores, String> columnStatus;
    @FXML
    private TableColumn columnOptions;
    @FXML
    private TableColumn<ShipStores, ShipStores> columnSend;
    @FXML
    private TableColumn<ShipStores, ShipStores> columnDelete;

    private final ObservableList<ShipStores> dataList = FXCollections.observableArrayList();
    private final Stage send = new Stage();

    private TextField search;
    private String name;
    private String type;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.table.getStylesheets().add("/resources/css/ShipStores.css");

        this.columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.columnShip.setCellValueFactory(new PropertyValueFactory<>("ship"));
        this.columnDetail.setCellValueFactory(new PropertyValueFactory<>("detail"));
        this.columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        this.columnSend.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        this.columnSend.setCellFactory(param -> new TableCell<ShipStores, ShipStores>() {

            private final Button btnSend;

            {
                this.btnSend = new Button("Send");
                this.btnSend.getStylesheets().add("/resources/css/ShipStores.css");
                this.btnSend.getStyleClass().add("btn-send");
            }

            @Override
            protected void updateItem(ShipStores shipStores, boolean empty) {

                super.updateItem(shipStores, empty);

                if (shipStores == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(btnSend);
                btnSend.setOnAction(event -> {
                    
                    if (!send.isShowing()) {

                        try {

                            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/resources/view/ShipStoresSend.fxml"));
                            Scene scene = new Scene(fxml.load());
                            send.setScene(scene);
                            send.getIcons().add(new Image("/resources/images/icon.png"));
                            send.setTitle("Ship Store - Ship Store Send");
                            send.setResizable(false);
                            send.show();

                            ShipStoresSendController controller = (ShipStoresSendController) fxml.getController();
                            controller.setShipStores(shipStores);
                            controller.setDataList(dataList);

                        } catch (IOException ex) {
                            
                            Logger.getLogger(ShipStoresController.class.getName()).log(Level.SEVERE, null, ex);
                            
                        }

                    } else {

                        send.hide();

                    }
                    
                });

            }

        });
        
        this.columnDelete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        this.columnDelete.setCellFactory(param -> new TableCell<ShipStores, ShipStores>() {

            private final Button btnDelete;

            {
                this.btnDelete = new Button("Delete");
                this.btnDelete.getStylesheets().add("/resources/css/ShipStores.css");
                this.btnDelete.getStyleClass().add("btn-delete");
            }

            @Override
            protected void updateItem(ShipStores shipStores, boolean empty) {

                super.updateItem(shipStores, empty);

                if (shipStores == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(btnDelete);
                btnDelete.setOnAction(event -> {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Ship Store - Suppliers Delete");
                    alert.setHeaderText("Are you sure you want to delete ?");
                    alert.setContentText(
                            "Detail: " + shipStores.getDate()
                    );

                    ButtonType btnCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
                    alert.getDialogPane().getButtonTypes().add(btnCancel);

                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("/resources/images/warning.png"));

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {

                        dataList.remove(shipStores);

                        try {

                            File fin = new File("dist/data/ship_stores.dat");
                            FileInputStream fis = new FileInputStream(fin);
                            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                            StringBuffer sb = new StringBuffer();

                            for (Object line : br.lines().toArray()) {

                                String[] cols = line.toString().split(";");

                                if (Integer.parseInt(cols[0]) != shipStores.getId()) {

                                    sb.append(line.toString()).append('\n');

                                }
                            }

                            sb.deleteCharAt(sb.length() - 1);

                            FileOutputStream fileOut = new FileOutputStream("dist/data/ship_stores.dat");
                            fileOut.write(sb.toString().getBytes());
                            fileOut.close();

                        } catch (FileNotFoundException ex) {

                            Logger.getLogger(SuppliersController.class.getName()).log(Level.SEVERE, null, ex);

                        } catch (IOException ex) {

                            Logger.getLogger(SuppliersController.class.getName()).log(Level.SEVERE, null, ex);

                        }
                    }

                });

            }

        });

        this.columnDetail.setReorderable(false);
        this.columnOptions.setReorderable(false);

        this.columnSend.setStyle("-fx-pref-height: 0; -fx-alignment: CENTER;");
        this.columnDelete.setStyle("-fx-pref-height: 0; -fx-alignment: CENTER;");

        this.table.getSortOrder().add(this.columnDate);

    }

    public void setSearch(TextField search) {

        this.search = search;

        try {

            File fin = new File("dist/data/ship_stores.dat");
            FileInputStream fis = new FileInputStream(fin);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            for (Object line : br.lines().toArray()) {

                String[] cols = line.toString().split(";");

                if (this.type.equals("admin")) {
                    this.dataList.add(
                            new ShipStores(
                                    Integer.parseInt(cols[0]),
                                    cols[1], cols[2], cols[3].replaceAll(",", "\n"), cols[4].replaceAll(",", "\n")
                            )
                    );
                } else if (this.name.equals(cols[2])) {
                    this.dataList.add(
                            new ShipStores(
                                    Integer.parseInt(cols[0]),
                                    cols[1], cols[2], cols[3].replaceAll(",", "\n"), cols[4].replaceAll(",", "\n")
                            )
                    );
                }

            }

            br.close();
            fis.close();

        } catch (FileNotFoundException ex) {

            Logger.getLogger(SuppliersController.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {

            Logger.getLogger(SuppliersController.class.getName()).log(Level.SEVERE, null, ex);

        }

        FilteredList<ShipStores> filteredData = new FilteredList<>(this.dataList, b -> true);
        this.search.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredData.setPredicate((shipStore) -> {

                if (newValue == null || newValue.isEmpty()) {

                    return true;

                }

                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(shipStore.getDate()).contains(lowerCaseFilter)) {

                    return true;

                } else if (shipStore.getShip().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                } else if (shipStore.getDetail().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                } else {

                    return shipStore.getStatus().toLowerCase().contains(lowerCaseFilter);

                }

            });
        }));

        SortedList<ShipStores> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(this.table.comparatorProperty());
        this.table.setItems(sortedData);

    }

    public ObservableList<ShipStores> getDataList() {

        return this.dataList;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
        
        if (this.type.equals("ship")) {
            this.columnSend.setVisible(false);
        }
    }

}
