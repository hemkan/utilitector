import json
import yaml
import streamlit as st
import requests
import hashlib
from urllib.parse import urlencode
import webbrowser
import base64
from streamlit_cookies_controller import CookieController

controller = CookieController()

# Load config file
with open('config.yaml', 'r', encoding='utf-8') as file:
    config = yaml.load(file, Loader=yaml.SafeLoader)

st.set_page_config(
    page_title="Utilitector",
    page_icon="",
)

if 'login' not in st.session_state:
    st.session_state["login"] = False

if 'register' not in st.session_state:
    # add it to the session state
    st.session_state["register"] = True

st.session_state.storedid = controller.get("storedid")
if st.session_state.storedid == None:
    st.session_state.storedid = 0

st.session_state.storedtoken = controller.get("storedtoken")
if st.session_state.storedtoken == None:
    st.session_state.storedtoken = ""

def savecredentials(user, token):
    controller.set("storedid", user)
    controller.set("storedtoken", token)

if st.session_state["register"]:
    st.write("## Register")

    username = st.text_input('Username')
    password = st.text_input('Password', type='password')
    hasher = hashlib.sha256()
    hasher.update(password.encode())
    passwordHash = hasher.digest()
    passwordHash = base64.b64encode(passwordHash).decode('ascii')
    register = {
        "username": username,
        "passwordHash": passwordHash
    }
    register = json.dumps(register)
    if st.button('Continue'):
        # Authenticate user
        response = requests.post("http://localhost:8080/api/auth/register", data=register, headers={
            "Content-Type": "application/json"
        })
        if not response.ok:
            st.error("Error attempting to register user. Username is likely taken.")
        else:
            st.success("User created!")
            savecredentials(response.json()["id"], response.json()["token"])
    
    st.write("Already Have An Account?")
    # link to login page
    


    if st.button('Login'):
        st.session_state["login"] = True
        st.session_state["register"] = False
        st.rerun()
    
#     if st.button('Login Instead'):
#         st.session_state["login"] = True
#         st.session_state["register"] = False
        


if st.session_state["login"]:
    st.write("## Login")

    username = st.text_input('Username')
    password = st.text_input('Password', type='password')
    if st.button('Continue'):
        response = requests.post("http://localhost:8080/api/auth/login", data=register, headers={
            "Content-Type": "application/json"
        })
        if not response.ok:
            st.error("Login invalid. Check your password or make a new account.")
        else:
            st.success("Login successful!")
            savecredentials(response.json()["id"], response.json()["token"])
    st.write("Don't Have An Account?")
    # link to register page
    

    if st.button('Register'):
        st.session_state["register"] = True
        st.session_state["login"] = False
        st.rerun()

