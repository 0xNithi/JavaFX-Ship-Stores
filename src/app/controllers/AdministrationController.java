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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
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
public class AdministrationController implements Initializable {

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
    private TableColumn<ShipStores, ShipStores> columnAccept;
    @FXML
    private TableColumn<ShipStores, ShipStores> columnDeny;

    private final ObservableList<ShipStores> dataList = FXCollections.observableArrayList();

    private String name;
    private String type;
    private TextField search;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.table.getStylesheets().add("/resources/css/ShipStores.css");

        this.columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.columnShip.setCellValueFactory(new PropertyValueFactory<>("ship"));
        this.columnDetail.setCellValueFactory(new PropertyValueFactory<>("detail"));
        this.columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        this.columnAccept.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        this.columnAccept.setCellFactory(param -> new TableCell<ShipStores, ShipStores>() {

            private final Button btnSend;

            {
                this.btnSend = new Button("Accept");
                this.btnSend.getStylesheets().add("/resources/css/Administration.css");
                this.btnSend.getStyleClass().add("btn-accept");
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

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Ship Store - Administration Accept");
                    alert.setHeaderText("Are you sure you want to accept ?");

                    ButtonType btnCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                    alert.getDialogPane().getButtonTypes().add(btnCancel);

                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("/resources/images/warning.png"));

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {

                        shipStores.setStatus("Accept");
                        dataList.set(dataList.indexOf(shipStores), shipStores);

                        try {

                            File fin = new File("dist/data/administration.dat");
                            FileInputStream fis = new FileInputStream(fin);
                            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                            StringBuffer sb = new StringBuffer();

                            for (Object line : br.lines().toArray()) {

                                String[] cols = line.toString().split(",");

                                BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(new File("dist/data/ship_stores.dat"))));

                                String date = "";
                                String ship = "";
                                for (Object lines : br2.lines().toArray()) {
                                    String[] col = lines.toString().split(";");
                                    if (cols[0].equals(col[0])) {
                                        date = col[1];
                                        ship = col[2];
                                    }
                                }
                                if (Integer.parseInt(cols[0]) == shipStores.getId() && shipStores.getDate().equals(date) && shipStores.getShip().equals(ship) && shipStores.getDetail().equals(cols[2] + ':' + cols[3])) {

                                    sb.append(cols[0] + ',' + cols[1] + ',' + cols[2] + ',' + cols[3] + ',' + "Accept").append('\n');

                                } else {

                                    sb.append(line.toString()).append('\n');

                                }
                            }
                            sb.deleteCharAt(sb.length() - 1);

                            FileOutputStream fileOut = new FileOutputStream("dist/data/administration.dat");
                            fileOut.write(sb.toString().getBytes());

                            fin = new File("dist/data/ship_stores.dat");
                            fis = new FileInputStream(fin);
                            br = new BufferedReader(new InputStreamReader(fis));
                            StringBuffer inputBuffer = new StringBuffer();
                            for (Object line : br.lines().toArray()) {

                                String[] row = line.toString().split(";");
                                if (shipStores.getId().equals(Integer.parseInt(row[0]))) {

                                    String[] detail = row[3].split(",");
                                    String[] status = row[4].split(",");
                                    String statusAll = "";
                                    for (int i = 0; i < detail.length; i++) {
                                        if (shipStores.getDetail().equals(detail[i])) {
                                            status[i] = "Accept";
                                            break;
                                        }
                                    }
                                    for (int i = 0; i < status.length; i++) {
                                        statusAll += status[i];
                                        if (i < status.length - 1) {
                                            statusAll += ',';
                                        }
                                    }

                                    String str = row[0] + ';' + row[1] + ';' + row[2] + ';' + row[3] + ';' + statusAll + '\n';
                                    inputBuffer.append(str);

                                } else {

                                    inputBuffer.append(line.toString()).append('\n');

                                }
                            }
                            inputBuffer.deleteCharAt(inputBuffer.length() - 1);

                            fileOut = new FileOutputStream("dist/data/ship_stores.dat");
                            fileOut.write(inputBuffer.toString().getBytes());
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

        this.columnDeny.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        this.columnDeny.setCellFactory(param -> new TableCell<ShipStores, ShipStores>() {

            private final Button btnDeny;

            {
                this.btnDeny = new Button("Deny");
                this.btnDeny.getStylesheets().add("/resources/css/Administration.css");
                this.btnDeny.getStyleClass().add("btn-deny");
            }

            @Override
            protected void updateItem(ShipStores shipStores, boolean empty) {

                super.updateItem(shipStores, empty);

                if (shipStores == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(btnDeny);
                btnDeny.setOnAction(event -> {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Ship Store - Administration Deny");
                    alert.setHeaderText("Are you sure you want to deny ?");

                    ButtonType btnCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                    alert.getDialogPane().getButtonTypes().add(btnCancel);

                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("/resources/images/warning.png"));

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {

                        shipStores.setStatus("Deny");
                        dataList.set(dataList.indexOf(shipStores), shipStores);

                        try {

                            File fin = new File("dist/data/administration.dat");
                            FileInputStream fis = new FileInputStream(fin);
                            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                            StringBuffer sb = new StringBuffer();

                            for (Object line : br.lines().toArray()) {

                                String[] cols = line.toString().split(",");

                                BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(new File("dist/data/ship_stores.dat"))));

                                String date = "";
                                String ship = "";
                                for (Object lines : br2.lines().toArray()) {
                                    String[] col = lines.toString().split(";");
                                    if (cols[0].equals(col[0])) {
                                        date = col[1];
                                        ship = col[2];
                                    }
                                }
                                if (Integer.parseInt(cols[0]) == shipStores.getId() && shipStores.getDate().equals(date) && shipStores.getShip().equals(ship) && shipStores.getDetail().equals(cols[2] + ':' + cols[3])) {

                                    sb.append(cols[0] + ',' + cols[1] + ',' + cols[2] + ',' + cols[3] + ',' + "Deny").append('\n');

                                } else {

                                    sb.append(line.toString()).append('\n');

                                }
                            }
                            sb.deleteCharAt(sb.length() - 1);

                            FileOutputStream fileOut = new FileOutputStream("dist/data/administration.dat");
                            fileOut.write(sb.toString().getBytes());

                            fin = new File("dist/data/ship_stores.dat");
                            fis = new FileInputStream(fin);
                            br = new BufferedReader(new InputStreamReader(fis));
                            StringBuffer inputBuffer = new StringBuffer();
                            for (Object line : br.lines().toArray()) {

                                String[] row = line.toString().split(";");
                                if (shipStores.getId().equals(Integer.parseInt(row[0]))) {

                                    String[] detail = row[3].split(",");
                                    String[] status = row[4].split(",");
                                    String statusAll = "";
                                    for (int i = 0; i < detail.length; i++) {
                                        if (shipStores.getDetail().equals(detail[i])) {
                                            status[i] = "Deny";
                                            break;
                                        }
                                    }
                                    for (int i = 0; i < status.length; i++) {
                                        statusAll += status[i];
                                        if (i < status.length - 1) {
                                            statusAll += ',';
                                        }
                                    }

                                    String str = row[0] + ';' + row[1] + ';' + row[2] + ';' + row[3] + ';' + statusAll + '\n';
                                    inputBuffer.append(str);

                                } else {

                                    inputBuffer.append(line.toString()).append('\n');

                                }
                            }
                            inputBuffer.deleteCharAt(inputBuffer.length() - 1);

                            fileOut = new FileOutputStream("dist/data/ship_stores.dat");
                            fileOut.write(inputBuffer.toString().getBytes());
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

        this.columnAccept.setStyle("-fx-pref-height: 0; -fx-alignment: CENTER;");
        this.columnDeny.setStyle("-fx-pref-height: 0; -fx-alignment: CENTER;");

        this.table.getSortOrder().add(this.columnDate);

    }

    void setName(String name) {
        this.name = name;
    }

    void setType(String type) {
        this.type = type;
    }

    void setSearch(TextField search) {
        this.search = search;
        try {

            File fin = new File("dist/data/administration.dat");
            FileInputStream fis = new FileInputStream(fin);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            for (Object line : br.lines().toArray()) {

                String[] cols = line.toString().split(",");

                if (this.type.equals("admin")) {

                    BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(new File("dist/data/ship_stores.dat"))));

                    String date = "";
                    String ship = "";
                    for (Object lines : br2.lines().toArray()) {
                        String[] col = lines.toString().split(";");
                        if (cols[0].equals(col[0])) {
                            date = col[1];
                            ship = col[2];
                        }
                    }

                    this.dataList.add(
                            new ShipStores(
                                    Integer.parseInt(cols[0]),
                                    date, ship, cols[2] + ':' + cols[3], cols[4]
                            )
                    );
                } else if (this.name.equals(cols[1])) {

                    BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(new File("dist/data/ship_stores.dat"))));

                    String date = "";
                    String ship = "";
                    for (Object lines : br2.lines().toArray()) {
                        String[] col = lines.toString().split(";");
                        if (cols[0].equals(col[0])) {
                            date = col[1];
                            ship = col[2];
                        }
                    }

                    this.dataList.add(
                            new ShipStores(
                                    Integer.parseInt(cols[0]),
                                    date, ship, cols[2] + ':' + cols[3], cols[4]
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

}
