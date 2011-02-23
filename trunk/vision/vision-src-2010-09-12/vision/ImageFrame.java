package vision;

import java.awt.BorderLayout;
import java.awt.Image;

/**
 *
 * @author Kotuc
 */
public class ImageFrame extends javax.swing.JFrame {

    private final ImagePanel iPanel;

    public ImageFrame(String title) {
        super(title);
        initComponents();
        iPanel = new ImagePanel();
        add(iPanel, BorderLayout.CENTER);
            this.setVisible(true);
    }

    public void setImage(Image image) {
        iPanel.setImage(image);
        
        this.pack();
//        this.setVisible(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ImageFrame("ImgFrame").setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
