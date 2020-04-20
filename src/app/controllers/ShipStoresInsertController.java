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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author bossn
 */
public class ShipStoresInsertController implements Initializable {

    @FXML
    private TextField search;
    @FXML
    private TableView<Suppliers> tableSuppliers;
    @FXML
    private TableColumn<Suppliers, String> columnType;
    @FXML
    private TableColumn<Suppliers, String> columnName;
    @FXML
    private TableColumn<Suppliers, String> columnQty;
    @FXML
    private TableView<Suppliers> tableRequest;
    @FXML
    private TableColumn<Suppliers, String> columnTypeRequest;
    @FXML
    private TableColumn<Suppliers, String> columnNameRequest;
    @FXML
    private TableColumn<Suppliers, String> columnQtyRequest;
    @FXML
    private Button btnSubmit;
    @FXML
    private Button btnCancel;

    private final ObservableList<Suppliers> dataListSuppliers = FXCollections.observableArrayList();
    private ObservableList<ShipStores> dataListShipStore = FXCollections.observableArrayList();
    private String name;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.tableSuppliers.setRowFactory(e -> {
            TableRow<Suppliers> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Suppliers suppliers = row.getItem();

                    Boolean bool = true;
                    for (Suppliers obj : this.dataListSuppliers) {

                        if (obj.getId() == suppliers.getId()) {
                            bool = false;
                            break;
                        }
                    }
                    if (bool) {
                        suppliers.setQty("1");
                        this.dataListSuppliers.add(suppliers);
                    }
                }
            });
            return row;
        });

        this.tableRequest.setRowFactory(e -> {
            TableRow<Suppliers> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 3 && (!row.isEmpty())) {
                    this.dataListSuppliers.remove(row.getItem());
                }
            });
            return row;
        });

        this.columnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.columnQty.setCellValueFactory(new PropertyValueFactory<>("qty"));

        this.columnTypeRequest.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.columnNameRequest.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.columnQtyRequest.setCellValueFactory(new PropertyValueFactory<>("qty"));
        this.columnQtyRequest.setCellFactory(TextFieldTableCell.forTableColumn());
        this.columnQtyRequest.setOnEditCommit((param) -> {

            Suppliers suppliers = this.tableRequest.getSelectionModel().getSelectedItem();
            suppliers.setQty(param.getNewValue().toString());

        });

        try {

            ObservableList<Suppliers> dataList = FXCollections.observableArrayList();

            File fin = new File("dist/data/suppliers.dat");
            FileInputStream fis = new FileInputStream(fin);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            for (Object line : br.lines().toArray()) {

                String[] cols = line.toString().split(",");

                dataList.add(
                        new Suppliers(
                                Integer.parseInt(cols[0]),
                                cols[1], cols[2], cols[3], cols[4]
                        )
                );

            }

            br.close();
            fis.close();

            FilteredList<Suppliers> filteredData = new FilteredList<>(dataList, b -> true);
            this.search.textProperty().addListener(((observable, oldValue, newValue) -> {
                filteredData.setPredicate((suppiers) -> {

                    if (newValue == null || newValue.isEmpty()) {

                        return true;

                    }

                    String lowerCaseFilter = newValue.toLowerCase();
                    if (String.valueOf(suppiers.getId()).contains(lowerCaseFilter)) {

                        return true;

                    } else if (suppiers.getSource().toLowerCase().contains(lowerCaseFilter)) {

                        return true;

                    } else if (suppiers.getType().toLowerCase().contains(lowerCaseFilter)) {

                        return true;

                    } else {

                        return suppiers.getName().toLowerCase().contains(lowerCaseFilter);

                    }

                });
            }));

            SortedList<Suppliers> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(this.tableSuppliers.comparatorProperty());
            this.tableSuppliers.setItems(sortedData);
            this.tableRequest.setItems(this.dataListSuppliers);

        } catch (FileNotFoundException ex) {

            Logger.getLogger(SuppliersController.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {

            Logger.getLogger(SuppliersController.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    @FXML
    private void submit(ActionEvent event) {

        Boolean isEmpty = true;
        for (Suppliers s : this.dataListSuppliers) {
            isEmpty = false;
            break;
        }

        if (!isEmpty) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ship Store - Ship Stores Request");
            alert.setHeaderText("Are you sure you want to request ?");

            ButtonType btnCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getDialogPane().getButtonTypes().add(btnCancel);

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/resources/images/warning.png"));

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                this.insert();

                this.btnSubmit.getScene().getWindow().hide();

            }

        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ship Store - Ship Stores Request Warning!");
            alert.setHeaderText("Please select any things.");

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/resources/images/warning.png"));

            alert.show();

        }

    }

    @FXML
    private void cancel(ActionEvent event) {

        this.btnCancel.getScene().getWindow().hide();

    }

    void setDataList(ObservableList<ShipStores> dataList) {

        this.dataListShipStore = dataList;

    }

    private void insert() {

        int id = 0;
        try {

            File fin = new File("dist/data/ship_stores.dat");
            FileInputStream fis = new FileInputStream(fin);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            for (Object ln : br.lines().toArray()) {

                String[] n = ln.toString().split(";");
                id = Integer.parseInt(n[0]);

            }

            id++;

            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String date = dateTime.format(format);
            String detail = "";
            String status = "";
            for (Suppliers suppliers : this.dataListSuppliers) {
                detail += suppliers.getName() + ":" + suppliers.getQty() + ",";
                status += "requesting,";
            }
            detail = detail.substring(0, detail.length() - 1);
            status = status.substring(0, status.length() - 1);

            File file = new File("dist/data/ship_stores.dat");
            FileWriter writer = new FileWriter(file, true);
            writer.write("\n" + id + ";" + date + ";" + this.name + ";" + detail + ";" + status);
            writer.close();

            this.dataListShipStore.add(new ShipStores(id, date, this.name, detail.toString().replaceAll(",", "\n"), status.toString().replaceAll(",", "\n")));

        } catch (IOException ex) {

            Logger.getLogger(ShipStoresInsertController.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    public void setName(String name) {
        this.name = name;
    }

}
