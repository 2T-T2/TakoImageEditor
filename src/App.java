import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class App extends JFrame implements ActionListener {
    private boolean isEditing = false;
    private int editFps = 10;
    private Color transparentCol = new Color(0,0,0,0);
    private Point draggedPoint = new Point(-1, -1);

    private JPanel main;
    private JPanel editMenu;
    private JPanel paramSetter;
    private JPanel status;

    private ImageUtil imgUtil;
    private JLabel jl = new JLabel();

    private JSlider slider = new JSlider(10, 500);
    private JLabel sliderLabel = new JLabel("100 %");

    private JTextField strInput;
    private IntegerInputPanel xPanel;
    private IntegerInputPanel yPanel;
    private IntegerInputPanel wPanel;
    private IntegerInputPanel hPanel;
    private IntegerInputPanel sizePanel;
    private IntegerInputPanel fontSizePanel;
    private IntegerInputPanel thicknessPanel;
    private IntegerInputPanel powerPanel;
    private IntegerInputPanel amountPanel;
    private IntegerInputPanel thresholdPanel;
    private JComboBox<String> fontCombo;
    private JCheckBox boldStyle;
    private JCheckBox italicCheck;
    private JButton colSelectBtn;
    private JButton inColSelectBtn;
    private JButton outColSelectBtn;

    private JButton addTexButton;
    private JButton addBorderTextBtn;
    private JButton resizeBtn;
    private JButton cropBtn;
    private JButton transparentByColBtn;
    private JButton transparentByRectBtn;
    private JButton transparentByPenBtn;
    private JButton addShapeBtn;
    private JButton drawBtn;
    private JButton addImageBtn;
    private JButton adjustmentRgbBtn;
    private JButton gammaBtn;
    private JButton changeHueBtn;
    private JButton changeSaturationBtn;
    private JButton changeBrightnessBtn;
    private JButton colInvertBtn;
    private JButton toSepiaBtn;
    private JButton toMosaicBtn;
    private JButton toGrayBtn;
    private JButton blurBtn;
    private JButton colEdgeBtn;
    private JButton edgeBtn;
    private JButton unsharpmaskBtn;
    private JButton roteBtn;
    private JButton done;

    public static void main(String[] args) {
        if ( args.length == 0 ) { new App( null ); }
        else { new App( args[0] ); }
    }

    App(String path) {
        setPreferredSize(new Dimension(500, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        File f;
        if ( path == null ) { f = openFileSelect(); }
        else { f = new File(path); }
        if ( !ImageUtil.Companion.isImageFile(f) ) { msgBox("画像ファイルを指定してください", ""); System.exit(9); }

        imgUtil = new ImageUtil(f);

// =================================== メニューバー =================================== //
{
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("ファイル(F)");
        fileMenu.setMnemonic(KeyEvent.VK_F);
            JMenuItem saveAs = new JMenuItem("保存");
            saveAs.addActionListener( new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    File f = saveAsFileSelect();
                    if( !f.getName().contains(".") ) { msgBox("拡張子を指定してください", ""); return; }
                    for (ImageExt ext : ImageExt.values()) {
                        if( f.getName().substring(f.getName().lastIndexOf(".")).toLowerCase().equals( "."+ext.toString().toLowerCase() ) ) {
                            imgUtil.save(f.getAbsolutePath(), ext);
                            msgBox("ほぞんしました", "");
                            return;
                        }
                    }
                    msgBox("その拡張子はサポートしていません", "保存時エラー");
                }
            } );
            saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(saveAs);

        JMenu editMenu = new JMenu("編集(E)");
        editMenu.setMnemonic(KeyEvent.VK_E);
            JMenuItem changeBackgoundCol = new JMenuItem("背景の色を変える");
            changeBackgoundCol.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jl.setOpaque( !jl.isOpaque() );
                    revalidate();
                    repaint();
                }
            });
        editMenu.add(changeBackgoundCol);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        setJMenuBar( menuBar );
}
// =================================== 画像編集部分 =================================== //
        jl.setBackground(Color.darkGray);
        jl.setIcon( new ImageIcon( imgUtil.getChangedImage() ) );
        jl.setSize(imgUtil.getWidth(), imgUtil.getHeight());
        jl.addMouseListener( new java.awt.event.MouseListener() {
            public void mouseClicked(MouseEvent e) {
                double scale = (double)slider.getValue() / 100.0;
                transparentCol = new Color( imgUtil.getChangedImage().getRGB((int)(e.getX() / scale), (int)(e.getY() / scale)) );
            }
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        } );
        jl.addMouseMotionListener( new java.awt.event.MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                double scale = (double)slider.getValue() / 100.0;
                draggedPoint = new Point( (int)(e.getX() / scale), (int)(e.getY() / scale) );
            }
            public void mouseMoved(MouseEvent e) {}

        } );

        main = new JPanel();
        main.setPreferredSize(new Dimension( imgUtil.getWidth(), imgUtil.getHeight() ));
        main.setLayout(null);
        main.add(jl);

        JScrollPane scroll = new JScrollPane(main);
        scroll.setPreferredSize( new Dimension( 450, 440 ) );
        scroll.setBorder( javax.swing.BorderFactory.createLineBorder( Color.BLACK ) );

