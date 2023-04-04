package gui;

import calculation.*;
import picture.Picture;
import picture.Pixel;
import picture.graphPanels.GaussPanel;
import picture.graphPanels.GraphPanel;
import saver.CsvSaver;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

/*данный класс отвечает за отрисовывание рабочего окна. основа - библиотека
Swing*/
public class Frame extends JFrame {
    Picture picture = new Picture();
    JLabel imageLabel;
    JLabel xAxisLabel;
    JLabel yAxisLabel;
    DecimalFormat df = new DecimalFormat("#");
    Converter converter;
    ArrayList<File> selectedFiles;
    int selectedFileId = 0;
    JTextField fileNameField;
    JTextField saveField;
    String pathToSaveDirectory;
    CsvSaver csvSaver;
    JLabel rzOutputLabel;
    JLabel rMaxOutputLabel;
    JLabel rAOutputLabel;



        public Frame() {
            //отрисовка окна
            super("PlotMath");
            setPreferredSize(new Dimension(1600, 1000));
            setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            setVisible(true);
            setResizable(false);
            GridBagConstraints c = new GridBagConstraints();

            df.setMaximumFractionDigits(8);
            //отрисовка рабочей области
            JSplitPane splitPane = new JSplitPane();
            splitPane.setName("test");
            splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

            //лэйбл для изображения
            imageLabel  = new JLabel("",  SwingConstants.CENTER);
            imageLabel.setPreferredSize(new Dimension(500, 500));
            imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            imageLabel.addMouseListener(new MouseAdapter() {
                Pixel firstPixel = new Pixel();
                Pixel secondPixel = new Pixel();
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    picture.pressEventStart();
                    imageLabel.setIcon(new ImageIcon(picture.getDefaultPicture()));
                    imageLabel.setIcon(new ImageIcon(picture.recolorPixel(true)));
                    firstPixel.setCoordinates(e.getX(), e.getY());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                        imageLabel.setIcon(new ImageIcon(picture.recolorPixel(false)));
                        secondPixel.setCoordinates(e.getX(), e.getY());
                        Graphics g = picture.getCurrentPicture().getGraphics();
                        g.setColor(Color.RED);
                        g.drawLine(firstPixel.getX(), firstPixel.getY(), secondPixel.getX(), secondPixel.getY());
                        Icon icon = imageLabel.getIcon();
                    BufferedImage bi = new BufferedImage(
                            icon.getIconWidth(),
                            icon.getIconHeight(),
                            BufferedImage.TYPE_INT_RGB);
                    g = bi.createGraphics();
                    // paint the Icon to the BufferedImage.
                    icon.paintIcon(null, g, 0,0);
                    picture.setCurrentPicture(bi);
                    picture.setData();
                    g.dispose();
                }
            });

            //Лейблы масштабов изображения
            xAxisLabel = new JLabel();
            xAxisLabel.setPreferredSize(new Dimension(500, 500));
            xAxisLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            yAxisLabel = new JLabel();
            yAxisLabel.setPreferredSize(new Dimension(500, 500));
            yAxisLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            //Панель управления и отображения названия файлов
            fileNameField = new JTextField("Файл не выбран");
            fileNameField.setPreferredSize(new Dimension(400, 50));
            fileNameField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            fileNameField.setText("Файл не выбран");

            //Зона сохранения
            saveField = new JTextField("Директория для сохранения не выбрана");
            saveField.setPreferredSize(new Dimension(400, 50));
            saveField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            saveField.setText("Файл не выбран");

