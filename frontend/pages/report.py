import streamlit as st

st.image('logo.png')

st.metric('Version', '0.0.1')


name = st.text_input("Name")

address = st.text_input("Address of Issue")

issue = st.text_input("What is your issue?")

if st.button('Confirm'):
    
    if not name or not address or not issue:
        st.error("Please fill out all fields.")
    else:
        st.switch_page('home.py')