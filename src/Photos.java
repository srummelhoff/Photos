import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import javax.imageio.ImageIO;

public class Photos extends Application {
     
    private ImageView centerStage;
    private final ImageView rightButton = new ImageView (new Image("http://pixsector.com/cache/d317f9c9/avefdb1ad8fbf8d8b72a2.png",100,100,true,true));
    private final ImageView leftButton = new ImageView (new Image("http://pixsector.com/cache/8ed3eed7/avb6b6c2625bcda563bf1.png", 100, 100, true, true));
    private File sample = new File("H:/Documents/All Photos/Random Photos");

    
    private final ImageView leftSubButton = new ImageView (new Image("http://pixsector.com/cache/8ed3eed7/avb6b6c2625bcda563bf1.png",50,50,true,true));
    private final ImageView rightSubButton = new ImageView (new Image("http://pixsector.com/cache/d317f9c9/avefdb1ad8fbf8d8b72a2.png",50,50,true,true));
    private final BorderPane pane = new BorderPane();
    private final BorderPane bottomPane = new BorderPane();
    private final StackPane sPane = new StackPane();
    private Scene scene;
    private final HBox forPhotos =  new HBox();
    private final HBox rightBox = new HBox();
    private final HBox leftBox = new HBox();
    private final HBox hbox = new HBox();
    private final HBox hbox1 = new HBox();
    private final MenuBar menuBar = new MenuBar();
    private final Menu fileMenu = new Menu("File");
    private final Menu editMenu = new Menu("Edit");  
    private final DirectoryChooser dc = new DirectoryChooser();
    protected Window ownerWindow;
    private final Insets inPhotos = new Insets(10);
    private int indexBottom = 0;
    private int indexTop = 0;
    private final ArrayList<ImageView> imageViewArray = new ArrayList();
    private final ArrayList<Image> imageArray = new ArrayList();

