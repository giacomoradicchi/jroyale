package jroyale.view.home_view;

import jroyale.view.IView;

public interface IHomeView extends IView {
    
    // static methods
    public static IHomeView getInstance() {
        return HomeView.getInstance();
    }
}
