package com.example.qaproject;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

public class MediaController implements Initializable {

    @FXML
    private MediaView viewMedia;
    private MediaPlayer playerVideo;
    private Media mediaVideo;

    @FXML
    private VBox mainWindow;

    @FXML
    private HBox controlBar;

    @FXML
    private HBox volumeControls;

    @FXML
    private Button playPauseButton;

    @FXML
    private Label totalTime;

    @FXML
    private Label currentTime;

    @FXML
    private Label fullScreen;

    @FXML
    private Label selectSpeed;

    @FXML
    private Label volumeNumber;

    @FXML
    private Slider volumeBar;

    @FXML
    private Slider progressBar;

    @FXML
    private Button openFileButton;

    @FXML
    private Button openPlaylist;

    private boolean isPlaying = true;
    private boolean isMuted = true;
    private boolean videoEnd = false;

    private ImageView playButton;
    private ImageView pauseButton;
    private ImageView restartButton;
    private ImageView fullScreenButton;
    private ImageView muteButton;
    private ImageView exitFullScreenButton;
    private ImageView volumeButton;

    private String filePath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        final int iconSize = 20;

        mediaVideo = new Media(new File("src/main/resources/dummyFile.mp4").toURI().toString());
        playerVideo = new MediaPlayer(mediaVideo);
        viewMedia.setMediaPlayer(playerVideo);

        Image fullscreenIcon = new Image(new File("src/main/resources/icons/fullscreen.png").toURI().toString());
        fullScreenButton = new ImageView(fullscreenIcon);
        fullScreenButton.setFitHeight(iconSize);
        fullScreenButton.setFitWidth(iconSize);

        Image muteIcon = new Image(new File("src/main/resources/icons/mute.png").toURI().toString());
        muteButton = new ImageView(muteIcon);
        muteButton.setFitHeight(iconSize);
        muteButton.setFitWidth(iconSize);

        Image exitFullScreenIcon = new Image(new File("src/main/resources/icons/exit_fullscreen.png").toURI().toString());
        exitFullScreenButton = new ImageView(exitFullScreenIcon);
        exitFullScreenButton.setFitHeight(iconSize);
        exitFullScreenButton.setFitWidth(iconSize);

        Image playIcon = new Image(new File("src/main/resources/icons/play.png").toURI().toString());
        playButton = new ImageView(playIcon);
        playButton.setFitHeight(iconSize);
        playButton.setFitWidth(iconSize);

        Image pauseIcon = new Image(new File("src/main/resources/icons/pause.png").toURI().toString());
        pauseButton = new ImageView(pauseIcon);
        pauseButton.setFitHeight(iconSize);
        pauseButton.setFitWidth(iconSize);

        Image restartIcon = new Image(new File("src/main/resources/icons/restart.png").toURI().toString());
        restartButton = new ImageView(restartIcon);
        restartButton.setFitHeight(iconSize);
        restartButton.setFitWidth(iconSize);

        Image volumeIcon = new Image(new File("src/main/resources/icons/volume.png").toURI().toString());
        volumeButton = new ImageView(volumeIcon);
        volumeButton.setFitHeight(iconSize);
        volumeButton.setFitWidth(iconSize);

        playPauseButton.setGraphic(pauseButton);
        volumeNumber.setGraphic(muteButton);
        selectSpeed.setText("2x");
        fullScreen.setGraphic(fullScreenButton);

        openFileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fc = new FileChooser();
                FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select a File", "*.mp4","*.mp3","*.wav","*.aac","*.avi");
                fc.getExtensionFilters().add(filter);
                File f = fc.showOpenDialog(null);
                filePath = f.toURI().toString();

                if (filePath != null) {
                    Media videoMedia = new Media(filePath);
                    playerVideo = new MediaPlayer(videoMedia);
                    viewMedia.setMediaPlayer(playerVideo);
                    playerVideo.play();
                }
            }
        });

