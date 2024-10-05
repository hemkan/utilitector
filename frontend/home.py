import yaml
import streamlit as st
from yaml.loader import SafeLoader
import streamlit_authenticator as stauth
from streamlit_authenticator.utilities import (CredentialsError,
                                               ForgotError,
                                               Hasher,
                                               LoginError,
                                               RegisterError,
                                               ResetError,
                                               UpdateError)

# Load config file
with open('config.yaml', 'r', encoding='utf-8') as file:
    config = yaml.load(file, Loader=SafeLoader)

st.set_page_config(
    page_title="Utilitector",
    page_icon="",
)

st.write("# Welcome to Utilitector!")
# st.navigation(postion='hidden')

# Create an image that is the tagline (Only way to change font size)
Empower = '<p style="font-family:sans-serif; color:White; font-size: 56px;">Empower Your Community!</p>'
st.markdown(Empower, unsafe_allow_html=True)

# Initialize states to track which widget shows up
if 'show_login' not in st.session_state:
    st.session_state.show_login = False
if 'show_register' not in st.session_state:
    st.session_state.show_register = False

if st.button('File a Report'):
    st.switch_page('pages/report.py')

# Login and register buttons
if st.button('Login'):
    st.session_state.show_login = True
    st.session_state.show_register = False

if st.button('Register'):
    st.session_state.show_register = True
    st.session_state.show_login = False

# Create the authenticator object
authenticator = stauth.Authenticate(
    config['credentials'],
    config['cookie']['name'],
    config['cookie']['key'],
    config['cookie']['expiry_days']
)

# Login form
if st.session_state.show_login:
    # Login widget
    try:
        authenticator.experimental_guest_login('Login with Google',
                                            provider='google',
                                            oauth2=config['oauth2'])
        # authenticator.experimental_guest_login('Login with Microsoft',
        #                                     provider='microsoft',
        #                                     oauth2=config['oauth2'])
    except LoginError as e:
        st.error(e)

    # TODO: Setup dashboard after login
    if st.session_state["authentication_status"]:
        st.switch_page('pages/dashboard.py')
        # st.write('___')
        # authenticator.logout()
        # st.write(f'Welcome *{st.session_state["name"]}*')
        # st.title('Some content')    
        # st.write('___')
    elif st.session_state["authentication_status"] is False:
        st.error('Username/password is incorrect')

# Register form
if st.session_state.show_register:
    # registration widget
    try:
        (email_of_registered_user,
         username_of_registered_user,
         name_of_registered_user) = authenticator.register_user()
        if email_of_registered_user:
            st.success('User registered successfully')
    except RegisterError as e:
        st.error(e)