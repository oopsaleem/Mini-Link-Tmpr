package com.kgwb.fxgui;

import com.kgwb.LongRunningTask;
import com.kgwb.model.MiniLinkDeviceTmprWrapper;
import com.kgwb.model.TemperatureModel;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.*;


public class Controller implements Initializable {

    private int proActiveVal = 10;
    private TableView<TemperatureModel> tableView;
    private ScheduledExecutorService ses;
    private ScheduledFuture<?> scheduledFuture;

    @FXML
    private Spinner<Integer> spinnerProactive;
    @FXML
    private Button btnExport;
    @FXML
    private AnchorPane contentAnchorPane;
    @FXML
    private ProgressBar progress;
    @FXML
    private Label rightStatusLabel;
    @FXML
    private TextField urlTextEntry;
    @FXML
    private TextField filterTextEntry;
    @FXML
    private Button btnChangePathAndRun;
    @FXML
    private Button btnRun;

    public void initialize(URL location, ResourceBundle resources) {
        tableView = new TableView<>();
        btnExport.setDisable(true);
        spinnerProactive.getValueFactory().setValue(proActiveVal);
        ((SpinnerValueFactory.IntegerSpinnerValueFactory)spinnerProactive.getValueFactory()).setAmountToStepBy(1);

        contentAnchorPane.getChildren().add(tableView);
        AnchorPane.setBottomAnchor(tableView, 0.0);
        AnchorPane.setTopAnchor(tableView, 0.0);
        AnchorPane.setLeftAnchor(tableView, 0.0);
        AnchorPane.setRightAnchor(tableView, 0.0);

        urlTextEntry.setPromptText("Click [Change ...] to select folder of Mini-Link QoS *.cfg files.");

        btnChangePathAndRun.setOnAction(event -> {
            if (progress.visibleProperty().get()) return;
            if (FileSourceNotReady(event, true)) return;
            startProcess();
        });

        btnRun.setOnAction(event -> {
            if (FileSourceNotReady(event, false)) return;
            startProcess();
        });

        btnExport.setOnAction(event -> {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MS Excel files (*.xlsx)", "*.xlsx");
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(extFilter);
            File file = chooser.showSaveDialog(primaryStage);

            if (file == null) return;

            SXSSFWorkbook workbook = new SXSSFWorkbook(-1); // keep 100 rows in memory, exceeding rows will be flushed to disk
            Sheet spreadsheet = workbook.createSheet();

            Row row = spreadsheet.createRow(0);

            int colSize = tableView.getColumns().size();
            int itemSize = tableView.getItems().size();

            for (int j = 0; j < colSize; j++) {
                row.createCell(j).setCellValue(tableView.getColumns().get(j).getText());
            }

            SheetConditionalFormatting sheetCF = spreadsheet.getSheetConditionalFormatting();

            for (int r = 0; r < itemSize; r++) {
                row = spreadsheet.createRow(r + 1);
                for (int c = 0; c < colSize; c++) {
                    if (colSize >= ((c + 1) * 3 + 1)) {
                        ConditionalFormattingRule criticalRule = sheetCF.createConditionalFormattingRule(
                                ComparisonOperator.GE,
                                String.format("%s%d-%d", getExcelColumnName((c + 1) * 3 + 3), r + 2, proActiveVal));//F2-20
                        PatternFormatting criticalPatternFmt = criticalRule.createPatternFormatting();
                        criticalPatternFmt.setFillBackgroundColor(IndexedColors.RED.index);
                        ConditionalFormattingRule majorRule = sheetCF.createConditionalFormattingRule(
                                ComparisonOperator.BETWEEN,
                                String.format("%s%d-%d", getExcelColumnName((c + 1) * 3 + 2), r + 2, proActiveVal), //E2-20
                                String.format("%s%d-%d", getExcelColumnName((c + 1) * 3 + 3), r + 2, proActiveVal));//F2-20
                        PatternFormatting majorPatternFmt = majorRule.createPatternFormatting();
                        majorPatternFmt.setFillBackgroundColor(IndexedColors.ORANGE.index);
//                        ConditionalFormattingRule minorRule = sheetCF.createConditionalFormattingRule(ComparisonOperator.LT,
//                                String.format("%s%d-%d", getExcelColumnName((c + 1) * 3 + 2), r + 2, proActiveVal));//E2-20
//                        PatternFormatting minorPatternFmt = minorRule.createPatternFormatting();
//                        minorPatternFmt.setFillBackgroundColor(IndexedColors.GREEN.index);
                        ConditionalFormattingRule[] cfRules = {
                                criticalRule,
                                majorRule,
                              //minorRule
                        };

                        CellRangeAddress[] regions = {CellRangeAddress.valueOf(String.format("%s%d",
                                getExcelColumnName((c + 1) * 3 + 1), r + 2))}; //D2
                        sheetCF.addConditionalFormatting(regions, cfRules);
                    }

                    Object fxCellData = tableView.getColumns().get(c).getCellData(r);
                    if (fxCellData != null) {
                        if (c >= 3) {
                            row.createCell(c, CellType.NUMERIC).setCellValue(Integer.parseInt(fxCellData.toString()));
                            spreadsheet.setColumnWidth(c, 255 * 4);
                        } else row.createCell(c).setCellValue(fxCellData.toString());
                    } else {
                        row.createCell(c).setCellValue("");
                    }
                }
            }

            FileOutputStream fileOut;
            try {
                fileOut = new FileOutputStream(file);
                workbook.write(fileOut);
                fileOut.close();
                java.awt.Desktop.getDesktop().open(file);
            } catch (FileNotFoundException e) {
//                messageBox("Export Error", e.getMessage());
                exceptionDialog("Export Error", "Could not export file !", file.getPath(), e);
            } catch (IOException e) {
                exceptionDialog("Export Error", "Could not export file due to Input/Output Error.", file.getPath(), e);
            } finally {
                workbook.dispose();
            }
        });

//        spinnerProactive.valueProperty().addListener((obs, oldValue, newValue) ->
//        {
//            System.out.printf("New value: %d, %s%n", newValue, isProcessStarted.get() ? "Started" : "NotStarted");
//
//            // init Delay = 5, repeat the task every 1 second
//            if (newValue > 0) {
//                if(ses == null || ses.isShutdown())
//                    ses = Executors.newScheduledThreadPool(1);
//                if(!isProcessStarted.get())
//                    scheduledFuture = ses.scheduleAtFixedRate(task, 5, newValue, TimeUnit.MINUTES);
//            }
//
//            if (newValue == 0) {
//                System.out.println("Cancel scheduledFuture as newValue =0 !");
//                scheduledFuture.cancel(true);
//                ses.shutdown();
//            }
//        });
    }