    @Override
    public void start(Stage primaryStage) {
        
        scene = new Scene(pane, 1000, 800);
        pane.setStyle("-fx-background-color: white;");
        bottomPane.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5,0,0,0))));
        bottomPane.setMinHeight(200);
        primaryStage.setTitle("Viewer");
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(800);
        primaryStage.setScene(scene);
        primaryStage.show(); 
        
        setUpMenu();        
        setUpBottom(sample);
        setUpLeftRightButtons(); 
    }
    
    public void setUpMenu() {
        MenuItem open = new MenuItem("Open");
        MenuItem exit = new MenuItem("Exit");
        MenuItem save = new MenuItem("Save As");
        MenuItem rotateLeft = new MenuItem("Rotate Left");
        MenuItem rotateRight = new MenuItem("Rotate Right");
        
        open.setOnAction((ActionEvent e) -> {
            sample = dc.showDialog(ownerWindow);
            setUpBottom(sample);
            indexTop = 0;
            indexBottom = 0;
        });
        exit.setOnAction(e -> {
            Platform.exit();
        });
        rotateLeft.setOnAction(e -> {
            centerStage.setRotate(centerStage.getRotate() - 90);
        });
        rotateRight.setOnAction(e -> {
            centerStage.setRotate(centerStage.getRotate() + 90);
        });
        save.setOnAction(e -> {
            saveToFile(centerStage.getImage());
        });
        
        fileMenu.getItems().addAll(open,save,exit);
        editMenu.getItems().addAll(rotateLeft, rotateRight);
        menuBar.getMenus().addAll(fileMenu, editMenu);
        pane.setTop(menuBar);
    }
    
    public void setUpCenter(Image image) {
        centerStage = new ImageView(image);
        centerStage.setRotate(0);
        centerStage.setPreserveRatio(true);
        centerStage.setFitHeight(400);
        centerStage.setFitWidth(400);
        sPane.getChildren().clear();
        sPane.getChildren().add(centerStage);
        sPane.setPrefHeight(400);
        sPane.setPrefWidth(400);
        pane.setCenter(sPane);
    }
    
    public static void saveToFile(Image image) {
    File outputFile = new File("newphoto.png");
    BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
    try {
      ImageIO.write(bImage, "png", outputFile);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
    
    public void setUpBottom(File photos1) {

        File[] photoList = photos1.listFiles();
        ArrayList <File> fileArray = new ArrayList(Arrays.asList(photoList));
        forPhotos.getChildren().clear();
        imageViewArray.clear();
        imageArray.clear();
        
        try {
            for(int i = 0; i < fileArray.size(); i++) {
                imageArray.add(new Image(new FileInputStream(fileArray.get(i))));
            }
        }
        catch(FileNotFoundException ex) {
            System.out.print(ex.getMessage());
        }
        
        for(int i = 0; i < imageArray.size(); i++) {
            imageViewArray.add(new ImageView(imageArray.get(i)));
            imageViewArray.get(i).setPreserveRatio(true);
            imageViewArray.get(i).setFitHeight(100);
            imageViewArray.get(i).setFitWidth(100);
            Image temp = imageArray.get(i);
            int tempint = i;
            imageViewArray.get(i).setOnMouseClicked(e -> {
                indexBottom = tempint;
                resizeCenter(tempint);
                setUpCenter(temp);
                rotateBottom(indexBottom);
                indexBottom=indexTop;
            });
        }
        for(int i = indexTop-2; i <= indexTop+2; i++) {
            VBox vbox = new VBox();
            if(i < 0) {
                vbox.getChildren().add(imageViewArray.get(imageViewArray.size()+i));
            }
            else if(i >= 0) {
                vbox.getChildren().add(imageViewArray.get(i));
            }  
            resizeCenter(indexTop);
            vbox.setPadding(inPhotos);
            vbox.setAlignment(Pos.CENTER);
            forPhotos.getChildren().add(vbox);
            forPhotos.setAlignment(Pos.CENTER);
            
        }
        bottomPane.setCenter(forPhotos);
        pane.setBottom(bottomPane);
        setUpCenter(imageArray.get(0));  
    }
    public void resizeCenter(int index) {
        for(int i = 0; i < imageViewArray.size(); i++) {
            imageViewArray.get(i).setFitHeight(100);
            imageViewArray.get(i).setFitWidth(100);
        }
        imageViewArray.get(index).setFitHeight(125);
        imageViewArray.get(index).setFitWidth(125);
    }
    public void left() {
        indexBottom=indexTop;
            if(indexTop >0) {
                indexTop--;
                resizeCenter(indexTop);
                Image temp = imageArray.get(indexTop);
                setUpCenter(temp);
                rotateBottom(indexTop);
            }
            else if(indexTop == 0) {
                indexTop = imageArray.size()-1;
                resizeCenter(indexTop);
                Image temp = imageArray.get(indexTop);
                setUpCenter(temp);
                rotateBottom(indexTop);
            }
    }
    
    public void right() {
         indexBottom=indexTop;
            if(indexTop < imageViewArray.size()-1) {
                indexTop++;
                resizeCenter(indexTop);
                Image temp = imageArray.get(indexTop);
                setUpCenter(temp);
                rotateBottom(indexTop);
            }
            else if(indexTop == imageViewArray.size()-1) {
                indexTop = 0;
                resizeCenter(indexTop);
                Image temp = imageArray.get(indexTop);
                setUpCenter(temp);
                rotateBottom(indexTop);
            }
    }

    public void setUpLeftRightButtons() {
        leftSubButton.setOnMouseClicked(e -> {
            if(indexBottom >0) {
                indexBottom--;
                rotateBottom(indexBottom);
            }
            else if(indexBottom == 0) {
                indexBottom = imageArray.size()-1;
                rotateBottom(indexBottom);
            }
    	});
        rightSubButton.setOnMouseClicked(e -> { 
            if(indexBottom < imageViewArray.size()-1) {
                indexBottom++;
                rotateBottom(indexBottom);
            }
            else if(indexBottom == imageViewArray.size()-1) {
                indexBottom = 0;
                rotateBottom(indexBottom);
            }
    	});
        
        hbox.setPrefHeight(100);
        hbox.setPrefWidth(100);
    	hbox.getChildren().add(leftButton);
    	hbox.setAlignment(Pos.CENTER);
        
    	leftButton.setOnMouseClicked(e -> {
            left();
    	});
        scene.setOnKeyPressed(e -> {
            
            if(e.getCode() == KeyCode.LEFT) {
                left();
            }
            if(e.getCode() == KeyCode.RIGHT) {
               right();
            }
        });
        hbox1.setPrefHeight(100);
        hbox1.setPrefWidth(100);
    	hbox1.getChildren().add(rightButton);
    	hbox1.setAlignment(Pos.CENTER);
        
        rightButton.setOnMouseClicked(e -> { 
           right();
    	});
        
        rightBox.getChildren().add(rightSubButton);
        rightBox.setAlignment(Pos.CENTER);
        leftBox.getChildren().add(leftSubButton);
        leftBox.setAlignment(Pos.CENTER);
        bottomPane.setLeft(leftBox);
        bottomPane.setRight(rightBox);
        pane.setLeft(hbox);
    	pane.setRight(hbox1);   	
    }
    
    public void rotateBottom(int index) {
        forPhotos.getChildren().clear();
        for(int i = index-2; i <= index+2; i++) {
            VBox vbox = new VBox();
            if(index < 2) {
                if(i < 0) {
                    vbox.getChildren().add(imageViewArray.get(imageViewArray.size()+i));
                }
                else if(i >= 0) {
                    vbox.getChildren().add(imageViewArray.get(i));
                }  
            }
            else if(index >= 2 && index < imageViewArray.size()-2) {
                vbox.getChildren().add(imageViewArray.get(i));
            }
            else if(index >=imageViewArray.size()-2) {
                if(i < imageViewArray.size()) {
                    vbox.getChildren().add(imageViewArray.get(i));
                }
                else if(i >= imageViewArray.size()) {
                    vbox.getChildren().add(imageViewArray.get(i-imageViewArray.size()));
                }
            }
            vbox.setAlignment(Pos.CENTER);
            vbox.setPadding(inPhotos);
            forPhotos.getChildren().add(vbox);
            forPhotos.setAlignment(Pos.CENTER);

        }
        bottomPane.setCenter(forPhotos);
    }
    
    public static void main(String[] args)
    {
       launch(args);
    }
}