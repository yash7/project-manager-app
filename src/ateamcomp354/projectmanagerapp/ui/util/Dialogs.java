package ateamcomp354.projectmanagerapp.ui.util;

import javax.swing.*;
import java.awt.*;

public class Dialogs {

    public static int dirty( Component parent, String item ) {

        return JOptionPane.showConfirmDialog(
                parent,
                "Would you like to save " + item + " first?",
                "You forgot to save",
                JOptionPane.YES_NO_CANCEL_OPTION
        );
    }
}
