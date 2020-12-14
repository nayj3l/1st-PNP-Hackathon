package RealTimeVerification;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Optional;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class Verification extends Application {
    
    Parent root;
    Stage stage;
    
    private double xOffset = 0; 
    private double yOffset = 0; 
    
    private static Double locX;
    private static Double locY;
            
    @Override
    public void start(Stage stage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("VerificationFXML.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add(("/css/vehicle.css"));
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
        stage.setTitle("Real-Time Verification");
        this.stage = stage;
        locX = stage.getX();
        locY = stage.getY();
        stage.xProperty().addListener((obs, oldVal, newVal) -> { locX = newVal.doubleValue(); });
        stage.yProperty().addListener((obs, oldVal, newVal) -> { locY = newVal.doubleValue(); });
        DragMouse();
    }
    
    private void DragMouse(){
        root.setOnMouseDragged(e -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
        root.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY(); 
        }); 
    }
    
    public static boolean go = false;
    public static void show(String title, String content, Alert.AlertType type){     
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.setX(locX + 320);
        alert.setY(locY + 200);
        Optional<ButtonType> result = alert.showAndWait();
        go = result.get() == ButtonType.OK;
    }
    
    public static void Notif(String title, String msg){
        Notifications notif = Notifications.create()
            .title(title)
            .text(msg)
            .position(Pos.BASELINE_RIGHT)
            .hideAfter(Duration.seconds(10));
        notif.showWarning();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
