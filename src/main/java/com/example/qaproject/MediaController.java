package com.example.qaproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class MediaController {

    @FXML
    private MediaView viewMedia;
    private MediaPlayer mpVideo;
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
}