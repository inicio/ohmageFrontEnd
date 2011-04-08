package edu.ucla.cens.mobilize.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


public class LoginViewImpl extends Composite implements LoginView {
    @UiTemplate("LoginView.ui.xml")
    interface LoginBoxViewUiBinder extends UiBinder<Widget, LoginViewImpl> {}
    private static LoginBoxViewUiBinder uiBinder =
      GWT.create(LoginBoxViewUiBinder.class);
    
    // Fields defined in the ui XML
    @UiField HTML msgDiv;
    @UiField TextBox userNameTextBox;
    @UiField PasswordTextBox passwordTextBox;
    @UiField Button loginButton;
    
    private Presenter presenter;
    
    public LoginViewImpl() {
        initWidget(uiBinder.createAndBindUi(this)); 
        
        // msg is only shown when there's a login failure
        //msgDiv.setVisible(false);
        initEventHandlers();
    }
    
    private void initEventHandlers() {
      // clicking login button submits (note: pressing enter
      //  while login has focus also submits)
      loginButton.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          submit();
        }
      });
      
      // pressing enter in user name box submits
      userNameTextBox.addKeyPressHandler(new KeyPressHandler() {
        @Override
        public void onKeyPress(KeyPressEvent event) {
          clearMessage(); // hide error message, if any
          if (event.getCharCode() == KeyCodes.KEY_ENTER) {
            submit();
          }
        }
      });
      
      // pressing enter in password box submits
      passwordTextBox.addKeyPressHandler(new KeyPressHandler() {
        @Override
        public void onKeyPress(KeyPressEvent event) {
          clearMessage(); // hide error message, if any
          if (event.getCharCode() == KeyCodes.KEY_ENTER) {
            submit();
          }
        }
      });
    }
   
    // verifies that both name and password are set then notifies presenter
    private void submit() {
      this.presenter.onSubmit();
    }
    
    /**
     * Sets the views presenter to call to handle events.
     * 
     * @param presenter The presenter to bind to.
     */
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setLoginFailed(String msg) {
      msgDiv.setHTML(msg);
      msgDiv.setVisible(true);
    }
    
    public void clearMessage() {
      msgDiv.setHTML("");
      msgDiv.setVisible(false);
    }

    @Override
    public String getUserName() {
      return userNameTextBox.getText();
    }

    @Override
    public String getPassword() {
      return passwordTextBox.getText();
    }

}
