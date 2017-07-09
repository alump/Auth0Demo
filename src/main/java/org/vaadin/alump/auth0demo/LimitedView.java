package org.vaadin.alump.auth0demo;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import javax.annotation.PostConstruct;

@SpringView(name = LimitedView.VIEW_NAME)
@SpringComponent(LimitedView.VIEW_NAME)
public class LimitedView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "limited";

    public LimitedView() {
        super();
    }

    @PostConstruct
    protected void init() {
        Button backToMain = new Button("Back to main view", e -> getUI().getNavigator().navigateTo(MainView.VIEW_NAME));

        Label label = new Label("Access Granted!");
        label.addStyleName(ValoTheme.LABEL_H1);


        addComponents(backToMain, label);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
