package gui;

import calculation.*;
import picture.Picture;
import picture.Pixel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*данный класс отвечает за отрисовывание рабочего окна. основа - библиотека
Swing*/
public class Frame extends JFrame {
    Picture picture = new Picture();
    JLabel imageLabel;
    DecimalFormat df = new DecimalFormat("#");


        public Frame() {
            //отрисовка окна
            super("PlotMath");
            setPreferredSize(new Dimension(1600, 800));
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

            //создание панелей для отображения значений
            JLabel rzOutputLabel = LabelFactory.createLabel("Enter Data to calculate Rz");
            rzOutputLabel.setPreferredSize(new Dimension(400, 50));
            JLabel rMaxOutputLabel = LabelFactory.createLabel("Enter Data to calculate Rmax");
            rMaxOutputLabel.setPreferredSize(new Dimension(400, 50));
            JLabel rAOutputLabel = LabelFactory.createLabel("Enter Data to calculate Ra");
            rAOutputLabel.setPreferredSize(new Dimension(400, 50));
            JLabel rzLabel = LabelFactory.createLabel("Rz");
            rzLabel.setPreferredSize(new Dimension(150, 50));
            JLabel rMaxLabel = LabelFactory.createLabel("RMax");
            rMaxLabel.setPreferredSize(new Dimension(150, 50));
            JLabel rALabel = LabelFactory.createLabel("Ra");
            rALabel.setPreferredSize(new Dimension(150, 50));

            //кнопка для запуска алгоритмов расчёта параметров шероховатости
            JButton buttonCalculate = new JButton("Calculate");
            buttonCalculate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent press) {
                    Map<Integer, Pixel> pixelList = new HashMap<>(picture.getCurrentPixelMap());
                    List<Integer> values = Converter.getMarkedDotsValuesArray(pixelList);
                    rAOutputLabel.setText(BigDecimal.valueOf(RaCalculator.calculate(values)).toPlainString());
                    rMaxOutputLabel.setText(BigDecimal.valueOf(RMaxCalculator.calculate(values)).toPlainString());
                    rzOutputLabel.setText(BigDecimal.valueOf(RzCalculator.calculate(values)).toPlainString());
                }
            });
            buttonCalculate.setPreferredSize(new Dimension(100, 100));

            JButton buttonGraph = new JButton("Show graph");
            buttonGraph.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent press) {
                    Map<Integer, Pixel> pixelList = new HashMap<>(picture.getCurrentPixelMap());
                    GraphPanel.createAndShow(Converter.getMarkedDotsValuesArray(pixelList));
                }
            });
            buttonGraph.setPreferredSize(new Dimension(110, 100));

            JButton gaussButton = new JButton("Gauss graph");
            gaussButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent press) {
                    List<Pixel> pixels = new ArrayList<>(picture.getDefaultPixelMap().values());

                }
            });
            //установка элементов в рабочую панель
            JPanel imagePanel = new JPanel();
            imagePanel.setPreferredSize(new Dimension(900, 800));
            imagePanel.setLayout(new GridBagLayout());
            c.fill = GridBagConstraints.CENTER;
            c.gridx = 0;
            c.gridy = 0;
            imagePanel.add(imageLabel, c);

            JPanel outputContents = new JPanel();
            outputContents.setPreferredSize(new Dimension(100, 100));
            outputContents.setLayout(new GridBagLayout());
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
            c.gridx = 0;
            c.gridy = 3;
            c.weighty = 1;
            outputContents.add(buttonCalculate, c);
            c.gridx = 1;
            outputContents.add(buttonGraph, c);

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
        JMenuItem open = new JMenuItem("Открыть");
        // Пункт меню из команды с выходом из программы
        JMenuItem exit = new JMenuItem(new ExitAction());
        fileMenu.add(open);
        // Добавление разделителя
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
                    } catch (IOException e) {
                        throw new RuntimeException("Ошибка выбора файла");
                    }
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