    private boolean FileSourceNotReady(ActionEvent event, boolean forceChange) {
        String filePath = urlTextEntry.getText();
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        String browsedPath;
        File file = new File(filePath);

        if (file.exists() && !forceChange)
            browsedPath = file.getAbsolutePath();
        else {
            FileChooser fileChooser = new FileChooser();
            if (file.exists()) {
                fileChooser.setInitialDirectory(file.getParentFile());
            }
            File fileSelected = fileChooser.showOpenDialog(primaryStage);
            browsedPath = fileSelected != null ? fileSelected.getAbsolutePath() : null;
        }

        if (browsedPath == null) return true;
        urlTextEntry.setText(browsedPath);
        return false;
    }

    //    private AtomicBoolean isProcessStarted = new AtomicBoolean(false);
    private void startProcess() {
        //tableView.getItems().clear();
        btnRun.setText("Stop");
        progress.setVisible(true);

        File file = new File(urlTextEntry.getText());
        if (!(file.exists() || !file.isDirectory())) return;

        final LongRunningTask task = new LongRunningTask(urlTextEntry.getText());

        rightStatusLabel.textProperty().bind(task.messageProperty());
        progress.progressProperty().bind(task.progressProperty());

        task.setOnSucceeded((succeededEvent) -> {
            btnRun.setText("Start");
            progress.setVisible(false);
            btnExport.setDisable(false);
//            spinnerProactive.setDisable(false);
//            isProcessStarted.set(false);
            populateTable(task.getValue());
        });

        task.setOnCancelled(cancelledEvent -> {
            btnRun.setText("Start");
            progress.setVisible(false);
//            spinnerProactive.setDisable(false);
            btnExport.setDisable(true);
//            isProcessStarted.set(false);
        });

        task.setOnFailed((failedEvent) -> {
            btnRun.setText("Start");
            progress.setVisible(true);
            btnExport.setDisable(true);
//            spinnerProactive.setDisable(false);
//            isProcessStarted.set(false);
            rightStatusLabel.textProperty().unbind();
            rightStatusLabel.setText(String.format("Failed when %s", rightStatusLabel.getText()));
            exceptionDialog("Processing Failure", "Processing failed to finish.",
                    "We experienced ", new Exception(task.getException()));
        });

        btnRun.setOnAction(event -> {
            if (task.isRunning()) {
                task.cancel();
                return;
            }
            if (FileSourceNotReady(event, false)) return;
            startProcess();
        });

//        spinnerProactive.setDisable(true);
//        isProcessStarted.set(true);
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(task);
        executorService.shutdown();
    }

