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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author bossn
 */
public class SuppliersController implements Initializable {

    @FXML
    private PieChart pieChart;
    @FXML
    private BarChart<String, Integer> barChart;
    @FXML
    private TableView<Suppliers> table;
    @FXML
    private TableColumn<Suppliers, Integer> columnId;
    @FXML
    private TableColumn<Suppliers, String> columnSource;
    @FXML
    private TableColumn<Suppliers, String> columnType;
    @FXML
    private TableColumn<Suppliers, String> columnName;
    @FXML
    private TableColumn<Suppliers, Integer> columnQty;
    @FXML
    private TableColumn columnOptions;
    @FXML
    private TableColumn<Suppliers, Suppliers> columnUpdate;
    @FXML
    private TableColumn<Suppliers, Suppliers> columnDelete;
    private TextField search;

    private final ObservableList<Suppliers> dataList = FXCollections.observableArrayList();
    private final Map<String, Integer> pieData = new HashMap<String, Integer>();
    private final Map<String, HashMap<String, Integer>> barData = new HashMap<String, HashMap<String, Integer>>();
    private final Stage info = new Stage();
    private final Stage update = new Stage();
    
    private XYChart.Series[] dataSeries;
    private String name;
    private String type;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.table.getStylesheets().add("/resources/css/Suppliers.css");

