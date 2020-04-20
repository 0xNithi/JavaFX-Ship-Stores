/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controllers;

import app.models.ShipStores;
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
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author bossn
 */
public class ShipStoresSendController implements Initializable {

    @FXML
    private TableView<Suppliers> tableRequest;
    @FXML
    private TableColumn<Suppliers, String> columnName;
    @FXML
    private TableColumn<Suppliers, String> columnQty;
    @FXML
    private TableView<Suppliers> tableSend;
    @FXML
    private TableColumn<Suppliers, String> columnSouce;
    @FXML
    private TableColumn<Suppliers, Suppliers> columnOption;
    @FXML
    private Button btnClose;
    private ShipStores shipStores;
    private ObservableList<ShipStores> dataList;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.tableRequest.setRowFactory(e -> {
            TableRow<Suppliers> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {

                    try {

                        File fin = new File("dist/data/suppliers.dat");
                        FileInputStream fis = new FileInputStream(fin);
                        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

                        ObservableList<Suppliers> datalist = FXCollections.observableArrayList();

                        for (Object line : br.lines().toArray()) {

                            String[] cols = line.toString().split(",");

                            if (row.getItem().getName().equals(cols[3])) {
                                datalist.add(
                                        new Suppliers(
                                                Integer.parseInt(cols[0]),
                                                cols[1], cols[2], cols[3], row.getItem().getQty()
                                        )
                                );
                            }

                        }
                        this.tableSend.getItems().clear();
                        this.tableSend.setItems(datalist);

                    } catch (FileNotFoundException ex) {

                        Logger.getLogger(ShipStoresSendController.class.getName()).log(Level.SEVERE, null, ex);

                    }

                }
            });
            return row;
        });

        this.columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.columnQty.setCellValueFactory(new PropertyValueFactory<>("qty"));

        this.columnSouce.setCellValueFactory(new PropertyValueFactory<>("source"));
        this.columnOption.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        this.columnOption.setCellFactory(param -> new TableCell<Suppliers, Suppliers>() {

            private final Button btnSend;

            {
                this.btnSend = new Button("Send");
                this.btnSend.getStylesheets().add("/resources/css/ShipStores.css");
                this.btnSend.getStyleClass().add("btn-send");
            }

            @Override
            protected void updateItem(Suppliers suppliers, boolean empty) {

                super.updateItem(suppliers, empty);

                if (suppliers == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(btnSend);
                btnSend.setOnAction(event -> {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Ship Store - Ship Stores Send");
                    alert.setHeaderText("Are you sure you want to send ?");

                    ButtonType btnCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                    alert.getDialogPane().getButtonTypes().add(btnCancel);

                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("/resources/images/warning.png"));

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {

                        try {

                            File fin = new File("dist/data/administration.dat");
                            FileInputStream fis = new FileInputStream(fin);
                            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                            StringBuffer sb = new StringBuffer();

                            Boolean isSend = false;
                            for (Object line : br.lines().toArray()) {

                                String[] cols = line.toString().split(",");

                                if (shipStores.getId().equals(Integer.parseInt(cols[0])) && suppliers.getSource().equals(cols[1]) && suppliers.getName().equals(cols[2])) {
                                    isSend = true;
                                }
                                sb.append(line.toString()).append('\n');
                            }

                            if (!isSend) {
                                sb.append(shipStores.getId()).append(',').append(suppliers.getSource()).append(',').append(suppliers.getName()).append(',').append(suppliers.getQty()).append(',').append("waiting").append('\n');

                                int i = 0;
                                int j = 0;
                                for (Object lines : shipStores.getDetail().lines().toArray()) {
                                    String[] cols = lines.toString().split(":");
                                    if (suppliers.getName().equals(cols[0])) {
                                        break;
                                    }
                                    i++;
                                }
                                String status = "";
                                for (Object lines : shipStores.getStatus().lines().toArray()) {
                                    String cols = lines.toString();
                                    if (i == j) {
                                        status += "waiting\n";
                                    } else {
                                        status += cols + '\n';
                                    }
                                    j++;
                                }
                                status.replace(status, status.substring(0, status.length() - 1));
                                shipStores.setStatus(status);
                                for (i = 0; i < dataList.size(); i++) {
                                    if (shipStores.getId().equals(dataList.get(i).getId())) {
                                        dataList.set(i, shipStores);
                                    }
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

                                    String str = row[0] + ';' + row[1] + ';' + row[2] + ';' + row[3] + ';' + shipStores.getStatus().replaceAll("\n", ",").substring(0, shipStores.getStatus().length() - 1) + '\n';
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
        this.columnOption.setStyle("-fx-alignment: CENTER;");
    }

    @FXML
    private void close(ActionEvent event) {

        this.btnClose.getScene().getWindow().hide();

    }

    void setShipStores(ShipStores shipStores) {
        this.shipStores = shipStores;

        ObservableList<Suppliers> datalist = FXCollections.observableArrayList();

        this.shipStores.getDetail().lines().forEach((t) -> {
            String[] cols = t.split(":");

            datalist.add(new Suppliers(0, null, null, cols[0], cols[1]));
        });

        this.tableRequest.setItems(datalist);
    }

    void setDataList(ObservableList<ShipStores> dataList) {

        this.dataList = dataList;

    }

}
