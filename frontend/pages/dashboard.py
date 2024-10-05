import streamlit as st

st.write("# Welcome to Utilitector!")
# check auth
if st.session_state["authentication_status"]:
    st.write('___')
    authenticator.logout()
    st.write(f'Welcome *{st.session_state["name"]}*')
    st.title('Some content')    
    st.write('___')
elif st.session_state["authentication_status"] is False:
    st.switch_page('home.py')