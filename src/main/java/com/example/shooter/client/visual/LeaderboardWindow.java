package com.example.shooter.client.visual;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.example.shooter.server.PlayerStatistic;

import java.util.List;

public class LeaderboardWindow {
    Stage window;
    public void show(List<PlayerStatistic> leaderboard) {
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        TableView<PlayerStatistic> table = new TableView<>(FXCollections.observableList(leaderboard));
        table.setPrefSize(267, 300);

        TableColumn<PlayerStatistic, Integer> indexColumn = new TableColumn<>("№");
        indexColumn.setCellValueFactory(column -> {
            int rowIndex = table.getItems().indexOf(column.getValue()) + 1;
            return javafx.beans.binding.Bindings.createObjectBinding(() -> rowIndex);
        });
        indexColumn.setSortable(false);
        table.getColumns().add(indexColumn);

        TableColumn<PlayerStatistic, String> name_column = new TableColumn<>("Имя");
        name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.getColumns().add(name_column);

        TableColumn<PlayerStatistic, Integer> num_wins_column = new TableColumn<>("Количество побед");
        num_wins_column.setCellValueFactory(new PropertyValueFactory<>("num_wins"));
        table.getColumns().add(num_wins_column);

        FlowPane main = new FlowPane(table);
        main.setAlignment(Pos.CENTER);

        Scene scene = new Scene(main, 267, 300);
        window.setScene(scene);
        window.setTitle("Таблица лидеров");
        window.setResizable(false);
        window.showAndWait();
    }
}
