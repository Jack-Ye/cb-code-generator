package com.chenbang.intellij.plugin.ui;

import com.chenbang.intellij.plugin.model.Config;
import com.chenbang.intellij.plugin.model.TableDelegate;
import com.chenbang.intellij.plugin.setting.PersistentConfig;
import com.chenbang.intellij.plugin.util.JTextFieldHintListener;
import com.chenbang.intellij.plugin.util.StringUtils;
import com.intellij.database.psi.DbTable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.psi.PsiElement;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

public class MainUI extends JFrame {


    final private AnActionEvent anActionEvent;
    final private Project project;
    final private PsiElement[] psiElements;

    final private JTextField projectNameField = new JTextField(15);
    final private TextFieldWithBrowseButton basePathField = new TextFieldWithBrowseButton();
    final private JBTextField packageField = new JBTextField(20);
    final private JTextField moduleField = new JTextField(20);
    final private JTextField tableNameField = new JTextField(15);
    final private JTextField tablePrefixField = new JTextField(10);



    public MainUI(AnActionEvent anActionEvent) throws HeadlessException {
        this.anActionEvent = anActionEvent;
        this.project = anActionEvent.getData(PlatformDataKeys.PROJECT);
        PersistentConfig persistentConfig = PersistentConfig.getInstance(project);
        this.psiElements = anActionEvent.getData(LangDataKeys.PSI_ELEMENT_ARRAY);
        if (project == null || psiElements == null || psiElements.length == 0 || persistentConfig == null) {
            return;
        }

        Map<String, Config> initConfigMap = persistentConfig.getInitConfig();


        setTitle("宸邦代码生成插件");
        setPreferredSize(new Dimension(800, 420));//设置大小
        setLocation(300, 100);
        pack();
        setVisible(true);
        JButton buttonOK = new JButton("确定");
        getRootPane().setDefaultButton(buttonOK);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        PsiElement psiElement = psiElements[0];
        TableDelegate tableInfo = new TableDelegate((DbTable) psiElement);
        String tableName = tableInfo.getTableName();
        String modelName = StringUtils.dbStringToCamelStyle(tableName);
        String primaryKey = "";
        if (tableInfo.getPrimaryKeys().size() > 0) {
            primaryKey = tableInfo.getPrimaryKeys().get(0);
        }
        String projectFolder = project.getBasePath();



        JPanel contentPanel = new JBPanel<>();
        contentPanel.setBorder(JBUI.Borders.empty(5));
        contentPanel.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 3, 3));//主要设置显示在这里
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(JBUI.Borders.empty(10, 30, 5, 40));

        //=======================================第一行 start===============================================

        JPanel line1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT)); //项目名 目录 包名

        //项目名
        JPanel projectPanel = new JPanel();
        projectPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        projectPanel.add(new JLabel("项目名(小写):"));
        projectPanel.add(projectNameField);

        //项目根目录
        JPanel basePathPanel = new JPanel();
        basePathPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        basePathPanel.add(new JLabel("项目根目录:"));
        basePathField.setTextFieldPreferredWidth(45);
        basePathField.setText(projectFolder);
        basePathField.addBrowseFolderListener(new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFileOrFolderDescriptor()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                super.actionPerformed(e);
                basePathField.setText(basePathField.getText().replaceAll("\\\\", "/"));
            }
        });
        basePathPanel.add(basePathField);



        line1Panel.add(projectPanel);
        line1Panel.add(basePathPanel);


        //==========================================第一行 end================================================

        JPanel line2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT)); //表名 表前缀

        //包名
        JPanel packagePanel = new JPanel();
        packagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        packagePanel.add(new JLabel("包名:"));
        packagePanel.add(packageField);

        //子模块
        JPanel modulePanel = new JPanel();
        modulePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        modulePanel.add(new JLabel("子模块:"));
        modulePanel.add(moduleField);
        moduleField.setText("api,admin,mobile,web");


        line2Panel.add(packagePanel);
        line2Panel.add(modulePanel);
        //==========================================第二行 start================================================

        JPanel line3Panel = new JPanel(new FlowLayout(FlowLayout.LEFT)); //表名 表前缀


        //表名字段
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel tableLabel = new JLabel("表名:");
        tableLabel.setSize(new Dimension(20, 30));
        tablePanel.add(tableLabel);
        if (psiElements.length > 1) {
            tableNameField.addFocusListener(new JTextFieldHintListener(tableNameField, tableName + "等多张表"));
        } else {
            tableNameField.setText(tableName);
        }
        tablePanel.add(tableNameField);

        //表前缀字段
        JPanel tablePrefixPanel = new JPanel();
        tablePrefixPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        tablePrefixPanel.add(new JLabel("表前缀（选填）:"));
        tablePrefixPanel.add(tablePrefixField);

        line3Panel.add(tablePanel);
        line3Panel.add(tablePrefixPanel);


        //==========================================第二行 end================================================

        topPanel.add(line1Panel);
        topPanel.add(line2Panel);
        topPanel.add(line3Panel);
        mainPanel.add(topPanel);


        JPanel bottomPanel = new JPanel();//确认和取消按钮
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(buttonOK);
        JButton buttonCancel = new JButton("取消");
        bottomPanel.add(buttonCancel);

//
//        JPanel panelLeft = new JPanel();
//        panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
//        this.getContentPane().add(Box.createVerticalStrut(10)); //采用x布局时，添加固定宽度组件隔开


        contentPanel.add(mainPanel, BorderLayout.CENTER);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);
//        contentPane.add(panelLeft, BorderLayout.WEST);

        setContentPane(contentPanel);


        buttonOK.addActionListener(e -> onOK());


        buttonCancel.addActionListener(e -> onCancel());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPanel.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        try {
            dispose();

            if (psiElements.length == 1) {

            } else {
                for (PsiElement psiElement : psiElements) {
                    TableDelegate tableInfo = new TableDelegate((DbTable) psiElement);
                    String tableName = tableInfo.getTableName();
                    String modelName = StringUtils.dbStringToCamelStyle(tableName);
                    String primaryKey = "";
                    if (tableInfo.getPrimaryKeys() != null && tableInfo.getPrimaryKeys().size() != 0) {
                        primaryKey = tableInfo.getPrimaryKeys().get(0);
                    }
                }

            }


        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            dispose();
        }
    }

    private void onCancel() {
        dispose();
    }
}
