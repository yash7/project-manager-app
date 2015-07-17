package ateamcomp354.projectmanagerapp.ui.util;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.function.Function;

/**
 * Renders a list cell as 2 columns of text, one left aligned, the other right aligned.
 * Use the constructor and java8 lambdas to describe what about an element E to display
 * in the first and second column.
 */
public class TwoColumnListCellRenderer<E> implements ListCellRenderer<E> {

    private final Function<E, String> col1Mapper;
    private final Function<E, String> col2Mapper;

    private final JPanel component;
    private final JLabel col1Lbl;
    private final JLabel col2Lbl;

    public TwoColumnListCellRenderer( Function<E, String> col1Mapper, Function<E, String> col2Mapper ) {

        this.col1Mapper = Objects.requireNonNull( col1Mapper, "col1Mapper argument is null" );
        this.col2Mapper = Objects.requireNonNull( col2Mapper, "col2Mapper argument is null" );

        component = new JPanel( new BorderLayout() );
        col1Lbl = new JLabel();
        col2Lbl = new JLabel();
        component.add(col1Lbl, BorderLayout.WEST );
        component.add(col2Lbl, BorderLayout.EAST );
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends E> list,
            E value,
            int index,
            boolean isSelected,
            boolean cellHasFocus
    ) {

        String col1Txt = col1Mapper.apply( value );
        String col2Txt = col2Mapper.apply( value );

        Color fg = isSelected ? list.getSelectionForeground() : list.getForeground();
        Color bg = isSelected ? list.getSelectionBackground() : list.getBackground();
        boolean enabled = list.isEnabled();
        Font font = list.getFont();

        prepLabel( col1Lbl, col1Txt, font, enabled, fg, bg );
        prepLabel( col2Lbl, col2Txt, font, enabled, fg, bg );
        prepComp( component, enabled, fg, bg );

        return component;
    }

    private void prepLabel( JLabel lbl, String txt, Font f, boolean enabled, Color fg, Color bg ) {
        lbl.setText( txt );
        lbl.setFont( f );
        prepComp( lbl, enabled, fg, bg );
    }

    private void prepComp( JComponent c, boolean enabled, Color fg, Color bg ) {
        c.setEnabled( enabled );
        c.setForeground( fg );
        c.setBackground( bg );
    }
}
