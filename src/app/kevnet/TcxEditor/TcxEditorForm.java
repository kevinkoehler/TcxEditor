package app.kevnet.TcxEditor;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TcxEditorForm {

  private static final String TITLE = "TCX Editor";
  private static final String FILE_NAME_FILTER_DESCRIPTION = "Garmin Training Center XML Files";
  private static final String FILE_TYPE = "tcx";
  public static final String EXTENSION = "." + FILE_TYPE;
  private static final String SUCCESS_TITLE = "Success";
  private static final String SUCCESS_MESSAGE = "TCX file has been parsed successfully."
      + "Updated file has been saved as _modified.tcx in the same directory as the input file.";
  private static final String ERROR_TITLE = "Error";
  private static final String ERROR_MESSAGE = "Unable to parse file. "
      + "Please ensure the input file is a valid TCX file.";
  private static final String INVALID_FILE_TITLE = "Invalid File";
  private static final String INVALID_FILE_MESSAGE = "Please select a valid TCX file.";
  private static final String INVALID_MULTIPLIER = "Invalid Multiplier";
  private static final String INVALID_MULTIPLIER_MESSAGE =
      "Please enter a valid number for the multiplier.";
  private static final String USER_HOME_PROPERTY = "user.home";
  private static final String ICON_PATH = "/resources/Icon.png";
  private JLabel lblPath;
  private JTextField txtPath;
  private JLabel lblMultiplier;
  private JTextField txtMultiplier;
  private JButton btnRun;
  private JPanel pnlMain;
  private JButton btnBrowse;
  private JButton btnExit;

  /**
   * UI for the TcxEditor application.
   */
  public TcxEditorForm() {
    txtPath.setText(System.getProperty(USER_HOME_PROPERTY));
    btnRun.addActionListener(e -> {
      String filePath = txtPath.getText();
      if (filePath == null || filePath.trim().isEmpty() || !filePath.toLowerCase()
          .endsWith(EXTENSION)) {
        JOptionPane.showMessageDialog(null, INVALID_FILE_MESSAGE, INVALID_FILE_TITLE,
            JOptionPane.WARNING_MESSAGE);
        return;
      }

      String multiplier = txtMultiplier.getText();
      if (multiplier == null || multiplier.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, INVALID_MULTIPLIER_MESSAGE, INVALID_MULTIPLIER,
            JOptionPane.WARNING_MESSAGE);
        return;
      }

      TcxParser parser = null;
      try {
        parser = new TcxParser(txtPath.getText(),
            Double.valueOf(txtMultiplier.getText()));
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(null, INVALID_MULTIPLIER_MESSAGE, INVALID_MULTIPLIER,
            JOptionPane.WARNING_MESSAGE);
      }
      if (parser != null && parser.parseFile()) {
        JOptionPane.showMessageDialog(null, SUCCESS_MESSAGE, SUCCESS_TITLE,
            JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      JOptionPane.showMessageDialog(null, ERROR_MESSAGE, ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    });
    btnBrowse.addActionListener(e -> {
      JFileChooser chooser = new JFileChooser();
      FileFilter filter = new FileNameExtensionFilter(FILE_NAME_FILTER_DESCRIPTION, FILE_TYPE);
      chooser.setFileFilter(filter);
      String filePath = txtPath.getText();
      if (filePath != null && !filePath.trim().isEmpty()) {
        File currentFile = new File(filePath);
        if (currentFile.isFile()) {
          currentFile = currentFile.getParentFile();
        }
        chooser.setCurrentDirectory(currentFile);
      }
      int choice = chooser.showOpenDialog(null);
      if (choice != JFileChooser.APPROVE_OPTION) {
        return;
      }
      File chosenFile = chooser.getSelectedFile();
      if (chosenFile != null && chosenFile.isFile()) {
        txtPath.setText(chosenFile.getAbsolutePath());
      }
    });
    btnExit.addActionListener(e -> System.exit(0));
  }

  /**
   * Main method to start the application.
   *
   * @param args Provided arguments. TcxEditor does not use any provided arguments to it's main
   * method.
   */
  public static void main(final String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException
        | IllegalAccessException | UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
    JFrame frame = new JFrame(TITLE);
    frame.setContentPane(new TcxEditorForm().pnlMain);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    try {
      frame.setIconImage(ImageIO.read(TcxEditorForm.class.getResource(ICON_PATH)));
    } catch (IOException e) {
      e.printStackTrace();
    }
    frame.pack();
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation(dim.width / 2 - frame.getSize().width / 2,
        dim.height / 2 - frame.getSize().height / 2);
    frame.setVisible(true);
  }
}
