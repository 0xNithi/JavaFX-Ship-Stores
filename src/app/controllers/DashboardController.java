/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

/**
 * FXML Controller class
 *
 * @author bossn
 */
public class DashboardController implements Initializable {

    @FXML
    private PieChart pieChart;
    @FXML
    private BarChart<String, Integer> barChart;
    @FXML
    private BarChart<String, Integer> barChart2;
    @FXML
    private PieChart pieChart2;

    private final Map<String, Integer> pieData = new HashMap<String, Integer>();
    private final Map<String, Integer> pieData2 = new HashMap<String, Integer>();
    private final Map<String, HashMap<String, Integer>> barData = new HashMap<String, HashMap<String, Integer>>();
    private final Map<String, HashMap<String, Integer>> barData2 = new HashMap<String, HashMap<String, Integer>>();
    private XYChart.Series[] dataSeries;
    private XYChart.Series[] dataSeries2;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {

            File fin = new File("dist/data/suppliers.dat");
            FileInputStream fis = new FileInputStream(fin);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            for (Object line : br.lines().toArray()) {

                String[] cols = line.toString().split(",");

                if (this.pieData.get(cols[2]) != null) {

                    this.pieData.put(cols[2], this.pieData.get(cols[2]) + Integer.parseInt(cols[4]));

                } else {

                    this.pieData.put(cols[2], Integer.parseInt(cols[4]));

                }

                if (this.barData.get(cols[1]) != null) {
                    Map<String, Integer> map = this.barData.get(cols[1]);
                    if (map.get(cols[2]) != null) {

                        map.put(cols[2], Integer.parseInt(cols[4]) + map.get(cols[2]));
                        this.barData.put(cols[1], (HashMap<String, Integer>) map);

                    } else {

                        map.put(cols[2], Integer.parseInt(cols[4]));
                        this.barData.put(cols[1], (HashMap<String, Integer>) map);

                    }

                } else {

                    this.barData.put(cols[1], new HashMap<String, Integer>() {
                        {
                            put(cols[2], Integer.parseInt(cols[4]));
                        }
                    });

                }

            }

            fin = new File("dist/data/administration.dat");
            fis = new FileInputStream(fin);
            br = new BufferedReader(new InputStreamReader(fis));

            for (Object line : br.lines().toArray()) {

                String[] cols = line.toString().split(",");

                if (this.pieData2.get(cols[4]) != null) {

                    this.pieData2.put(cols[4], this.pieData2.get(cols[4]) + 1);

                } else {

                    this.pieData2.put(cols[4], 1);

                }
                
                if (this.barData2.get(cols[4]) != null) {
                    Map<String, Integer> map = this.barData2.get(cols[4]);
                    if (map.get(cols[2]) != null) {

                        map.put(cols[2], Integer.parseInt(cols[3]) + map.get(cols[2]));
                        this.barData2.put(cols[4], (HashMap<String, Integer>) map);

                    } else {

                        map.put(cols[2], Integer.parseInt(cols[3]));
                        this.barData2.put(cols[4], (HashMap<String, Integer>) map);

                    }

                } else {

                    this.barData2.put(cols[4], new HashMap<String, Integer>() {
                        {
                            put(cols[2], Integer.parseInt(cols[3]));
                        }
                    });

                }

            }
            
            br.close();
            fis.close();

            this.pieData.keySet().forEach((str) -> {

                this.pieChart.getData().add(new PieChart.Data(str, this.pieData.get(str)));

            });
            this.pieData2.keySet().forEach((str) -> {

                this.pieChart2.getData().add(new PieChart.Data(str, this.pieData2.get(str)));

            });

            this.dataSeries = new XYChart.Series[this.barData.size()];
            for (int i = 0; i < this.barData.size(); i++) {

                ArrayList<Object> arr = new ArrayList<>(this.barData.keySet());
                Object obj = arr.get(i);

                this.dataSeries[i] = new XYChart.Series();
                this.dataSeries[i].setName((String) obj);

                Map<String, Integer> map = this.barData.get(obj);
                for (Map.Entry<String, Integer> entry : map.entrySet()) {

                    this.dataSeries[i].getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

                }

                this.barChart.getData().add(this.dataSeries[i]);

            }
            this.dataSeries2 = new XYChart.Series[this.barData2.size()];
            for (int i = 0; i < this.barData2.size(); i++) {

                ArrayList<Object> arr = new ArrayList<>(this.barData2.keySet());
                Object obj = arr.get(i);

                this.dataSeries2[i] = new XYChart.Series();
                this.dataSeries2[i].setName((String) obj);

                Map<String, Integer> map = this.barData2.get(obj);
                for (Map.Entry<String, Integer> entry : map.entrySet()) {

                    this.dataSeries2[i].getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

                }

                this.barChart2.getData().add(this.dataSeries2[i]);

            }

        } catch (FileNotFoundException ex) {

            Logger.getLogger(SuppliersController.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {

            Logger.getLogger(SuppliersController.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

}