            JButton leftFileButton = new JButton("<--");
            leftFileButton.setPreferredSize(new Dimension(50, 50));
            leftFileButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (selectedFileId == 0) {
                        selectedFileId = selectedFiles.size() - 1;
                    } else {
                        selectedFileId--;
                    }
                    try {
                        picture.setPicture(selectedFiles.get(selectedFileId));
                        imageLabel.setIcon(new ImageIcon(picture.getDefaultPicture()));
                        fileNameField.setText(selectedFiles.get(selectedFileId).toPath().toString());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            JButton rightFileButton = new JButton("-->");
            rightFileButton.setPreferredSize(new Dimension(50, 50));
            rightFileButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (selectedFileId == selectedFiles.size()-1) {
                        selectedFileId = 0;
                    } else {
                        selectedFileId++;
                    }
                    try {
                        picture.setPicture(selectedFiles.get(selectedFileId));
                        imageLabel.setIcon(new ImageIcon(picture.getDefaultPicture()));
                        fileNameField.setText(selectedFiles.get(selectedFileId).toPath().toString());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            //Панель сохранения файлов в формат
            JButton chooseDirectoryToSaveButton = new JButton("Выбор Директорию");
            chooseDirectoryToSaveButton.setPreferredSize(new Dimension(150, 50));
            chooseDirectoryToSaveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser saveDirectory = new JFileChooser();
                    saveDirectory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int ret = saveDirectory.showDialog( null, "Выбрать директорию");
                    if (ret == JFileChooser.APPROVE_OPTION) {
                        pathToSaveDirectory = saveDirectory.getSelectedFile().getPath();
                        saveField.setText(pathToSaveDirectory);
                        try {
                            csvSaver = new CsvSaver(Path.of(saveDirectory.getSelectedFile().getPath()));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            });

            JButton saveButton = new JButton("Сохранить измерение");
            saveButton.setPreferredSize(new Dimension(200, 50));
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        csvSaver.addData(fileNameField.getText() + "," + rAOutputLabel.getText() + "," +
                                rzOutputLabel.getText() + "," + rMaxOutputLabel.getText());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            //создание панелей для отображения значений
            rzOutputLabel = LabelFactory.createLabel("Enter Data to calculate Rz");
            rzOutputLabel.setPreferredSize(new Dimension(400, 50));
            rMaxOutputLabel = LabelFactory.createLabel("Enter Data to calculate Rmax");
            rMaxOutputLabel.setPreferredSize(new Dimension(400, 50));
            rAOutputLabel = LabelFactory.createLabel("Enter Data to calculate Ra");
            rAOutputLabel.setPreferredSize(new Dimension(400, 50));
            JLabel rzLabel = LabelFactory.createLabel("Rz");
            rzLabel.setPreferredSize(new Dimension(150, 50));
            JLabel rMaxLabel = LabelFactory.createLabel("RMax");
            rMaxLabel.setPreferredSize(new Dimension(150, 50));
            JLabel rALabel = LabelFactory.createLabel("Ra");
            rALabel.setPreferredSize(new Dimension(150, 50));

            //кнопки сохранения данных в csv файл



            //кнопка для запуска алгоритмов расчёта параметров шероховатости
            JButton buttonCalculate = new JButton("Calculate");
            buttonCalculate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent press) {
                    Map<Integer, Pixel> pixelList = new HashMap<>(picture.getCurrentPixelMap());
                    converter = new Converter(pixelList);
                    List<Integer> values = converter.getMarkedDotsValuesArray();
                    rAOutputLabel.setText(BigDecimal.valueOf(RaCalculator.calculate(values)).toPlainString() + " мкм");
                    rMaxOutputLabel.setText(BigDecimal.valueOf(RMaxCalculator.calculate(values)).toPlainString()
                            + " мкм");
                    rzOutputLabel.setText(BigDecimal.valueOf(RzCalculator.calculate(values)).toPlainString() + " мкм");
                }
            });
            buttonCalculate.setPreferredSize(new Dimension(100, 100));

            JButton buttonGraph = new JButton("Show graph");
            buttonGraph.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent press) {
                    Map<Integer, Pixel> pixelList = new HashMap<>(picture.getCurrentPixelMap());
                    converter = new Converter(pixelList);
                    GraphPanel.createAndShow(converter.getMarkedDotsValuesArray());
                }
            });
            buttonGraph.setPreferredSize(new Dimension(110, 100));

            JButton gaussButton = new JButton("Gauss graph");
            gaussButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent press) {
                    List<Pixel> pixels = new ArrayList<>(picture.getDefaultPixelMap().values());
                    List<Integer> nums = new ArrayList<>();
                    for (Pixel pixel : pixels) {
                        nums.add(pixel.getValue());
                    }
                    GaussPanel.createAndShow(nums);
                }
            });
            //установка элементов в рабочую панель
            JPanel imagePanel = new JPanel();
            imagePanel.setPreferredSize(new Dimension(900, 800));
            imagePanel.setLayout(new GridBagLayout());
            c.fill = GridBagConstraints.CENTER;
            c.gridx = 0;
            c.gridy = 0;
            imagePanel.add(yAxisLabel, c);
            c.gridx = 1;
            c.gridy = 0;
            imagePanel.add(imageLabel, c);
            c.gridy = 1;
            imagePanel.add(xAxisLabel, c);
            c.gridy = 2;
            c.gridx = 1;
            imagePanel.add(fileNameField, c);
            c.gridx = 0;
            imagePanel.add(leftFileButton, c);
            c.gridx = 2;
            imagePanel.add(rightFileButton, c);

            JPanel outputContents = new JPanel();
            outputContents.setPreferredSize(new Dimension(100, 100));
            outputContents.setLayout(new GridBagLayout());
            c.weighty = 1;
            c.gridx = 0;
            c.gridy = 0;
            outputContents.add(rzLabel, c);
            c.weighty = 1;
            c.gridx = 1;
            outputContents.add(rzOutputLabel, c);
            c.gridx = 0;
            c.gridy = 1;
            outputContents.add(rMaxLabel, c);
            c.gridx = 1;
            outputContents.add(rMaxOutputLabel,c);
            c.gridx = 0;
            c.gridy = 2;
            outputContents.add(rALabel,c);
            c.gridx = 1;
            outputContents.add(rAOutputLabel,c);

            c.gridy = 3;
            c.gridx = 0;
            outputContents.add(chooseDirectoryToSaveButton, c);
            c.gridx = 1;
            outputContents.add(saveButton, c);

            c.gridy = 4;
            c.gridx = 1;
            outputContents.add(saveField, c);

            c.gridx = 0;
            c.gridy = 5;
            c.weighty = 1;
            outputContents.add(buttonCalculate, c);
            c.gridx = 1;
            outputContents.add(buttonGraph, c);
            c.gridx = 2;
            outputContents.add(gaussButton, c);

            splitPane.setLeftComponent(imagePanel);
            splitPane.setRightComponent(outputContents);

            setContentPane(splitPane);

            JMenuBar menuBar = new JMenuBar();
            menuBar.add(createFileMenu());
            menuBar.add(createSettingsMenu());
            setJMenuBar(menuBar);
            pack();

        }

    private JMenu createFileMenu() {
        // Создание выпадающего меню
        JMenu fileMenu = new JMenu("Файл");
        // Пункт меню "Открыть" с изображением
        JMenuItem open = new JMenuItem("Открыть файл");
        //Пункт меню с выбором директории
        JMenuItem openDir = new JMenuItem("Открыть директорию");
        // Пункт меню с выходом из программы
        JMenuItem exit = new JMenuItem(new ExitAction());
        fileMenu.add(open);
        // Добавление разделителя
        fileMenu.add(openDir);
        fileMenu.addSeparator();
        fileMenu.add(exit);


        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                //логика открытия
                JFileChooser fileOpen = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(".csv", "csv");
                fileOpen.setFileFilter(filter);
                fileOpen.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int ret = fileOpen.showDialog( null, "Открыть файл");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = fileOpen.getSelectedFile();
                    try {
                        imageLabel.setIcon(new ImageIcon(picture.setPicture(file)));
                        imageLabel.setPreferredSize(new Dimension(picture.getDefaultPicture().getWidth(),
                                picture.getDefaultPicture().getHeight()));
                        fileNameField.setText(file.toPath().toString());
                    } catch (IOException e) {
                        throw new RuntimeException("Ошибка выбора файла");
                    }
                    //построение масштаба
                    if (picture.getDefaultPicture().getHeight() < 300) {
                        xAxisLabel.setPreferredSize(new Dimension(picture.getDefaultPicture().getWidth() + 20,
                                picture.getDefaultPicture().getHeight()/2));
                        yAxisLabel.setPreferredSize(new Dimension(picture.getDefaultPicture().getWidth()/3,
                                picture.getDefaultPicture().getHeight() + 40));
                    } else {
                        xAxisLabel.setPreferredSize(new Dimension(picture.getDefaultPicture().getWidth() + 20,
                                picture.getDefaultPicture().getHeight()/7));
                        yAxisLabel.setPreferredSize(new Dimension(picture.getDefaultPicture().getWidth()/7,
                                picture.getDefaultPicture().getHeight() + 40));
                    }

                    yAxisLabel.setPreferredSize(new Dimension(picture.getDefaultPicture().getWidth()/5,
                          picture.getDefaultPicture().getHeight()));

                    xAxisLabel.setIcon(new ImageIcon(picture.drawXAxisImage()));
                    yAxisLabel.setIcon(new ImageIcon(picture.drawYAxisImage()));
                }
            }
        });

        openDir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                //логика открытия
                JFileChooser fileOpen = new JFileChooser();
                fileOpen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int ret = fileOpen.showDialog( null, "Открыть директорию");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    //получение файлов из выбранной директории
                    File directory = fileOpen.getSelectedFile();
                    //фильтр для получения только csv файлов
                    File[] filesFromDirectory = directory.listFiles();
                    //в потоке добавляем все файлы в лист файлов
                    selectedFiles = new ArrayList<>();

                    selectedFiles.addAll(Arrays.asList(filesFromDirectory));

                    try {
                        imageLabel.setIcon(new ImageIcon(picture.setPicture(selectedFiles.get(0))));
                        imageLabel.setPreferredSize(new Dimension(picture.getDefaultPicture().getWidth(),
                                picture.getDefaultPicture().getHeight()));
                        fileNameField.setText(selectedFiles.get(0).toPath().toString());
                    } catch (IOException e) {
                        throw new RuntimeException("Ошибка выбора файла");
                    }
                    //построение масштаба
                    if (picture.getDefaultPicture().getHeight() < 300) {
                        xAxisLabel.setPreferredSize(new Dimension(picture.getDefaultPicture().getWidth() + 20,
                                picture.getDefaultPicture().getHeight()/2));
                        yAxisLabel.setPreferredSize(new Dimension(picture.getDefaultPicture().getWidth()/3,
                                picture.getDefaultPicture().getHeight() + 40));
                    } else {
                        xAxisLabel.setPreferredSize(new Dimension(picture.getDefaultPicture().getWidth() + 20,
                                picture.getDefaultPicture().getHeight()/7));
                        yAxisLabel.setPreferredSize(new Dimension(picture.getDefaultPicture().getWidth()/7,
                                picture.getDefaultPicture().getHeight() + 40));
                    }

                    yAxisLabel.setPreferredSize(new Dimension(picture.getDefaultPicture().getWidth()/5,
                            picture.getDefaultPicture().getHeight()));

                    xAxisLabel.setIcon(new ImageIcon(picture.drawXAxisImage()));
                    yAxisLabel.setIcon(new ImageIcon(picture.drawYAxisImage()));
                }
            }
        });

        return fileMenu;
    }


    private JMenu createSettingsMenu() {
            JMenu settingMenu = new JMenu("Настройки");
            JMenuItem pictureSettings = new JMenuItem("Настройки изображения");
            settingMenu.add(pictureSettings);

            pictureSettings.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //настройки
                    JFrame settingsFrame = new JFrame();
                    settingsFrame.setPreferredSize(new Dimension(800, 800));
                    settingsFrame.setResizable(false);
                    JPanel settingsPanel = new JPanel();
                    settingsPanel.setPreferredSize(new Dimension(500, 500));
                    settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    settingsFrame.setVisible(true);

                    JLabel regexLabel = LabelFactory.createLabel("Regex в csv файле");
                    TextArea regexInputArea = new TextArea(picture.getRegexInFile());
                    regexInputArea.setPreferredSize(new Dimension(300, 100));
                    regexInputArea.setFont(new Font("Times New Roman", Font.PLAIN, 28));

                    settingsPanel.setLayout(new GridBagLayout());
                    GridBagConstraints c = new GridBagConstraints();
                    c.fill = GridBagConstraints.CENTER;
                    c.weighty = 1;
                    c.gridy = 0;
                    c.gridx = 1;
                    c.weightx = 1;
                    settingsPanel.add(regexLabel, c);
                    c.gridx = 2;
                    settingsPanel.add(regexInputArea, c);

                    JButton settingsButton = new JButton("Сохранить");
                    settingsButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent press) {
                            String input = regexInputArea.getText();
                            picture.setRegexInFile(input);
                        }
                    });
                    c.gridx = 1;
                    c.gridy = 1;
                    settingsPanel.add(settingsButton, c);

                    settingsFrame.add(settingsPanel);
                    settingsFrame.pack();

                }
            });

            return settingMenu;
    }

    private class ExitAction extends AbstractAction
    {
        private static final long serialVersionUID = 1L;
        ExitAction() {
            putValue(NAME, "Выход");
        }
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}

