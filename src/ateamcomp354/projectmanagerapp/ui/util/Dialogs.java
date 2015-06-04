package ateamcomp354.projectmanagerapp.ui.util;

import javax.swing.*;
import java.awt.*;

/**
 * A collection of dialog functions that are widely common throughout the app
 */
public class Dialogs {

    /**
     * A yes/no/cancel dialog asking the user if they want to saves the item
     *
     * @param parent The component to display the dialog over
     * @param item User friendly text that describes the item in question
     * @return JOptionPane.YES_OPTION, JOptionPane.NO_OPTION, or JOptionPane.CANCEL_OPTION,
     */
    public static int dirty( Component parent, String item ) {

        return JOptionPane.showConfirmDialog(
                parent,
                "Would you like to save " + item + " first?",
                "You forgot to save",
                JOptionPane.YES_NO_CANCEL_OPTION
        );
    }
}
