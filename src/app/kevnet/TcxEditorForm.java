package app.kevnet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TcxEditorForm {

  private JLabel lblPath;
  private JTextField txtPath;
  private JLabel lblDistanceSpeedMultiplier;
  private JTextField txtDistanceSpeedMultiplier;
  private JButton btnRun;
  private JPanel pnlMain;


  public TcxEditorForm() {
    btnRun.addActionListener(e -> {
      TcxParser parser = new TcxParser(txtPath.getText(),
          Double.valueOf(txtDistanceSpeedMultiplier.getText()));
      parser.updateFile();
    });
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame("TcxEditor");
    frame.setContentPane(new TcxEditorForm().pnlMain);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }
}
