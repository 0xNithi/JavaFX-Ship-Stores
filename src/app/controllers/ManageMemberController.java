/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controllers;

import app.models.Member;
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
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
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
public class ManageMemberController implements Initializable {

    @FXML
    private TableView<Member> table;
    @FXML
    private TableColumn<Member, Integer> columnId;
    @FXML
    private TableColumn<Member, String> columnUsername;
    @FXML
    private TableColumn<Member, String> columnPassword;
    @FXML
    private TableColumn<Member, String> columnName;
    @FXML
    private TableColumn<Member, String> columnType;
    @FXML
    private TableColumn columnOptions;
    @FXML
    private TableColumn<Member, Member> columnUpdate;
    @FXML
    private TableColumn<Member, Member> columnDelete;

    private final ObservableList<Member> dataList = FXCollections.observableArrayList();
    private final Stage update = new Stage();

    private TextField search;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.table.getStylesheets().add("/resources/css/Member.css");

        this.columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.columnUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        this.columnPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        this.columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.columnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.columnUpdate.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        this.columnUpdate.setCellFactory(param -> new TableCell<Member, Member>() {

            private final Button btnUpdate;

            {
                this.btnUpdate = new Button("Update");
                this.btnUpdate.getStylesheets().add("/resources/css/Member.css");
                this.btnUpdate.getStyleClass().add("btn-update");
            }

            @Override
            protected void updateItem(Member member, boolean empty) {

                super.updateItem(member, empty);

                if (member == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(btnUpdate);
                btnUpdate.setOnAction(event -> {

                    if (!update.isShowing()) {

                        try {

                            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/resources/view/ManageMemberUpdate.fxml"));
                            Scene scene = new Scene(fxml.load());
                            update.setScene(scene);
                            update.getIcons().add(new Image("/resources/images/icon.png"));
                            update.setTitle("Ship Store - Suppliers Update");
                            update.setResizable(false);
                            update.show();

                            ManageMemberUpdatesController controller = (ManageMemberUpdatesController) fxml.getController();
                            controller.setDataList(dataList);
                            controller.setMember(member);

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
        this.columnDelete.setCellFactory(param -> new TableCell<Member, Member>() {

            private final Button btnDelete;

            {
                this.btnDelete = new Button("Delete");
                this.btnDelete.getStylesheets().add("/resources/css/Member.css");
                this.btnDelete.getStyleClass().add("btn-delete");
            }

            @Override
            protected void updateItem(Member member, boolean empty) {

                super.updateItem(member, empty);

                if (member == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(btnDelete);
                btnDelete.setOnAction(event -> {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Ship Store - Suppliers Delete");
                    alert.setHeaderText("Are you sure you want to delete ?");
                    alert.setContentText(
                            "Sourse: " + member.getUsername()
                            + ", Name: " + member.getName()
                            + ", Type: " + member.getType()
                    );

                    ButtonType btnCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                    alert.getDialogPane().getButtonTypes().add(btnCancel);

                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("/resources/images/warning.png"));

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {

                        dataList.remove(member);

                        try {

                            File fin = new File("dist/data/users.dat");
                            FileInputStream fis = new FileInputStream(fin);
                            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                            StringBuffer sb = new StringBuffer();

                            for (Object line : br.lines().toArray()) {

                                String[] cols = line.toString().split(",");

                                if (Integer.parseInt(cols[0]) != member.getId()) {

                                    sb.append(line.toString()).append('\n');

                                }
                            }

                            sb.deleteCharAt(sb.length() - 1);

                            FileOutputStream fileOut = new FileOutputStream("dist/data/users.dat");
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

        this.columnUpdate.setStyle("-fx-pref-height: 0; -fx-alignment: CENTER;");
        this.columnDelete.setStyle("-fx-pref-height: 0; -fx-alignment: CENTER;");

        try {

            File fin = new File("dist/data/users.dat");
            FileInputStream fis = new FileInputStream(fin);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            for (Object line : br.lines().toArray()) {

                String[] cols = line.toString().split(",");

                this.dataList.add(
                        new Member(
                                Integer.parseInt(cols[0]),
                                cols[1], cols[2], cols[3], cols[4]
                        )
                );

            }

            br.close();
            fis.close();

        } catch (FileNotFoundException ex) {

            Logger.getLogger(ManageMemberController.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {

            Logger.getLogger(ManageMemberController.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public void setSearch(TextField search) {

        this.search = search;

        FilteredList<Member> filteredData = new FilteredList<>(this.dataList, b -> true);
        this.search.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredData.setPredicate((suppiers) -> {

                if (newValue == null || newValue.isEmpty()) {

                    return true;

                }

                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(suppiers.getId()).contains(lowerCaseFilter)) {

                    return true;

                } else if (suppiers.getUsername().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                } else if (suppiers.getPassword().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                } else if (suppiers.getName().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                } else {

                    return suppiers.getType().toLowerCase().contains(lowerCaseFilter);

                }

            });
        }));

        SortedList<Member> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(this.table.comparatorProperty());
        this.table.setItems(sortedData);

    }

    public ObservableList<Member> getDataList() {

        return this.dataList;

    }

}
