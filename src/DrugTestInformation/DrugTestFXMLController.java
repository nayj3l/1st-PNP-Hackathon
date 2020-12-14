package DrugTestInformation;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.controlsfx.control.table.TableFilter;
import org.controlsfx.control.textfield.TextFields;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class DrugTestFXMLController implements Initializable {
    
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private Date date;
    
    ToggleGroup toggleGroup = new ToggleGroup();
    
    TableFilter<Table_Data_Statistics> tableFilter;

    @FXML private Button btn_minimize;
    @FXML private Button btn_close;
    @FXML private FontAwesomeIconView minimize;
    @FXML private FontAwesomeIconView close;
    @FXML private JFXTextField fld_record_number;
    @FXML private JFXTextField fld_name;
    @FXML private JFXTextField fld_birthday;
    @FXML private JFXTextField fld_address;
    @FXML private JFXTextField fld_sex;
    @FXML private JFXTextField fld_unitname;
    @FXML private JFXTextField fld_purpose;
    @FXML private JFXRadioButton radio_confirm;
    @FXML private JFXRadioButton radio_clear;
    @FXML private JFXTextField fld_mobile_phone;
    @FXML private TableView<Table_Data_Statistics> tbl_data_stat;
    @FXML private TableColumn<Table_Data_Statistics, String> col_rec_num;
    @FXML private TableColumn<Table_Data_Statistics, String> col_unit_name;
    @FXML private TableColumn<Table_Data_Statistics, String> col_name;
    @FXML private TableColumn<Table_Data_Statistics, String> col_sex;
    @FXML private TableColumn<Table_Data_Statistics, String> col_transac_date;
    @FXML private TableColumn<Table_Data_Statistics, String> col_purpose;
    @FXML private TableColumn<Table_Data_Statistics, String> col_month;
    @FXML private TableColumn<Table_Data_Statistics, String> col_day;
    @FXML private TableColumn<Table_Data_Statistics, String> col_yr;
    @FXML private Label lbl_res;
    @FXML private Button btn_generate_report;
    @FXML private TextField fld_search_box;
    @FXML private JFXRadioButton radio_crim_confirm;
    @FXML private JFXRadioButton radio_crim_clear;
    @FXML private Button btn_generate_pdf;
    @FXML private TextField fld_crime_keyword;
    @FXML private Button btn_criminal_submit;
    @FXML private JFXTextField fld_crim_fname;
    @FXML private JFXTextField fld_crim_lname;
    @FXML private JFXTextField fld_crim_sex;
    @FXML private JFXTextField fld_crim_birthdate;
    @FXML private JFXTextField fld_crim_address;
    @FXML private Button btn_non_crim_submit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ButtonDesign();
        AutoFill();
        DisplayDataStatisticTable();
        FilterDataStatisticTable();
        SetRadioButtonFX();
        tableFilter = new TableFilter<>(tbl_data_stat); // set filter
        lbl_res.setText("Showing all result: " + Integer.toString(tbl_data_stat.getItems().size()));
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
    
    private void SetRadioButtonFX(){
        radio_crim_confirm.setUserData("For Confimation");
        radio_crim_confirm.setToggleGroup(toggleGroup);
        radio_crim_clear.setUserData("Clear");
        radio_crim_clear.setToggleGroup(toggleGroup);
        radio_confirm.setUserData("For Confimation");
        radio_confirm.setToggleGroup(toggleGroup);
        radio_clear.setUserData("Clear");
        radio_clear.setToggleGroup(toggleGroup);
    }
    
    private void GenerateExcel() {
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("DIGS Generated Report");
            dialog.setHeaderText(null);
            dialog.setContentText("Input File Name: ");
            Optional<String> result = dialog.showAndWait();
            if(result.isPresent()){
                TableView<Table_Data_Statistics> table = new TableView<>();

                ObservableList<Table_Data_Statistics> teamMembers = tbl_data_stat.getItems();
                table.setItems(teamMembers);

                TableColumn<Table_Data_Statistics,String> Record_Number = new TableColumn<>("Record Number");
                TableColumn<Table_Data_Statistics,String> Unit_Name = new TableColumn<>("Unit Name");
                TableColumn<Table_Data_Statistics,String> Purpose = new TableColumn<>("Purpose");
                TableColumn<Table_Data_Statistics,String> Name = new TableColumn<>("Name");
                TableColumn<Table_Data_Statistics,String> Sex = new TableColumn<>("Sex");
                TableColumn<Table_Data_Statistics,String> Transaction_Date = new TableColumn<>("Transaction Date");

                Record_Number.setCellValueFactory(new PropertyValueFactory<>("record_number"));
                Unit_Name.setCellValueFactory(new PropertyValueFactory<>("unit_name"));
                Purpose.setCellValueFactory(new PropertyValueFactory<>("purpose"));
                Name.setCellValueFactory(new PropertyValueFactory<>("full_name"));
                Sex.setCellValueFactory(new PropertyValueFactory<>("sex"));
                Transaction_Date.setCellValueFactory(new PropertyValueFactory<>("transaction_date"));

                ObservableList<TableColumn<Table_Data_Statistics, ?>> columns = table.getColumns();
                columns.add(Record_Number);
                columns.add(Unit_Name);
                columns.add(Purpose);
                columns.add(Name);
                columns.add(Sex);
                columns.add(Transaction_Date);

                Workbook workbook = new HSSFWorkbook();
                Sheet spreadsheet = workbook.createSheet("sample");
                Row row = spreadsheet.createRow(0);

                for (int j = 0; j < table.getColumns().size(); j++) {
                    row.createCell(j).setCellValue(table.getColumns().get(j).getText());
                } 

                for (int i = 0; i < table.getItems().size(); i++) {
                    row = spreadsheet.createRow(i + 1);
                    for (int j = 0; j < table.getColumns().size(); j++) {
                        if(table.getColumns().get(j).getCellData(i) != null) {
                            row.createCell(j).setCellValue(table.getColumns().get(j).getCellData(i).toString());
                        }
                        else {
                            row.createCell(j).setCellValue("");
                        }
                    }
                }

                FileOutputStream fileOut = new FileOutputStream(result.get()+".xls");
                workbook.write(fileOut);
                DrugTest.show("Successful", "Data Report: " +result.get()+ ".xls generated successfully!", Alert.AlertType.INFORMATION);
                fileOut.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(DrugTestFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void GeneratePDF(){
        Document document = new Document();
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("DIGS Generated PDF");
            dialog.setHeaderText(null);
            dialog.setContentText("Input File Name: ");
            Optional<String> result = dialog.showAndWait();
            if(result.isPresent()){
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream( result.get() + ".pdf"));
                document.open();

                Paragraph paragraphOne = new Paragraph("Republic of the Philippines\n" +
                "National Police Commission\n" +
                "PHILIPPINES NATIONAL POLICE\n" +
                "CAMP RAFAEL T. CRAME CRIME LABORATORY OFFICE\n" +
                "Cubao, Quezon City, Metro Manila\n " +
                "Tel. No. +63 02 721 8598 / pnpdo.adm1n@gmail.com");
                paragraphOne.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraphOne);

                Image image1 = Image.getInstance("C:\\Users\\Bebe Nayj\\Desktop\\temp\\pnp.png");
                //Fixed Positioning
                image1.setAbsolutePosition(30f, 690f); // x y
                //Scale to new height and new width of image
                image1.scaleAbsolute(95, 120); // w l
                //Add to document
                document.add(image1);

                Image image2 = Image.getInstance("C:\\Users\\Bebe Nayj\\Desktop\\temp\\pnp_crime_lab.jpg");
                image2.setAbsolutePosition(465f, 690f); // x y
                image2.scaleAbsolute(100, 120); // w l
                document.add(image2);

                Date date = new Date();
                SimpleDateFormat  sdf = new SimpleDateFormat ("MMMM dd, yyyy");
                paragraphOne = new Paragraph("\n\n" + sdf.format(date));
                paragraphOne.setAlignment(Element.ALIGN_RIGHT);
                document.add(paragraphOne);

                paragraphOne = new Paragraph("\nCHEMISTRY REPORT NUMBER: " );
                document.add(paragraphOne);

                paragraphOne = new Paragraph("\nINITIAL LABORATORY REPORT" );
                paragraphOne.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraphOne);

                paragraphOne = new Paragraph("\nCASE: " );
                document.add(paragraphOne);

                paragraphOne = new Paragraph("\nREQUESTING PARTY/UNIT:    " + fld_name.getText().toUpperCase()
                + "\n\t\t\t\t                                                   " + fld_unitname.getText());
                document.add(paragraphOne);

                sdf = new SimpleDateFormat ("hh:mm:ss a MMMM dd, yyyy");
                paragraphOne = new Paragraph("\nTIME AND DATE RECEIVED: " + sdf.format(date) );
                document.add(paragraphOne);

                paragraphOne = new Paragraph("\nSPECIMEN SUBMITTED: " );
                document.add(paragraphOne);

                paragraphOne = new Paragraph("\nPURPOSE: " + fld_purpose.getText());
                document.add(paragraphOne);

                paragraphOne = new Paragraph("\nFINDINGS: " );
                document.add(paragraphOne);

                paragraphOne = new Paragraph("\nREMARKS: " );
                document.add(paragraphOne);

                paragraphOne = new Paragraph("\n                       "
                        + "                          EXAMINED BY:" );
                paragraphOne.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraphOne);
                DrugTest.show("Successful", "PDF Generated Successfully!", Alert.AlertType.INFORMATION);
                document.close();
                writer.close();
                if (Desktop.isDesktopSupported()) {
                    try {
                        File myFile = new File("C:\\Users\\Bebe Nayj\\Documents\\NetBeansProjects\\1st PNP ITMS HACKATHON 2019\\"+result.get()+".pdf");
                        Desktop.getDesktop().open(myFile);
                    } catch (IOException ex) {
                        // no application registered for PDFs
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void DisplayDataStatisticTable(){
        try {
            ObservableList<Table_Data_Statistics> list = FXCollections.observableArrayList();
            con = getDBConnection();
            pst = con.prepareStatement("SELECT record_number, unit_name, purpose, fname, mname, lname, Month, Day, Year FROM users");
            rs = pst.executeQuery();
            while(rs.next()){
                list.add(new Table_Data_Statistics(rs.getString(1), rs.getString(2), rs.getString(3), 
                        rs.getString(4), rs.getString(5), rs.getString(6), 
                        rs.getString(7), rs.getString(8), rs.getString(9)));
            }
            col_rec_num.setCellValueFactory(new PropertyValueFactory<>("record_number"));
            col_unit_name.setCellValueFactory(new PropertyValueFactory<>("unit_name"));
            col_purpose.setCellValueFactory(new PropertyValueFactory<>("purpose"));
            
            col_name.setCellValueFactory(new PropertyValueFactory<>("full_name"));

            col_month.setCellValueFactory(new PropertyValueFactory<>("month"));
            col_day.setCellValueFactory(new PropertyValueFactory<>("day"));
            col_yr.setCellValueFactory(new PropertyValueFactory<>("year"));
            
            tbl_data_stat.setItems(list);
        } catch (SQLException ex) {
            Logger.getLogger(DrugTestFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void FilterDataStatisticTable(){
        ObservableList data =  tbl_data_stat.getItems();
        fld_search_box.textProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null && (newValue.length() < oldValue.length())) {
                tbl_data_stat.setItems(data);
            }
            String value = newValue.toLowerCase(); // yung iniinput sa search box
            ObservableList<Table_Data_Statistics> subentries = FXCollections.observableArrayList();
            long count = tbl_data_stat.getColumns().stream().count(); // number of columns
            for (int i = 0; i < tbl_data_stat.getItems().size(); i++) {
                for (int j = 0; j < count; j++) {
                    String entry = "" + tbl_data_stat.getColumns().get(j).getCellData(i);
                    if (entry.toLowerCase().contains(value)) {
                        subentries.add(tbl_data_stat.getItems().get(i));
                        break;
                    }
                }
            }
            tbl_data_stat.setItems(subentries);
            tableFilter = new TableFilter<>(tbl_data_stat); // set filter
        });
    }
    
    private void AutoFill(){
        String[] purpose = {"Promotion", "PNP Recruitment", "Internal Cleansing", 
                "UN Mission", "Schooling", "NUP Applicant", "Reassignment", "Restoration","Others"};
        TextFields.bindAutoCompletion(fld_purpose, purpose);
        try {
            List<String> getNum = new ArrayList<>();
            con = getDBConnection();
            pst = con.prepareStatement("SELECT OR_Number, F_name, L_name, contact_number FROM `criminal_records`");
            rs = pst.executeQuery();
            int i=0;
            while(rs.next()){
                i++;
                getNum.add(rs.getString(2) + " " + rs.getString(3));
            }
            TextFields.bindAutoCompletion(fld_crime_keyword, getNum);
            con.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
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
        fld_name.clear();
        fld_birthday.clear();
        fld_address.clear();
        fld_sex.clear();
        fld_mobile_phone.clear();
        fld_unitname.clear();
        fld_crim_fname.clear();
        fld_crim_lname.clear();
        fld_crim_sex.clear();
        fld_crim_birthdate.clear();
        fld_crim_address.clear();
        fld_name.clear();
        fld_sex.clear();
        fld_mobile_phone.clear();
        fld_birthday.clear();
        fld_address.clear();
        fld_unitname.clear();
        if (toggleGroup.getSelectedToggle() != null) {
            toggleGroup.getSelectedToggle().setSelected(false);
        }
    }

    @FXML private void fld_record_onKeyReleased(KeyEvent event) {
        try {
            con = getDBConnection();
            pst = con.prepareStatement("SELECT * FROM `reg_users` WHERE record_number = ?");
            pst.setString(1, fld_record_number.getText());
            rs = pst.executeQuery();
            if(rs.next()){
                fld_name.setText(rs.getString(2).trim());
                fld_birthday.setText(rs.getString(3));
                fld_address.setText(rs.getString(4));
                fld_sex.setText(rs.getString(5));
                fld_mobile_phone.setText(rs.getString(6));
                fld_unitname.setText(rs.getString(7));
            }
            else {
                ClearFields();
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DrugTestFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } // Keyboard typed update
    
    @FXML private void tbl_data_stat_setOnMouseEntered(MouseEvent event) {
        lbl_res.setText("Showing all result: " + Integer.toString(tbl_data_stat.getItems().size()));
    } // Updating results of table 

    @FXML private void btn_generate_report_setOnAction(ActionEvent event) throws IOException {
        GenerateExcel();
    } // Generate Excel here

    @FXML private void fld_search_box_setOnKeyReleased(KeyEvent event) {
        lbl_res.setText("Showing all result: " + Integer.toString(tbl_data_stat.getItems().size()));
    } // Updating results of table 
    
    @FXML private void btn_generate_pdf_setOnAction(ActionEvent event) {
        GeneratePDF();
    } // Generate PDF

    @FXML private void btn_criminal_submit_setOnAction(ActionEvent event) {
        try {
            con = getDBConnection();
            pst = con.prepareStatement("INSERT INTO `criminal_records` "
                    + "(`OR_Number`, `F_name`, `L_name`, `sex`, `birthdate`, `address`, `drugtest_result`, `contact_number`) "
                    + "VALUES (?,?,?,?,?,?,?,'');");
            pst.setDouble(1, Math.random());
            pst.setString(2, fld_crim_fname.getText());
            pst.setString(3, fld_crim_lname.getText());
            pst.setString(4, fld_crim_sex.getText());
            pst.setString(5, fld_crim_birthdate.getText());
            pst.setString(6, fld_crim_address.getText());
            pst.setString(7, toggleGroup.getSelectedToggle().getUserData().toString());
            if(pst.executeUpdate()>0){
                DrugTest.show("Message", "Data submitted", Alert.AlertType.INFORMATION);
            }
            ClearFields();
            AutoFill();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DrugTestFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML private void btn_non_crim_submit_setOnAction(ActionEvent event) {
        if(!fld_purpose.getText().isEmpty()){
            //DrugTest.show("Message", "Details forwared to " + fld_purpose.getText() + " Office.", Alert.AlertType.INFORMATION);
            DrugTest.Notif(fld_purpose.getText() + " Office", "You got a new transaction from "
                + "Crime Laboratory DIGS");
        }
        else{
            DrugTest.show("Message", "Please select your purpose.", Alert.AlertType.ERROR);
        }
    }

}