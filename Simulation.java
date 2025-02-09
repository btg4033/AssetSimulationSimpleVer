/*
 * Github：https://github.com/btg4033/AssetSimulationSimpleVer.git
 */


package 自主制作_資産シミュレーション簡易版;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class Simulation extends JFrame implements ActionListener {

    private JTextField ageField = new JTextField(15);
    private JTextField incomeField = new JTextField(15);
    private JTextField currentSavingsField = new JTextField(15);
    private JTextField rentField = new JTextField(15);
    private JTextField foodField = new JTextField(15);
    private JTextField insuranceField = new JTextField(15);
    private JTextField otherExpensesField = new JTextField(15);

    private JButton saveButton = new JButton("データ保存");
    private JButton detailedDisplayButton = new JButton("シミュレーション");
    private JButton formulaDisplayButton = new JButton("計算式表示");
    private JButton loadButton = new JButton("データ読込");

    // コンストラクタ
    Simulation() {
        setLayout(new BorderLayout());

        // タブペイン
        JTabbedPane tabbedPane = new JTabbedPane();

        // 収支情報パネル
        JPanel agePanel = createInputPanel("年齢: ", ageField);
        JPanel incomePanel = createInputPanel("年収 (単位: 円): ", incomeField);
        JPanel currentSavingsPanel = createInputPanel("現在の貯蓄額 (単位: 円): ", currentSavingsField);
        JPanel rentPanel = createInputPanel("月々の家賃 (単位: 円): ", rentField);
        JPanel foodPanel = createInputPanel("月々の食費 (単位: 円): ", foodField);
        JPanel insurancePanel = createInputPanel("月々の保険料 (単位: 円): ", insuranceField);
        JPanel otherExpensesPanel = createInputPanel("月々のその他出費 (単位: 円): ", otherExpensesField);

        JPanel incomeInfoPanel = new JPanel(new GridLayout(7, 1));
        incomeInfoPanel.add(agePanel);
        incomeInfoPanel.add(incomePanel);
        incomeInfoPanel.add(currentSavingsPanel);
        incomeInfoPanel.add(rentPanel);
        incomeInfoPanel.add(foodPanel);
        incomeInfoPanel.add(insurancePanel);
        incomeInfoPanel.add(otherExpensesPanel);

        // 設定/シミュレーションパネル
        JPanel buttonPanel = new JPanel(new FlowLayout());
        saveButton.addActionListener(this);
        detailedDisplayButton.addActionListener(this);
        formulaDisplayButton.addActionListener(this);
        loadButton.addActionListener(this);
        buttonPanel.add(saveButton);
        buttonPanel.add(detailedDisplayButton);
        buttonPanel.add(formulaDisplayButton);
        buttonPanel.add(loadButton);

        JPanel settingsPanel = new JPanel(new BorderLayout());
        settingsPanel.add(buttonPanel, BorderLayout.CENTER);

        // タブペインに追加
        tabbedPane.addTab("収支情報", incomeInfoPanel);
        tabbedPane.addTab("設定/シミュレーション", settingsPanel);

        // フレームに追加
        add(tabbedPane, BorderLayout.CENTER);

        // フレーム設定
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setVisible(true);
    }

    private JPanel createInputPanel(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel(labelText));
        panel.add(textField);
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == saveButton) {
            try {
                StringBuilder jsonBuilder = new StringBuilder();
                jsonBuilder.append("{");
                jsonBuilder.append("\"年齢\": \"").append(ageField.getText()).append("\", ");
                jsonBuilder.append("\"年収\": \"").append(incomeField.getText()).append("\", ");
                jsonBuilder.append("\"現在の貯蓄額\": \"").append(currentSavingsField.getText()).append("\", ");
                jsonBuilder.append("\"月々の家賃\": \"").append(rentField.getText()).append("\", ");
                jsonBuilder.append("\"月々の食費\": \"").append(foodField.getText()).append("\", ");
                jsonBuilder.append("\"月々の保険料\": \"").append(insuranceField.getText()).append("\", ");
                jsonBuilder.append("\"月々のその他出費\": \"").append(otherExpensesField.getText()).append("\"");
                jsonBuilder.append("}");

                try (FileWriter file = new FileWriter("simulationData.json")) {
                    file.write(jsonBuilder.toString());
                    file.flush();
                    System.out.println("データが simulationData.json に保存されました。");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == loadButton) {
            try (BufferedReader reader = new BufferedReader(new FileReader("simulationData.json"))) {
                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                String jsonString = jsonBuilder.toString();

                ageField.setText(jsonString.split("\"年齢\": \"")[1].split("\"")[0]);
                incomeField.setText(jsonString.split("\"年収\": \"")[1].split("\"")[0]);
                currentSavingsField.setText(jsonString.split("\"現在の貯蓄額\": \"")[1].split("\"")[0]);
                rentField.setText(jsonString.split("\"月々の家賃\": \"")[1].split("\"")[0]);
                foodField.setText(jsonString.split("\"月々の食費\": \"")[1].split("\"")[0]);
                insuranceField.setText(jsonString.split("\"月々の保険料\": \"")[1].split("\"")[0]);
                otherExpensesField.setText(jsonString.split("\"月々のその他出費\": \"")[1].split("\"")[0]);

                System.out.println("保存されたデータを読み込みました。");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == detailedDisplayButton) {
            try {
                double income = Double.parseDouble(incomeField.getText());
                double currentSavings = Double.parseDouble(currentSavingsField.getText());
                double rent = Double.parseDouble(rentField.getText());
                double food = Double.parseDouble(foodField.getText());
                double insurance = Double.parseDouble(insuranceField.getText());
                double otherExpenses = Double.parseDouble(otherExpensesField.getText());
                int currentAge = Integer.parseInt(ageField.getText());

                double annualSavings = income - (rent * 12 + food * 12 + insurance * 12 + otherExpenses * 12);
                int remainingYears = 65 - currentAge;

                double futureSavings = currentSavings;
                for (int i = 0; i < remainingYears; i++) {
                    futureSavings += annualSavings;
                }

                JFrame resultFrame = new JFrame("シミュレーション結果");
                resultFrame.setLayout(new GridLayout(0, 1));
                resultFrame.add(new JLabel("年間貯蓄額: " + String.format("%,.0f", annualSavings) + " 円"));
                resultFrame.add(new JLabel("退職残り年数: " + remainingYears + " 年"));
                resultFrame.add(new JLabel("65歳時の貯蓄額: " + String.format("%,.0f", futureSavings) + " 円"));

                resultFrame.setSize(400, 200);
                resultFrame.setVisible(true);
            } catch (NumberFormatException e) {
                JFrame errorFrame = new JFrame("エラー");
                errorFrame.add(new JLabel("入力にエラーがあります。すべてのフィールドを正しく入力してください。"));
                errorFrame.setSize(400, 100);
                errorFrame.setVisible(true);
            }
        } else if (ae.getSource() == formulaDisplayButton) {
            JFrame formulaFrame = new JFrame("計算式表示");
            formulaFrame.setLayout(new GridLayout(0, 1));

            formulaFrame.add(new JLabel("年間貯蓄額 = 年収 - (家賃 + 食費 + 保険料 + その他出費) × 12 (単位: 円)"));
            formulaFrame.add(new JLabel("退職残り年数 = 65 - 現在の年齢"));
            formulaFrame.add(new JLabel("65歳時の貯蓄額 = 現在の貯蓄額 + 毎年の貯蓄額 (単位: 円)"));

            formulaFrame.setSize(400, 150);
            formulaFrame.setVisible(true);
        }
    }

    public static void main(String[] args) {
        new Simulation();
    }
}