//        openPlaylist.setOnAction(new EventHandler<ActionEvent>() {
//                                         public void handle(ActionEvent event) {
//                                             Parent root;
//                                             try {
//                                                 root = FXMLLoader.load(getClass().getClassLoader().getResource("E:\\QAProject\\src\\main\\resources\\com\\example\\qaproject\\playlist.fxml"));
//                                                 Stage stage = new Stage();
//                                                 stage.setTitle("Make a playlist");
//                                                 stage.setScene(new Scene(root, 450, 450));
//                                                 stage.show();
//                                                 ((Node)(event.getSource())).getScene().getWindow().hide();
//                                             }
//                                             catch (IOException e) {
//                                                 e.printStackTrace();
//                                             }
//                                         }
//                                     });

        volumeControls.getChildren().remove(volumeBar);

        playerVideo.volumeProperty().bindBidirectional(volumeBar.valueProperty());

        bindCurrentTime();

        volumeBar.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                playerVideo.setVolume(volumeBar.getValue());
                if (playerVideo.getVolume() != 0.0) {
                    volumeNumber.setGraphic(volumeButton);
                    isMuted = false;
                } else {
                    volumeNumber.setGraphic(muteButton);
                    isMuted = true;
                }
            }
        });

        playPauseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bindCurrentTime();
                Button pressPlay = (Button) actionEvent.getSource();
                if (videoEnd) {
                    progressBar.setValue(0);
                    videoEnd = false;
                    isPlaying = false;
                }
                if (isPlaying) {
                    pressPlay.setGraphic(playButton);
                    playerVideo.pause();
                    isPlaying = false;
                } else {
                    pressPlay.setGraphic(pauseButton);
                    playerVideo.play();
                    isPlaying = true;
                }
            }
        });

        selectSpeed.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (selectSpeed.getText().equals("1x")) {
                    selectSpeed.setText("2x");
                    playerVideo.setRate(2.0);
                } else {
                    selectSpeed.setText("1x");
                    playerVideo.setRate(1.0);
                }
            }
        });

        volumeNumber.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (isMuted) {
                    volumeNumber.setGraphic(volumeButton);
                    volumeBar.setValue(0.2);
                    isMuted = false;
                } else {
                    volumeNumber.setGraphic(muteButton);
                    volumeBar.setValue(0);
                    isMuted = true;
                }
            }
        });

        volumeNumber.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (volumeControls.lookup("#volumeBar") == null) {
                    volumeControls.getChildren().add(volumeBar);
                    volumeBar.setValue(playerVideo.getVolume());
                }
            }
        });

        volumeControls.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                volumeControls.getChildren().remove(volumeBar);
            }
        });

        playerVideo.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                playPauseButton.setGraphic(restartButton);
                videoEnd = true;
                if (!currentTime.textProperty().equals(totalTime.textProperty())) {
                    currentTime.textProperty().unbind();
                    currentTime.setText(getTime(playerVideo.getTotalDuration()) + " / ");
                }
            }
        });

        mainWindow.sceneProperty().addListener(new ChangeListener<Scene>() {
            @Override
            public void changed(ObservableValue<? extends Scene> observableValue, Scene originalScene, Scene resizedScene) {
                if (originalScene == null && resizedScene != null) {
                    viewMedia.fitHeightProperty().bind(resizedScene.heightProperty().subtract(controlBar.heightProperty().add(20)));
                }
            }
        });

        fullScreen.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Label label = (Label) mouseEvent.getSource();
                Stage stage = (Stage) label.getScene().getWindow();
                if (stage.isFullScreen()) {
                    stage.setFullScreen(false);
                    fullScreen.setGraphic(fullScreenButton);
                } else {
                    stage.setFullScreen(true);
                    fullScreen.setGraphic(exitFullScreenButton);
                }
                stage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent keyEvent) {
                        if (keyEvent.getCode() == KeyCode.ESCAPE) {
                            fullScreen.setGraphic(fullScreenButton);
                        }
                    }
                });
            }
        });

        playerVideo.totalDurationProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration originalDuration, Duration newDuration) {
                bindCurrentTime();
                progressBar.setMax(newDuration.toSeconds());
                totalTime.setText(getTime(newDuration));
            }
        });

        progressBar.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean changed, Boolean changing) {
                bindCurrentTime();
                if (!changing) {
                    playerVideo.seek(Duration.seconds(progressBar.getValue()));
                }
            }
        });

        progressBar.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number originalValue, Number newValue) {
                bindCurrentTime();
                double presentTime = playerVideo.getCurrentTime().toSeconds();
                if (Math.abs(presentTime - newValue.doubleValue()) > 0.5) {
                    playerVideo.seek(Duration.seconds(newValue.doubleValue()));
                }
                atEndOfVideo(currentTime.getText(), totalTime.getText());
            }
        });

        playerVideo.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration originalTime, Duration newTime) {
                bindCurrentTime();
                if (!progressBar.isValueChanging()) {
                    progressBar.setValue(newTime.toSeconds());
                }
                atEndOfVideo(currentTime.getText(), totalTime.getText());
            }
        });
    }

    public void bindCurrentTime() {
        currentTime.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getTime(playerVideo.getCurrentTime()) + " / ";
            }
        }, playerVideo.currentTimeProperty()));
    }

    public String getTime(Duration time) {
        int hours = (int) time.toHours();
        int minutes = (int) time.toMinutes();
        int seconds = (int) time.toSeconds();

        if (seconds > 59) seconds = seconds % 60;
        if (minutes > 59) minutes = minutes % 60;
        if (hours > 59) hours = hours % 60;

        if (hours > 0) return String.format("%d:%02d:%02d",
                hours,
                minutes,
                seconds);
        else return String.format("%02d:%02d",
                minutes,
                seconds);
    }

    public void atEndOfVideo(String timeNow, String totalTime) {
        for (int i = 0; i < totalTime.length(); i++) {
            if (timeNow.charAt(i) != totalTime.charAt(i)) {
                videoEnd = false;
                if (isPlaying) playPauseButton.setGraphic(pauseButton);
                else playPauseButton.setGraphic(playButton);
                break;
            } else {
                videoEnd = true;
                playPauseButton.setGraphic(restartButton);
            }
        }
    }
}