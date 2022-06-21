package com.chenbang.intellij.plugin.ui;

import com.chenbang.intellij.plugin.generate.Generate;
import com.chenbang.intellij.plugin.model.Config;
import com.chenbang.intellij.plugin.model.TableDelegate;
import com.chenbang.intellij.plugin.setting.PersistentConfig;
import com.chenbang.intellij.plugin.util.JTextFieldHintListener;
import com.chenbang.intellij.plugin.util.StringUtils;
import com.intellij.database.psi.DbTable;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPackage;
import com.intellij.ui.components.JBLabel;
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
    private Config config;

    final private JTextField tableNameField = new JTextField(10);
    final private JBTextField modelPackageField = new JBTextField(12);
    final private JBTextField daoPackageField = new JBTextField(12);
    final private JBTextField xmlPackageField = new JBTextField(12);
    final private JTextField mapperNameField = new JTextField(10);
    final private JTextField modelNameField = new JTextField(10);
    final private JTextField keyField = new JTextField(10);

    final private TextFieldWithBrowseButton projectFolderBtn = new TextFieldWithBrowseButton();
    final private TextFieldWithBrowseButton modelFolderBtn = new TextFieldWithBrowseButton();
    final private TextFieldWithBrowseButton daoFolderBtn = new TextFieldWithBrowseButton();
    final private TextFieldWithBrowseButton xmlFolderBtn = new TextFieldWithBrowseButton();
    final private JTextField modelMvnField = new JBTextField(15);
    final private JTextField daoMvnField = new JBTextField(15);
    final private JTextField xmlMvnField = new JBTextField(15);

    final private JCheckBox offsetLimitBox = new JCheckBox("Page(分页)");
    final private JCheckBox commentBox = new JCheckBox("comment(实体注释)");
    final private JCheckBox overrideXMLBox = new JCheckBox("Overwrite-Xml");
    final private JCheckBox needToStringHashcodeEqualsBox = new JCheckBox("toString/hashCode/equals");
    final private JCheckBox useSchemaPrefixBox = new JCheckBox("Use-Schema(使用Schema前缀)");
    final private JCheckBox needForUpdateBox = new JCheckBox("Add-ForUpdate(select增加ForUpdate)");
    final private JCheckBox annotationDAOBox = new JCheckBox("Repository-Annotation(Repository注解)");
    final private JCheckBox useDAOExtendStyleBox = new JCheckBox("Parent-Interface(公共父接口)");
    final private JCheckBox jsr310SupportBox = new JCheckBox("JSR310: Date and Time API");
    final private JCheckBox annotationBox = new JCheckBox("JPA-Annotation(JPA注解)");
    final private JCheckBox useActualColumnNamesBox = new JCheckBox("Actual-Column(实际的列名)");
    final private JCheckBox useTableNameAliasBox = new JCheckBox("Use-Alias(启用别名查询)");
    final private JCheckBox useExampleBox = new JCheckBox("Use-Example");
    final private JCheckBox mysql_8Box = new JCheckBox("mysql_8");


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
        setPreferredSize(new Dimension(1200, 700));//设置大小
        setLocation(120, 100);
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


        if (psiElements.length > 1) {//多表时，只使用默认配置
            if (initConfigMap != null) {
                config = initConfigMap.get("initConfig");
            }
        } else {
            if (initConfigMap != null) {//单表时，优先使用已经存在的配置
                config = initConfigMap.get("initConfig");
            }
        }


        JPanel contentPane = new JBPanel<>();
        contentPane.setBorder(JBUI.Borders.empty(5));
        contentPane.setLayout(new BorderLayout());

        JPanel paneMain = new JPanel(new GridLayout(2, 1, 3, 3));//主要设置显示在这里
        JPanel paneMainTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        paneMainTop.setBorder(JBUI.Borders.empty(10, 30, 5, 40));

        JPanel paneMainTop1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel paneMainTop2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel paneMainTop3 = new JPanel(new GridLayout(4, 1, 3, 3));
        paneMainTop.add(paneMainTop1);
        paneMainTop.add(paneMainTop2);
        paneMainTop.add(paneMainTop3);


        JPanel paneLeft1 = new JPanel();
        paneLeft1.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel tablejLabel = new JLabel("table  name:");
        tablejLabel.setSize(new Dimension(20, 30));
        paneLeft1.add(tablejLabel);
        if (psiElements.length > 1) {
            tableNameField.addFocusListener(new JTextFieldHintListener(tableNameField, "eg:db_table"));
        } else {
            tableNameField.setText(tableName);
        }
        paneLeft1.add(tableNameField);

        JPanel paneLeft2 = new JPanel();
        paneLeft2.setLayout(new FlowLayout(FlowLayout.CENTER));
        paneLeft2.add(new JLabel("主键（选填）:"));
        if (psiElements.length > 1) {
            keyField.addFocusListener(new JTextFieldHintListener(keyField, "eg:primary key"));
        } else {
            keyField.setText(primaryKey);
        }
        paneLeft2.add(keyField);

        JPanel paneRight1 = new JPanel();
        paneRight1.setLayout(new FlowLayout(FlowLayout.CENTER));
        paneRight1.add(new JLabel("model   :"));
        if (psiElements.length > 1) {
            modelNameField.addFocusListener(new JTextFieldHintListener(modelNameField, "eg:DbTable"));
        } else {
            modelNameField.setText(modelName);
        }
        paneRight1.add(modelNameField);

        JPanel paneRight2 = new JPanel();
        paneRight2.setLayout(new FlowLayout(FlowLayout.CENTER));
        paneRight2.add(new JLabel("dao name:"));
        if (psiElements.length > 1) {
            if (config != null && !StringUtils.isEmpty(config.getDaoPostfix())) {
                mapperNameField.addFocusListener(new JTextFieldHintListener(mapperNameField, "eg:DbTable" + config.getDaoPostfix()));
            } else {
                mapperNameField.addFocusListener(new JTextFieldHintListener(mapperNameField, "eg:DbTable" + "Dao"));
            }
        } else {
            if (config != null && !StringUtils.isEmpty(config.getDaoPostfix())) {
                mapperNameField.setText(modelName + config.getDaoPostfix());
            } else {
                mapperNameField.setText(modelName + "Dao");
            }
        }
        paneRight2.add(mapperNameField);

        paneMainTop1.add(paneLeft1);
        paneMainTop1.add(paneLeft2);
        paneMainTop1.add(paneRight1);
        paneMainTop1.add(paneRight2);

        JPanel modelPackagePanel = new JPanel();
        modelPackagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JBLabel labelLeft4 = new JBLabel("Model package:");
        modelPackagePanel.add(labelLeft4);
        if (config != null && !StringUtils.isEmpty(config.getModelPackage())) {
            modelPackageField.setText(config.getModelPackage());
        } else {
            modelPackageField.setText("generator");
        }
        modelPackagePanel.add(modelPackageField);
        JButton packageBtn1 = new JButton("...");
        packageBtn1.addActionListener(actionEvent -> {
            final PackageChooserDialog chooser = new PackageChooserDialog("Choose Model Package", project);
            chooser.selectPackage(modelPackageField.getText());
            chooser.show();
            final PsiPackage psiPackage = chooser.getSelectedPackage();
            String packageName = psiPackage == null ? null : psiPackage.getQualifiedName();
            modelPackageField.setText(packageName);
            MainUI.this.toFront();
        });
        modelPackagePanel.add(packageBtn1);


        JPanel daoPackagePanel = new JPanel();
        daoPackagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel labelLeft5 = new JLabel("dao package:");
        daoPackagePanel.add(labelLeft5);


        if (config != null && !StringUtils.isEmpty(config.getDaoPackage())) {
            daoPackageField.setText(config.getDaoPackage());
        } else {
            daoPackageField.setText("generator");
        }
        daoPackagePanel.add(daoPackageField);

        JButton packageBtn2 = new JButton("...");
        packageBtn2.addActionListener(actionEvent -> {
            final PackageChooserDialog chooser = new PackageChooserDialog("Choose Dao Package", project);
            chooser.selectPackage(daoPackageField.getText());
            chooser.show();
            final PsiPackage psiPackage = chooser.getSelectedPackage();
            String packageName = psiPackage == null ? null : psiPackage.getQualifiedName();
            daoPackageField.setText(packageName);
            MainUI.this.toFront();
        });
        daoPackagePanel.add(packageBtn2);

        JPanel xmlPackagePanel = new JPanel();
        xmlPackagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel labelLeft6 = new JLabel("xml package:");
        xmlPackagePanel.add(labelLeft6);
        if (config != null && !StringUtils.isEmpty(config.getXmlPackage())) {
            xmlPackageField.setText(config.getXmlPackage());
        } else {
            xmlPackageField.setText("generator");
        }
        xmlPackagePanel.add(xmlPackageField);

        paneMainTop2.add(modelPackagePanel);
        paneMainTop2.add(daoPackagePanel);
        paneMainTop2.add(xmlPackagePanel);


        JPanel projectFolderPanel = new JPanel();
        projectFolderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel projectLabel = new JLabel("project folder:");
        projectFolderPanel.add(projectLabel);
        projectFolderBtn.setTextFieldPreferredWidth(45);
        if (config != null && !StringUtils.isEmpty(config.getProjectFolder())) {
            projectFolderBtn.setText(config.getProjectFolder());
        } else {
            projectFolderBtn.setText(projectFolder);
        }
        projectFolderBtn.addBrowseFolderListener(new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFileOrFolderDescriptor()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                super.actionPerformed(e);
                projectFolderBtn.setText(projectFolderBtn.getText().replaceAll("\\\\", "/"));
            }
        });
        projectFolderPanel.add(projectFolderBtn);
        JButton setProjectBtn = new JButton("Set-Project-Path");
        projectFolderPanel.add(setProjectBtn);


        JPanel modelFolderPanel = new JPanel();
        modelFolderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        modelFolderPanel.add(new JLabel("model  folder:"));

        modelFolderBtn.setTextFieldPreferredWidth(45);
        if (config != null && !StringUtils.isEmpty(config.getModelTargetFolder())) {
            modelFolderBtn.setText(config.getModelTargetFolder());
        } else {
            modelFolderBtn.setText(projectFolder);
        }
        modelFolderBtn.addBrowseFolderListener(new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFileOrFolderDescriptor()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                super.actionPerformed(e);
                modelFolderBtn.setText(modelFolderBtn.getText().replaceAll("\\\\", "/"));
            }
        });
        modelFolderPanel.add(modelFolderBtn);
        modelFolderPanel.add(new JLabel("mvn path:"));
        modelMvnField.setText("src/main/java");
        modelFolderPanel.add(modelMvnField);


        JPanel daoFolderPanel = new JPanel();
        daoFolderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        daoFolderPanel.add(new JLabel("dao     folder:"));
        daoFolderBtn.setTextFieldPreferredWidth(45);
        if (config != null && !StringUtils.isEmpty(config.getDaoTargetFolder())) {
            daoFolderBtn.setText(config.getDaoTargetFolder());
        } else {
            daoFolderBtn.setText(projectFolder);
        }
        daoFolderBtn.addBrowseFolderListener(new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFileOrFolderDescriptor()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                super.actionPerformed(e);
                daoFolderBtn.setText(daoFolderBtn.getText().replaceAll("\\\\", "/"));
            }
        });
        daoFolderPanel.add(daoFolderBtn);
        daoFolderPanel.add(new JLabel("mvn path:"));
        daoMvnField.setText("src/main/java");
        daoFolderPanel.add(daoMvnField);


        JPanel xmlFolderPanel = new JPanel();
        xmlFolderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        xmlFolderPanel.add(new JLabel("xml     folder:"));

        xmlFolderBtn.setTextFieldPreferredWidth(45);
        if (config != null && !StringUtils.isEmpty(config.getXmlTargetFolder())) {
            xmlFolderBtn.setText(config.getXmlTargetFolder());
        } else {
            xmlFolderBtn.setText(projectFolder);
        }
        xmlFolderBtn.addBrowseFolderListener(new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFileOrFolderDescriptor()) {
        });
        xmlFolderPanel.add(xmlFolderBtn);
        xmlFolderPanel.add(new JLabel("mvn path:"));
        xmlMvnField.setText("src/main/resources");
        xmlFolderPanel.add(xmlMvnField);

        paneMainTop3.add(projectFolderPanel);
        paneMainTop3.add(modelFolderPanel);
        paneMainTop3.add(daoFolderPanel);
        paneMainTop3.add(xmlFolderPanel);

        if (config == null) {
            offsetLimitBox.setSelected(true);
            commentBox.setSelected(true);
            overrideXMLBox.setSelected(true);
            needToStringHashcodeEqualsBox.setSelected(true);
            useSchemaPrefixBox.setSelected(true);
            useExampleBox.setSelected(true);

        } else {
            if (config.isOffsetLimit()) {
                offsetLimitBox.setSelected(true);
            }
            if (config.isComment()) {
                commentBox.setSelected(true);
            }

            if (config.isOverrideXML()) {
                overrideXMLBox.setSelected(true);
            }
            if (config.isNeedToStringHashcodeEquals()) {
                needToStringHashcodeEqualsBox.setSelected(true);
            }
            if (config.isUseSchemaPrefix()) {
                useSchemaPrefixBox.setSelected(true);
            }
            if (config.isNeedForUpdate()) {
                needForUpdateBox.setSelected(true);
            }
            if (config.isAnnotationDAO()) {
                annotationDAOBox.setSelected(true);
            }
            if (config.isUseDAOExtendStyle()) {
                useDAOExtendStyleBox.setSelected(true);
            }
            if (config.isJsr310Support()) {
                jsr310SupportBox.setSelected(true);
            }
            if (config.isAnnotation()) {
                annotationBox.setSelected(true);
            }
            if (config.isUseActualColumnNames()) {
                useActualColumnNamesBox.setSelected(true);
            }
            if (config.isUseTableNameAlias()) {
                useTableNameAliasBox.setSelected(true);
            }
            if (config.isUseExample()) {
                useExampleBox.setSelected(true);
            }
            if (config.isMysql_8()) {
                mysql_8Box.setSelected(true);
            }
        }


        JBPanel paneMainDown = new JBPanel(new GridLayout(5, 5, 5, 5));
        paneMainDown.setBorder(JBUI.Borders.empty(2, 80, 100, 40));

        paneMainDown.add(offsetLimitBox);
        paneMainDown.add(commentBox);
        paneMainDown.add(overrideXMLBox);
        paneMainDown.add(needToStringHashcodeEqualsBox);
        paneMainDown.add(useSchemaPrefixBox);
        paneMainDown.add(needForUpdateBox);
        paneMainDown.add(annotationDAOBox);
        paneMainDown.add(useDAOExtendStyleBox);
        paneMainDown.add(jsr310SupportBox);
        paneMainDown.add(annotationBox);
        paneMainDown.add(useActualColumnNamesBox);
        paneMainDown.add(useTableNameAliasBox);
        paneMainDown.add(useExampleBox);
        paneMainDown.add(mysql_8Box);

        paneMain.add(paneMainTop);
        paneMain.add(paneMainDown);


        JPanel paneBottom = new JPanel();//确认和取消按钮
        paneBottom.setLayout(new FlowLayout(2));
        paneBottom.add(buttonOK);
        JButton buttonCancel = new JButton("取消");
        paneBottom.add(buttonCancel);


        JPanel panelLeft = new JPanel();
        panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
        this.getContentPane().add(Box.createVerticalStrut(10)); //采用x布局时，添加固定宽度组件隔开


        contentPane.add(paneMain, BorderLayout.CENTER);
        contentPane.add(paneBottom, BorderLayout.SOUTH);
        contentPane.add(panelLeft, BorderLayout.WEST);

        setContentPane(contentPane);

        setProjectBtn.addActionListener(e -> {
            modelFolderBtn.setText(projectFolderBtn.getText());
            daoFolderBtn.setText(projectFolderBtn.getText());
            xmlFolderBtn.setText(projectFolderBtn.getText());
        });

        buttonOK.addActionListener(e -> onOK());


        buttonCancel.addActionListener(e -> onCancel());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        try {
            dispose();

            if (psiElements.length == 1) {
                Config generator_config = new Config();
                generator_config.setName(tableNameField.getText());
                generator_config.setTableName(tableNameField.getText());
                generator_config.setProjectFolder(projectFolderBtn.getText());

                generator_config.setModelPackage(modelPackageField.getText());
                generator_config.setModelTargetFolder(modelFolderBtn.getText());
                generator_config.setDaoPackage(daoPackageField.getText());
                generator_config.setDaoTargetFolder(daoFolderBtn.getText());
                generator_config.setXmlPackage(xmlPackageField.getText());
                generator_config.setXmlTargetFolder(xmlFolderBtn.getText());
                generator_config.setDaoName(mapperNameField.getText());
                generator_config.setModelName(modelNameField.getText());
                generator_config.setPrimaryKey(keyField.getText());

                generator_config.setOffsetLimit(offsetLimitBox.getSelectedObjects() != null);
                generator_config.setComment(commentBox.getSelectedObjects() != null);
                generator_config.setOverrideXML(overrideXMLBox.getSelectedObjects() != null);
                generator_config.setNeedToStringHashcodeEquals(needToStringHashcodeEqualsBox.getSelectedObjects() != null);
                generator_config.setUseSchemaPrefix(useSchemaPrefixBox.getSelectedObjects() != null);
                generator_config.setNeedForUpdate(needForUpdateBox.getSelectedObjects() != null);
                generator_config.setAnnotationDAO(annotationDAOBox.getSelectedObjects() != null);
                generator_config.setUseDAOExtendStyle(useDAOExtendStyleBox.getSelectedObjects() != null);
                generator_config.setJsr310Support(jsr310SupportBox.getSelectedObjects() != null);
                generator_config.setAnnotation(annotationBox.getSelectedObjects() != null);
                generator_config.setUseActualColumnNames(useActualColumnNamesBox.getSelectedObjects() != null);
                generator_config.setUseTableNameAlias(useTableNameAliasBox.getSelectedObjects() != null);
                generator_config.setUseExample(useExampleBox.getSelectedObjects() != null);
                generator_config.setMysql_8(mysql_8Box.getSelectedObjects() != null);

                generator_config.setModelMvnPath(modelMvnField.getText());
                generator_config.setDaoMvnPath(daoMvnField.getText());
                generator_config.setXmlMvnPath(xmlMvnField.getText());


                new Generate(generator_config).execute(anActionEvent);
            } else {
                for (PsiElement psiElement : psiElements) {
                    TableDelegate tableInfo = new TableDelegate((DbTable) psiElement);
                    String tableName = tableInfo.getTableName();
                    String modelName = StringUtils.dbStringToCamelStyle(tableName);
                    String primaryKey = "";
                    if (tableInfo.getPrimaryKeys() != null && tableInfo.getPrimaryKeys().size() != 0) {
                        primaryKey = tableInfo.getPrimaryKeys().get(0);
                    }
                    Config generator_config = new Config();
                    generator_config.setName(tableName);
                    generator_config.setTableName(tableName);
                    generator_config.setProjectFolder(projectFolderBtn.getText());

                    generator_config.setModelPackage(modelPackageField.getText());
                    generator_config.setModelTargetFolder(modelFolderBtn.getText());
                    generator_config.setDaoPackage(daoPackageField.getText());
                    generator_config.setDaoTargetFolder(daoFolderBtn.getText());
                    generator_config.setXmlPackage(xmlPackageField.getText());
                    generator_config.setXmlTargetFolder(xmlFolderBtn.getText());

                    if (this.config != null) {
                        generator_config.setDaoName(modelName + this.config.getDaoPostfix());
                    } else {
                        generator_config.setDaoName(modelName + "Dao");
                    }
                    generator_config.setModelName(modelName);
                    generator_config.setPrimaryKey(primaryKey);

                    generator_config.setOffsetLimit(offsetLimitBox.getSelectedObjects() != null);
                    generator_config.setComment(commentBox.getSelectedObjects() != null);
                    generator_config.setOverrideXML(overrideXMLBox.getSelectedObjects() != null);
                    generator_config.setNeedToStringHashcodeEquals(needToStringHashcodeEqualsBox.getSelectedObjects() != null);
                    generator_config.setUseSchemaPrefix(useSchemaPrefixBox.getSelectedObjects() != null);
                    generator_config.setNeedForUpdate(needForUpdateBox.getSelectedObjects() != null);
                    generator_config.setAnnotationDAO(annotationDAOBox.getSelectedObjects() != null);
                    generator_config.setUseDAOExtendStyle(useDAOExtendStyleBox.getSelectedObjects() != null);
                    generator_config.setJsr310Support(jsr310SupportBox.getSelectedObjects() != null);
                    generator_config.setAnnotation(annotationBox.getSelectedObjects() != null);
                    generator_config.setUseActualColumnNames(useActualColumnNamesBox.getSelectedObjects() != null);
                    generator_config.setUseTableNameAlias(useTableNameAliasBox.getSelectedObjects() != null);
                    generator_config.setUseExample(useExampleBox.getSelectedObjects() != null);
                    generator_config.setMysql_8(mysql_8Box.getSelectedObjects() != null);

                    generator_config.setModelMvnPath(modelMvnField.getText());
                    generator_config.setDaoMvnPath(daoMvnField.getText());
                    generator_config.setXmlMvnPath(xmlMvnField.getText());


                    new Generate(generator_config).execute(anActionEvent);
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
