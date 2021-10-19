package JavaFX.Controller.Revue;

import JavaFX.Application;
import JavaFX.Controller.DAO.DAOHolder;
import dao.RevueDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import metier.Revue;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MenuGeneralRevueController implements Initializable, ChangeListener<Revue>
{
    RevueDAO revueDAO = (RevueDAO) DAOHolder.getInstance().getDaoFactory().getRevueDAO();

    @FXML
    private Button btnAjouter;

    @FXML
    private Button boutonRetour;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    @FXML
    private TableColumn<Revue, String> colDescription;

    @FXML
    private TableColumn<Revue, Integer> colId;

    @FXML
    private TableColumn<Revue, Integer> colIdPeriodicite;

    @FXML
    private TableColumn<Revue, Double> colTarifUnit;

    @FXML
    private TableColumn<Revue, String> colTitre;

    @FXML
    private TableView<Revue> tableViewRevue;

    @FXML
    void boutonRetourClick(ActionEvent event) throws IOException
    {
        //Charger la page que l'on veux afficher
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Vue/DAO/choixTable.fxml"));
        //Creer une Scene contenant cette page
        Scene scene = new Scene(fxmlLoader.load(), 600, 450);
        //Recuperer la Stage de l'ancienne page
        Stage stage = (Stage) tableViewRevue.getScene().getWindow();
        //Afficher la nouvelle Scene dans l'ancienne Stage
        stage.setScene(scene);
    }

    @FXML
    void btnAjouterClick(ActionEvent event) throws IOException
    {
        //Charger la page que l'on veux afficher
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Vue/Revue/creerRevue.fxml"));
        //Creer une Scene contenant cette page
        Scene scene = new Scene(fxmlLoader.load(), 600, 450);
        //Recuperer la Stage de l'ancienne page
        Stage stage = (Stage) tableViewRevue.getScene().getWindow();
        //Afficher la nouvelle Scene dans l'ancienne Stage
        stage.setScene(scene);
    }

    @FXML
    void btnModifierClick(ActionEvent event) throws IOException
    {
        Revue revueAModifier = this.tableViewRevue.getSelectionModel().getSelectedItem();
        sendData(revueAModifier);

        //Charger la page que l'on veux afficher
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Vue/Revue/modifierRevue.fxml"));
        //Creer une Scene contenant cette page
        Scene scene = new Scene(fxmlLoader.load(), 600, 450);
        //Recuperer la Stage de l'ancienne page
        Stage stage = (Stage) tableViewRevue.getScene().getWindow();
        //Afficher la nouvelle Scene dans l'ancienne Stage
        stage.setScene(scene);
    }

    @FXML
    void btnSupprimerClick(ActionEvent event) throws SQLException
    {
        Revue revueASupprimer = this.tableViewRevue.getSelectionModel().getSelectedItem();
        revueDAO.delete(revueASupprimer);

        //Reset la liste
        genererListeRevue();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        //Definir les propriétés des differentes colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colTarifUnit.setCellValueFactory(new PropertyValueFactory<>("tarifNumero"));
        colIdPeriodicite.setCellValueFactory(new PropertyValueFactory<>("idPeriodicite"));

        genererListeRevue();

        //Appliquer le listener sur la selection et desactiver les boutons
        this.tableViewRevue.getSelectionModel().selectedItemProperty().addListener(this);
        this.btnSupprimer.setVisible(false);
        this.btnModifier.setVisible(false);
    }

    @Override
    public void changed(ObservableValue<? extends Revue> observable, Revue oldValue, Revue newValue)
    {
        /*
        Si l'item selectionné n'est pas nul, ca veux dire qu'une ligne est select
         */
        this.btnSupprimer.setVisible(!(newValue == null));
        this.btnModifier.setVisible(!(newValue == null));
    }

    public void genererListeRevue()
    {
        //Permet de supprimer tout les elements afficher dans le tableau
        //Pour ensuite re importer uniquement ceux dans la base
        this.tableViewRevue.getItems().clear();
        try
        {
            this.tableViewRevue.getItems().addAll(revueDAO.findAll());
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private void sendData(Revue revue)
    {
        RevueHolder revueHolder = RevueHolder.getInstance();
        revueHolder.setRevue(revue);
    }
}

