
public class IntegerInputPanel extends WithLabelComponent {
    public IntegerInputPanel(String labelStr) {
        super(labelStr, new javax.swing.JTextField("30"));
        setLayout( new java.awt.FlowLayout() );
        setBackground(java.awt.Color.gray);

        comp.setPreferredSize(new java.awt.Dimension(37,20));
        ((javax.swing.text.PlainDocument)(((javax.swing.JTextField)comp).getDocument())).setDocumentFilter(new IntegerDocFilter());
    }
    public int getIntValue() {
        try { return Integer.parseInt( ((javax.swing.JTextField)comp).getText() ); }
        catch (Exception e) { return 0; }
    }
    public void setIntValue(int val) {
        ((javax.swing.JTextField)comp).setText(""+val);
    }
}
