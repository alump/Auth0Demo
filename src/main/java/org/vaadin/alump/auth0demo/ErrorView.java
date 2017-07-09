package org.vaadin.alump.auth0demo;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;

@SpringComponent
@UIScope
public class ErrorView extends VerticalLayout implements View {

    public ErrorView() {

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // Just redirect to main view for now
        event.getNavigator().navigateTo(MainView.VIEW_NAME);
    }
}
