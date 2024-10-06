import yaml
import streamlit as st
import requests
from urllib.parse import urlencode
import webbrowser
from auth0.authentication import Database
from auth0.authentication import GetToken


# Load config file
with open('config.yaml', 'r', encoding='utf-8') as file:
    config = yaml.load(file, Loader=yaml.SafeLoader)

# Auth0 Configuration
AUTH0_DOMAIN = config['auth0']['domain']
CLIENT_ID = config['auth0']['client_id']
CLIENT_SECRET = config['auth0']['client_secret']
REDIRECT_URI = config['auth0']['redirect_uri']
AUTH0_CALLBACK_URL = f"{REDIRECT_URI}?code={{code}}&state={{state}}"
AUTH0_LOGOUT_URL = f"https://{AUTH0_DOMAIN}/v2/logout"

st.set_page_config(
    page_title="Utilitector",
    page_icon="",
)

if 'login' not in st.session_state:
    st.session_state["login"] = False

if 'register' not in st.session_state:
    # add it to the session state
    st.session_state["register"] = True


# Register button

# if st.button('Register'):
#     st.session_state["register"] = True
#     st.session_state["login"] = False

if st.session_state["login"]:
    st.write("## Login")
    st.session_state["register"] = False
    login = st.text_input('Email' , key='Login_Email')
    password = st.text_input('Password', type='password', key='Login_Password')
    if st.button('Continue'):
        # Authenticate user
        token = GetToken(AUTH0_DOMAIN, CLIENT_ID, CLIENT_SECRET)
        token.login(username=login, password=password, realm='Username-Password-Authentication')
        st.success('User authenticated successfully')
    st.write("Don't Have An Account?")


    if st.button('Register'):
        st.session_state["login"] = False
        st.session_state["register"] = True


if st.session_state["register"]:
    st.write("## Register")
    st.session_state["login"] = False
    username = st.text_input('Email', key='register_email')
    pass_word = st.text_input('Password', type='password')
    if st.button('Continue', key='register_continue'):
        # Register user
        token = Database(AUTH0_DOMAIN, CLIENT_ID, CLIENT_SECRET)
        token.signup(email=username, password=pass_word, connection='Username-Password-Authentication')
        st.success('User registered successfully')

    st.write("Already Have An Account?")


    if st.button('Login'):
        st.session_state["login"] = True
        st.session_state["register"] = False