    public int convertNetmaskToCIDR(InetAddress netmask) {

        byte[] netmaskBytes = netmask.getAddress();
        int cidr = 0;
        boolean zero = false;
        for (byte b : netmaskBytes) {
            int mask = 0x80;

            for (int i = 0; i < 8; i++) {
                int result = b & mask;
                if (result == 0) {
                    zero = true;
                } else if (zero) {
                    throw new IllegalArgumentException("Invalid netmask.");
                } else {
                    cidr++;
                }
                mask >>>= 1;
            }
        }
        return cidr;
    }

    //https://code.makery.ch/blog/javafx-dialogs-official/
    private void exceptionDialog(String title, String headerText, String contentText, Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
    }

    private void populateTable(List<MiniLinkDeviceTmprWrapper> objectList) {
//        tableView.getItems().clear();
        tableView.getColumns().clear();
        tableView.setPlaceholder(new Label("Loading..."));

        proActiveVal = spinnerProactive.getValueFactory().getValue();
        Map<String, String> fieldCaption = new LinkedHashMap<>();

        fieldCaption.put("site", "siteId");
        fieldCaption.put("ip address", "ipAddress");
        fieldCaption.put("Comment", "comment");
        fieldCaption.forEach((k, v) -> {
            TableColumn<TemperatureModel, String> column = new TableColumn<>(k);
            column.setId(v + "Col");
            column.setCellValueFactory(new PropertyValueFactory<>(v));
            tableView.getColumns().add(column);
        });

        if (objectList != null && objectList.size() > 0) {

            ObservableList<TemperatureModel> modelData = FXCollections.observableArrayList();
            objectList.forEach(mlWrpr -> modelData.add(new TemperatureModel((mlWrpr))));

            int maxSlots = modelData
                    .stream()
                    .mapToInt(TemperatureModel::getSlotMax)
                    .max().orElse(-1);

            fieldCaption.clear();
            fieldCaption.put("T", "temp");
            fieldCaption.put("H", "high");
            fieldCaption.put("E", "exce");
            NumberFormat nf = new DecimalFormat("00");

            for (int slotPos = 0; slotPos <= maxSlots; slotPos++)
                for (Map.Entry<String, String> entry : fieldCaption.entrySet()) {
                    String k = entry.getKey();
                    String v = entry.getValue();
                    TableColumn<TemperatureModel, Integer> column = new TableColumn<>(String.format("%s%s", k, nf.format(slotPos)));
                    column.setId(String.format("%s%sCol", v, nf.format(slotPos)));
                    column.setPrefWidth(28);
                    column.setCellValueFactory(new PropertyValueFactory<>(String.format("%s%s", v, nf.format(slotPos))));

                    if (v.startsWith("temp"))
                        switch (slotPos) {
                            case  0: column.setCellFactory(param -> new TableCell<TemperatureModel, Integer>() { @Override protected void updateItem(Integer item, boolean empty) { super.updateItem(item, empty); if (item == null || empty) { setText(null); setStyle(""); } else { setText(String.valueOf(item)); int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue(); SimpleIntegerProperty temp00P = param.getTableView().getItems().get(currentIndex).temp00Property(); if (temp00P != null) { int temp = param.getTableView().getItems().get(currentIndex).getTemp00(); int high = param.getTableView().getItems().get(currentIndex).getHigh00() - proActiveVal; int exce = param.getTableView().getItems().get(currentIndex).getExce00() - proActiveVal; if (temp >= exce) { setStyle("-fx-background-color: red"); setTextFill(Color.WHITE); } else if (temp >= high) { setStyle("-fx-background-color: orange"); setTextFill(Color.WHITE); } else { setStyle(""); setTextFill(Color.BLACK); } } } } } ); break;
                            case  1: column.setCellFactory(param -> new TableCell<TemperatureModel, Integer>() { @Override protected void updateItem(Integer item, boolean empty) { super.updateItem(item, empty); if (item == null || empty) { setText(null); setStyle(""); } else { setText(String.valueOf(item)); int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue(); SimpleIntegerProperty temp01P = param.getTableView().getItems().get(currentIndex).temp01Property(); if (temp01P != null) { int temp = param.getTableView().getItems().get(currentIndex).getTemp01(); int high = param.getTableView().getItems().get(currentIndex).getHigh01() - proActiveVal; int exce = param.getTableView().getItems().get(currentIndex).getExce01() - proActiveVal; if (temp >= exce) { setStyle("-fx-background-color: red"); setTextFill(Color.WHITE); } else if (temp >= high) { setStyle("-fx-background-color: orange"); setTextFill(Color.WHITE); } else { setStyle(""); setTextFill(Color.BLACK); } } } } } ); break;
                            case  2: column.setCellFactory(param -> new TableCell<TemperatureModel, Integer>() { @Override protected void updateItem(Integer item, boolean empty) { super.updateItem(item, empty); if (item == null || empty) { setText(null); setStyle(""); } else { setText(String.valueOf(item)); int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue(); SimpleIntegerProperty temp02P = param.getTableView().getItems().get(currentIndex).temp02Property(); if (temp02P != null) { int temp = param.getTableView().getItems().get(currentIndex).getTemp02(); int high = param.getTableView().getItems().get(currentIndex).getHigh02() - proActiveVal; int exce = param.getTableView().getItems().get(currentIndex).getExce02() - proActiveVal; if (temp >= exce) { setStyle("-fx-background-color: red"); setTextFill(Color.WHITE); } else if (temp >= high) { setStyle("-fx-background-color: orange"); setTextFill(Color.WHITE); } else { setStyle(""); setTextFill(Color.BLACK); } } } } } ); break;
                            case  3: column.setCellFactory(param -> new TableCell<TemperatureModel, Integer>() { @Override protected void updateItem(Integer item, boolean empty) { super.updateItem(item, empty); if (item == null || empty) { setText(null); setStyle(""); } else { setText(String.valueOf(item)); int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue(); SimpleIntegerProperty temp03P = param.getTableView().getItems().get(currentIndex).temp03Property(); if (temp03P != null) { int temp = param.getTableView().getItems().get(currentIndex).getTemp03(); int high = param.getTableView().getItems().get(currentIndex).getHigh03() - proActiveVal; int exce = param.getTableView().getItems().get(currentIndex).getExce03() - proActiveVal; if (temp >= exce) { setStyle("-fx-background-color: red"); setTextFill(Color.WHITE); } else if (temp >= high) { setStyle("-fx-background-color: orange"); setTextFill(Color.WHITE); } else { setStyle(""); setTextFill(Color.BLACK); } } } } } ); break;
                            case  4: column.setCellFactory(param -> new TableCell<TemperatureModel, Integer>() { @Override protected void updateItem(Integer item, boolean empty) { super.updateItem(item, empty); if (item == null || empty) { setText(null); setStyle(""); } else { setText(String.valueOf(item)); int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue(); SimpleIntegerProperty temp04P = param.getTableView().getItems().get(currentIndex).temp04Property(); if (temp04P != null) { int temp = param.getTableView().getItems().get(currentIndex).getTemp04(); int high = param.getTableView().getItems().get(currentIndex).getHigh04() - proActiveVal; int exce = param.getTableView().getItems().get(currentIndex).getExce04() - proActiveVal; if (temp >= exce) { setStyle("-fx-background-color: red"); setTextFill(Color.WHITE); } else if (temp >= high) { setStyle("-fx-background-color: orange"); setTextFill(Color.WHITE); } else { setStyle(""); setTextFill(Color.BLACK); } } } } } ); break;
                            case  5: column.setCellFactory(param -> new TableCell<TemperatureModel, Integer>() { @Override protected void updateItem(Integer item, boolean empty) { super.updateItem(item, empty); if (item == null || empty) { setText(null); setStyle(""); } else { setText(String.valueOf(item)); int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue(); SimpleIntegerProperty temp05P = param.getTableView().getItems().get(currentIndex).temp05Property(); if (temp05P != null) { int temp = param.getTableView().getItems().get(currentIndex).getTemp05(); int high = param.getTableView().getItems().get(currentIndex).getHigh05() - proActiveVal; int exce = param.getTableView().getItems().get(currentIndex).getExce05() - proActiveVal; if (temp >= exce) { setStyle("-fx-background-color: red"); setTextFill(Color.WHITE); } else if (temp >= high) { setStyle("-fx-background-color: orange"); setTextFill(Color.WHITE); } else { setStyle(""); setTextFill(Color.BLACK); } } } } } ); break;
                            case  6: column.setCellFactory(param -> new TableCell<TemperatureModel, Integer>() { @Override protected void updateItem(Integer item, boolean empty) { super.updateItem(item, empty); if (item == null || empty) { setText(null); setStyle(""); } else { setText(String.valueOf(item)); int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue(); SimpleIntegerProperty temp06P = param.getTableView().getItems().get(currentIndex).temp06Property(); if (temp06P != null) { int temp = param.getTableView().getItems().get(currentIndex).getTemp06(); int high = param.getTableView().getItems().get(currentIndex).getHigh06() - proActiveVal; int exce = param.getTableView().getItems().get(currentIndex).getExce06() - proActiveVal; if (temp >= exce) { setStyle("-fx-background-color: red"); setTextFill(Color.WHITE); } else if (temp >= high) { setStyle("-fx-background-color: orange"); setTextFill(Color.WHITE); } else { setStyle(""); setTextFill(Color.BLACK); } } } } } ); break;
                            case  7: column.setCellFactory(param -> new TableCell<TemperatureModel, Integer>() { @Override protected void updateItem(Integer item, boolean empty) { super.updateItem(item, empty); if (item == null || empty) { setText(null); setStyle(""); } else { setText(String.valueOf(item)); int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue(); SimpleIntegerProperty temp07P = param.getTableView().getItems().get(currentIndex).temp07Property(); if (temp07P != null) { int temp = param.getTableView().getItems().get(currentIndex).getTemp07(); int high = param.getTableView().getItems().get(currentIndex).getHigh07() - proActiveVal; int exce = param.getTableView().getItems().get(currentIndex).getExce07() - proActiveVal; if (temp >= exce) { setStyle("-fx-background-color: red"); setTextFill(Color.WHITE); } else if (temp >= high) { setStyle("-fx-background-color: orange"); setTextFill(Color.WHITE); } else { setStyle(""); setTextFill(Color.BLACK); } } } } } ); break;
                            case  8: column.setCellFactory(param -> new TableCell<TemperatureModel, Integer>() { @Override protected void updateItem(Integer item, boolean empty) { super.updateItem(item, empty); if (item == null || empty) { setText(null); setStyle(""); } else { setText(String.valueOf(item)); int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue(); SimpleIntegerProperty temp08P = param.getTableView().getItems().get(currentIndex).temp08Property(); if (temp08P != null) { int temp = param.getTableView().getItems().get(currentIndex).getTemp08(); int high = param.getTableView().getItems().get(currentIndex).getHigh08() - proActiveVal; int exce = param.getTableView().getItems().get(currentIndex).getExce08() - proActiveVal; if (temp >= exce) { setStyle("-fx-background-color: red"); setTextFill(Color.WHITE); } else if (temp >= high) { setStyle("-fx-background-color: orange"); setTextFill(Color.WHITE); } else { setStyle(""); setTextFill(Color.BLACK); } } } } } ); break;
                            case  9: column.setCellFactory(param -> new TableCell<TemperatureModel, Integer>() { @Override protected void updateItem(Integer item, boolean empty) { super.updateItem(item, empty); if (item == null || empty) { setText(null); setStyle(""); } else { setText(String.valueOf(item)); int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue(); SimpleIntegerProperty temp09P = param.getTableView().getItems().get(currentIndex).temp09Property(); if (temp09P != null) { int temp = param.getTableView().getItems().get(currentIndex).getTemp09(); int high = param.getTableView().getItems().get(currentIndex).getHigh09() - proActiveVal; int exce = param.getTableView().getItems().get(currentIndex).getExce09() - proActiveVal; if (temp >= exce) { setStyle("-fx-background-color: red"); setTextFill(Color.WHITE); } else if (temp >= high) { setStyle("-fx-background-color: orange"); setTextFill(Color.WHITE); } else { setStyle(""); setTextFill(Color.BLACK); } } } } } ); break;
                            case 10: column.setCellFactory(param -> new TableCell<TemperatureModel, Integer>() { @Override protected void updateItem(Integer item, boolean empty) { super.updateItem(item, empty); if (item == null || empty) { setText(null); setStyle(""); } else { setText(String.valueOf(item)); int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue(); SimpleIntegerProperty temp10P = param.getTableView().getItems().get(currentIndex).temp10Property(); if (temp10P != null) { int temp = param.getTableView().getItems().get(currentIndex).getTemp10(); int high = param.getTableView().getItems().get(currentIndex).getHigh10() - proActiveVal; int exce = param.getTableView().getItems().get(currentIndex).getExce10() - proActiveVal; if (temp >= exce) { setStyle("-fx-background-color: red"); setTextFill(Color.WHITE); } else if (temp >= high) { setStyle("-fx-background-color: orange"); setTextFill(Color.WHITE); } else { setStyle(""); setTextFill(Color.BLACK); } } } } } ); break;
                            case 11: column.setCellFactory(param -> new TableCell<TemperatureModel, Integer>() { @Override protected void updateItem(Integer item, boolean empty) { super.updateItem(item, empty); if (item == null || empty) { setText(null); setStyle(""); } else { setText(String.valueOf(item)); int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue(); SimpleIntegerProperty temp11P = param.getTableView().getItems().get(currentIndex).temp11Property(); if (temp11P != null) { int temp = param.getTableView().getItems().get(currentIndex).getTemp11(); int high = param.getTableView().getItems().get(currentIndex).getHigh11() - proActiveVal; int exce = param.getTableView().getItems().get(currentIndex).getExce11() - proActiveVal; if (temp >= exce) { setStyle("-fx-background-color: red"); setTextFill(Color.WHITE); } else if (temp >= high) { setStyle("-fx-background-color: orange"); setTextFill(Color.WHITE); } else { setStyle(""); setTextFill(Color.BLACK); } } } } } ); break;
                            case 12: column.setCellFactory(param -> new TableCell<TemperatureModel, Integer>() { @Override protected void updateItem(Integer item, boolean empty) { super.updateItem(item, empty); if (item == null || empty) { setText(null); setStyle(""); } else { setText(String.valueOf(item)); int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue(); SimpleIntegerProperty temp12P = param.getTableView().getItems().get(currentIndex).temp12Property(); if (temp12P != null) { int temp = param.getTableView().getItems().get(currentIndex).getTemp12(); int high = param.getTableView().getItems().get(currentIndex).getHigh12() - proActiveVal; int exce = param.getTableView().getItems().get(currentIndex).getExce12() - proActiveVal; if (temp >= exce) { setStyle("-fx-background-color: red"); setTextFill(Color.WHITE); } else if (temp >= high) { setStyle("-fx-background-color: orange"); setTextFill(Color.WHITE); } else { setStyle(""); setTextFill(Color.BLACK); } } } } } ); break;
                            case 13: column.setCellFactory(param -> new TableCell<TemperatureModel, Integer>() { @Override protected void updateItem(Integer item, boolean empty) { super.updateItem(item, empty); if (item == null || empty) { setText(null); setStyle(""); } else { setText(String.valueOf(item)); int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue(); SimpleIntegerProperty temp13P = param.getTableView().getItems().get(currentIndex).temp13Property(); if (temp13P != null) { int temp = param.getTableView().getItems().get(currentIndex).getTemp13(); int high = param.getTableView().getItems().get(currentIndex).getHigh13() - proActiveVal; int exce = param.getTableView().getItems().get(currentIndex).getExce13() - proActiveVal; if (temp >= exce) { setStyle("-fx-background-color: red"); setTextFill(Color.WHITE); } else if (temp >= high) { setStyle("-fx-background-color: orange"); setTextFill(Color.WHITE); } else { setStyle(""); setTextFill(Color.BLACK); } } } } } ); break;
                            case 14: column.setCellFactory(param -> new TableCell<TemperatureModel, Integer>() { @Override protected void updateItem(Integer item, boolean empty) { super.updateItem(item, empty); if (item == null || empty) { setText(null); setStyle(""); } else { setText(String.valueOf(item)); int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue(); SimpleIntegerProperty temp14P = param.getTableView().getItems().get(currentIndex).temp14Property(); if (temp14P != null) { int temp = param.getTableView().getItems().get(currentIndex).getTemp14(); int high = param.getTableView().getItems().get(currentIndex).getHigh14() - proActiveVal; int exce = param.getTableView().getItems().get(currentIndex).getExce14() - proActiveVal; if (temp >= exce) { setStyle("-fx-background-color: red"); setTextFill(Color.WHITE); } else if (temp >= high) { setStyle("-fx-background-color: orange"); setTextFill(Color.WHITE); } else { setStyle(""); setTextFill(Color.BLACK); } } } } } ); break;
                            case 15: column.setCellFactory(param -> new TableCell<TemperatureModel, Integer>() { @Override protected void updateItem(Integer item, boolean empty) { super.updateItem(item, empty); if (item == null || empty) { setText(null); setStyle(""); } else { setText(String.valueOf(item)); int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue(); SimpleIntegerProperty temp15P = param.getTableView().getItems().get(currentIndex).temp15Property(); if (temp15P != null) { int temp = param.getTableView().getItems().get(currentIndex).getTemp15(); int high = param.getTableView().getItems().get(currentIndex).getHigh15() - proActiveVal; int exce = param.getTableView().getItems().get(currentIndex).getExce15() - proActiveVal; if (temp >= exce) { setStyle("-fx-background-color: red"); setTextFill(Color.WHITE); } else if (temp >= high) { setStyle("-fx-background-color: orange"); setTextFill(Color.WHITE); } else { setStyle(""); setTextFill(Color.BLACK); } } } } } ); break;
                            case 16: column.setCellFactory(param -> new TableCell<TemperatureModel, Integer>() { @Override protected void updateItem(Integer item, boolean empty) { super.updateItem(item, empty); if (item == null || empty) { setText(null); setStyle(""); } else { setText(String.valueOf(item)); int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue(); SimpleIntegerProperty temp16P = param.getTableView().getItems().get(currentIndex).temp16Property(); if (temp16P != null) { int temp = param.getTableView().getItems().get(currentIndex).getTemp16(); int high = param.getTableView().getItems().get(currentIndex).getHigh16() - proActiveVal; int exce = param.getTableView().getItems().get(currentIndex).getExce16() - proActiveVal; if (temp >= exce) { setStyle("-fx-background-color: red"); setTextFill(Color.WHITE); } else if (temp >= high) { setStyle("-fx-background-color: orange"); setTextFill(Color.WHITE); } else { setStyle(""); setTextFill(Color.BLACK); } } } } } ); break;
                            case 17: column.setCellFactory(param -> new TableCell<TemperatureModel, Integer>() { @Override protected void updateItem(Integer item, boolean empty) { super.updateItem(item, empty); if (item == null || empty) { setText(null); setStyle(""); } else { setText(String.valueOf(item)); int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue(); SimpleIntegerProperty temp17P = param.getTableView().getItems().get(currentIndex).temp17Property(); if (temp17P != null) { int temp = param.getTableView().getItems().get(currentIndex).getTemp17(); int high = param.getTableView().getItems().get(currentIndex).getHigh17() - proActiveVal; int exce = param.getTableView().getItems().get(currentIndex).getExce17() - proActiveVal; if (temp >= exce) { setStyle("-fx-background-color: red"); setTextFill(Color.WHITE); } else if (temp >= high) { setStyle("-fx-background-color: orange"); setTextFill(Color.WHITE); } else { setStyle(""); setTextFill(Color.BLACK); } } } } } ); break;
                            case 18: column.setCellFactory(param -> new TableCell<TemperatureModel, Integer>() { @Override protected void updateItem(Integer item, boolean empty) { super.updateItem(item, empty); if (item == null || empty) { setText(null); setStyle(""); } else { setText(String.valueOf(item)); int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue(); SimpleIntegerProperty temp18P = param.getTableView().getItems().get(currentIndex).temp18Property(); if (temp18P != null) { int temp = param.getTableView().getItems().get(currentIndex).getTemp18(); int high = param.getTableView().getItems().get(currentIndex).getHigh18() - proActiveVal; int exce = param.getTableView().getItems().get(currentIndex).getExce18() - proActiveVal; if (temp >= exce) { setStyle("-fx-background-color: red"); setTextFill(Color.WHITE); } else if (temp >= high) { setStyle("-fx-background-color: orange"); setTextFill(Color.WHITE); } else { setStyle(""); setTextFill(Color.BLACK); } } } } } ); break;
                            case 19: column.setCellFactory(param -> new TableCell<TemperatureModel, Integer>() { @Override protected void updateItem(Integer item, boolean empty) { super.updateItem(item, empty); if (item == null || empty) { setText(null); setStyle(""); } else { setText(String.valueOf(item)); int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue(); SimpleIntegerProperty temp19P = param.getTableView().getItems().get(currentIndex).temp19Property(); if (temp19P != null) { int temp = param.getTableView().getItems().get(currentIndex).getTemp19(); int high = param.getTableView().getItems().get(currentIndex).getHigh19() - proActiveVal; int exce = param.getTableView().getItems().get(currentIndex).getExce19() - proActiveVal; if (temp >= exce) { setStyle("-fx-background-color: red"); setTextFill(Color.WHITE); } else if (temp >= high) { setStyle("-fx-background-color: orange"); setTextFill(Color.WHITE); } else { setStyle(""); setTextFill(Color.BLACK); } } } } } ); break;
                            case 20: column.setCellFactory(param -> new TableCell<TemperatureModel, Integer>() { @Override protected void updateItem(Integer item, boolean empty) { super.updateItem(item, empty); if (item == null || empty) { setText(null); setStyle(""); } else { setText(String.valueOf(item)); int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue(); SimpleIntegerProperty temp20P = param.getTableView().getItems().get(currentIndex).temp20Property(); if (temp20P != null) { int temp = param.getTableView().getItems().get(currentIndex).getTemp20(); int high = param.getTableView().getItems().get(currentIndex).getHigh20() - proActiveVal; int exce = param.getTableView().getItems().get(currentIndex).getExce20() - proActiveVal; if (temp >= exce) { setStyle("-fx-background-color: red"); setTextFill(Color.WHITE); } else if (temp >= high) { setStyle("-fx-background-color: orange"); setTextFill(Color.WHITE); } else { setStyle(""); setTextFill(Color.BLACK); } } } } } ); break;
                            case 21: column.setCellFactory(param -> new TableCell<TemperatureModel, Integer>() { @Override protected void updateItem(Integer item, boolean empty) { super.updateItem(item, empty); if (item == null || empty) { setText(null); setStyle(""); } else { setText(String.valueOf(item)); int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue(); SimpleIntegerProperty temp21P = param.getTableView().getItems().get(currentIndex).temp21Property(); if (temp21P != null) { int temp = param.getTableView().getItems().get(currentIndex).getTemp21(); int high = param.getTableView().getItems().get(currentIndex).getHigh21() - proActiveVal; int exce = param.getTableView().getItems().get(currentIndex).getExce21() - proActiveVal; if (temp >= exce) { setStyle("-fx-background-color: red"); setTextFill(Color.WHITE); } else if (temp >= high) { setStyle("-fx-background-color: orange"); setTextFill(Color.WHITE); } else { setStyle(""); setTextFill(Color.BLACK); } } } } } ); break;
                        }
                    tableView.getColumns().add(column);
                }
            //Filter capability
            FilteredList<TemperatureModel> filteredData = new FilteredList<>(modelData, p -> true);
            filterTextEntry.textProperty().addListener((observable, oldValue, newValue) ->
                    filteredData.setPredicate(model -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        String lowerCaseFilter = newValue.toLowerCase();

                        if (model.getSiteId().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (model.getIpAddress().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        return false;
                    }));

            SortedList<TemperatureModel> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedData);
        }
    }

    private String getExcelColumnName(int columnNumber) {
        int dividend = columnNumber;
        String columnName = "";
        int modulo;

        while (dividend > 0) {
            modulo = (dividend - 1) % 26;
            columnName = String.format("%s%s", (char) (65 + modulo), columnName);
            dividend = (dividend - modulo) / 26;
        }

        return columnName;
    }

}