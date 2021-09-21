import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class IntegerDocFilter extends DocumentFilter {
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        int val = 0;
        try { val = Integer.parseInt(text); } catch (Exception e) { return; }
        text = String.valueOf(val);
        super.replace(fb, offset, length, text, attrs);
    }
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        int val = 0;
        try { val = Integer.parseInt(string); } catch (Exception e) { return; }
        string = String.valueOf(val);
        super.insertString(fb, offset, string, attr);
    }
    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        String strBefore = fb.getDocument().getText(0, fb.getDocument().getLength());
        String strAfter  = strBefore.substring(0, offset) + strBefore.substring(offset+length, fb.getDocument().getLength());
        if ( strAfter.equals("") ) { return; }
        super.remove(fb, offset, length);
    }
}