package RealTimeVerification;

import DrugTestInformation.DrugTestFXMLController;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

public class VerificationFXMLController implements Initializable {
    
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private Date date; 

    @FXML private Button btn_close;
    @FXML private FontAwesomeIconView close;
    @FXML private Button btn_minimize;
    @FXML private FontAwesomeIconView minimize;
    @FXML private JFXTextField fld_unique;
    @FXML private JFXTextField fld_verif_fname;
    @FXML private JFXTextField fld_verif_mname;
    @FXML private JFXTextField fld_verif_lname;
    @FXML private JFXTextField fld_verif_body_type;
    @FXML private JFXTextField fld_verif_classification;
    @FXML private JFXTextField fld_verif_color;
    @FXML private JFXTextField fld_verif_chassis;
    @FXML private JFXTextField fld_verif_engine;
    @FXML private Button btn_verif_submit;
    @FXML private CheckBox box_verified;
    @FXML private CheckBox box_notVerif;
    @FXML private JFXTextField fld_verif_make;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ButtonDesign();
        AutoFill();
    }    
    
    private Connection getDBConnection(){
        try {            
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hackathon2019","root","");
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Error in DBConnection()\n" + ex);
        }
        return con;
    }
    
    private void ButtonDesign(){
        btn_minimize.setOnMouseEntered(e -> { btn_minimize.setStyle("-fx-background-color: #203040;");  minimize.setFill(Color.web("#ffffff"));}); 
        btn_minimize.setOnMouseClicked(e -> {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setIconified(true);
            btn_minimize.setStyle("-fx-background-color: #203040 ;"); 
            minimize.setFill(Color.web("#ffffff"));
                });
        btn_minimize.setOnMouseExited(e -> { btn_minimize.setStyle("-fx-background-color: transparent;"); minimize.setFill(Color.web("#1A1A1D")); });
        btn_minimize.setOnMousePressed(e -> { btn_minimize.setStyle("-fx-background-color: transparent;"); minimize.setFill(Color.web("#1A1A1D")); });
        
        btn_close.setOnMouseEntered(e -> { btn_close.setStyle("-fx-background-color: #fe4365;");  close.setFill(Color.web("#1A1A1D"));}); 
        btn_close.setOnMouseClicked(e -> {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.close();
            btn_close.setStyle("-fx-background-color: #fe4365;"); 
            close.setFill(Color.web("#1A1A1D"));
                });
        btn_close.setOnMouseExited(e -> { btn_close.setStyle("-fx-background-color: transparent;"); close.setFill(Color.web("#fe4365")); });
        btn_close.setOnMousePressed(e -> { btn_close.setStyle("-fx-background-color: transparent;");  close.setFill(Color.web("#fe4365")); });
    }
    
    private void ClearFields(){
        fld_verif_fname.clear();
        fld_verif_mname.clear();
        fld_verif_lname.clear();
        fld_verif_body_type.clear();
        fld_verif_make.clear();
        fld_verif_classification.clear();
        fld_verif_color.clear();
        fld_verif_chassis.clear();
        fld_verif_engine.clear();
    }
    
    private void AutoFill(){
        try {
            List<String> getNum = new ArrayList<>();
            con = getDBConnection();
            pst = con.prepareStatement("SELECT unique_id FROM `vehicle_verif`");
            rs = pst.executeQuery();
            int i=0;
            while(rs.next()){
                i++;
                getNum.add(rs.getString(1));
            }
            TextFields.bindAutoCompletion(fld_unique, getNum);
            con.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    @FXML private void fld_unique_onKeyReleased(KeyEvent event) {
        try {
            con = getDBConnection();
            pst = con.prepareStatement("SELECT * FROM `vehicle_verif` where unique_id = ?");
            pst.setString(1, fld_unique.getText());
            rs = pst.executeQuery();
            if(rs.next()){
                fld_verif_fname.setText(rs.getString(2));
                fld_verif_mname.setText(rs.getString(3));
                fld_verif_lname.setText(rs.getString(4));
                fld_verif_body_type.setText(rs.getString(5));
                fld_verif_make.setText(rs.getString(6));
                fld_verif_classification.setText(rs.getString(7));
                fld_verif_color.setText(rs.getString(8));
                fld_verif_chassis.setText(rs.getString(9));
                fld_verif_engine.setText(rs.getString(10));
            }
            else {
                ClearFields();
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DrugTestFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } // Key Typed

    @FXML private void btn_verif_submit_setOnAction(ActionEvent event) {
         if(fld_unique.getText().length() > 0){
             if(!box_verified.isSelected() && !box_notVerif.isSelected()){
                 Verification.show("Message", "Please choose verification", Alert.AlertType.INFORMATION);
             }
             else{
                Verification.show("Message", "Transaction successfully forwarded!", Alert.AlertType.INFORMATION);
                fld_unique.clear();
                fld_verif_fname.clear();
                fld_verif_mname.clear();
                fld_verif_lname.clear();
                fld_verif_body_type.clear();
                fld_verif_make.clear();
                fld_verif_classification.clear();
                fld_verif_color.clear();
                fld_verif_chassis.clear();
                fld_verif_engine.clear();
                Verification.Notif("Highway Patrol Group Information System", "You have another transaction.");
                box_verified.setSelected(false);
                box_notVerif.setSelected(false);
             }
         }
         else {
            Verification.show("Message", "No input UID!", Alert.AlertType.INFORMATION);
         }
    }
    
}
