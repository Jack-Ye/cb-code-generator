package com.chenbang.intellij.plugin.ui;

import com.chenbang.intellij.plugin.api.controller.CBGenerator;
import com.chenbang.intellij.plugin.api.jdbc.meta.table.TableInfo;
import com.chenbang.intellij.plugin.model.TableDelegate;
import com.chenbang.intellij.plugin.util.JTextFieldHintListener;
import com.google.common.base.Strings;
import com.intellij.database.psi.DbTable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainUI extends JFrame {


    final private PsiElement[] psiElements;

    final private JTextField projectNameField = new JTextField(15);
    final private TextFieldWithBrowseButton basePathField = new TextFieldWithBrowseButton();
    final private JBTextField packageField = new JBTextField(20);
    final private JTextField moduleField = new JTextField(20);
    final private JTextField tableNameField = new JTextField(15);
    final private JTextField tablePrefixField = new JTextField(10);


    public MainUI(AnActionEvent anActionEvent) throws HeadlessException {
        Project project = anActionEvent.getData(PlatformDataKeys.PROJECT);
        this.psiElements = anActionEvent.getData(LangDataKeys.PSI_ELEMENT_ARRAY);
        if (project == null || psiElements == null || psiElements.length == 0 ) {
            return;
        }


        setTitle("宸邦代码生成工具");
        setPreferredSize(new Dimension(780, 420));//设置大小
        setLocation(300, 100);
        pack();
        setVisible(true);
        JButton buttonOK = new JButton("生成DAO");
        getRootPane().setDefaultButton(buttonOK);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        PsiElement psiElement = psiElements[0];
        TableDelegate tableInfo = new TableDelegate((DbTable) psiElement);
        String tableName = tableInfo.getTableName();
        String projectFolder = project.getBasePath();


        JPanel contentPanel = new JBPanel<>();
        contentPanel.setBorder(JBUI.Borders.empty(5));
        contentPanel.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 3, 3));//主要设置显示在这里
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(JBUI.Borders.empty(10, 30, 5, 40));

        //=======================================第一行 start===============================================

        JPanel line1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        //项目名
        JPanel projectPanel = new JPanel();
        projectPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        projectPanel.add(new JLabel("项目名(小写):"));
        projectPanel.add(projectNameField);

        //项目根目录
        JPanel basePathPanel = new JPanel();
        basePathPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
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

        //==========================================第二行 start================================================
        JPanel line2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        //包名
        JPanel packagePanel = new JPanel();
        packagePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        packagePanel.add(new JLabel("包名:"));
        packagePanel.add(packageField);
        packageField.setText("com.chenbang");

        //子模块
        JPanel modulePanel = new JPanel();
        modulePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        modulePanel.add(new JLabel("子模块:"));
        modulePanel.add(moduleField);
        moduleField.setText("api,admin,mobile,web");


        line2Panel.add(packagePanel);
        line2Panel.add(modulePanel);
        //==========================================第二行 end================================================

        //==========================================第三行 start================================================

        JPanel line3Panel = new JPanel(new FlowLayout(FlowLayout.LEFT)); //表名 表前缀


        //表名字段
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel tableLabel = new JLabel("表名:");
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


        //==========================================第三行 end================================================

        topPanel.add(line1Panel);
        topPanel.add(line2Panel);
        topPanel.add(line3Panel);
        mainPanel.add(topPanel);


        JPanel bottomPanel = new JPanel();//确认和取消按钮
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton buttonProject = new JButton("创建项目");
        bottomPanel.add(buttonProject);
        bottomPanel.add(buttonOK);
        JButton buttonCancel = new JButton("取消");
        bottomPanel.add(buttonCancel);

        JPanel panelLeft = new JPanel();
        panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
        this.getContentPane().add(Box.createVerticalStrut(10)); //采用x布局时，添加固定宽度组件隔开

        contentPanel.add(mainPanel, BorderLayout.CENTER);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(contentPanel);


        buttonOK.addActionListener(e -> onDAO());

        buttonProject.addActionListener(e -> onProject());

        buttonCancel.addActionListener(e -> onCancel());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPanel.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onDAO() {
        try {

            if (Strings.isNullOrEmpty(projectNameField.getText()) ||
                    Strings.isNullOrEmpty(basePathField.getText()) ||
                    Strings.isNullOrEmpty(packageField.getText()) ||
                    Strings.isNullOrEmpty(tableNameField.getText())) {
                Messages.showMessageDialog("项目名、根目录、包名或表名为空！", "提示", Messages.getInformationIcon());
                return;
            }

            dispose();
            CBGenerator.generateDAO(getForm());
            Messages.showMessageDialog("DAO生成成功！", "提示", Messages.getInformationIcon());

        } catch (Exception e1) {
            Messages.showMessageDialog("DAO生成异常：" + e1.getMessage(), "提示", Messages.getInformationIcon());
        } finally {
            dispose();
        }
    }

    private void onProject() {
        try {
            if (Strings.isNullOrEmpty(projectNameField.getText()) ||
                    Strings.isNullOrEmpty(basePathField.getText()) ||
                    Strings.isNullOrEmpty(packageField.getText()) ||
                    Strings.isNullOrEmpty(moduleField.getText())) {
                Messages.showMessageDialog("项目名、根目录、包名或模块名template为空！", "提示", Messages.getInformationIcon());
                return;
            }

            CBGenerator.generateProject(getForm());
            Messages.showMessageDialog("项目生成成功！", "提示", Messages.getInformationIcon());
        } catch (Exception e1) {
            Messages.showMessageDialog("项目生成异常：" + e1.getMessage(), "提示", Messages.getInformationIcon());
        }
    }

    private void onCancel() {
        dispose();
    }


    private CBGenerator.Form getForm() {
        CBGenerator.Form cbgForm = new CBGenerator.Form();
        cbgForm.setProjectName(projectNameField.getText());
        cbgForm.setBaseDirName(basePathField.getText());
        cbgForm.setBasePackageName(packageField.getText());
        cbgForm.setSubprojectNames(moduleField.getText());
        cbgForm.setTableNames(tableNameField.getText());
        cbgForm.setTablePrefix(tablePrefixField.getText());
        if (psiElements.length == 1) {
            TableDelegate tableDelegate = new TableDelegate((DbTable) psiElements[0]);
            cbgForm.setTableInfoList(Collections.singletonList(tableDelegate.getTableInfo()));
        } else {
            List<TableInfo> tableInfoList = new ArrayList<>();
            for (PsiElement psiElement : psiElements) {
                TableDelegate tableDelegate = new TableDelegate((DbTable) psiElement);
                tableInfoList.add(tableDelegate.getTableInfo());
            }
            cbgForm.setTableInfoList(tableInfoList);
        }
        return cbgForm;
    }
}