        this.table.setRowFactory(e -> {
            TableRow<Suppliers> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {

                    try {

                        Suppliers suppliers = row.getItem();

                        FXMLLoader fxml = new FXMLLoader(getClass().getResource("/resources/view/SuppliersInfo.fxml"));
                        Scene scene = new Scene(fxml.load());
                        this.info.setScene(scene);
                        this.info.getIcons().add(new Image("/resources/images/icon.png"));
                        this.info.setTitle("Ship Store - Suppliers Info");
                        this.info.setResizable(false);
                        this.info.show();

                        SuppliersInfoController controller = (SuppliersInfoController) fxml.getController();
                        controller.init(suppliers);

                    } catch (IOException ex) {

                        Logger.getLogger(SuppliersController.class.getName()).log(Level.SEVERE, null, ex);

                    }
                }
            });
            return row;
        });

        this.columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.columnSource.setCellValueFactory(new PropertyValueFactory<>("source"));
        this.columnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.columnQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        this.columnUpdate.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        this.columnUpdate.setCellFactory(param -> new TableCell<Suppliers, Suppliers>() {

            private final Button btnUpdate;

            {
                this.btnUpdate = new Button("Update");
                this.btnUpdate.getStylesheets().add("/resources/css/Suppliers.css");
                this.btnUpdate.getStyleClass().add("btn-update");
            }

            @Override
            protected void updateItem(Suppliers suppliers, boolean empty) {

                super.updateItem(suppliers, empty);

                if (suppliers == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(btnUpdate);
                btnUpdate.setOnAction(event -> {

                    if (!update.isShowing()) {

                        try {

                            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/resources/view/SuppliersUpdate.fxml"));
                            Scene scene = new Scene(fxml.load());
                            update.setScene(scene);
                            update.getIcons().add(new Image("/resources/images/icon.png"));
                            update.setTitle("Ship Store - Suppliers Update");
                            update.setResizable(false);
                            update.show();

                            SuppliersUpdateController controller = (SuppliersUpdateController) fxml.getController();
                            controller.setSuppliersController(SuppliersController.this);
                            controller.setDataList(dataList);
                            controller.setSuppliers(suppliers);

                        } catch (IOException ex) {
                            
                            Logger.getLogger(SuppliersController.class.getName()).log(Level.SEVERE, null, ex);
                            
                        }

                    } else {

                        update.hide();

                    }

                });

            }

        });
        this.columnDelete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        this.columnDelete.setCellFactory(param -> new TableCell<Suppliers, Suppliers>() {

            private final Button btnDelete;

            {
                this.btnDelete = new Button("Delete");
                this.btnDelete.getStylesheets().add("/resources/css/Suppliers.css");
                this.btnDelete.getStyleClass().add("btn-delete");
            }

            @Override
            protected void updateItem(Suppliers suppliers, boolean empty) {

                super.updateItem(suppliers, empty);

                if (suppliers == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(btnDelete);
                btnDelete.setOnAction(event -> {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Ship Store - Suppliers Delete");
                    alert.setHeaderText("Are you sure you want to delete ?");
                    alert.setContentText(
                            "Sourse: " + suppliers.getSource()
                            + ", Type: " + suppliers.getType()
                            + ", Name: " + suppliers.getName()
                    );

                    ButtonType btnCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
                    alert.getDialogPane().getButtonTypes().add(btnCancel);

                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("/resources/images/warning.png"));

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {

                        dataList.remove(suppliers);

                        try {

                            File fin = new File("dist/data/suppliers.dat");
                            FileInputStream fis = new FileInputStream(fin);
                            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                            StringBuffer sb = new StringBuffer();

                            for (Object line : br.lines().toArray()) {

                                String[] cols = line.toString().split(",");

                                if (Integer.parseInt(cols[0]) != suppliers.getId()) {

                                    sb.append(line.toString()).append('\n');

                                } else {

                                    ChartData(cols[1], cols[2], -Integer.parseInt(cols[4]));

                                }
                            }

                            sb.deleteCharAt(sb.length() - 1);

                            FileOutputStream fileOut = new FileOutputStream("dist/data/suppliers.dat");
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

        this.columnId.setReorderable(false);
        this.columnOptions.setReorderable(false);

        this.columnUpdate.setStyle("-fx-pref-height: 0; -fx-alignment: CENTER;");
        this.columnDelete.setStyle("-fx-pref-height: 0; -fx-alignment: CENTER;");

        try {

            File fin = new File("dist/data/suppliers.dat");
            FileInputStream fis = new FileInputStream(fin);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            for (Object line : br.lines().toArray()) {

                String[] cols = line.toString().split(",");

                if (this.pieData.get(cols[2]) != null) {

                    this.pieData.put(cols[2], this.pieData.get(cols[2]) + Integer.parseInt(cols[4]));

                } else {

                    this.pieData.put(cols[2], Integer.parseInt(cols[4]));

                }

                if (this.barData.get(cols[1]) != null) {
                    Map<String, Integer> map = this.barData.get(cols[1]);
                    if (map.get(cols[2]) != null) {

                        map.put(cols[2], Integer.parseInt(cols[4]) + map.get(cols[2]));
                        this.barData.put(cols[1], (HashMap<String, Integer>) map);

                    } else {

                        map.put(cols[2], Integer.parseInt(cols[4]));
                        this.barData.put(cols[1], (HashMap<String, Integer>) map);

                    }

                } else {

                    this.barData.put(cols[1], new HashMap<String, Integer>() {
                        {
                            put(cols[2], Integer.parseInt(cols[4]));
                        }
                    });

                }

                this.dataList.add(
                        new Suppliers(
                                Integer.parseInt(cols[0]),
                                cols[1], cols[2], cols[3],cols[4]
                        )
                );

            }

            br.close();
            fis.close();

            this.pieData.keySet().forEach((str) -> {

                this.pieChart.getData().add(new PieChart.Data(str, this.pieData.get(str)));

            });

            this.dataSeries = new XYChart.Series[this.barData.size()];
            for (int i = 0; i < this.barData.size(); i++) {

                ArrayList<Object> arr = new ArrayList<>(this.barData.keySet());
                Object obj = arr.get(i);

                this.dataSeries[i] = new XYChart.Series();
                this.dataSeries[i].setName((String) obj);

                Map<String, Integer> map = this.barData.get(obj);
                for (Map.Entry<String, Integer> entry : map.entrySet()) {

                    this.dataSeries[i].getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

                }

                this.barChart.getData().add(this.dataSeries[i]);

            }

        } catch (FileNotFoundException ex) {

            Logger.getLogger(SuppliersController.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {

            Logger.getLogger(SuppliersController.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    public void setSearch(TextField search) {

        this.search = search;

        FilteredList<Suppliers> filteredData = new FilteredList<>(this.dataList, b -> true);
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
        sortedData.comparatorProperty().bind(this.table.comparatorProperty());
        this.table.setItems(sortedData);

    }

    public ObservableList<Suppliers> getDataList() {

        return this.dataList;

    }

    public void ChartData(String source, String type, int qty) {

        this.pieChart.getData().clear();
        this.barChart.getData().clear();

        if (this.pieData.get(type) != null) {

            this.pieData.put(type, this.pieData.get(type) + qty);

            if (this.pieData.get(type) <= 0) {

                this.pieData.remove(type);

            }

        } else {

            this.pieData.put(type, qty);

        }

        this.pieData.keySet().forEach((str) -> {

            this.pieChart.getData().add(new PieChart.Data(str, this.pieData.get(str)));

        });

        if (this.barData.get(source) != null) {

            Map<String, Integer> map = this.barData.get(source);
            if (map.get(type) != null) {

                map.put(type, map.get(type) + qty);

                if (map.get(type) <= 0) {

                    map.remove(type);

                }

            } else {

                map.put(type, qty);

            }
            this.barData.put(source, (HashMap<String, Integer>) map);

        } else {

            this.barData.put(source, new HashMap<String, Integer>() {
                {
                    put(type, qty);
                }
            });

        }

        this.dataSeries = new XYChart.Series[this.barData.size()];
        for (int i = 0; i < this.barData.size(); i++) {

            ArrayList<Object> arr = new ArrayList<>(this.barData.keySet());
            Object obj = arr.get(i);

            this.dataSeries[i] = new XYChart.Series();
            this.dataSeries[i].setName((String) obj);

            Map<String, Integer> mapEntry = this.barData.get(obj);
            for (Map.Entry<String, Integer> entry : mapEntry.entrySet()) {

                this.dataSeries[i].getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

            }

            this.barChart.getData().add(this.dataSeries[i]);

        }

    }

    public void UpdateChartData(Suppliers before, Suppliers after) {

        this.pieChart.getData().clear();
        this.barChart.getData().clear();

        if (this.pieData.get(after.getType()) != null) {

            this.pieData.put(before.getType(), this.pieData.get(before.getType()) - Integer.parseInt(before.getQty()));
            this.pieData.put(after.getType(), this.pieData.get(after.getType()) + Integer.parseInt(after.getQty()));

        } else {

            this.pieData.put(before.getType(), this.pieData.get(before.getType()) - Integer.parseInt(before.getQty()));
            this.pieData.put(after.getType(), Integer.parseInt(after.getQty()));

        }

        if (this.pieData.get(before.getType()) <= 0) {

            this.pieData.remove(before.getType());

        }

        this.pieData.keySet().forEach((str) -> {

            this.pieChart.getData().add(new PieChart.Data(str, this.pieData.get(str)));

        });

        Map<String, Integer> mapBefore = this.barData.get(before.getSource());
        Map<String, Integer> mapAfter = this.barData.get(after.getSource());

        if (this.barData.get(after.getSource()) != null) {

            mapBefore.put(before.getType(), mapBefore.get(before.getType()) - Integer.parseInt(before.getQty()));
            if (mapAfter.get(after.getType()) != null) {

                mapAfter.put(after.getType(), mapAfter.get(after.getType()) + Integer.parseInt(after.getQty()));

            } else {

                mapAfter.put(after.getType(), Integer.parseInt(after.getQty()));

            }

        } else {

            mapBefore.put(before.getType(), mapBefore.get(before.getType()) - Integer.parseInt(before.getQty()));

            this.barData.put(after.getSource(), new HashMap<String, Integer>() {
                {
                    put(after.getType(), Integer.parseInt(after.getQty()));
                }

            });

        }

        if (mapBefore.get(before.getType()) <= 0) {

            mapBefore.remove(before.getType());

        }

        this.dataSeries = new XYChart.Series[this.barData.size()];
        for (int i = 0; i < this.barData.size(); i++) {

            ArrayList<Object> arr = new ArrayList<>(this.barData.keySet());
            Object obj = arr.get(i);

            this.dataSeries[i] = new XYChart.Series();
            this.dataSeries[i].setName((String) obj);

            for (Map.Entry<String, Integer> entry : this.barData.get(obj).entrySet()) {

                this.dataSeries[i].getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

            }

            this.barChart.getData().add(this.dataSeries[i]);

        }

    }

    void setName(String name) {
        this.name = name;
    }

    void setType(String type) {
        this.type = type;
        
        if (this.type.equals("ship")) {
            this.columnOptions.setVisible(false);
        }
    }
}
