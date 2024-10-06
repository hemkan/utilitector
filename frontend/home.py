import yaml
import streamlit as st
import requests
from urllib.parse import urlencode
import webbrowser
# from auth0.authentication import Database

# Load config file
with open('config.yaml', 'r', encoding='utf-8') as file:
    config = yaml.load(file, Loader=yaml.SafeLoader)

st.set_page_config(
    page_title="Utilitector",
    page_icon="",
)

# if 'login' not in st.session_state:
#     st.session_state["login"] = False

# if 'register' not in st.session_state:
#     st.session_state["register"] = False

st.write("## Utilitector: Empower Your Community!")
# Empower = '<p style="font-family:sans-serif; color:White; font-size: 56px;">Empower Your Community!</p>'
# st.markdown(Empower, unsafe_allow_html=True)
st.write("At Utilitector, we believe in the power of community-driven solutions. Our platform provides essential tools and resources that enable individuals to report issues, share insights, and collaborate on improvements within their communities. Whether it’s environmental concerns, public safety, or local initiatives, Utilitector streamlines the reporting process, ensuring that every voice is heard and every issue is addressed.")

# File a Report button
if st.button('File a Report'):
    st.switch_page('pages/report.py')

st.write('___')
st.write("## How it works")

st.write("1. **File a Report**: Report issues in your communuity, such as flooding, potholes, or broken streetlights.")
st.write("2. **Dispatcher**: The dispatcher receives and processes the report to dispatch the appropriate team for resolution.")
st.write("3. **Resolution**: The team resolves the issue and updates the status of the report.")

st.write('___')
# for companies
st.write("## For Companies")
st.write("Utilitector provides a platform for companies to receive and manage reports from the community. Companies can view, process, and resolve issues reported by users, ensuring that the community's needs are addressed in a timely manner.")

if st.button('Get Started'):
    st.switch_page('pages/auth.py')


# # Login button
# if st.button('Login'):
#     st.session_state["login"] = True
#     st.session_state["register"] = False
    
# if st.button('Register'):
#     st.session_state["register"] = True
#     st.session_state["login"] = False
        
# if st.session_state["login"]:
#     username = st.text_input('Username')
#     password = st.text_input('Password', type='password')
#     if st.button('Continue'):
#         # Authenticate user
#         token = GetToken(AUTH0_DOMAIN, CLIENT_ID, CLIENT_SECRET)
#         token.login(username=username, password=password, realm='Username-Password-Authentication')
#         st.success('User authenticated successfully')

# if st.session_state["register"]:
#     username = st.text_input('Username')
#     password = st.text_input('Password', type='password')
#     if st.button('Continue'):
#         # Register user
#         token = GetToken(AUTH0_DOMAIN, CLIENT_ID, CLIENT_SECRET)
#         token.signup(username=username, password=password, connection='Username-Password-Authentication')
#         st.success('User registered successfully')
