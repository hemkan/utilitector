import streamlit as st

st.image('logo.png')

st.metric('Version', '0.4.1')


name = st.text_input("Name")


st.write("The current name is", name)