// ================================= 編集メニュー部分 ================================= //
{
        Dimension btnSize = new Dimension(20, 20);

        addTexButton = new JButton( new ImageIcon("res/icon/text.png") );
        addTexButton.setPreferredSize(btnSize);
        addTexButton.setToolTipText("文字を挿入します...");
        addTexButton.addActionListener(this);

        addBorderTextBtn = new JButton( new ImageIcon("res/icon/bordertext.png") );
        addBorderTextBtn.setPreferredSize(btnSize);
        addBorderTextBtn.setToolTipText("縁取り文字を挿入します...");
        addBorderTextBtn.addActionListener(this);

        drawBtn = new JButton( new ImageIcon("res/icon/draw.png") );
        drawBtn.setPreferredSize( btnSize );
        drawBtn.setToolTipText( "落書きします..." );
        drawBtn.addActionListener(this);

        addShapeBtn = new JButton( new ImageIcon("res/icon/shape.png") );
        addShapeBtn.setPreferredSize(btnSize);
        addShapeBtn.setToolTipText("図形を挿入します");
        addShapeBtn.addActionListener(this);

        changeHueBtn = new JButton( new ImageIcon("res/icon/hue.png") );
        changeHueBtn.setPreferredSize(btnSize);
        changeHueBtn.setToolTipText("色合いを変化させます...");
        changeHueBtn.addActionListener(this);

        changeSaturationBtn = new JButton( new ImageIcon("res/icon/Saturation.png") );
        changeSaturationBtn.setPreferredSize(btnSize);
        changeSaturationBtn.setToolTipText("鮮やかさを変化させます");
        changeSaturationBtn.addActionListener(this);

        changeBrightnessBtn = new JButton( new ImageIcon("res/icon/brightness.png") );
        changeBrightnessBtn.setPreferredSize(btnSize);
        changeBrightnessBtn.setToolTipText("明るさを変化させます...");
        changeBrightnessBtn.addActionListener(this);

        gammaBtn = new JButton( new ImageIcon("res/icon/gamma.png") );
        gammaBtn.setPreferredSize(btnSize);
        gammaBtn.setToolTipText("ガンマ調整をします");
        gammaBtn.addActionListener(this);

        adjustmentRgbBtn = new JButton( new ImageIcon("res/icon/rgb.png") );
        adjustmentRgbBtn.setPreferredSize( btnSize );
        adjustmentRgbBtn.setToolTipText("RGBの調整を行います");
        adjustmentRgbBtn.addActionListener(this);

        resizeBtn = new JButton( new ImageIcon("res/icon/resize.png") );
        resizeBtn.setPreferredSize(btnSize);
        resizeBtn.setToolTipText("リサイズします...");
        resizeBtn.addActionListener(this);

        cropBtn = new JButton( new ImageIcon("res/icon/crop.png") );
        cropBtn.setPreferredSize(btnSize);
        cropBtn.setToolTipText("画像を四角形で切り抜きます...");
        cropBtn.addActionListener(this);

        addImageBtn = new JButton( new ImageIcon("res/icon/image.png") );
        addImageBtn.setPreferredSize(btnSize);
        addImageBtn.setToolTipText("画像を挿入します...");
        addImageBtn.addActionListener(this);

        transparentByColBtn = new JButton( new ImageIcon("res/icon/tbyc.png") );
        transparentByColBtn.setPreferredSize( btnSize );
        transparentByColBtn.setToolTipText("色を指定して透明にします...");
        transparentByColBtn.addActionListener(this);

        transparentByRectBtn = new JButton( new ImageIcon("res/icon/tbyr.png") );
        transparentByRectBtn.setPreferredSize( btnSize );
        transparentByRectBtn.setToolTipText("四角で指定した部分を透明にします...");
        transparentByRectBtn.addActionListener(this);

        transparentByPenBtn = new JButton( new ImageIcon("res/icon/tbym.png") );
        transparentByPenBtn.setPreferredSize( btnSize );
        transparentByPenBtn.setToolTipText("マウスを操作して透過処理します...");
        transparentByPenBtn.addActionListener(this);
        
        roteBtn = new JButton( new ImageIcon("res/icon/rote.png") );
        roteBtn.setPreferredSize(btnSize);
        roteBtn.setToolTipText("画像を回転させます...");
        roteBtn.addActionListener(this);

        unsharpmaskBtn = new JButton( new ImageIcon("res/icon/unsharpmask.png") );
        unsharpmaskBtn.setPreferredSize(btnSize);
        unsharpmaskBtn.setToolTipText("アンシャープマスク処理をします...");
        unsharpmaskBtn.addActionListener(this);

        blurBtn = new JButton( new ImageIcon("res/icon/blur.png") );
        blurBtn.setPreferredSize( btnSize );
        blurBtn.setToolTipText("画像をぼかします...");
        blurBtn.addActionListener(this);

        toMosaicBtn = new JButton( new ImageIcon("res/icon/mosaic.png") );
        toMosaicBtn.setPreferredSize( btnSize );
        toMosaicBtn.setToolTipText("モザイクをかけます...");
        toMosaicBtn.addActionListener(this);

        toSepiaBtn = new JButton( new ImageIcon("res/icon/sepia.png") );
        toSepiaBtn.setPreferredSize( btnSize );
        toSepiaBtn.setToolTipText("画像をセピア色にします...");
        toSepiaBtn.addActionListener(this);

        colInvertBtn = new JButton( new ImageIcon("res/icon/colorInvert.png") );
        colInvertBtn.setPreferredSize( btnSize );
        colInvertBtn.setToolTipText("色反転を行います...");
        colInvertBtn.addActionListener(this);
                
        toGrayBtn = new JButton( new ImageIcon("res/icon/gray.png") );
        toGrayBtn.setPreferredSize( btnSize );
        toGrayBtn.setToolTipText("グレーにします...");
        toGrayBtn.addActionListener(this);

        edgeBtn = new JButton( new ImageIcon("res/icon/edge.png") );
        edgeBtn.setPreferredSize(btnSize);
        edgeBtn.setToolTipText("エッジのみの画像にします...");
        edgeBtn.addActionListener(this);

        colEdgeBtn = new JButton( new ImageIcon("res/icon/colorEdge.png") );
        colEdgeBtn.setPreferredSize(btnSize);
        colEdgeBtn.setToolTipText("カラーエッジのみの画像にします...");
        colEdgeBtn.addActionListener(this);

        editMenu = new JPanel();
        editMenu.setLayout(new FlowLayout());
        editMenu.setBackground( new Color(255, 255, 255) );
        editMenu.setPreferredSize(new Dimension( 50, 500 ));
        editMenu.add(addTexButton);
        editMenu.add(addBorderTextBtn);
        editMenu.add(drawBtn);
        editMenu.add(addShapeBtn);
        editMenu.add(addImageBtn);
        editMenu.add(changeHueBtn);
        editMenu.add(changeSaturationBtn);
        editMenu.add(changeBrightnessBtn);
        editMenu.add(gammaBtn);
        editMenu.add(adjustmentRgbBtn);
        editMenu.add(resizeBtn);
        editMenu.add(cropBtn);
        editMenu.add(transparentByColBtn);
        editMenu.add(transparentByRectBtn);
        editMenu.add(transparentByPenBtn);
        editMenu.add(roteBtn);
        editMenu.add(blurBtn);
        editMenu.add(unsharpmaskBtn);
        editMenu.add(toMosaicBtn);
        editMenu.add(toGrayBtn);
        editMenu.add(toSepiaBtn);
        editMenu.add(colInvertBtn);
        editMenu.add(edgeBtn);
        editMenu.add(colEdgeBtn);
}
// ===================================== 設定項目 ===================================== //
        strInput = new JTextField("Sample");
        strInput.setPreferredSize(new Dimension(80, 30));

        done = new JButton("確定");
        done.setPreferredSize(new Dimension(50, 30));
        done.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 8));
        done.addActionListener(this);

        xPanel = new IntegerInputPanel("x座標:");
        yPanel = new IntegerInputPanel("y座標:");
        wPanel = new IntegerInputPanel("幅:");
        hPanel = new IntegerInputPanel("高さ:");
        sizePanel = new IntegerInputPanel("サイズ");

        colSelectBtn = new JButton("色選択");
        colSelectBtn.setForeground(Color.white);
        colSelectBtn.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color col = colorSelect();
                if( col != null ) { colSelectBtn.setForeground(col);}
            }
        } );
        inColSelectBtn = new JButton("内色選択");
        inColSelectBtn.setForeground(Color.white);
        inColSelectBtn.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color col = colorSelect();
                if( col != null  ) { inColSelectBtn.setForeground(col);}
            }
        } );
        outColSelectBtn = new JButton("外色選択");
        outColSelectBtn.setForeground(Color.black);
        outColSelectBtn.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color col = colorSelect();
                if( col != null ) { outColSelectBtn.setForeground(col);}
            }
        } );

        String[] fontfamilys = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontCombo = new JComboBox<String>( fontfamilys );

        boldStyle = new JCheckBox("太字");
        italicCheck = new JCheckBox("斜体");

        fontSizePanel = new IntegerInputPanel("フォントサイズ(px)");
        thicknessPanel = new IntegerInputPanel("太さ");
        thicknessPanel.setIntValue(2);

        powerPanel = new IntegerInputPanel("パワー:");
        powerPanel.setIntValue(5);
        thresholdPanel = new IntegerInputPanel("しきい値");
        thresholdPanel.setIntValue(10);
        amountPanel = new IntegerInputPanel("量(%):");
        amountPanel.setIntValue(10);

        paramSetter = new JPanel(new FlowLayout());
        paramSetter.setBackground(Color.gray);
        paramSetter.setPreferredSize(new Dimension(500, 30));

// ===================================== 下の部分 ===================================== //
        slider.setValue(100);
        slider.addChangeListener( new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                sliderLabel.setText(""+slider.getValue()+"%");
                double scale = (double)slider.getValue() / 100.0;
                Dimension size = new Dimension( (int)(imgUtil.getWidth()*scale), (int)(imgUtil.getHeight()*scale) );
                updateImageView( size );
            }
        });
        slider.setPreferredSize(new Dimension(100, 20) );
        sliderLabel.setPreferredSize( new Dimension(50, 20) );

        Dimension btnSize = new Dimension( 30,30 );
        JButton plusBtn = new JButton("＋");
        plusBtn.setSize(btnSize);
        plusBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int val = slider.getValue() + 10;
                slider.setValue( val );
                sliderLabel.setText(""+val+"%");
                double scale = (double)val / 100.0;
                Dimension size = new Dimension((int)(imgUtil.getWidth()*scale), (int)(imgUtil.getHeight()*scale));
                updateImageView( size );
            }
        });
        JButton subBtn = new JButton("－");
        subBtn.setSize(btnSize);
        subBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int val = slider.getValue() - 10;
                slider.setValue( val );
                sliderLabel.setText(""+val+"%");
                double scale = (double)val / 100.0;
                Dimension size = new Dimension((int)(imgUtil.getWidth()*scale), (int)(imgUtil.getHeight()*scale));
                updateImageView( size );
            }
        });

        status = new JPanel();
        status.add(sliderLabel, BorderLayout.WEST);
        status.add(subBtn, BorderLayout.EAST);
        status.add(slider, BorderLayout.EAST);
        status.add(plusBtn, BorderLayout.EAST);
        status.setPreferredSize( new Dimension(500, 30) );

