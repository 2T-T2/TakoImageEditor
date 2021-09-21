
public class WithLabelComponent extends javax.swing.JPanel {
    protected java.awt.Component comp;
    protected javax.swing.JLabel jl;
    public WithLabelComponent(String labelStr, java.awt.Component comp) { this(labelStr, comp, new java.awt.FlowLayout()); }
    public WithLabelComponent(String labelStr, java.awt.Component comp, java.awt.LayoutManager mgr) {
        setLayout(mgr);
        jl = new javax.swing.JLabel( labelStr );
        this.comp = comp;
        add(jl);
        add(this.comp);
    }
    public void setCompEnable(boolean b) {
        comp.setEnabled(b);
    }
    public void setLabelText(String str) {
        jl.setText(str);
    }
    public String getLabelText() {
        return jl.getText();
    }
}
