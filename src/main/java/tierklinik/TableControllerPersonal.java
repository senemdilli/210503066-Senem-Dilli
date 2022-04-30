package tierklinik;

import Classes.Person;
import Classes.Personal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableControllerPersonal implements Initializable {
    @FXML
    private TableView<Personal> table;
    @FXML
    private TableColumn<Person,Integer> col_id;
    @FXML
    private TableColumn<Person,String> col_name;
    @FXML
    private TableColumn<Person,String> col_nachname;
    @FXML
    private TableColumn<Person,Integer> col_telnum;
    @FXML
    private TableColumn<Person,String> col_mail;
    @FXML
    private TableColumn<Person,String> col_adresse;
    @FXML
    private TableColumn<Personal,String> col_arbeit;
    @FXML
    private TableColumn<Personal,Double> col_gehalt;
    @FXML
    private TableColumn<Personal,Integer> col_personalnummer;

    String query = null;
    Connection connection = null;
    ResultSet resultSet = null;
    PreparedStatement preparedStatement;
    Personal personal = null;
    ObservableList<Personal> oblist = FXCollections.observableArrayList();

    @FXML
    private void getAddView() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/addPersonal.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void refreshTable() {
        try {
            oblist.clear();
            query = "SELECT * FROM personal, person WHERE person.id = personal.id";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                oblist.add(new Personal(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("nachname"), resultSet.getInt("telefonnummer"),
                        resultSet.getString("email"), resultSet.getString("adresse"),
                        resultSet.getString("arbeit"), resultSet.getInt("personalnummer"),
                        resultSet.getDouble("gehalt")));
                table.setItems(oblist);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*
        try {
            Connection con = FullDB.connect();
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM personal");

            while (rs.next()) {
                oblist.add(new Personal(rs.getString("name"), rs.getString("nachname"),
                        rs.getInt("telefonnummer"), rs.getString("email"),
                        rs.getString("adresse"), rs.getString("arbeit"),
                        rs.getInt("personalnummer"), rs.getDouble("gehalt")));
            }
        } catch (SQLException e) {
            Logger.getLogger(TableController.class.getName()).log(Level.SEVERE, null, e);
        }

        table.setItems(oblist); */
        loadDate();
    }

    @FXML
    private void loadDate() {
        try {
            connection = FullDB.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        refreshTable();

        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_nachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));
        col_telnum.setCellValueFactory(new PropertyValueFactory<>("telefonnummer"));
        col_mail.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        col_personalnummer.setCellValueFactory(new PropertyValueFactory<>("personalnummer"));
        col_arbeit.setCellValueFactory(new PropertyValueFactory<>("arbeit"));
        col_gehalt.setCellValueFactory(new PropertyValueFactory<>("gehalt"));


        Callback<TableColumn<Personal, String>, TableCell<Personal,String>> cellFoctory = (TableColumn<Personal,String> param) -> {
            final TableCell<Personal,String> cell = new TableCell<Personal,String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty) {
                        setGraphic(null);
                    } else {

                        ImageView deleteIcon = new ImageView("/person-remove.png");
                        ImageView addIcon = new ImageView("/person-add.png");
                        ImageView refreshIcon = new ImageView("/refresh.png");

                        deleteIcon.setStyle("-fx-cursor: hand;" +"-glyph-size:28px");
                        addIcon.setStyle("-fx-cursor: hand;" +"-glyph-size:28px");
                        refreshIcon.setStyle("-fx-cursor: hand;" +"-glyph-size:28px");

                        deleteIcon.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
                            @Override
                            public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                                try {
                                    personal = table.getSelectionModel().getSelectedItem();
                                    query = "DELETE * FROM 'personal' WHERE personalnummer = " + personal.getPersonalnummer();
                                    connection = FullDB.connect();
                                    preparedStatement = connection.prepareStatement(query);
                                    preparedStatement.execute();
                                    refreshTable();
                                } catch (SQLException e) {
                                    Logger.getLogger(TableController.class.getName()).log(Level.SEVERE, null, e);
                                }
                            }
                        });

                        addIcon.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
                            @Override
                            public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                                personal = table.getSelectionModel().getSelectedItem();
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getResource("/addPersonal.fxml"));
                                try {
                                    loader.load();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                AddPersonalController addPersonalController = loader.getController();
                                addPersonalController.setUpdate(true);
                                addPersonalController.setTextField(personal.getId(), personal.getName(), personal.getNachname(),
                                        personal.getTelefonnummer(), personal.getEmail(), personal.getAdresse(), personal.getArbeit(),
                                        personal.getPersonalnummer(), personal.getGehalt());
                                Parent parent =loader.getRoot();
                                Stage stage = new Stage();
                                stage.setScene(new Scene(parent));
                                stage.initStyle(StageStyle.UTILITY);
                                stage.show();
                            }
                        });

                        HBox managebtn = new HBox(refreshIcon, addIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment: center");
                        /* HBox.setMargin(deleteIcon, new Insets());
                        HBox.setMargin(addIcon,);
                        HBox.setMargin(refreshIcon,); */

                        setGraphic(managebtn);

                    }
                    setText(null);
                }
            };
            return cell;

        };
        table.setItems(oblist);

    }
}