// ==================================== 全体 =================================== //
        setIconImage(new ImageIcon("res/icon/App.png").getImage());

        Container contentPane = getContentPane();
        contentPane.add(scroll, BorderLayout.CENTER);
        contentPane.add(status, BorderLayout.SOUTH);
        contentPane.add(editMenu, BorderLayout.WEST);
        contentPane.add(paramSetter, BorderLayout.NORTH);

        pack();
        setVisible(true);
        setExtendedState(MAXIMIZED_BOTH);
    }

    // imgUtil に変化があった時に呼び出す関数
    void updateImageView( Dimension size ) {
        jl.setIcon( new ImageIcon( new ImageIcon(imgUtil.getChangedImage()).getImage().getScaledInstance((int)size.getWidth(), (int)size.getHeight(), Image.SCALE_DEFAULT) ) );
        jl.setSize( size );
        main.setPreferredSize( size );
    }
    // imgUtil を変更させずに表示内容を変える時に呼び出す関数
    void changeImageView( Dimension size, BufferedImage img ) {
        ImageUtil _imgUtil = new ImageUtil( img );
        jl.setIcon( new ImageIcon( new ImageIcon(_imgUtil.getChangedImage()).getImage().getScaledInstance((int)size.getWidth(), (int)size.getHeight(), Image.SCALE_DEFAULT) ) );
        jl.setSize( size );
        main.setPreferredSize( size );
    }

    File openFileSelect() {
        FileSelector fSelector = new FileSelector(this);
        fSelector.show(FileDialog.LOAD);
        return new File(fSelector.getFileName());
    }
    File saveAsFileSelect() {
        FileSelector fSelector = new FileSelector(this);
        fSelector.show(FileDialog.SAVE);
        return new File(fSelector.getFileName());
    }

    Color colorSelect() {
        return JColorChooser.showDialog(this, "色選択", Color.white);
    }

    void msgBox(String mes, String title) {
        JOptionPane.showMessageDialog(this, mes, title, JOptionPane.INFORMATION_MESSAGE);
    }

    void resetParamSetter() {
        paramSetter.removeAll();
        strInput.setText("Sample");
        xPanel.setIntValue(30);
        yPanel.setIntValue(30);
        colSelectBtn.setForeground(Color.white);
        fontSizePanel.setIntValue(30);
        fontCombo.setSelectedIndex(0);
        sizePanel.setIntValue(30);
        hPanel.setCompEnable(true);
        wPanel.setCompEnable(true);
        powerPanel.setIntValue(5);
        amountPanel.setIntValue(10);
        thresholdPanel.setIntValue(10);
        revalidate();
        repaint();
    }
    void addParamSetter(java.awt.Component c) {
        paramSetter.add(c);
        revalidate();
        repaint();
    }
    void removeParamSetter(java.awt.Component c) {
        paramSetter.remove(c);
        revalidate();
        repaint();
    }
    void addStatusMessage(JLabel mesLabel) {
        status.add(mesLabel, BorderLayout.WEST);
        revalidate();
        repaint();
    }
    void removeStatusMessage(JLabel mesLabel) {
        status.remove(mesLabel);
        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JLabel mesLabel = new JLabel("処理中...");
        if( e.getSource().equals(addBorderTextBtn) ){
            if ( isEditing ) { msgBox("編集中です。", ""); return; }
            // 縁取り文字入処理
            isEditing = true;
            paramSetter.add(fontCombo);
            paramSetter.add(strInput);
            paramSetter.add(fontSizePanel);
            paramSetter.add(boldStyle);
            paramSetter.add(italicCheck);
            paramSetter.add(thicknessPanel);
            paramSetter.add(xPanel);
            paramSetter.add(yPanel);
            paramSetter.add(inColSelectBtn);
            paramSetter.add(outColSelectBtn);
            addParamSetter(done);
            new Thread( new Runnable(){
                public void run() {
                    ImageUtil tmp = new ImageUtil( imgUtil.getChangedImage() );
                    while( isEditing ) {
                        tmp = new ImageUtil( imgUtil.getChangedImage() );
                        tmp.addBorderText(strInput.getText(), new Font((String)fontCombo.getSelectedItem(), (boldStyle.isSelected() ? Font.BOLD : Font.PLAIN) | ( italicCheck.isSelected() ? Font.ITALIC : Font.PLAIN ), fontSizePanel.getIntValue()), inColSelectBtn.getForeground(), outColSelectBtn.getForeground(), xPanel.getIntValue(), yPanel.getIntValue(), thicknessPanel.getIntValue());

                        double scale = (double)slider.getValue() / 100.0;
                        Dimension size = new Dimension( (int)(tmp.getWidth()*scale), (int)(tmp.getHeight()*scale) );
                        changeImageView(size, tmp.getChangedImage());

                      try { Thread.sleep( (int)((1.0 / editFps) * 1000) ); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    msgBox("編集確定！", "");
                    imgUtil = new ImageUtil( tmp.getChangedImage() );
                    updateImageView(new Dimension( tmp.getWidth(), tmp.getHeight() ));
                    resetParamSetter();
                }
            }).start();

        } else if( e.getSource().equals( addTexButton ) ){
            // 文字入処理
            if ( isEditing ) { msgBox("編集中です。", ""); return; }
            isEditing = true;
            paramSetter.add(fontCombo);
            paramSetter.add(fontSizePanel);
            paramSetter.add(boldStyle);
            paramSetter.add(italicCheck);
            paramSetter.add(strInput);
            paramSetter.add(xPanel);
            paramSetter.add(yPanel);
            paramSetter.add(colSelectBtn);
            addParamSetter(done);
            new Thread( new Runnable(){
                public void run() {
                    ImageUtil tmp = new ImageUtil( imgUtil.getChangedImage() );
                    while( isEditing ) {
                        tmp = new ImageUtil( imgUtil.getChangedImage() );
                        tmp.addText(strInput.getText(), new Font((String)fontCombo.getSelectedItem(), (boldStyle.isSelected() ? Font.BOLD : Font.PLAIN) | ( italicCheck.isSelected() ? Font.ITALIC : Font.PLAIN ), fontSizePanel.getIntValue() ), colSelectBtn.getForeground(), xPanel.getIntValue(), yPanel.getIntValue());

                        double scale = (double)slider.getValue() / 100.0;
                        Dimension size = new Dimension( (int)(tmp.getWidth()*scale), (int)(tmp.getHeight()*scale) );
                        changeImageView(size, tmp.getChangedImage());

                      try { Thread.sleep( (int)((1.0 / editFps) * 1000) ); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    msgBox("編集確定！", "");
                    imgUtil = new ImageUtil( tmp.getChangedImage() );
                    updateImageView(new Dimension( tmp.getWidth(), tmp.getHeight() ));
                    resetParamSetter();
                }
            }).start();

        } else if( e.getSource().equals( resizeBtn ) ){
            // リサイズ処理、大きすぎる値は処理できなくなる
            if ( isEditing ) { msgBox("編集中です。", ""); return; }
            isEditing = true;
            JCheckBox keepAspect = new JCheckBox("縦横比を維持する");
            JCheckBox isPer = new JCheckBox("パーセントでサイズ指定する");
            isPer.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    if ( !isPer.isSelected() ) { return; }
                    wPanel.setIntValue(100);
                    hPanel.setIntValue(100);
                }
            });
            keepAspect.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    if ( !keepAspect.isSelected() || isPer.isSelected() ) { return; }
                    wPanel.setIntValue(imgUtil.getWidth());
                    hPanel.setIntValue(imgUtil.getHeight());
                }
            });
            wPanel.setIntValue(imgUtil.getWidth());
            hPanel.setIntValue(imgUtil.getHeight());
            paramSetter.add(wPanel);
            paramSetter.add(hPanel);
            paramSetter.add(keepAspect);
            paramSetter.add(isPer);
            addParamSetter(done);
            new Thread( new Runnable(){
                public void run() {
                    ImageUtil tmp = new ImageUtil( imgUtil.getChangedImage() );
                    double wPerH = ((double)imgUtil.getWidth() / imgUtil.getHeight());
                    while( isEditing ) {
                        if( wPanel.getIntValue() == 0 || hPanel.getIntValue() == 0 ) { continue; }
                        tmp = new ImageUtil( imgUtil.getChangedImage() );
                        if ( !keepAspect.isSelected() ) {
                            hPanel.setCompEnable(true);
                            try {
                                if ( isPer.isSelected() ) { tmp.resize( (double)wPanel.getIntValue() / 100.0, (double)hPanel.getIntValue() / 100.0 ); }
                                else { tmp.resize(wPanel.getIntValue(), hPanel.getIntValue()); }
                            }catch(Exception e) {
                                continue;
                            }catch(OutOfMemoryError e) {
                                continue;
                            }
                        }
                        else {
                            hPanel.setCompEnable(false);
                            try {
                                if ( isPer.isSelected() ) {
                                    tmp.resize( (double)wPanel.getIntValue() / 100.0 , (double)wPanel.getIntValue() / 100.0 );
                                    hPanel.setIntValue(wPanel.getIntValue());
                                }else{
                                    tmp.resize(wPanel.getIntValue(), (int)(wPanel.getIntValue()/wPerH));
                                    hPanel.setIntValue((int)(wPanel.getIntValue()/wPerH));
                                }
                            }catch(Exception e) {
                                continue;
                            }catch(OutOfMemoryError e) {
                                continue;
                            }
                        }

                        double scale = (double)slider.getValue() / 100.0;
                        Dimension size = new Dimension( (int)(tmp.getWidth()*scale), (int)(tmp.getHeight()*scale) );
                        changeImageView(size, tmp.getChangedImage());

                      try { Thread.sleep( (int)((1.0 / editFps) * 1000) ); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    msgBox("編集確定！", "");
                    imgUtil = new ImageUtil( tmp.getChangedImage() );
                    updateImageView(new Dimension( tmp.getWidth(), tmp.getHeight() ));
                    resetParamSetter();
                }
            }).start();

        } else if( e.getSource().equals( cropBtn ) ) {
            // crop処理
            if ( isEditing ) { msgBox("編集中です。", ""); return; }
            isEditing = true;
            xPanel.setIntValue(0);
            yPanel.setIntValue(0);
            wPanel.setIntValue(imgUtil.getWidth());
            hPanel.setIntValue(imgUtil.getHeight());
            paramSetter.add(xPanel);
            paramSetter.add(yPanel);
            paramSetter.add(wPanel);
            paramSetter.add(hPanel);
            addParamSetter(done);
            new Thread( new Runnable(){
                public void run() {
                    ImageUtil tmp = new ImageUtil( imgUtil.getChangedImage() );
                    while( isEditing ) {
                        tmp = new ImageUtil( imgUtil.getChangedImage() );
                        tmp.addLineRect(new java.awt.Rectangle( xPanel.getIntValue(), yPanel.getIntValue(), wPanel.getIntValue(), hPanel.getIntValue() ));

                        double scale = (double)slider.getValue() / 100.0;
                        Dimension size = new Dimension( (int)(tmp.getWidth()*scale), (int)(tmp.getHeight()*scale) );
                        changeImageView(size, tmp.getChangedImage());

                      try { Thread.sleep( (int)((1.0 / editFps) * 1000) ); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    msgBox("編集確定！", "");
                    imgUtil.crop(xPanel.getIntValue(), yPanel.getIntValue(), wPanel.getIntValue(), hPanel.getIntValue());
                    updateImageView(new Dimension( imgUtil.getWidth(), imgUtil.getHeight() ));
                    resetParamSetter();
                }
            }).start();

        } else if( e.getSource().equals( transparentByColBtn ) ){
            // 色指定透過処理
            if ( isEditing ) { msgBox("編集中です。", ""); return; }
            isEditing = true;
            transparentCol = new Color(0,0,0,0);
            JLabel l = new JLabel("透明にしたい色の場所をクリック！");
            JButton changeMode = new JButton("RGBモードに切替");
            changeMode.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if( changeMode.getText().equals( "クリック色指定モードに切り替え" ) ) {
                        changeMode.setText("RGBモードに切り替え");
                        l.setText("透明にしたい色の場所をクリック！");
                        resetParamSetter();
                        paramSetter.add(l);
                        paramSetter.add(changeMode);
                        addParamSetter(done);
                    }
                    else {
                        changeMode.setText("クリック色指定モードに切り替え");
                        resetParamSetter();
                        paramSetter.add(colSelectBtn);
                        paramSetter.add(changeMode);
                        addParamSetter(done);
                    }
                }
            });
            paramSetter.add( l );
            paramSetter.add(changeMode);
            addParamSetter(done);
            new Thread( new Runnable(){
                public void run() {
                    ImageUtil tmp = new ImageUtil( imgUtil.getChangedImage() );
                    while( isEditing ) {
                        tmp = new ImageUtil( imgUtil.getChangedImage() );
                        if( changeMode.getText().equals( "クリック色指定モードに切り替え" ) ) {
                            tmp.transparentByColor( colSelectBtn.getForeground() );
                        }else {
                            tmp.transparentByColor( transparentCol );
                        }

                        double scale = (double)slider.getValue() / 100.0;
                        Dimension size = new Dimension( (int)(tmp.getWidth()*scale), (int)(tmp.getHeight()*scale) );
                        changeImageView(size, tmp.getChangedImage());

                      try { Thread.sleep( (int)((1.0 / editFps) * 1000) ); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    msgBox("編集確定！", "");
                    imgUtil = new ImageUtil( tmp.getChangedImage() );
                    updateImageView(new Dimension( tmp.getWidth(), tmp.getHeight() ));
                    resetParamSetter();
                }
            }).start();

        } else if( e.getSource().equals( transparentByRectBtn ) ){
            // 矩形透過処理
            if ( isEditing ) { msgBox("編集中です。", ""); return; }
            isEditing = true;
            paramSetter.add(xPanel);
            paramSetter.add(yPanel);
            paramSetter.add(wPanel);
            paramSetter.add(hPanel);
            addParamSetter(done);
            new Thread( new Runnable(){
                public void run() {
                    ImageUtil tmp = new ImageUtil( imgUtil.getChangedImage() );
                    while( isEditing ) {
                        tmp = new ImageUtil( imgUtil.getChangedImage() );
                        tmp.transparentByRect(xPanel.getIntValue(), yPanel.getIntValue(), wPanel.getIntValue(), hPanel.getIntValue());

                        double scale = (double)slider.getValue() / 100.0;
                        Dimension size = new Dimension( (int)(tmp.getWidth()*scale), (int)(tmp.getHeight()*scale) );
                        changeImageView(size, tmp.getChangedImage());

                      try { Thread.sleep( (int)((1.0 / editFps) * 1000) ); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    msgBox("編集確定！", "");
                    imgUtil = new ImageUtil( tmp.getChangedImage() );
                    updateImageView(new Dimension( tmp.getWidth(), tmp.getHeight() ));
                    resetParamSetter();
                }
            }).start();

        } else if( e.getSource().equals( transparentByPenBtn ) ){
            // マウス操作透過処理
            if ( isEditing ) { msgBox("編集中です。", ""); return; }
            isEditing = true;
            draggedPoint = new Point(-1, -1);
            String[] penKinds = { "まる", "四角" };
            JComboBox<String> penKindsBox = new JComboBox<String>(penKinds);
            paramSetter.add(penKindsBox);
            paramSetter.add(sizePanel);
            addParamSetter(done);
            new Thread( new Runnable(){
                public void run() {
                    ImageUtil tmp = new ImageUtil( imgUtil.getChangedImage() );
                    while( isEditing ) {
                        if( draggedPoint.x != -1 && draggedPoint.y != -1 ) {
                            if( penKindsBox.getSelectedItem().equals("まる") )
                                tmp.transparentByEllipse2D( new  java.awt.geom.Ellipse2D.Float(draggedPoint.x - sizePanel.getIntValue() / 3, draggedPoint.y - sizePanel.getIntValue() / 3, sizePanel.getIntValue(), sizePanel.getIntValue()) );
                            else
                                tmp.transparentByRect( draggedPoint.x - sizePanel.getIntValue() / 3, draggedPoint.y - sizePanel.getIntValue() / 3, sizePanel.getIntValue(), sizePanel.getIntValue());
                        }

                        double scale = (double)slider.getValue() / 100.0;
                        Dimension size = new Dimension( (int)(tmp.getWidth()*scale), (int)(tmp.getHeight()*scale) );
                        changeImageView(size, tmp.getChangedImage());

                      try { Thread.sleep( (int)((1.0 / editFps) * 1000) ); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    msgBox("編集確定！", "");
                    imgUtil = new ImageUtil( tmp.getChangedImage() );
                    updateImageView(new Dimension( tmp.getWidth(), tmp.getHeight() ));
                    resetParamSetter();
                }
            }).start();

        } else if( e.getSource().equals( colInvertBtn ) ){
            // 色反転処理
            if ( isEditing ) { msgBox("編集中です。", ""); return; }
            isEditing = true;
            addParamSetter(done);
            new Thread( new Runnable(){
                public void run() {
                    ImageUtil tmp = new ImageUtil( imgUtil.getChangedImage() );
                    addStatusMessage(mesLabel);
                    tmp.colorInvert();
                    removeStatusMessage(mesLabel);
                    while( isEditing ) {
                        double scale = (double)slider.getValue() / 100.0;
                        Dimension size = new Dimension( (int)(tmp.getWidth()*scale), (int)(tmp.getHeight()*scale) );
                        changeImageView(size, tmp.getChangedImage());

                      try { Thread.sleep( (int)((1.0 / editFps) * 1000) ); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    msgBox("編集確定！", "");
                    imgUtil = new ImageUtil( tmp.getChangedImage() );
                    updateImageView(new Dimension( tmp.getWidth(), tmp.getHeight() ));
                    resetParamSetter();
                }
            }).start();
        } else if( e.getSource().equals( toSepiaBtn ) ){
            // セピア色処理
            if ( isEditing ) { msgBox("編集中です。", ""); return; }
            isEditing = true;
            addParamSetter(done);
            new Thread( new Runnable(){
                public void run() {
                    ImageUtil tmp = new ImageUtil( imgUtil.getChangedImage() );
                    addStatusMessage(mesLabel);
                    tmp.toSepia();
                    removeStatusMessage(mesLabel);
                    while( isEditing ) {
                        double scale = (double)slider.getValue() / 100.0;
                        Dimension size = new Dimension( (int)(tmp.getWidth()*scale), (int)(tmp.getHeight()*scale) );
                        changeImageView(size, tmp.getChangedImage());

                      try { Thread.sleep( (int)((1.0 / editFps) * 1000) ); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    msgBox("編集確定！", "");
                    imgUtil = new ImageUtil( tmp.getChangedImage() );
                    updateImageView(new Dimension( tmp.getWidth(), tmp.getHeight() ));
                    resetParamSetter();
                }
            }).start();
        } else if( e.getSource().equals( toMosaicBtn ) ){
            // Mosaic処理
            if ( isEditing ) { msgBox("編集中です。", ""); return; }
            isEditing = true;
            paramSetter.add(sizePanel);
            addParamSetter(done);
            new Thread( new Runnable(){
                public void run() {
                    int beforSize = 0;
                    ImageUtil tmp = new ImageUtil( imgUtil.getChangedImage() );
                    while( isEditing ) {
                        if( beforSize != sizePanel.getIntValue() ) {
                            addStatusMessage(mesLabel);
                            tmp = new ImageUtil( imgUtil.getChangedImage() );
                            tmp.toMosaic(sizePanel.getIntValue());
                            removeStatusMessage(mesLabel);
                        }

                        double scale = (double)slider.getValue() / 100.0;
                        Dimension size = new Dimension( (int)(tmp.getWidth()*scale), (int)(tmp.getHeight()*scale) );
                        changeImageView(size, tmp.getChangedImage());

                        beforSize = sizePanel.getIntValue();
                        try { Thread.sleep( (int)((1.0 / editFps) * 1000) ); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    msgBox("編集確定！", "");
                    imgUtil = new ImageUtil( tmp.getChangedImage() );
                    updateImageView(new Dimension( tmp.getWidth(), tmp.getHeight() ));
                    resetParamSetter();
                }
            }).start();
        } else if( e.getSource().equals( toGrayBtn ) ){
            // グレーにする
            if ( isEditing ) { msgBox("編集中です。", ""); return; }
            isEditing = true;
            addParamSetter(done);
            new Thread( new Runnable(){
                public void run() {
                    ImageUtil tmp = new ImageUtil( imgUtil.getChangedImage() );
                    addStatusMessage(mesLabel);
                    tmp.toGray();
                    removeStatusMessage(mesLabel);
                    while( isEditing ) {
                        double scale = (double)slider.getValue() / 100.0;
                        Dimension size = new Dimension( (int)(tmp.getWidth()*scale), (int)(tmp.getHeight()*scale) );
                        changeImageView(size, tmp.getChangedImage());

                      try { Thread.sleep( (int)((1.0 / editFps) * 1000) ); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    msgBox("編集確定！", "");
                    imgUtil = new ImageUtil( tmp.getChangedImage() );
                    updateImageView(new Dimension( tmp.getWidth(), tmp.getHeight() ));
                    resetParamSetter();
                }
            }).start();
        } else if( e.getSource().equals( edgeBtn ) ){
            // エッジ処理
            if ( isEditing ) { msgBox("編集中です。", ""); return; }
            isEditing = true;
            paramSetter.add(powerPanel);
            powerPanel.setIntValue(5);
            addParamSetter(done);
            new Thread( new Runnable(){
                public void run() {
                    int beforPower = 0;
                    ImageUtil tmp = new ImageUtil( imgUtil.getChangedImage() );
                    while( isEditing ) {
                        if( beforPower != powerPanel.getIntValue() ){
                            addStatusMessage(mesLabel);
                            tmp = new ImageUtil( imgUtil.getChangedImage() );
                            tmp.edge(powerPanel.getIntValue());
                            removeStatusMessage(mesLabel);
                            beforPower = powerPanel.getIntValue();
                        }

                        double scale = (double)slider.getValue() / 100.0;
                        Dimension size = new Dimension( (int)(tmp.getWidth()*scale), (int)(tmp.getHeight()*scale) );
                        changeImageView(size, tmp.getChangedImage());

                        try { Thread.sleep( (int)((1.0 / editFps) * 1000) ); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    msgBox("編集確定！", "");
                    imgUtil = new ImageUtil( tmp.getChangedImage() );
                    updateImageView(new Dimension( tmp.getWidth(), tmp.getHeight() ));
                    resetParamSetter();
                }
            }).start();

        } else if( e.getSource().equals( colEdgeBtn ) ){
            // カラーエッジ処理
            if ( isEditing ) { msgBox("編集中です。", ""); return; }
            isEditing = true;
            paramSetter.add(powerPanel);
            powerPanel.setIntValue(5);
            addParamSetter(done);
            new Thread( new Runnable(){
                public void run() {
                    int beforPower = 0;
                    ImageUtil tmp = new ImageUtil( imgUtil.getChangedImage() );
                    while( isEditing ) {
                        if( beforPower != powerPanel.getIntValue() ){
                            addStatusMessage(mesLabel);
                            tmp = new ImageUtil( imgUtil.getChangedImage() );
                            tmp.colorEdge(powerPanel.getIntValue());
                            removeStatusMessage(mesLabel);
                            beforPower = powerPanel.getIntValue();
                        }

                        double scale = (double)slider.getValue() / 100.0;
                        Dimension size = new Dimension( (int)(tmp.getWidth()*scale), (int)(tmp.getHeight()*scale) );
                        changeImageView(size, tmp.getChangedImage());

                        try { Thread.sleep( (int)((1.0 / editFps) * 1000) ); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    msgBox("編集確定！", "");
                    imgUtil = new ImageUtil( tmp.getChangedImage() );
                    updateImageView(new Dimension( tmp.getWidth(), tmp.getHeight() ));
                    resetParamSetter();
                }
            }).start();
        } else if( e.getSource().equals( blurBtn ) ){
            // ぼかし処理
            if ( isEditing ) { msgBox("編集中です。", ""); return; }
            isEditing = true;
            paramSetter.add( powerPanel );
            addParamSetter(done);
            new Thread( new Runnable(){
                public void run() {
                    int beforPower = 0;
                    ImageUtil tmp = new ImageUtil( imgUtil.getChangedImage() );
                    while( isEditing ) {
                        if( beforPower != powerPanel.getIntValue() ){
                            addStatusMessage(mesLabel);
                            tmp = new ImageUtil( imgUtil.getChangedImage() );
                            tmp.blur(powerPanel.getIntValue());
                            removeStatusMessage(mesLabel);
                            beforPower = powerPanel.getIntValue();
                        }
                        double scale = (double)slider.getValue() / 100.0;
                        Dimension size = new Dimension( (int)(tmp.getWidth()*scale), (int)(tmp.getHeight()*scale) );
                        changeImageView(size, tmp.getChangedImage());

                        try { Thread.sleep( (int)((1.0 / editFps) * 1000) ); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    msgBox("編集確定！", "");
                    imgUtil = new ImageUtil( tmp.getChangedImage() );
                    updateImageView(new Dimension( tmp.getWidth(), tmp.getHeight() ));
                    resetParamSetter();
                }
            }).start();
        } else if( e.getSource().equals( unsharpmaskBtn ) ){
            // アンシャープマスク処理
            if ( isEditing ) { msgBox("編集中です。", ""); return; }
            isEditing = true;
            paramSetter.add(powerPanel);
            paramSetter.add(amountPanel);
            paramSetter.add(thresholdPanel);
            addParamSetter(done);
            new Thread( new Runnable(){
                public void run() {
                    int beforPower = 0;
                    int beforAmount = 0;
                    int beforThreshold = 0;
                    ImageUtil tmp = new ImageUtil( imgUtil.getChangedImage() );
                    while( isEditing ) {
                        if ( beforPower != powerPanel.getIntValue() || beforAmount != amountPanel.getIntValue() || beforThreshold != thresholdPanel.getIntValue() ) {
                            addStatusMessage(mesLabel);
                            tmp = new ImageUtil( imgUtil.getChangedImage() );
                            tmp.unsharpmask(powerPanel.getIntValue(), amountPanel.getIntValue() / 100, thresholdPanel.getIntValue());
                            removeStatusMessage(mesLabel);
                            beforPower = powerPanel.getIntValue();
                            beforAmount = amountPanel.getIntValue();
                            beforThreshold = thresholdPanel.getIntValue();
                        }

                        double scale = (double)slider.getValue() / 100.0;
                        Dimension size = new Dimension( (int)(tmp.getWidth()*scale), (int)(tmp.getHeight()*scale) );
                        changeImageView(size, tmp.getChangedImage());

                        try { Thread.sleep( (int)((1.0 / editFps) * 1000) ); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    msgBox("編集確定！", "");
                    imgUtil = new ImageUtil( tmp.getChangedImage() );
                    updateImageView(new Dimension( tmp.getWidth(), tmp.getHeight() ));
                    resetParamSetter();
                }
            }).start();
        } else if( e.getSource().equals( roteBtn ) ) {
            // 回転処理
            if ( isEditing ) { msgBox("編集中です。", ""); return; }
            isEditing = true;
            IntegerInputPanel degPanel = new IntegerInputPanel("度");
            degPanel.setIntValue(0);
            paramSetter.add(degPanel);
            addParamSetter(done);
            new Thread( new Runnable(){
                public void run() {
                    int beforDeg = 0;
                    ImageUtil tmp = new ImageUtil( imgUtil.getChangedImage() );
                    while( isEditing ) {
                        if( beforDeg != degPanel.getIntValue() ) {
                            addStatusMessage(mesLabel);
                            tmp = new ImageUtil( imgUtil.getChangedImage() );
                            tmp.rote(degPanel.getIntValue());
                            beforDeg = degPanel.getIntValue();
                            removeStatusMessage(mesLabel);
                        }

                        double scale = (double)slider.getValue() / 100.0;
                        Dimension size = new Dimension( (int)(tmp.getWidth()*scale), (int)(tmp.getHeight()*scale) );
                        changeImageView(size, tmp.getChangedImage());

                      try { Thread.sleep( (int)((1.0 / editFps) * 1000) ); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    msgBox("編集確定！", "");
                    imgUtil = new ImageUtil( tmp.getChangedImage() );
                    updateImageView(new Dimension( tmp.getWidth(), tmp.getHeight() ));
                    resetParamSetter();
                }
            }).start();

        } else if( e.getSource().equals( gammaBtn ) ) {
            if ( isEditing ) { msgBox("編集中です。", ""); return; }
            isEditing = true;
            IntegerInputPanel gammmaPanel = new IntegerInputPanel("ガンマ(％)");
            gammmaPanel.setIntValue(100);
            paramSetter.add(gammmaPanel);
            addParamSetter(done);
            new Thread( new Runnable(){
                public void run() {
                    int beforGamma = 0;
                    ImageUtil tmp = new ImageUtil( imgUtil.getChangedImage() );
                    while( isEditing ) {
                        if ( beforGamma != gammmaPanel.getIntValue() ) {
                            addStatusMessage(mesLabel);
                            tmp = new ImageUtil( imgUtil.getChangedImage() );
                            tmp.gammaCorrection(((float) gammmaPanel.getIntValue() / 100.0f));
                            beforGamma = gammmaPanel.getIntValue();
                            removeStatusMessage(mesLabel);
                        }

                        double scale = (double)slider.getValue() / 100.0;
                        Dimension size = new Dimension( (int)(tmp.getWidth()*scale), (int)(tmp.getHeight()*scale) );
                        changeImageView(size, tmp.getChangedImage());

                      try { Thread.sleep( (int)((1.0 / editFps) * 1000) ); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    msgBox("編集確定！", "");
                    imgUtil = new ImageUtil( tmp.getChangedImage() );
                    updateImageView(new Dimension( tmp.getWidth(), tmp.getHeight() ));
                    resetParamSetter();
                }
            }).start();

        } else if( e.getSource().equals( addImageBtn ) ) {
            if ( isEditing ) { msgBox("編集中です。", ""); return; }
            isEditing = true;
            JCheckBox keepAspect = new JCheckBox("縦横比を維持する");
            keepAspect.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    if ( !keepAspect.isSelected() ) { hPanel.setCompEnable(true); return; }
                    hPanel.setCompEnable(false);
                }
            });
            JButton imgSelectBtn = new JButton("画像を選択");
            WithLabelComponent imgSelectBtnWithLabel = new WithLabelComponent("", imgSelectBtn);
            imgSelectBtnWithLabel.setBackground(java.awt.Color.gray);
            imgSelectBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    File f = openFileSelect();
                    if( !ImageUtil.Companion.isImageFile(f) ) { return; }
                    imgSelectBtnWithLabel.setLabelText( f.getAbsolutePath() );
                }
            });
            paramSetter.add(imgSelectBtnWithLabel);
            paramSetter.add(xPanel);
            paramSetter.add(yPanel);
            paramSetter.add(wPanel);
            paramSetter.add(hPanel);
            paramSetter.add(keepAspect);
            addParamSetter(done);
            new Thread( new Runnable(){
                public void run() {
                    String beforPath = "";
                    int beforX = xPanel.getIntValue();
                    int beforY = yPanel.getIntValue();
                    int beforW = wPanel.getIntValue();
                    int beforH = hPanel.getIntValue();
                    ImageUtil tmp = new ImageUtil( imgUtil.getChangedImage() );
                    while( isEditing ) {
                        if ( beforPath != imgSelectBtnWithLabel.getLabelText() ){
                            addStatusMessage(mesLabel);
                            try {
                                tmp = new ImageUtil( imgUtil.getChangedImage() );
                                BufferedImage buffImg = ImageIO.read(new File(imgSelectBtnWithLabel.getLabelText()));
                                wPanel.setIntValue( buffImg.getWidth() );
                                hPanel.setIntValue( buffImg.getHeight() );
                                tmp.addImage(buffImg, xPanel.getIntValue(), yPanel.getIntValue(), wPanel.getIntValue(), hPanel.getIntValue());

                                beforH = hPanel.getIntValue();
                                beforW = wPanel.getIntValue();
                                beforX = xPanel.getIntValue();
                                beforY = yPanel.getIntValue();
                                beforPath = imgSelectBtnWithLabel.getLabelText();
                            } catch (IOException e1) { e1.printStackTrace(); }
                            removeStatusMessage(mesLabel);

                        }else if( beforH != hPanel.getIntValue() || beforW != wPanel.getIntValue() || beforX != xPanel.getIntValue() || beforY != yPanel.getIntValue() ) {
                            addStatusMessage(mesLabel);
                            if( !keepAspect.isSelected()) {
                                try {
                                    tmp = new ImageUtil( imgUtil.getChangedImage() );
                                    BufferedImage buffImg = ImageIO.read(new File(imgSelectBtnWithLabel.getLabelText()));
                                    tmp.addImage(buffImg, xPanel.getIntValue(), yPanel.getIntValue(), wPanel.getIntValue(), hPanel.getIntValue());

                                    beforH = hPanel.getIntValue();
                                    beforW = wPanel.getIntValue();
                                    beforX = xPanel.getIntValue();
                                    beforY = yPanel.getIntValue();
                                    beforPath = imgSelectBtnWithLabel.getLabelText();
                                } catch (IOException e1) { e1.printStackTrace(); }
                            }else {
                                try {
                                    tmp = new ImageUtil( imgUtil.getChangedImage() );
                                    BufferedImage buffImg = ImageIO.read(new File(imgSelectBtnWithLabel.getLabelText()));
                                    double wPerH = ((double)buffImg.getWidth() / buffImg.getHeight());
                                    hPanel.setIntValue((int)(wPanel.getIntValue()/wPerH));
                                    tmp.addImage(buffImg, xPanel.getIntValue(), yPanel.getIntValue(), wPanel.getIntValue(), hPanel.getIntValue());

                                    beforH = hPanel.getIntValue();
                                    beforW = wPanel.getIntValue();
                                    beforX = xPanel.getIntValue();
                                    beforY = yPanel.getIntValue();
                                    beforPath = imgSelectBtnWithLabel.getLabelText();
                                } catch (IOException e1) { e1.printStackTrace(); }
                            }
                            removeStatusMessage(mesLabel);
                        }

                        double scale = (double)slider.getValue() / 100.0;
                        Dimension size = new Dimension( (int)(tmp.getWidth()*scale), (int)(tmp.getHeight()*scale) );
                        changeImageView(size, tmp.getChangedImage());
                        try { Thread.sleep( (int)((1.0 / editFps) * 1000) ); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    msgBox("編集確定！", "");
                    imgUtil = new ImageUtil( tmp.getChangedImage() );
                    updateImageView(new Dimension( tmp.getWidth(), tmp.getHeight() ));
                    resetParamSetter();
                }
            }).start();

        } else if( e.getSource().equals( adjustmentRgbBtn ) ) {
            // RGB 調整
            if ( isEditing ) { msgBox("編集中です。", ""); return; }
            isEditing = true;
            IntegerInputPanel rInputPanel = new IntegerInputPanel("R(%):");
            IntegerInputPanel gInputPanel = new IntegerInputPanel("G(%):");
            IntegerInputPanel bInputPanel = new IntegerInputPanel("B(%):");
            rInputPanel.setIntValue(100);
            gInputPanel.setIntValue(100);
            bInputPanel.setIntValue(100);
            paramSetter.add( rInputPanel );
            paramSetter.add( gInputPanel );
            paramSetter.add( bInputPanel );
            addParamSetter(done);
            new Thread( new Runnable(){
                public void run() {
                    int beforR = 100;
                    int beforG = 100;
                    int beforB = 100;
                    ImageUtil tmp = new ImageUtil( imgUtil.getChangedImage() );
                    while( isEditing ) {
                        if ( beforR != rInputPanel.getIntValue() || beforG != gInputPanel.getIntValue() || beforB != bInputPanel.getIntValue() ) {
                            tmp = new ImageUtil( imgUtil.getChangedImage() );
                            addStatusMessage(mesLabel);
                            tmp.adjustmentRGB(((float)rInputPanel.getIntValue() / 100.0f), ((float)gInputPanel.getIntValue() / 100.0f), ((float)bInputPanel.getIntValue() / 100.0f));
                            removeStatusMessage(mesLabel);
                            beforR = rInputPanel.getIntValue();
                            beforG = gInputPanel.getIntValue();
                            beforB = bInputPanel.getIntValue();
                        }
                        // tmp.addText(strInput.getText(), new Font((String)fontCombo.getSelectedItem(), (boldStyle.isSelected() ? Font.BOLD : Font.PLAIN) | ( italicCheck.isSelected() ? Font.ITALIC : Font.PLAIN ), fontSizePanel.getIntValue() ), colSelectBtn.getForeground(), xPanel.getIntValue(), yPanel.getIntValue());

                        double scale = (double)slider.getValue() / 100.0;
                        Dimension size = new Dimension( (int)(tmp.getWidth()*scale), (int)(tmp.getHeight()*scale) );
                        changeImageView(size, tmp.getChangedImage());

                      try { Thread.sleep( (int)((1.0 / editFps) * 1000) ); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    msgBox("編集確定！", "");
                    imgUtil = new ImageUtil( tmp.getChangedImage() );
                    updateImageView(new Dimension( tmp.getWidth(), tmp.getHeight() ));
                    resetParamSetter();
                }
            }).start();

        } else if( e.getSource().equals( changeHueBtn )) {
            if ( isEditing ) { msgBox("編集中です。", ""); return; }
            isEditing = true;
            IntegerInputPanel huePanel = new IntegerInputPanel("色合い:");
            paramSetter.add(xPanel);
            paramSetter.add(yPanel);
            paramSetter.add(wPanel);
            paramSetter.add(hPanel);
            paramSetter.add(huePanel);
            addParamSetter(done);
            new Thread( new Runnable(){
                public void run() {
                    int beforX = 0;
                    int beforY = 0;
                    int beforW = 0;
                    int beforH = 0;
                    int beforHue = 0;
                    ImageUtil tmp = new ImageUtil( imgUtil.getChangedImage() );
                    while( isEditing ) {
                        if( beforX != xPanel.getIntValue() || beforY != yPanel.getIntValue() || beforW != wPanel.getIntValue() || beforH != hPanel.getIntValue() || beforHue != huePanel.getIntValue()) {
                            addStatusMessage(mesLabel);
                            tmp = new ImageUtil( imgUtil.getChangedImage() );
                            tmp.changeHue(xPanel.getIntValue(), yPanel.getIntValue(), wPanel.getIntValue(), hPanel.getIntValue(), huePanel.getIntValue());
                            removeStatusMessage(mesLabel);
                            beforX = xPanel.getIntValue();
                            beforY = yPanel.getIntValue();
                            beforW = wPanel.getIntValue();
                            beforH = hPanel.getIntValue();
                            beforHue = huePanel.getIntValue();
                        }

                        double scale = (double)slider.getValue() / 100.0;
                        Dimension size = new Dimension( (int)(tmp.getWidth()*scale), (int)(tmp.getHeight()*scale) );
                        changeImageView(size, tmp.getChangedImage());

                      try { Thread.sleep( (int)((1.0 / editFps) * 1000) ); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    msgBox("編集確定！", "");
                    imgUtil = new ImageUtil( tmp.getChangedImage() );
                    updateImageView(new Dimension( tmp.getWidth(), tmp.getHeight() ));
                    resetParamSetter();
                }
            }).start();

        } else if( e.getSource().equals( changeSaturationBtn ) ) {
            if ( isEditing ) { msgBox("編集中です。", ""); return; }
            isEditing = true;
            IntegerInputPanel saturation = new IntegerInputPanel("鮮やかさ(%):");
            saturation.setIntValue(100);
            paramSetter.add(xPanel);
            paramSetter.add(yPanel);
            paramSetter.add(wPanel);
            paramSetter.add(hPanel);
            paramSetter.add(saturation);
            addParamSetter(done);
            new Thread( new Runnable(){
                public void run() {
                    int beforX = 0;
                    int beforY = 0;
                    int beforW = 0;
                    int beforH = 0;
                    int beforSaturation = 0;
                    ImageUtil tmp = new ImageUtil( imgUtil.getChangedImage() );
                    while( isEditing ) {
                        if ( beforX != xPanel.getIntValue() || beforY != yPanel.getIntValue() || beforW != wPanel.getIntValue() || beforH != hPanel.getIntValue() || beforSaturation != saturation.getIntValue() ) {
                            addStatusMessage(mesLabel);
                            tmp = new ImageUtil( imgUtil.getChangedImage() );
                            tmp.changeSaturation(xPanel.getIntValue(), yPanel.getIntValue(), wPanel.getIntValue(), hPanel.getIntValue(), ((float)saturation.getIntValue() / 100.0f));
                            beforX = xPanel.getIntValue();
                            beforY = yPanel.getIntValue();
                            beforW = wPanel.getIntValue();
                            beforH = hPanel.getIntValue();
                            beforSaturation = saturation.getIntValue();
                            removeStatusMessage(mesLabel);
                        }

                        double scale = (double)slider.getValue() / 100.0;
                        Dimension size = new Dimension( (int)(tmp.getWidth()*scale), (int)(tmp.getHeight()*scale) );
                        changeImageView(size, tmp.getChangedImage());

                      try { Thread.sleep( (int)((1.0 / editFps) * 1000) ); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    msgBox("編集確定！", "");
                    imgUtil = new ImageUtil( tmp.getChangedImage() );
                    updateImageView(new Dimension( tmp.getWidth(), tmp.getHeight() ));
                    resetParamSetter();
                }
            }).start();

        } else if( e.getSource().equals( changeBrightnessBtn ) ) {
            if ( isEditing ) { msgBox("編集中です。", ""); return; }
            isEditing = true;
            IntegerInputPanel brightness = new IntegerInputPanel("明るさ(%):");
            brightness.setIntValue(100);
            paramSetter.add(xPanel);
            paramSetter.add(yPanel);
            paramSetter.add(wPanel);
            paramSetter.add(hPanel);
            paramSetter.add(brightness);
            addParamSetter(done);
            new Thread( new Runnable(){
                public void run() {
                    int beforX = 0;
                    int beforY = 0;
                    int beforW = 0;
                    int beforH = 0;
                    int beforBrightness = 0;
                    ImageUtil tmp = new ImageUtil( imgUtil.getChangedImage() );
                    while( isEditing ) {
                        if ( beforX != xPanel.getIntValue() || beforY != yPanel.getIntValue() || beforW != wPanel.getIntValue() || beforH != hPanel.getIntValue() || beforBrightness != brightness.getIntValue() ) {
                            addStatusMessage(mesLabel);
                            tmp = new ImageUtil( imgUtil.getChangedImage() );
                            tmp.changeBrightness(xPanel.getIntValue(), yPanel.getIntValue(), wPanel.getIntValue(), hPanel.getIntValue(), ((float)brightness.getIntValue() / 100.0f));
                            removeStatusMessage(mesLabel);

                            beforX = xPanel.getIntValue();
                            beforY = yPanel.getIntValue();
                            beforW = wPanel.getIntValue();
                            beforH = hPanel.getIntValue();
                            beforBrightness = brightness.getIntValue();
                        }

                        double scale = (double)slider.getValue() / 100.0;
                        Dimension size = new Dimension( (int)(tmp.getWidth()*scale), (int)(tmp.getHeight()*scale) );
                        changeImageView(size, tmp.getChangedImage());

                      try { Thread.sleep( (int)((1.0 / editFps) * 1000) ); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    msgBox("編集確定！", "");
                    imgUtil = new ImageUtil( tmp.getChangedImage() );
                    updateImageView(new Dimension( tmp.getWidth(), tmp.getHeight() ));
                    resetParamSetter();
                }
            }).start();

        } else if( e.getSource().equals( drawBtn ) ) {
            if ( isEditing ) { msgBox("編集中です。", ""); return; }
            isEditing = true;
            draggedPoint = new Point(-1, -1);
            String[] penKinds = { "まる", "四角" };
            JComboBox<String> penKindsBox = new JComboBox<String>(penKinds);
            thicknessPanel.setIntValue(30);
            paramSetter.add( penKindsBox );
            paramSetter.add( thicknessPanel );
            paramSetter.add( colSelectBtn );
            addParamSetter(done);
            new Thread( new Runnable(){
                public void run() {
                    ImageUtil tmp = new ImageUtil( imgUtil.getChangedImage() );
                    Point beforPoint = new Point(0,0);
                    tmp = new ImageUtil( imgUtil.getChangedImage() );
                    while( isEditing ) {
                        if( draggedPoint.x != -1 && draggedPoint.y != -1 ) {
                            if( penKindsBox.getSelectedItem().equals("まる") ){
                                if ( !draggedPoint.equals( beforPoint ) ) {
                                    tmp.addFilledCircle(colSelectBtn.getForeground(), draggedPoint.x-thicknessPanel.getIntValue()*3/4, draggedPoint.y-thicknessPanel.getIntValue()*3/4, thicknessPanel.getIntValue());
                                    beforPoint = new Point(draggedPoint.x, draggedPoint.y);
                                }
                            }
                            else{
                                if ( !draggedPoint.equals( beforPoint ) ) {
                                    tmp.addFilledRect(colSelectBtn.getForeground(), draggedPoint.x-thicknessPanel.getIntValue()*3/4, draggedPoint.y-thicknessPanel.getIntValue()*3/4, thicknessPanel.getIntValue(), thicknessPanel.getIntValue());
                                    beforPoint = new Point(draggedPoint.x, draggedPoint.y);
                                }
                            }
                        }

                        double scale = (double)slider.getValue() / 100.0;
                        Dimension size = new Dimension( (int)(tmp.getWidth()*scale), (int)(tmp.getHeight()*scale) );
                        changeImageView(size, tmp.getChangedImage());

                      try { Thread.sleep( (int)((1.0 / editFps) * 1000) ); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    msgBox("編集確定！", "");
                    imgUtil = new ImageUtil( tmp.getChangedImage() );
                    updateImageView(new Dimension( tmp.getWidth(), tmp.getHeight() ));
                    resetParamSetter();
                }
            }).start();

        } else if( e.getSource().equals( addShapeBtn ) ) {
            if ( isEditing ) { msgBox("編集中です。", ""); return; }
            isEditing = true;
            IntegerInputPanel arcSizePanel = new IntegerInputPanel("角の丸さ");
            arcSizePanel.setIntValue(5);
            IntegerInputPanel startAnglePanel = new IntegerInputPanel("開始角度(0°=3時方向)");
            startAnglePanel.setIntValue(45);
            IntegerInputPanel endAnglePanel = new IntegerInputPanel("終端角度(0°=3時方向)");
            endAnglePanel.setIntValue(270);
            JCheckBox isFilled = new JCheckBox("塗りつぶし");
            JComboBox<String> figureBox = new JComboBox<String>( "長方形,楕円,丸角長方形,円弧".split(",") );
            figureBox.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    switch ( figureBox.getSelectedIndex() ) {
                        case 0:
                            paramSetter.removeAll();
                            paramSetter.add(xPanel);
                            paramSetter.add(yPanel);
                            paramSetter.add(wPanel);
                            paramSetter.add(hPanel);
                            paramSetter.add(colSelectBtn);
                            paramSetter.add(isFilled);
                            paramSetter.add(figureBox);
                        break;
                        case 1:
                            paramSetter.removeAll();
                            paramSetter.add(xPanel);
                            paramSetter.add(yPanel);
                            paramSetter.add(wPanel);
                            paramSetter.add(hPanel);
                            paramSetter.add(colSelectBtn);
                            paramSetter.add(isFilled);
                            paramSetter.add(figureBox);
                        break;
                        case 2:
                            paramSetter.removeAll();
                            paramSetter.add(xPanel);
                            paramSetter.add(yPanel);
                            paramSetter.add(wPanel);
                            paramSetter.add(hPanel);
                            paramSetter.add(arcSizePanel);
                            paramSetter.add(colSelectBtn);
                            paramSetter.add(isFilled);
                            paramSetter.add(figureBox);
                        break;
                        case 3:
                            paramSetter.removeAll();
                            paramSetter.add(xPanel);
                            paramSetter.add(yPanel);
                            paramSetter.add(wPanel);
                            paramSetter.add(hPanel);
                            paramSetter.add(startAnglePanel);
                            paramSetter.add(endAnglePanel);
                            paramSetter.add(colSelectBtn);
                            paramSetter.add(isFilled);
                            paramSetter.add(figureBox);
                        break;
                        default:
                            System.out.println("error");
                        break;
                    }
                    addParamSetter(done);
                }
            });
            paramSetter.add(xPanel);
            paramSetter.add(yPanel);
            paramSetter.add(wPanel);
            paramSetter.add(hPanel);
            paramSetter.add(colSelectBtn);
            paramSetter.add(isFilled);
            paramSetter.add(figureBox);
            addParamSetter(done);
            new Thread( new Runnable(){
                public void run() {
                    int beforX = 0;
                    int beforY = 0;
                    int beforW = 0;
                    int beforH = 0;
                    int beforRGB = 0;
                    int beforArcSize = 0;
                    int beforstartAngle = 0;
                    int beforendAngle = 0;
                    int beforFigureIdx = 0;
                    boolean beforisFilled = false;
                    ImageUtil tmp = new ImageUtil( imgUtil.getChangedImage() );
                    while( isEditing ) {
                        if( beforX != xPanel.getIntValue() || beforY != yPanel.getIntValue() || beforW != wPanel.getIntValue() || beforH != hPanel.getIntValue() || beforRGB != colSelectBtn.getForeground().getRGB() || beforArcSize != arcSizePanel.getIntValue() || beforstartAngle != startAnglePanel.getIntValue() || beforendAngle != endAnglePanel.getIntValue() || beforisFilled != isFilled.isSelected() || beforFigureIdx != figureBox.getSelectedIndex() ) {
                            tmp = new ImageUtil( imgUtil.getChangedImage() );
                            if( isFilled.isSelected() ) {
                                switch ( figureBox.getSelectedIndex() ) {
                                    case 0:
                                        tmp.addFilledRect( colSelectBtn.getForeground(), xPanel.getIntValue(), yPanel.getIntValue(), wPanel.getIntValue(), hPanel.getIntValue());
                                    break;
                                    case 1:
                                        tmp.addFilledOval( colSelectBtn.getForeground(), xPanel.getIntValue(), yPanel.getIntValue(), wPanel.getIntValue(), hPanel.getIntValue() );
                                    break;
                                    case 2:
                                        tmp.addFilledRoundedRect( colSelectBtn.getForeground(), xPanel.getIntValue(), yPanel.getIntValue(), wPanel.getIntValue(), hPanel.getIntValue(), arcSizePanel.getIntValue(), arcSizePanel.getIntValue());
                                    break;
                                    case 3:
                                        System.out.println("Arc");
                                        tmp.addFilledArc( colSelectBtn.getForeground(), xPanel.getIntValue(), yPanel.getIntValue(), wPanel.getIntValue(), hPanel.getIntValue(), startAnglePanel.getIntValue(), endAnglePanel.getIntValue());
                                    break;
                                    default:
                                        System.out.println("error");
                                    break;
                                }
                            }else {
                                switch ( figureBox.getSelectedIndex() ) {
                                    case 0:
                                        tmp.addLineRect( colSelectBtn.getForeground(), xPanel.getIntValue(), yPanel.getIntValue(), wPanel.getIntValue(), hPanel.getIntValue());
                                    break;
                                    case 1:
                                        tmp.addLineOval( colSelectBtn.getForeground(), xPanel.getIntValue(), yPanel.getIntValue(), wPanel.getIntValue(), hPanel.getIntValue() );
                                    break;
                                    case 2:
                                        tmp.addLineRoundedRect( colSelectBtn.getForeground(), xPanel.getIntValue(), yPanel.getIntValue(), wPanel.getIntValue(), hPanel.getIntValue(), arcSizePanel.getIntValue(), arcSizePanel.getIntValue());
                                    break;
                                    case 3:
                                        tmp.addArcLine( colSelectBtn.getForeground(), xPanel.getIntValue(), yPanel.getIntValue(), wPanel.getIntValue(), hPanel.getIntValue(), startAnglePanel.getIntValue(), endAnglePanel.getIntValue());
                                    break;
                                    default:
                                        System.out.println("error");
                                    break;
                                }
                            }
                            beforX = xPanel.getIntValue();
                            beforY = yPanel.getIntValue();
                            beforW = wPanel.getIntValue();
                            beforH = hPanel.getIntValue();
                            beforRGB = colSelectBtn.getForeground().getRGB();
                            beforArcSize = arcSizePanel.getIntValue();
                            beforstartAngle = startAnglePanel.getIntValue();
                            beforendAngle = endAnglePanel.getIntValue();
                            beforisFilled = isFilled.isSelected();
                            beforFigureIdx = figureBox.getSelectedIndex();
                        }

                        double scale = (double)slider.getValue() / 100.0;
                        Dimension size = new Dimension( (int)(tmp.getWidth()*scale), (int)(tmp.getHeight()*scale) );
                        changeImageView(size, tmp.getChangedImage());

                      try { Thread.sleep( (int)((1.0 / editFps) * 1000) ); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                    msgBox("編集確定！", "");
                    imgUtil = new ImageUtil( tmp.getChangedImage() );
                    updateImageView(new Dimension( tmp.getWidth(), tmp.getHeight() ));
                    resetParamSetter();
                }
            }).start();

        } else if( e.getActionCommand().equals( done.getText() ) ) {
            isEditing = false;
        }
    }
}
